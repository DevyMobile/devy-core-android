package com.devy.global.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by JeYHoon on 2025. 10. 3..
 */

fun Long?.toDateString(): String = SimpleDateFormat("yy.MM.dd HH:mm:ss", Locale.getDefault()).format(Date(this ?: System.currentTimeMillis()))