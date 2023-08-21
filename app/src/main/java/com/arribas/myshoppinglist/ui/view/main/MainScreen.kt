package com.arribas.myshoppinglist.ui.view.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.data.MainDataProvider
import com.arribas.myshoppinglist.data.MainTag
import com.arribas.myshoppinglist.data.NavigationMainItemContent
import com.arribas.myshoppinglist.ui.navigation.NavigationDestination
import com.arribas.myshoppinglist.ui.navigation.menudrawer.Routes
import com.arribas.myshoppinglist.ui.view.TopBar
import com.arribas.myshoppinglist.ui.view.detailArticle.NewScreen
import com.arribas.myshoppinglist.ui.view.listArticle.ListArticleScreen
import com.arribas.myshoppinglist.ui.view.listArticle.ListArticleShopScreen
import com.arribas.myshoppinglist.ui.viewModel.MainUiState
import com.arribas.myshoppinglist.ui.viewModel.MainViewModel

object MainDestination : NavigationDestination {
    override val route = "main"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
){
    val uiState by viewModel.uiState.collectAsState()
    val navigationItemContentList = MainDataProvider.getMainData()

    Scaffold(
        topBar = {
            TopBar(
                title = when(uiState.currentTab){
                    MainTag.SHOP_LIST -> "Lista de la compra"
                    MainTag.ITEM_LIST -> "Lista de productos"
                    MainTag.NEW_ITEM  -> "Nuevo producto"
                },
                tag = uiState.currentTab,
                onClickDrawer = {}
            )
        },

        bottomBar = {
            BottomBarMain(
                currentTab = uiState.currentTab,
                onTabPressed = { type: MainTag ->
                    viewModel.updateCurrentTypeList(tab = type)
                },
                navigationItemContentList = navigationItemContentList,
                modifier = modifier
            )
        }

    ) { innerPadding ->
        Row(modifier = modifier
            .wrapContentWidth()
            .padding(innerPadding)) {

            when(uiState.currentTab){
                MainTag.SHOP_LIST -> {
                    ListArticleShopScreen(
                        navigateToItemUpdate = {
                            navController.navigate("${Routes.ArticleDetailScreen.route}/${it}")
                        }
                    )
                }

                MainTag.ITEM_LIST -> {
                    ListArticleScreen(
                        navigateToItemUpdate = {
                            navController.navigate("${Routes.ArticleDetailScreen.route}/${it}"){
                                launchSingleTop = true
                            }
                        }
                    )
                }

                MainTag.NEW_ITEM -> {
                    NewScreen(
                        //navigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBarMain(
    currentTab: MainTag,
    onTabPressed: ((MainTag) -> Unit) = {},
    navigationItemContentList: List<NavigationMainItemContent>,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = Modifier.height(60.dp),
        containerColor = colorResource(R.color.my_primary)) {

        for (navItem in navigationItemContentList) {
            NavigationBarItem(
                selected = currentTab == navItem.type,
                onClick = { onTabPressed(navItem.type) },

                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor= colorResource(R.color.black),
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    indicatorColor= colorResource(R.color.my_secondary),
                    unselectedIconColor=MaterialTheme.colorScheme.onPrimary,
                    unselectedTextColor =MaterialTheme.colorScheme.onPrimary),

                icon = {
                    Image(
                        painter = painterResource(navItem.icon),
                        contentDescription = navItem.text,
                        colorFilter = ColorFilter.tint(Color.White)

                    )
                },
                modifier = Modifier
                    .background(colorResource(R.color.my_primary))
                    .padding(0.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarMainPreview() {
    TopBar(
        title = "Lista de la compra",
        modifier = Modifier.background(Color.Green),
        tag = MainTag.NEW_ITEM,
        onClickDrawer = {}
    )
}


@Preview(showBackground = true)
@Composable
fun BottomBarMainPreview() {
    val uiState = MainUiState()
    val navigationItemContentList = MainDataProvider.getMainData()

    BottomBarMain(
        currentTab = uiState.currentTab,
        navigationItemContentList = navigationItemContentList,
    )
}
