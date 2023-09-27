package com.arribas.myshoppinglist.ui.view.bottomsheet.shoplistBottomSheet

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.data.repository.ShoplistRepository
import com.arribas.myshoppinglist.data.utils.CrudMessageEnum
import com.arribas.myshoppinglist.data.utils.CrudMessageManager
import com.arribas.myshoppinglist.data.utils.DIALOG_UI_TAG
import com.arribas.myshoppinglist.data.utils.DialogUiState
import com.arribas.myshoppinglist.ui.view.screen.shoplist.ShoplistUiState
import com.arribas.myshoppinglist.ui.view.screen.shoplist.isValid
import com.arribas.myshoppinglist.ui.view.screen.shoplist.toItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.lang.Exception

class ShoplistBottomSheetViewModel(
    private val context: Context,
    private val shoplistRepository: ShoplistRepository
): ViewModel() {
    var shoplistUiState by mutableStateOf(ShoplistUiState())
        private set

    /**AlertDialog functions****************************************/
    private val _dialogState = MutableStateFlow(DialogUiState())
    val dialogState: StateFlow<DialogUiState> = _dialogState.asStateFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    fun onUpdateUiState(shoplistUiState: ShoplistUiState) {
        this.shoplistUiState = shoplistUiState.copy()
    }

    fun onClearUiState(){
        shoplistUiState = ShoplistUiState()
    }

    fun onChange(shoplistUiState: ShoplistUiState){
        this.shoplistUiState = shoplistUiState
    }

    fun onSave(){
        if (!shoplistUiState.isValid()) {
            return
        }

        if(shoplistUiState.id > 0){
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
                shoplistRepository.updateItem(shoplistUiState.toItem())

                sendMessage(context.getString(R.string.crud_updated_success))

            }catch (e: Exception){
                sendMessage(context.getString(R.string.crud_updated_error))
            }
        }
    }

    private fun createItem() {
        viewModelScope.launch(Dispatchers.IO) {
            val shoplist = shoplistRepository.getItemByName(shoplistUiState.name)

            if(shoplist.first() != null) {
                onOpenDialogClicked(tag = DIALOG_UI_TAG.TAG_ELEMENT_EXISTS)

            }else{
                try {
                    val new = shoplistRepository.insertItem(shoplistUiState.toItem())

                    new.let{
                        shoplistUiState = ShoplistUiState()
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