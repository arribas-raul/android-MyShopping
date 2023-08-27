package com.arribas.myshoppinglist.ui.view.shoplist.shoplistDetail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arribas.myshoppinglist.ui.view.AppViewModelProvider
import com.arribas.myshoppinglist.ui.view.shoplist.shoplistList.ShoplistListViewModel
import com.arribas.myshoppinglist.ui.viewModel.listArticleShop.ShoplistDetailViewModel

@Composable
fun ShoplistDetailScreen(
    navigateBack: (() -> Unit)? = null,
    listViewModel: ShoplistDetailViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
){
    Text(text = "lista")
}