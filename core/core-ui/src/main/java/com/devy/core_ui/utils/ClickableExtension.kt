package com.devy.core_ui.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Modifier.noRippleCombinedClickable(
    enabled: Boolean = true,
    onLongClick: (() -> Unit)?,
    onClick: () -> Unit
) = this.combinedClickable(
    enabled = enabled,
    onClick = onClick,
    onLongClick = onLongClick,
    indication = null,
    interactionSource = remember { MutableInteractionSource() }
)

fun Modifier.noRippleClickable(
    enabled: Boolean = true,
    onClick: () -> Unit,
) = composed {
    this then Modifier.clickable(
        enabled = enabled,
        onClick = onClick,
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    )
}

fun Modifier.singleClickable(
    enabled: Boolean = true,
    onClick: () -> Unit
) = composed {
    val multipleEventsCutter = remember { MultipleEventsCutter() }
    this then Modifier.clickable(
        enabled = enabled,
        onClick = { multipleEventsCutter.processEvent { onClick() } },
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    )
}

class MultipleEventsCutter(
    private val throttleDuration: kotlin.time.Duration = 300.milliseconds
) {
    private var lastEventTime: TimeMark? = null
    fun processEvent(event: () -> Unit) {
        val elapsed = lastEventTime?.elapsedNow()
        if (elapsed == null || elapsed >= throttleDuration) {
            event.invoke()
            lastEventTime = TimeSource.Monotonic.markNow()
        }
    }

    suspend fun processSuspendEvent(event: suspend () -> Unit) {
        val elapsed = lastEventTime?.elapsedNow()
        if (elapsed == null || elapsed >= throttleDuration) {
            event.invoke()
            lastEventTime = TimeSource.Monotonic.markNow()
        }
    }
}

fun <T> Flow<T>.throttleFirst(): Flow<T> {
    return flow {
        val multipleEventsCutter = MultipleEventsCutter()
        collect { value ->
            multipleEventsCutter.processSuspendEvent {
                emit(value)
            }
        }
    }
}