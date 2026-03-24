package com.dshelper.app.data.api

import com.dshelper.app.data.api.dto.AuthResponse
import com.dshelper.app.data.api.dto.KakaoLoginRequest
import com.dshelper.app.data.api.dto.TokenDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/v1/mobile/oauth/kakao/login")
    suspend fun loginWithKakao(
        @Body request: KakaoLoginRequest
    ): AuthResponse<TokenDto>
}