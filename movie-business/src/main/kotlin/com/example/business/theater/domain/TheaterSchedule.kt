package com.example.business.theater.domain

import java.time.LocalDate

data class TheaterSchedule (
    val scheduleId: Long,
    val movieId: Long,
    val theaterId: Long,
    val screeningDate: LocalDate,
    val startTime: String,
    val endTime: String
)