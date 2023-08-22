package com.arribas.myshoppinglist.ui.navigation.route

import com.arribas.myshoppinglist.R

sealed class Routes{
    object ShopListScreen: Route {
        override val route = "shop_list"
        override val titleRes = R.string.shop_list_title
    }

    object ArticleListScreen: Route {
        override val route = "article_list"
        override val titleRes = R.string.article_list_title
    }

    object ArticleNewScreen: Route {
        override val route = "article_new"
        override val titleRes = R.string.article_new_title
    }

    object ArticleDetailScreen: Route {
        const val itemIdArg = "itemId"

        override val route = "article_detail"
        val routeWithArgs = "$route/{$itemIdArg}"

        override val titleRes = R.string.article_detail_title
    }
}
