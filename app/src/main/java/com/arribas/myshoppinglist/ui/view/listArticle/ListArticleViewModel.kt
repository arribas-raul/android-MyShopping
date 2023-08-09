package com.arribas.myshoppinglist.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arribas.myshoppinglist.data.MainTag
import com.arribas.myshoppinglist.data.model.Article
import com.arribas.myshoppinglist.data.model.ArticleShop
import com.arribas.myshoppinglist.data.repository.ArticleRepository
import com.arribas.myshoppinglist.data.repository.ArticleShopRepository
import com.arribas.myshoppinglist.data.utils.DIALOG_UI_TAG
import com.arribas.myshoppinglist.data.utils.DialogUiState
import com.arribas.myshoppinglist.ui.view.listArticle.ArticleUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListArticleViewModel(
    private val articleRepository: ArticleRepository,
    private val articleShopRepository: ArticleShopRepository): ViewModel() {

    private val _searchUiState = MutableStateFlow(SearchUiState())
    var searchUiState: StateFlow<SearchUiState> = _searchUiState



    private val _listUiState = MutableStateFlow(ListUiState())
    var listUiState: StateFlow<ListUiState> = _listUiState

    lateinit var article: Article

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

    fun updateItem(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            //println("article: $article")
            if(article.shopCheked){
                val articleShop = articleShopRepository.getItemByName(article.name)

                if(articleShop.first() != null) {
                    articleShopRepository.deleteItem(articleShop.first()!!)
                }

            }else{
                val count: Int = articleShopRepository.count()

                val articleShop = ArticleShop(
                    name = article.name,
                    order = count + 1
                )

                articleShopRepository.insertItem(articleShop)
            }

            articleRepository.updateItem(article.copy(shopCheked = !article.shopCheked))
        }
    }

    fun search(_name: String){

        println("article $_name")
        _searchUiState.value = _searchUiState.value.copy(name = _name)
        getData()
    }

    fun clearName(){
        println("article clearName")
        _searchUiState.value = _searchUiState.value.copy(name = "")
        getData()
    }

    fun onDialogDelete(article: Article){
        onOpenDialogClicked(
            _article = article,
            tag = DIALOG_UI_TAG.TAG_DELETE
        )
    }

    /**Private functions***********************/
    private fun deleteItem() {
        viewModelScope.launch {
            articleRepository.deleteItem(article)
        }
    }

    /**AlertDialog functions****************************************/
    private val _dialogState = MutableStateFlow(DialogUiState())
    val dialogState: StateFlow<DialogUiState> = _dialogState.asStateFlow()

    fun onOpenDialogClicked(_article: Article? = null, tag: DIALOG_UI_TAG) {
        if(_article !== null) {
            article = _article
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
    var itemList: List<Article> = listOf(),
    val itemCount: Int = 0,
    val itemSelectCount: Int = 0,
    val searchName: String = ""
)

data class SearchUiState(
    val name: String = ""
)