package com.devy.network.retrofit2.response

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

sealed class ApiResponse<out T> {
    data class Success<T>(val data: T) : ApiResponse<T>()

    sealed class Failure(val message: String?) : ApiResponse<Nothing>() {
        data class HttpError(
            val statusCode: Int,
            val statusMessage: String?,
            val error: ErrorDto?
        ): Failure(error?.message ?: statusMessage)

        data class NetworkError(val throwable: Throwable): Failure(throwable.localizedMessage)

        data class Exception(val throwable: Throwable): Failure(throwable.localizedMessage)

        data class EmptyResponse(val throwable: Throwable): Failure(throwable.localizedMessage)

        companion object {
            const val ERROR_CODE_EMPTY_RESPONSE = -4444
        }
    }

    val messageOrNull: String?
        get() = if(this is Failure) message else null

    fun <R: Any> map(transform: (T) -> R): ApiResponse<R> = when (this) {
        is Success -> Success(transform(data))
        is Failure.HttpError -> Failure.HttpError(statusCode, statusMessage, error)
        is Failure.NetworkError -> Failure.NetworkError(throwable)
        is Failure.Exception -> Failure.Exception(throwable)
        is Failure.EmptyResponse -> Failure.EmptyResponse(throwable)
    }
}

fun <T> ApiResponse<T>.commonErrorCode(): Int? =
    (this as? ApiResponse.Failure.HttpError)?.error?.code

fun <T> ApiResponse<T>.httpStatusCode(): Int? =
    (this as? ApiResponse.Failure.HttpError)?.statusCode