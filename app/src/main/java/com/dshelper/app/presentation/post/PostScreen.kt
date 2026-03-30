import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.dshelper.app.domain.model.Post
import com.dshelper.app.presentation.common.DsSnackbarHost
import com.dshelper.app.presentation.common.DsTopBar
import com.dshelper.app.presentation.post.PostViewModel
import com.dshelper.app.presentation.theme.BgEnabled
import com.dshelper.app.presentation.theme.BorderDefault
import com.dshelper.app.presentation.theme.Gray50
import com.dshelper.app.presentation.theme.TextInverse
import com.dshelper.app.presentation.theme.TextTertiary
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun PostScreen(
    onPostClick: (String) -> Unit,
    viewModel: PostViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()

    // 맨 아래 도달 감지
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { lastIndex ->
                val totalItems = listState.layoutInfo.totalItemsCount
                if (
                    lastIndex != null &&
                    lastIndex >= totalItems - 1 &&
                    uiState.hasNext &&
                    !uiState.isLoading
                ) {
                    viewModel.onEvent(PostEvent.OnLoadMore)
                }
            }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is PostSideEffect.NavigateToDetail -> onPostClick(effect.id)
                is PostSideEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(
        containerColor = Color.White,
        snackbarHost = { DsSnackbarHost(snackbarHostState) },
        topBar = {
            DsTopBar(
                title = "활동"
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            PostSearchBar(
                query = uiState.searchQuery,
                onQueryChange = { viewModel.onEvent(PostEvent.OnSearchQueryChange(it)) }
            )

            PostSortButtons(
                selectedSort = uiState.selectedSort,
                onSortChange = { viewModel.onEvent(PostEvent.OnSortChange(it)) }
            )

            if (uiState.isLoading && uiState.posts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (uiState.filteredPosts.isEmpty() && !uiState.isLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(48.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (uiState.searchQuery.isBlank()) "게시글이 없어요."
                                    else "'${uiState.searchQuery}' 검색 결과가 없어요.",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Color(0xFFAAAAAA)
                                    )
                                )
                            }
                        }
                    }else{
                        items(uiState.filteredPosts, key = { post -> "post_${post.id}" }) { post ->
                            PostItem(
                                post = post,
                                onClick = { viewModel.onEvent(PostEvent.OnPostClick(post.id)) }
                            )
                        }
                    }
                    if (uiState.isLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PostSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color(0xFFF2F2F2), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                decorationBox = { innerTextField ->
                    if (query.isEmpty()) {
                        Text(
                            text = "게시물의 제목을 입력해보세요",
                            style = MaterialTheme.typography.bodySmall,
                            color = Gray50
                        )
                    }
                    innerTextField()
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            if (query.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "검색어 지우기",
                    tint = Color(0xFFAAAAAA),
                    modifier = Modifier
                        .size(18.dp)
                        .clickable { onQueryChange("") }
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "검색",
                    tint = Color(0xFFAAAAAA)
                )
            }
        }
    }
}

@Composable
private fun PostSortButtons(
    selectedSort: PostSort,
    onSortChange: (PostSort) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PostSort.entries.forEach { sort ->
            val isSelected = selectedSort == sort
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) BgEnabled else Color.Transparent)
                    .border(
                        width = 1.dp,
                        color = BorderDefault,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable { onSortChange(sort) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = sort.label,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (isSelected) TextInverse else TextTertiary
                    )
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
private fun PostItem(
    post: Post,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(Color.Transparent)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = post.createdAt,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextTertiary
                )
                Text(
                    text = "조회 ${post.viewCount}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextTertiary
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        AsyncImage(
            model = post.imageUrl,
            contentDescription = post.title,
            modifier = Modifier
                .width(100.dp)
                .height(75.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )
    }
}
