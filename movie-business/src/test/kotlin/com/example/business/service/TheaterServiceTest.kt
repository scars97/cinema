package com.example.business.service

import com.example.business.fake.FakeTheaterRepository
import com.example.business.theater.service.TheaterService
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TheaterServiceTest {

    private lateinit var sut: TheaterService

    @BeforeEach
    fun setUp() {
        sut = TheaterService(FakeTheaterRepository())
    }

    @DisplayName("특정 상영관 목록이 반환된다.")
    @Test
    fun getTheaters() {
        // given
        val theaterIds = setOf(1L, 2L)

        // when
        val result = sut.getTheaters(theaterIds)

        // then
        assertThat(result).hasSize(2)
            .extracting("theaterId", "name")
            .containsExactly(
                tuple(1L, "A관"),
                tuple(2L, "B관")
            )
    }

}