package com.devy.core_ui.model

import androidx.annotation.DrawableRes

/**
 * Created by JeYHoon on 2025. 11. 25..
 */
data class DDropDownMenuItem(
    @field:DrawableRes val iconResId: Int? = null,
    val text: String,
    val onClick: () -> Unit,
)
