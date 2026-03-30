package com.dshelper.app.presentation.auth.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dshelper.app.BuildConfig
import com.dshelper.app.R
import com.dshelper.app.domain.model.LoginType
import com.dshelper.app.presentation.common.DsSnackbarHost
import com.dshelper.app.presentation.common.DsTopBar
import com.dshelper.app.presentation.common.UserViewModel
import com.dshelper.app.presentation.theme.White
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    userViewModel: UserViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isLoggedIn by userViewModel.isLoggedIn.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) onBackClick()
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is LoginSideEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is LoginSideEffect.NavigateToHome -> {
                    onBackClick()
                }
                is LoginSideEffect.RequestSocialLogin -> {
                    when (effect.type) {
                        LoginType.KAKAO -> handleKakaoLogin(
                            context = context,
                            onSuccess = { token ->
                                viewModel.onEvent(LoginEvent.OnSocialLoginSuccess(LoginType.KAKAO, token))
                            },
                            onFailure = { error ->
                                viewModel.onEvent(LoginEvent.OnSocialLoginFailure(LoginType.KAKAO, error))
                            },
                            onCancel = {}
                        )
                        LoginType.NAVER -> handleNaverLogin(
                            context = context,
                            onSuccess = { token ->
                                viewModel.onEvent(LoginEvent.OnSocialLoginSuccess(LoginType.NAVER, token))
                            },
                            onFailure = { error ->
                                viewModel.onEvent(LoginEvent.OnSocialLoginFailure(LoginType.NAVER, error))
                            }
                        )
                        LoginType.GOOGLE -> scope.launch {
                            handleGoogleLogin(
                                context = context,
                                webClientId = BuildConfig.GOOGLE_WEB_CLIENT_ID,
                                onSuccess = { token ->
                                    viewModel.onEvent(LoginEvent.OnSocialLoginSuccess(LoginType.GOOGLE, token))
                                },
                                onFailure = { error ->
                                    viewModel.onEvent(LoginEvent.OnSocialLoginFailure(LoginType.GOOGLE, error))
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        containerColor = White,
        snackbarHost = { DsSnackbarHost(snackbarHostState) },
        topBar = {
            DsTopBar(
                title = "로그인",
                showBackButton = true,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SocialLoginButton(
                text = "카카오로 로그인",
                backgroundColor = Color(0xFFFEE500),
                textColor = Color.Black,
                iconPainter = painterResource(id = R.drawable.ic_kakao),
                iconSize = 24.dp,
                onClick = { viewModel.onEvent(LoginEvent.OnSocialLoginClick(LoginType.KAKAO)) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            SocialLoginButton(
                text = "네이버로 로그인",
                backgroundColor = Color(0xFF03C75A),
                textColor = Color.White,
                iconPainter = painterResource(id = R.drawable.ic_naver),
                onClick = { viewModel.onEvent(LoginEvent.OnSocialLoginClick(LoginType.NAVER)) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            SocialLoginButton(
                text = "구글로 로그인",
                backgroundColor = Color.White,
                textColor = Color.Black,
                borderColor = Color.LightGray,
                iconPainter = painterResource(id = R.drawable.ic_google),
                iconSize = 28.dp,
                onClick = { viewModel.onEvent(LoginEvent.OnSocialLoginClick(LoginType.GOOGLE)) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            SocialLoginButton(
                text = "기관 로그인",
                backgroundColor = Color(0xFFFF8645),
                textColor = Color.White,
                iconPainter = painterResource(id = R.drawable.ic_institution),
                iconSize = 24.dp,
                onClick = { viewModel.onEvent(LoginEvent.OnOrganizationLoginClick) }
            )
            if (uiState.isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}


@Composable
fun SocialLoginButton(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    borderColor: Color? = null,
    iconPainter: Painter? = null,
    iconSize: Dp = 20.dp,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        border = borderColor?.let { BorderStroke(1.dp, it) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            iconPainter?.let {
                Image(
                    painter = it,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                color = textColor,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = 18.sp
                ),
            )
        }
    }
}
