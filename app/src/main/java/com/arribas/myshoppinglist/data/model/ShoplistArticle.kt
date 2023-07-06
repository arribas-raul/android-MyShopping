package com.arribas.myshoppinglist.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "shoplist_article",
    foreignKeys = [
        ForeignKey(
            entity = Article::class,
            childColumns = ["article_id"],
            parentColumns = ["id"]
        ),
        ForeignKey(
            entity = Shoplist::class,
            childColumns = ["shoplist_id"],
            parentColumns = ["id"]
        )
    ]
)
data class ShoplistArticle(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val article_id: Int,
    val shoplist_id: Int,
    val name: String,
    var quantity: Int = 1,
    var check: Boolean = false,
    val order: Int = 1,
)