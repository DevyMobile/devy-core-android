package com.devy.core_ui.component.snackbar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devy.core_ui.component.local.LocalDSnackbarColor
import com.devy.core_ui.theme.CoreTheme

@Composable
fun DSnackbar(data: SnackbarData) {

    val colors = LocalDSnackbarColor.current

    Card(
        modifier = Modifier
            .padding(16.dp)
            .padding(bottom = 60.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.container),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        border = BorderStroke(
            width = 1.dp,
            color = colors.border
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = data.visuals.message,
                color = colors.text,
                style = CoreTheme.typo.body,
            )
        }
    }
}