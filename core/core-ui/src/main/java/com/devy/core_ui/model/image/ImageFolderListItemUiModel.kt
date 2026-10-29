package com.devy.core_ui.model.image

data class ImageFolderListItemUiModel(
    val thumbnail: String,
    val title: String,
    val count: Int,
)

fun ImageFolderData.toUiModel() =
    _root_ide_package_.com.devy.core_ui.model.image.ImageFolderListItemUiModel(
        thumbnail = thumbnailUri.toString(),
        title = title,
        count = count,
    )