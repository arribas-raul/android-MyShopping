package com.arribas.myshoppinglist.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.arribas.myshoppinglist.data.model.Article
import com.arribas.myshoppinglist.data.model.QArticle
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(article: Article): Long

    @Update
    suspend fun update(article: Article)

    @Delete
    suspend fun delete(article: Article)

    @Query("SELECT * from article WHERE id = :id")
    fun getItem(id: Int): Flow<Article>

    @Query("SELECT * from article WHERE name = :name")
    fun getItemByName(name: String): Flow<Article>

    @Query("SELECT a.*, " +

            "(select group_concat(c.name, ',') as category " +
            "from category as c " +
            "join article_category as ac on ac.category_id = c.id " +
            "where ac.article_id = a.id " +
            "order by c.name) as category " +

            "FROM article as a " +
            "LEFT JOIN shoplist_article as sla on a.id = sla.article_id " +
            "   and sla.shoplist_id = :shoplist_id " +

            "WHERE a.name like '%' || :name || '%' " +

            "ORDER BY a.name ASC")
    fun getItemsByName(shoplist_id: Int, name: String): Flow<List<QArticle>>

    @Query("SELECT * from article WHERE shopCheked = 0 ORDER BY name ASC")
    fun getAllItems(): Flow<List<Article>>

}