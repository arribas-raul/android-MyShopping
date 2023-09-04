package com.arribas.myshoppinglist.ui.navigation.navigationDrawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arribas.myshoppinglist.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(
    items: List<ItemNavigationDrawer>,
    selectedItem: ItemNavigationDrawer,
    onNavigatePressed: (ItemNavigationDrawer)-> Unit
){
    ModalDrawerSheet{
        DrawerHeader()

        DrawerBody(
            items = items,
            onNavigatePressed = { onNavigatePressed(it) },
            selectedItem = selectedItem,
            modifier = Modifier.padding(top = 20.dp)
        )
    }
}

@Composable
fun DrawerHeader(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.my_primary))
            .padding(vertical = 64.dp, horizontal = 20.dp)
    ){
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 24.sp,
            color = Color.White
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerBody(
    items: List<ItemNavigationDrawer>,
    onNavigatePressed: (ItemNavigationDrawer) -> Unit,
    selectedItem: ItemNavigationDrawer,
    modifier: Modifier = Modifier
){
    Spacer(modifier = modifier)

    items.forEach { item ->
        if(item.menuLeftVisible) {
            NavigationDrawerItem(
                label = { Text(text = item.text) },
                selected = item == selectedItem,
                onClick = { onNavigatePressed(item) },

                icon = {
                    Image(
                        painter = painterResource(item.icon),
                        contentDescription = item.text,
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                },

                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}