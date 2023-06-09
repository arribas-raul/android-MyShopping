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

package com.arribas.myshoppinglist.ui.viewModel.NewArticle

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.arribas.myshoppinglist.data.repository.ArticleRepository
import com.arribas.myshoppinglist.data.utils.DialogUiState
import com.arribas.myshoppinglist.ui.viewModel.listArticle.ArticleUiState
import com.arribas.myshoppinglist.ui.viewModel.listArticle.isValid
import com.arribas.myshoppinglist.ui.viewModel.listArticle.toItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

/**
 * View Model to validate and insert items in the Room database.
 */
class NewViewModel(
    private val articleRepository: ArticleRepository) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var articleUiState by mutableStateOf(ArticleUiState())
        private set

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

        val article = articleRepository.getItemByName(articleUiState.name)

        if(article.first() != null){
            onOpenDialogClicked()

        }else{
            articleRepository.insertItem(articleUiState.toItem())

            articleUiState = ArticleUiState()
        }
    }

    suspend fun deleteItem() {
        articleRepository.deleteItem(articleUiState.toItem())
    }

    /**AlertDialog functions****************************************/
    private val _dialogState = MutableStateFlow(DialogUiState())
    val dialogState: StateFlow<DialogUiState> = _dialogState.asStateFlow()


    fun onOpenDialogClicked() {
        _dialogState.value.isShow = true
    }

    fun onDialogConfirm() {
        _dialogState.value.isShow = false
    }

    fun onDialogDismiss() {
        _dialogState.value.isShow = false
    }
}
