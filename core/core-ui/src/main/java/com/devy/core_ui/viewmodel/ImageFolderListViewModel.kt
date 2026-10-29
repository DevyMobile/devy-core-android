package com.devy.core_ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class ImageFolderListViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _allImageFolders = MutableStateFlow<List<ImageFolderType>>(emptyList())
    private val allImageFolders = _allImageFolders.asStateFlow()

    private val _imageFolders = MutableStateFlow<List<ImageFolderType>>(emptyList())
    val imageFolders = _imageFolders.asStateFlow()

    private val _allImageCount = MutableStateFlow(0)
    val allImageCount = _allImageCount.asStateFlow()

    private val _favoriteBucketIds = MutableStateFlow<List<Long>>(emptyList())
    val favoriteBucketIds = _favoriteBucketIds.asStateFlow()

    init {
        loadImageFolders()
        loadAllImageCount()
    }

    private fun loadImageFolders() {
        viewModelScope.launch(Dispatchers.IO) {
            val arrayList: ArrayList<ImageFolderType> = arrayListOf(ImageFolderType.All)
            arrayList.addAll(
                ImageFolderUtil.getImageFolders(context).map {
                    ImageFolderType.Folder(data = it)
                }
            )
            _imageFolders.update {
                arrayList.toList()
            }
            _allImageFolders.update {
                arrayList.toList()
            }
        }
    }

    private fun loadAllImageCount() {
        viewModelScope.launch(Dispatchers.IO) {
            _allImageCount.update {
                ImageFolderUtil.getAllImageCount(context)
            }
        }
    }

    fun filterFolder(keyword: String) {
        _imageFolders.update {
            allImageFolders.value.filter { item ->
                when (item) {
                    is ImageFolderType.All -> {
                        true
                    }

                    is ImageFolderType.Folder -> {
                        item.data.title.contains(keyword)
                    }
                }
            }
        }
    }
}