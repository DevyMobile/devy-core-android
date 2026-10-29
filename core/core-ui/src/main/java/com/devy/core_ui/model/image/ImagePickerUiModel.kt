package com.devy.core_ui.model.image

data class ImagePickerUiModel(
    val imgSrc: String,
    val checked: Boolean = false,
    val isGif: Boolean = false,
)