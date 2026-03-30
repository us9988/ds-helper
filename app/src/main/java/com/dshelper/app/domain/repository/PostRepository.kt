package com.dshelper.app.domain.repository

import com.dshelper.app.domain.model.Post
import com.dshelper.app.domain.model.PostDetail

interface PostRepository {
    suspend fun getPosts(
        page: Int,
        size: Int,
        sort: String,
        sortBy: String
    ): Result<PostListResult>

    suspend fun getPostDetail(postId: String): Result<PostDetail>
}

data class PostListResult(
    val posts: List<Post>,
    val hasNext: Boolean,
    val totalElements: Int
)
