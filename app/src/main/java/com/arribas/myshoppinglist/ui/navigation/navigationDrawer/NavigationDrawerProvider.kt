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
                type = RouteEnum.ITEM_LIST,
                text = "Lista de productos",
                icon = R.drawable.ic_view_list,
                menuLeftVisible = true,
                menuBottomVisible = true
            ),

            ItemNavigationDrawer(
                type = RouteEnum.NEW_ITEM,
                text = "Nuevo producto",
                icon = R.drawable.ic_create,
                menuLeftVisible = true,
                menuBottomVisible = true
            ),

            ItemNavigationDrawer(
                type = RouteEnum.DETAIL_ITEM,
                text = "Detalle producto",
                icon = R.drawable.ic_create
            )
        )
    }
}