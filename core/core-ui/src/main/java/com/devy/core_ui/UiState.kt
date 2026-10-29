package com.devy.core_ui

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

sealed class UiState<out T> {
    data object Idle: UiState<Nothing>()
    data object Loading: UiState<Nothing>()
    data object Empty: UiState<Nothing>()
    data class Progress(val percent: Float, val message: String? = null): UiState<Nothing>()
    data class Success<T>(val data: T): UiState<T>()
    data class Error(
        val message: String?,
        val code: Int? = null,
        val isNetworkError: Boolean = false,
    ): UiState<Nothing>()
}