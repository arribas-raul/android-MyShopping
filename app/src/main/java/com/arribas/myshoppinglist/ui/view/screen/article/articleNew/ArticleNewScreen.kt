package com.arribas.myshoppinglist.ui.view.screen.article.articleNew

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arribas.myshoppinglist.data.utils.DialogUiState
import com.arribas.myshoppinglist.data.utils.TextFieldDialogUiState
import com.arribas.myshoppinglist.ui.theme.MyShoppingListTheme
import com.arribas.myshoppinglist.ui.view.app.AppViewModelProvider
import com.arribas.myshoppinglist.ui.view.app.topBar.AppBarState
import com.arribas.myshoppinglist.ui.view.dialog.general.SimpleAlertDialog
import com.arribas.myshoppinglist.ui.view.screen.category.CategoryViewModel
import com.arribas.myshoppinglist.ui.view.screen.category.ListCategoryUiState
import com.arribas.myshoppinglist.ui.view.general.DetailBody
import com.arribas.myshoppinglist.ui.view.general.TextFieldAlertDialog
import com.arribas.myshoppinglist.ui.view.screen.article.articleList.ArticleUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ArticleNewScreen(
    onComposing: (AppBarState) -> Unit,
    viewModel: NewViewModel = viewModel(factory = AppViewModelProvider.Factory),
    categoryViewModel: CategoryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val dialogState: DialogUiState by viewModel.dialogState.collectAsState()
    val categoryDialogState: TextFieldDialogUiState by categoryViewModel.dialogState.collectAsState()
    val listCategoryUiState by categoryViewModel.listUiState.collectAsState()

    LaunchedEffect(key1 = true) {
        onComposing(
            AppBarState(
                actions = {

                }
            )
        )
    }

    NewForm(
        updateUiState = { viewModel.updateUiState(it) },

        saveItem = {
            coroutineScope.launch(Dispatchers.IO) {
                viewModel.saveItem()
            }
        },

        articleUiState = viewModel.articleUiState,
        listCategoryUiState = listCategoryUiState,
        onCategoryClick = categoryViewModel::openDialog,

        modifier = modifier
    )

    SimpleAlertDialog(
        dialogState = dialogState,
        onDismiss = viewModel::onDialogDismiss,
        onConfirm = viewModel::onDialogConfirm
    )

    TextFieldAlertDialog(
        dialogState = categoryDialogState,
        onConfirm = categoryViewModel::onDialogConfirm,
        onDismiss = categoryViewModel::onDialogDismiss,
        onValueChange = { categoryViewModel.updateUiState(it) },
        onKeyEvent = {}
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