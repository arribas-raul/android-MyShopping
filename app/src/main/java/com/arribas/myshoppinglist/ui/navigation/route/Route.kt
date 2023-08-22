package com.arribas.myshoppinglist.ui.navigation.route

/**
 * Interface to describe the navigation destinations for the app
 */
interface Route {
    /**
     * Unique name to define the path for a composable
     */
    val route: String

    /**
     * String resource id to that contains title to be displayed for the screen.
     */
    val titleRes: Int
}