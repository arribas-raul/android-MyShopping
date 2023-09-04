package com.arribas.myshoppinglist.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.ui.navigation.navigationDrawer.ItemNavigationDrawer
import com.arribas.myshoppinglist.ui.navigation.route.RouteEnum
import com.arribas.myshoppinglist.ui.navigation.route.Routes
import com.arribas.myshoppinglist.ui.view.app.AppUiState
import com.arribas.myshoppinglist.ui.view.app.topBar.AppBarState
import com.arribas.myshoppinglist.ui.view.article.articleDetail.ArticleDetailScreen
import com.arribas.myshoppinglist.ui.view.article.articleList.ArticleListScreen
import com.arribas.myshoppinglist.ui.view.article.articleNew.ArticleNewScreen
import com.arribas.myshoppinglist.ui.view.listArticleShop.ListArticleShopScreen
import com.arribas.myshoppinglist.ui.view.shoplist.shoplistDetail.ShoplistDetailScreen
import com.arribas.myshoppinglist.ui.view.shoplist.shoplistList.ShoplistListScreen

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun MyAppNavHost(
    appUiState: AppUiState,
    appBarState: AppBarState,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onItemClick: (RouteEnum) -> Unit,
) {

    NavHost(
        navController = navController,
        startDestination = Routes.ShopListScreen.route.toString(),
        modifier = modifier
    ) {

        composable(
            route = Routes.ShopListScreen.route.toString()
        ){
            ListArticleShopScreen(
                onComposing = {
                    appBarState.actions = it.actions
                },
                navigateToItemUpdate = {}
            )
        }

        composable(
            route = Routes.ArticleListScreen.routeWithArgs,

            arguments = listOf(
                navArgument(Routes.itemIdArg) {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ){
            ArticleListScreen(
                onComposing = {
                    appBarState.actions = it.actions
                },

                navigateBack = { navController.popBackStack() },

                navigateToItemUpdate = {
                    onItemClick(RouteEnum.ARTICLE_DETAIL)

                    navController.navigate(
                        route = "${Routes.ArticleDetailScreen.route}/${it.id}"
                    )

                    appUiState.title = it.name
                }
            )
        }

        composable(
            route = Routes.ArticleNewScreen.route.toString()
        ){
            ArticleNewScreen(
                onComposing = {
                    appBarState.actions = it.actions
                },
            )
        }

        composable(
            route = Routes.ArticleDetailScreen.routeWithArgs,
            arguments = listOf(
                navArgument(Routes.itemIdArg) {
                    type = NavType.IntType
                }
            )
        ){

            ArticleDetailScreen(
                onComposing = {
                    appBarState.actions = it.actions
                },
                navigateBack = {
                    navController.popBackStack()
                },
                appUiState = appUiState,
                modifier = modifier,
            )
        }

        composable(
            route = Routes.ShoplistListScreen.route.toString()
        ){
            ShoplistListScreen(
                onComposing = {
                    appBarState.actions = it.actions
                },
                navigateToItemUpdate = {
                    onItemClick(RouteEnum.SHOPLIST_DETAIL)

                    navController.navigate("${Routes.ShoplistDetailScreen.route}?${it.id}")
                    appUiState.title = it.name
                }
            )
        }

        composable(
            route = Routes.ShoplistDetailScreen.routeWithArgs,

            arguments = listOf(
                navArgument(Routes.itemIdArg) {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ){
            ShoplistDetailScreen(
                onComposing = {
                    appBarState.actions = it.actions
                },

                navigateBack = { navController.popBackStack() },

                navigateToItemUpdate = {
                    onItemClick(RouteEnum.ARTICLE_LIST)

                    navController.navigate(
                        route = "${Routes.ArticleListScreen.route}?${it.id}"
                    )

                    appUiState.title = appUiState.actualTitle()
                },
                appUiState = appUiState,

                modifier = modifier
            )
        }
    }
}
