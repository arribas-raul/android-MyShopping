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

package com.arribas.myshoppinglist.ui.navigation.menudrawer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.arribas.myshoppinglist.ui.view.detailArticle.DetailScreen
import com.arribas.myshoppinglist.ui.view.detailArticle.NewScreen
import com.arribas.myshoppinglist.ui.view.listArticle.ListArticleScreen
import com.arribas.myshoppinglist.ui.view.listArticle.ListArticleShopScreen

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun MyAppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onItemClick: (NavTag) -> Unit,
) {
    //val mainViewModel = MainViewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.ShopListScreen.route,
        modifier = modifier
    ) {
        composable(
            route = Routes.ShopListScreen.route
        ){
            ListArticleShopScreen(
                navigateToItemUpdate = {}
            )
        }

        composable(
            route = Routes.ArticleListScreen.route
        ){
            ListArticleScreen(
                navigateToItemUpdate = {
                    onItemClick(NavTag.DETAIL_ITEM)
                    navController.navigate("${Routes.ArticleDetailScreen.route}/${it}")
                }
            )
        }

        composable(
            route = Routes.ArticleNewScreen.route
        ){
            NewScreen()
        }

        composable(
            route = Routes.ArticleDetailScreen.routeWithArgs,
            arguments = listOf(navArgument(Routes.ArticleDetailScreen.itemIdArg) {
                type = NavType.IntType
            })
        ){
            DetailScreen(
                navigateBack = { navController.popBackStack() },
                modifier = modifier,
            )
        }
    }
}
