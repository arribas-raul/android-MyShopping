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

import com.arribas.myshoppinglist.data.dao.ArticleDao
import com.arribas.myshoppinglist.data.model.Article
import com.arribas.myshoppinglist.data.model.QArticle
import kotlinx.coroutines.flow.Flow

interface ArticleRepositoryInterface {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllItems(): Flow<List<Article>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getItem(id: Int): Flow<Article?>

    fun getItemByName(name: String): Flow<Article?>

    fun getItemsByName(shoplist_id: Int, name: String): Flow<List<QArticle>>

    fun getItemsByFilter(shoplist_id: Int, name: String, category: Int): Flow<List<QArticle>>

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(item: Article): Long

    /**
     * Delete item from the data source
     */
    suspend fun deleteItem(item: Article)

    /**
     * Update item in the data source
     */
    suspend fun updateItem(item: Article)
}

class ArticleRepository(private val articleDao: ArticleDao) : ArticleRepositoryInterface {
    override fun getAllItems(): Flow<List<Article>> = articleDao.getAllItems()

    override fun getItem(id: Int): Flow<Article?> = articleDao.getItem(id)

    override fun getItemByName(name: String): Flow<Article?> = articleDao.getItemByName(name)

    override fun getItemsByName(shoplist_id: Int, name: String): Flow<List<QArticle>> =
        articleDao.getItemsByName(shoplist_id, name)

    override fun getItemsByFilter(shoplist_id: Int, name: String, category: Int): Flow<List<QArticle>> =
        articleDao.getItemsByFilter(shoplist_id, name, category)

    override suspend fun insertItem(item: Article) = articleDao.insert(item)

    override suspend fun deleteItem(item: Article) = articleDao.delete(item)

    override suspend fun updateItem(item: Article) = articleDao.update(item)
}
