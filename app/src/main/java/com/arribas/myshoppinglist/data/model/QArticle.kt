package com.arribas.myshoppinglist.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QArticle(
    val id: Int,
    val shoplist_id: Int,
    val name: String,
    val category: String? = null,
    val shopCheked: Boolean
)
