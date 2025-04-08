package com.example.business.reservation.domain

import com.example.business.seat.domain.Seat
import com.example.common.exception.BusinessException
import com.example.common.exception.ErrorCode.*

data class Reservation(
    val reservationId: Long,
    val status: ReservationStatus,
    val userId: Long
) {

    constructor(status: ReservationStatus, userId: Long):
            this(0, status, userId)

    companion object {
        fun of(userId: Long): Reservation {
            return Reservation(
                ReservationStatus.DONE,
                userId
            )
        }

        fun checkExceedLimit(count: Int) {
            val seatLimit = 5
            if (seatLimit < count) {
                throw BusinessException(SEAT_EXCEED_LIMIT)
            }
        }

        fun checkContinuousSeats(seats: List<Seat>) {
            if (seats.size == 1) return

            // 행(A, B, C...) 기준 그룹화
            val groupedSeats = seats.map { it.seatNumber }.groupBy { it.first() }

            groupedSeats.values.forEach { seatList ->
                // 단일 좌석이면 실패
                if (seatList.size == 1) {
                    throw BusinessException(NOT_SAME_ROW)
                }

                // 모든 좌석이 연속된 경우만 성공
                val numbers = seatList.map { it.drop(1).toInt() }
                if (!numbers.zipWithNext().all { (a, b) -> b - a == 1 }) {
                    throw BusinessException(NOT_CONTINUOUS_SEAT)
                }
            }
        }
    }

}
