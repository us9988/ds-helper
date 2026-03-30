package com.dshelper.app.data.api.dto


data class ReservationResponse(
    val success: Boolean,
    val code: String,
    val message: String,
    val data: Any?
)
