package com.dshelper.app.presentation.help

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dshelper.app.R
import com.dshelper.app.presentation.address.AddressSearchActivity
import com.dshelper.app.presentation.common.DsButton
import com.dshelper.app.presentation.common.DsSnackbarHost
import com.dshelper.app.presentation.common.DsTopBar
import com.dshelper.app.presentation.theme.BgBrand
import com.dshelper.app.presentation.theme.BgNeutralSoft
import com.dshelper.app.presentation.theme.Gray20
import com.dshelper.app.presentation.theme.Gray30
import com.dshelper.app.presentation.theme.Gray40
import com.dshelper.app.presentation.theme.Gray60
import com.dshelper.app.presentation.theme.Gray80
import com.dshelper.app.presentation.theme.TextPlaceholder
import com.dshelper.app.presentation.theme.White
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun HelpFormScreen(
    onNavigateBack: () -> Unit,
    onNavigateToComplete: () -> Unit,
    viewModel: HelpFormViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val addressSearchLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val roadAddress = result.data?.getStringExtra(
                AddressSearchActivity.EXTRA_ROAD_ADDRESS
            ) ?: ""
            viewModel.onEvent(HelpFormEvent.OnAddressChange(roadAddress))
        }
    }
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is HelpFormSideEffect.NavigateToComplete -> onNavigateToComplete()
                is HelpFormSideEffect.NavigateBack -> onNavigateBack()
                is HelpFormSideEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(
        containerColor = White,
        snackbarHost = { DsSnackbarHost(snackbarHostState) },
        topBar = {
            DsTopBar(
                title = stringResource(R.string.help_form_top_bar),
                showBackButton = true,
                onBackClick = { viewModel.onEvent(HelpFormEvent.OnBackClick) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 개인 / 기관 선택
            VisitorTypeSelector(
                selectedType = uiState.visitorType,
                onTypeChange = { viewModel.onEvent(HelpFormEvent.OnVisitorTypeChange(it)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 달력
            CalendarSection(
                year = uiState.selectedYear,
                month = uiState.selectedMonth,
                selectedDay = uiState.selectedDay,
                onPrevMonth = { viewModel.onEvent(HelpFormEvent.OnPrevMonthClick) },
                onNextMonth = { viewModel.onEvent(HelpFormEvent.OnNextMonthClick) },
                onDaySelect = { viewModel.onEvent(HelpFormEvent.OnDaySelect(it)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = Color(0xFFE0E0E0)
            )

// TimeSelector 파라미터 수정
            TimeSelector(
                startTime = uiState.startTime,      // ✅
                endTime = uiState.endTime,          // ✅
                maxSelectableTime = uiState.maxSelectableTime,
                reservedTimes = uiState.reservedTimes,
                isLoading = uiState.isTimesLoading,
                onTimeSelect = { viewModel.onEvent(HelpFormEvent.OnTimeSelect(it)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 이름
            HelpFormTextField(
                label = "이름",
                value = uiState.name,
                onValueChange = { viewModel.onEvent(HelpFormEvent.OnNameChange(it)) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 전화번호
            HelpFormTextField(
                label = "전화번호",
                value = uiState.phone,
                onValueChange = { viewModel.onEvent(HelpFormEvent.OnPhoneChange(it)) },
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 방문주소
            HelpFormTextField(
                label = "방문주소",
                value = uiState.address,
                onValueChange = { viewModel.onEvent(HelpFormEvent.OnAddressChange(it)) },
                placeholder = "주소 입력",
                readOnly = true,  // 직접 입력 불가, 검색으로만 ✅
                onClick = {
                    Log.d("AddressSearchActivity", "onClick called")
                    addressSearchLauncher.launch(
                        Intent(context, AddressSearchActivity::class.java)
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "위치",
                        tint = Color(0xFFAAAAAA)
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 상세주소
            HelpFormTextField(
                label = "상세주소",
                value = uiState.detailAddress,
                onValueChange = { viewModel.onEvent(HelpFormEvent.OnDetailAddressChange(it)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 도움 요청 내용 ✅
            HelpFormTextField(
                label = "도움 요청 내용",
                value = uiState.helpContent,
                onValueChange = { viewModel.onEvent(HelpFormEvent.OnHelpContentChange(it)) },
                placeholder = "저희 어머니 병원 동행 해주세요."
            )

            Spacer(modifier = Modifier.height(24.dp))

            GenderSelector(
                selectedGender = uiState.gender,
                onGenderChange = { viewModel.onEvent(HelpFormEvent.OnGenderChange(it)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 도움받는 사람수
            HelpCountSelector(
                count = uiState.helpCount,
                onIncrease = { viewModel.onEvent(HelpFormEvent.OnHelpCountIncrease) },
                onDecrease = { viewModel.onEvent(HelpFormEvent.OnHelpCountDecrease) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            HelpFormTextField(
                label = "특이사항",
                value = uiState.specialNote,
                onValueChange = { viewModel.onEvent(HelpFormEvent.OnSpecialNoteChange(it)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            DsButton(
                text = "예약하기",
                enabled = uiState.isAllFilled,
                onClick = { viewModel.onEvent(HelpFormEvent.OnReserveClick) },
                textAlign = TextAlign.Center
            )

            if (uiState.isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}

// 개인 / 기관 선택
@Composable
private fun VisitorTypeSelector(
    selectedType: VisitorType,
    onTypeChange: (VisitorType) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        VisitorType.entries.forEach { type ->
            // 기관 선택 추후 추가 예정
            val isEnabled = type == VisitorType.INDIVIDUAL
            Box(
                modifier = Modifier
                    .weight(1f)
                    .widthIn(max = 160.dp)
                    .aspectRatio(1f)
                    .then(
                        if (isEnabled) Modifier.clickable { onTypeChange(type) }
                        else Modifier
                    )
            ) {
                Image(
                    painter = painterResource(
                        if (type == VisitorType.INDIVIDUAL) R.drawable.ic_individual
                        else R.drawable.ic_organization
                    ),
                    contentDescription = type.label,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                )
            }
        }
    }
}

// 달력
@Composable
private fun CalendarSection(
    year: Int,
    month: Int,
    selectedDay: Int?,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onDaySelect: (Int) -> Unit
) {
    val daysInMonth = YearMonth.of(year, month).lengthOfMonth()
    val firstDayOfWeek = LocalDate.of(year, month, 1).dayOfWeek.value % 7

    Column {

        Text(
            text = "날짜 및 시간",
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        // 월 헤더
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onPrevMonth
            ) {
                Icon(Icons.Default.ChevronLeft, contentDescription = "이전 달", tint = Gray60)
            }
            Text(
                text = String.format("%d.%02d", year, month),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            IconButton(
                onClick = onNextMonth
            ) {
                Icon(Icons.Default.ChevronRight, contentDescription = "다음 달", tint = Gray60)
            }
        }

        // 요일 헤더
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("일", "월", "화", "수", "목", "금", "토").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 달력 날짜
        val today = LocalDate.now()
        val totalCells = firstDayOfWeek + daysInMonth
        val rows = (totalCells + 6) / 7

        for (row in 0 until rows) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (col in 0 until 7) {
                    val cellIndex = row * 7 + col
                    val day = cellIndex - firstDayOfWeek + 1
                    val isValid = day in 1..daysInMonth
                    // 해당 날짜 LocalDate 생성
                    val currentDate = if (isValid) LocalDate.of(year, month, day) else null

                    val isSunday = col == 0
                    val isPast = currentDate?.isBefore(today) == true  // 오늘 이전 ✅
                    val isEnabled = isValid && isSunday && !isPast    // 주말 + 오늘 이후만 ✅
                    val isSelected = isEnabled && day == selectedDay

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) BgBrand else Color.Transparent)
                            .then(
                                if (isEnabled) Modifier.clickable { onDaySelect(day) }
                                else Modifier
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isValid) {
                            Text(
                                text = day.toString(),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = when {
                                        isSelected -> Color.White
                                        !isEnabled -> Gray40
                                        else -> Color.Black
                                    },
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TimeSelector(
    startTime: String?,
    endTime: String?,
    maxSelectableTime: String?,
    reservedTimes: List<String>,
    isLoading: Boolean,
    onTimeSelect: (String) -> Unit
) {
    val times = listOf(
        "10:00", "10:30", "11:00", "11:30",
        "12:00", "12:30", "13:00", "13:30",
        "14:00", "14:30", "15:00", "15:30",
        "16:00", "16:30", "17:00"
    )

    val rows = listOf(
        times.slice(0..3),
        times.slice(4..7),
        times.slice(8..11),
        times.slice(12..14)
    )

    Column {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            rows.forEach { rowTimes ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    rowTimes.forEach { time ->
                        val isReserved = reservedTimes.contains(time)

                        // 최대 시간 초과 여부
                        val isExceedMax = startTime != null &&
                                endTime == null &&
                                maxSelectableTime != null &&
                                time > maxSelectableTime

                        // 17:00 시작 불가 (startTime 미선택 상태에서만)
                        val is17Disabled = startTime == null && time == "17:00"

                        val isStart = time == startTime
                        val isEnd = time == endTime
                        val isBetween = startTime != null && endTime != null &&
                                time > startTime && time < endTime

                        val isDisabled = isReserved || isExceedMax || is17Disabled

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .clip(
                                    when {
                                        isStart -> RoundedCornerShape(
                                            topStart = 8.dp, bottomStart = 8.dp,
                                            topEnd = 0.dp, bottomEnd = 0.dp
                                        )
                                        isEnd -> RoundedCornerShape(
                                            topStart = 0.dp, bottomStart = 0.dp,
                                            topEnd = 8.dp, bottomEnd = 8.dp
                                        )
                                        isBetween -> RoundedCornerShape(0.dp)
                                        else -> RoundedCornerShape(0.dp)
                                    }
                                )
                                .background(
                                    when {
                                        isStart || isEnd -> BgBrand
                                        isBetween -> BgNeutralSoft
                                        else -> Color.Transparent
                                    }
                                )
                                .clickable { onTimeSelect(time) },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = time,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = when {
                                            isStart || isEnd -> Color.White
                                            isReserved -> Gray40
                                            else -> Color.Black
                                        },
                                        fontWeight = when {
                                            isStart || isEnd -> FontWeight.SemiBold
                                            else -> FontWeight.Medium
                                        }
                                    )
                                )
                                // 높이 유지를 위해 항상 텍스트 존재
                                Text(
                                    text = when {
                                        isStart -> "시작"
                                        isEnd -> "종료"
                                        else -> ""
                                    },
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isStart || isEnd) Color.White
                                        else Color.Transparent,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }

                    // 빈 칸 채우기
                    repeat(4 - rowTimes.size) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

// 텍스트 필드
@Composable
private fun HelpFormTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    readOnly: Boolean = false,         // 추가 ✅
    onClick: (() -> Unit)? = null,     // 추가 ✅
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box {
            OutlinedTextField(
                value = value,
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
                onValueChange = onValueChange,
                placeholder = {
                    if (placeholder.isNotEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
                            color = TextPlaceholder
                        )
                    }
                },
                readOnly = readOnly,  // ✅
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (onClick != null) Modifier.clickable { onClick() }
                        else Modifier
                    ),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color(0xFFAAAAAA)
                ),
                trailingIcon = trailingIcon
            )
            if (onClick != null) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { onClick() }
                )
            }

        }

    }
}

@Composable
private fun HelpCountSelector(
    count: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Column {
        Text(
            text = "도움 받는 사람 수",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // + 버튼
            Text(
                text = "+",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Light,
                    color = Color(0xFF555555)
                ),
                modifier = Modifier.clickable { onIncrease() }
            )

            // 숫자
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            // - 버튼
            Text(
                text = "-",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = if (count > 1) Gray80 else Gray20
                ),
                modifier = Modifier.clickable {
                    if (count > 1) onDecrease()
                }
            )
        }
    }
}

@Composable
private fun GenderSelector(
    selectedGender: String,
    onGenderChange: (String) -> Unit
) {
    data class GenderItem(
        val value: String,
        val label: String,
        val drawableRes: Int
    )

    val genderItems = listOf(
        GenderItem("MALE", "남자", R.drawable.ic_gender_male),
        GenderItem("FEMALE", "여자", R.drawable.ic_gender_female),
        GenderItem("BOTH", "둘 다 있음", R.drawable.ic_gender_both)
    )

    Column {
        Text(
            text = "도움 받는 사람 성별",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            genderItems.forEach { item ->
                val isSelected = selectedGender == item.value

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) Color(0xFF2196F3) else Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onGenderChange(item.value) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(item.drawableRes),
                            contentDescription = item.label,
                            modifier = Modifier.size(48.dp),
                            colorFilter = if (isSelected) {
                                null
                            } else {
                                ColorFilter.tint(Gray30)  // 미선택 시 회색 ✅
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (isSelected) Color(0xFF2196F3) else Color(0xFFAAAAAA),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    }
                }
            }
        }
    }
}