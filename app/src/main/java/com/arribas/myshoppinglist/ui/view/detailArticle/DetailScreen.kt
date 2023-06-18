package com.arribas.myshoppinglist.ui.view.detailArticle

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.ui.navigation.NavigationDestination
import com.arribas.myshoppinglist.ui.theme.MyShoppingListTheme
import com.arribas.myshoppinglist.ui.view.TopBar
import com.arribas.myshoppinglist.ui.view.general.DetailBody
import com.arribas.myshoppinglist.ui.view.general.SimpleAlertDialog
import com.arribas.myshoppinglist.ui.viewModel.AppViewModelProvider
import com.arribas.myshoppinglist.ui.viewModel.detailArticle.DetailViewModel
import com.arribas.myshoppinglist.ui.viewModel.listArticle.ArticleUiState
import com.arribas.myshoppinglist.ui.viewModel.listArticleShop.DialogUiState
import kotlinx.coroutines.launch

object DetailDestination : NavigationDestination {
    const val itemIdArg = "itemId"

    override val route = "detail"
    val routeWithArgs = "$route/{$itemIdArg}"

    override val titleRes = R.string.item_detail_title
}

@Composable
fun DetailScreen(
    navigateBack: () -> Unit,
    viewModel: DetailViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val dialogState: DialogUiState by viewModel.dialogState.collectAsState()

    DetailForm(
        navigateBack = { navigateBack() },
        updateUiState = { viewModel.updateUiState(it) },

        updateItem = {
            coroutineScope.launch {
                viewModel.updateItem()
                navigateBack()
            }
        },

        deleteItem = viewModel::onDialogDelete,
        articleUiState = viewModel.articleUiState,
        modifier = modifier
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailForm(
    navigateBack: () -> Unit = {},
    updateUiState: (ArticleUiState) -> Unit = {},
    updateItem: () -> Unit = {},
    deleteItem: () -> Unit = {},
    articleUiState: ArticleUiState,
    modifier: Modifier = Modifier)
{
    Scaffold(
        topBar = {
            TopBar(
                title = articleUiState.name,
                canNavigateBack = true,
                navigateUp = navigateBack
            ) },

        ) { innerPadding ->

        DetailBody(
            articleUiState = articleUiState,
            onItemValueChange = { updateUiState(it) },
            onSaveClick = { updateItem() },
            modifier = modifier.padding(innerPadding),
            onDeleteClick = deleteItem
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    val articleUiState = ArticleUiState(name = "Bolsa de patatas")

    MyShoppingListTheme {
        DetailForm(articleUiState = articleUiState )
    }
}