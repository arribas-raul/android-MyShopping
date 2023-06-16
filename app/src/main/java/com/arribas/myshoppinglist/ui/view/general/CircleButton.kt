package com.arribas.myshoppinglist.ui.view.general

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.ui.theme.MyShoppingListTheme

@Composable
fun CircleButton(
    onChangeItem: (Int) -> Unit = {},
    icon: Int,
    color: Int,
    backgroundColor: Int,
    quantity: Int,
    description: Int,
    modifier: Modifier = Modifier
){
    OutlinedIconButton(
        onClick = {
            onChangeItem(quantity)
        },

        modifier = Modifier
            .padding(end = 5.dp)
            .width(30.dp)
            .height(30.dp)
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = stringResource(description),
            colorFilter = ColorFilter.tint(colorResource(color)),
            modifier = Modifier
                .background(
                    colorResource(backgroundColor),
                    shape = CircleShape
                )
                .fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CircleButtonPreview() {
    MyShoppingListTheme {
        CircleButton(
            icon = R.drawable.ic_add,
            color = R.color.white,
            backgroundColor = R.color.my_primary,
            quantity = 1,
            description = R.string.bt_add
        )
    }
}