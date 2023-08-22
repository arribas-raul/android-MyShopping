package com.arribas.myshoppinglist.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.arribas.myshoppinglist.ui.navigation.navigationDrawer.ItemNavigationDrawer
import com.arribas.myshoppinglist.ui.navigation.route.RouteEnum
import com.arribas.myshoppinglist.ui.navigation.route.Routes
import com.arribas.myshoppinglist.ui.view.detailArticle.DetailScreen
import com.arribas.myshoppinglist.ui.view.detailArticle.NewScreen
import com.arribas.myshoppinglist.ui.view.listArticle.ListArticleScreen
import com.arribas.myshoppinglist.ui.view.listArticle.ListArticleShopScreen

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun MyAppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onItemClick: (RouteEnum) -> Unit,
) {
    //val mainViewModel = MainViewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.ShopListScreen.route,
        modifier = modifier
    ) {
        composable(
            route = Routes.ShopListScreen.route
        ){
            ListArticleShopScreen(
                navigateToItemUpdate = {}
            )
        }

        composable(
            route = Routes.ArticleListScreen.route
        ){
            ListArticleScreen(
                navigateToItemUpdate = {
                    onItemClick(RouteEnum.DETAIL_ITEM)
                    navController.navigate("${Routes.ArticleDetailScreen.route}/${it}")
                }
            )
        }

        composable(
            route = Routes.ArticleNewScreen.route
        ){
            NewScreen()
        }

        composable(
            route = Routes.ArticleDetailScreen.routeWithArgs,
            arguments = listOf(navArgument(Routes.ArticleDetailScreen.itemIdArg) {
                type = NavType.IntType
            })
        ){
            DetailScreen(
                navigateBack = { navController.popBackStack() },
                modifier = modifier,
            )
        }
    }
}

fun navigate(
    item: ItemNavigationDrawer,
    navController: NavHostController
){
    when(item.type){
        RouteEnum.SHOP_LIST    -> navController.navigate(Routes.ShopListScreen.route)
        RouteEnum.ITEM_LIST    -> navController.navigate(Routes.ArticleListScreen.route)
        RouteEnum.NEW_ITEM     -> navController.navigate(Routes.ArticleNewScreen.route)
        RouteEnum.DETAIL_ITEM  -> navController.navigate(Routes.ArticleDetailScreen.routeWithArgs)
    }
}
