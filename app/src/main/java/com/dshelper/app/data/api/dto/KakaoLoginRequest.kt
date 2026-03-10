package com.dshelper.app.data.api.dto

import com.google.gson.annotations.SerializedName

data class KakaoLoginRequest(
    @SerializedName("provider")
    val provider: String = "KAKAO",
    @SerializedName("accessToken")
    val accessToken: String
)