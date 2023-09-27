package com.arribas.myshoppinglist.ui.view.bottomsheet.categoryBottomSheet

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.data.repository.CategoryRepository
import com.arribas.myshoppinglist.data.utils.CrudMessageEnum
import com.arribas.myshoppinglist.data.utils.CrudMessageManager
import com.arribas.myshoppinglist.data.utils.DIALOG_UI_TAG
import com.arribas.myshoppinglist.data.utils.DialogUiState
import com.arribas.myshoppinglist.ui.view.screen.category.categoryList.CategoryUiState
import com.arribas.myshoppinglist.ui.view.screen.category.categoryList.isValid
import com.arribas.myshoppinglist.ui.view.screen.category.categoryList.toItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.lang.Exception

class CategoryBottomSheetViewModel(
    private val context: Context,
    private val categoryRepository: CategoryRepository
): ViewModel() {
    var categoryUiState by mutableStateOf(CategoryUiState())
        private set

    /**AlertDialog functions****************************************/
    private val _dialogState = MutableStateFlow(DialogUiState())
    val dialogState: StateFlow<DialogUiState> = _dialogState.asStateFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    fun onUpdateUiState(categoryUiState: CategoryUiState) {
        this.categoryUiState = categoryUiState.copy()
    }

    fun onClearUiState(){
        categoryUiState = CategoryUiState()
    }

    fun onChange(categoryUiState: CategoryUiState){
        this.categoryUiState = categoryUiState
    }

    fun onSave(){
        if (!categoryUiState.isValid()) {
            return
        }

        if(categoryUiState.id > 0){
            updateItem()

        }else{
            createItem()
        }
    }

    /**AlertDialog functions****************************************/
    fun onDialogConfirm() {
        _dialogState.value = DialogUiState(isShow = false)
    }

    fun onDialogDismiss() {
        _dialogState.value = DialogUiState(isShow = false)
    }

    /**Private functions****************************************/
    private fun updateItem() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                categoryRepository.updateItem(categoryUiState.toItem())

                sendMessage(context.getString(R.string.crud_updated_success))

            }catch (e: Exception){
                sendMessage(context.getString(R.string.crud_updated_error))
            }
        }
    }

    private fun createItem() {
        viewModelScope.launch(Dispatchers.IO) {
            val shoplist = categoryRepository.getItemByName(categoryUiState.name)

            if(shoplist.first() != null) {
                onOpenDialogClicked(tag = DIALOG_UI_TAG.TAG_ELEMENT_EXISTS)

            }else{
                try {
                    val new = categoryRepository.insertItem(categoryUiState.toItem())

                    new.let{
                        categoryUiState = CategoryUiState()
                    }

                    sendMessage(CrudMessageManager(context)
                        .getMessage(CrudMessageEnum.CREATED_SUCCESS))

                }catch (e: Exception){
                    sendMessage(CrudMessageManager(context)
                        .getMessage(CrudMessageEnum.CREATED_ERROR))
                }
            }
        }
    }

    private fun onOpenDialogClicked(tag: DIALOG_UI_TAG) {

        val _dialog: DialogUiState

        when(tag) {
            DIALOG_UI_TAG.TAG_ELEMENT_EXISTS ->
                _dialog = DialogUiState(
                    tag = tag,
                    title = CrudMessageManager(context).getMessage(CrudMessageEnum.ELEMENT_EXIST),
                    isShow = true
                )
            else ->
                _dialog = DialogUiState()
        }

        _dialogState.value = _dialog
        _dialogState.value.isShow = true
    }

    /**Toast functions++++++*/
    private fun sendMessage(message: String) {
        viewModelScope.launch {
            _toastMessage.emit(message)
        }
    }
}