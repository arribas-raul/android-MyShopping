package com.arribas.myshoppinglist.ui.view.screen.category.categoryList

import com.arribas.myshoppinglist.data.model.Category
import com.arribas.myshoppinglist.data.model.Shoplist

data class CategoryUiState(
    val id: Int = 0,
    val name: String = ""
){
    fun existElement(): Boolean{
        return id > 0
    }
}

fun CategoryUiState.toItem(): Category = Category(
    id = id,
    name = name
)

/**
 * Extension function to convert [Item] to [ItemUiState]
 */
fun Category.toCategoryUiState(): CategoryUiState = CategoryUiState(
    id = id,
    name = name
)

fun CategoryUiState.isValid() : Boolean {
    return name.isNotBlank()
}

