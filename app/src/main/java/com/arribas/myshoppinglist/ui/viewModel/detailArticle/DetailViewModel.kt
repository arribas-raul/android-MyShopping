package com.arribas.myshoppinglist.ui.viewModel.detailArticle

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arribas.myshoppinglist.data.repository.ArticleRepository
import com.arribas.myshoppinglist.data.utils.DIALOG_UI_TAG
import com.arribas.myshoppinglist.data.utils.DialogUiState
import com.arribas.myshoppinglist.ui.view.detailArticle.DetailDestination
import com.arribas.myshoppinglist.ui.viewModel.listArticle.ArticleUiState
import com.arribas.myshoppinglist.ui.viewModel.listArticle.isValid
import com.arribas.myshoppinglist.ui.viewModel.listArticle.toArticleUiState
import com.arribas.myshoppinglist.ui.viewModel.listArticle.toItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * View Model to validate and insert items in the Room database.
 */
class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val articleRepository: ArticleRepository) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle[DetailDestination.itemIdArg])

    var articleUiState by mutableStateOf(ArticleUiState())
        private set

    init {
        try {
            viewModelScope.launch {
                articleUiState = articleRepository.getItem(itemId)
                    .filterNotNull()
                    .first()
                    .toArticleUiState(actionEnabled = true)
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
            actionEnabled = newItemUiState.isValid())
    }

    suspend fun updateItem() {
        if (articleUiState.isValid()) {
            articleRepository.updateItem(articleUiState.toItem())
        }
    }

    fun onDialogDelete(){
        onOpenDialogClicked(tag = DIALOG_UI_TAG.TAG_DELETE)
    }

    /**Private functions****************************/
    suspend private fun deleteItem() {
        articleRepository.deleteItem(articleUiState.toItem())
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
