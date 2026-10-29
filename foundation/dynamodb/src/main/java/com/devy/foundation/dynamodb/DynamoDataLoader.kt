package com.devy.foundation.dynamodb

import com.devy.global.DLog
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by JeYHoon on 2025. 10. 18..
 */
interface DynamoDataLoader {
    val showProgress: StateFlow<Boolean>

    fun <T> CoroutineScope.loadData(
        call: suspend () -> Result<T>,
        onSuccess: suspend (result: T) -> Unit = {},
        onError: suspend (error: Result<T>) -> Unit = {},
        onNetworkError: suspend () -> Unit = {},
        onComplete: suspend () -> Unit = {},
        useProgress: Boolean = true
    )

    fun setShowProgress(value: Boolean)
}

class DynamoDataLoaderImpl : DynamoDataLoader {
    private val _showProgress = MutableStateFlow(false)
    override val showProgress = _showProgress.asStateFlow()

    override fun <T> CoroutineScope.loadData(
        call: suspend () -> Result<T>,
        onSuccess: suspend (T) -> Unit,
        onError: suspend (Result<T>) -> Unit,
        onNetworkError: suspend () -> Unit,
        onComplete: suspend () -> Unit,
        useProgress: Boolean
    ) {
        launch {
            if (useProgress) _showProgress.value = true
            val result = withContext(Dispatchers.IO) { call() }
            val logMessage = when (val data = result.getOrNull()) {
                null -> {
                    "null"
                }

                is List<*> -> {
                    Gson().toJson(data).toString()
                }

                else -> {
                    data.toString()
                }
            }
            DLog.f("DynamoDB", logMessage)
            if (useProgress) _showProgress.value = false
            onComplete()
            when {
                result.isSuccess -> {
                    val data = result.getOrNull()
                    if (data != null) {
                        onSuccess(data)
                    } else {
                        onError(result)
                    }
                }

                result.isFailure -> {
                    onError(result)
                }

                else -> onError(result)
            }
        }
    }

    override fun setShowProgress(value: Boolean) {
        _showProgress.value = value
    }
}