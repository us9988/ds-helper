package com.dshelper.app.domain.usecase.reservation

import com.dshelper.app.domain.repository.ReservationRepository
import javax.inject.Inject

class GetPreReservedTimesUseCase @Inject constructor(
    private val reservationRepository: ReservationRepository
) {
    suspend operator fun invoke(date: String): Result<List<String>> {
        return reservationRepository.getPreReservedTimes(date)
    }
}
