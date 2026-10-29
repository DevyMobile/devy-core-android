package com.devy.core_ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.devy.core_ui.component.local.LocalDCommonColor
import com.devy.core_ui.theme.CoreTheme

@Composable
fun BoxScope.DHorizontalGradient(
    modifier: Modifier,
    backgroundColor: Color? = null,
    reverse: Boolean = false,
) {
    val providedBackground = LocalDCommonColor.current.background

    val background = when {
        backgroundColor != null -> backgroundColor
        providedBackground != Color.Unspecified -> providedBackground
        else -> CoreTheme.colors.background
    }

    val colors = if (reverse) {
        listOf(
            background,
            CoreTheme.colors.tr
        )
    } else {
        listOf(
            CoreTheme.colors.tr,
            background
        )
    }

    Box(
        modifier = modifier
            .align(
                if (reverse) {
                    Alignment.CenterStart
                } else {
                    Alignment.CenterEnd
                }
            )
            .background(
                Brush.linearGradient(
                    colors = colors,
                    start = Offset.Zero, // 왼쪽 중앙
                    end = Offset(Float.POSITIVE_INFINITY, 0f) // 오른쪽 중앙
                )
            )
    )
}