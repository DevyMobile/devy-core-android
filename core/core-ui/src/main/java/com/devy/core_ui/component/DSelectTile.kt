package com.devy.core_ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.devy.core_ui.R
import com.devy.core_ui.theme.CoreTheme
import com.devy.core_ui.utils.singleClickable

@Composable
fun <T> DSelectTile(
    modifier: Modifier = Modifier,
    items: List<T>,
    displayTitle: (T) -> (String),
    selected: T,
    onSelected: (T) -> Unit,
    selectedColor: Color = CoreTheme.colors.primary,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items.forEach { item ->
            DSelectTileItem(
                modifier = Modifier.weight(1f),
                selectedColor = selectedColor,
                selected = selected == item,
                title = displayTitle(item),
                onClick = {
                    onSelected(item)
                }
            )
        }
    }
}

private const val MIN_HEIGHT = 120
@Composable
fun DSelectTileItem(
    modifier: Modifier = Modifier,
    selectedColor: Color = CoreTheme.colors.primary,
    selected: Boolean,
    title: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .heightIn(min = MIN_HEIGHT.dp)
            .background(
                color = if(selected) selectedColor else CoreTheme.colors.onSurface60,
                shape = RoundedCornerShape(20.dp)
            )
            .singleClickable{ onClick() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = if(selected) R.drawable.ic_radio_checked_24 else R.drawable.ic_radio_unchecked_24),
            color = CoreTheme.colors.onSurface0,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = title,
            style = if(selected) CoreTheme.typo.bodyB else CoreTheme.typo.body,
            color = CoreTheme.colors.onSurface0,
        )
    }
}