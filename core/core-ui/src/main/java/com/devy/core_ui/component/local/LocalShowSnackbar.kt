package com.devy.core_ui.component.local

import androidx.compose.runtime.staticCompositionLocalOf
import com.devy.core_ui.model.snackbar.DSnackbarData

val LocalShowSnackbar = staticCompositionLocalOf<(DSnackbarData) -> Unit> { { _ -> } }