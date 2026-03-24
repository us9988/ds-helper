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
    ): Result<Unit> {  // ← 반환은 Result<Unit>
        return runCatching {
            val response = reservationApi.postPersonalReservation(request)
            // ↑ API에서 ReservationResponse 받아서
            if (!response.success) {
                throw Exception(response.message)  // 실패면 예외 던짐
            }
            // 성공이면 Unit 반환 (runCatching이 Result<Unit>으로 감쌈)
        }
    }

}