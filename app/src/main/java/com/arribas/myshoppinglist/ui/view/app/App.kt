package com.arribas.myshoppinglist.ui.view.app

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
import com.arribas.myshoppinglist.ui.navigation.route.RouteEnum
import com.arribas.myshoppinglist.ui.view.AppViewModelProvider
import com.arribas.myshoppinglist.ui.view.app.topBar.AppBarState
import com.arribas.myshoppinglist.ui.view.app.topBar.MyTopBar
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
    var selectedItem by remember { mutableStateOf(appViewModel.menuItems.first()) }

    val appUiState by appViewModel.appUiState.collectAsState()

    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,

        drawerContent = {
            NavigationDrawer(
                context = context,
                appUiState = appUiState,
                items = appViewModel.menuItems,
                selectedItem = selectedItem,
                onSelectedItem = { selectedItem = it },
                drawerState = drawerState,
                scope = scope,
                navController = navController
            )
        }
    ) {
        Content(
            appUiState = appUiState,
            title = appUiState.title,
            items = appViewModel.menuItems,
            navController = navController,
            selectedItem = selectedItem,
            onClick = { scope.launch { drawerState.open() } },

            onItemClick = {
                val routeEnum: RouteEnum = it

                //selectedItem = appViewModel.menuItems.find { item -> item.type == routeEnum }!!
                selectedItem = appViewModel.findItem(routeEnum)

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

                //selectedItem = appViewModel.menuItems.find { item -> item.type == routeEnum }!!
                selectedItem = appViewModel.findItem(routeEnum)

                appUiState.lastSelectedItems.add(selectedItem)
            },

            navigateUp = {

                appUiState.lastSelectedItems.removeLast()
                selectedItem = appUiState.lastSelectedItems.last()
                appUiState.title = appUiState.actualTitle()

                navController.popBackStack()
            },

            modifier = modifier
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(
    appUiState: AppUiState,
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

    var appBarState by remember {
        mutableStateOf(AppBarState())
    }

    Scaffold(
        topBar = {
            MyTopBar(
                title = title,
                appBarState = appBarState,
                canNavigateBack = !selectedItem.menuLeftVisible || appUiState.isLastItem(),
                navigateUp = navigateUp,
                onClickDrawer = onClick
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
                appUiState = appUiState,
                appBarState = appBarState,
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

    appUiState.lastSelectedItems.clear()
    appUiState.addItem(selectedItem)

    navigate(
        context = context,
        appUiState = appUiState,
        item = selectedItem,
        navController = navController
    )
}