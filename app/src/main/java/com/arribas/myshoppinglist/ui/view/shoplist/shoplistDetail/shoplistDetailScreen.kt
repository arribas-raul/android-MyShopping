package com.arribas.myshoppinglist.ui.view.shoplist.shoplistDetail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arribas.myshoppinglist.ui.view.AppViewModelProvider
import com.arribas.myshoppinglist.ui.view.app.topBar.AppBarState
import com.arribas.myshoppinglist.ui.viewModel.listArticleShop.ShoplistDetailViewModel

@Composable
fun ShoplistDetailScreen(
    onComposing: (AppBarState) -> Unit = {},
    navigateBack: (() -> Unit)? = null,
    listViewModel: ShoplistDetailViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {

    LaunchedEffect(key1 = true) {
        onComposing(
            AppBarState(
                actions = {

                }
            )
        )
    }

    if (listViewModel.shoplistUiState.id == 0){
        Text(text = "No hay ninguna lista selecionada")
    }
}

