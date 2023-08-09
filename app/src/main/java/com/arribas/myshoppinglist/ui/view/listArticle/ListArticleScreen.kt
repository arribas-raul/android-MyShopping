package com.arribas.myshoppinglist.ui.view.listArticle

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.data.model.Article
import com.arribas.myshoppinglist.data.utils.DialogUiState
import com.arribas.myshoppinglist.ui.theme.MyShoppingListTheme
import com.arribas.myshoppinglist.ui.view.general.SimpleAlertDialog
import com.arribas.myshoppinglist.ui.view.AppViewModelProvider
import com.arribas.myshoppinglist.ui.viewModel.ListArticleViewModel

@Composable
fun ListArticleScreen(
    navigateToItemUpdate: (Int) -> Unit,
    viewModel: ListArticleViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier){

    val listUiState by viewModel.listUiState.collectAsState()
    val searchUiState by viewModel.searchUiState.collectAsState()
    val dialogState: DialogUiState by viewModel.dialogState.collectAsState()

    Column {
        ListArticleHeader(
            searchUiState = searchUiState,
            onValueChange = { viewModel.search(it) },
            clearName = viewModel::clearName
        )

        ListArticleBody(
            itemList = listUiState.itemList,
            navigateToItemUpdate = { navigateToItemUpdate(it) },
            deleteItem = { viewModel.onDialogDelete(it) },
            updateItem = { viewModel.updateItem(it) }
        )
    }

    SimpleAlertDialog(
        dialogState = dialogState,
        onDismiss = viewModel::onDialogDismiss,
        onConfirm = viewModel::onDialogConfirm
    )

}

@Composable
fun ListArticleBody(
    itemList: List<Article>,
    navigateToItemUpdate: (Int) -> Unit,
    deleteItem: (Article) -> Unit,
    updateItem: (Article) -> Unit,
    modifier: Modifier = Modifier){

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp)
    )
    {
        items(items = itemList, key = { it.id }) { item ->
            InventoryItem(
                item = item,
                onItemClick = onItemClick,
                onDeleteClick = onDeleteClick,
                onCheckClick = {onCheckClick(it)},
                modifier = Modifier
            )
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
    Card(
        border = BorderStroke(0.2.dp, Color.Gray),
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckClick(item) }
            .background(colorResource(R.color.white))
            .padding(8.dp)
        ) {
            /*var checked = remember { mutableStateOf(item.shopCheked) }

            Checkbox(
                checked = checked.value,

                onCheckedChange = { _checked ->
                    checked.value = _checked
                    onCheckClick(item.copy(shopCheked = _checked))
                },

                modifier = Modifier
                    .weight(0.3f)
                    .padding(end = 10.dp)
            )*/
            if(item.shopCheked) {
                Image(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = stringResource(R.string.update)
                )
            }

            Text(
                text = item.name,
                modifier = Modifier
                    .weight(2f)
                    .align(Alignment.CenterVertically),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )

            IconButton(
                onClick = { onItemClick(item) },
                modifier = Modifier
                    .weight(0.3f)
                    .align(Alignment.CenterVertically)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_create),
                    contentDescription = stringResource(R.string.update)
                )
            }

            IconButton(
                onClick = { onDeleteClick(item) },
                modifier = Modifier
                    .weight(0.3f)
                    .align(Alignment.CenterVertically)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = stringResource(R.string.delete)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ListArticleHeaderPreview() {
    MyShoppingListTheme {
        ListArticleHeader()
    }
}

@Preview(showBackground = true)
@Composable
fun ListArticleItemPreview() {
    val article = Article(
        id = 1,
        name = "Bolsa de patatas",
        shopCheked = false
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


