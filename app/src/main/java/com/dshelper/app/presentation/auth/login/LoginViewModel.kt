package com.dshelper.app.presentation.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dshelper.app.data.local.TokenDataStore
import com.dshelper.app.domain.model.LoginType
import com.dshelper.app.domain.usecase.auth.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val tokenDataStore: TokenDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<LoginSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnSocialLoginClick -> {
                viewModelScope.launch {
                    _sideEffect.emit(LoginSideEffect.RequestSocialLogin(event.type))
                }
            }
            is LoginEvent.OnSocialLoginSuccess -> {
                Log.d("LOGIN", "OnSocialLoginSuccess 받음: type=${event.type}, token=${event.accessToken}")
                login(event.type, event.accessToken)
            }
            is LoginEvent.OnSocialLoginFailure -> {
                viewModelScope.launch {
                    _sideEffect.emit(
                        LoginSideEffect.ShowSnackbar(event.error.message ?: "로그인 실패")
                    )
                }
            }
            is LoginEvent.OnOrganizationLoginClick -> {
//                viewModelScope.launch {
//                    _sideEffect.emit(LoginSideEffect.ShowSnackbar("기관 로그인은 추후 예정입니다."))
//                }
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true) }
                    // 테스트용 임시 로그인
                    tokenDataStore.saveTokens("test_token","test_token")
                    _sideEffect.emit(LoginSideEffect.NavigateToHome)
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    private fun login(type: LoginType, accessToken: String) {
        viewModelScope.launch {
            Log.d("LOGIN", "login() 호출됨")
            _uiState.update { it.copy(isLoading = true) }
            loginUseCase(type, accessToken)
                .onSuccess {
                    Log.d("LOGIN", "로그인 성공 → NavigateToHome emit")
                    _sideEffect.emit(LoginSideEffect.NavigateToHome)
                }
                .onFailure { error ->
                    Log.e("LOGIN", "로그인 실패: ${error.message}")
                    _sideEffect.emit(LoginSideEffect.ShowSnackbar(error.message ?: "로그인 실패"))
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

}