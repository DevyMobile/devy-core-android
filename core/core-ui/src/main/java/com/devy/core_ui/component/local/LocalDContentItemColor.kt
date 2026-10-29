package com.devy.core_ui.component.local

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalDContentItemColor = staticCompositionLocalOf { DContentItemColor() }

data class DContentItemColor(
    val background: Color = Color.Unspecified,
    val border: Color = Color.Unspecified,
    val surface: Color = Color.Unspecified,
)