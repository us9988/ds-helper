package com.dshelper.app.presentation.auth.login

import android.app.Activity
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback

fun handleKakaoLogin(
    context: Context,
    onSuccess: (String) -> Unit,
    onFailure: (Throwable) -> Unit,
    onCancel: () -> Unit
) {
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        when {
            error != null -> onFailure(error)
            token != null -> onSuccess(token.accessToken)
        }
    }

    if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
        UserApiClient.instance.loginWithKakaoTalk(
            context as Activity,
            callback = { token, error ->
                if (error != null) {
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        onCancel()
                    } else {
                        UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                    }
                } else if (token != null) {
                    onSuccess(token.accessToken)
                }
            }
        )
    } else {
        UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
    }
}

fun handleNaverLogin(
    context: Context,
    onSuccess: (String) -> Unit,
    onFailure: (Throwable) -> Unit
) {
    NaverIdLoginSDK.authenticate(
        context,
        object : OAuthLoginCallback {
            override fun onSuccess() {
                val token = NaverIdLoginSDK.getAccessToken()
                if (token != null) onSuccess(token)
                else onFailure(Exception("네이버 토큰을 가져올 수 없습니다."))
            }

            override fun onFailure(httpStatus: Int, message: String) {
                onFailure(Exception(message))
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(Exception(message))
            }
        }
    )
}

suspend fun handleGoogleLogin(
    context: Context,
    webClientId: String,
    onSuccess: (String) -> Unit,
    onFailure: (Throwable) -> Unit
) {
    val credentialManager = CredentialManager.create(context)
    val googleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(webClientId)
        .build()
    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    try {
        val result = credentialManager.getCredential(
            context = context as Activity,
            request = request
        )
        val credential = result.credential
        when (credential.type) {

            GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
                onSuccess(googleCredential.idToken)
            }
            else -> onFailure(Exception("구글 로그인 실패"))
        }
    } catch (e: GetCredentialException) {
        onFailure(e)
    }
}
