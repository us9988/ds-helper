package com.dshelper.app.data.api.dto

data class ApiResponse<T>(
    val success: Boolean,
    val code: CodeDto,
    val message: String,
    val data: T?
)

data class CodeDto(
    val message: String,
    val code: String,
    val httpStatus: String
)

data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val profileImage: String?
)