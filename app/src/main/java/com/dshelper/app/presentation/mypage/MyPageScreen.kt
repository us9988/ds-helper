package com.dshelper.app.presentation.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MyPageScreen(
    isLoggedIn: Boolean,
    onLogoutClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))  // 전체 배경 회색
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // 프로필 섹션
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 프로필 이미지
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "프로필",
                    tint = Color(0xFFAAAAAA),
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                // 이름
                Text(
                    text = if (isLoggedIn) "신인호" else "로그인이 필요해요",  // 추후 실제 이름으로 교체
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))

                // 프로필 보기
                Row(
                    modifier = Modifier.clickable {
                        if (isLoggedIn) onProfileClick()
                        else onLoginClick()
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isLoggedIn) "프로필 보기" else "로그인하기",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFAAAAAA)
                        )
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color(0xFFAAAAAA),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 메뉴 섹션
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
        ) {
            MyPageMenuItem(
                title = "도움 요청 내역",
                onClick = { /* 추후 구현 */ }
            )
            MyPageDivider()
            MyPageMenuItem(
                title = "고객 문의",
                onClick = { /* 추후 구현 */ }
            )
            MyPageDivider()
            MyPageMenuItem(
                title = "좋아요",
                onClick = { /* 추후 구현 */ }
            )
            MyPageDivider()
            MyPageMenuItem(
                title = "로그아웃",
                showArrow = false,  // 로그아웃은 화살표 없음
                onClick = onLogoutClick
            )
        }
    }
}

@Composable
private fun MyPageMenuItem(
    title: String,
    showArrow: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium
        )
        if (showArrow) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFFAAAAAA),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun MyPageDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = Color(0xFFF2F2F2)
    )
}
