package com.arribas.myshoppinglist.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.arribas.myshoppinglist.data.model.Shoplist
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoplistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shoplist: Shoplist)

    @Update
    suspend fun update(shoplist: Shoplist)

    @Delete
    suspend fun delete(shopList: Shoplist)

    @Query("SELECT * from shoplist WHERE id = :id")
    fun getItem(id: Int): Flow<Shoplist>

    @Query("SELECT * from shoplist ORDER BY name ASC")
    fun getAllItems(): Flow<List<Shoplist>>
}