package com.arribas.myshoppinglist.ui.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.arribas.myshoppinglist.ui.navigation.AppNavHost

@Composable
fun App(navController: NavHostController = rememberNavController()){
    AppNavHost(navController = navController)
}
