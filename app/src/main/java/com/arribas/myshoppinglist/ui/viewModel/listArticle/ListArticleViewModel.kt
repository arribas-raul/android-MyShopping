package com.arribas.myshoppinglist.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arribas.myshoppinglist.data.model.Article
import com.arribas.myshoppinglist.data.model.ArticleShop
import com.arribas.myshoppinglist.data.repository.ArticleRepository
import com.arribas.myshoppinglist.data.repository.ArticleShopRepository
import com.arribas.myshoppinglist.ui.viewModel.listArticleShop.DIALOG_UI_TAG
import com.arribas.myshoppinglist.ui.viewModel.listArticleShop.DialogUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListArticleViewModel(
    private val articleRepository: ArticleRepository,
    private val articleShopRepository: ArticleShopRepository): ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val listUiState: StateFlow<ListUiState> =
        articleRepository.getAllItems().map { list ->
            ListUiState(
                    itemList = list,
                    itemCount = list.count(),
                    itemSelectCount = list.count { it.shopCheked }
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ListUiState()
            )

    lateinit var article: Article

    fun updateItem(article: Article) {
        viewModelScope.launch {
            articleRepository.updateItem(article)

            val items = articleShopRepository.getAllItems()

            val count = items?.let { _items->
                _items.count()
            }

            val articleShop = ArticleShop(
                name = article.name,
                order = count ?: 1
            )

            articleShopRepository.insertItem(articleShop)
        }
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
    val itemList: List<Article> = listOf(),
    val itemCount: Int = 0,
    val itemSelectCount: Int = 0
)