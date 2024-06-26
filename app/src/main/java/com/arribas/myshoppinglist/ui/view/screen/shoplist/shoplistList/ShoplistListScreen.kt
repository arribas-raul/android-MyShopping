package com.arribas.myshoppinglist.ui.view.screen.shoplist.shoplistList

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.data.model.Shoplist
import com.arribas.myshoppinglist.data.utils.DialogUiState
import com.arribas.myshoppinglist.ui.theme.MyShoppingListTheme
import com.arribas.myshoppinglist.ui.view.app.AppViewModelProvider
import com.arribas.myshoppinglist.ui.view.app.topBar.AppBarState
import com.arribas.myshoppinglist.ui.view.general.FloatingButton
import com.arribas.myshoppinglist.ui.view.filter.GeneralFilter
import com.arribas.myshoppinglist.ui.view.bottomsheet.shoplistBottomSheet.ShoplistBottomSheet
import com.arribas.myshoppinglist.ui.view.bottomsheet.shoplistBottomSheet.ShoplistBottomSheetViewModel
import com.arribas.myshoppinglist.ui.view.dialog.general.SimpleAlertDialog
import com.arribas.myshoppinglist.ui.view.screen.shoplist.toShopListUiState
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ShoplistListScreen(
    onComposing: (AppBarState) -> Unit = {},
    onSelectItem: (Shoplist) -> Unit,
    listViewModel: ShoplistListViewModel = viewModel(factory = AppViewModelProvider.Factory),
    detailViewModel: ShoplistBottomSheetViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
){
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val listUiState by listViewModel.listUiState.collectAsState()
    val filterUiState by listViewModel.filterUiState.collectAsState()
    val dialogState: DialogUiState by listViewModel.dialogState.collectAsState()

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )

    val listState = rememberLazyListState()

    val fabVisibility by derivedStateOf {
        listState.firstVisibleItemIndex == 0
    }

    LaunchedEffect(key1 = true) {
        onComposing(
            AppBarState(
                actions = {

                }
            )
        )
    }

    LaunchedEffect(Unit) {
        listViewModel
            .toastMessage
            .collect { message ->
                Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_SHORT,
                ).show()
            }
    }

    Scaffold(
        floatingActionButton = {
            FloatingButton(
                onClick = {
                    scope.launch {
                        detailViewModel.onClearUiState()
                        sheetState.show()
                    }
                },

                fabVisibility = fabVisibility,
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
            ) {
                GeneralFilter(
                    filterUiState = filterUiState,
                    onValueChange = { listViewModel.onSearch(it) },
                    onKeyEvent = { },
                    clearName = listViewModel::onClearName,
                    isTextEmptyVisible = listUiState.itemList.isEmpty()
                )

                ShopListBody(
                    itemList = listUiState.itemList,
                    onDeleteItem = { listViewModel.onDialogDelete(it) },

                    onClickItem  = {
                        detailViewModel.onUpdateUiState(it.toShopListUiState())

                        scope.launch {
                            sheetState.show()
                        }
                    },

                    onEditClick  = { onSelectItem(it) },

                    lazyState = listState
                )
            }
        }
    }

    SimpleAlertDialog(
        dialogState = dialogState,
        onDismiss = listViewModel::onDialogDismiss,
        onConfirm = listViewModel::onDialogConfirm
    )

    ShoplistBottomSheet(
        viewModel = detailViewModel,
        sheetState = sheetState
    )
}

@Composable
fun ShopListBody(
    itemList: List<Shoplist>,
    onDeleteItem: (Shoplist) -> Unit,
    onClickItem: (Shoplist) -> Unit,
    onEditClick: (Shoplist) -> Unit,
    lazyState: LazyListState,
    modifier: Modifier = Modifier){

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 10.dp),
            state = lazyState,
        ){
            items(items = itemList, key = { it.id }) { item ->
                ShoplistListItem(
                    item = item,
                    onItemClick = { onClickItem(it) },
                    onDeleteClick = { onDeleteItem(it) },
                    onEditClick = { onEditClick(it) },
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
private fun ShoplistListItem(
    item: Shoplist,
    onItemClick: (Shoplist) -> Unit,
    onDeleteClick: (Shoplist) -> Unit,
    onEditClick: (Shoplist) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        border = BorderStroke(0.2.dp, Color.Gray),
        shape = RoundedCornerShape(6.dp),
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { onEditClick(item) }
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
                    color = Color.Black,
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
        ShoplistListScreen( onSelectItem = {})
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
            onEditClick = {}
        )
    }
}