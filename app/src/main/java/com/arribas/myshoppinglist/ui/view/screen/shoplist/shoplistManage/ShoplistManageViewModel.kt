package com.arribas.myshoppinglist.ui.view.screen.shoplist.shoplistManage

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.data.model.ShoplistArticle
import com.arribas.myshoppinglist.data.repository.ShoplistArticleRepository
import com.arribas.myshoppinglist.data.repository.ShoplistRepository
import com.arribas.myshoppinglist.data.utils.DIALOG_UI_TAG
import com.arribas.myshoppinglist.data.utils.DialogUiState
import com.arribas.myshoppinglist.data.utils.PreferencesEnum
import com.arribas.myshoppinglist.data.utils.PreferencesManager
import com.arribas.myshoppinglist.ui.view.filter.GeneralFilterUiState
import com.arribas.myshoppinglist.ui.view.screen.shoplist.ShoplistUiState
import com.arribas.myshoppinglist.ui.view.screen.shoplist.shoplistList.ShoplisListUiState
import com.arribas.myshoppinglist.ui.view.screen.shoplist.toShopListUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ShoplistManageViewModel(
    private val context: Context,
    private val shoplistRepository: ShoplistRepository,
    private val shoplistArticleRepository: ShoplistArticleRepository
) : ViewModel() {

    private var itemId: String? = null

    var shoplistUiState by mutableStateOf(ShoplistUiState())
        private set

    private val _dialogState = MutableStateFlow(DialogUiState())
    val dialogState: StateFlow<DialogUiState> = _dialogState

    private val _listUiState = MutableStateFlow(ShoplistManageUiState(context))
    var listUiState: StateFlow<ShoplistManageUiState> = _listUiState

    lateinit var article: ShoplistArticle

    init{
        getData()
    }

    /**Public functions**************************************/
    fun onUpdateItem(_article: ShoplistArticle) {
        article = _article

        if(article.quantity == 0){
            onOpenDialogClicked(tag = DIALOG_UI_TAG.TAG_DELETE)

        }else{
            updateItem()
        }
    }

    fun onReorderItems(startIndex: Int, endIndex: Int){
        val articleTo = listUiState.value.itemList.get(startIndex)
        val articleFrom = listUiState.value.itemList.get(endIndex)

        viewModelScope.launch(Dispatchers.IO) {
            shoplistArticleRepository.updateItem(articleTo.copy(order = articleFrom.order))
            shoplistArticleRepository.updateItem(articleFrom.copy(order = articleTo.order))
        }
    }

    fun onDialogDelete(_article: ShoplistArticle){
        article = _article

        onOpenDialogClicked(tag = DIALOG_UI_TAG.TAG_DELETE)
    }

    fun onDialogReset(){
        onOpenDialogClicked(tag = DIALOG_UI_TAG.TAG_RESET)
    }

    fun setShoplist(element: ShoplistUiState){
        shoplistUiState = shoplistUiState.copy(
            id = element.id,
            name = element.name,
            type = element.type
        )

        PreferencesManager(context).saveData(
            PreferencesEnum.MAIN_LIST.toString(),
            this.shoplistUiState.id.toString()
        )

        PreferencesManager(context).saveData(
            PreferencesEnum.MAIN_LIST_NAME.toString(),
            this.shoplistUiState.name
        )

        getData()
    }

    /**Private functions********************************************/
    private fun  getData() {
        viewModelScope.launch{
            delay(100)
            try {
                itemId = PreferencesManager(context)
                    .getData(PreferencesEnum.MAIN_LIST.toString(), "0")

                if(itemId.isNullOrEmpty()){
                    shoplistUiState.copy(id = 0)

                }else {
                    viewModelScope.launch(Dispatchers.IO) {
                        delay(100)
                        shoplistUiState = shoplistRepository.getItem(itemId!!.toInt())
                            .filterNotNull()
                            .first()
                            .toShopListUiState()

                        shoplistArticleRepository.getItemsByList(itemId!!.toInt())
                            .collect { list ->
                                _listUiState.value = ShoplistManageUiState(
                                    context = context,
                                    itemList = list,
                                    itemCount = list.count(),
                                    itemSelectCount = list.count { it.check }
                                )
                            }
                    }
                }

            }catch (e: IllegalArgumentException){

            }
        }
    }

    private fun reset(){
        viewModelScope.launch(Dispatchers.IO) {
            shoplistArticleRepository.reset(shoplistUiState.id)
        }
    }

    private fun updateItem(){
        viewModelScope.launch {
            shoplistArticleRepository.updateItem(article)
        }
    }

    private fun deleteItem() {
        viewModelScope.launch {
            shoplistArticleRepository.deleteItem(article)

            updateOrderItems()
        }
    }

    private fun updateOrderItems(){
        viewModelScope.launch(Dispatchers.IO) {
            var count = 1;

            listUiState.value.itemList.forEach { article ->
                shoplistArticleRepository.updateItem(article.copy(order = count++))
            }
        }
    }

    /**AlertDialog functions****************************************/
    fun onDialogConfirm() {
        when(_dialogState.value.tag) {
            DIALOG_UI_TAG.TAG_DELETE  -> deleteItem()
            DIALOG_UI_TAG.TAG_RESET   -> reset()
            DIALOG_UI_TAG.TAG_SUCCESS -> onDialogDismiss()

            else -> {}
        }

        _dialogState.value = DialogUiState(
            isShow = false)
    }

    fun onDialogDismiss() {
        when(_dialogState.value.tag) {
            DIALOG_UI_TAG.TAG_DELETE -> {
                if(article.quantity == 0){
                    article.copy(quantity = 1)
                }
            }
            else -> {}
        }

        _dialogState.value = DialogUiState(isShow = false)
    }

    private fun onOpenDialogClicked(tag: DIALOG_UI_TAG) {
        val _dialog: DialogUiState

        when(tag) {
            DIALOG_UI_TAG.TAG_DELETE ->
                _dialog = DialogUiState(
                    tag = tag,
                    title = "¿Seguro que quieres continuar",
                    body = "Si continuas se eliminará el artículo de la lista",
                    isShow = true,
                    isShowBtDismiss = true
                )

            DIALOG_UI_TAG.TAG_RESET  ->
                _dialog = DialogUiState(
                    tag = tag,
                    title = "¿Seguro que quieres continuar",
                    body = "Si continuas se desmarcarán todos los artículos",
                    isShow = true,
                    isShowBtDismiss = true
                )

            DIALOG_UI_TAG.TAG_SUCCESS ->
                _dialog = DialogUiState(
                    tag = tag,
                    title = "Compra finalizada",
                    body = "Se han marcado todos los artículos",
                    isShow = true,
                    isShowBtDismiss = false
                )

            else -> _dialog = DialogUiState()
        }

        _dialogState.value = _dialog
    }
}

data class ShoplistManageUiState(
    var context: Context,
    var itemList: List<ShoplistArticle> = listOf(),
    val itemCount: Int = 0,
    val itemSelectCount: Int = 0
){
    fun getTotalItemText(): String {
        return context.getString(R.string.managelist_header_items,  itemCount,  itemSelectCount)
    }
}
