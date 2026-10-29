package com.devy.core_ui.component.local

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalDSnackbarColor = staticCompositionLocalOf { DSnackbarColor() }

data class DSnackbarColor(
    val container: Color = Color.Unspecified,
    val text: Color = Color.Unspecified,
    val border: Color = Color.Unspecified,
)