package com.dshelper.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dshelper.app.presentation.activity.ActivityScreen
import com.dshelper.app.presentation.community.CommunityScreen
import com.dshelper.app.presentation.home.HomeScreen
import com.dshelper.app.presentation.profile.ProfileScreen

@Composable
fun DsHelperNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 바텀 탭 숨길 화면
    val hideBottomBar = currentRoute == Screen.Login.route

    Scaffold(
        bottomBar = {
            if (!hideBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.screen.route,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(Screen.Home.route) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onLoginClick = {
                        navController.navigate(Screen.Login.route)
                    }
                )
            }
            composable(Screen.Activity.route) { ActivityScreen() }
            composable(Screen.Community.route) { CommunityScreen() }
            composable(Screen.Profile.route) { ProfileScreen() }

            // 로그인은 홈에서 push — 하단 탭 숨김
//            composable(Screen.Login.route) {
//                LoginScreen(
//                    onBackClick = { navController.popBackStack() }
//                )
//            }
        }
    }
}