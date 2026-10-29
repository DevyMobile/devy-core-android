package com.devy.network

import com.devy.network.retrofit2.response.ApiResponse
import com.devy.network.retrofit2.response.Error
import com.devy.network.retrofit2.response.ErrorDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by JeYHoon on 2025. 10. 2..
 */
interface DataLoader {
    val showProgress: StateFlow<Boolean>

    fun <T> CoroutineScope.loadData(
        call: suspend () -> ApiResponse<T>,
        onSuccess: suspend (result: T) -> Unit = {},
        onError: suspend (error: ErrorDto) -> Unit = {},
        onNetworkError: suspend () -> Unit = {},
        onComplete: suspend () -> Unit = {},
        useProgress: Boolean = true
    )

    fun setShowProgress(value: Boolean)
}

class DataLoaderImpl : DataLoader {
    private val _showProgress = MutableStateFlow(false)
    override val showProgress = _showProgress.asStateFlow()

    override fun <T> CoroutineScope.loadData(
        call: suspend () -> ApiResponse<T>,
        onSuccess: suspend (result: T) -> Unit,
        onError: suspend (error: ErrorDto) -> Unit,
        onNetworkError: suspend () -> Unit,
        onComplete: suspend () -> Unit,
        useProgress: Boolean
    ) {
        launch {
            if (useProgress) _showProgress.value = true
            val result = withContext(Dispatchers.IO) { call() }
            if (useProgress) _showProgress.value = false
            onComplete()
            when (result) {
                is ApiResponse.Success -> onSuccess(result.data)
                is ApiResponse.Failure.HttpError -> onError(
                    result.error ?: ErrorDto(
                        Error(
                            message = result.statusMessage,
                            code = result.statusCode.toString()
                        ),
                        null
                    )
                )
                is ApiResponse.Failure.NetworkError -> onNetworkError()
                is ApiResponse.Failure.EmptyResponse -> onError(
                    ErrorDto(
                        Error(
                            message = result.messageOrNull,
                            code = ApiResponse.Failure.ERROR_CODE_EMPTY_RESPONSE.toString()
                        ),
                        null
                    )
                )
                else -> onError(
                    ErrorDto(
                        Error(
                            message = result.messageOrNull,
                            code = null
                        ),
                        null
                    )
                )
            }
        }
    }

    override fun setShowProgress(value: Boolean) {
        _showProgress.value = value
    }
}

class AsyncDataLoaderImpl: DataLoader {
    private val _showProgress = MutableStateFlow(false)
    override val showProgress = _showProgress.asStateFlow()

    override fun <T> CoroutineScope.loadData(
        call: suspend () -> ApiResponse<T>,
        onSuccess: suspend (result: T) -> Unit,
        onError: suspend (error: ErrorDto) -> Unit,
        onNetworkError: suspend () -> Unit,
        onComplete: suspend () -> Unit,
        useProgress: Boolean
    ) {
        launch(Dispatchers.IO) {
            if (useProgress) _showProgress.value = true
            val result = call()
            if (useProgress) _showProgress.value = false
            onComplete()
            when (result) {
                is ApiResponse.Success -> onSuccess(result.data)
                is ApiResponse.Failure.HttpError -> onError(
                    result.error ?: ErrorDto(
                        Error(
                            message = result.statusMessage,
                            code = result.statusCode.toString()
                        ),
                        null
                    )
                )
                is ApiResponse.Failure.NetworkError -> onNetworkError()
                else -> onError(
                    ErrorDto(
                        Error(
                            message = result.messageOrNull,
                            code = null
                        ),
                        null
                    )
                )
            }
        }
    }

    override fun setShowProgress(value: Boolean) {
        _showProgress.value = value
    }
}