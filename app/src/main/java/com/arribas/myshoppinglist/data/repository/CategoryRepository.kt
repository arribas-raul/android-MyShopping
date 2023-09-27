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

import com.arribas.myshoppinglist.data.dao.CategoryDao
import com.arribas.myshoppinglist.data.model.Article
import com.arribas.myshoppinglist.data.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepositoryInterface {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllItems(): Flow<List<Category>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getItem(id: Int): Flow<Category?>

    fun getItemByName(name: String): Flow<Category?>

    fun getItemsByName(name: String): Flow<List<Category>>

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(item: Category)

    /**
     * Delete item from the data source
     */
    suspend fun deleteItem(item: Category)

    /**
     * Update item in the data source
     */
    suspend fun updateItem(item: Category)
}

class CategoryRepository(private val categoryDao: CategoryDao) : CategoryRepositoryInterface {
    override fun getAllItems(): Flow<List<Category>> = categoryDao.getAllItems()

    override fun getItem(id: Int): Flow<Category?> = categoryDao.getItem(id)

    override fun getItemByName(name: String): Flow<Category?> = categoryDao.getItemByName(name)

    override fun getItemsByName(name: String): Flow<List<Category>> = categoryDao.getItemsByName(name)

    override suspend fun insertItem(item: Category) = categoryDao.insert(item)

    override suspend fun deleteItem(item: Category) = categoryDao.delete(item)

    override suspend fun updateItem(item: Category) = categoryDao.update(item)
}
