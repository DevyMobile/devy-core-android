package com.devy.core_ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devy.core_ui.model.DBottomButtonInfo
import com.devy.core_ui.model.DBottomTabUiModel
import com.devy.core_ui.theme.CoreTheme
import com.devy.core_ui.utils.conditional

@Composable
fun BoxScope.DBottomTabContainer(
    bottomTabs: List<DBottomTabUiModel>,
    selectedTabId: Int,
    onSelectedTabId: (selectedTabId: Int) -> Unit,
    borderColor: Color = CoreTheme.colors.border,
    titleColor: Color = CoreTheme.colors.onSurface0,
    selectedTitleColor: Color = CoreTheme.colors.onSurface0,
    backgroundColor: Color = CoreTheme.colors.background.copy(alpha = 0.75f),
    selectedColor: Color = CoreTheme.colors.primary.copy(alpha = 0.5f),
    containerShape: Shape = RoundedCornerShape(100),
    selectedStyle: TextStyle = CoreTheme.typo.bodyB,
    unSelectedStyle: TextStyle = CoreTheme.typo.body,
    selectionCornerRadius: Dp = 100.dp,
    visibleBottomGradient: Boolean = true,
) {
    DBottomContainer(
        visibleBottomGradient = visibleBottomGradient
    ) {
        DBottomTab(
            modifier = Modifier.fillMaxWidth(),
            list = bottomTabs,
            backgroundColor = backgroundColor,
            selectedColor = selectedColor,
            titleColor = titleColor,
            selectedTitleColor = selectedTitleColor,
            borderColor = borderColor,
            containerShape = containerShape,
            selectedStyle = selectedStyle,
            unSelectedStyle = unSelectedStyle,
            selectionCornerRadius = selectionCornerRadius,
            selectedTabId = selectedTabId,
            onSelectedTabId = onSelectedTabId,
        )
    }
}

@Composable
fun BoxScope.DBottomButtonContainer(
    bottomButtonInfos: List<DBottomButtonInfo>,
    visibleBottomGradient: Boolean = true,
) {
    DBottomContainer(
        visibleBottomGradient = visibleBottomGradient,
        gradientHeight = bottomButtonInfos.gradientHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            bottomButtonInfos.forEach { buttonInfo ->
                DBottomButton(
                    modifier = Modifier
                        .conditional(buttonInfo.type.isText()) {
                            weight(1f)
                        },
                    buttonInfo = buttonInfo,
                    padding = PaddingValues(0.dp)
                )
            }
        }
    }
}

@Composable
fun BoxScope.DBottomContainer(
    visibleBottomGradient: Boolean = true,
    gradientHeight: Dp = BOTTOM_GRADIENT_WITH_BUTTON_HEIGHT.dp,
    content: @Composable BoxScope.() -> Unit,
) {
    if (visibleBottomGradient) {
        GradientOverlay(
            modifier = Modifier
                .fillMaxWidth()
                .height(gradientHeight),
            type = GradientType.Bottom
        )
    }
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(vertical = 16.dp, horizontal = 24.dp)
    ) {
        content()
    }
}