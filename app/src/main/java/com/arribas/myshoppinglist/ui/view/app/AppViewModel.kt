package com.arribas.myshoppinglist.ui.view.app

import android.content.Context
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.arribas.myshoppinglist.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppViewModel(private val context: Context) : ViewModel() {

    private val _appUiState = MutableStateFlow(AppUiState())
    val appUiState: StateFlow<AppUiState> = _appUiState

    init{
        appUiState.value.title = context.resources.getString(R.string.shoplist_detail_title)
    }
}

data class AppUiState(
    var title: String = "",
)

