package com.devy.core_ui.utils

import com.devy.core_ui.navigation.DummyDestination.route

/**
 * Created by JeYHoon on 2025. 10. 5..
 */

fun createRoute(path: String, vararg params: String) = buildString {
    append(path)
    append("?")
    params.forEachIndexed { index, string ->
        append("$string={$string}")
        if (index != params.lastIndex) {
            append("&")
        }
    }
}

fun String.createRoute(params: Map<String, Any>): String {
    return params.entries.fold(this) { route, (key, value) ->
        route.replace("{$key}", value.toString())
    }
}