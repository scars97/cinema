package com.example.infrastructure.`in`.dto

import com.example.application.dto.ReservationInfo

data class CreateReservationRequest(
    val userId: Long,
    val scheduleId: Long,
    val seatIds: List<Long> = listOf()
) {

    fun toInfo(): ReservationInfo {
        return ReservationInfo(
            userId = this.userId,
            scheduleId = this.scheduleId,
            seatIds = this.seatIds
        )
    }

}
