package com.arribas.myshoppinglist.ui.viewModel

import com.arribas.myshoppinglist.data.MainTag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class MainViewModel {
    private val _uiState = MutableStateFlow(MainUiState())

    val uiState: StateFlow<MainUiState> = _uiState

    init {
        //_uiState.value = MainUiState()
    }

    fun updateCurrentTypeList(tab: MainTag) {
        _uiState.update {
            it.copy(
                currentTab = tab
            )
        }
    }
}

data class MainUiState(
    val currentTab: MainTag = MainTag.SHOP_LIST
)