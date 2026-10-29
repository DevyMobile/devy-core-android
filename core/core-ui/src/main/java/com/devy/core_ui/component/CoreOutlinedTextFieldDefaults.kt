package com.devy.core_ui.component

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.devy.core_ui.theme.CoreTheme

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

object CoreOutlinedTextFieldDefaults {

    @Composable
    fun colors(
        containerColor: Color = CoreTheme.colors.background,
        borderColor: Color = CoreTheme.colors.border,
    ): TextFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedBorderColor = borderColor,
        focusedBorderColor = borderColor,
        disabledBorderColor = borderColor,
        focusedTextColor = CoreTheme.colors.onSurface0,
        disabledTextColor = CoreTheme.colors.onSurface0,
        unfocusedTextColor = CoreTheme.colors.onSurface0,
        focusedContainerColor = containerColor,
        disabledContainerColor = containerColor,
        unfocusedContainerColor = containerColor,
    )
}