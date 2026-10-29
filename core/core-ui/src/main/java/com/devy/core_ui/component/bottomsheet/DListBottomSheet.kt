package com.devy.core_ui.component.bottomsheet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.devy.core_ui.model.DBottomSheetListItemUiModel
import com.devy.core_ui.model.DBottomSheetListTextStyle
import com.devy.core_ui.model.DBottomSheetStyle
import com.devy.core_ui.theme.CoreTheme
import com.devy.core_ui.utils.toContentString

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

@Composable
fun DListBottomSheet(
    title: Any,
    list: List<DBottomSheetListItemUiModel>,
    style: DBottomSheetStyle = DBottomSheetStyle(
        backgroundColor = Color.Unspecified,
        titleStyle = CoreTheme.typo.titleB,
        titleColor = Color.Unspecified,
    ),
    listTextStyle: DBottomSheetListTextStyle = DBottomSheetListTextStyle(
        color = CoreTheme.colors.onSurface0,
        style = CoreTheme.typo.body
    ),
    bottomContent: @Composable ColumnScope.() -> Unit = {},
    clickWithDismiss: Boolean = false,
    onDismissRequest: () -> Unit,
) {
    DBottomSheetModal(
        title = title.toContentString(),
        style = style,
        onDismissRequest = onDismissRequest
    ) {
        list.forEach { item ->
            DBottomSheetListItem(
                item.copy(
                    onClick = {
                        if (clickWithDismiss) onDismissRequest()
                        item.onClick()
                    }
                ),
                listTextStyle
            )
        }

        bottomContent()
    }
}

@Composable
fun DListBottomSheet(
    title: Any,
    list: List<DBottomSheetListItemUiModel>,
    style: DBottomSheetStyle = DBottomSheetStyle(
        backgroundColor = Color.Unspecified,
        titleStyle = CoreTheme.typo.titleB,
        titleColor = Color.Unspecified,
    ),
    listTextStyle: DBottomSheetListTextStyle = DBottomSheetListTextStyle(
        color = CoreTheme.colors.onSurface0,
        style = CoreTheme.typo.body
    ),
    button: BottomSheetModalButton,
    clickWithDismiss: Boolean = false,
    onDismissRequest: () -> Unit,
) {
    DBottomSheetModal(
        title = title.toContentString(),
        style = style,
        button = button,
        onDismissRequest = onDismissRequest
    ) {
        list.forEach { item ->
            DBottomSheetListItem(
                item.copy(
                    onClick = {
                        if (clickWithDismiss) onDismissRequest()
                        item.onClick()
                    }
                ),
                listTextStyle
            )
        }
    }
}