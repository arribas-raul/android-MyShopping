package com.arribas.myshoppinglist.ui.view.screen.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arribas.myshoppinglist.data.model.Category
import com.arribas.myshoppinglist.data.repository.CategoryRepository
import com.arribas.myshoppinglist.data.utils.TextFieldDialogUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
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

    fun updateUiState(name: String) {
        _dialogState.value = _dialogState.value.copy(name = name)
    }

    /**TextFieldDialog functions****************/
    fun openDialog() {
        _dialogState.value = TextFieldDialogUiState(
            title= "Nueva categoria",
            isShow = true,
            isShowBtDismiss = true)
    }

    fun onDialogConfirm() {
        viewModelScope.launch {
            val category = categoryRepository.getItemByName(dialogState.value.name)

            if(category.first() != null){
                _dialogState.value = _dialogState.value.copy(msgError = "Esta categoría ya existe.")

            }else{
                categoryRepository.insertItem(
                    Category(
                        id = 0,
                        name = dialogState.value.name
                    )
                )
                _dialogState.value = TextFieldDialogUiState(isShow = false)
            }
        }
    }

    fun onDialogDismiss() {
        //dialogState.value.copy(name = name)
        _dialogState.value = TextFieldDialogUiState(isShow = false)
    }
}

data class ListCategoryUiState(
    var itemList: List<Category> = listOf(),
    var itemCount: Number = 0
)