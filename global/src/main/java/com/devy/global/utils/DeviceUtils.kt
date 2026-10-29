package com.devy.global.utils

import android.content.Context
import android.os.PowerManager

/**
 * Created by JeYHoon on 2025. 10. 3..
 */

fun isBatteryOptimizationEnabled(context: Context): Boolean {
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    val packageName = context.packageName
    return !powerManager.isIgnoringBatteryOptimizations(packageName)
}