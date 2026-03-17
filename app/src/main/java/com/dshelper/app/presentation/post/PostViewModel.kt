package com.dshelper.app.presentation.post

import PostEvent
import PostSideEffect
import PostUiState
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dshelper.app.domain.usecase.post.GetPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PostUiState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<PostSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        loadPosts()
    }

    fun onEvent(event: PostEvent) {
        when (event) {
            is PostEvent.OnSearchQueryChange -> {
                _uiState.update { it.copy(searchQuery = event.query) }
            }
            is PostEvent.OnSortChange -> {
                _uiState.update { it.copy(selectedSort = event.sort, currentPage = 0, posts = emptyList()) }
                loadPosts()
            }
            is PostEvent.OnPostClick -> {
                viewModelScope.launch {
                    _sideEffect.emit(PostSideEffect.NavigateToDetail(event.id))
                }
            }
            is PostEvent.OnLoadMore -> {
                if (!_uiState.value.hasNext || _uiState.value.isLoading) return
                loadPosts()
            }
        }
    }

    private fun loadPosts() {
        if (_uiState.value.isLoading) return
        viewModelScope.launch {
            Log.d("POST", "2. loadPosts 호출됨")
            Log.d("POST", "3. currentPage: ${_uiState.value.currentPage}")
            Log.d("POST", "4. sort: ${_uiState.value.selectedSort.sort}")
            Log.d("POST", "5. sortBy: ${_uiState.value.selectedSort.sortBy}")
            _uiState.update { it.copy(isLoading = true) }
            val state = _uiState.value
            getPostsUseCase(
                page = state.currentPage,
                sort = state.selectedSort.sort,
                sortBy = state.selectedSort.sortBy
            ).onSuccess { result ->
                Log.d("POST", "6. 성공: posts 개수 = ${result.posts.size}")
                Log.d("POST", "7. hasNext: ${result.hasNext}")
                _uiState.update {
                    it.copy(
                        posts = it.posts + result.posts,
                        hasNext = result.hasNext,
                        currentPage = it.currentPage + 1
                    )
                }
            }.onFailure { error ->
                Log.e("POST", "6. 실패: ${error.message}")
                Log.e("POST", "7. 실패 cause: ${error.cause}")
                _sideEffect.emit(PostSideEffect.ShowSnackbar(error.message ?: "불러오기 실패"))
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}