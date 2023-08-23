package com.arribas.myshoppinglist.ui.view.general

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.ui.navigation.navigationDrawer.ItemNavigationDrawer
import com.arribas.myshoppinglist.ui.navigation.route.RouteEnum

@Composable
fun MyBottomBar(
    currentTab: RouteEnum,
    onTabPressed: ((RouteEnum) -> Unit) = {},
    navigationItemContentList: List<ItemNavigationDrawer>,
    //modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = Modifier.height(60.dp),
        containerColor = colorResource(R.color.my_primary)
    ) {

        for (navItem in navigationItemContentList) {
            if (navItem.menuBottomVisible) {

                NavigationBarItem(
                    selected = currentTab == navItem.type,
                    onClick = { onTabPressed(navItem.type) },

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorResource(R.color.black),
                        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                        indicatorColor = colorResource(R.color.my_secondary),
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedTextColor = MaterialTheme.colorScheme.onPrimary
                    ),

                    icon = {
                        Image(
                            painter = painterResource(navItem.icon),
                            contentDescription = navItem.text,
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    },

                    modifier = Modifier
                        .background(colorResource(R.color.my_primary))
                        .padding(0.dp)
                )
            }
        }
    }
}