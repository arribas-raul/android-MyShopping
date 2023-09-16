package com.arribas.myshoppinglist.ui.view.app

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.arribas.myshoppinglist.AppApplication
import com.arribas.myshoppinglist.ui.view.app.app.AppViewModel
import com.arribas.myshoppinglist.ui.view.screen.category.CategoryViewModel
import com.arribas.myshoppinglist.ui.view.screen.article.articleDetail.ArticleDetailViewModel
import com.arribas.myshoppinglist.ui.view.screen.article.articleNew.NewViewModel
import com.arribas.myshoppinglist.ui.view.screen.shoplist.shoplistBottomSheet.ShoplistBottomSheetViewModel
import com.arribas.myshoppinglist.ui.view.screen.article.articleList.ListArticleViewModel
import com.arribas.myshoppinglist.ui.view.screen.shoplist.shoplistList.ShoplistListViewModel
import com.arribas.myshoppinglist.ui.view.screen.listArticleShop.ListArticleShopViewModel
import com.arribas.myshoppinglist.ui.view.screen.shoplist.shoplistDetail.ShoplistDetailViewModel
import com.arribas.myshoppinglist.ui.view.screen.shoplist.shoplistDetail.ShoplistSelectDialogViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            AppViewModel(AppApplication())
        }

        initializer {
            ListArticleShopViewModel(
                AppApplication().applicationContext,
                AppApplication().container.articleRepository,
                AppApplication().container.articleShopRepository
            )
        }

        initializer {
            ListArticleViewModel(
                this.createSavedStateHandle(),
                AppApplication().applicationContext,
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
            ArticleDetailViewModel(
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
            ShoplistSelectDialogViewModel(
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
                AppApplication().applicationContext,
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
