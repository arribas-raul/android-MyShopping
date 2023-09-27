package com.arribas.myshoppinglist.ui.view.dialog.shoplistSelectDialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arribas.myshoppinglist.data.repository.ShoplistRepository
import com.arribas.myshoppinglist.ui.view.screen.shoplist.ShoplistUiState
import com.arribas.myshoppinglist.ui.view.screen.shoplist.shoplistList.ShoplisListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShoplistSelectDialogViewModel(
    private val shoplistRepository: ShoplistRepository
): ViewModel() {

    private val _listUiState = MutableStateFlow(ShoplisListUiState())
    val listUiState: StateFlow<ShoplisListUiState> = _listUiState

    var shoplistUiState by mutableStateOf(ShoplistUiState())
        private set

    init{
        getData()
    }

    fun setShoplist(element: ShoplistUiState){
        shoplistUiState = shoplistUiState.copy(
            id = element.id,
            name = element.name,
            type = element.type
        )

    }

    private fun getData(){
        viewModelScope.launch {
            shoplistRepository.getItemsByName().collect {
                list ->
                    _listUiState.value = ShoplisListUiState(
                        itemList = list,
                        itemCount = list.count()
                    )
            }
        }
    }
}