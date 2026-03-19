package com.dshelper.app.navigation

sealed class Screen(val route: String) {

    // Auth
    object Login : Screen("login")

    // Bottom tab
    object Home : Screen("home")
    object Post : Screen("post")
    object Community : Screen("community")
    object Profile : Screen("profile")

    object HelpNotice : Screen("help_notice")
    object HelpForm : Screen("help_form")
    object HelpComplete : Screen("help_complete")

    // etc
    object PostDetail : Screen("post/{postId}") {
        fun createRoute(postId: String) = "post/$postId"
    }
}
