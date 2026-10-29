package com.devy.core_ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.devy.core_ui.component.local.LocalDCommonColor
import com.devy.core_ui.model.FloatingActionButtonInfo
import com.devy.core_ui.theme.CoreTheme
import com.devy.core_ui.utils.noRippleClickable

/**
 * Created by JeYHoon on 2025. 10. 3..
 */
@Composable
fun FloatingActionButton(
    uiModel: FloatingActionButtonInfo,
    padding: PaddingValues = PaddingValues(bottom = 24.dp, end = 16.dp)
) {

    val providedColors = LocalDCommonColor.current
    val providedPrimary = providedColors.primary
    val providedOnSurface = providedColors.onSurface.primary

    val backgroundColor = when {
        uiModel.backgroundColor != Color.Unspecified -> uiModel.backgroundColor
        providedPrimary != Color.Unspecified -> providedPrimary
        else -> CoreTheme.colors.primary
    }

    val iconColor = when {
        uiModel.iconColor != Color.Unspecified -> uiModel.iconColor
        providedOnSurface != Color.Unspecified -> providedOnSurface
        else -> CoreTheme.colors.onSurface0
    }

    Box(
        modifier = Modifier
            .padding(padding)
            .size(uiModel.size)
            .background(
                shape = CircleShape,
                color = backgroundColor
            )
            .noRippleClickable {
                uiModel.onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(uiModel.iconSize),
            painter = painterResource(uiModel.iconResId),
            color = iconColor
        )
    }
}