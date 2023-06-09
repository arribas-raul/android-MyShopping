package com.arribas.myshoppinglist.ui.view.listArticle

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.data.model.Article
import com.arribas.myshoppinglist.ui.theme.MyShoppingListTheme

@Composable
fun ListArticleHeader(modifier: Modifier = Modifier) {
    Column {
        Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            headerListArticle.forEach {
                Text(
                    text = stringResource(it.headerStringId),
                    textAlign = it.textAlign,
                    modifier = Modifier
                        .weight(it.weight)
                        .padding(5.dp),
                    style = MaterialTheme.typography.titleSmall
                )
            }

        }
    }
}

private val headerListArticle = listOf(
    ArticleHeader(headerStringId = R.string.check, weight = 0.5f, textAlign = TextAlign.Center),
    ArticleHeader(headerStringId = R.string.item, weight = 1.8f, textAlign = TextAlign.Left),
    ArticleHeader(headerStringId = R.string.string_void, weight = 0.3f, TextAlign.Right),
)

private data class ArticleHeader(
    @StringRes val headerStringId: Int,
    val weight: Float,
    val textAlign: TextAlign)

@Preview(showBackground = true)
@Composable
fun HeaderArticlePreview() {
    MyShoppingListTheme {
        ListArticleHeader()
    }
}
