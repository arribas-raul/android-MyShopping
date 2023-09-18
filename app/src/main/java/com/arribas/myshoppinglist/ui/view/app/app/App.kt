package com.arribas.myshoppinglist.ui.view.app.app

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.arribas.myshoppinglist.ui.navigation.MyAppNavHost
import com.arribas.myshoppinglist.ui.navigation.navigationDrawer.ItemNavigationDrawer
import com.arribas.myshoppinglist.ui.view.app.bottomBar.MyBottomBar
import com.arribas.myshoppinglist.ui.navigation.navigationDrawer.NavigationDrawer
import com.arribas.myshoppinglist.ui.navigation.route.RouteEnum
import com.arribas.myshoppinglist.ui.navigation.route.Routes
import com.arribas.myshoppinglist.ui.view.app.AppViewModelProvider
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

    val appUiState by appViewModel.appUiState.collectAsState()
    val title by appViewModel.title.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,

        drawerContent = {
            NavigationDrawer(
                items = appViewModel.menuItems,
                selectedItem = appViewModel.selectedItem,

                onNavigatePressed = {
                    appViewModel.changeSelectedItem(it)

                    onSelectItemNavDrawer(
                        appUiState,
                        it,
                        drawerState,
                        scope,
                        navController)
                }
            )
        }
    ) {
        Content(
            onUpdateTitle = {appViewModel.onUpdateTitle(it)},
            title = title,
            appUiState = appUiState,
            items = appViewModel.menuItems,
            navController = navController,
            selectedItem = appViewModel.selectedItem,

            onClickDrawer = {
                scope.launch { drawerState.open() }
            },

            onTabPressed = {
                appViewModel.changeSelectedItem(it)

                onSelectItemNavDrawer(
                    appUiState,
                    appViewModel.selectedItem,
                    drawerState,
                    scope,
                    navController)
            },

            onSelectItem = {
                val routeEnum: RouteEnum = it

                appViewModel.changeFindSelectedItem(routeEnum)

                appUiState.lastSelectedItems.add(appViewModel.selectedItem)
            },

            navigateUp = {
                appViewModel.changeSelectedItem(appUiState.popItem())
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
    onUpdateTitle: (String) ->Unit,
    title: String,
    appUiState: AppUiState,
    navController: NavHostController,
    items: List<ItemNavigationDrawer>,
    selectedItem: ItemNavigationDrawer,
    onTabPressed: (ItemNavigationDrawer) -> Unit,
    onSelectItem: (RouteEnum) -> Unit,
    onClickDrawer: () -> Unit,
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
                onClickDrawer = onClickDrawer
            )
        },

        bottomBar = {
            MyBottomBar(
                currentTab =  selectedItem.type,
                onTabPressed = { onTabPressed(it) },
                navigationItemContentList = items
            )
        }

    ) { padding ->
        Row(modifier = modifier
            .wrapContentWidth()
            .padding(padding)) {

            MyAppNavHost(
                onUpdateTitle = { onUpdateTitle(it) },
                appUiState = appUiState,
                appBarState = appBarState,
                navController = navController,
                onSelectItem = { onSelectItem(it) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun onSelectItemNavDrawer(
    appUiState: AppUiState,
    selectedItem: ItemNavigationDrawer,
    drawerState: DrawerState,
    scope: CoroutineScope,
    navController: NavHostController
){
    scope.launch { drawerState.close() }

    appUiState.addFirstItem(selectedItem)

    Routes.navigate(
        item = selectedItem,
        navController = navController
    )
}