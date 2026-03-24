package com.dshelper.app.domain.repository

import com.dshelper.app.data.api.dto.PersonalReservationRequest
import com.dshelper.app.data.api.dto.ReservationResponse

interface ReservationRepository {
    suspend fun getPreReservedTimes(date: String): Result<List<String>>
    suspend fun postPersonalReservation(request: PersonalReservationRequest): Result<Unit>
}
