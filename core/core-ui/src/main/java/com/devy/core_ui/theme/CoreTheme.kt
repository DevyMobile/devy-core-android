package com.devy.core_ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.devy.core_ui.theme.CoreTypography.Companion.createCoreTypography

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

val dColorsScheme = DColors(
    primary = Color(0xFF8821CF),
    surface = Color(0xFF2C2C3E),
)

val coreTypography = createCoreTypography()

object CoreTheme {
    val colors: DColors
        @Composable
        @ReadOnlyComposable
        get() = dColorsScheme

    val typo: CoreTypography
        @Composable
        @ReadOnlyComposable
        get() = coreTypography
}