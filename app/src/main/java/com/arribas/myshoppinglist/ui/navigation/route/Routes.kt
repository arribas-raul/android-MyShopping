package com.arribas.myshoppinglist.ui.navigation.route

import androidx.navigation.NavHostController
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.ui.navigation.navigationDrawer.ItemNavigationDrawer

enum class RouteEnum {
    //SHOP_LIST,
    ARTICLE_LIST,
    ARTICLE_NEW,
    ARTICLE_DETAIL,
    SHOPLIST_LIST,
    SHOPLIST_DETAIL,
    SHOPLIST_MANAGE,
}

sealed class Routes{
    /*object ShopListScreen: Route {
        override val route = RouteEnum.SHOP_LIST
        override val titleRes = R.string.shop_list_title
    }*/

    object ArticleListScreen: Route {
        override val route = RouteEnum.ARTICLE_LIST
        override val titleRes = R.string.article_list_title

        val routeWithArgs = "$route?{$itemIdArg}"
    }

    object ArticleNewScreen: Route {
        override val route = RouteEnum.ARTICLE_NEW
        override val titleRes = R.string.article_new_title
    }

    object ArticleDetailScreen: Route {
        override val route = RouteEnum.ARTICLE_DETAIL
        val routeWithArgs = "$route/{$itemIdArg}"

        override val titleRes = R.string.article_detail_title
    }

    object ShoplistListScreen: Route {
        override val route = RouteEnum.SHOPLIST_LIST
        override val titleRes = R.string.shoplist_list_title
    }

    object ShoplistDetailScreen: Route {
        override val route = RouteEnum.SHOPLIST_DETAIL
        override val titleRes = R.string.shoplist_detail_title

        val routeWithArgs = "$route/{$itemIdArg}"
    }

    object ShoplistManageScreen: Route {
        override val route = RouteEnum.SHOPLIST_MANAGE
        override val titleRes = R.string.shoplist_manage_title
    }

    companion object {
        val itemIdArg = "itemId"

        fun navigate(
            item: ItemNavigationDrawer,
            navController: NavHostController
        ){
            when(item.type){
                //RouteEnum.SHOP_LIST       -> navController.navigate(ShopListScreen.route.toString())
                RouteEnum.ARTICLE_LIST    -> navController.navigate(ArticleListScreen.routeWithArgs)
                RouteEnum.ARTICLE_NEW     -> navController.navigate(ArticleNewScreen.route.toString())
                RouteEnum.ARTICLE_DETAIL  -> navController.navigate(ArticleDetailScreen.routeWithArgs)
                RouteEnum.SHOPLIST_LIST   -> navController.navigate(ShoplistListScreen.route.toString())
                RouteEnum.SHOPLIST_DETAIL -> navController.navigate(ShoplistDetailScreen.routeWithArgs)
                RouteEnum.SHOPLIST_MANAGE -> navController.navigate(ShoplistManageScreen.route.toString())
            }
        }
    }
}
