package com.devy.core_ui.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

/**
 * Created by JeYHoon on 2025. 10. 12..
 */
data class DBottomButtonInfo(
    val text: Any? = null,
    val icon: Painter? = null,
    val enabled: Boolean = true,
    val visibleRedDot: Boolean = false,
    val textColor: Color? = null,
    val color: Color? = null,
    val redDotColor: Color? = null,
    val type: DBottomButtonType = DBottomButtonType.TextSolid,
    val onClick: () -> Unit,
    val onDisabledClick: () -> Unit = {},
)

enum class DBottomButtonType(val isSolid: Boolean) {
    TextSolid(true),
    TextStroke(false),
    IconSolid(true),
    IconStroke(false),
    ;

    fun isText() = this == TextSolid || this == TextStroke
    fun isIcon() = this == IconSolid || this == IconStroke
}