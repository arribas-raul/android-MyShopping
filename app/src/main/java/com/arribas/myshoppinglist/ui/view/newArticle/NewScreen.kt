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
import com.arribas.myshoppinglist.ui.theme.MyShoppingListTheme
import com.arribas.myshoppinglist.ui.view.general.DetailBody
import com.arribas.myshoppinglist.ui.view.general.SimpleAlertDialog
import com.arribas.myshoppinglist.ui.viewModel.AppViewModelProvider
import com.arribas.myshoppinglist.ui.viewModel.NewArticle.NewViewModel
import com.arribas.myshoppinglist.ui.viewModel.listArticle.ArticleUiState
import kotlinx.coroutines.launch

@Composable
fun NewScreen(
    viewModel: NewViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val dialogState: DialogUiState by viewModel.dialogState.collectAsState()

    NewForm(
        updateUiState = { viewModel.updateUiState(it) },

        saveItem = {
            coroutineScope.launch {
                viewModel.saveItem()
            }
        },

        articleUiState = viewModel.articleUiState,
        modifier = Modifier.background(colorResource(R.color.my_background))
    )

    SimpleAlertDialog(
        dialogState = dialogState,
        onConfirm = viewModel::onDialogConfirm
    )
}

@Composable
fun NewForm(
    updateUiState: (ArticleUiState) -> Unit = {},
    saveItem: () -> Unit = {},
    articleUiState: ArticleUiState,
    modifier: Modifier = Modifier) {

    DetailBody(
        articleUiState = articleUiState,
        onItemValueChange = { updateUiState(it) },
        onSaveClick = { saveItem() }
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