package com.dshelper.app.navigation

import com.dshelper.app.R

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val drawableRes: Int
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home, "홈", R.drawable.ic_home),
    BottomNavItem(Screen.Post, "활동", R.drawable.ic_post),
    BottomNavItem(Screen.Community, "소통", R.drawable.ic_community),
    BottomNavItem(Screen.MyPage, "마이", R.drawable.ic_my_page)
)
