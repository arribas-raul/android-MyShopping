package com.arribas.myshoppinglist.ui.view.detailArticle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.ui.navigation.NavigationDestination
import com.arribas.myshoppinglist.ui.theme.MyShoppingListTheme
import com.arribas.myshoppinglist.ui.view.TopBar
import com.arribas.myshoppinglist.ui.view.general.DetailBody
import com.arribas.myshoppinglist.ui.view.general.SimpleAlertDialog
import com.arribas.myshoppinglist.ui.viewModel.AppViewModelProvider
import com.arribas.myshoppinglist.ui.viewModel.detailArticle.DetailViewModel
import com.arribas.myshoppinglist.ui.viewModel.detailArticle.NewViewModel
import com.arribas.myshoppinglist.ui.viewModel.listArticle.ArticleUiState
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale


object DetailDestination : NavigationDestination {
    const val itemIdArg = "itemId"

    override val route = "detail"
    val routeWithArgs = "$route/{$itemIdArg}"

    override val titleRes = R.string.item_detail_title

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: DetailViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val showDialogState: Boolean by viewModel.showDialog.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),

        topBar = {
            TopBar(
                title = stringResource(id = R.string.app_name),
                canNavigateBack = true,
                navigateUp = navigateBack) },

        ) { innerPadding ->
            DetailBody(
                articleUiState = viewModel.articleUiState,
                onItemValueChange = { viewModel.updateUiState(it) },

                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.updateItem()
                        navigateBack()
                    }
                },

                modifier = Modifier.padding(innerPadding),

                onDeleteClick = {
                    viewModel.onOpenDialogClicked()
                },
            )

            SimpleAlertDialog(
                show = showDialogState,
                onDismiss = viewModel::onDialogDismiss,
                onConfirm = {
                    coroutineScope.launch {
                        viewModel.deleteItem()
                        navigateBack()
                    }
                }
            )
    }
}



@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    MyShoppingListTheme {
        DetailScreen(navigateBack = {}, onNavigateUp = {})
    }
}