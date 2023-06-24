package com.arribas.myshoppinglist.data.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arribas.myshoppinglist.data.dao.ArticleDao
import com.arribas.myshoppinglist.data.dao.ArticleShopDao
import com.arribas.myshoppinglist.data.model.Article
import com.arribas.myshoppinglist.data.model.ArticleShop

@Database(
    entities = [Article::class, ArticleShop::class],
    version = 1,
    exportSchema = false)

abstract class AppDatabase: RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    abstract fun articleShopDao(): ArticleShopDao

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