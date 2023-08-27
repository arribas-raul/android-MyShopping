package com.arribas.myshoppinglist.ui.view.article.articleDetail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arribas.myshoppinglist.data.model.ArticleCategory
import com.arribas.myshoppinglist.data.repository.ArticleCategoryRepository
import com.arribas.myshoppinglist.data.repository.ArticleRepository
import com.arribas.myshoppinglist.data.utils.DIALOG_UI_TAG
import com.arribas.myshoppinglist.data.utils.DialogUiState
import com.arribas.myshoppinglist.ui.navigation.route.Routes
import com.arribas.myshoppinglist.ui.view.app.AppObject
import com.arribas.myshoppinglist.ui.view.app.AppUiState
import com.arribas.myshoppinglist.ui.view.article.articleList.ArticleUiState
import com.arribas.myshoppinglist.ui.view.article.articleList.isValid
import com.arribas.myshoppinglist.ui.view.article.articleList.toArticleUiState
import com.arribas.myshoppinglist.ui.view.article.articleList.toItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * View Model to validate and insert items in the Room database.
 */
class ArticleDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val articleRepository: ArticleRepository,
    private val articleCategoryRepository: ArticleCategoryRepository
    ) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle[Routes.ArticleDetailScreen.itemIdArg])

    var articleUiState by mutableStateOf(ArticleUiState())
        private set

    init {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                delay(100)
                articleUiState = articleRepository.getItem(itemId)
                    .filterNotNull()
                    .first()
                    .toArticleUiState(actionEnabled = true)

                val articleCategorys = articleCategoryRepository.getByArticle(itemId)

                if(articleCategorys.isNotEmpty()){
                    articleUiState = articleUiState.copy(category = articleCategorys[0].category_id)
                }
            }

        }catch (e: IllegalArgumentException){

        }
    }

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */

    fun updateUiState(newItemUiState: ArticleUiState) {
        articleUiState = newItemUiState.copy(
            name = newItemUiState.name,
            quantity = newItemUiState.quantity,
            category = newItemUiState.category,
            actionEnabled = newItemUiState.isValid())
    }

    suspend fun updateItem() {
        if (articleUiState.isValid()) {
            articleRepository.updateItem(articleUiState.toItem())

            val articleCategorys = articleCategoryRepository.getByArticle(itemId)

            if(articleCategorys.isNotEmpty()){
                if(articleCategorys[0].category_id !== articleUiState.category){

                    articleCategoryRepository.deleteItem(
                        ArticleCategory(
                            id = articleCategorys[0].id,
                            article_id = articleCategorys[0].article_id,
                            category_id = articleCategorys[0].category_id)
                    )

                    articleCategoryRepository.insertItem(
                        ArticleCategory(
                            id = 0,
                            article_id = itemId,
                            category_id = articleUiState.category
                        )
                    )
                }
            }else{
                articleCategoryRepository.insertItem(
                    ArticleCategory(
                        id = 0,
                        article_id = itemId,
                        category_id = articleUiState.category
                    )
                )
            }
        }
    }

    fun onDialogDelete(){
        onOpenDialogClicked(tag = DIALOG_UI_TAG.TAG_DELETE)
    }

    /**Private functions****************************/
    suspend private fun deleteItem() {
        viewModelScope.launch(Dispatchers.IO) {
            val articleCategorys =
                articleCategoryRepository.getByArticle(articleUiState.toItem().id)

            if (articleCategorys.isNotEmpty()) {
                articleCategorys.forEach { articleCategory ->
                    articleCategoryRepository.deleteItem(
                        ArticleCategory(
                            id = articleCategory.id,
                            article_id = articleCategory.article_id,
                            category_id = articleCategory.category_id
                        )
                    )
                }
            }

            articleRepository.deleteItem(articleUiState.toItem())
        }
    }

    /**AlertDialog functions****************************************/
    private val _dialogState = MutableStateFlow(DialogUiState())
    val dialogState: StateFlow<DialogUiState> = _dialogState.asStateFlow()

    fun onOpenDialogClicked(tag: DIALOG_UI_TAG) {

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
                _dialog = DialogUiState(
                    tag = tag
                )
        }

        _dialogState.value = _dialog
    }

    suspend fun onDialogConfirm() {
        when(_dialogState.value.tag) {
            DIALOG_UI_TAG.TAG_DELETE -> deleteItem()
            else -> {}
        }

        _dialogState.value = DialogUiState(isShow = false)
    }

    fun onDialogDismiss() {
        _dialogState.value = DialogUiState(isShow = false)
    }
}
