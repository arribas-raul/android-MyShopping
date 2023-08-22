package com.arribas.myshoppinglist.ui.view

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.arribas.myshoppinglist.AppApplication
import com.arribas.myshoppinglist.ui.view.Category.CategoryViewModel
import com.arribas.myshoppinglist.ui.view.detailArticle.DetailViewModel
import com.arribas.myshoppinglist.ui.view.newArticle.NewViewModel
import com.arribas.myshoppinglist.ui.viewModel.ListArticleViewModel
import com.arribas.myshoppinglist.ui.viewModel.listArticleShop.ListArticleShopViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ListArticleShopViewModel(
                AppApplication().container.articleRepository,
                AppApplication().container.articleShopRepository
            )
        }

        initializer {
            ListArticleViewModel(
                AppApplication().container.articleRepository,
                AppApplication().container.articleShopRepository
            )
        }

        initializer {
            NewViewModel(
                AppApplication().container.articleCategoryRepository,
                AppApplication().container.articleRepository
            )
        }

        initializer {
            DetailViewModel(
                this.createSavedStateHandle(),
                AppApplication().container.articleRepository,
                AppApplication().container.articleCategoryRepository,
            )
        }

        initializer {
            CategoryViewModel(
                AppApplication().container.categoryRepository
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.AppApplication(): AppApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as AppApplication)
