package com.devy.network.retrofit2.response

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

inline fun <T> ApiResponse<T>.onSuccess(
    crossinline onResult: ApiResponse.Success<T>.() -> Unit
): ApiResponse<T> {
    if (this is ApiResponse.Success) {
        onResult(this)
    }
    return this
}

suspend inline fun <T> ApiResponse<T>.suspendOnSuccess(
    crossinline onResult: suspend ApiResponse.Success<T>.() -> Unit,
): ApiResponse<T> {
    if (this is ApiResponse.Success) {
        onResult(this)
    }
    return this
}

inline fun <T> ApiResponse<T>.onHttpError(
    crossinline onResult: ApiResponse.Failure.HttpError.() -> Unit,
): ApiResponse<T> {
    if (this is ApiResponse.Failure.HttpError) {
        onResult(this)
    }
    return this
}

suspend inline fun <T> ApiResponse<T>.suspendOnHttpError(
    crossinline onResult: suspend ApiResponse.Failure.HttpError.() -> Unit,
): ApiResponse<T> {
    if (this is ApiResponse.Failure.HttpError) {
        onResult(this)
    }
    return this
}

inline fun <T> ApiResponse<T>.onFailure(
    crossinline onResult: ApiResponse.Failure.() -> Unit,
): ApiResponse<T> {
    if (this is ApiResponse.Failure) {
        onResult(this)
    }
    return this
}

suspend inline fun <T> ApiResponse<T>.suspendOnFailure(
    crossinline onResult: suspend ApiResponse.Failure.() -> Unit,
): ApiResponse<T> {
    if (this is ApiResponse.Failure) {
        onResult(this)
    }
    return this
}