package com.dshelper.app.presentation.common

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dshelper.app.data.local.TokenDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val tokenDataStore: TokenDataStore
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    init {
        viewModelScope.launch {
            tokenDataStore.accessToken.collect { token ->
                Log.d("LOGIN", "UserViewModel token 감지: $token")
                _isLoggedIn.value = !token.isNullOrEmpty()
                Log.d("LOGIN", "isLoggedIn: ${_isLoggedIn.value}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            tokenDataStore.clearTokens()
        }
    }
}