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

package com.arribas.myshoppinglist.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.ui.view.detailArticle.DetailDestination
import com.arribas.myshoppinglist.ui.view.detailArticle.DetailScreen
import com.arribas.myshoppinglist.ui.view.main.MainDestination
import com.arribas.myshoppinglist.ui.view.main.MainScreen
import com.arribas.myshoppinglist.ui.viewModel.MainViewModel

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val mainViewModel = MainViewModel()

    NavHost(
        navController = navController,
        startDestination = MainDestination.route,
        modifier = modifier.background(colorResource(R.color.my_background))
    ) {
        composable(
            route = MainDestination.route
        ){
            MainScreen(
                navController = navController,
                viewModel = mainViewModel,
                modifier = modifier
            )
        }

        composable(
            route = DetailDestination.routeWithArgs,
            arguments = listOf(navArgument(DetailDestination.itemIdArg) {
                type = NavType.IntType
            })
        ){
            DetailScreen(
                navigateBack = { navController.popBackStack() },
                modifier = modifier
            )
        }

        /*composable(route = ListArticleDestination.route) {
            ListScreen(
                navigateToItemEntry = { navController.navigate("${NewDestination.route}") },
                navigateToItemUpdate = {
                    navController.navigate("${DetailDestination.route}/${it}")
                }
            )
        }*/

        /*composable(route = NewDestination.route) {
            NewScreen(
                navigateBack = { navController.popBackStack() }
            )
        }*/
    }
}
