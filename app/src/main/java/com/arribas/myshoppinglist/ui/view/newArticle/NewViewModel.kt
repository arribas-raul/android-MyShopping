/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.arribas.myshoppinglist.ui.view.newArticle

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.arribas.myshoppinglist.data.model.ArticleCategory
import com.arribas.myshoppinglist.data.repository.ArticleCategoryRepository
import com.arribas.myshoppinglist.data.repository.ArticleRepository
import com.arribas.myshoppinglist.data.utils.DIALOG_UI_TAG
import com.arribas.myshoppinglist.data.utils.DialogUiState
import com.arribas.myshoppinglist.data.utils.TextFieldDialogUiState
import com.arribas.myshoppinglist.ui.view.listArticle.ArticleUiState
import com.arribas.myshoppinglist.ui.view.listArticle.isValid
import com.arribas.myshoppinglist.ui.view.listArticle.toItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

/**
 * View Model to validate and insert items in the Room database.
 */
class NewViewModel(
    private val articleCategoryRepository: ArticleCategoryRepository,
    private val articleRepository: ArticleRepository
) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var articleUiState by mutableStateOf(ArticleUiState())
        private set

    /**AlertDialog functions****************************************/
    private val _dialogState = MutableStateFlow(DialogUiState())
    val dialogState: StateFlow<DialogUiState> = _dialogState.asStateFlow()

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(newItemUiState: ArticleUiState) {
        articleUiState = newItemUiState.copy( actionEnabled = newItemUiState.isValid())
    }

    suspend fun saveItem() {
        if (!articleUiState.isValid()) {
            return
        }

        articleUiState.copy(name = articleUiState.name
            .trim().lowercase().replace("\n", ""))

        val article = articleRepository.getItemByName(articleUiState.name)

        if(article.first() != null){
            onOpenDialogClicked(tag = DIALOG_UI_TAG.TAG_ELEMENT_EXISTS)

        }else{
            val newArticle = articleRepository.insertItem(articleUiState.toItem())

            newArticle.let{id ->
                if(articleUiState.category > 0) {
                    articleCategoryRepository.insertItem(
                        ArticleCategory(
                            id = 0,
                            article_id = id.toInt(),
                            category_id = articleUiState.category
                        )
                    )
                }
            }

            articleUiState = ArticleUiState()
        }
    }

    suspend fun deleteItem() {
        articleRepository.deleteItem(articleUiState.toItem())
    }

    fun onOpenDialogClicked(tag: DIALOG_UI_TAG) {
        val _dialog: DialogUiState

        when(tag) {
            DIALOG_UI_TAG.TAG_ELEMENT_EXISTS ->
                _dialog = DialogUiState(
                    tag = tag,
                    title = "Este elemento ya existe",
                    isShow = true
                )
            else ->
                _dialog = DialogUiState()
        }

        _dialogState.value = _dialog
        _dialogState.value.isShow = true
    }

    fun onDialogConfirm() {
        _dialogState.value = DialogUiState(isShow = false)
    }

    fun onDialogDismiss() {
    }
}
