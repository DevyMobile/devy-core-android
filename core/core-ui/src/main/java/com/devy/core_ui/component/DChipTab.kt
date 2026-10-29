package com.devy.core_ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devy.core_ui.model.DChipDefaults
import com.devy.core_ui.model.DChipSelectType
import com.devy.core_ui.model.DChipStyle
import com.devy.core_ui.theme.CoreTheme

/**
 * Created by JeYHoon on 2025. 10. 3..
 */

@Composable
fun <T> DChipTab(
    modifier: Modifier = Modifier,
    chips: List<T>,
    displayTitle: (T) -> (String),
    icon: @Composable ((T) -> (Painter))? = null,
    selected: T,
    chipColor: @Composable ((T) -> (Color))? = null,
    onSelected: (T) -> Unit,
    itemPadding: Dp = 16.dp,
    innerPadding: PaddingValues = PaddingValues(),
    style: DChipStyle = DChipDefaults.style()
) {

    var selectedItem by remember(selected) { mutableStateOf(selected) }

    val selectType =
        when {
            chipColor != null -> DChipSelectType.Solid
            else -> style.selectType
        }

    val selectedChipColor =
        when (selectType) {
            DChipSelectType.Solid -> {
                chipColor?.invoke(selectedItem) ?: style.unSelectedColor
            }

            DChipSelectType.Stroke -> {
                style.selectedColor
            }
        }
    val selectedTextColor = when (selectType) {
        DChipSelectType.Solid -> {
            CoreTheme.colors.onSurface0
        }

        DChipSelectType.Stroke -> {
            style.selectedColor
        }
    }

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(itemPadding),
        contentPadding = innerPadding
    ) {
        items(chips) { chip ->
            val selected = selectedItem == chip
            DChip(
                title = displayTitle(chip),
                color = if (selected) selectedChipColor else chipColor?.invoke(chip)
                    ?: style.unSelectedColor,
                icon = icon?.invoke(chip),
                textColor = if (selected) selectedTextColor else chipColor?.invoke(chip)
                    ?: style.unSelectedColor,
                style = if (selected) style.selectedTextStyle else style.unSelectedTextStyle,
                roundedCorner = style.roundedCorner,
                selectType = if (selected) selectType else DChipSelectType.Stroke,
                onClick = {
                    onSelected(chip)
                }
            )
        }
    }
}

@Composable
fun <T> DChipFlowTab(
    modifier: Modifier = Modifier,
    chips: List<T>,
    displayTitle: (T) -> (String),
    icon: @Composable ((T) -> (Painter))? = null,
    selected: T,
    chipColor: @Composable ((T) -> (Color))? = null,
    onSelected: (T) -> Unit,
    itemPadding: Dp = 16.dp,
    innerPadding: PaddingValues = PaddingValues(),
    style: DChipStyle = DChipDefaults.style()
) {

    var selectedItem by remember(selected) { mutableStateOf(selected) }

    val selectType =
        when {
            chipColor != null -> DChipSelectType.Solid
            else -> style.selectType
        }

    val selectedChipColor =
        when (selectType) {
            DChipSelectType.Solid -> {
                chipColor?.invoke(selectedItem) ?: style.unSelectedColor
            }

            DChipSelectType.Stroke -> {
                style.selectedColor
            }
        }
    val selectedTextColor = when (selectType) {
        DChipSelectType.Solid -> {
            CoreTheme.colors.onSurface0
        }

        DChipSelectType.Stroke -> {
            style.selectedColor
        }
    }

    FlowRow(
        modifier = modifier.padding(innerPadding),
        horizontalArrangement = Arrangement.spacedBy(itemPadding),
        verticalArrangement = Arrangement.spacedBy(itemPadding),
    ) {
        chips.forEach { chip ->
            val selected = selectedItem == chip
            DChip(
                title = displayTitle(chip),
                color = if (selected) selectedChipColor else chipColor?.invoke(chip)
                    ?: style.unSelectedColor,
                icon = icon?.invoke(chip),
                textColor = if (selected) selectedTextColor else chipColor?.invoke(chip)
                    ?: style.unSelectedColor,
                style = if (selected) style.selectedTextStyle else style.unSelectedTextStyle,
                roundedCorner = style.roundedCorner,
                selectType = if (selected) selectType else DChipSelectType.Stroke,
                onClick = {
                    onSelected(chip)
                }
            )
        }
    }
}