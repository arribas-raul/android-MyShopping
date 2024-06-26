package com.arribas.myshoppinglist.ui.view.screen.article.articleDetail

import android.annotation.SuppressLint
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
import com.arribas.myshoppinglist.ui.view.general.DetailBody
import com.arribas.myshoppinglist.ui.view.app.topBar.AppBarState
import com.arribas.myshoppinglist.ui.view.dialog.general.SimpleAlertDialog
import com.arribas.myshoppinglist.ui.view.screen.category.CategoryViewModel
import com.arribas.myshoppinglist.ui.view.general.TextFieldAlertDialog
import com.arribas.myshoppinglist.ui.view.screen.article.articleList.ArticleUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ArticleDetailScreen(
    onComposing: (AppBarState) -> Unit,
    navigateBack: () -> Unit,
    viewModel: ArticleDetailViewModel = viewModel(factory = AppViewModelProvider.Factory),
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

    DetailBody(
        articleUiState = viewModel.articleUiState,

        onItemValueChange = {
            viewModel.updateUiState(it)
        },

        onSaveClick = {
            coroutineScope.launch(Dispatchers.IO) {
                viewModel.updateItem()
                //navigateBack()
            }
        },

        onDeleteClick = {
            coroutineScope.launch(Dispatchers.IO) {
                viewModel.onDialogDelete()
                //navigateBack()
            }
        },

        listCategoryUiState = listCategoryUiState,
        onCategoryClick = categoryViewModel::openDialog
    )

    SimpleAlertDialog(
        dialogState = dialogState,
        onDismiss = viewModel::onDialogDismiss,
        onConfirm = {
            coroutineScope.launch {
                viewModel.onDialogConfirm()
                navigateBack()
            }
        }
    )

    TextFieldAlertDialog(
        dialogState = categoryDialogState,
        onConfirm = categoryViewModel::onDialogConfirm,
        onDismiss = categoryViewModel::onDialogDismiss,
        onValueChange = { categoryViewModel.updateUiState(it) },
        onKeyEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    val articleUiState = ArticleUiState(name = "Bolsa de patatas")

    MyShoppingListTheme {
        //DetailScreen(articleUiState = articleUiState )
    }
}