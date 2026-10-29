package com.devy.core_ui.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

/**
 * Created by JeYHoon on 2025. 10. 3..
 */

sealed class DContentItemUiModel(
    val itemTitle: Any,
    val itemDescription: Any? = null,
    val itemRequired: Boolean = false,
    val titleTrailingContent: Any? = null,
    val showTitleTrailingIcon: Boolean = true,
) {
    data class DTitleUiModel(
        val title: Any,
        val description: Any? = null,
        val required: Boolean = false,
        val content: String? = null,
        val showArrow: Boolean = true,
    ) : DContentItemUiModel(
        itemTitle = title,
        itemDescription = description,
        itemRequired = required,
        titleTrailingContent = content,
        showTitleTrailingIcon = showArrow
    )

    data class DContentUiModel(
        val title: Any,
        val description: Any? = null,
        val required: Boolean = false,
        val content: String? = null,
        val trailingContent: String? = null,
        val showArrow: Boolean = true,
    ) : DContentItemUiModel(
        itemTitle = title,
        itemDescription = description,
        itemRequired = required,
        titleTrailingContent = trailingContent,
        showTitleTrailingIcon = showArrow
    )

    data class DContentInputUiModel(
        val title: Any,
        val description: Any? = null,
        val required: Boolean = false,
        val placeholder: Any? = null,
        val content: String,
        val showClear: Boolean = true,
    ) : DContentItemUiModel(
        itemTitle = title,
        itemRequired = required,
        itemDescription = description,
    )

    data class DContentOnOffUiModel(
        val title: Any,
        val description: Any? = null,
        val required: Boolean = false,
        val isOn: Boolean,
    ) : DContentItemUiModel(
        itemTitle = title,
        itemDescription = description,
        itemRequired = required,
        showTitleTrailingIcon = false,
    )

    data class DContentDropdownUiModel<T>(
        val title: Any,
        val description: Any? = null,
        val required: Boolean = false,
        val content: List<T>,
        val selectedContent: T,
        val selectedTrailing: Boolean = false,
        val displayTextMapper: (T) -> Any,
    ) : DContentItemUiModel(
        itemTitle = title,
        itemDescription = description,
        itemRequired = required,
        titleTrailingContent = if(selectedTrailing) displayTextMapper(selectedContent) else "",
        showTitleTrailingIcon = selectedTrailing
    )
}

data class DContentActionUiModel(
    val icon: Painter? = null,
    val text: String,
    val background: Color? = null,
    val color: Color? = null,
    val onClick: () -> Unit = {},
)