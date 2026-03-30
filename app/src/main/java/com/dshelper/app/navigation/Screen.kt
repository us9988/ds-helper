package com.dshelper.app.navigation

sealed class Screen(val route: String) {

    // Auth
    object Login : Screen("login")

    // 홈 탭
    object Home : Screen("home")
    object HelpNotice : Screen("help_notice")
    object HelpForm : Screen("help_form")
    object HelpComplete : Screen("help_complete")

    // 활동 탭
    object Post : Screen("post")
    object PostDetail : Screen("post/{postId}") {
        fun createRoute(postId: String) = "post/$postId"
    }

    // 소통 탭
    object Community : Screen("community")

    // 마이 탭
    object MyPage : Screen("my_page")
    object Profile : Screen("profile")
    object EditName : Screen("edit_name")

}
