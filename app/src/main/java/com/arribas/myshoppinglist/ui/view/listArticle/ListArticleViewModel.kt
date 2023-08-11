package com.arribas.myshoppinglist.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arribas.myshoppinglist.data.model.Article
import com.arribas.myshoppinglist.data.model.ArticleShop
import com.arribas.myshoppinglist.data.model.QArticle
import com.arribas.myshoppinglist.data.repository.ArticleRepository
import com.arribas.myshoppinglist.data.repository.ArticleShopRepository
import com.arribas.myshoppinglist.data.utils.DIALOG_UI_TAG
import com.arribas.myshoppinglist.data.utils.DialogUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ListArticleViewModel(
    private val articleRepository: ArticleRepository,
    private val articleShopRepository: ArticleShopRepository): ViewModel() {

    private val _searchUiState = MutableStateFlow(SearchUiState())
    var searchUiState: StateFlow<SearchUiState> = _searchUiState

    private val _listUiState = MutableStateFlow(ListUiState())
    var listUiState: StateFlow<ListUiState> = _listUiState

    lateinit var article: QArticle

    init{
        getData()
    }

    fun getData(){
        viewModelScope.launch{
            articleRepository.getItemsByName(_searchUiState.value.name).collect { list ->
                _listUiState.value = ListUiState(
                    itemList = list,
                    itemCount = list.count(),
                    itemSelectCount = list.count { it.shopCheked }
                )
            }
        }
    }

    fun updateItem(_Q_article: QArticle) {
        viewModelScope.launch(Dispatchers.IO) {
            //println("article: $article")
            if(_Q_article.shopCheked){
                val articleShop = articleShopRepository.getItemByName(_Q_article.name)

                if(articleShop.first() != null) {
                    articleShopRepository.deleteItem(articleShop.first()!!)
                }

            }else{
                val count: Int = articleShopRepository.count()

                val articleShop = ArticleShop(
                    name = _Q_article.name,
                    order = count + 1
                )

                articleShopRepository.insertItem(articleShop)
            }

            val article = Article(
                id = article.id,
                name = article.name,
                shopCheked = !article.shopCheked)

            articleRepository.updateItem(article)
        }
    }

    fun search(_name: String){
        //println("article $_name")
        _searchUiState.value = _searchUiState.value.copy(name = _name)
        getData()
    }

    fun clearName(){
        println("article clearName")
        _searchUiState.value = _searchUiState.value.copy(name = "")
        getData()
    }

    fun onDialogDelete(article: QArticle){
        onOpenDialogClicked(
            _Q_article = article,
            tag = DIALOG_UI_TAG.TAG_DELETE
        )
    }

    /**Private functions***********************/
    private fun deleteItem() {
        viewModelScope.launch {

            val article = Article(
                id = article.id,
                name = article.name,
                shopCheked = !article.shopCheked)

            articleRepository.deleteItem(article)
        }
    }

    /**AlertDialog functions****************************************/
    private val _dialogState = MutableStateFlow(DialogUiState())
    val dialogState: StateFlow<DialogUiState> = _dialogState.asStateFlow()

    fun onOpenDialogClicked(_Q_article: QArticle? = null, tag: DIALOG_UI_TAG) {
        if(_Q_article !== null) {
            article = _Q_article
        }

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
            else ->
                _dialog = DialogUiState()
        }

        _dialogState.value = _dialog
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
}

data class ListUiState(
    var itemList: List<QArticle> = listOf(),
    val itemCount: Int = 0,
    val itemSelectCount: Int = 0,
    val searchName: String = ""
)

data class SearchUiState(
    val name: String = ""
)