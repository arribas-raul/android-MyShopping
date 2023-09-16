package com.arribas.myshoppinglist.ui.view.screen.listArticleShop

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arribas.myshoppinglist.R

@Composable
fun HeaderArticleShopList(
    listUiState: ListArticleShopUiState,
    searchUiState: SearchListArticleUiState = SearchListArticleUiState(),
    onCheckFilter: (SearchListArticleUiState) -> Unit = {},
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
            Spacer(Modifier.weight(4f).background(Color.Green))

            IconButton(
                onClick = onResetBt,

                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
                    .align(Alignment.CenterVertically)
                    .padding(end = 5.dp)

            ) {
                Image(
                    painter = painterResource(R.drawable.ic_restart),
                    contentDescription = "Restart",
                    modifier = Modifier

                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderArticleShopListPreview() {
    val listUiState = ListArticleShopUiState(
        itemCount = 20,
        itemSelectCount = 10
    )

    HeaderArticleShopList(listUiState)
}
