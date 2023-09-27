package com.arribas.myshoppinglist.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.arribas.myshoppinglist.data.utils.PreferencesEnum
import com.arribas.myshoppinglist.data.utils.PreferencesManager
import com.arribas.myshoppinglist.ui.navigation.route.RouteEnum
import com.arribas.myshoppinglist.ui.navigation.route.Routes
import com.arribas.myshoppinglist.ui.view.app.app.AppUiState
import com.arribas.myshoppinglist.ui.view.app.topBar.AppBarState
import com.arribas.myshoppinglist.ui.view.screen.article.articleDetail.ArticleDetailScreen
import com.arribas.myshoppinglist.ui.view.screen.article.articleList.ArticleListScreen
import com.arribas.myshoppinglist.ui.view.screen.article.articleNew.ArticleNewScreen
import com.arribas.myshoppinglist.ui.view.screen.category.categoryList.CategoryListScreen
import com.arribas.myshoppinglist.ui.view.screen.listArticleShop.ListArticleShopScreen
import com.arribas.myshoppinglist.ui.view.screen.shoplist.shoplistDetail.ShoplistDetailScreen
import com.arribas.myshoppinglist.ui.view.screen.shoplist.shoplistList.ShoplistListScreen
import com.arribas.myshoppinglist.ui.view.screen.shoplist.shoplistManage.ShoplistManageScreen

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun MyAppNavHost(
    onUpdateTitle: (String)->Unit,
    appUiState: AppUiState,
    appBarState: AppBarState,
    navController: NavHostController,
    onSelectItem: (RouteEnum) -> Unit,
    modifier: Modifier = Modifier,
) {
    onUpdateTitle(appUiState.lastSelectedItems.last().text)

    NavHost(
        navController = navController,
        startDestination = Routes.ShoplistManageScreen.route.toString(),
        modifier = modifier
    ) {
        composable(
            route = Routes.ShoplistManageScreen.route.toString(),
        ){
            ShoplistManageScreen(
                onComposing = {
                    appBarState.actions = it.actions
                    onUpdateTitle("")
                },
                onUpdateTitle = {
                    appUiState.lastSelectedItems.last().text = it
                    onUpdateTitle(it)
                },

                modifier = modifier
            )
        }
        /*composable(
            route = Routes.ShopListScreen.route.toString()
        ){
            ListArticleShopScreen(
                onComposing = { appBarState.actions = it.actions },
                navigateToItemUpdate = {}
            )
        }*/

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
                onComposing = { appBarState.actions = it.actions },
                navigateBack = { navController.popBackStack() },

                onSelectItem = {
                    onSelectItem(RouteEnum.ARTICLE_DETAIL)

                    navController.navigate(
                        route = "${Routes.ArticleDetailScreen.route}/${it.id}"
                    )

                    appUiState.lastSelectedItems.last().text = it.name
                }
            )
        }

        composable(
            route = Routes.ArticleNewScreen.route.toString()
        ){
            ArticleNewScreen(
                onComposing = { appBarState.actions = it.actions },
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
                onComposing = { appBarState.actions = it.actions },
                navigateBack = { navController.popBackStack() },
                modifier = modifier,
            )
        }

        composable(
            route = Routes.CategoryListScreen.route.toString()
        ){
            CategoryListScreen(
                onComposing = { appBarState.actions = it.actions }
            )
        }

        composable(
            route = Routes.ShoplistListScreen.route.toString()
        ){
            ShoplistListScreen(
                onComposing = { appBarState.actions = it.actions },

                onSelectItem = {
                    onSelectItem(RouteEnum.SHOPLIST_DETAIL)
                    appUiState.lastSelectedItems.last().text = it.name

                    navController.navigate("${Routes.ShoplistDetailScreen.route}/${it.id}")
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
                onComposing = { appBarState.actions = it.actions },
                onSelectItem = {
                    onSelectItem(RouteEnum.ARTICLE_LIST)
                    appUiState.title = appUiState.actualTitle()

                    navController.navigate(
                        route = "${Routes.ArticleListScreen.route}?${it.id}"
                    )
                },

                modifier = modifier
            )
        }
    }
}
