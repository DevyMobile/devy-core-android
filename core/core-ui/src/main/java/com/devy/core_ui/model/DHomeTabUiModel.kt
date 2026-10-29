package com.devy.core_ui.model

data class DHomeTabUiModel(
    val id: Int,
    val title: String,
) {
    companion object {
        val All: DHomeTabUiModel = DHomeTabUiModel(
            id = HOME_TAB_ID_ALL,
            title = "전체",
        )
    }
}

const val HOME_TAB_ID_ALL = 0