package com.dshelper.app.data.api.dto

data class PostListResponse(
    val posts: List<PostDto>,
    val page: PageDto
)

data class PostDto(
    val postId: String,
    val title: String,
    val content: String,
    val writerName: String,
    val imageUrls: List<String>,
    val viewCount: Int,
    val createdAt: String
)

data class PageDto(
    val page: Int,
    val size: Int,
    val totalElements: Int,
    val totalPages: Int,
    val first: Boolean,
    val last: Boolean,
    val hasNext: Boolean,
    val hasPrevious: Boolean,
    val sort: SortDto
)

data class SortDto(
    val sorted: Boolean,
    val empty: Boolean,
    val unsorted: Boolean
)
