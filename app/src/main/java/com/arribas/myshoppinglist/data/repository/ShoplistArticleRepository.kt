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

import com.arribas.myshoppinglist.data.dao.ShoplistArticleDao
import com.arribas.myshoppinglist.data.dao.ShoplistDao
import com.arribas.myshoppinglist.data.model.Shoplist
import com.arribas.myshoppinglist.data.model.ShoplistArticle
import kotlinx.coroutines.flow.Flow

interface ShoplistArticleRepositoryInterface {
    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getItem(id: Int): Flow<ShoplistArticle?>

    fun getItemByListAndArticle(list_id: Int, article_id: Int): Flow<ShoplistArticle?>

    fun getItemsByList(list_id: Int): Flow<List<ShoplistArticle>>

    suspend fun count(id: Int): Int

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(item: ShoplistArticle)

    /**
     * Delete item from the data source
     */
    suspend fun deleteItem(item: ShoplistArticle)

    /**
     * Update item in the data source
     */
    suspend fun updateItem(item: ShoplistArticle)

    suspend fun reset(id: Int)
}

class ShoplistArticleRepository(private val shoplistArticleDao: ShoplistArticleDao) : ShoplistArticleRepositoryInterface {

    override fun getItem(id: Int): Flow<ShoplistArticle?> = shoplistArticleDao.getItem(id)

    override fun getItemByListAndArticle(list_id: Int, article_id: Int): Flow<ShoplistArticle?> =
        shoplistArticleDao.getItemByListAndArticle(list_id, article_id)

    override fun getItemsByList(list_id: Int): Flow<List<ShoplistArticle>> = shoplistArticleDao.getItemsByList(list_id)

    override suspend fun count(id: Int): Int = shoplistArticleDao.count(id)

    override suspend fun insertItem(item: ShoplistArticle) = shoplistArticleDao.insert(item)

    override suspend fun deleteItem(item: ShoplistArticle) = shoplistArticleDao.delete(item)

    override suspend fun updateItem(item: ShoplistArticle) = shoplistArticleDao.update(item)

    override suspend fun reset(id: Int) = shoplistArticleDao.reset(id)
}
