package com.arribas.myshoppinglist.ui.viewModel.listArticleShop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arribas.myshoppinglist.data.model.ArticleShop
import com.arribas.myshoppinglist.data.repository.ArticleRepository
import com.arribas.myshoppinglist.data.repository.ArticleShopRepository
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

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val listUiState: StateFlow<ListShopUiState> =
        articleShopRepository.getAllItems().map { list ->
            ListShopUiState(
                itemList = list,
                itemCount = list.count(),
                itemSelectCount = list.count { it.check }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ListShopUiState()
        )

    lateinit var article: ArticleShop

    fun updateItem(article: ArticleShop) {
        viewModelScope.launch {
            articleShopRepository.updateItem(article)
        }
    }

    fun onDialogDelete(article: ArticleShop){
        onOpenDialogClicked(_article = article, tag = DIALOG_UI_TAG.TAG_DELETE)
    }

    fun onDialogReset(){
        onOpenDialogClicked(tag = DIALOG_UI_TAG.TAG_RESET)
    }

    /**Private functions********************************************/
    private fun reset(){
        viewModelScope.launch(Dispatchers.IO) {
            articleShopRepository.reset()
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
        }
    }

    /**AlertDialog functions****************************************/
    private val _dialogState = MutableStateFlow(DialogUiState())
    val dialogState: StateFlow<DialogUiState> = _dialogState.asStateFlow()

    fun onOpenDialogClicked(_article: ArticleShop? = null, tag: DIALOG_UI_TAG) {
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
        }

        _dialogState.value = _dialog
    }

    fun onDialogConfirm() {
        when(_dialogState.value.tag) {
            DIALOG_UI_TAG.TAG_DELETE  -> deleteItem()
            DIALOG_UI_TAG.TAG_RESET   -> reset()
            DIALOG_UI_TAG.TAG_SUCCESS -> onDialogDismiss()
        }

        _dialogState.value = DialogUiState(
            isShow = false)
    }

    fun onDialogDismiss() {
        _dialogState.value = DialogUiState(isShow = false)
    }
}

/*
* title = "Please confirm",
        body =  "Should I continue with the requested action?",
* */

data class ListShopUiState(
    val itemList: List<ArticleShop> = listOf(),
    val itemCount: Int = 0,
    val itemSelectCount: Int = 0
)

data class DialogUiState(
    val tag: DIALOG_UI_TAG = DIALOG_UI_TAG.TAG_RESET,
    val title: String = "",
    val body: String = "",
    var isShow: Boolean = false,
    val isShowBtDismiss: Boolean = false
)

enum class DIALOG_UI_TAG{
    TAG_DELETE, TAG_RESET, TAG_SUCCESS
}