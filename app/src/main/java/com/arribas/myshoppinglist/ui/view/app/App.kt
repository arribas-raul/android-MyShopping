package com.arribas.myshoppinglist.ui.view

import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.arribas.myshoppinglist.ui.navigation.MyAppNavHost
import com.arribas.myshoppinglist.ui.navigation.navigate
import com.arribas.myshoppinglist.ui.navigation.navigationDrawer.ItemNavigationDrawer
import com.arribas.myshoppinglist.ui.view.general.MyBottomBar
import com.arribas.myshoppinglist.ui.navigation.navigationDrawer.NavigationDrawer
import com.arribas.myshoppinglist.ui.navigation.navigationDrawer.NavigationDrawerProvider
import com.arribas.myshoppinglist.ui.navigation.route.RouteEnum
import com.arribas.myshoppinglist.ui.view.app.AppUiState
import com.arribas.myshoppinglist.ui.view.app.AppViewModel
import com.arribas.myshoppinglist.ui.view.general.MyTopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    appViewModel: AppViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
){

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navController = rememberNavController()
    val items = NavigationDrawerProvider.getData()

    var lastSelectedItem by remember { mutableStateOf(items[0]) }
    var selectedItem by remember { mutableStateOf(items[0]) }

    val appUiState by appViewModel.appUiState.collectAsState()

    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,

        drawerContent = {
            NavigationDrawer(
                context = context,
                appUiState = appUiState,
                items = items,
                selectedItem = selectedItem,
                onSelectedItem = { selectedItem = it },
                drawerState = drawerState,
                scope = scope,
                navController = navController
            )
        }
    ) {
        Content(
            appViewModel = appViewModel,
            title = appUiState.title,
            items = items,
            navController = navController,
            selectedItem = selectedItem,
            onClick = { scope.launch { drawerState.open() } },

            onItemClick = {
                val routeEnum: RouteEnum = it

                lastSelectedItem = selectedItem
                selectedItem = items.find { item -> item.type == routeEnum }!!

                onSelectItemNavDrawer(
                    context,
                    appUiState,
                    selectedItem,
                    drawerState,
                    scope,
                    navController)
            },

            onSelectItem = {
                val routeEnum: RouteEnum = it

                lastSelectedItem = selectedItem
                selectedItem = items.find { item -> item.type == routeEnum }!!
            },

            navigateUp = {
                selectedItem = lastSelectedItem
                navController.popBackStack()
            },

            modifier = modifier
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(
    appViewModel: AppViewModel,
    title: String,
    navController: NavHostController,
    items: List<ItemNavigationDrawer>,
    selectedItem: ItemNavigationDrawer,
    onItemClick: (RouteEnum) -> Unit,
    onSelectItem: (RouteEnum) -> Unit,
    onClick: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
){

    Scaffold(
        topBar = {
            MyTopBar(
                title = title,
                canNavigateBack = !selectedItem.menuLeftVisible,
                navigateUp = navigateUp,
                onClickDrawer = onClick,
                tag = RouteEnum.SHOP_LIST
            )
        },

        bottomBar = {
            MyBottomBar(
                currentTab =  selectedItem.type,
                onTabPressed = { onItemClick(it) },
                navigationItemContentList = items
            )
        }
    ) { padding ->
        Row(modifier = modifier
            .wrapContentWidth()
            .padding(padding)) {
            MyAppNavHost(
                appViewModel = appViewModel,
                navController = navController,
                onItemClick = { onSelectItem(it) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun onSelectItemNavDrawer(
    context: Context,
    appUiState: AppUiState,
    selectedItem: ItemNavigationDrawer,
    drawerState: DrawerState,
    scope: CoroutineScope,
    navController: NavHostController
){
    scope.launch { drawerState.close() }

    navigate(
        context = context,
        appUiState = appUiState,
        item = selectedItem,
        navController = navController
    )
}