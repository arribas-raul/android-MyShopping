package com.arribas.myshoppinglist.ui.view

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.arribas.myshoppinglist.AppApplication
import com.arribas.myshoppinglist.ui.view.category.CategoryViewModel
import com.arribas.myshoppinglist.ui.view.article.articleDetail.DetailViewModel
import com.arribas.myshoppinglist.ui.view.article.articleNew.NewViewModel
import com.arribas.myshoppinglist.ui.view.shoplist.shoplistBottomSheet.ShoplistBottomSheetViewModel
import com.arribas.myshoppinglist.ui.view.shoplistList.ListArticleViewModel
import com.arribas.myshoppinglist.ui.view.shoplist.shoplistList.ShoplistListViewModel
import com.arribas.myshoppinglist.ui.viewModel.listArticleShop.ListArticleShopViewModel
import com.arribas.myshoppinglist.ui.viewModel.listArticleShop.ShoplistDetailViewModel

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
                AppApplication().container.articleShopRepository,
                AppApplication().container.articleCategoryRepository
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

        initializer {
            ShoplistListViewModel(
                AppApplication().container.shoplistRepository
            )
        }

        initializer {
            ShoplistBottomSheetViewModel(
                AppApplication().container.shoplistRepository
            )
        }

        initializer {
            ShoplistDetailViewModel(
                this.createSavedStateHandle(),
                AppApplication().container.shoplistRepository,
                AppApplication().container.shoplistArticleRepository
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
