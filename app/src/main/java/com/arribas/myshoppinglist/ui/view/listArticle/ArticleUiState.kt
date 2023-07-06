package com.arribas.myshoppinglist.ui.view.listArticle

import com.arribas.myshoppinglist.data.model.Article

data class ArticleUiState(
    val id: Int = 0,
    val name: String = "",
    val quantity: String = "1",
    val category: Int = 0,
    val check: Boolean = false,
    val actionEnabled: Boolean = false
)

fun ArticleUiState.toItem(): Article = Article(
    id = id,
    name = name,
    shopCheked = check
)

/**
 * Extension function to convert [Item] to [ItemUiState]
 */
fun Article.toArticleUiState(actionEnabled: Boolean = false): ArticleUiState = ArticleUiState(
    id = id,
    name = name,
    check = shopCheked,
    actionEnabled = actionEnabled
)

fun ArticleUiState.isValid() : Boolean {
    return name.isNotBlank() && quantity.isNotBlank()
}
