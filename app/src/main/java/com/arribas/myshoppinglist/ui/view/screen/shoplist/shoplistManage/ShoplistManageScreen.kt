package com.arribas.myshoppinglist.ui.view.screen.shoplist.shoplistManage

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.data.model.ArticleShop
import com.arribas.myshoppinglist.data.model.ShoplistArticle
import com.arribas.myshoppinglist.data.utils.DialogUiState
import com.arribas.myshoppinglist.ui.theme.MyShoppingListTheme
import com.arribas.myshoppinglist.ui.view.app.AppViewModelProvider
import com.arribas.myshoppinglist.ui.view.app.app.AppUiState
import com.arribas.myshoppinglist.ui.view.app.topBar.AppBarState
import com.arribas.myshoppinglist.ui.view.general.CircleButton
import com.arribas.myshoppinglist.ui.view.general.FloatingButton
import com.arribas.myshoppinglist.ui.view.filter.GeneralFilter
import com.arribas.myshoppinglist.ui.view.filter.GeneralFilterUiState
import com.arribas.myshoppinglist.ui.view.general.SimpleAlertDialog
import com.arribas.myshoppinglist.ui.view.screen.listArticleShop.SearchListArticleUiState
import com.arribas.myshoppinglist.ui.view.screen.shoplist.ShoplistUiState
import com.arribas.myshoppinglist.ui.view.screen.shoplist.shoplistDetail.ShoplistDetailHeader
import com.arribas.myshoppinglist.ui.view.screen.shoplist.shoplistDetail.ShoplistSelectDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import kotlin.time.Duration.Companion.seconds


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun ShoplistManageScreen(
    onComposing: (AppBarState) -> Unit = {},
    navigateBack: (() -> Unit)? = null,
    listViewModel: ShoplistManageViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onSelectItem: (ShoplistUiState) -> Unit,
    onUpdateTitle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val listUiState by listViewModel.listUiState.collectAsState()
    val filterUiState by listViewModel.filterUiState.collectAsState()
    val dialogState: DialogUiState by listViewModel.dialogState.collectAsState()
    val showDialog =  remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        onComposing(
            AppBarState(
                actions = {
                    IconButton(onClick = { showDialog.value = true}) {
                        Icon(
                            imageVector = Icons.Rounded.ShoppingCart,
                            contentDescription = "",
                            tint = Color.White,
                        )
                    }
                }
            )
        )
    }

    SimpleAlertDialog(
        dialogState = dialogState,
        onDismiss = listViewModel::onDialogDismiss,
        onConfirm = listViewModel::onDialogConfirm
    )

    if(showDialog.value)
        ShoplistSelectDialog(
            value = listViewModel.shoplistUiState,
            onItemValueChange = {
                listViewModel.setShoplist(it)
                onUpdateTitle(it.name)
                showDialog.value = false
            },
            setShowDialog = {
                showDialog.value = false
            }
        )

    Scaffold(
        modifier = modifier

    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .background(colorResource(R.color.my_background))
        ) {
            if (listViewModel.shoplistUiState.id == 0) {
                ShoplistListTitle(
                    title = stringResource(R.string.shoplist_manage_not_select_list),
                    modifier = modifier.padding(8.dp)
                )
            }

            ShoplistManageHeader(
                listUiState = listUiState,
                onResetBt = { listViewModel.onDialogReset() },
                modifier = Modifier.padding(8.dp)
            )

            ShoplistDetailBody(
                listUiState = listUiState,
                filterUiState = filterUiState,
                isVisibleHeader = listViewModel.shoplistUiState.existElement(),
                navigateToItemUpdate = {  },
                deleteItem = { listViewModel.onDialogDelete(it) },
                updateItem = { listViewModel.onUpdateItem(it) },
                onSearch = { listViewModel.onSearch(it) },
                onReset = {  },
                onClearName = { },
                onReorderItems = listViewModel::onReorderItems,
                onCheckFilter = { }
            )
        }
    }
}

