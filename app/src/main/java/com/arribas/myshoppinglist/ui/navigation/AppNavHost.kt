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
import com.arribas.myshoppinglist.ui.view.article.articleDetail.ArticleDetailScreen
import com.arribas.myshoppinglist.ui.view.article.articleList.ListArticleScreen
import com.arribas.myshoppinglist.ui.view.article.articleNew.ArticleNewScreen
import com.arribas.myshoppinglist.ui.view.listArticleShop.ListArticleShopScreen
import com.arribas.myshoppinglist.ui.view.shoplist.shoplistList.ShoplistListScreen

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
        startDestination = Routes.ShopListScreen.route.toString(),
        modifier = modifier
    ) {
        composable(
            route = Routes.ShopListScreen.route.toString()
        ){
            ListArticleShopScreen(
                navigateToItemUpdate = {}
            )
        }

        composable(
            route = Routes.ArticleListScreen.route.toString()
        ){
            ListArticleScreen(
                navigateToItemUpdate = {
                    onItemClick(RouteEnum.ARTICLE_DETAIL)
                    navController.navigate("${Routes.ArticleDetailScreen.route}/${it}")
                }
            )
        }

        composable(
            route = Routes.ArticleNewScreen.route.toString()
        ){
            ArticleNewScreen()
        }

        composable(
            route = Routes.ArticleDetailScreen.routeWithArgs,
            arguments = listOf(navArgument(Routes.ArticleDetailScreen.itemIdArg) {
                type = NavType.IntType
            })
        ){
            ArticleDetailScreen(
                navigateBack = { navController.popBackStack() },
                modifier = modifier,
            )
        }

        composable(
            route = Routes.ShoplistListScreen.route.toString()
        ){
            ShoplistListScreen()
        }
    }
}

fun navigate(
    item: ItemNavigationDrawer,
    navController: NavHostController
){
    when(item.type){
        RouteEnum.SHOP_LIST       -> navController.navigate(Routes.ShopListScreen.route.toString())
        RouteEnum.ARTICLE_LIST    -> navController.navigate(Routes.ArticleListScreen.route.toString())
        RouteEnum.ARTICLE_NEW     -> navController.navigate(Routes.ArticleNewScreen.route.toString())
        RouteEnum.ARTICLE_DETAIL  -> navController.navigate(Routes.ArticleDetailScreen.routeWithArgs)
        RouteEnum.SHOPLIST_LIST   -> navController.navigate(Routes.ShoplistListScreen.route.toString())
    }
}
