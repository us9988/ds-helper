package com.dshelper.app.presentation.post.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dshelper.app.domain.usecase.post.GetPostDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val getPostDetailUseCase: GetPostDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val postId: String = checkNotNull(savedStateHandle["postId"])

    private val _uiState = MutableStateFlow(PostDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<PostDetailSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        loadPostDetail()
    }

    fun onEvent(event: PostDetailEvent) {
        when (event) {
            is PostDetailEvent.OnBackClick -> {
                viewModelScope.launch {
                    _sideEffect.emit(PostDetailSideEffect.NavigateBack)
                }
            }
        }
    }

    private fun loadPostDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getPostDetailUseCase(postId)
                .onSuccess { postDetail ->
                    _uiState.update { it.copy(postDetail = postDetail) }
                }
                .onFailure { error ->
                    _sideEffect.emit(PostDetailSideEffect.ShowSnackbar(error.message ?: "불러오기 실패"))
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}