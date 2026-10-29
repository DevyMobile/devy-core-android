package com.devy.core_ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DGradientBox(
    modifier: Modifier = Modifier,
    gradientHeight: Dp = BOTTOM_GRADIENT_WITH_BUTTON_HEIGHT.dp,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        GradientOverlay(
            modifier = Modifier
                .fillMaxWidth()
                .height(gradientHeight),
            type = GradientType.Bottom
        )
        content()
    }
}