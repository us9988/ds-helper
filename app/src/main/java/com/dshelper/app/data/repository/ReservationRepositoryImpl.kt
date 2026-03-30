package com.dshelper.app.data.repository

import com.dshelper.app.data.api.ReservationApi
import com.dshelper.app.data.api.dto.PersonalReservationRequest
import com.dshelper.app.domain.repository.ReservationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReservationRepositoryImpl @Inject constructor(
    private val reservationApi: ReservationApi
) : ReservationRepository {

    override suspend fun getPreReservedTimes(date: String): Result<List<String>> {
        return runCatching {
            reservationApi.getPreReservedTimes(date)
        }
    }

    override suspend fun postPersonalReservation(
        request: PersonalReservationRequest
    ): Result<Unit> {
        return runCatching {
            val response = reservationApi.postPersonalReservation(request)
            if (!response.success) {
                throw Exception(response.message)
            }
        }
    }

}
