package com.dshelper.app.domain.repository

import com.dshelper.app.domain.model.User

interface AuthRepository {
    suspend fun loginWithKakao(accessToken: String): Result<User>
    suspend fun loginWithNaver(accessToken: String): Result<User>
    suspend fun loginWithGoogle(accessToken: String): Result<User>
}