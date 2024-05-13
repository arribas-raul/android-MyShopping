package com.arribas.myshoppinglist.ui.view.screen.article.articleList

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.data.model.QArticle
import com.arribas.myshoppinglist.data.utils.DialogUiState
import com.arribas.myshoppinglist.ui.theme.MyShoppingListTheme
import com.arribas.myshoppinglist.ui.view.app.AppViewModelProvider
import com.arribas.myshoppinglist.ui.view.app.topBar.AppBarState
import com.arribas.myshoppinglist.ui.view.dialog.general.SimpleAlertDialog
import com.arribas.myshoppinglist.ui.view.screen.category.CategoryViewModel
import com.arribas.myshoppinglist.ui.view.screen.category.ListCategoryUiState
import com.arribas.myshoppinglist.ui.view.shoplistList.ListArticleHeader

enum class SidePanelState {
    Open, Closed
}

@Composable
fun ArticleListScreen(
    onComposing: (AppBarState) -> Unit,
    navigateBack: () -> Unit,
    onSelectItem: (QArticle) -> Unit,
    viewModel: ListArticleViewModel = viewModel(factory = AppViewModelProvider.Factory),
    categoryViewModel: CategoryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier){

    val listUiState by viewModel.listUiState.collectAsState()
    val searchUiState by viewModel.searchUiState.collectAsState()
    val dialogState: DialogUiState by viewModel.dialogState.collectAsState()
    val drawerState = remember { mutableStateOf(SidePanelState.Closed) }
    val categoryListUiState by categoryViewModel.listUiState.collectAsState()

    LaunchedEffect(key1 = true) {
        onComposing(
            AppBarState(
                actions = {
                    IconButton(
                        onClick = {
                            if(drawerState.value == SidePanelState.Closed){
                                drawerState.value = SidePanelState.Open
                            }else{
                                drawerState.value = SidePanelState.Closed
                            }
                        }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_filter),
                            contentDescription = "Filtro",
                            colorFilter = ColorFilter.tint(Color.White),
                        )
                    }
                }
            )
        )
    }

    Column(
        Modifier.fillMaxWidth()) {

        ListArticleHeader(
            searchUiState = searchUiState,
            onValueChange = { viewModel.search(it) },
            clearName = viewModel::clearName
        )

        ListArticleBody(
            itemList = listUiState.itemList,
            onSelectItem = { onSelectItem(it) },
            deleteItem = { viewModel.onDialogDelete(it) },
            updateItem = { viewModel.updateItem(it) }
        )
    }

    if(drawerState.value == SidePanelState.Open) {
        val menuWidth = 250.dp
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp
        val offsetMax = screenWidth - menuWidth

        Box(
            Modifier
                .offset(x = offsetMax)
        ) {
            LateralRightMenu(
                searchUiState = searchUiState,
                width = menuWidth,
                categoryListUiState = categoryListUiState,
                onItemValueChange = {
                    drawerState.value = SidePanelState.Closed
                    viewModel.searchByCategory(it.category)
                },
            )
        }
    }

    SimpleAlertDialog(
        dialogState = dialogState,
        onDismiss = viewModel::onDialogDismiss,
        onConfirm = viewModel::onDialogConfirm
    )
}

@Composable
fun ListArticleBody(
    itemList: List<QArticle>,
    onSelectItem: (QArticle) -> Unit,
    deleteItem: (QArticle) -> Unit,
    updateItem: (QArticle) -> Unit,
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
                onItemClick = { onSelectItem(it) },
                onDeleteClick = { deleteItem(it) },
                onCheckClick =  { updateItem(it) }
            )
        }
    }
}

@Composable
private fun InventoryList(
    itemList: List<QArticle>,
    onItemClick: (QArticle) -> Unit,
    onDeleteClick: (QArticle) -> Unit,
    onCheckClick: (QArticle) -> Unit,
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
    item: QArticle,
    onItemClick: (QArticle) -> Unit,
    onDeleteClick: (QArticle) -> Unit,
    onCheckClick: (QArticle) -> Unit,
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

            if(item.shoplist_id > 0) {
                Image(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = stringResource(R.string.update),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
            }

            Column(modifier = Modifier
                .weight(2f)
                .align(Alignment.CenterVertically)) {

                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )

                LazyRow(
                    modifier = modifier.padding(top = 5.dp, bottom = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(space = 3.dp)
                ) {
                    val categories: List<String>? = item.category?.split(",")
                    if (categories != null) {
                        if(categories.isNotEmpty()) {
                            items(categories) { category ->
                                Text(
                                    text = category,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color.Black,
                                    modifier = Modifier
                                        .background(
                                            color = Color.LightGray,
                                            shape = RoundedCornerShape(size = 10.dp)
                                        )
                                        .padding(start = 8.dp, end = 8.dp), // add inner padding
                                )
                            }
                        }
                    }
                }
            }



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
    val article = QArticle(
        id = 1,
        shoplist_id = 1,
        name = "Bolsa de patatas",
        category = "Fruta, Carne",
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LateralRightMenu(
    searchUiState: SearchUiState,
    width: Dp,
    categoryListUiState: ListCategoryUiState,
    onItemValueChange: (SearchUiState) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .background(colorResource(R.color.my_primary))
            .width(width)
            .fillMaxHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            var expanded by remember { mutableStateOf(false) }
            var selectedOptionText by remember { mutableStateOf(searchUiState.category) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    readOnly = true,
                    value = categoryListUiState?.itemList?.find {
                        it.id === searchUiState.category }?.name.orEmpty(),
                    onValueChange = { },
                    label = { Text("Categorias") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxWidth()
                        .menuAnchor()
                        .background(colorResource(id = R.color.white))
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                    modifier = Modifier.background(colorResource(id = R.color.my_primary))
                ) {
                    DropdownMenuItem(
                        onClick = {
                            selectedOptionText = 0
                            onItemValueChange(searchUiState.copy(category = 0))
                            expanded = false
                        },
                        text = { Text(text = "") }
                    )
                    categoryListUiState.itemList.forEachIndexed() { index, category ->
                        DropdownMenuItem(
                            onClick = {
                                selectedOptionText = category.id
                                onItemValueChange(searchUiState.copy(category = category.id))
                                expanded = false
                            },
                            text = { Text(text = category.name) }
                        )
                    }
                }
            }
        }
    }
}


