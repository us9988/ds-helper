package com.dshelper.app.presentation.help

import java.time.LocalDate
import java.util.Locale

data class HelpFormUiState(
    val isLoading: Boolean = false,
    val visitorType: VisitorType = VisitorType.INDIVIDUAL,
    val selectedYear: Int = LocalDate.now().year,
    val selectedMonth: Int = LocalDate.now().monthValue,
    val selectedDay: Int? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val reservedTimes: List<String> = emptyList(),
    val isTimesLoading: Boolean = false,
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val detailAddress: String = "",
    val helpContent: String = "",
    val gender: String = "MALE",
    val helpCount: Int = 1,
    val specialNote: String = ""
) {

    val maxSelectableTime: String?
        get() {
            val start = startTime ?: return null
            val startMinutes = timeToMinutes(start)
            val maxMinutes = startMinutes + 180
            val maxHour = maxMinutes / 60
            val maxMinute = maxMinutes % 60
            return String.format(Locale.KOREAN, "%02d:%02d", maxHour, maxMinute)
        }

    private fun timeToMinutes(time: String): Int {
        val (hour, minute) = time.split(":").map { it.toInt() }
        return hour * 60 + minute
    }

    val isAllFilled: Boolean
        get() =
            selectedDay != null &&
                    startTime != null &&
                    endTime != null &&
                    name.isNotBlank() &&
                    phone.isNotBlank() &&
                    address.isNotBlank() &&
                    detailAddress.isNotBlank() &&
                    gender.isNotBlank() &&
                    helpContent.isNotBlank()
}

enum class VisitorType(val label: String) {
    INDIVIDUAL("개인"),
    ORGANIZATION("기관")
}

sealed interface HelpFormEvent {
    data class OnVisitorTypeChange(val type: VisitorType) : HelpFormEvent
    data object OnPrevMonthClick : HelpFormEvent
    data object OnNextMonthClick : HelpFormEvent
    data class OnDaySelect(val day: Int) : HelpFormEvent
    data class OnTimeSelect(val time: String) : HelpFormEvent
    data class OnNameChange(val name: String) : HelpFormEvent
    data class OnPhoneChange(val phone: String) : HelpFormEvent
    data class OnAddressChange(val address: String) : HelpFormEvent
    data class OnDetailAddressChange(val address: String) : HelpFormEvent
    data class OnHelpContentChange(val content: String) : HelpFormEvent
    data class OnGenderChange(val gender: String) : HelpFormEvent
    data class OnSpecialNoteChange(val note: String) : HelpFormEvent
    data object OnHelpCountIncrease : HelpFormEvent
    data object OnHelpCountDecrease : HelpFormEvent
    data object OnReserveClick : HelpFormEvent
    data object OnBackClick : HelpFormEvent
}

sealed interface HelpFormSideEffect {
    data object NavigateToComplete : HelpFormSideEffect
    data object NavigateBack : HelpFormSideEffect
    data class ShowSnackbar(val message: String) : HelpFormSideEffect
}
