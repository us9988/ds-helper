package com.dshelper.app.data.repository

import com.dshelper.app.data.api.PostApi
import com.dshelper.app.data.api.dto.PostDetailDto
import com.dshelper.app.data.api.dto.PostDto
import com.dshelper.app.domain.model.Post
import com.dshelper.app.domain.model.PostDetail
import com.dshelper.app.domain.repository.PostListResult
import com.dshelper.app.domain.repository.PostRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val postApi: PostApi
) : PostRepository {

    override suspend fun getPosts(
        page: Int,
        size: Int,
        sort: String,
        sortBy: String
    ): Result<PostListResult> {
        return runCatching {
            val response = postApi.getPosts(page, size, sort, sortBy)
            PostListResult(
                posts = response.posts.map { it.toDomain() },
                hasNext = response.page.hasNext,
                totalElements = response.page.totalElements
            )
        }
    }

    override suspend fun getPostDetail(postId: String): Result<PostDetail> {
        return runCatching {
            val response = postApi.getPostDetail(postId)
            response.toDomain()
        }
    }
}

fun PostDto.toDomain() = Post(
    id = postId,
    title = title,
    content = content,
    writerName = writerName,
    imageUrl = imageUrls.firstOrNull(),
    viewCount = viewCount,
    createdAt = createdAt
)

fun PostDetailDto.toDomain() = PostDetail(
    id = postId,
    title = title,
    content = content,
    writerName = writerName,
    imageUrls = imageUrls,
    viewCount = viewCount,
    createdAt = createdAt
)