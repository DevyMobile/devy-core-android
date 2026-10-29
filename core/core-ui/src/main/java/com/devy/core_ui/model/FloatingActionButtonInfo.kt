package com.devy.core_ui.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Created by JeYHoon on 2025. 10. 2..
 */
data class FloatingActionButtonInfo(
    @DrawableRes val iconResId: Int,
    val size: Dp = 60.dp,
    val iconSize: Dp = 24.dp,
    val iconColor: Color = Color.Unspecified,
    val backgroundColor: Color = Color.Unspecified,
    val onClick: () -> Unit,
)

