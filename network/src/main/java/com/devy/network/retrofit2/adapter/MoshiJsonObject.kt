package com.devy.network.retrofit2.adapter

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

typealias MoshiJsonObject = Map<String, Any>
typealias MoshiJsonString = String

inline fun <reified T> T.toMoshiJsonString(
    moshi: Moshi = Moshi.Builder().build()
): MoshiJsonString {
    val jsonAdapter = moshi.adapter(T::class.java)
    return jsonAdapter.toJson(this)
}

fun MoshiJsonString.toMoshiJsonObject(
    moshi: Moshi = Moshi.Builder().build()
): MoshiJsonObject {
    val type = Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java)
    val mapAdapter = moshi.adapter<Map<String, Any>>(type)
    return mapAdapter.fromJson(this) ?: emptyMap() // JSON 문자열을 Map으로 변환
}

inline fun <reified T> T.toMoshiJsonObject(moshi: Moshi = Moshi.Builder().build()) = this
    .toMoshiJsonString(moshi) // 객체를 JSON 문자열로 변환
    .toMoshiJsonObject(moshi) // JSON 문자열을 Map으로 변환
