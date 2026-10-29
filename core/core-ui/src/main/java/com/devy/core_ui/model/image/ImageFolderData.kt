package com.devy.core_ui.model.image

import android.net.Uri

data class ImageFolderData(
    val bucketId: Long,
    val title: String,
    val count: Int,
    val thumbnailUri: Uri,
)