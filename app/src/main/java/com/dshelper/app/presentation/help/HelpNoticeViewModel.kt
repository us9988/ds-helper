package com.dshelper.app.presentation.help

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
class HelpNoticeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HelpNoticeUiState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<HelpNoticeSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun onEvent(event: HelpNoticeEvent) {
        when (event) {
            is HelpNoticeEvent.OnCheckChange -> {
                val updatedItems = _uiState.value.noticeItems.toMutableList()
                updatedItems[event.index] = updatedItems[event.index].copy(
                    checked = !updatedItems[event.index].checked
                )
                _uiState.update { it.copy(noticeItems = updatedItems) }
            }
            is HelpNoticeEvent.OnConfirmClick -> {
                viewModelScope.launch {
                    _sideEffect.emit(HelpNoticeSideEffect.NavigateToForm)
                }
            }
            is HelpNoticeEvent.OnBackClick -> {
                viewModelScope.launch {
                    _sideEffect.emit(HelpNoticeSideEffect.NavigateBack)
                }
            }
        }
    }
}