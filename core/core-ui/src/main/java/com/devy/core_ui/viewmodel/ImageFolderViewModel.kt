package com.devy.core_ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devy.core_ui.R
import com.devy.core_ui.model.image.ImageFolderType
import com.devy.core_ui.utils.ImageFolderUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageFolderViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
): ViewModel() {

    private val _images = MutableStateFlow<List<String>>(emptyList())
    val images = _images.asStateFlow()

    private val _folderTitle = MutableStateFlow("")
    val folderTitle = _folderTitle.asStateFlow()

    private val _folderCount = MutableStateFlow(0)
    val folderCount = _folderCount.asStateFlow()

    private fun loadAllImageFolders() {
        viewModelScope.launch(Dispatchers.IO) {
            _images.update {
                ImageFolderUtil.getAllImages(context)
            }
        }
    }

    private fun loadAllImageCount() {
        viewModelScope.launch(Dispatchers.IO) {
            _folderCount.update {
                ImageFolderUtil.getAllImageCount(context)
            }
        }
    }

    private fun loadImageFolder(bucketId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _images.update {
                ImageFolderUtil.getImagesInFolder(context, bucketId)
            }
        }
    }

    fun initImageFolders() {
        loadAllImageFolders()
        loadAllImageCount()
        setTitleAll()
    }

    fun selectImageFolder(
        imageFolderData: ImageFolderType
    ) {
        when(imageFolderData) {
            is ImageFolderType.All -> {
                loadAllImageFolders()
                loadAllImageCount()
                setTitleAll()
            }

            is ImageFolderType.Folder -> {
                val data = imageFolderData.data
                loadImageFolder(data.bucketId)
                setTitle(data.title)
                setCount(data.count)
            }
        }
    }

    private fun setTitleAll() {
        setTitle(context.getString(R.string.show_all))
    }

    private fun setTitle(title: String) {
        _folderTitle.update { title }
    }

    private fun setCount(count: Int) {
        _folderCount.update { count }
    }
}