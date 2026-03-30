package com.dshelper.app.presentation.help

import com.dshelper.app.R


data class NoticeItem(
    val stringRes: Int,
    val checked: Boolean = false
)

data class HelpNoticeUiState(
    val noticeItems: List<NoticeItem> = listOf(
        NoticeItem(R.string.help_notice_content_1),
        NoticeItem(R.string.help_notice_content_2),
        NoticeItem(R.string.help_notice_content_3),
        NoticeItem(R.string.help_notice_content_4),
    )
) {
    val isAllChecked: Boolean get() = noticeItems.all { it.checked }
}

sealed interface HelpNoticeEvent {
    data class OnCheckChange(val index: Int) : HelpNoticeEvent
    data object OnConfirmClick : HelpNoticeEvent
    data object OnBackClick : HelpNoticeEvent
}

sealed interface HelpNoticeSideEffect {
    data object NavigateToForm : HelpNoticeSideEffect
    data object NavigateBack : HelpNoticeSideEffect
}
