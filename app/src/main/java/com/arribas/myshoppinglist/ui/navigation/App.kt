package com.arribas.myshoppinglist.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.arribas.myshoppinglist.ui.navigation.AppNavHost

@Composable
fun App(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier
){
    AppNavHost(
        navController = navController,
        modifier = modifier
    )
}
