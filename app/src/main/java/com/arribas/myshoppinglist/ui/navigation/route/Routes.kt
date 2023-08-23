package com.arribas.myshoppinglist.ui.navigation.route

import com.arribas.myshoppinglist.R

enum class RouteEnum {
    SHOP_LIST, ARTICLE_LIST, ARTICLE_NEW, ARTICLE_DETAIL, SHOPLIST_LIST
}
sealed class Routes{
    object ShopListScreen: Route {
        override val route = RouteEnum.SHOP_LIST
        override val titleRes = R.string.shop_list_title
    }

    object ArticleListScreen: Route {
        override val route = RouteEnum.ARTICLE_LIST
        override val titleRes = R.string.article_list_title
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
        override val titleRes = R.string.shop_list_title
    }
}
