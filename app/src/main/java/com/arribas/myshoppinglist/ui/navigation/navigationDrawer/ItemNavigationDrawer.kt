package com.arribas.myshoppinglist.ui.navigation.navigationDrawer

import com.arribas.myshoppinglist.ui.navigation.route.RouteEnum

data class ItemNavigationDrawer(
    val type: RouteEnum,
    val text: String,
    val icon: Int,
    val menuLeftVisible: Boolean = false,
    val menuBottomVisible: Boolean = false
)
