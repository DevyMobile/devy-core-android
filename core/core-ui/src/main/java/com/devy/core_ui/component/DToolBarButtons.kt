package com.devy.core_ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devy.core_ui.R
import com.devy.core_ui.theme.CoreTheme
import com.devy.core_ui.utils.conditional
import com.devy.core_ui.utils.noRippleClickable

/**
 * Created by JeYHoon on 2025. 10. 2..
 */
@Composable
fun DToolBarTrailingText(
    @StringRes text: Int,
    onClick: () -> Unit,
    endPadding: Dp = 16.dp
) {
    DToolBarTrailingText(
        text = stringResource(id = text),
        onClick = onClick,
        endPadding = endPadding,
    )
}

@Composable
fun DToolBarTrailingText(
    text: String,
    onClick: () -> Unit,
    endPadding: Dp = 16.dp,
    style: TextStyle = CoreTheme.typo.title,
    color: Color = CoreTheme.colors.onSurface0,
) {
    Text(
        modifier = Modifier
            .padding(end = endPadding)
            .noRippleClickable { onClick() },
        text = text,
        style = style,
        color = color,
    )
}

@Composable
fun DToolBarTrailingIcon(
    iconResId: Int,
    onClick: () -> Unit,
    iconSize: Dp? = null,
    iconColor: Color = CoreTheme.colors.onSurface0,
) {
    Box(
        modifier = Modifier
            .size(BOX_HEIGHT.dp)
            .noRippleClickable{ onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .conditional(iconSize != null) {
                    iconSize?.let {
                        size(it)
                    } ?: wrapContentSize()
                },
            painter = painterResource(id = iconResId),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = iconColor)
        )
    }
}

private const val BOX_HEIGHT = 48

sealed class NavigationIcon(
    val iconResId: Int?,
    val onClick: () -> Unit,
) {
    data class None(val onClickIcon: () -> Unit = {}) :
        NavigationIcon(iconResId = null, onClick = onClickIcon)

    data class Back(val onClickBack: () -> Unit) :
        NavigationIcon(iconResId = R.drawable.ic_back_arrow_24, onClick = onClickBack)

    data class Close(val onClickClose: () -> Unit) :
        NavigationIcon(iconResId = R.drawable.ic_close_24, onClick = onClickClose)

    data class Custom(val icon: Int, val onClickIcon: () -> Unit = {}) :
        NavigationIcon(iconResId = icon, onClick = onClickIcon)
}