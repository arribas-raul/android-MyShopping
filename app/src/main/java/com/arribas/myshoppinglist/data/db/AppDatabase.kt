package com.arribas.myshoppinglist.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arribas.myshoppinglist.data.dao.ArticleCategoryDao
import com.arribas.myshoppinglist.data.dao.ArticleDao
import com.arribas.myshoppinglist.data.dao.ArticleShopDao
import com.arribas.myshoppinglist.data.dao.CategoryDao
import com.arribas.myshoppinglist.data.dao.ShoplistArticleDao
import com.arribas.myshoppinglist.data.dao.ShoplistDao
import com.arribas.myshoppinglist.data.model.Article
import com.arribas.myshoppinglist.data.model.ArticleCategory
import com.arribas.myshoppinglist.data.model.ArticleShop
import com.arribas.myshoppinglist.data.model.Category
import com.arribas.myshoppinglist.data.model.Shoplist
import com.arribas.myshoppinglist.data.model.ShoplistArticle

@Database(
    entities = [
        Article::class,
        ArticleShop::class,
        Category::class,
        ArticleCategory::class,
        Shoplist::class,
        ShoplistArticle::class
    ],
    version = 2,
    exportSchema = false)

abstract class AppDatabase: RoomDatabase() {

    abstract fun articleDao(): ArticleDao
    abstract fun articleShopDao(): ArticleShopDao
    abstract fun categoryDao(): CategoryDao
    abstract fun articleCategoryDao(): ArticleCategoryDao
    abstract fun shoplistDao(): ShoplistDao
    abstract fun shoplistArticleDao(): ShoplistArticleDao




    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                        context, AppDatabase::class.java,"app_database")

                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }


}