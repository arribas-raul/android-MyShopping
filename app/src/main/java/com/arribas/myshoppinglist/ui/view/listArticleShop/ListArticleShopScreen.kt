package com.arribas.myshoppinglist.ui.view.listArticle

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap.Companion.Square
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.data.model.Article
import com.arribas.myshoppinglist.data.model.ArticleShop
import com.arribas.myshoppinglist.ui.theme.MyShoppingListTheme
import com.arribas.myshoppinglist.ui.view.general.CircleButton
import com.arribas.myshoppinglist.ui.view.general.SimpleAlertDialog
import com.arribas.myshoppinglist.ui.view.listArticleShop.ListArticleShopHeader
import com.arribas.myshoppinglist.ui.viewModel.AppViewModelProvider
import com.arribas.myshoppinglist.ui.viewModel.ListArticleViewModel
import com.arribas.myshoppinglist.ui.viewModel.listArticleShop.ListArticleShopViewModel

@Composable
fun ListArticleShopScreen(
    navigateToItemUpdate: (Int) -> Unit,
    viewModel: ListArticleShopViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier){

    val listUiState by viewModel.listUiState.collectAsState()
    val showDialogState: Boolean by viewModel.showDialog.collectAsState()

    ListArticleShopBody(
        itemList = listUiState.itemList,
        navigateToItemUpdate = { navigateToItemUpdate(it) },
        deleteItem = { viewModel.onOpenDialogClicked(it) },
        updateItem = { viewModel.updateItem(it) }
    )

    SimpleAlertDialog(
        show = showDialogState,
        title = "Please confirm",
        body =  "Should I continue with the requested action?",
        showDismissButton = true,
        onDismiss = viewModel::onDialogDismiss,
        onConfirm = viewModel::onDialogConfirm
    )
}

@Composable
fun ListArticleShopBody(
    itemList: List<ArticleShop>,
    navigateToItemUpdate: (Int) -> Unit,
    deleteItem: (ArticleShop) -> Unit,
    updateItem: (ArticleShop) -> Unit,
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
            InventoryArticleShopList(
                itemList = itemList,
                onItemClick = { navigateToItemUpdate(it.id) },
                onDeleteClick = { deleteItem(it) },
                onCheckClick =  { updateItem(it) },
                onChangeItem = { updateItem(it) }
            )
        }
    }
}

@Composable
private fun InventoryArticleShopList(
    itemList: List<ArticleShop>,
    onItemClick: (ArticleShop) -> Unit,
    onDeleteClick: (ArticleShop) -> Unit,
    onCheckClick: (ArticleShop) -> Unit,
    onChangeItem: (ArticleShop) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp)
    )
    {
        items(items = itemList, key = { it.id }) { item ->
            InventoryArticleShopItem(
                item = item,
                onItemClick = onItemClick,
                onDeleteClick = onDeleteClick,
                onCheckClick = onCheckClick,
                onChangeItem = onChangeItem,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun InventoryArticleShopItem(
    item: ArticleShop,
    onItemClick: (ArticleShop) -> Unit,
    onDeleteClick: (ArticleShop) -> Unit,
    onCheckClick: (ArticleShop) -> Unit,
    onChangeItem: (ArticleShop) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        border = BorderStroke(0.2.dp, Color.Gray),
    ) {
        Row(
            modifier = modifier
                .background(colorResource(R.color.white))
                .padding(8.dp)
        ) {
            var checked = remember { mutableStateOf(item.check) }

            Checkbox(
                checked = checked.value,

                onCheckedChange = { _checked ->
                    checked.value = _checked
                    onCheckClick(item.copy(check = _checked))
                },

                modifier = Modifier
                    .weight(0.3f)
                    .padding(end = 10.dp)
            )

            Text(
                text = item.name,
                modifier = Modifier
                    .weight(2f)
                    .align(Alignment.CenterVertically),

                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )

            Row(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                CircleButton(
                    icon = R.drawable.ic_remove,
                    color = R.color.white,
                    backgroundColor = R.color.my_warning,
                    quantity = item.quantity,
                    description = R.string.bt_remove,
                    onChangeItem = {
                        if (item.quantity - 1 > 0) {
                            onChangeItem(item.copy(quantity = item.quantity - 1))
                        }
                    }
                )

                CircleButton(
                    icon = R.drawable.ic_add,
                    color = R.color.white,
                    backgroundColor = R.color.my_primary,
                    quantity = item.quantity,
                    description = R.string.bt_add,
                    onChangeItem = {
                        onChangeItem(item.copy(quantity = item.quantity + 1))
                    }
                )

                Text(
                    text = item.quantity.toString(),

                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 5.dp, end = 5.dp),

                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
            }

            IconButton(
                onClick = { onDeleteClick(item) },

                modifier = Modifier
                    .weight(0.3f)
                    .width(30.dp)
                    .height(30.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = stringResource(R.string.delete),
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListArticleShopItemPreview() {
    val article = ArticleShop(
        id = 1,
        name = "Bolsa de patatas",
        quantity = 1,
        check = false,
        order = 1
    )

    MyShoppingListTheme {
        InventoryArticleShopItem(
            item = article,
            onItemClick = {},
            onDeleteClick = {},
            onCheckClick = {}
        )
    }
}


