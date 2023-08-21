package com.arribas.myshoppinglist.ui.navigation.menudrawer

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationMainItemContent(
    val type: NavTag,
    val text: String,
    val icon: Int,
    val menuLeftVisible: Boolean = false,
    val menuBottomVisible: Boolean = false
)
