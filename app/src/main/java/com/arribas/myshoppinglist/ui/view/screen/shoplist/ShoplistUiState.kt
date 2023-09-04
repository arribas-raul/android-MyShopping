package com.arribas.myshoppinglist.ui.view.screen.shoplist

import com.arribas.myshoppinglist.data.model.Shoplist

data class ShoplistUiState(
    val id: Int = 0,
    val name: String = "",
    val type: String = ""
){
    fun existElement(): Boolean{
        return id > 0
    }
}

fun ShoplistUiState.toItem(): Shoplist = Shoplist(
    id = id,
    name = name,
    type = type
)

/**
 * Extension function to convert [Item] to [ItemUiState]
 */
fun Shoplist.toShopListUiState(): ShoplistUiState = ShoplistUiState(
    id = id,
    name = name,
    type = type
)

fun ShoplistUiState.isValid() : Boolean {
    return name.isNotBlank() && type.isNotBlank()
}

