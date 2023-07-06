package com.arribas.myshoppinglist.ui.view.Category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arribas.myshoppinglist.data.model.Article
import com.arribas.myshoppinglist.data.model.Category
import com.arribas.myshoppinglist.data.repository.CategoryRepository
import com.arribas.myshoppinglist.data.utils.DialogUiState
import com.arribas.myshoppinglist.data.utils.TextFieldDialogUiState
import com.arribas.myshoppinglist.ui.viewModel.ListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val categoryRepository: CategoryRepository
): ViewModel() {

    private val _dialogState = MutableStateFlow(TextFieldDialogUiState())
    val dialogState: StateFlow<TextFieldDialogUiState> = _dialogState.asStateFlow()

    private val _listUiState = MutableStateFlow(ListCategoryUiState())
    var listUiState: StateFlow<ListCategoryUiState> = _listUiState

    init{
        getData()
    }

    fun getData(){
        viewModelScope.launch{

            categoryRepository.getAllItems().collect { list ->
                _listUiState.value = ListCategoryUiState(
                    itemList = list,
                    itemCount = list.count()
                )
            }
        }
    }

    /**TextFieldDialog functions****************/
    fun openDialog() {
        _dialogState.value = TextFieldDialogUiState(
            title= "Nueva categoria",
            isShow = true,
            isShowBtDismiss = true)
    }

    fun onDialogConfirm() {
        _dialogState.value = TextFieldDialogUiState(isShow = false)
    }

    fun onDialogDismiss() {
        _dialogState.value = TextFieldDialogUiState(isShow = false)
    }
}

data class ListCategoryUiState(
    var itemList: List<Category> = listOf(),
    var itemCount: Number = 0
)