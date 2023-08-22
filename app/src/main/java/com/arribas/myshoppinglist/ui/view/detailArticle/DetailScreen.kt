package com.arribas.myshoppinglist.ui.view.detailArticle

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arribas.myshoppinglist.data.MainTag
import com.arribas.myshoppinglist.data.utils.DialogUiState
import com.arribas.myshoppinglist.data.utils.TextFieldDialogUiState
import com.arribas.myshoppinglist.ui.navigation.route.RouteEnum
import com.arribas.myshoppinglist.ui.theme.MyShoppingListTheme
import com.arribas.myshoppinglist.ui.view.general.DetailBody
import com.arribas.myshoppinglist.ui.view.general.SimpleAlertDialog
import com.arribas.myshoppinglist.ui.view.AppViewModelProvider
import com.arribas.myshoppinglist.ui.view.Category.CategoryViewModel
import com.arribas.myshoppinglist.ui.view.Category.ListCategoryUiState
import com.arribas.myshoppinglist.ui.view.general.MyTopBar
import com.arribas.myshoppinglist.ui.view.general.TextFieldAlertDialog
import com.arribas.myshoppinglist.ui.view.listArticle.ArticleUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun DetailScreen(
    navigateBack: () -> Unit,
    viewModel: DetailViewModel = viewModel(factory = AppViewModelProvider.Factory),
    categoryViewModel: CategoryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val dialogState: DialogUiState by viewModel.dialogState.collectAsState()
    val categoryDialogState: TextFieldDialogUiState by categoryViewModel.dialogState.collectAsState()
    val listCategoryUiState by categoryViewModel.listUiState.collectAsState()

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
        onDeleteClick = viewModel::onDialogDelete,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailForm(
    navigateBack: () -> Unit = {},
    updateUiState: (ArticleUiState) -> Unit = {},
    updateItem: () -> Unit = {},
    deleteItem: () -> Unit = {},
    articleUiState: ArticleUiState,
    listCategoryUiState: ListCategoryUiState = ListCategoryUiState(),
    onCategoryClick: () -> Unit = {},
    modifier: Modifier = Modifier)
{
    Scaffold(
        topBar = {
            MyTopBar(
                title = articleUiState.name,
                canNavigateBack = true,
                navigateUp = navigateBack,
                tag = RouteEnum.ITEM_LIST,
                onClickDrawer = {}
            )
        },

        ) { innerPadding ->

        Row(modifier = modifier
            .wrapContentWidth()
            .padding(innerPadding)
        ) {

            DetailBody(
                articleUiState = articleUiState,
                onItemValueChange = { updateUiState(it) },
                onSaveClick = { updateItem() },
                onDeleteClick = deleteItem,
                listCategoryUiState = listCategoryUiState,
                onCategoryClick = onCategoryClick
            )
        }
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