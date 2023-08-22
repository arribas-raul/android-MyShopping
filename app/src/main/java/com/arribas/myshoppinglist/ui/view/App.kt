package com.arribas.myshoppinglist.ui.navigation

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.arribas.myshoppinglist.ui.navigation.navigationDrawer.ItemNavigationDrawer
import com.arribas.myshoppinglist.ui.navigation.navigationDrawer.MyBottomBar
import com.arribas.myshoppinglist.ui.navigation.navigationDrawer.NavigationDrawer
import com.arribas.myshoppinglist.ui.navigation.navigationDrawer.NavigationDrawerProvider
import com.arribas.myshoppinglist.ui.navigation.route.RouteEnum
import com.arribas.myshoppinglist.ui.navigation.route.Routes
import com.arribas.myshoppinglist.ui.view.general.MyTopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(modifier: Modifier = Modifier){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navController = rememberNavController()
    val items = NavigationDrawerProvider.getData()

    var lastSelectedItem by remember { mutableStateOf(items[0]) }
    var selectedItem by remember { mutableStateOf(items[0]) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,

        drawerContent = {
            NavigationDrawer(
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
            title = selectedItem.text,
            items = items,
            navController = navController,
            selectedItem = selectedItem,
            onClick = { scope.launch { drawerState.open() } },

            onItemClick = {
                val routeEnum: RouteEnum = it

                lastSelectedItem = selectedItem
                selectedItem = items.find { item -> item.type == routeEnum }!!

                onSelectItemNavDrawer(
                    selectedItem, drawerState, scope, navController)
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
                navController = navController,
                onItemClick = { onSelectItem(it) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun onSelectItemNavDrawer(
    selectedItem: ItemNavigationDrawer,
    drawerState: DrawerState,
    scope: CoroutineScope,
    navController: NavHostController
){
    scope.launch { drawerState.close() }

    navigate(
        item = selectedItem,
        navController = navController
    )
}