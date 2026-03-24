package com.dshelper.app.data.api.dto

data class  AuthResponse<T>(
    val success: Boolean,
    val code: String,
    val message: String,
    val data: T?
)

data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val profileImage: String?
)

data class TokenDto(
    val accessToken: String,
    val refreshToken: String
)