/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.arribas.myshoppinglist.data

import android.content.Context
import com.arribas.myshoppinglist.data.db.AppDatabase
import com.arribas.myshoppinglist.data.repository.ArticleCategoryRepository
import com.arribas.myshoppinglist.data.repository.ArticleRepository
import com.arribas.myshoppinglist.data.repository.ArticleShopRepository
import com.arribas.myshoppinglist.data.repository.CategoryRepository
import com.arribas.myshoppinglist.data.repository.ShoplistArticleRepository
import com.arribas.myshoppinglist.data.repository.ShoplistRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val articleRepository: ArticleRepository
    val articleShopRepository: ArticleShopRepository
    val shoplistRepository: ShoplistRepository
    val shoplistArticleRepository: ShoplistArticleRepository
    val categoryRepository: CategoryRepository
    val articleCategoryRepository: ArticleCategoryRepository

}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {

    override val articleRepository: ArticleRepository by lazy {
        ArticleRepository(AppDatabase.getDatabase(context).articleDao())
    }

    override val articleShopRepository: ArticleShopRepository by lazy {
        ArticleShopRepository(AppDatabase.getDatabase(context).articleShopDao())
    }

    override val shoplistRepository: ShoplistRepository by lazy {
        ShoplistRepository(AppDatabase.getDatabase(context).shoplistDao())
    }

    override val shoplistArticleRepository: ShoplistArticleRepository by lazy {
        ShoplistArticleRepository(AppDatabase.getDatabase(context).shoplistArticleDao())
    }

    override val categoryRepository: CategoryRepository by lazy {
        CategoryRepository(AppDatabase.getDatabase(context).categoryDao())
    }

    override val articleCategoryRepository: ArticleCategoryRepository by lazy {
        ArticleCategoryRepository(AppDatabase.getDatabase(context).articleCategoryDao())
    }
}