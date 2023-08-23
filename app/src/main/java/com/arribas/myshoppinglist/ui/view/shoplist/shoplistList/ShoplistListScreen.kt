package com.arribas.myshoppinglist.ui.view.shoplist.shoplistList

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.arribas.myshoppinglist.data.model.Shoplist
import com.arribas.myshoppinglist.ui.theme.MyShoppingListTheme
import com.arribas.myshoppinglist.ui.view.AppViewModelProvider
import com.arribas.myshoppinglist.ui.view.general.FloatingButton
import com.arribas.myshoppinglist.ui.view.shoplist.shoplistDetail.ShoplistBottomSheet
import com.arribas.myshoppinglist.ui.view.shoplistList.SearchName
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ShoplistListScreen(
    viewModel: ShoplistListViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
){
    val listUiState by viewModel.listUiState.collectAsState()
    val searchUiState by viewModel.searchUiState.collectAsState()

    val scope = rememberCoroutineScope()

    val showModalSheet = rememberSaveable {
        mutableStateOf(false)
    }

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )

    Scaffold(
        floatingActionButton = {
            FloatingButton(
                onClick = { showModalSheet.value = true

                    scope.launch {
                        sheetState.show()
                    }}
            )
        },
        modifier = modifier
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .background(colorResource(R.color.my_background))
        ) {

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {

                SearchName(
                    searchUiState = searchUiState,
                    onValueChange = { viewModel.search(it) },
                    onKeyEvent = { },
                    clearName = viewModel::clearName
                )

                ShopListBody(
                    itemList = listUiState.itemList,
                    deleteItem = { viewModel.onDialogDelete(it) },
                    updateItem = { viewModel.updateItem(it) }
                )
            }
        }
    }

    ShoplistBottomSheet(
        sheetState = sheetState,
        showModalSheet = showModalSheet
    )
}

@Composable
fun ShopListBody(
    itemList: List<Shoplist>,
    deleteItem: (Shoplist) -> Unit,
    updateItem: (Shoplist) -> Unit,
    modifier: Modifier = Modifier){

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (itemList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_item_description),
                style = MaterialTheme.typography.labelMedium,
                modifier = modifier.padding(horizontal = 8.dp),
            )

        } else {
            LazyColumn(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp)
            )
            {
                items(items = itemList, key = { it.id }) { item ->
                    ShoplistListItem(
                        item = item,
                        onItemClick = { },
                        onDeleteClick = { deleteItem(it) },
                        onCheckClick = { updateItem(it) },
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Composable
private fun ShoplistListItem(
    item: Shoplist,
    onItemClick: (Shoplist) -> Unit,
    onDeleteClick: (Shoplist) -> Unit,
    onCheckClick: (Shoplist) -> Unit,
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
            Column(
                modifier = Modifier
                    .weight(2f)
                    .align(Alignment.CenterVertically)
            ) {

                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )

                Text(
                    text = item.type,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )
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
fun ShoplistPreview() {
    MyShoppingListTheme {
        ShoplistListScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ShoplistListItemPreview() {
    val shoplist = Shoplist(
        id = 1,
        name = "Lista d ela compra",
        type = "Lista de la compra de la semana"
    )

    MyShoppingListTheme {
        ShoplistListItem(
            item = shoplist,
            onItemClick = {},
            onDeleteClick = {},
            onCheckClick = {}
        )
    }
}