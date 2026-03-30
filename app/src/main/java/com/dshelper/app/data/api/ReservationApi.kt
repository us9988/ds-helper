package com.dshelper.app.data.api

import com.dshelper.app.data.api.dto.PersonalReservationRequest
import com.dshelper.app.data.api.dto.ReservationResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ReservationApi {

    /**
     * @param date "YYYY-mm-dd"
     */
    @GET("reservations/pre-reserved")
    suspend fun getPreReservedTimes(
        @Query("date") date: String
    ): List<String>

    @POST("personal-reservations")
    suspend fun postPersonalReservation(
        @Body request: PersonalReservationRequest
    ): ReservationResponse

}
