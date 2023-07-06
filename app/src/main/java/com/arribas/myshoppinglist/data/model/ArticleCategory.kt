package com.arribas.myshoppinglist.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "article_category",
    foreignKeys = [
        ForeignKey(
            entity = Article::class,
            childColumns = ["article_id"],
            parentColumns = ["id"]
        ),
        ForeignKey(
            entity = Category::class,
            childColumns = ["category_id"],
            parentColumns = ["id"]
        )
    ]
)
data class ArticleCategory (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val article_id: Int,
    val category_id: Int
)