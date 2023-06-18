package com.arribas.myshoppinglist.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.arribas.myshoppinglist.data.model.ArticleShop
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleShopDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(article: ArticleShop)

    @Update
    suspend fun update(article: ArticleShop)

    @Delete
    suspend fun delete(article: ArticleShop)

    @Query("SELECT * from articleShop WHERE id = :id")
    fun getItem(id: Int): Flow<ArticleShop>

    @Query("SELECT * from articleShop WHERE name = :name")
    fun getItemByName(name: String): Flow<ArticleShop>

    @Query("SELECT * from articleShop ORDER BY `order` DESC")
    fun getAllItems(): Flow<List<ArticleShop>>

    /**Update functions***************/
    @Query("UPDATE articleShop SET `check` = 0")
    fun reset()
}