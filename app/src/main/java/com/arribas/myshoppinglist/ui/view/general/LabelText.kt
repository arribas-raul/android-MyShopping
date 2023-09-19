package com.arribas.myshoppinglist.ui.view.general

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LabelText(
    isShow: Boolean,
    title: String,
    modifier: Modifier
){
    if(isShow) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = Color.LightGray,
                    shape = RoundedCornerShape(size = 5.dp)
                )
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                modifier = modifier
                    .padding(vertical = 4.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}