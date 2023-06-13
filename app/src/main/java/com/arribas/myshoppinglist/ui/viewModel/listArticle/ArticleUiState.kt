package com.arribas.myshoppinglist.ui.viewModel.listArticle

import com.arribas.myshoppinglist.data.model.Article

data class ArticleUiState(
    val id: Int = 0,
    val name: String = "",
    val quantity: String = "1",
    val check: Boolean = false,
    val actionEnabled: Boolean = false
)

fun ArticleUiState.toItem(): Article = Article(
    id = id,
    name = name,
    quantity =  quantity.toIntOrNull() ?: 0,
    check = check
)

/**
 * Extension function to convert [Item] to [ItemUiState]
 */
fun Article.toArticleUiState(actionEnabled: Boolean = false): ArticleUiState = ArticleUiState(
    id = id,
    name = name,
    quantity = quantity.toString(),
    check = check,
    actionEnabled = actionEnabled
)

fun ArticleUiState.isValid() : Boolean {
    return name.isNotBlank() && quantity.isNotBlank()
}
