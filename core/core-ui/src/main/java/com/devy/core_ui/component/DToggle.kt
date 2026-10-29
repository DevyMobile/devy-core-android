package com.devy.core_ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devy.core_ui.component.local.LocalDCommonColor
import com.devy.core_ui.theme.CoreTheme
import com.devy.core_ui.utils.UnitCallback
import com.devy.core_ui.utils.conditional
import com.devy.core_ui.utils.noRippleClickable

@Composable
fun DToggle(
    checked: Boolean,
    enabled: Boolean = true,
    circleSize: Dp = 20.dp,
    width: Dp = 48.dp,
    padding: Dp = 2.dp,
    onClick: UnitCallback,
) {

    val providedColor = LocalDCommonColor.current
    val primary = providedColor.primary
    val onSurface = CoreTheme.colors.onSurface0
    val onSurface40 = CoreTheme.colors.onSurface40
    val shape = RoundedCornerShape(percent = 100)
    val circleColor = if(checked) onSurface else onSurface40
    val contentAlignment = if(checked && enabled) Alignment.TopEnd else Alignment.TopStart

    Box(
        modifier = Modifier
            .width(width)
            .conditional(checked && enabled) {
                background(
                    color = primary,
                    shape = shape,
                )
            }
            .conditional(checked.not() || enabled.not()) {
                border(
                    width = 1.dp,
                    color = if (enabled) onSurface40 else CoreTheme.colors.onSurface30,
                    shape = shape,
                )
            }
            .noRippleClickable { if(enabled) onClick() }
            .padding(padding),
        contentAlignment = contentAlignment,
    ) {
        Box(
            modifier = Modifier
                .size(circleSize)
                .background(
                    color = if (enabled) circleColor else CoreTheme.colors.onSurface30,
                    shape = shape,
                )
        )
    }
}