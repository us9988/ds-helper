package com.dshelper.app.presentation.help

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dshelper.app.data.api.dto.PersonalReservationRequest
import com.dshelper.app.domain.usecase.reservation.GetPreReservedTimesUseCase
import com.dshelper.app.domain.usecase.reservation.PostPersonalReservationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HelpFormViewModel @Inject constructor(
    private val getPreReservedTimesUseCase: GetPreReservedTimesUseCase,
    private val postPersonalReservationUseCase: PostPersonalReservationUseCase

) : ViewModel() {

    private val _uiState = MutableStateFlow(
        run {
            val firstAvailable = findFirstAvailableDate()
            HelpFormUiState(
                selectedYear = firstAvailable.year,
                selectedMonth = firstAvailable.monthValue,
                selectedDay = firstAvailable.dayOfMonth
            )
        }
    )
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<HelpFormSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        loadReservedTimes()
    }

    fun onEvent(event: HelpFormEvent) {
        when (event) {
            is HelpFormEvent.OnVisitorTypeChange -> {
                _uiState.update { it.copy(visitorType = event.type) }
            }
            is HelpFormEvent.OnPrevMonthClick -> {
                _uiState.update {
                    val prevMonth = if (it.selectedMonth == 1) 12 else it.selectedMonth - 1
                    val prevYear = if (it.selectedMonth == 1) it.selectedYear - 1 else it.selectedYear
                    it.copy(selectedMonth = prevMonth, selectedYear = prevYear, selectedDay = null)
                }
            }
            is HelpFormEvent.OnNextMonthClick -> {
                _uiState.update {
                    val nextMonth = if (it.selectedMonth == 12) 1 else it.selectedMonth + 1
                    val nextYear = if (it.selectedMonth == 12) it.selectedYear + 1 else it.selectedYear
                    it.copy(selectedMonth = nextMonth, selectedYear = nextYear, selectedDay = null)
                }
            }
            is HelpFormEvent.OnDaySelect -> {
                _uiState.update {
                    it.copy(
                        selectedDay = event.day,
                        startTime = null,
                        endTime = null,     // 날짜 바뀌면 시간 초기화
                        reservedTimes = emptyList()
                    )
                }
                loadReservedTimes()  // ✅ 날짜 선택 시 API 호출
            }
            is HelpFormEvent.OnTimeSelect -> {
                val state = _uiState.value
                when {
                    state.startTime == null -> handleFirstTimeSelect(event.time)
                    state.startTime == event.time -> {
                        _uiState.update { it.copy(startTime = null, endTime = null) }
                    }
                    event.time < state.startTime -> handleFirstTimeSelect(event.time)
                    isExceedMaxTime(state.startTime, event.time) -> {
                        viewModelScope.launch {
                            _sideEffect.emit(HelpFormSideEffect.ShowSnackbar("예약가능 시간은 최대 3시간이에요."))
                        }
                    }
                    state.endTime == null -> {
                        _uiState.update { it.copy(endTime = event.time) }
                    }
                    else -> handleFirstTimeSelect(event.time)
                }
            }
            is HelpFormEvent.OnNameChange -> {
                _uiState.update { it.copy(name = event.name) }
            }
            is HelpFormEvent.OnPhoneChange -> {
                _uiState.update { it.copy(phone = event.phone) }
            }
            is HelpFormEvent.OnAddressChange -> {
                _uiState.update { it.copy(address = event.address) }
            }
            is HelpFormEvent.OnDetailAddressChange -> {
                _uiState.update { it.copy(detailAddress = event.address) }
            }
            is HelpFormEvent.OnHelpCountIncrease -> {
                _uiState.update { it.copy(helpCount = it.helpCount + 1) }
            }
            is HelpFormEvent.OnHelpCountDecrease -> {
                if (_uiState.value.helpCount > 1) {
                    _uiState.update { it.copy(helpCount = it.helpCount - 1) }
                }
            }
            is HelpFormEvent.OnHelpContentChange -> {
                _uiState.update { it.copy(helpContent = event.content) }
            }
            is HelpFormEvent.OnGenderChange -> {
                _uiState.update { it.copy(gender = event.gender) }
            }
            is HelpFormEvent.OnSpecialNoteChange -> {
                _uiState.update { it.copy(specialNote = event.note) }
            }
            is HelpFormEvent.OnReserveClick -> {
                viewModelScope.launch {
                    _sideEffect.emit(HelpFormSideEffect.NavigateToComplete)
                }
            }
            is HelpFormEvent.OnBackClick -> {
                viewModelScope.launch {
                    _sideEffect.emit(HelpFormSideEffect.NavigateBack)
                }
            }
        }
    }

    private fun findFirstAvailableDate(): LocalDate {
        var date = LocalDate.now()
        while (date.dayOfWeek != DayOfWeek.SUNDAY) {
            date = date.plusDays(1)
        }
        return date
    }

    private fun loadReservedTimes() {
        Log.d("HelpFormViewModel", "loadReservedTimes called")
        val state = _uiState.value
        val day = state.selectedDay ?: return
        val date = String.format("%d-%02d-%02d", state.selectedYear, state.selectedMonth, day)

        viewModelScope.launch {
            _uiState.update { it.copy(isTimesLoading = true) }
            getPreReservedTimesUseCase(date)
                .onSuccess { times ->
                    _uiState.update { it.copy(reservedTimes = times) }  // ✅
                }
                .onFailure { error ->
                    _sideEffect.emit(HelpFormSideEffect.ShowSnackbar(error.message ?: "시간 조회 실패"))
                }
            _uiState.update { it.copy(isTimesLoading = false) }
        }
    }

    private fun handleFirstTimeSelect(time: String) {
        if (time == "17:00") {
            viewModelScope.launch {
                _sideEffect.emit(HelpFormSideEffect.ShowSnackbar("시작 시간은 오후 5시가 안 돼요."))
            }
        } else {
            _uiState.update { it.copy(startTime = time, endTime = null) }
        }
    }

    // 최대 3시간 초과 여부 체크
    private fun isExceedMaxTime(startTime: String, selectedTime: String): Boolean {
        val startMinutes = timeToMinutes(startTime)
        val selectedMinutes = timeToMinutes(selectedTime)
        return selectedMinutes - startMinutes > 180  // 3시간 = 180분
    }

    private fun timeToMinutes(time: String): Int {
        val (hour, minute) = time.split(":").map { it.toInt() }
        return hour * 60 + minute
    }

    private fun reserve() {
        val state = _uiState.value
        val day = state.selectedDay ?: return
        val visitDate = String.format(
            "%d-%02d-%02d",
            state.selectedYear,
            state.selectedMonth,
            day
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            postPersonalReservationUseCase(
                PersonalReservationRequest(
                    name = state.name,
                    phoneNumber = state.phone,
                    visitDate = visitDate,
                    startTime = state.startTime ?: "",
                    endTime = state.endTime ?: "",
                    address = "${state.address} ${state.detailAddress}".trim(),  // 주소 + 상세주소 합침
                    requirement = state.helpContent,
                    recipientGenderType = state.gender,
                    recipientNumber = state.helpCount,
                    note = state.specialNote
                )
            ).onSuccess {
                _sideEffect.emit(HelpFormSideEffect.NavigateToComplete)
            }.onFailure { error ->
                _sideEffect.emit(
                    HelpFormSideEffect.ShowSnackbar(error.message ?: "예약 실패")
                )
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
