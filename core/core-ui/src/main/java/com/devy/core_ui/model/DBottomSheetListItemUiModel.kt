package com.devy.core_ui.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * Created by JeYHoon on 2025. 10. 2..
 */
data class DBottomSheetListItemUiModel(
    val title: Any,
    val onClick: () -> Unit,
) {

    @Composable
    fun title(): String {
        return when (title) {
            is String -> title
            is Int -> runCatching { stringResource(id = title as Int) }.getOrDefault(title.toString())
            else -> title.toString()
        }
    }
}