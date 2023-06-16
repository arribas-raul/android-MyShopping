package com.arribas.myshoppinglist.ui.viewModel.listArticleShop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arribas.myshoppinglist.data.model.Article
import com.arribas.myshoppinglist.data.model.ArticleShop
import com.arribas.myshoppinglist.data.repository.ArticleRepository
import com.arribas.myshoppinglist.data.repository.ArticleShopRepository
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
        articleShopRepository.getAllItems().map { ListShopUiState(it) }
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

    fun deleteItem() {
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
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    fun onOpenDialogClicked(_article: ArticleShop) {
        article = _article
        _showDialog.value = true
    }

    fun onDialogConfirm() {
        _showDialog.value = false
        deleteItem()
    }

    fun onDialogDismiss() {
        _showDialog.value = false
    }
}

data class ListShopUiState(val itemList: List<ArticleShop> = listOf())