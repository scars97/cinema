package com.example.business.theater.domain

import java.time.LocalDate
import java.time.LocalTime

data class TheaterSchedule (
    val scheduleId: Long,
    val movieId: Long,
    val theaterId: Long,
    val screeningDate: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime
)