package com.arribas.myshoppinglist.ui.viewModel.listArticleShop

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arribas.myshoppinglist.data.model.ArticleShop
import com.arribas.myshoppinglist.data.repository.ArticleRepository
import com.arribas.myshoppinglist.data.repository.ArticleShopRepository
import com.arribas.myshoppinglist.data.utils.DIALOG_UI_TAG
import com.arribas.myshoppinglist.data.utils.DialogUiState
import com.arribas.myshoppinglist.ui.viewModel.ListUiState
import com.arribas.myshoppinglist.ui.viewModel.SearchUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListArticleShopViewModel(
    private val articleRepository: ArticleRepository,
    private val articleShopRepository: ArticleShopRepository): ViewModel() {

    private val _dialogState = MutableStateFlow(DialogUiState())
    val dialogState: StateFlow<DialogUiState> = _dialogState

    //var listUiState: StateFlow<ListArticleShopUiState> = getData()

    private val _listUiState = MutableStateFlow(ListArticleShopUiState())
    var listUiState: StateFlow<ListArticleShopUiState> = _listUiState

    private val _searchListArticleUiState = MutableStateFlow(SearchListArticleUiState())
    var searchListArticleUiState: StateFlow<SearchListArticleUiState> = _searchListArticleUiState

    lateinit var article: ArticleShop

    init{
        getData()
    }

    /**Public functions**************************************/
    fun onUpdateItem(_article: ArticleShop) {
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
            articleShopRepository.updateItem(articleTo.copy(order = articleFrom.order))
            articleShopRepository.updateItem(articleFrom.copy(order = articleTo.order))
        }
    }

    fun onCheckFilter(_searchUiFilter: SearchListArticleUiState){
        //_searchListArticleUiState = _searchListArticleUiState.copy(check = _searchUiFilter.check)
        _searchListArticleUiState.value = _searchUiFilter

        getData()
    }

    fun onDialogDelete(_article: ArticleShop){
        article = _article

        onOpenDialogClicked(tag = DIALOG_UI_TAG.TAG_DELETE)
    }

    fun onDialogReset(){
        onOpenDialogClicked(tag = DIALOG_UI_TAG.TAG_RESET)
    }

    /**Private functions********************************************/
    private fun getData() {
        viewModelScope.launch{
            articleShopRepository.getAllItems()
                .collect { list ->
                    _listUiState.value = ListArticleShopUiState(
                        itemList = list,
                        itemCount = list.count(),
                        itemSelectCount = list.count { it.check }
                    )
                }
        }
    }

    private fun reset(){
        viewModelScope.launch(Dispatchers.IO) {
            articleShopRepository.reset()
        }
    }

    private fun updateItem(){
        viewModelScope.launch {
            articleShopRepository.updateItem(article)
        }
    }

    private fun deleteItem() {
        viewModelScope.launch {
            articleShopRepository.deleteItem(article)

            val article = articleRepository.getItemByName(article.name);

            article.first()?.let{ _article->
                val article = _article.copy(
                    shopCheked = false
                )

                articleRepository.updateItem(article)
            }

            updateOrderItems()
        }
    }

    private fun updateOrderItems(){
        viewModelScope.launch(Dispatchers.IO) {
            var count = 1;

            listUiState.value.itemList.forEach { article ->
                articleShopRepository.updateItem(article.copy(order = count++))
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

data class ListArticleShopUiState(
    var itemList: List<ArticleShop> = listOf(),
    val itemCount: Int = 0,
    val itemSelectCount: Int = 0
)

data class SearchListArticleUiState(
    val check: Boolean = false
)

