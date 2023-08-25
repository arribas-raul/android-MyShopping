package com.arribas.myshoppinglist.ui.view.shoplist.shoplistList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arribas.myshoppinglist.data.model.ArticleCategory
import com.arribas.myshoppinglist.data.model.QArticle
import com.arribas.myshoppinglist.data.model.Shoplist
import com.arribas.myshoppinglist.data.repository.ShoplistRepository
import com.arribas.myshoppinglist.data.utils.DIALOG_UI_TAG
import com.arribas.myshoppinglist.data.utils.DialogUiState
import com.arribas.myshoppinglist.ui.view.shoplist.ShoplistUiState
import com.arribas.myshoppinglist.ui.view.shoplist.toItem
import com.arribas.myshoppinglist.ui.view.shoplistList.SearchUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class ShoplistListViewModel(
    private val shoplistRepository: ShoplistRepository
): ViewModel() {

    private val _searchUiState = MutableStateFlow(SearchUiState())
    var searchUiState: StateFlow<SearchUiState> = _searchUiState

    private val _listUiState = MutableStateFlow(ShoplisListUiState())
    val listUiState: StateFlow<ShoplisListUiState> = _listUiState

    /**AlertDialog functions****************************************/
    private val _dialogState = MutableStateFlow(DialogUiState())
    val dialogState: StateFlow<DialogUiState> = _dialogState.asStateFlow()

    lateinit var shoplist: Shoplist

    var shoplistUiState by mutableStateOf(ShoplistUiState())
        private set

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    fun sendMessage(message: String) {
        viewModelScope.launch {
            _toastMessage.emit(message)
        }
    }

    init{
        getData()
    }

    fun search(_name: String){
        _searchUiState.value = _searchUiState.value.copy(name = _name)
        getData()
    }

    fun clearName(){
        _searchUiState.value = _searchUiState.value.copy(name = "")
        getData()
    }

    fun onDialogDelete(shoplist: Shoplist){
        onOpenDialogClicked(
            shoplist = shoplist,
            tag = DIALOG_UI_TAG.TAG_DELETE
        )
    }

    fun onUpdateItem(shoplist: Shoplist){
        this.shoplistUiState.copy(id = shoplist.id, name = shoplist.name, type = shoplist.type)
    }

    fun updateItem(shoplist: Shoplist) {
        /*article = qArticle

        viewModelScope.launch(Dispatchers.IO) {
            //println("article: $article")
            if(qArticle.shopCheked){
                val articleShop = articleShopRepository.getItemByName(qArticle.name)

                if(articleShop.first() != null) {
                    articleShopRepository.deleteItem(articleShop.first()!!)
                }

            }else{
                val count: Int = articleShopRepository.count()

                val articleShop = ArticleShop(
                    name = qArticle.name,
                    order = count + 1
                )

                articleShopRepository.insertItem(articleShop)
            }

            articleRepository.updateItem(
                qArticleToArticle(
                    article.copy(shopCheked = !article.shopCheked)))
        }*/
    }

    private fun onOpenDialogClicked(shoplist: Shoplist? = null, tag: DIALOG_UI_TAG) {
        if(shoplist !== null) {
            this.shoplist = shoplist
        }

        val _dialog: DialogUiState

        when(tag) {
            DIALOG_UI_TAG.TAG_DELETE ->
                _dialog = DialogUiState(
                    tag = tag,
                    title = "¿Seguro que quieres continuar",
                    body = "Si continuas se eliminará el item de la lista",
                    isShow = true,
                    isShowBtDismiss = true
                )
            else ->
                _dialog = DialogUiState()
        }

        _dialogState.value = _dialog
    }

    private fun deleteItem() {
        if(shoplist == null){
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            //val articleCategorys = articleCategoryRepository.getByArticle(article.id)
            //TODO:: Delete articles to list

            try {
                shoplistRepository.deleteItem(shoplist!!)

                sendMessage("Item borrado correctamente")

            }catch (e: Exception){
                sendMessage("Se ha producido un error al borrar el item")
            }
        }
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

    private fun getData(){
        viewModelScope.launch {
            shoplistRepository.getItemsByName(searchUiState.value.name).collect {
                list ->
                    _listUiState.value = ShoplisListUiState(
                        itemList = list,
                        itemCount = list.count()
                    )
            }
        }
    }
}

data class ShoplisListUiState(
    var itemList: List<Shoplist> = listOf(),
    val itemCount: Int = 0,
    val filterName: String = ""
)

data class ShoplistFilter(
    val name: String = ""
)