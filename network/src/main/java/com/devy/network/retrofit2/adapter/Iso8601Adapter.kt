package com.devy.network.retrofit2.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Created by JeYHoon on 2025. 10. 25..
 */

class Iso8601ToLocalDateTimeAdapter {
    @FromJson
    fun fromJson(iso8601Date: String): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.parse(iso8601Date), ZoneId.systemDefault())
    }

    @ToJson
    fun toJson(localDateTime: LocalDateTime): String {
        return localDateTime.toString()
    }
}