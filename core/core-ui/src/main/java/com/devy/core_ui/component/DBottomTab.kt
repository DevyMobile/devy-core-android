package com.devy.core_ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devy.core_ui.model.DBottomTabUiModel
import com.devy.core_ui.model.rememberSelectionTrackerState
import com.devy.core_ui.model.selectionBorderContainer
import com.devy.core_ui.model.trackSelectedItem
import com.devy.core_ui.theme.CoreTheme
import com.devy.core_ui.utils.noRippleClickable

private const val MIN_BOTTOM_TAB_HEIGHT = 60

@Composable
fun BoxScope.DBottomTab(
    modifier: Modifier = Modifier,
    list: List<DBottomTabUiModel>,
    selectedTabId: Int,
    onSelectedTabId: (selectedTabId: Int) -> Unit,
    borderColor: Color = CoreTheme.colors.border,
    titleColor: Color = CoreTheme.colors.onSurface0,
    selectedTitleColor: Color = CoreTheme.colors.onSurface0,
    backgroundColor: Color = CoreTheme.colors.background.copy(alpha = 0.75f),
    redDotColor: Color = Color.Red,
    selectedColor: Color = CoreTheme.colors.primary.copy(alpha = 0.5f),
    containerShape: Shape = RoundedCornerShape(100),
    selectedStyle: TextStyle = CoreTheme.typo.bodyB,
    unSelectedStyle: TextStyle = CoreTheme.typo.body,
    selectionCornerRadius: Dp = 100.dp,
) {

    val selectionState = rememberSelectionTrackerState()

    Row(
        modifier = modifier
            .heightIn(min = MIN_BOTTOM_TAB_HEIGHT.dp)
            .background(
                color = backgroundColor,
                shape = containerShape,
            )
            .border(
                color = borderColor,
                shape = containerShape,
                width = 2.dp
            )
            .padding(8.dp)
            .selectionBorderContainer(
                cornerRadius = selectionCornerRadius,
                state = selectionState,
                selectedId = selectedTabId,
                color = selectedColor,
            )
            .align(Alignment.BottomCenter),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        list.forEach { tab ->
            DBottomTabItem(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = MIN_BOTTOM_TAB_HEIGHT.dp)
                    .trackSelectedItem(tab.tabId, selectionState),
                selected = selectedTabId == tab.tabId,
                titleColor = titleColor,
                selectedTitleColor = selectedTitleColor,
                tab = tab,
                redDotColor = redDotColor,
                selectedStyle = selectedStyle,
                unSelectedStyle = unSelectedStyle,
                onClick = {
                    onSelectedTabId(tab.tabId)
                    tab.tabOnClick()
                },
            )
        }
    }
}

@Composable
fun DBottomTabItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    titleColor: Color,
    selectedTitleColor: Color,
    tab: DBottomTabUiModel,
    redDotColor: Color,
    onClick: () -> Unit,
    selectedStyle: TextStyle = CoreTheme.typo.bodyB,
    unSelectedStyle: TextStyle = CoreTheme.typo.body,
) {

    Box(
        modifier = modifier
            .noRippleClickable { onClick() },
    ) {
        when (tab) {
            is DBottomTabUiModel.All -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = tab.icon,
                        color = if(selected) selectedTitleColor else titleColor,
                    )

                    Text(
                        text = tab.title,
                        style = if (selected) selectedStyle else unSelectedStyle,
                        color = if(selected) selectedTitleColor else titleColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            is DBottomTabUiModel.Text -> {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = tab.title,
                    style = if (selected) selectedStyle else unSelectedStyle,
                    color = titleColor
                )
            }

            is DBottomTabUiModel.Icon -> {
                Image(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(24.dp),
                    painter = tab.icon,
                    color = titleColor,
                )
            }
        }

        if (tab.tabVisibleRedDot) {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, end = 14.dp)
                    .size(6.dp)
                    .align(Alignment.TopEnd)
                    .background(
                        color = redDotColor,
                        shape = RoundedCornerShape(100)
                    )
            )
        }
    }
}

fun List<DBottomTabUiModel>.gradientHeight(): Dp {
    return if (isEmpty()) BOTTOM_GRADIENT_HEIGHT.dp else BOTTOM_GRADIENT_WITH_BUTTON_HEIGHT.dp
}