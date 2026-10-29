package com.devy.core_ui.component

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.devy.core_ui.theme.CoreTheme

/**
 * Created by JeYHoon on 2025. 10. 4..
 */

@Composable
fun DProgressDialog(
    onDismissRequest: () -> Unit = {},
) {

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        DProgress()
    }
}

@Composable
fun DProgress(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier,
        color = CoreTheme.colors.primary,
    )
}
