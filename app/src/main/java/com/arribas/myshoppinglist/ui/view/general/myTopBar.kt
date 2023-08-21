package com.arribas.myshoppinglist.ui.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.data.MainTag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    canNavigateBack: Boolean = false,
    navigateUp: () -> Unit = {},
    tag: MainTag,
    onClickDrawer: () -> Unit,
    modifier: Modifier = Modifier){

    TopAppBar(
        title = { Text(text= title) },

        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = colorResource(R.color.my_primary),
            titleContentColor = colorResource(R.color.white),
            navigationIconContentColor = colorResource(R.color.white),
            actionIconContentColor = colorResource(R.color.white)
        ),

        navigationIcon = {
            if(canNavigateBack){
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }else{
                IconButton(onClick = onClickDrawer){
                    Icon(
                        imageVector = Icons.Rounded.Menu,
                        contentDescription = "Drawer Icon"
                    )
                }

            }
        },

        actions = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "",
                    tint = Color.White,
                )
            }
        },

        modifier = modifier)

}

@Preview(showBackground = true)
@Composable
fun TopBarMainPreview() {
    TopBar(
        title = "Lista de la compra",
        canNavigateBack = true,
        tag = MainTag.NEW_ITEM,
        onClickDrawer = {}
    )
}