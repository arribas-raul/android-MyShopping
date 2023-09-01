package com.arribas.myshoppinglist.ui.navigation.route

import com.arribas.myshoppinglist.R

enum class RouteEnum {
    SHOP_LIST,
    ARTICLE_MAIN_LIST,
    ARTICLE_LIST,
    ARTICLE_NEW,
    ARTICLE_DETAIL,
    SHOPLIST_LIST,
    SHOPLIST_MAIN_DETAIL,
    SHOPLIST_DETAIL,
}
sealed class Routes{
    object ShopListScreen: Route {
        override val route = RouteEnum.SHOP_LIST
        override val titleRes = R.string.shop_list_title
    }

    object ArticleMainListScreen: Route {
        override val route = RouteEnum.ARTICLE_MAIN_LIST
        override val titleRes = R.string.article_list_title
    }

    object ArticleListScreen: Route {
        const val itemIdArg = "itemId"

        override val route = RouteEnum.ARTICLE_LIST
        override val titleRes = R.string.article_list_title

        val routeWithArgs = "$route/{$itemIdArg}"
    }

    object ArticleNewScreen: Route {
        override val route = RouteEnum.ARTICLE_NEW
        override val titleRes = R.string.article_new_title
    }

    object ArticleDetailScreen: Route {
        const val itemIdArg = "itemId"

        override val route = RouteEnum.ARTICLE_DETAIL
        val routeWithArgs = "$route/{$itemIdArg}"

        override val titleRes = R.string.article_detail_title
    }

    object ShoplistListScreen: Route {
        override val route = RouteEnum.SHOPLIST_LIST
        override val titleRes = R.string.shoplist_list_title
    }

    object ShoplistMainDetailScreen: Route {
        override val route = RouteEnum.SHOPLIST_MAIN_DETAIL
        override val titleRes = R.string.shoplist_detail_title
    }

    object ShoplistDetailScreen: Route {
        const val itemIdArg = "itemId"

        override val route = RouteEnum.SHOPLIST_DETAIL
        override val titleRes = R.string.shoplist_detail_title

        val routeWithArgs = "$route/{$itemIdArg}"

    }
}
