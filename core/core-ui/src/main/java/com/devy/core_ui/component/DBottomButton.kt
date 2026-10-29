package com.devy.core_ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devy.core_ui.component.local.LocalDToolBarColor
import com.devy.core_ui.model.DBottomButtonInfo
import com.devy.core_ui.model.DBottomButtonType
import com.devy.core_ui.theme.CoreTheme
import com.devy.core_ui.utils.conditional
import com.devy.core_ui.utils.singleClickable
import com.devy.core_ui.utils.toContentString

/**
 * Created by JeYHoon on 2025. 10. 12..
 */

private const val BUTTON_ROUNDED_CORNER = 24
const val MIN_BOTTOM_BUTTON_HEIGHT = 60

@Composable
fun DBottomButton(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(16.dp),
    buttonInfo: DBottomButtonInfo,
) {
    when (buttonInfo.type) {
        DBottomButtonType.TextSolid,
        DBottomButtonType.TextStroke -> {
            TextButton(
                modifier = modifier,
                padding = padding,
                buttonInfo = buttonInfo,
            )
        }

        DBottomButtonType.IconSolid,
        DBottomButtonType.IconStroke -> {
            IconButton(
                modifier = modifier,
                padding = padding,
                buttonInfo = buttonInfo
            )
        }
    }
}

@Composable
private fun TextButton(
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    buttonInfo: DBottomButtonInfo,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(padding)
            .clip(RoundedCornerShape(BUTTON_ROUNDED_CORNER.dp))
            .heightIn(min = MIN_BOTTOM_BUTTON_HEIGHT.dp)
            .singleClickable {
                if (buttonInfo.enabled) buttonInfo.onClick() else buttonInfo.onDisabledClick()
            }
            .conditional(buttonInfo.type.isSolid) {
                solidButton(
                    color = buttonInfo.color,
                    enabled = buttonInfo.enabled,
                    isText = buttonInfo.type.isText()
                )
            }
            .conditional(buttonInfo.type.isSolid.not()) {
                strokeButton(
                    color = buttonInfo.color,
                    enabled = buttonInfo.enabled,
                    isText = buttonInfo.type.isText()
                )
            }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = buttonInfo.text?.toContentString() ?: "",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            style = CoreTheme.typo.titleB,
            color = buttonInfo.textColor ?: CoreTheme.colors.onSurface0,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun IconButton(
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    buttonInfo: DBottomButtonInfo,
) {
    Box(
        modifier = modifier
            .padding(padding)
            .size(MIN_BOTTOM_BUTTON_HEIGHT.dp)
            .conditional(buttonInfo.type.isSolid) {
                solidButton(
                    color = buttonInfo.color,
                    enabled = buttonInfo.enabled,
                    isText = buttonInfo.type.isText()
                )
            }
            .conditional(buttonInfo.type.isSolid.not()) {
                strokeButton(
                    color = buttonInfo.color,
                    enabled = buttonInfo.enabled,
                    isText = buttonInfo.type.isText()
                )
            }
            .singleClickable {
                if (buttonInfo.enabled) buttonInfo.onClick() else buttonInfo.onDisabledClick()
            },
    ) {
        buttonInfo.icon?.let { icon ->
            Image(
                modifier = Modifier.align(Alignment.Center),
                painter = icon,
                color = CoreTheme.colors.onSurface0
            )
        }

        if (buttonInfo.visibleRedDot) {
            Box(
                modifier = Modifier
                    .padding(top = 14.dp, end = 14.dp)
                    .size(6.dp)
                    .align(Alignment.TopEnd)
                    .background(
                        color = buttonInfo.redDotColor ?: Color.Red,
                        shape = RoundedCornerShape(100)
                    )
            )
        }
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun Modifier.solidButton(
    color: Color? = null,
    enabled: Boolean = true,
    isText: Boolean = false
) = run {
    val providedPrimary = LocalDToolBarColor.current.primary
    val primaryColor =
        when {
            color != null && color != Color.Unspecified -> color
            providedPrimary != Color.Unspecified -> providedPrimary
            else -> CoreTheme.colors.primary
        }

    this.background(
        color = if (enabled) primaryColor else CoreTheme.colors.onSurface70,
        shape = if (isText) RoundedCornerShape(BUTTON_ROUNDED_CORNER.dp) else RoundedCornerShape(100)
    )
}

@SuppressLint("ComposableNaming")
@Composable
fun Modifier.strokeButton(
    color: Color? = null,
    enabled: Boolean = true,
    isText: Boolean = false
) = run {
    val providedPrimary = LocalDToolBarColor.current.primary
    val primaryColor =
        when {
            color != null && color != Color.Unspecified -> color
            providedPrimary != Color.Unspecified -> providedPrimary
            else -> CoreTheme.colors.primary
        }

    this
        .background(
            CoreTheme.colors.background.copy(alpha = 0.5f),
            if (isText) RoundedCornerShape(BUTTON_ROUNDED_CORNER.dp) else RoundedCornerShape(100)
        )
        .border(
            width = 1.dp,
            color = if (enabled) primaryColor else CoreTheme.colors.onSurface70,
            shape = if (isText) RoundedCornerShape(BUTTON_ROUNDED_CORNER.dp) else RoundedCornerShape(100)
        )
}

const val BOTTOM_GRADIENT_WITH_BUTTON_HEIGHT = 140
const val BOTTOM_GRADIENT_HEIGHT = 48

fun List<DBottomButtonInfo>.gradientHeight(): Dp {
    return if (isEmpty()) BOTTOM_GRADIENT_HEIGHT.dp else BOTTOM_GRADIENT_WITH_BUTTON_HEIGHT.dp
}