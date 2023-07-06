package com.arribas.myshoppinglist.ui.view.listArticleShop

import com.arribas.myshoppinglist.data.model.ArticleShop

data class ArticleShopUiState(
    val id: Int = 0,
    val name: String = "",
    val quantity: String = "1",
    val check: Boolean = false,
    val order: Int = 1,
    val actionEnabled: Boolean = false
)

fun ArticleShopUiState.toItem(): ArticleShop = ArticleShop(
    id = id,
    name = name,
    quantity =  quantity.toIntOrNull() ?: 0,
    check = check,
    order = order
)

/**
 * Extension function to convert [Item] to [ItemUiState]
 */
fun ArticleShop.toArticleShopUiState(
    actionEnabled: Boolean = false): ArticleShopUiState = ArticleShopUiState(
    id = id,
    name = name,
    quantity = quantity.toString(),
    check = check,
    order = order,
    actionEnabled = actionEnabled
)

fun ArticleShopUiState.isValid() : Boolean {
    return name.isNotBlank() &&
           quantity.isNotBlank() &&
           order > 0
}
