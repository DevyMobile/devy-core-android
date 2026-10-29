package com.devy.core_ui.component.image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devy.core_ui.R
import com.devy.core_ui.component.BOTTOM_GRADIENT_WITH_BUTTON_HEIGHT
import com.devy.core_ui.component.DLazyColumn
import com.devy.core_ui.model.image.ImageFolderListItemUiModel
import com.devy.core_ui.model.image.ImageFolderType
import com.devy.core_ui.model.image.toUiModel
import com.devy.core_ui.theme.CoreTheme
import com.devy.core_ui.utils.noRippleClickable
import com.devy.core_ui.viewmodel.ImageFolderListViewModel

@Composable
fun DImageFolderListSide(
    searchKeyword: String,
    listState: LazyListState = rememberLazyListState(),
    favoriteBucketIds: List<Long>? = null,
    onClick: (ImageFolderType) -> Unit,
    onDismissRequest: () -> Unit,
    viewModel: ImageFolderListViewModel = hiltViewModel()
) {

    val imageFolderList by viewModel.imageFolders.collectAsStateWithLifecycle()
    val allImageCount by viewModel.allImageCount.collectAsStateWithLifecycle()
    val sortedImageFolderList = imageFolderList.sortedBy {
        favoriteBucketIds?.let { favoriteBucketIds ->
            if (it is ImageFolderType.Folder) {
                val bucketId = it.data.bucketId
                favoriteBucketIds.contains(bucketId).not()
            } else {
                false
            }
        } ?: false
    }


    LaunchedEffect(searchKeyword) {
        viewModel.filterFolder(searchKeyword)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CoreTheme.colors.black.copy(alpha = 0.75f))
            .noRippleClickable { onDismissRequest() }
    ) {
        DLazyColumn(
            modifier = Modifier
                .width(300.dp)
                .fillMaxHeight()
                .background(CoreTheme.colors.background),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                top = 12.dp,
                bottom = BOTTOM_GRADIENT_WITH_BUTTON_HEIGHT.dp
            ),
            state = listState
        ) {

            items(sortedImageFolderList) { item ->
                when (item) {
                    is ImageFolderType.All -> {
                        DImageFolderListItem(
                            data = ImageFolderListItemUiModel(
                                thumbnail = "",
                                title = stringResource(id = R.string.show_all),
                                count = allImageCount
                            ),
                            onClick = {
                                onClick(ImageFolderType.All)
                                onDismissRequest()
                            }
                        )
                    }

                    is ImageFolderType.Folder -> {
                        DImageFolderListItem(
                            data = item.data.toUiModel(),
                            keyword = searchKeyword,
                            pinned = favoriteBucketIds?.contains(item.data.bucketId) == true,
                            onClick = {
                                onClick(item)
                                onDismissRequest()
                            }
                        )
                    }
                }
            }
        }
    }
}