package com.dshelper.app.domain.usecase.reservation

import com.dshelper.app.data.api.dto.PersonalReservationRequest
import com.dshelper.app.domain.repository.ReservationRepository
import javax.inject.Inject

class PostPersonalReservationUseCase @Inject constructor(
    private val reservationRepository: ReservationRepository
) {
    suspend operator fun invoke(request: PersonalReservationRequest): Result<Unit> {
        return reservationRepository.postPersonalReservation(request)
    }
}
