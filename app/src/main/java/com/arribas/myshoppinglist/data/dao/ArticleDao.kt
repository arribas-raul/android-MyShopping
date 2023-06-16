package com.arribas.myshoppinglist.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.arribas.myshoppinglist.data.model.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(article: Article)

    @Update
    suspend fun update(article: Article)

    @Delete
    suspend fun delete(article: Article)

    @Query("SELECT * from article WHERE id = :id")
    fun getItem(id: Int): Flow<Article>

    @Query("SELECT * from article WHERE name = :name")
    fun getItemByName(name: String): Flow<Article>

    @Query("SELECT * from article WHERE shopCheked = 0 ORDER BY name ASC")
    fun getAllItems(): Flow<List<Article>>
}