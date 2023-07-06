package com.arribas.myshoppinglist.data.utils

data class TextFieldDialogUiState(
    val title: String = "",
    val body: String = "",
    val name: String = "",
    val msgError: String = "",
    var isShow: Boolean = false,
    val isShowBtDismiss: Boolean = false
)


