package com.arribas.myshoppinglist.ui.view.shoplist.shoplistDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arribas.myshoppinglist.data.model.ArticleShop
import com.arribas.myshoppinglist.data.model.QArticle
import com.arribas.myshoppinglist.data.model.Shoplist
import com.arribas.myshoppinglist.data.repository.ShoplistRepository
import com.arribas.myshoppinglist.data.utils.DIALOG_UI_TAG
import com.arribas.myshoppinglist.data.utils.DialogUiState
import com.arribas.myshoppinglist.ui.view.shoplist.ShoplistUiState
import com.arribas.myshoppinglist.ui.view.shoplistList.SearchUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ShoplistDetailViewModel(
    private val shoplistRepository: ShoplistRepository
): ViewModel() {

    private val _shoplistUiState = MutableStateFlow(ShoplistUiState())
    val shoplistUiState: StateFlow<ShoplistUiState> = _shoplistUiState

    init{}

    fun clearName(){
        _shoplistUiState.value = _shoplistUiState.value.copy(name = "")
    }

    fun clearType(){
        _shoplistUiState.value = _shoplistUiState.value.copy(type = "")
    }

    fun onDialogDelete(shoplist: Shoplist){
        /*onOpenDialogClicked(
            qArticle = article,
            tag = DIALOG_UI_TAG.TAG_DELETE
        )*/
    }

    fun updateItem(shoplist: Shoplist) {
        /*article = qArticle

        viewModelScope.launch(Dispatchers.IO) {
            //println("article: $article")
            if(qArticle.shopCheked){
                val articleShop = articleShopRepository.getItemByName(qArticle.name)

                if(articleShop.first() != null) {
                    articleShopRepository.deleteItem(articleShop.first()!!)
                }

            }else{
                val count: Int = articleShopRepository.count()

                val articleShop = ArticleShop(
                    name = qArticle.name,
                    order = count + 1
                )

                articleShopRepository.insertItem(articleShop)
            }

            articleRepository.updateItem(
                qArticleToArticle(
                    article.copy(shopCheked = !article.shopCheked)))
        }*/
    }

    private fun onOpenDialogClicked(qArticle: QArticle? = null, tag: DIALOG_UI_TAG) {
        /*if(qArticle !== null) {
            article = qArticle
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

        _dialogState.value = _dialog*/
    }

    fun onDialogConfirm() {
        /*when(_dialogState.value.tag) {
            DIALOG_UI_TAG.TAG_DELETE  -> deleteItem()
            else -> {}
        }

        _dialogState.value = DialogUiState(isShow = false)*/
    }

    fun onDialogDismiss() {
        //_dialogState.value = DialogUiState(isShow = false)
    }
}