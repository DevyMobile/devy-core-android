package com.devy.core_ui.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devy.core_ui.theme.CoreTheme

/**
 * Created by JeYHoon on 2025. 10. 3..
 */
data class DChipStyle(
    val selectedColor: Color,
    val unSelectedColor: Color,
    val selectedTextStyle: TextStyle,
    val unSelectedTextStyle: TextStyle,
    val roundedCorner: Dp,
    val selectType: DChipSelectType,
)

object DChipDefaults {

    @Composable
    fun style(
        selectedColor: Color = CoreTheme.colors.onSurface0,
        unSelectedColor: Color = CoreTheme.colors.onSurface60,
        selectedTextStyle: TextStyle = CoreTheme.typo.titleB,
        unSelectedTextStyle: TextStyle = CoreTheme.typo.title,
        roundedCorner: Dp = 20.dp,
        selectType: DChipSelectType = DChipSelectType.Solid
    ): DChipStyle = DChipStyle(
        selectedColor = selectedColor,
        unSelectedColor = unSelectedColor,
        selectedTextStyle = selectedTextStyle,
        unSelectedTextStyle = unSelectedTextStyle,
        roundedCorner = roundedCorner,
        selectType = selectType,
    )
}

enum class DChipSelectType {
    Stroke, Solid
}