package com.arribas.myshoppinglist.ui.view.screen.article.articleList

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arribas.myshoppinglist.data.model.Article
import com.arribas.myshoppinglist.data.model.ArticleCategory
import com.arribas.myshoppinglist.data.model.ArticleShop
import com.arribas.myshoppinglist.data.model.Category
import com.arribas.myshoppinglist.data.model.QArticle
import com.arribas.myshoppinglist.data.model.ShoplistArticle
import com.arribas.myshoppinglist.data.repository.ArticleCategoryRepository
import com.arribas.myshoppinglist.data.repository.ArticleRepository
import com.arribas.myshoppinglist.data.repository.ArticleShopRepository
import com.arribas.myshoppinglist.data.repository.ShoplistArticleRepository
import com.arribas.myshoppinglist.data.utils.DIALOG_UI_TAG
import com.arribas.myshoppinglist.data.utils.DialogUiState
import com.arribas.myshoppinglist.data.utils.PreferencesEnum
import com.arribas.myshoppinglist.data.utils.PreferencesManager
import com.arribas.myshoppinglist.data.utils.TextFieldDialogUiState
import com.arribas.myshoppinglist.ui.navigation.route.Routes
import com.arribas.myshoppinglist.ui.view.screen.category.ListCategoryUiState
import com.arribas.myshoppinglist.ui.view.screen.shoplist.ShoplistUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ListArticleViewModel(
    savedStateHandle: SavedStateHandle,
    private val context: Context,
    private val articleRepository: ArticleRepository,
    private val shoplistArticleRepository: ShoplistArticleRepository,
    private val articleCategoryRepository: ArticleCategoryRepository
): ViewModel() {

    private var itemId: String? = savedStateHandle[Routes.itemIdArg]

    private val _searchUiState = MutableStateFlow(SearchUiState())
    var searchUiState: StateFlow<SearchUiState> = _searchUiState

    private val _listUiState = MutableStateFlow(ListUiState())
    var listUiState: StateFlow<ListUiState> = _listUiState

    lateinit var article: QArticle

    var shoplistUiState by mutableStateOf(ShoplistUiState())
        private set

    init{

        getData()
    }

    private fun getData(){
        try {
            if(itemId == null) {
                itemId = PreferencesManager(context)
                    .getData(PreferencesEnum.MAIN_LIST.toString(), "0")
            }

            if(itemId.isNullOrEmpty()){
                shoplistUiState.copy(id = 0)

            }else {
                shoplistUiState = shoplistUiState.copy(id = itemId!!.toInt())

                viewModelScope.launch {
                    if(_searchUiState.value.category == 0){
                        articleRepository.getItemsByName(
                            shoplistUiState.id,
                            _searchUiState.value.name
                        )
                            .collect { list ->
                                _listUiState.value = ListUiState(
                                    itemList = list,
                                    itemCount = list.count(),
                                    itemSelectCount = list.count { it.shopCheked }
                                )
                            }

                    }else{
                        articleRepository.getItemsByFilter(
                            shoplistUiState.id,
                            _searchUiState.value.name,
                            _searchUiState.value.category
                        )
                            .collect { list ->
                                _listUiState.value = ListUiState(
                                    itemList = list,
                                    itemCount = list.count(),
                                    itemSelectCount = list.count { it.shopCheked }
                                )
                            }
                    }

                }
            }
        }catch (e: IllegalArgumentException){

        }
    }

    fun updateItem(qArticle: QArticle) {
        if(!shoplistUiState.existElement()){
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            if(qArticle.shoplist_id > 0){
                val shoplistArticle = shoplistArticleRepository.getItemByListAndArticle(
                    qArticle.shoplist_id, qArticle.id
                )

                if(shoplistArticle.first() != null) {
                    shoplistArticleRepository.deleteItem(shoplistArticle.first()!!)
                }

            }else{
                val count: Int = shoplistArticleRepository.count(shoplistUiState.id)

                val shoplistArticle = ShoplistArticle(
                    id = 0,
                    article_id = qArticle.id,
                    shoplist_id = shoplistUiState.id,
                    name = qArticle.name,
                    order = count + 1
                )

                shoplistArticleRepository.insertItem(shoplistArticle)
            }
        }
    }

    fun searchByCategory(_category: Int){
        _searchUiState.value.category = _category
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
    val name: String = "",
    var category: Int = 0
)

