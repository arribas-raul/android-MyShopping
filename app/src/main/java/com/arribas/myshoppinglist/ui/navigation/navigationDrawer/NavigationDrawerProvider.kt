package com.arribas.myshoppinglist.ui.navigation.navigationDrawer

import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.ui.navigation.route.RouteEnum

object NavigationDrawerProvider {
    fun getData(): List<ItemNavigationDrawer>{
        return listOf(
            ItemNavigationDrawer(
                type = RouteEnum.SHOP_LIST,
                text = "Lista de la compra",
                icon = R.drawable.ic_shopping_cart,
                menuLeftVisible = true,
                menuBottomVisible = true
            ),

            ItemNavigationDrawer(
                type = RouteEnum.ARTICLE_MAIN_LIST,
                text = "Lista de productos",
                icon = R.drawable.ic_view_list,
                menuLeftVisible = true,
                menuBottomVisible = true
            ),

            ItemNavigationDrawer(
                type = RouteEnum.ARTICLE_LIST,
                text = "Lista de productos",
                icon = R.drawable.ic_view_list,
                menuLeftVisible = false,
                menuBottomVisible = false
            ),

            ItemNavigationDrawer(
                type = RouteEnum.ARTICLE_NEW,
                text = "Nuevo producto",
                icon = R.drawable.ic_create,
                menuLeftVisible = true,
                menuBottomVisible = true
            ),

            ItemNavigationDrawer(
                type = RouteEnum.ARTICLE_DETAIL,
                text = "Detalle producto",
                icon = R.drawable.ic_create
            ),

            ItemNavigationDrawer(
                type = RouteEnum.SHOPLIST_LIST,
                text = "Lista de listas",
                icon = R.drawable.ic_view_list,
                menuLeftVisible = true
            ),

            ItemNavigationDrawer(
                type = RouteEnum.SHOPLIST_MAIN_DETAIL,
                text = "Lista de objetos",
                icon = R.drawable.ic_view_list,
                menuLeftVisible = true,
                menuBottomVisible = true
            ),

            ItemNavigationDrawer(
                type = RouteEnum.SHOPLIST_DETAIL,
                text = "Lista de objetos",
                icon = R.drawable.ic_view_list
            ),
        )
    }
}