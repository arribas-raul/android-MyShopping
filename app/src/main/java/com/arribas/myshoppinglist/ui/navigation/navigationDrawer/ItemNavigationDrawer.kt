package com.arribas.myshoppinglist.ui.navigation.navigationDrawer

import com.arribas.myshoppinglist.ui.navigation.route.RouteEnum

data class ItemNavigationDrawer(
    val type: RouteEnum = RouteEnum.SHOPLIST_MANAGE,
    var text: String = "",
    var text_menu: String = "",
    val icon: Int = 0,
    val menuLeftVisible: Boolean = false,
    val menuBottomVisible: Boolean = false
)
