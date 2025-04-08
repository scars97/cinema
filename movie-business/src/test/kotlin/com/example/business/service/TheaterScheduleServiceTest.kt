package com.example.business.service

import com.example.business.fake.FakeTheaterScheduleRepository
import com.example.business.movie.domain.Movie
import com.example.business.theater.service.TheaterScheduleService
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class TheaterScheduleServiceTest {

    private lateinit var sut: TheaterScheduleService

    @BeforeEach
    fun setUp() {
        sut = TheaterScheduleService(FakeTheaterScheduleRepository())
    }

    @DisplayName("영화 ID에 해당하는 상영 일정 목록이 반환된다.")
    @Test
    fun getSchedules() {
        // given
        val movie = Movie(
            movieId = 1L,
            title = "영화 A",
            thumbnail = "url",
            releaseDate = LocalDate.now().minusDays(1),
            runTime = 120,
            genre = "액션",
            rating = "전체 이용가"
        )

        // when
        val result = sut.getSchedules(listOf(movie))

        // then
        assertThat(result).hasSize(1)
            .extracting("scheduleId", "movieId", "screeningDate", "startTime")
            .containsExactly(
                tuple(1L, 1L, LocalDate.now(), LocalTime.of(14, 0))
            )
    }

}