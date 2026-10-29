package com.devy.core_ui.model

import androidx.compose.ui.graphics.painter.Painter

sealed class DBottomTabUiModel(
    val tabId: Int,
    val tabVisibleRedDot: Boolean = false,
    val tabOnClick: () -> Unit,
) {
    data class All(
        val id: Int,
        val icon: Painter,
        val title: String,
        val visibleRedDot: Boolean = false,
        val onClick: () -> Unit = {},
    ): DBottomTabUiModel(
        tabId = id,
        tabVisibleRedDot = visibleRedDot,
        tabOnClick = onClick,
    )

    data class Text(
        val id: Int,
        val title: String,
        val visibleRedDot: Boolean = false,
        val onClick: () -> Unit = {},
    ): DBottomTabUiModel(
        tabId = id,
        tabVisibleRedDot = visibleRedDot,
        tabOnClick = onClick,
    )

    data class Icon(
        val id: Int,
        val icon: Painter,
        val visibleRedDot: Boolean = false,
        val onClick: () -> Unit = {},
    ): DBottomTabUiModel(
        tabId = id,
        tabVisibleRedDot = visibleRedDot,
        tabOnClick = onClick,
    )
}