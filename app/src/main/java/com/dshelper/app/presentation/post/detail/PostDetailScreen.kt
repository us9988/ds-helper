package com.dshelper.app.presentation.post.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dshelper.app.R
import com.dshelper.app.presentation.common.DsTopBar
import com.dshelper.app.presentation.common.DsSnackbarHost
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PostDetailScreen(
    postId: String,
    viewModel: PostDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is PostDetailSideEffect.NavigateBack -> onNavigateBack()
                is PostDetailSideEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { DsSnackbarHost(snackbarHostState) },
        topBar = {
            DsTopBar(
                title = "활동",                  // ✅ 활동으로 변경
                showBackButton = true,
                onBackClick = { viewModel.onEvent(PostDetailEvent.OnBackClick) }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            uiState.postDetail?.let { post ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 제목 + 날짜 (가운데 정렬)
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = post.title,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = formatDate(post.createdAt),  // 날짜 + 요일
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFAAAAAA),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    if (post.imageUrls.isNotEmpty()) {
                        items(post.imageUrls) { imageUrl ->
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(imageUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop,
                                error = painterResource(R.drawable.ic_image_error),  // 실패 이미지 ✅
                            )
                        }
                    }

                    // 본문
                    item {
                        Text(
                            text = post.content,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

// 날짜 + 요일 포맷 함수
private fun formatDate(dateStr: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA)
        val date = inputFormat.parse(dateStr) ?: return dateStr
        val outputFormat = SimpleDateFormat("yyyy년 MM월 dd일 (E)", Locale.KOREA)
        outputFormat.format(date)
    } catch (e: Exception) {
        dateStr  // 파싱 실패 시 원본 반환
    }
}