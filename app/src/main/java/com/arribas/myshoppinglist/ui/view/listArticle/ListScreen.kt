package com.arribas.myshoppinglist.ui.view.listArticle

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.data.model.Article
import com.arribas.myshoppinglist.ui.navigation.NavigationDestination
import com.arribas.myshoppinglist.ui.theme.MyShoppingListTheme
import com.arribas.myshoppinglist.ui.view.TopBar
import com.arribas.myshoppinglist.ui.view.general.FloatingButton
import com.arribas.myshoppinglist.ui.view.general.SimpleAlertDialog
import com.arribas.myshoppinglist.ui.viewModel.AppViewModelProvider
import com.arribas.myshoppinglist.ui.viewModel.ListViewModel
import kotlinx.coroutines.launch

object ListDestination : NavigationDestination {
    override val route = "list"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    navigateToItemEntry: () -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    viewModel: ListViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier){

    val listUiState by viewModel.listUiState.collectAsState()
    val showDialogState: Boolean by viewModel.showDialog.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),

        topBar = { TopBar(title = stringResource(id = R.string.app_name)) },

        floatingActionButton = { FloatingButton(onClick = navigateToItemEntry,) },

        ) { innerPadding ->
            ListBody(
                itemList = listUiState.itemList,
                modifier = Modifier.padding(innerPadding),
                navigateToItemUpdate = { navigateToItemUpdate(it) },
                deleteItem = { viewModel.onOpenDialogClicked(it) },
                updateItem = { viewModel.updateItem(it) }
            )

            SimpleAlertDialog(
                show = showDialogState,
                onDismiss = viewModel::onDialogDismiss,
                onConfirm = viewModel::onDialogConfirm
            )
    }
}

@Composable
fun ListBody(
    itemList: List<Article>,
    navigateToItemUpdate: (Int) -> Unit,
    deleteItem: (Article) -> Unit,
    updateItem: (Article) -> Unit,
    modifier: Modifier = Modifier){

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        InventoryListHeader()
        Divider()
        if (itemList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_item_description),
                style = MaterialTheme.typography.labelMedium
            )
        } else {
            InventoryList(
                itemList = itemList,
                onItemClick = { navigateToItemUpdate(it.id) },
                onDeleteClick = { deleteItem(it) },
                onCheckClick =  { updateItem(it) }
            )
        }
    }
}

@Composable
private fun InventoryList(
    itemList: List<Article>,
    onItemClick: (Article) -> Unit,
    onDeleteClick: (Article) -> Unit,
    onCheckClick: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = itemList, key = { it.id }) { item ->
            InventoryItem(
                item = item,
                onItemClick = onItemClick,
                onDeleteClick = onDeleteClick,
                onCheckClick = onCheckClick,
                modifier = Modifier
            )

            Divider()
        }
    }
}

@Composable
private fun InventoryItem(
    item: Article,
    onItemClick: (Article) -> Unit,
    onDeleteClick: (Article) -> Unit,
    onCheckClick: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .height(50.dp)
        .clickable { onItemClick(item) }
        .padding(vertical = 16.dp, horizontal = 5.dp)
    ) {
        val checked = remember { mutableStateOf(item.check) }

        Checkbox(
            checked = checked.value,

            onCheckedChange = {
                checked.value = it
                onCheckClick(item.copy(check = it))
            },

            modifier = Modifier.fillMaxWidth().weight(0.5f).padding(start = 10.dp, end = 30.dp)
        )

        Text(
            text = item.name,
            modifier = Modifier.weight(2f),
            style = MaterialTheme.typography.labelMedium,
        )

        Text(
            text = item.quantity.toString(),
            modifier = Modifier.fillMaxWidth().weight(0.6f).align(Alignment.CenterVertically),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium
        )

        IconButton(
            onClick = { onDeleteClick(item) },
            modifier = Modifier.weight(0.3f).align(Alignment.CenterVertically)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_delete),
                contentDescription = stringResource(R.string.delete)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListHeaderPreview() {
    MyShoppingListTheme {
        InventoryListHeader()
    }
}

@Preview(showBackground = true)
@Composable
fun ListItemPreview() {
    val article = Article(
        id = 1,
        name = "Bolsa de patatas",
        quantity = 1,
        check = true
    )

    MyShoppingListTheme {
        InventoryItem(
            item = article,
            onItemClick = {},
            onDeleteClick = {},
            onCheckClick = {}
        )
    }
}


