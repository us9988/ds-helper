package com.dshelper.app.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DsSnackbarHost(
    hostState: SnackbarHostState
) {
    SnackbarHost(hostState = hostState) { snackbarData ->
        DsSnackbar(snackbarData = snackbarData)
    }
}

@Composable
private fun DsSnackbar(
    snackbarData: SnackbarData
) {
    Snackbar(
        modifier = Modifier.padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        containerColor = Color(0xFF323232),
        contentColor = Color.White,
        action = {
            IconButton(
                onClick = { snackbarData.dismiss() }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "닫기",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    ) {
        Text(
            text = snackbarData.visuals.message,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White
            )
        )
    }
}
