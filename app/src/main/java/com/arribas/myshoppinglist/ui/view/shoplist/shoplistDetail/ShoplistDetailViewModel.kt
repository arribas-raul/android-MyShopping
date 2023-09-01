package com.arribas.myshoppinglist.ui.view.shoplist.shoplistDetail

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arribas.myshoppinglist.data.model.ArticleShop
import com.arribas.myshoppinglist.data.repository.ShoplistArticleRepository
import com.arribas.myshoppinglist.data.repository.ShoplistRepository
import com.arribas.myshoppinglist.data.utils.DIALOG_UI_TAG
import com.arribas.myshoppinglist.data.utils.DialogUiState
import com.arribas.myshoppinglist.data.utils.PreferencesEnum
import com.arribas.myshoppinglist.data.utils.PreferencesManager
import com.arribas.myshoppinglist.ui.navigation.route.Routes
import com.arribas.myshoppinglist.ui.view.general.filter.GeneralFilterUiState
import com.arribas.myshoppinglist.ui.view.shoplist.ShoplistUiState
import com.arribas.myshoppinglist.ui.view.shoplist.toShopListUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ShoplistDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val context: Context,
    private val shoplistRepository: ShoplistRepository,
    private val shoplistArticleRepository: ShoplistArticleRepository
) : ViewModel() {

    //private val itemId: Int = checkNotNull(savedStateHandle[Routes.ShoplistDetailScreen.itemIdArg])
    private var itemId: Int = savedStateHandle[Routes.ShoplistDetailScreen.itemIdArg] ?: 0

    var shoplistUiState by mutableStateOf(ShoplistUiState())
        private set

    private val _dialogState = MutableStateFlow(DialogUiState())
    val dialogState: StateFlow<DialogUiState> = _dialogState

    private val _listUiState = MutableStateFlow(ShoplistDetailUiState())
    var listUiState: StateFlow<ShoplistDetailUiState> = _listUiState

    private val _filterUiState = MutableStateFlow(GeneralFilterUiState())
    var filterUiState: StateFlow<GeneralFilterUiState> = _filterUiState

    lateinit var article: ArticleShop

    init{
        getData()
    }

    /**Public functions**************************************/
    fun onUpdateItem(_article: ArticleShop) {
        article = _article

        if(article.quantity == 0){
            //onOpenDialogClicked(tag = DIALOG_UI_TAG.TAG_DELETE)

        }else{
            updateItem()
        }
    }

    fun onReorderItems(startIndex: Int, endIndex: Int){
        val articleTo = listUiState.value.itemList.get(startIndex)
        val articleFrom = listUiState.value.itemList.get(endIndex)
    /*
        viewModelScope.launch(Dispatchers.IO) {
            articleShopRepository.updateItem(articleTo.copy(order = articleFrom.order))
            articleShopRepository.updateItem(articleFrom.copy(order = articleTo.order))
        }
    */
    }

    fun onSearch(_name: String){
        _filterUiState.value = _filterUiState.value.copy(name = _name)

        getData()
    }

    fun onClearName(){
        _filterUiState.value = _filterUiState.value.copy(name = "")
        getData()
    }

    fun onDialogDelete(_article: ArticleShop){
        article = _article

        //onOpenDialogClicked(tag = DIALOG_UI_TAG.TAG_DELETE)
    }

    fun onDialogReset(){
        //onOpenDialogClicked(tag = DIALOG_UI_TAG.TAG_RESET)
    }

    /**Private functions********************************************/
    private fun getData() {
        viewModelScope.launch{
            try {
                if(itemId == 0){
                    itemId = PreferencesManager(context)
                        .getData(PreferencesEnum.MAIN_LIST.toString(), "0")
                        .toInt()
                }

                if(itemId == 0){
                    //TODO: Mostrar mensaje de que no hay lista
                }else {
                    viewModelScope.launch(Dispatchers.IO) {
                        delay(100)
                        shoplistUiState = shoplistRepository.getItem(itemId)
                            .filterNotNull()
                            .first()
                            .toShopListUiState()

                        val shoplistArticles = shoplistArticleRepository.getItemsByList(itemId)

                        if (shoplistArticles.count() > 0) {
                            /*articleUiState =
                                articleUiState.copy(category = articleCategorys[0].category_id)*/
                        }
                    }
                }

            }catch (e: IllegalArgumentException){

            }
            /*articleShopRepository.getAllItems()
                .collect { list ->
                    _listUiState.value = ListArticleShopUiState(
                        itemList = list,
                        itemCount = list.count(),
                        itemSelectCount = list.count { it.check }
                    )
                }*/
        }
    }

    private fun reset(){
        viewModelScope.launch(Dispatchers.IO) {
           // articleShopRepository.reset()
        }
    }

    private fun updateItem(){
        viewModelScope.launch {
          //  articleShopRepository.updateItem(article)
        }
    }

    private fun deleteItem() {
        viewModelScope.launch {
         /*   articleShopRepository.deleteItem(article)

            val article = articleRepository.getItemByName(article.name);

            article.first()?.let{ _article->
                val article = _article.copy(
                    shopCheked = false
                )

                articleRepository.updateItem(article)
            }

            updateOrderItems()*/
        }
    }

    private fun updateOrderItems(){
        viewModelScope.launch(Dispatchers.IO) {
            var count = 1;

            listUiState.value.itemList.forEach { article ->
               // articleShopRepository.updateItem(article.copy(order = count++))
            }
        }
    }

    /**AlertDialog functions****************************************/
    fun onDialogConfirm() {
       /* when(_dialogState.value.tag) {
            DIALOG_UI_TAG.TAG_DELETE  -> deleteItem()
            DIALOG_UI_TAG.TAG_RESET   -> reset()
            DIALOG_UI_TAG.TAG_SUCCESS -> onDialogDismiss()

            else -> {}
        }

        _dialogState.value = DialogUiState(
            isShow = false)*/
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

data class ShoplistDetailUiState(
    var itemList: List<ArticleShop> = listOf(),
    val itemCount: Int = 0,
    val itemSelectCount: Int = 0
){
    fun getTotalItemText(): String {
        return "Total Items ${itemCount} - Select Items ${itemSelectCount}"
    }
}/*
data class ListArticleShopUiState(
    var itemList: List<ArticleShop> = listOf(),
    val itemCount: Int = 0,
    val itemSelectCount: Int = 0
)

data class SearchListArticleUiState(
    val check: Boolean = false
)
*/
