package com.dshelper.app.data.api.dto

data class PreReservedRequest(
    /**
     *  // yyyy-MM-dd 형식
     */
    val date: String
)

data class ReservationResponse(
    val success: Boolean,
    val code: String,
    val message: String,
    val data: Any?
)