package com.devy.core_ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.devy.core_ui.R
import com.devy.core_ui.theme.CoreTheme
import com.devy.core_ui.utils.noRippleClickable

/**
 * Created by JeYHoon on 2025. 10. 26..
 */

@Composable
fun DCheckItem(
    modifier: Modifier = Modifier,
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    checkedColor: Color = CoreTheme.colors.primary,
) {

    val color = if (checked) checkedColor else CoreTheme.colors.onSurface0
    val icon = if(checked) R.drawable.ic_radio_checked_24 else R.drawable.ic_radio_unchecked_24

    Row(
        modifier = modifier
            .noRippleClickable{
                onCheckedChange(!checked)
            },
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = icon),
            tint = color,
            contentDescription = null,
        )

        Text(
            text = text,
            style = CoreTheme.typo.body,
            color = color,
        )
    }
}