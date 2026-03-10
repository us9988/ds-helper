package com.dshelper.app.presentation.home

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dshelper.app.R
import com.dshelper.app.presentation.common.UserViewModel

@Composable
fun HomeScreen(
    onLoginClick: () -> Unit,
) {
    val activity = LocalActivity.current as ViewModelStoreOwner
    val userViewModel: UserViewModel = hiltViewModel(viewModelStoreOwner = activity)
    val isLoggedIn by userViewModel.isLoggedIn.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            LogoHeader(
                isLoggedIn = isLoggedIn,
                onLoginClick = onLoginClick,
                onNotificationClick = { }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 홈 컨텐츠 — 나중에 채울 것
        }
    }
}

@Composable
fun LogoHeader(
    isLoggedIn: Boolean,
    onLoginClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 왼쪽 — 로고 이미지
        Image(
            painter = painterResource(id = R.drawable.app_logo), // 로고 이미지 파일명 맞게 수정
            contentDescription = "DSHelper 로고",
            modifier = Modifier.height(28.dp)
        )

        if (isLoggedIn) {
            IconButton(onClick = onNotificationClick) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "알림",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            OutlinedButton(onClick = onLoginClick) {
                Text("로그인")
            }
        }
    }
}