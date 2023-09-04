package com.arribas.myshoppinglist.ui.navigation.navigationDrawer

import com.arribas.myshoppinglist.ui.navigation.route.RouteEnum

data class ItemNavigationDrawer(
    val type: RouteEnum = RouteEnum.SHOP_LIST,
    var text: String = "",
    val icon: Int = 0,
    val menuLeftVisible: Boolean = false,
    val menuBottomVisible: Boolean = false
)
