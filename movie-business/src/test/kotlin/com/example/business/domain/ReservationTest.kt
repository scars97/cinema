package com.example.business.domain

import com.example.business.reservation.domain.Reservation
import com.example.business.seat.domain.Seat
import com.example.business.seat.domain.SeatStatus
import com.example.common.exception.BusinessException
import com.example.common.exception.ErrorCode.*
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class ReservationTest {

    @DisplayName("요청한 예약 좌석 개수가 5개 초과하면 예외가 발생한다..")
    @Test
    fun exceedLimitThenThrowException() {
        // given
        val invalidCount = 6

        // when // then
        assertThatThrownBy { Reservation.checkExceedLimit(invalidCount) }
            .isInstanceOf(BusinessException::class.java)
            .hasFieldOrPropertyWithValue("errorCode", SEAT_EXCEED_LIMIT)
    }

    @DisplayName("연속된 좌석 검증 테스트 시나리오")
    @TestFactory
    fun isNotContinuousSeatsThenThrowException(): Collection<DynamicTest> {
        // given
        val notSameRow = listOf(
            Seat(1L, "A1", SeatStatus.AVAILABLE, null, 1L),
            Seat(2L, "C2", SeatStatus.AVAILABLE, null, 1L)
        )
        val notContinuous = listOf(
            Seat(1L, "A1", SeatStatus.AVAILABLE, null, 1L),
            Seat(2L, "A2", SeatStatus.AVAILABLE, null, 1L),
            Seat(4L, "A4", SeatStatus.AVAILABLE, null, 1L),
        )

        // when // then
        return listOf(
            DynamicTest.dynamicTest("2개 이상 좌석 예약 시, 같은 행이 아니라면 예외가 발생한다.") {
                assertThatThrownBy { Reservation.checkContinuousSeats(notSameRow) }
                    .isInstanceOf(BusinessException::class.java)
                    .hasFieldOrPropertyWithValue("errorCode", NOT_SAME_ROW)
            },
            DynamicTest.dynamicTest("하나라도 연속된 좌석이 아니라면 예외가 발생한다.") {
                assertThatThrownBy { Reservation.checkContinuousSeats(notContinuous) }
                    .isInstanceOf(BusinessException::class.java)
                    .hasFieldOrPropertyWithValue("errorCode", NOT_CONTINUOUS_SEAT)
            }
        )
    }

}