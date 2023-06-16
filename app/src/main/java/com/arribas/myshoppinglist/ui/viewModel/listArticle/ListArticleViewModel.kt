package com.arribas.myshoppinglist.ui.viewModel

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
        articleRepository.getAllItems().map { ListUiState(it) }
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
/*
            items?.let { _items->
                val count = _items.count()
            }
*/
            var count = 0;

            val articleShop = ArticleShop(
                name = article.name,
                order = count
            )

            articleShopRepository.insertItem(articleShop)
        }
    }

    fun deleteItem() {
        viewModelScope.launch {
            articleRepository.deleteItem(article)
        }
    }

    /**AlertDialog functions****************************************/
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    fun onOpenDialogClicked(_article: Article) {
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

data class ListUiState(val itemList: List<Article> = listOf())