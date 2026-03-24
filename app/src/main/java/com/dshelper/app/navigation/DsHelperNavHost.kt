package com.dshelper.app.navigation

import PostScreen
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dshelper.app.presentation.auth.login.LoginScreen
import com.dshelper.app.presentation.common.UserViewModel
import com.dshelper.app.presentation.community.CommunityScreen
import com.dshelper.app.presentation.help.HelpCompleteScreen
import com.dshelper.app.presentation.help.HelpFormScreen
import com.dshelper.app.presentation.help.HelpNoticeScreen
import com.dshelper.app.presentation.home.HomeScreen
import com.dshelper.app.presentation.post.detail.PostDetailScreen
import com.dshelper.app.presentation.profile.ProfileScreen
import com.dshelper.app.presentation.theme.Gray20
import com.dshelper.app.presentation.theme.IconDisabled
import com.dshelper.app.presentation.theme.IconPrimary
import com.dshelper.app.presentation.theme.White

@Composable
fun DsHelperNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val userViewModel: UserViewModel = hiltViewModel()  // NavHost 레벨에서 1번만 생성
    val isLoggedIn by userViewModel.isLoggedIn.collectAsStateWithLifecycle()

    val hideBottomBarRoutes = listOf(
        Screen.HelpForm.route,
        Screen.HelpComplete.route,
        Screen.PostDetail.route
    )

    val showBottomBar = currentRoute !in hideBottomBarRoutes

    val containerColor = when (currentRoute) {
        Screen.Home.route -> Gray20
        else -> Color.White
    }

    Scaffold(
        containerColor = containerColor,
        bottomBar = {
            if (showBottomBar) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    shadowElevation = 8.dp,
                    color = White,
                ) {
                    NavigationBar(
                        containerColor = Color.Transparent
                    ) {
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
                                        painter = painterResource(item.drawableRes),
                                        contentDescription = item.label,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        item.label,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Light
                                        )
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    // 선택됐을 때
                                    selectedIconColor = IconPrimary,
                                    selectedTextColor = IconPrimary,
                                    // 선택 안 됐을 때
                                    unselectedIconColor = IconDisabled,
                                    unselectedTextColor = IconDisabled,
                                    // 클릭 배경 제거
                                    indicatorColor = Color.Transparent
                                )
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        key(isLoggedIn) {
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        isLoggedIn = isLoggedIn,
                        onLoginClick = {
                            navController.navigate(Screen.Login.route)
                        },
                        onRequestHelpClick = {
                            if (isLoggedIn) {
                                navController.navigate(Screen.HelpNotice.route)
                            } else {
                                navController.navigate(Screen.Login.route)
                            }
                        },
                        onNotificationClick = {
                            userViewModel.logout()
                        }
                    )
                }
                composable(Screen.Post.route) {
                    PostScreen(
                        onPostClick = { postId ->
                            navController.navigate(Screen.PostDetail.createRoute(postId))
                        }
                    )
                }
                composable(Screen.Community.route) { CommunityScreen() }
                composable(Screen.Profile.route) { ProfileScreen() }

                // 로그인은 홈에서 push — 하단 탭 숨김
                composable(Screen.Login.route) {
                    LoginScreen(
                        onBackClick = {
                            navController.popBackStack()  // 로그인 화면 제거
                        },
                        userViewModel = userViewModel
                    )
                }
                composable(
                    route = Screen.PostDetail.route,
                    arguments = listOf(navArgument("postId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val postId = backStackEntry.arguments?.getString("postId") ?: return@composable
                    PostDetailScreen(
                        postId = postId,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                // 도움 요청 - 로그인 필요
                authComposable(
                    route = Screen.HelpNotice.route,
                    isLoggedIn = isLoggedIn,
                    navController = navController
                ) {
                    HelpNoticeScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToForm = { navController.navigate(Screen.HelpForm.route) }
                    )
                }

                authComposable(
                    route = Screen.HelpForm.route,
                    isLoggedIn = isLoggedIn,
                    navController = navController
                ) {
                    HelpFormScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToComplete = {
                            navController.navigate(Screen.HelpComplete.route) {
                                popUpTo(Screen.Home.route)
                            }
                        }
                    )
                }

                authComposable(
                    route = Screen.HelpComplete.route,
                    isLoggedIn = isLoggedIn,
                    navController = navController
                ) {
                    HelpCompleteScreen(
                        onNavigateToHome = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        }
                    )
                }

            }

        }
    }
}

// 로그인 필요한 화면 통합 처리
fun NavGraphBuilder.authComposable(
    route: String,
    isLoggedIn: Boolean,
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    composable(route) {
        Log.e("LOGIN", "isLoggedIn: $isLoggedIn")
        if (isLoggedIn) {
            content()
        } else {
            LaunchedEffect(Unit) {
                navController.navigate(Screen.Login.route) {
                    popUpTo(route) { inclusive = true }
                }
            }
        }
    }
}