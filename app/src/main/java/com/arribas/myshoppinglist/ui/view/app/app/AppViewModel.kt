package com.arribas.myshoppinglist.ui.view.app.app

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arribas.myshoppinglist.data.utils.PreferencesEnum
import com.arribas.myshoppinglist.data.utils.PreferencesManager
import com.arribas.myshoppinglist.ui.navigation.navigationDrawer.ItemNavigationDrawer
import com.arribas.myshoppinglist.ui.navigation.navigationDrawer.NavigationDrawerProvider
import com.arribas.myshoppinglist.ui.navigation.route.RouteEnum
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AppViewModel(context: Context) : ViewModel() {

    private val _appUiState = MutableStateFlow(AppUiState())
    val appUiState: StateFlow<AppUiState> = _appUiState

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    var menuItems: List<ItemNavigationDrawer>

    var selectedItem by mutableStateOf(ItemNavigationDrawer())
        private set

    init{
        menuItems = NavigationDrawerProvider.getData(context)

        appUiState.value.setup(menuItems.first())
        selectedItem = menuItems.first()
        viewModelScope.launch {
            val title = PreferencesManager(context)
                .getData(PreferencesEnum.MAIN_LIST_NAME.toString(), "")

            if (title !== "") {
                _appUiState.value.selectedItem.text = title
                onUpdateTitle(title)
            }
        }
    }

    fun onUpdateTitle(title: String){
        _title.value = title
    }

    fun changeSelectedItem(item: ItemNavigationDrawer){
        selectedItem = item
        _appUiState.value.selectedItem = item
    }

    fun changeFindSelectedItem(routeEnum: RouteEnum){
        changeSelectedItem(findItem(routeEnum))
    }

    /**Private functions******************************/
    private fun findItem(routeEnum: RouteEnum): ItemNavigationDrawer{
        val item: ItemNavigationDrawer? = menuItems.find { item -> item.type == routeEnum }

        return item ?: menuItems.first()
    }
}

data class AppUiState(
    var title: String = "",
    var lastSelectedItems: MutableList<ItemNavigationDrawer> = arrayListOf(),
    var selectedItem: ItemNavigationDrawer = ItemNavigationDrawer()
){
    fun setup(item: ItemNavigationDrawer){
        addItem(item)
        title = item.text
        selectedItem = item
    }
    fun addFirstItem(item: ItemNavigationDrawer) {
        lastSelectedItems.clear()
        lastSelectedItems.add(item)
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

    fun updateActualTitle(newTitle: String){
        if(lastSelectedItems.size > 0){
            lastSelectedItems.last().text = newTitle
        }

        title = newTitle
    }

    fun popItem(): ItemNavigationDrawer {
        lastSelectedItems.removeLast()

        return lastSelectedItems.last()
    }
}

