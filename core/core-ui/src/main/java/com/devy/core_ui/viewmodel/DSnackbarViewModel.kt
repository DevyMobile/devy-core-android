package com.devy.core_ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devy.core_ui.model.snackbar.DSnackbarData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DSnackbarViewModel @Inject constructor(

) : ViewModel() {

    private val _snackbar = MutableSharedFlow<DSnackbarData>()
    val snackbar = _snackbar.asSharedFlow()

    fun showSnackbar(snackbar: DSnackbarData) {
        viewModelScope.launch(Dispatchers.IO) {
            _snackbar.emit(snackbar)
        }
    }
}