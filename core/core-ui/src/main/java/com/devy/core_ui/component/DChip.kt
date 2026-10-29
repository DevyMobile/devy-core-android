package com.devy.core_ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devy.core_ui.model.DChipSelectType
import com.devy.core_ui.model.DChipUiModel
import com.devy.core_ui.utils.conditional
import com.devy.core_ui.utils.noRippleClickable

/**
 * Created by JeYHoon on 2025. 10. 3..
 */


@Composable
fun DChip(
    modifier: Modifier = Modifier,
    item: DChipUiModel,
    onClick: () -> Unit = {},
) {
    DChip(
        modifier = modifier,
        title = item.title,
        icon = item.icon,
        color = item.color,
        textColor = item.textColor,
        style = item.style,
        roundedCorner = item.roundedCorner,
        selectType = item.selectType,
        onClick = onClick,
    )
}

@Composable
fun DChip(
    modifier: Modifier = Modifier,
    title: String,
    icon: Painter? = null,
    color: Color,
    textColor: Color,
    style: TextStyle,
    roundedCorner: Dp = 20.dp,
    selectType: DChipSelectType,
    onClick: () -> Unit = {},
) {

    Row(
        modifier = modifier
            .conditional(selectType == DChipSelectType.Stroke) {
                border(
                    width = 2.dp,
                    color = color,
                    shape = RoundedCornerShape(roundedCorner)
                )
            }
            .conditional(selectType == DChipSelectType.Solid) {
                background(
                    color = color,
                    shape = RoundedCornerShape(roundedCorner)
                )
            }
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .noRippleClickable { onClick() },
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon?.let { icon ->
            Image(
                painter = icon,
                color = textColor,
            )
        }

        Text(
            text = title,
            style = style,
            color = textColor,
        )
    }
}