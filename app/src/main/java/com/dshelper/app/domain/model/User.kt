package com.dshelper.app.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val profileImage: String? = null,
    val loginType: LoginType
)

enum class LoginType {
    KAKAO, NAVER, GOOGLE
}
