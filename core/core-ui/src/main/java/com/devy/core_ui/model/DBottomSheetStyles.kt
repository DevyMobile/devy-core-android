package com.devy.core_ui.model

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.devy.core_ui.theme.CoreTheme

/**
 * Created by JeYHoon on 2025. 10. 2..
 */
data class DBottomSheetStyle(
    val backgroundColor: Color,
    val titleStyle: TextStyle,
    val titleColor: Color,
) {
    companion object {
        @SuppressLint("ComposableNaming")
        @Composable
        fun Default() = DBottomSheetStyle(
            backgroundColor = CoreTheme.colors.background,
            titleStyle = CoreTheme.typo.titleB,
            titleColor = CoreTheme.colors.onSurface0,
        )
    }
}

data class DBottomSheetListTextStyle(
    val color: Color,
    val style: TextStyle,
) {
}