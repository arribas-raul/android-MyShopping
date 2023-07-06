package com.arribas.myshoppinglist.ui.view.detailArticle

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.data.utils.DialogUiState
import com.arribas.myshoppinglist.data.utils.TextFieldDialogUiState
import com.arribas.myshoppinglist.ui.theme.MyShoppingListTheme
import com.arribas.myshoppinglist.ui.view.AppViewModelProvider
import com.arribas.myshoppinglist.ui.view.Category.CategoryViewModel
import com.arribas.myshoppinglist.ui.view.Category.ListCategoryUiState
import com.arribas.myshoppinglist.ui.view.general.DetailBody
import com.arribas.myshoppinglist.ui.view.general.SimpleAlertDialog
import com.arribas.myshoppinglist.ui.view.general.TextFieldAlertDialog
import com.arribas.myshoppinglist.ui.view.newArticle.NewViewModel
import com.arribas.myshoppinglist.ui.view.listArticle.ArticleUiState
import kotlinx.coroutines.launch

@Composable
fun NewScreen(
    viewModel: NewViewModel = viewModel(factory = AppViewModelProvider.Factory),
    categoryViewModel: CategoryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val dialogState: DialogUiState by viewModel.dialogState.collectAsState()
    val categoryDialogState: TextFieldDialogUiState by categoryViewModel.dialogState.collectAsState()
    val listCategoryUiState by categoryViewModel.listUiState.collectAsState()

    NewForm(
        updateUiState = { viewModel.updateUiState(it) },

        saveItem = {
            coroutineScope.launch {
                viewModel.saveItem()
            }
        },

        articleUiState = viewModel.articleUiState,
        listCategoryUiState = listCategoryUiState,
        onCategoryClick = categoryViewModel::openDialog,

        modifier = Modifier.background(colorResource(R.color.my_background))
    )

    SimpleAlertDialog(
        dialogState = dialogState,
        onConfirm = viewModel::onDialogConfirm
    )

    TextFieldAlertDialog(
        dialogState = categoryDialogState,
        onConfirm = categoryViewModel::onDialogConfirm,
        onDismiss = categoryViewModel::onDialogDismiss
    )
}

@Composable
fun NewForm(
    updateUiState: (ArticleUiState) -> Unit = {},
    saveItem: () -> Unit = {},
    onCategoryClick: () -> Unit = {},
    articleUiState: ArticleUiState,
    listCategoryUiState: ListCategoryUiState = ListCategoryUiState(),
    modifier: Modifier = Modifier) {

    DetailBody(
        articleUiState = articleUiState,
        onItemValueChange = { updateUiState(it) },
        onSaveClick = saveItem,
        listCategoryUiState = listCategoryUiState,
        onCategoryClick = onCategoryClick
    )
}

@Preview(showBackground = true)
@Composable
fun NewScreenPreview() {
    val articleUiState = ArticleUiState()

    MyShoppingListTheme {
        NewForm(articleUiState = articleUiState )
    }
}