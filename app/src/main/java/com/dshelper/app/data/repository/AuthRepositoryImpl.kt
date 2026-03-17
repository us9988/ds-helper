package com.dshelper.app.data.repository

import android.util.Log
import com.dshelper.app.data.api.AuthApi
import com.dshelper.app.data.api.dto.KakaoLoginRequest
import com.dshelper.app.data.api.dto.UserDto
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
        Log.d("AUTH", "1. loginWithKakao 호출됨")
        Log.d("AUTH", "2. accessToken: $accessToken")
        return runCatching {
            Log.d("AUTH", "3. API 호출 시작")
            val response = authApi.loginWithKakao(KakaoLoginRequest(accessToken = accessToken))
            Log.d("AUTH", "4. API 응답 받음")
            Log.d("AUTH", "5. success: ${response.success}")
            Log.d("AUTH", "6. message: ${response.message}")
            Log.d("AUTH", "7. data: ${response.data}")
            if (!response.success || response.data == null) {
                throw Exception(response.message)
            }
            Log.d("AUTH", "9. 토큰 저장 시작")
            tokenDataStore.saveToken(response.data)
            User(id = "", name = "", email = "", loginType = LoginType.KAKAO)
        }.also { result ->
            result.onFailure {
                when (it) {
                    is retrofit2.HttpException -> {
                        Log.e("AUTH", "HTTP 에러: ${it.code()}")
                        Log.e("AUTH", "HTTP 에러 바디: ${it.response()?.errorBody()?.string()}")
                    }
                    else -> Log.e("AUTH", "기타 에러: ${it.message}")
                }
            }
        }
    }

    override suspend fun loginWithNaver(accessToken: String): Result<User> {
        return runCatching {
            val response = authApi.loginWithKakao(KakaoLoginRequest(accessToken = accessToken))
            if (!response.success || response.data == null) {
                throw Exception(response.message)
            }
            tokenDataStore.saveToken(response.data)
            User(id = "", name = "", email = "", loginType = LoginType.KAKAO)
        }
    }

    override suspend fun loginWithGoogle(accessToken: String): Result<User> {
        return runCatching {
            val response = authApi.loginWithKakao(KakaoLoginRequest(accessToken = accessToken))
            if (!response.success || response.data == null) {
                throw Exception(response.message)
            }
            tokenDataStore.saveToken(response.data)
            User(id = "", name = "", email = "", loginType = LoginType.KAKAO)
        }
    }
}

fun UserDto.toDomain() = User(
    id = id,
    name = name,
    email = email,
    profileImage = profileImage,
    loginType = LoginType.KAKAO
)