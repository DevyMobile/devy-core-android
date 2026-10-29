package com.devy.core_ui.component.local

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalDCommonColor = staticCompositionLocalOf { DCommonColor() }

data class DCommonColor(
    val primary: Color = Color.Unspecified,
    val background: Color = Color.Unspecified,
    val border: Color = Color.Unspecified,
    val surface: Color = Color.Unspecified,
    val onSurface: DCommonOnSurfaceColor = DCommonOnSurfaceColor(),
)

data class DCommonOnSurfaceColor(
    val primary: Color = Color.Unspecified,
    val secondary: Color = Color.Unspecified,
)