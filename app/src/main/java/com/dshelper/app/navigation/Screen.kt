package com.dshelper.app.navigation

sealed class Screen(val route: String) {

    // Auth
    object Login : Screen("login")

    // Bottom tab
    object Home : Screen("home")
    object Post : Screen("post")
    object Community : Screen("community")
    object Profile : Screen("profile")

    // etc
    object PostDetail : Screen("post/{postId}") {
        fun createRoute(postId: String) = "post/$postId"
    }
}
