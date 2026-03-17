package com.dshelper.app.domain.model

data class Post(
    val id: String,
    val title: String,
    val content: String,
    val writerName: String,
    val imageUrl: String?,
    val viewCount: Int,
    val createdAt: String
)