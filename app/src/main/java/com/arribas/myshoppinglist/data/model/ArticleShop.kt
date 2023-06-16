package com.arribas.myshoppinglist.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ArticleShop(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val quantity: Int = 1,
    val check: Boolean = false,
    val order: Int = 1,
)
