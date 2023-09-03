package com.arribas.myshoppinglist.ui.view.app

import android.content.Context
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.data.model.QArticle
import com.arribas.myshoppinglist.ui.navigation.navigationDrawer.ItemNavigationDrawer
import com.arribas.myshoppinglist.ui.navigation.navigationDrawer.NavigationDrawerProvider
import com.arribas.myshoppinglist.ui.navigation.route.RouteEnum
import com.arribas.myshoppinglist.ui.view.article.articleList.ListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppViewModel(context: Context) : ViewModel() {

    private val _appUiState = MutableStateFlow(AppUiState())
    val appUiState: StateFlow<AppUiState> = _appUiState
    var menuItems: List<ItemNavigationDrawer>

    init{
        appUiState.value.title = context.resources.getString(R.string.shoplist_detail_title)
        menuItems = NavigationDrawerProvider.getData()
    }

    fun findItem(routeEnum: RouteEnum): ItemNavigationDrawer{
        val item: ItemNavigationDrawer? =  menuItems.find { item -> item.type == routeEnum }

        return item ?: menuItems.first()
    }
}

data class AppUiState(
    var title: String = "",
    var lastSelectedItems: MutableList<ItemNavigationDrawer> = arrayListOf()
){
    init{
        addItem(NavigationDrawerProvider.getData().first())
    }

    fun isLastItem(): Boolean{
        return lastSelectedItems.count() > 1
    }

    fun addItem(item: ItemNavigationDrawer){
        lastSelectedItems.add(item)
    }

    fun actualTitle(): String{
        if(lastSelectedItems.size > 0){
            return lastSelectedItems.last().text
        }

        return title
    }
}

