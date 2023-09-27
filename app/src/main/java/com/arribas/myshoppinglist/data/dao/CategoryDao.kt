package com.arribas.myshoppinglist.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.arribas.myshoppinglist.data.model.Article
import com.arribas.myshoppinglist.data.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(category: Category)

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Query("SELECT * from category WHERE id = :id")
    fun getItem(id: Int): Flow<Category>

    @Query("SELECT * from category WHERE name = :name")
    fun getItemByName(name: String): Flow<Category>

    @Query("SELECT * " +
            "FROM category " +
            "WHERE name like '%' || :name || '%' ")
    fun getItemsByName(name: String): Flow<List<Category>>

    @Query("SELECT * from category ORDER BY name ASC")
    fun getAllItems(): Flow<kotlin.collections.List<Category>>
}