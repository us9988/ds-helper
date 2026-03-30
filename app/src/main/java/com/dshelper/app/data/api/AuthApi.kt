package com.dshelper.app.data.api

import com.dshelper.app.data.api.dto.AuthResponse
import com.dshelper.app.data.api.dto.SocialLoginRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/v1/mobile/oauth/kakao/login")
    suspend fun loginWithKakao(
        @Body request: SocialLoginRequest
    ): AuthResponse

    @POST("api/v1/mobile/oauth/naver/login")
    suspend fun loginWithNaver(
        @Body request: SocialLoginRequest
    ): AuthResponse

    @POST("api/v1/mobile/oauth/google/login")
    suspend fun loginWithGoogle(
        @Body request: SocialLoginRequest
    ): AuthResponse

}
