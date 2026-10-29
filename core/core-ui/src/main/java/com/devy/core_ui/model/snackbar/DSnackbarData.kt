package com.devy.core_ui.model.snackbar

import androidx.compose.material3.SnackbarDuration

data class DSnackbarData(
    val message: String,
    val duration: SnackbarDuration = SnackbarDuration.Short,
)

fun String.toSnackbarData() = DSnackbarData(message = this)
fun String.makeSnackbarData() = toSnackbarData()