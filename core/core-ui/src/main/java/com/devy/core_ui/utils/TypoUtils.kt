package com.devy.core_ui.utils

import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

fun getConfiguration(): Configuration {
    return Resources.getSystem().configuration
}

fun getDensity(): Float {
    return getConfiguration().densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT
}

fun getFontScale(): Float {
    return getConfiguration().fontScale
}

fun getScreenWidth(): Dp {
    return getConfiguration().screenWidthDp.dp
}

fun getScreenHeight(): Dp {
    return getConfiguration().screenHeightDp.dp
}

fun Dp.toPx(): Float {
    return this.value * getDensity()
}

fun Int.toDp(): Dp {
    return (this / getDensity()).dp
}

fun Float.toDp(): Dp {
    return (this / getDensity()).dp
}

fun TextUnit.toPx(): Float {
    val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity
    return this.value * scaledDensity
}

fun dpToSp(dp: Dp): TextUnit {
    return (dp.value / getFontScale()).sp
}