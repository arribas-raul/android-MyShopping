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

import com.arribas.myshoppinglist.data.dao.ShoplistDao
import com.arribas.myshoppinglist.data.model.Article
import com.arribas.myshoppinglist.data.model.QArticle
import com.arribas.myshoppinglist.data.model.Shoplist
import kotlinx.coroutines.flow.Flow

interface ShoplistRepositoryInterface {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllItems(): Flow<List<Shoplist>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getItem(id: Int): Flow<Shoplist?>
    fun getItemByName(name: String): Flow<Shoplist?>

    fun getItemsByName(name: String): Flow<List<Shoplist>>

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(item: Shoplist)

    /**
     * Delete item from the data source
     */
    suspend fun deleteItem(item: Shoplist)

    /**
     * Update item in the data source
     */
    suspend fun updateItem(item: Shoplist)
}

class ShoplistRepository(private val shoplistDao: ShoplistDao) : ShoplistRepositoryInterface {
    override fun getAllItems(): Flow<List<Shoplist>> = shoplistDao.getAllItems()

    override fun getItem(id: Int): Flow<Shoplist?> = shoplistDao.getItem(id)

    override fun getItemByName(name: String): Flow<Shoplist?> = shoplistDao.getItemByName(name)

    override fun getItemsByName(name: String): Flow<List<Shoplist>> = shoplistDao.getItemsByName(name)

    override suspend fun insertItem(item: Shoplist) = shoplistDao.insert(item)

    override suspend fun deleteItem(item: Shoplist) = shoplistDao.delete(item)

    override suspend fun updateItem(item: Shoplist) = shoplistDao.update(item)
}
