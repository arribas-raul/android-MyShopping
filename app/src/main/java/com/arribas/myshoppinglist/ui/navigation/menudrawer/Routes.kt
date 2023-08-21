package com.arribas.myshoppinglist.ui.navigation.menudrawer

import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.ui.navigation.NavigationDestination

sealed class Routes{
    object ShopListScreen : NavigationDestination {
        override val route = "shop_list"
        override val titleRes = R.string.shop_list_title
    }

    object ArticleListScreen : NavigationDestination {
        override val route = "article_list"
        override val titleRes = R.string.article_list_title
    }

    object ArticleNewScreen : NavigationDestination {
        override val route = "article_new"
        override val titleRes = R.string.article_new_title
    }

    object ArticleDetailScreen : NavigationDestination {
        const val itemIdArg = "itemId"

        override val route = "article_detail"
        val routeWithArgs = "$route/{$itemIdArg}"

        override val titleRes = R.string.article_detail_title
    }

}
