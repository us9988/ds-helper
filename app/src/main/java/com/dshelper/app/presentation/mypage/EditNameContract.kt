package com.dshelper.app.presentation.mypage

data class EditNameUiState(
    val name: String = "",
    val isLoading: Boolean = false
)

sealed interface EditNameEvent {
    data class OnNameChange(val name: String) : EditNameEvent
    data object OnConfirmClick : EditNameEvent
}

sealed interface EditNameSideEffect {
    data object NavigateBack : EditNameSideEffect
    data class ShowSnackbar(val message: String) : EditNameSideEffect
}
