package com.dshelper.app.navigation

sealed class Screen(val route: String) {

    // Auth
    object Login : Screen("login")

    // Main
    object Home : Screen("home")
    object Activity : Screen("activity")
    object Community : Screen("community")
    object Profile : Screen("profile")
}
