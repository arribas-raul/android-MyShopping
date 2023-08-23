package com.arribas.myshoppinglist.ui.view.shoplistList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arribas.myshoppinglist.data.model.Article
import com.arribas.myshoppinglist.data.model.ArticleCategory
import com.arribas.myshoppinglist.data.model.ArticleShop
import com.arribas.myshoppinglist.data.model.QArticle
import com.arribas.myshoppinglist.data.repository.ArticleCategoryRepository
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
    private val articleShopRepository: ArticleShopRepository,
    private val articleCategoryRepository: ArticleCategoryRepository
): ViewModel() {

    private val _searchUiState = MutableStateFlow(SearchUiState())
    var searchUiState: StateFlow<SearchUiState> = _searchUiState

    private val _listUiState = MutableStateFlow(ListUiState())
    var listUiState: StateFlow<ListUiState> = _listUiState

    lateinit var article: QArticle

    init{
        getData()
    }

    private fun getData(){
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

    fun updateItem(qArticle: QArticle) {
        article = qArticle

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
            qArticle = article,
            tag = DIALOG_UI_TAG.TAG_DELETE
        )
    }

    /**Private functions***********************/
    private fun deleteItem() {
        viewModelScope.launch(Dispatchers.IO) {
            val articleCategorys = articleCategoryRepository.getByArticle(article.id)

            if(articleCategorys.isNotEmpty()){
                articleCategorys.forEach {
                    articleCategory->
                        articleCategoryRepository.deleteItem(
                            ArticleCategory(
                                id = articleCategory.id,
                                article_id = articleCategory.article_id,
                                category_id = articleCategory.category_id)
                        )
                }
            }

            articleRepository.deleteItem(qArticleToArticle(article))
        }
    }

    /**AlertDialog functions****************************************/
    private val _dialogState = MutableStateFlow(DialogUiState())
    val dialogState: StateFlow<DialogUiState> = _dialogState.asStateFlow()

    private fun onOpenDialogClicked(qArticle: QArticle? = null, tag: DIALOG_UI_TAG) {
        if(qArticle !== null) {
            article = qArticle
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

    fun qArticleToArticle(qArticle: QArticle): Article{
        return Article(
            id = qArticle.id,
            name = qArticle.name,
            shopCheked = qArticle.shopCheked)
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