package com.dshelper.app.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditNameViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(EditNameUiState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<EditNameSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun onEvent(event: EditNameEvent) {
        when (event) {
            is EditNameEvent.OnNameChange -> {
                _uiState.update { it.copy(name = event.name) }
            }
            is EditNameEvent.OnConfirmClick -> {
                viewModelScope.launch {
                    // 추후 API 연동
                    _sideEffect.emit(EditNameSideEffect.NavigateBack)
                }
            }
        }
    }
}
