package com.dshelper.app.presentation.post.detail

import com.dshelper.app.domain.model.PostDetail

data class PostDetailUiState(
    val postDetail: PostDetail? = null,
    val isLoading: Boolean = false
)

sealed interface PostDetailEvent {
    data object OnBackClick : PostDetailEvent
}

sealed interface PostDetailSideEffect {
    data object NavigateBack : PostDetailSideEffect
    data class ShowSnackbar(val message: String) : PostDetailSideEffect
}
