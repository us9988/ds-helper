package com.dshelper.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home, "홈", Icons.Default.Home),
    BottomNavItem(Screen.Activity, "활동", Icons.Default.Favorite),
    BottomNavItem(Screen.Community, "소통", Icons.Default.Forum),
    BottomNavItem(Screen.Profile, "프로필", Icons.Default.Person)
)