package com.arribas.myshoppinglist.data

import com.arribas.myshoppinglist.R

object MainDataProvider {
    fun getMainData(): List<NavigationMainItemContent>{
        return listOf(
            NavigationMainItemContent(
                type = MainTag.SHOP_LIST,
                text = "Lista de la compra",
                icon = R.drawable.ic_shopping_cart

            ),
            NavigationMainItemContent(
                type = MainTag.ITEM_LIST,
                text = "Lista de productos",
                icon = R.drawable.ic_view_list
            ),

            NavigationMainItemContent(
                type = MainTag.NEW_ITEM,
                text = "Nuevo producto",
                icon = R.drawable.ic_create
            )
        )
    }
}