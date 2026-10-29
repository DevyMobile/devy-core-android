package com.devy.core_ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.devy.core_ui.R
import com.devy.core_ui.component.local.LocalDCommonColor
import com.devy.core_ui.model.DChipSelectType
import com.devy.core_ui.model.DChipUiModel
import com.devy.core_ui.theme.CoreTheme

@Composable
fun DOnOffSelector(
    modifier: Modifier,
    isOn: Boolean,
    onSelected: (Boolean) -> Unit,
) {

    val providedPrimary = LocalDCommonColor.current.primary

    fun selectType(isOn: Boolean) = if (isOn) DChipSelectType.Solid else DChipSelectType.Stroke

    @Composable
    fun color(isOn: Boolean) = if (isOn) providedPrimary else CoreTheme.colors.onSurface50

    @Composable
    fun textColor(isOn: Boolean) =
        if (isOn) CoreTheme.colors.onSurface0 else CoreTheme.colors.onSurface50

    @Composable
    fun style(isOn: Boolean) = if (isOn) CoreTheme.typo.bodyB else CoreTheme.typo.body

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DChip(
            modifier = Modifier.weight(1f),
            item = DChipUiModel(
                selectType = selectType(isOn),
                title = "On",
                icon = painterResource(id = R.drawable.ic_radio_checked_24),
                color = color(isOn),
                textColor = textColor(isOn),
                style = style(isOn),
            ),
            onClick = { onSelected(true) }
        )

        DChip(
            modifier = Modifier.weight(1f),
            item = DChipUiModel(
                selectType = selectType(isOn.not()),
                title = "Off",
                icon = painterResource(id = R.drawable.ic_radio_checked_24),
                color = color(isOn.not()),
                textColor = textColor(isOn.not()),
                style = style(isOn.not()),
            ),
            onClick = { onSelected(false) }
        )
    }
}