package com.arribas.myshoppinglist.ui.view.listArticleShop

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.ui.theme.MyShoppingListTheme
import com.arribas.myshoppinglist.ui.viewModel.listArticleShop.ListShopUiState

@Composable
fun HeaderArticleShopList(
    listUiState: ListShopUiState,
    onResetBt: () -> Unit = {},
    modifier: Modifier = Modifier.background(colorResource(R.color.white))
){
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.white)),
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(0.2.dp, Color.Gray),
    ){
        Row(modifier = modifier.fillMaxWidth())
        {
            IconButton(
                onClick = onResetBt,

                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_check_box_outline),
                    contentDescription = stringResource(R.string.delete),
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

            Text(
                text = "Total Items ${listUiState.itemCount}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                modifier = Modifier
                    .padding(start = 5.dp)
                    .align(Alignment.CenterVertically)
            )

            Text(
                text = " - ",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )

            Text(
                text = "Select Items ${listUiState.itemSelectCount}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderArticleShopListPreview() {
    val listUiState = ListShopUiState(
        itemCount = 20,
        itemSelectCount = 10
    )

    HeaderArticleShopList(listUiState)
}
