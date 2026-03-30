import com.dshelper.app.domain.model.Post

data class PostUiState(
    val posts: List<Post> = emptyList(),
    val searchQuery: String = "",
    val selectedSort: PostSort = PostSort.LATEST,
    val isLoading: Boolean = false,
    val hasNext: Boolean = false,
    val currentPage: Int = 0
){
    val filteredPosts: List<Post>
        get() = if (searchQuery.isBlank()) posts
        else posts.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.content.contains(searchQuery, ignoreCase = true) ||
                    it.writerName.contains(searchQuery, ignoreCase = true)
        }
}

enum class PostSort(val label: String, val sort: String, val sortBy: String) {
    LATEST("최신순", "desc", "createdAt"),
    POPULAR("인기순", "desc", "createdAt")
}

sealed interface PostEvent {
    data class OnSearchQueryChange(val query: String) : PostEvent
    data class OnSortChange(val sort: PostSort) : PostEvent
    data class OnPostClick(val id: String) : PostEvent
    data object OnLoadMore : PostEvent
}

sealed interface PostSideEffect {
    data class NavigateToDetail(val id: String) : PostSideEffect
    data class ShowSnackbar(val message: String) : PostSideEffect
}
