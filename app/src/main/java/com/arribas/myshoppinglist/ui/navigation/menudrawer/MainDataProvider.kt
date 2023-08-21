package com.arribas.myshoppinglist.ui.navigation.menudrawer

import com.arribas.myshoppinglist.R

object MainDataProvider {
    fun getMenuData(): List<NavigationMainItemContent>{
        return listOf(
            NavigationMainItemContent(
                type = NavTag.SHOP_LIST,
                text = "Lista de la compra",
                icon = R.drawable.ic_shopping_cart,
                menuLeftVisible = true,
                menuBottomVisible = true
            ),

            NavigationMainItemContent(
                type = NavTag.ITEM_LIST,
                text = "Lista de productos",
                icon = R.drawable.ic_view_list,
                menuLeftVisible = true,
                menuBottomVisible = true
            ),

            NavigationMainItemContent(
                type = NavTag.NEW_ITEM,
                text = "Nuevo producto",
                icon = R.drawable.ic_create,
                menuLeftVisible = true,
                menuBottomVisible = true
            ),

            NavigationMainItemContent(
                type = NavTag.DETAIL_ITEM,
                text = "Detalle producto",
                icon = R.drawable.ic_create
            )
        )
    }
}