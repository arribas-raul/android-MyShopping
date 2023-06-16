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
import com.arribas.myshoppinglist.ui.navigation.NavigationDestination
import com.arribas.myshoppinglist.ui.theme.MyShoppingListTheme
import com.arribas.myshoppinglist.ui.view.general.DetailBody
import com.arribas.myshoppinglist.ui.view.general.SimpleAlertDialog
import com.arribas.myshoppinglist.ui.viewModel.AppViewModelProvider
import com.arribas.myshoppinglist.ui.viewModel.detailArticle.NewViewModel
import com.arribas.myshoppinglist.ui.viewModel.listArticle.ArticleUiState
import kotlinx.coroutines.launch

object NewDestination : NavigationDestination {
    override val route = "new"
    override val titleRes = R.string.item_entry_title
}

@Composable
fun NewScreen(
    navigateBack: () -> Unit,
    viewModel: NewViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val showDialogState: Boolean by viewModel.showDialog.collectAsState()

    NewForm(
        updateUiState = { viewModel.updateUiState(it) },

        saveItem = {
            coroutineScope.launch {
                viewModel.saveItem()
                //navigateBack()
            }
        },

        onDialogDismiss = { viewModel.onDialogDismiss() },
        articleUiState = viewModel.articleUiState,
        showDialogState = showDialogState,
        modifier = Modifier.background(colorResource(R.color.my_background))
    )
}

@Composable
fun NewForm(
    updateUiState: (ArticleUiState) -> Unit = {},
    saveItem: () -> Unit = {},
    onDialogDismiss: () -> Unit = {},
    articleUiState: ArticleUiState,
    showDialogState: Boolean = false,
    modifier: Modifier = Modifier) {

    DetailBody(
        articleUiState = articleUiState,
        onItemValueChange = { updateUiState(it) },
        onSaveClick = { saveItem() },
        onDeleteClick = { }
    )

    SimpleAlertDialog(
        show = showDialogState,
        title = "Exist this item",
        showDismissButton = false,
        onConfirm = onDialogDismiss
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