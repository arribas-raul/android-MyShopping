package com.arribas.myshoppinglist.ui.view.detailArticle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
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
    modifier: Modifier = Modifier.fillMaxHeight()
        .background(colorResource(R.color.my_background))
) {
    val coroutineScope = rememberCoroutineScope()
    val showDialogState: Boolean by viewModel.showDialog.collectAsState()

    DetailForm(
        navigateBack = { navigateBack() },
        updateUiState = { viewModel.updateUiState(it) },

        updateItem = {
            coroutineScope.launch {
                viewModel.updateItem()
                navigateBack()
            }
        },

        deleteItem = {
            coroutineScope.launch {
                viewModel.deleteItem()
                navigateBack()
            }
        },

        onOpenDialogClicked = { viewModel.onOpenDialogClicked() },
        onDialogDismiss = { viewModel.onDialogDismiss() },
        articleUiState = viewModel.articleUiState,
        showDialogState = showDialogState,
        modifier = Modifier.fillMaxHeight()
            .background(colorResource(R.color.my_background))
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailForm(
    navigateBack: () -> Unit = {},
    updateUiState: (ArticleUiState) -> Unit = {},
    updateItem: () -> Unit = {},
    deleteItem: () -> Unit = {},
    onOpenDialogClicked: () -> Unit = {},
    onDialogDismiss: () -> Unit = {},
    articleUiState: ArticleUiState,
    showDialogState: Boolean = false,
    modifier: Modifier = Modifier)
{
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.my_background)),

        topBar = {
            TopBar(
                title = articleUiState.name,
                canNavigateBack = true,
                navigateUp = navigateBack,
                modifier = Modifier
                    .fillMaxWidth()
            ) },

        ) { innerPadding ->

        DetailBody(
            articleUiState = articleUiState,
            onItemValueChange = { updateUiState(it) },
            onSaveClick = { updateItem() },
            modifier = modifier.padding(innerPadding),
            onDeleteClick = {
                onOpenDialogClicked()
            },
        )

        SimpleAlertDialog(
            show = showDialogState,
            title = "Please confirm",
            body =  "Should I continue with the requested action?",
            showDismissButton = true,
            onDismiss = onDialogDismiss,
            onConfirm = { deleteItem() }
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