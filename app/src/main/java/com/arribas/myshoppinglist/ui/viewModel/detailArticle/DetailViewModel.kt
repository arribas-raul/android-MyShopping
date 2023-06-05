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

package com.arribas.myshoppinglist.ui.viewModel.detailArticle

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arribas.myshoppinglist.data.repository.ArticleRepository
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
        viewModelScope.launch {
            articleUiState = articleRepository.getItem(itemId)
                .filterNotNull()
                .first()
                .toArticleUiState(actionEnabled = true)
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

    suspend fun deleteItem() {
        articleRepository.deleteItem(articleUiState.toItem())
    }

    /**AlertDialog functions****************************************/
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    fun onOpenDialogClicked() {
        _showDialog.value = true
    }

    fun onDialogConfirm() {
        _showDialog.value = false
    }

    fun onDialogDismiss() {
        _showDialog.value = false
    }
}
