package com.devy.core_ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.devy.core_ui.component.local.LocalDCommonColor
import com.devy.core_ui.model.DDropDownMenuItem
import com.devy.core_ui.theme.CoreTheme
import com.devy.core_ui.utils.orProvided

/**
 * Created by JeYHoon on 2025. 11. 25..
 */

@Composable
fun DDropDownMenu(
    showDropDown: MutableState<Boolean>,
    items: List<DDropDownMenuItem>,
    background: Color? = null,
    borderColor: Color? = null,
    itemTextColor: Color? = null,
    shadowElevation: Dp = 12.dp,
    clickWithDismiss: Boolean = true,
    content: @Composable () -> Unit,
) {
    DDropDownMenu(
        showDropDown = showDropDown.value,
        items = items,
        background = background,
        borderColor = borderColor,
        itemTextColor = itemTextColor,
        shadowElevation = shadowElevation,
        clickWithDismiss = clickWithDismiss,
        onDismissRequest = { showDropDown.value = false },
        content = content
    )
}

@Composable
fun DDropDownMenu(
    showDropDown: Boolean,
    items: List<DDropDownMenuItem>,
    background: Color? = null,
    borderColor: Color? = null,
    itemTextColor: Color? = null,
    shadowElevation: Dp = 12.dp,
    clickWithDismiss: Boolean = true,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) {

    val commonColor = LocalDCommonColor.current
    val providedBackground = commonColor.background
    val providedBorder = commonColor.border
    val providedOnSurface = commonColor.onSurface.primary

    Box {
        content()

        DropdownMenu(
            modifier = Modifier,
            containerColor = background.orProvided(providedBackground),
            offset = DpOffset((-8).dp, 0.dp),
            shape = RoundedCornerShape(12.dp),
            expanded = showDropDown,
            onDismissRequest = onDismissRequest,
            shadowElevation = shadowElevation,
            border = BorderStroke(width = 1.dp, color = borderColor.orProvided(providedBorder)),
            properties = PopupProperties(focusable = true)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    leadingIcon = item.iconResId?.let {iconResId ->
                        {
                            Image(
                                modifier = Modifier.size(16.dp),
                                painter = painterResource(id = iconResId),
                                color = itemTextColor.orProvided(providedOnSurface),
                            )
                        }
                    },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    text = {
                        Text(
                            modifier = Modifier.padding(horizontal = 0.dp),
                            text = item.text,
                            style = CoreTheme.typo.body,
                            color = itemTextColor.orProvided(providedOnSurface),
                        )
                    },
                    onClick = {
                        item.onClick()
                        if (clickWithDismiss) { onDismissRequest() }
                    }
                )
            }
        }
    }
}