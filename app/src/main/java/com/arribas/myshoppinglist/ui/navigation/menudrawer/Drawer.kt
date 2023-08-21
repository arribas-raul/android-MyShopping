package com.arribas.myshoppinglist.ui.navigation.menudrawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navController = rememberNavController()
    val items = MainDataProvider.getMenuData()

    var lastSelectedItem by remember { mutableStateOf(items[0]) }
    var selectedItem by remember { mutableStateOf(items[0]) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet{
                DrawerHeader()

                DrawerBody(
                    items = items,
                    selectedItem = selectedItem,
                    onItemClick = {
                        selectedItem = it
                        onSelectItemNavDrawer(
                            selectedItem, drawerState, scope, navController)
                    }
                )
            }
        }
    ) {
        Content(
            title = selectedItem.text,
            items = items,
            navController = navController,
            selectedItem = selectedItem,
            onClick = { scope.launch { drawerState.open() } },

            onItemClick = {
                val navTag: NavTag = it

                lastSelectedItem = selectedItem
                selectedItem = items.find { item -> item.type == navTag }!!

                onSelectItemNavDrawer(
                    selectedItem, drawerState, scope, navController)
            },

            onSelectItem = {
                val navTag: NavTag = it

                lastSelectedItem = selectedItem
                selectedItem = items.find { item -> item.type == navTag }!!
            },

            navigateUp = {
                selectedItem = lastSelectedItem
                navController.popBackStack()
            }
        )
    }
}

@Composable
fun DrawerHeader(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(vertical = 64.dp)
    ){
        Text(
            text = "Cabecera",
            fontSize = 24.sp,
            color = Color.White
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerBody(
    items: List<NavigationMainItemContent>,
    onItemClick: (NavigationMainItemContent) -> Unit,
    selectedItem: NavigationMainItemContent
){
    items.forEach { item ->
        if(!item.menuLeftVisible){
            return
        }

        NavigationDrawerItem(
            label = { Text( text = item.text ) },
            selected = item == selectedItem,
            onClick = { onItemClick(item) },

            icon = {
                Image(
                    painter = painterResource(item.icon),
                    contentDescription = item.text,
                    colorFilter = ColorFilter.tint(Color.White)
                )
            },

            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(
    title: String,
    navController: NavHostController,
    items: List<NavigationMainItemContent>,
    selectedItem: NavigationMainItemContent,
    onItemClick: (NavTag) -> Unit,
    onSelectItem: (NavTag) -> Unit,
    onClick: () -> Unit,
    navigateUp: () -> Unit,
){
    Scaffold(
        topBar = {
            MyTopBar(
                title = title,
                canNavigateBack = !selectedItem.menuLeftVisible,
                navigateUp = navigateUp,
                onClickDrawer = onClick,
                tag = NavTag.SHOP_LIST
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

        MyAppNavHost(
            navController = navController,
            modifier = Modifier.padding(padding),
            onItemClick = { onSelectItem(it) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun onSelectItemNavDrawer(
    selectedItem: NavigationMainItemContent,
    drawerState: DrawerState,
    scope: CoroutineScope,
    navController: NavHostController
){
    scope.launch { drawerState.close() }

    when(selectedItem.type){
        NavTag.SHOP_LIST    -> navController.navigate(Routes.ShopListScreen.route)
        NavTag.ITEM_LIST    -> navController.navigate(Routes.ArticleListScreen.route)
        NavTag.NEW_ITEM     -> navController.navigate(Routes.ArticleNewScreen.route)
        NavTag.DETAIL_ITEM  -> navController.navigate(Routes.ArticleDetailScreen.routeWithArgs)
    }
}