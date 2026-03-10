package com.dshelper.app.domain.usecase.auth

import com.dshelper.app.domain.model.LoginType
import com.dshelper.app.domain.model.User
import com.dshelper.app.domain.repository.AuthRepository
import jakarta.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(type: LoginType, accessToken: String): Result<User> {
        return when (type) {
            LoginType.KAKAO -> authRepository.loginWithKakao(accessToken)
            LoginType.NAVER -> authRepository.loginWithNaver(accessToken)
            LoginType.GOOGLE -> authRepository.loginWithGoogle(accessToken)
            else -> Result.failure(Exception("지원하지 않는 로그인 방식"))
        }
    }
}
