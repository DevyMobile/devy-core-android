package com.devy.core_ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.devy.core_ui.component.local.LocalDCommonColor
import com.devy.core_ui.theme.CoreTheme
import com.devy.core_ui.utils.noRippleClickable

@Composable
fun SelectButton(
    modifier: Modifier = Modifier,
    icon: Painter,
    title: String,
    description: String? = null,
    onClick: () -> Unit,
) {

    val colors = LocalDCommonColor.current

    Row(
        modifier = modifier
            .noRippleClickable { onClick() }
            .border(
                width = 1.dp,
                color = colors.border,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = colors.surface.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.size(24.dp),
                painter = icon,
                contentDescription = null,
                color = colors.onSurface.primary,
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = CoreTheme.typo.bodyB,
                color = colors.onSurface.primary,
            )

            description?.let {
                Text(
                    text = it,
                    style = CoreTheme.typo.footnote,
                    color = colors.onSurface.secondary,
                )
            }
        }
    }
}