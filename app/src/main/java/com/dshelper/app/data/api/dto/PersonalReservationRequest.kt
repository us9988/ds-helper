package com.dshelper.app.data.api.dto

import com.google.gson.annotations.SerializedName

data class PersonalReservationRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("visitDate")
    val visitDate: String,          // yyyy-MM-dd
    @SerializedName("startTime")
    val startTime: String,          // HH:mm
    @SerializedName("endTime")
    val endTime: String,            // HH:mm
    @SerializedName("address")
    val address: String,
    @SerializedName("requirement")
    val requirement: String,
    @SerializedName("recipientGenderType")
    val recipientGenderType: String, // "남자", "여자", "둘 다 있음"
    @SerializedName("recipientNumber")
    val recipientNumber: Int,
    @SerializedName("note")
    val note: String
)
