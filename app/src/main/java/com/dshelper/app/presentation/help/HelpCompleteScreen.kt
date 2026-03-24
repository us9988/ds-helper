package com.dshelper.app.presentation.help

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dshelper.app.R
import com.dshelper.app.presentation.common.DsButton
import com.dshelper.app.presentation.theme.BgBrand
import com.dshelper.app.presentation.theme.BgDangerSoft
import com.dshelper.app.presentation.theme.Gray80
import com.dshelper.app.presentation.theme.TextDanger
import com.dshelper.app.presentation.theme.TextInverse
import com.dshelper.app.presentation.theme.TextSecondary

@Composable
fun HelpCompleteScreen(
    onNavigateToHome: () -> Unit
) {
    var showNotificationDialog by remember { mutableStateOf(true) }
    // 알림 설정 다이얼로그 ✅
    if (showNotificationDialog) {
        Dialog(onDismissRequest = { showNotificationDialog = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                // 상단 이미지
                Image(
                    painter = painterResource(R.drawable.ic_notification),
                    contentDescription = "알림",
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.Start)  // 시작 정렬 ✅
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 제목
                Text(
                    text = "요청 처리 결과를 알려드릴게요!",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 서브텍스트
                Text(
                    text = "알림을 허용하시면, 확정 및 취소 안내를 받을 수 있어요.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary
                    ),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 허용하기 - 초록색 ✅
                    Button(
                        onClick = {
                            showNotificationDialog = false
                            // 알림 설정 로직 추후 구현
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BgBrand
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    ) {
                        Text(
                            text = "허용하기",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = TextInverse
                            )
                        )
                    }
                    // 거절하기 - 빨간색 ✅
                    Button(
                        onClick = { showNotificationDialog = false },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BgDangerSoft
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    ) {
                        Text(
                            text = "거절하기",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = TextDanger
                            )
                        )
                    }
                }
            }
        }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 48.dp)
            ) {
                Text(
                    text = "도움 요청이 접수되었어요",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "헬퍼가 예약 내용을 확인한 후 확정 여부를 알려드릴게요.\n확정되면 알림을 보내드릴 예정이에요.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Gray80
                    ),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(12.dp))

                Image(
                    painter = painterResource(R.drawable.ic_help_complete),
                    contentDescription = "접수 완료",
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
            }

            DsButton(
                text = "돌아가기",
                onClick = onNavigateToHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}