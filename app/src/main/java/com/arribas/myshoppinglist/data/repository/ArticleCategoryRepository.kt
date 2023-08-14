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

package com.arribas.myshoppinglist.data.repository

import com.arribas.myshoppinglist.data.dao.ArticleCategoryDao
import com.arribas.myshoppinglist.data.dao.CategoryDao
import com.arribas.myshoppinglist.data.model.Article
import com.arribas.myshoppinglist.data.model.ArticleCategory
import com.arribas.myshoppinglist.data.model.Category
import com.arribas.myshoppinglist.data.model.QArticleCategory
import kotlinx.coroutines.flow.Flow

interface ArticleCategoryRepositoryInterface {
    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getItem(id: Int): Flow<ArticleCategory?>

    fun getByArticle(article_id: Int): List<QArticleCategory>

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(item: ArticleCategory)

    /**
     * Delete item from the data source
     */
    suspend fun deleteItem(item: ArticleCategory)

    /**
     * Update item in the data source
     */
    suspend fun updateItem(item: ArticleCategory)
}

class ArticleCategoryRepository(private val articleCategoryDao: ArticleCategoryDao) : ArticleCategoryRepositoryInterface {

    override fun getItem(id: Int): Flow<ArticleCategory?> = articleCategoryDao.getItem(id)

    override fun getByArticle(article_id: Int): List<QArticleCategory> = articleCategoryDao.getByArticle(article_id)

    override suspend fun insertItem(item: ArticleCategory) = articleCategoryDao.insert(item)

    override suspend fun deleteItem(item: ArticleCategory) = articleCategoryDao.delete(item)

    override suspend fun updateItem(item: ArticleCategory) = articleCategoryDao.update(item)
}
