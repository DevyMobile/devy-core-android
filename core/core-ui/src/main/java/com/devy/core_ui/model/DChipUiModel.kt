package com.devy.core_ui.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Created by JeYHoon on 2025. 10. 10..
 */
data class DChipUiModel(
    val title: String,
    val icon: Painter? = null,
    val color: Color,
    val textColor: Color,
    val style: TextStyle,
    val roundedCorner: Dp = 20.dp,
    val selectType: DChipSelectType,
)
