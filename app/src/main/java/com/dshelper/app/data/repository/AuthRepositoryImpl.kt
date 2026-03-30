package com.dshelper.app.data.repository

import com.dshelper.app.data.api.AuthApi
import com.dshelper.app.data.api.dto.SocialLoginRequest
import com.dshelper.app.data.local.TokenDataStore
import com.dshelper.app.domain.model.LoginType
import com.dshelper.app.domain.model.User
import com.dshelper.app.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenDataStore: TokenDataStore
) : AuthRepository {

    override suspend fun loginWithKakao(accessToken: String): Result<User> {
        return runCatching {
            val response = authApi.loginWithKakao(SocialLoginRequest(LoginType.KAKAO.name, accessToken))
            if (!response.success) {
                throw Exception(response.message)
            }
            tokenDataStore.saveTokens(
                accessToken = response.data.accessToken,
                refreshToken = response.data.refreshToken
            )
            User(id = "", name = "", email = "", loginType = LoginType.KAKAO)
        }
    }

    override suspend fun loginWithNaver(accessToken: String): Result<User> {
        return runCatching {
            val response = authApi.loginWithNaver(
                SocialLoginRequest(LoginType.NAVER.name, accessToken)
            )
            if (!response.success) {
                throw Exception(response.message)
            }
            tokenDataStore.saveTokens(
                accessToken = response.data.accessToken,
                refreshToken = response.data.refreshToken
            )
            User(id = "", name = "", email = "", loginType = LoginType.NAVER)
        }
    }

    override suspend fun loginWithGoogle(accessToken: String): Result<User> {
        return runCatching {
            val response = authApi.loginWithGoogle(
                SocialLoginRequest(LoginType.GOOGLE.name, accessToken)
            )
            if (!response.success) {
                throw Exception(response.message)
            }
            tokenDataStore.saveTokens(
                accessToken = response.data.accessToken,
                refreshToken = response.data.refreshToken
            )
            User(id = "", name = "", email = "", loginType = LoginType.GOOGLE)
        }
    }
}
