package com.dshelper.app.presentation.help

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dshelper.app.R
import com.dshelper.app.presentation.common.DsButton
import com.dshelper.app.presentation.common.DsTopBar
import com.dshelper.app.presentation.theme.BorderDefault
import com.dshelper.app.presentation.theme.Primary50
import com.dshelper.app.presentation.theme.TextPrimary
import com.dshelper.app.presentation.theme.White

@Composable
fun HelpNoticeScreen(
    onNavigateBack: () -> Unit,
    onNavigateToForm: () -> Unit,
    viewModel: HelpNoticeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is HelpNoticeSideEffect.NavigateToForm -> onNavigateToForm()
                is HelpNoticeSideEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    Scaffold(
        containerColor = White,
        topBar = {
            DsTopBar(
                title = stringResource(R.string.request_help),
                showBackButton = true,
                onBackClick = { viewModel.onEvent(HelpNoticeEvent.OnBackClick) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.help_notice_title),
                style = MaterialTheme.typography.titleSmall,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(24.dp))

            uiState.noticeItems.forEachIndexed { index, item ->
                HelpNoticeCheckItem(
                    stringRes = item.stringRes,
                    checked = item.checked,
                    onCheckedChange = {
                        viewModel.onEvent(HelpNoticeEvent.OnCheckChange(index))
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            DsButton(
                text = stringResource(R.string.confirm),
                enabled = uiState.isAllChecked,
                textAlign = TextAlign.Center,
                onClick = { viewModel.onEvent(HelpNoticeEvent.OnConfirmClick) },
            )
        }
    }
}

@Composable
private fun HelpNoticeCheckItem(
    stringRes: Int,
    checked: Boolean,
    onCheckedChange: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange() },
        verticalAlignment = Alignment.Top
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onCheckedChange() },
            modifier = Modifier.size(24.dp),
            colors = CheckboxDefaults.colors(
                checkedColor = Primary50,
                uncheckedColor = BorderDefault
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(stringRes),
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary
        )
    }
}
