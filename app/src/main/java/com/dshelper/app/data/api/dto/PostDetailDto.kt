package com.dshelper.app.data.api.dto

data class PostDetailDto(
    val postId: String,
    val title: String,
    val content: String,
    val writerName: String,
    val imageUrls: List<String>,
    val viewCount: Int,
    val createdAt: String
)