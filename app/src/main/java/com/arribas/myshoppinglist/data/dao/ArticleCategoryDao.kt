package com.arribas.myshoppinglist.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.arribas.myshoppinglist.data.model.ArticleCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleCategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(articleCategory: ArticleCategory)

    @Update
    suspend fun update(articleCategory: ArticleCategory)

    @Delete
    suspend fun delete(articleCategory: ArticleCategory)

    @Query("SELECT * from article_category WHERE id = :id")
    fun getItem(id: Int): Flow<ArticleCategory>

    @Query("SELECT * from article_category WHERE article_id = :article_id")
    fun getByArticle(article_id: Int): List<ArticleCategory>
}