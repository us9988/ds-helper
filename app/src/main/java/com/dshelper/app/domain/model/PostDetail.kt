package com.dshelper.app.domain.model

data class PostDetail(
    val id: String,
    val title: String,
    val content: String,
    val writerName: String,
    val imageUrls: List<String>,
    val viewCount: Int,
    val createdAt: String
)