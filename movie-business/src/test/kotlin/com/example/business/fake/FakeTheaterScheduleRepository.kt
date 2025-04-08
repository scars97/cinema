package com.example.business.fake

import com.example.business.theater.domain.TheaterSchedule
import com.example.business.theater.repository.TheaterScheduleRepository
import java.time.LocalDate
import java.time.LocalTime

class FakeTheaterScheduleRepository: TheaterScheduleRepository {

    override fun getScheduleBy(movieIds: List<Long>): List<TheaterSchedule> {
        return listOf(
            TheaterSchedule(
                scheduleId = 1L,
                movieId = 1L,
                theaterId = 1L,
                screeningDate = LocalDate.now(),
                startTime = LocalTime.of(14, 0),
                endTime = LocalTime.of(16, 0)
            )
        )
    }

    override fun existsBy(scheduleId: Long): Boolean {
        return true
    }

}