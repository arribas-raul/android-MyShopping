package com.arribas.myshoppinglist.ui.view.screen.shoplist.shoplistList

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.data.model.Shoplist
import com.arribas.myshoppinglist.data.repository.ShoplistArticleRepository
import com.arribas.myshoppinglist.data.repository.ShoplistRepository
import com.arribas.myshoppinglist.data.utils.CrudMessageEnum
import com.arribas.myshoppinglist.data.utils.CrudMessageManager
import com.arribas.myshoppinglist.data.utils.DIALOG_UI_TAG
import com.arribas.myshoppinglist.data.utils.DialogUiState
import com.arribas.myshoppinglist.data.utils.PreferencesEnum
import com.arribas.myshoppinglist.data.utils.PreferencesManager
import com.arribas.myshoppinglist.ui.view.filter.GeneralFilterUiState
import com.arribas.myshoppinglist.ui.view.screen.shoplist.ShoplistUiState
import com.arribas.myshoppinglist.ui.view.screen.shoplist.toItem
import com.arribas.myshoppinglist.ui.view.screen.shoplist.toShopListUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class ShoplistListViewModel(
    private val context: Context,
    private val shoplistRepository: ShoplistRepository,
    private val shoplistArticleRepository: ShoplistArticleRepository
): ViewModel() {

    private val _filterUiState = MutableStateFlow(GeneralFilterUiState())
    var filterUiState: StateFlow<GeneralFilterUiState> = _filterUiState

    private val _listUiState = MutableStateFlow(ShoplisListUiState())
    val listUiState: StateFlow<ShoplisListUiState> = _listUiState

    /**AlertDialog functions****************************************/
    private val _dialogState = MutableStateFlow(DialogUiState())
    val dialogState: StateFlow<DialogUiState> = _dialogState.asStateFlow()

    var shoplistUiState by mutableStateOf(ShoplistUiState())
        private set

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    init{
        getData()
    }

    fun onSearch(_name: String){
        _filterUiState.value = _filterUiState.value.copy(name = _name)

        getData()
    }

    fun onClearName(){
        _filterUiState.value = _filterUiState.value.copy(name = "")
        getData()
    }

    /**AlertDialog functions****************************************/
    fun onDialogDelete(shoplist: Shoplist){
        this.shoplistUiState = shoplist.toShopListUiState()

        openDialogClicked(
            tag = DIALOG_UI_TAG.TAG_DELETE
        )
    }

    fun onDialogConfirm() {
        when(_dialogState.value.tag) {
            DIALOG_UI_TAG.TAG_DELETE  -> deleteItem()
            else -> {}
        }

        _dialogState.value = DialogUiState(isShow = false)
    }

    fun onDialogDismiss() {
        _dialogState.value = DialogUiState(isShow = false)
    }

    /**Private functions************************************************/
    private fun openDialogClicked(tag: DIALOG_UI_TAG) {
        val _dialog: DialogUiState

        when(tag) {
            DIALOG_UI_TAG.TAG_DELETE ->
                _dialog = DialogUiState(
                    tag = tag,
                    title = context.getString(R.string.dialog_sure_title),
                    body = context.getString(R.string.dialog_sure_delete_description),
                    isShow = true,
                    isShowBtDismiss = true
                )
            else ->
                _dialog = DialogUiState()
        }

        _dialogState.value = _dialog
    }

    private fun deleteItem() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                shoplistArticleRepository.deleteByShoplist(shoplistUiState.id)
                shoplistRepository.deleteItem(shoplistUiState.toItem())

                val itemId = PreferencesManager(context)
                    .getData(PreferencesEnum.MAIN_LIST.toString(), "0")

                if(itemId.toInt() == shoplistUiState.id){
                    PreferencesManager(context)
                        .deleteData(PreferencesEnum.MAIN_LIST.toString())

                    PreferencesManager(context).deleteData(
                        PreferencesEnum.MAIN_LIST_NAME.toString())
                }

                sendMessage(CrudMessageManager(context)
                    .getMessage(CrudMessageEnum.DELETED_SUCCESS))

            }catch (e: Exception){
                sendMessage(CrudMessageManager(context)
                    .getMessage(CrudMessageEnum.DELETED_ERROR))
            }
        }
    }

    private fun getData(){
        viewModelScope.launch {
            shoplistRepository.getItemsByName(filterUiState.value.name).collect {
                list ->
                    _listUiState.value = ShoplisListUiState(
                        itemList = list,
                        itemCount = list.count()
                    )
            }
        }
    }

    /**Toast functions++++++*/
    private fun sendMessage(message: String) {
        viewModelScope.launch {
            _toastMessage.emit(message)
        }
    }
}

data class ShoplisListUiState(
    var itemList: List<Shoplist> = listOf(),
    val itemCount: Int = 0,
    val filterName: String = ""
)