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

import com.arribas.myshoppinglist.data.dao.ArticleShopDao
import com.arribas.myshoppinglist.data.model.ArticleShop
import kotlinx.coroutines.flow.Flow

interface ArticleShopRepositoryInterface {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllItems(): Flow<List<ArticleShop>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getItem(id: Int): Flow<ArticleShop?>

    fun getItemByName(name: String): Flow<ArticleShop?>

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(item: ArticleShop)

    /**
     * Delete item from the data source
     */
    suspend fun deleteItem(item: ArticleShop)

    /**
     * Update item in the data source
     */
    suspend fun updateItem(item: ArticleShop)

    suspend fun reset()
}

class ArticleShopRepository(private val articleShopDao: ArticleShopDao) : ArticleShopRepositoryInterface {
    override fun getAllItems(): Flow<List<ArticleShop>> = articleShopDao.getAllItems()

    override fun getItem(id: Int): Flow<ArticleShop?> = articleShopDao.getItem(id)

    override fun getItemByName(name: String): Flow<ArticleShop?> = articleShopDao.getItemByName(name)

    override suspend fun insertItem(item: ArticleShop) = articleShopDao.insert(item)

    override suspend fun deleteItem(item: ArticleShop) = articleShopDao.delete(item)

    override suspend fun updateItem(item: ArticleShop) = articleShopDao.update(item)

    override suspend fun reset() = articleShopDao.reset()
}
