package com.devy.network.retrofit2.response

import com.squareup.moshi.JsonClass

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

@JsonClass(generateAdapter = true)
data class ErrorDto(
    val error: Error?,
    val detail: Map<String, Any>?
) {
    val code: Int? get() = error?.code?.toInt()
    val message: String? get() = error?.message
}

@JsonClass(generateAdapter = true)
data class Error(
    val message: String?,
    val code: String?
)