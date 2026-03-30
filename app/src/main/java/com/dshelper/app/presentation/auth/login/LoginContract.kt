package com.dshelper.app.presentation.auth.login

import com.dshelper.app.domain.model.LoginType

data class LoginUiState(
    val isLoading: Boolean = false
)

sealed interface LoginEvent {
    data class OnSocialLoginClick(val type: LoginType) : LoginEvent
    data class OnSocialLoginSuccess(val type: LoginType, val accessToken: String) : LoginEvent
    data class OnSocialLoginFailure(val type: LoginType, val error: Throwable) : LoginEvent
    data object OnOrganizationLoginClick : LoginEvent
}

sealed interface LoginSideEffect {
    data class ShowSnackbar(val message: String) : LoginSideEffect
    object NavigateToHome : LoginSideEffect
    data class RequestSocialLogin(val type: LoginType) : LoginSideEffect
}
