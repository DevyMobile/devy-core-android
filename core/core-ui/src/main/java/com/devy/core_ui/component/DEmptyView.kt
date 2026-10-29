package com.devy.core_ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.devy.core_ui.R
import com.devy.core_ui.theme.CoreTheme

@Composable
fun DEmptyView(
    modifier: Modifier = Modifier,
    text: String = stringResource(id = R.string.empty_text),
) {
    Column(
        modifier = modifier
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_empty_mood_120),
            color = CoreTheme.colors.onSurface70
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = text,
            style = CoreTheme.typo.body,
            color = CoreTheme.colors.onSurface70
        )
    }
}