@Composable
fun ShoplistListTitle(
    title: String,
    modifier: Modifier
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(size = 5.dp)
            )
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black,
            modifier = modifier
                .padding(vertical = 4.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun ShoplistDetailBody(
    listUiState: ShoplistManageUiState,
    filterUiState: GeneralFilterUiState,
    isVisibleHeader: Boolean,
    navigateToItemUpdate: (Int) -> Unit,
    deleteItem: (article: ShoplistArticle) -> Unit,
    updateItem: (ShoplistArticle) -> Unit,
    onSearch: (String) -> Unit,
    onReset: () -> Unit,
    onClearName: () -> Unit,
    onReorderItems: (to: Int, from:Int) -> Unit,
    onCheckFilter: (SearchListArticleUiState) -> Unit = {},
    modifier: Modifier = Modifier)
{
    Column(
        modifier = modifier
    ) {

        if (listUiState.itemList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_item_description),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(16.dp)
            )

        } else {
            ShoplistManagelList(
                itemList = listUiState.itemList,
                onItemClick = { navigateToItemUpdate(it.id) },
                onDeleteClick = { deleteItem(it) },
                onCheckClick =  { updateItem(it) },
                onChangeItem = { updateItem(it) },
                onReorderItems = onReorderItems
            )
        }
    }
}

@Composable
private fun ShoplistManagelList(
    itemList: List<ShoplistArticle>,
    onItemClick: (ShoplistArticle) -> Unit,
    onDeleteClick: (ShoplistArticle) -> Unit,
    onCheckClick: (ShoplistArticle) -> Unit,
    onChangeItem: (ShoplistArticle) -> Unit,
    onReorderItems: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
){
    val data = remember { mutableStateOf(itemList) }
    val state = rememberReorderableLazyListState(
        onMove = { to, from ->
            data.value = data.value.toMutableList().apply {
                add(to.index, removeAt(from.index))
                onReorderItems(to.index, from.index)
            }
        },

        canDragOver = {draggedOver, dragging ->
            data.value.getOrNull(draggedOver.index)?.isLocked != true

        }
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
        state = state.listState,
        modifier = modifier
            .reorderable(state)
    )
    {
        items(items = itemList, key = { it.id }) { item ->
            ReorderableItem(state, key = item.id, defaultDraggingModifier = Modifier) { isDragging ->
                ShoplistManageItem(
                    item = item,
                    onItemClick = onItemClick,
                    onDeleteClick = { onDeleteClick(it) },
                    onCheckClick = { onCheckClick(it) },
                    onChangeItem = { onChangeItem(it) },
                    modifier = Modifier.detectReorderAfterLongPress(state)
                )
            }
        }
    }
}

@Composable
private fun ShoplistManageItem(
    item: ShoplistArticle,
    onItemClick: (ShoplistArticle) -> Unit,
    onDeleteClick: (ShoplistArticle) -> Unit,
    onCheckClick: (ShoplistArticle) -> Unit,
    onChangeItem: (ShoplistArticle) -> Unit = {},
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
            var checked by remember { mutableStateOf(item.check) }

            Checkbox(
                checked = item.check,

                onCheckedChange = { _checked ->
                    checked = _checked
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
                    .weight(0.8f)
                    .align(Alignment.CenterVertically)
            ) {
                CircleButton(
                    icon = R.drawable.ic_remove,
                    color = R.color.white,
                    backgroundColor = R.color.my_warning,
                    quantity = item.quantity,
                    description = R.string.bt_remove,
                    onChangeItem = {
                        //if (item.quantity - 1 > 0) {
                        onChangeItem(item.copy(quantity = item.quantity - 1))
                        //}
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
                        .padding(start = 5.dp),

                    textAlign = TextAlign.Right,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
            }

            /*IconButton(
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
            }*/
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShoplistDetailItemPreview() {
    val article = ShoplistArticle(
        id = 1,
        name = "Bolsa de patatas",
        article_id = 8,
        shoplist_id = 19,
        quantity = 1,
        check = false,
        order = 1
    )

    MyShoppingListTheme {
        ShoplistManageItem(
            item = article,
            onItemClick = {},
            onDeleteClick = {},
            onCheckClick = {},
        )
    }
}

