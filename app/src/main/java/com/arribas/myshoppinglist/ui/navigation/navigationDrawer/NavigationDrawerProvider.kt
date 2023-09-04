package com.arribas.myshoppinglist.ui.navigation.navigationDrawer

import android.content.Context
import androidx.compose.ui.res.stringResource
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.ui.navigation.route.RouteEnum

object NavigationDrawerProvider {
    fun getData(context: Context): List<ItemNavigationDrawer>{
        return listOf(
            ItemNavigationDrawer(
                type = RouteEnum.SHOP_LIST,
                text = context.getString(R.string.shoplist_list_title),
                icon = R.drawable.ic_shopping_cart,
                menuLeftVisible = true,
                menuBottomVisible = true
            ),

            ItemNavigationDrawer(
                type = RouteEnum.ARTICLE_LIST,
                text = context.getString(R.string.article_list_title),
                icon = R.drawable.ic_view_list,
                menuLeftVisible = true,
                menuBottomVisible = true
            ),

            ItemNavigationDrawer(
                type = RouteEnum.ARTICLE_NEW,
                text = context.getString(R.string.article_new_title),
                icon = R.drawable.ic_create,
                menuLeftVisible = true,
                menuBottomVisible = true
            ),

            ItemNavigationDrawer(
                type = RouteEnum.ARTICLE_DETAIL,
                text = context.getString(R.string.article_detail_title),
                icon = R.drawable.ic_create
            ),

            ItemNavigationDrawer(
                type = RouteEnum.SHOPLIST_LIST,
                text = context.getString(R.string.shoplist_list_title),
                icon = R.drawable.ic_view_list,
                menuLeftVisible = true
            ),

            ItemNavigationDrawer(
                type = RouteEnum.SHOPLIST_DETAIL,
                text = context.getString(R.string.shoplist_detail_title),
                icon = R.drawable.ic_view_list,
                menuLeftVisible = true,
                menuBottomVisible = true
            ),
        )
    }
}