package com.devy.core_ui.component.local

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


val LocalDToolBarColor = staticCompositionLocalOf { DToolBarColor() }

data class DToolBarColor(
    val primary: Color = Color.Unspecified,
    val background: Color = Color.Unspecified,
    val border: Color = Color.Unspecified,
)