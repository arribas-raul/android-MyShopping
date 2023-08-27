package com.arribas.myshoppinglist.ui.view.app

import android.app.Application
import androidx.lifecycle.ViewModel
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.data.model.Shoplist
import com.arribas.myshoppinglist.data.utils.DialogUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppViewModel(private val application: Application) : ViewModel() {

    private val _appUiState = MutableStateFlow(AppUiState())
    val appUiState: StateFlow<AppUiState> = _appUiState

    init{
        appUiState.value.title = application.resources.getString(R.string.shoplist_detail_title)
    }
}

data class AppUiState(
    var title: String = ""
)

