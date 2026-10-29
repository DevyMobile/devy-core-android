package com.devy.core_ui.model.image

sealed class ImageFolderType {
    data object All: ImageFolderType()
    data class Folder(
        val data: ImageFolderData,
    ): ImageFolderType()
}
