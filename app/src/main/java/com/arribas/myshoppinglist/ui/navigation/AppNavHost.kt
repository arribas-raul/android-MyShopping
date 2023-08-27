package com.arribas.myshoppinglist.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.ui.navigation.navigationDrawer.ItemNavigationDrawer
import com.arribas.myshoppinglist.ui.navigation.route.RouteEnum
import com.arribas.myshoppinglist.ui.navigation.route.Routes
import com.arribas.myshoppinglist.ui.view.app.AppViewModel
import com.arribas.myshoppinglist.ui.view.article.articleDetail.ArticleDetailScreen
import com.arribas.myshoppinglist.ui.view.article.articleList.ListArticleScreen
import com.arribas.myshoppinglist.ui.view.article.articleNew.ArticleNewScreen
import com.arribas.myshoppinglist.ui.view.listArticleShop.ListArticleShopScreen
import com.arribas.myshoppinglist.ui.view.shoplist.shoplistDetail.ShoplistDetailScreen
import com.arribas.myshoppinglist.ui.view.shoplist.shoplistList.ShoplistListScreen

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun MyAppNavHost(
    appViewModel: AppViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onItemClick: (RouteEnum) -> Unit,
) {
    //val mainViewModel = MainViewModel()
    val appUiState by appViewModel.appUiState.collectAsState()

    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Routes.ShopListScreen.route.toString(),
        modifier = modifier
    ) {

        composable(
            route = Routes.ShopListScreen.route.toString()
        ){
            appUiState.title = context.getString(R.string.shoplist_detail_title)

            ListArticleShopScreen(
                navigateToItemUpdate = {}
            )
        }

        composable(
            route = Routes.ArticleListScreen.route.toString()
        ){
            appUiState.title = context.getString(R.string.article_list_title)

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
            appUiState.title = context.getString(R.string.article_new_title)

            ArticleNewScreen()
        }

        composable(
            route = Routes.ArticleDetailScreen.routeWithArgs,
            arguments = listOf(navArgument(Routes.ArticleDetailScreen.itemIdArg) {
                type = NavType.IntType
            })
        ){
            appUiState.title = context.getString(R.string.article_detail_title)

            ArticleDetailScreen(
                navigateBack = { navController.popBackStack() },
                modifier = modifier,
            )
        }

        composable(
            route = Routes.ShoplistListScreen.route.toString()
        ){
            appUiState.title = context.getString(R.string.shoplist_list_title)

            ShoplistListScreen(
                navigateToItemUpdate = {
                    onItemClick(RouteEnum.SHOPLIST_DETAIL)
                    navController.navigate("${Routes.ShoplistDetailScreen.route}/${it}")
                }
            )
        }

        composable(
            route = Routes.ShoplistMainDetailScreen.route.toString()
        ){
            appUiState.title = context.getString(R.string.shoplist_detail_title)

            ShoplistDetailScreen()
        }

        composable(
            route = Routes.ShoplistDetailScreen.routeWithArgs,
            arguments = listOf(navArgument(Routes.ShoplistDetailScreen.itemIdArg) {
                type = NavType.IntType
            })
        ){
            appUiState.title = context.getString(R.string.shoplist_detail_title)

            ShoplistDetailScreen(
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
        RouteEnum.SHOP_LIST       -> navController.navigate(Routes.ShopListScreen.route.toString())
        RouteEnum.ARTICLE_LIST    -> navController.navigate(Routes.ArticleListScreen.route.toString())
        RouteEnum.ARTICLE_NEW     -> navController.navigate(Routes.ArticleNewScreen.route.toString())
        RouteEnum.ARTICLE_DETAIL  -> navController.navigate(Routes.ArticleDetailScreen.routeWithArgs)
        RouteEnum.SHOPLIST_LIST   -> navController.navigate(Routes.ShoplistListScreen.route.toString())
        RouteEnum.SHOPLIST_MAIN_DETAIL -> navController.navigate(Routes.ShoplistMainDetailScreen.route.toString())
        RouteEnum.SHOPLIST_DETAIL   -> navController.navigate(Routes.ShoplistDetailScreen.route.toString())
    }
}
