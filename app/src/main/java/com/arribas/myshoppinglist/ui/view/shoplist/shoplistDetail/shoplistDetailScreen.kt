package com.arribas.myshoppinglist.ui.view.shoplist.shoplistDetail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.ui.view.AppViewModelProvider
import com.arribas.myshoppinglist.ui.view.app.AppUiState
import com.arribas.myshoppinglist.ui.view.shoplist.shoplistList.ShoplistListViewModel
import com.arribas.myshoppinglist.ui.viewModel.listArticleShop.ShoplistDetailViewModel

@Composable
fun ShoplistDetailScreen(
    appUiState: AppUiState = AppUiState(),
    navigateBack: (() -> Unit)? = null,
    listViewModel: ShoplistDetailViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
){
    //appUiState.title = stringResource(R.string.shoplist_detail_title)

    Text(text = "lista")
}

