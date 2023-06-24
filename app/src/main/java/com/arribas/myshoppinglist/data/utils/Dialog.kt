package com.arribas.myshoppinglist.data.utils

enum class DIALOG_UI_TAG{
    TAG_DELETE, TAG_RESET, TAG_SUCCESS, TAG_ELEMENT_EXISTS
}

data class DialogUiState(
    val tag: DIALOG_UI_TAG = DIALOG_UI_TAG.TAG_RESET,
    val title: String = "",
    val body: String = "",
    var isShow: Boolean = false,
    val isShowBtDismiss: Boolean = false
)


