package com.dshelper.app.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dshelper.app.R
import com.dshelper.app.presentation.common.DsButton
import com.dshelper.app.presentation.theme.BorderDefault
import com.dshelper.app.presentation.theme.Gray20
import com.dshelper.app.presentation.theme.TextPrimary

@Composable
fun HomeScreen(
    isLoggedIn: Boolean,
    onLoginClick: () -> Unit,
    onRequestHelpClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Gray20)
    ) {
        LogoHeader(
            isLoggedIn = isLoggedIn,
            onLoginClick = onLoginClick,
            onNotificationClick = onNotificationClick
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DsButton(
                text = stringResource(R.string.request_help),
                onClick = onRequestHelpClick,
                filled = true
            )
            DsButton(
                text = stringResource(R.string.find_trashcan),
                onClick = { },
                filled = false
            )
            DsButton(
                text = stringResource(R.string.benefits),
                onClick = { },
                filled = false
            )
        }
    }
}

@Composable
private fun LogoHeader(
    isLoggedIn: Boolean,
    onLoginClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 왼쪽 — 로고 이미지
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = stringResource(R.string.content_description_logo),
            modifier = Modifier.height(22.dp)
        )
        if (isLoggedIn) {
            IconButton(onClick = onNotificationClick) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = stringResource(R.string.content_description_notification),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            OutlinedButton(
                onClick = onLoginClick,
                modifier = Modifier
                    .height(30.dp),
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, BorderDefault),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = TextPrimary
                ),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp)
            ) {
                Text(stringResource(R.string.login), style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
