package com.devy.core_ui.utils

import java.text.DecimalFormat

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

fun Int.toFormatString(): String {
    val formatter = DecimalFormat("#,###")
    return formatter.format(this)
}