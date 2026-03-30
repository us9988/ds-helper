package com.dshelper.app.data.api.dto

data class PersonalReservationRequest(
    val name: String,
    val phoneNumber: String,
    val visitDate: String,
    val startTime: String,
    val endTime: String,
    val address: String,
    val requirement: String,
    val recipientGenderType: String,
    val recipientNumber: Int,
    val note: String
)
