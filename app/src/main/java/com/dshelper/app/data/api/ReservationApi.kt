package com.dshelper.app.data.api

import com.dshelper.app.data.api.dto.PersonalReservationRequest
import com.dshelper.app.data.api.dto.ReservationResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ReservationApi {
    @GET("reservations/pre-reserved")
    suspend fun getPreReservedTimes(
        @Query("date") date: String  // 예: "2026-03-22"
    ): List<String>

    @POST("personal-reservations")  // ✅ 추가
    suspend fun postPersonalReservation(
        @Body request: PersonalReservationRequest
    ): ReservationResponse

}
