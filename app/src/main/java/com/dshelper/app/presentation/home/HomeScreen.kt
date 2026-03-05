package com.dshelper.app.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dshelper.app.R

@Composable
fun HomeScreen(
    onLoginClick: () -> Unit
) {
    Scaffold(
        topBar = {
            LogoHeader(onLoginClick = onLoginClick)
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
    onLoginClick: () -> Unit
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

        // 오른쪽 — 로그인 버튼
        OutlinedButton(
            onClick = onLoginClick,
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.Black),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text(
                text = "로그인",
                color = Color.Black,
                fontSize = 14.sp
            )
        }
    }
}