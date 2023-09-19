package com.arribas.myshoppinglist.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.arribas.myshoppinglist.data.model.ShoplistArticle
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoplistArticleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shopListArticle: ShoplistArticle)

    @Update
    suspend fun update(shopListArticle: ShoplistArticle)

    @Delete
    suspend fun delete(shopListArticle: ShoplistArticle)

    @Query("SELECT * from shoplist_article WHERE id = :id")
    fun getItem(id: Int): Flow<ShoplistArticle>

    @Query("SELECT * " +
            "FROM shoplist_article " +
            "WHERE shoplist_id = :list_id " +
            "AND article_id = :article_id ")
    fun getItemByListAndArticle(list_id: Int, article_id: Int): Flow<ShoplistArticle>

    @Query("SELECT * " +
            "FROM shoplist_article " +
            "WHERE shoplist_id = :list_id " +
            "ORDER BY `order`")
    fun getItemsByList(list_id: Int): Flow<List<ShoplistArticle>>

    @Query("SELECT count(id) " +
            "FROM shoplist_article " +
            "WHERE shoplist_id = :id")
    fun count(id: Int): Int

    /**Update functions***************/
    @Query("UPDATE shoplist_article " +
            "SET `check` = 0 " +
            "WHERE shoplist_id = :id")
    fun reset(id: Int)
}