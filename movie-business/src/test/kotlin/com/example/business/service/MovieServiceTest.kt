package com.example.business.service

import com.example.business.fake.FakeMovieRepository
import com.example.business.movie.service.MovieService
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import java.time.LocalDate
import kotlin.test.Test

class MovieServiceTest {

    private lateinit var sut: MovieService

    @BeforeEach
    fun setUp() {
        sut = MovieService(FakeMovieRepository())
    }

    @DisplayName("현재 날짜를 기준으로 개봉일이 지난 영화 목록을 조회한다.")
    @Test
    fun getAvailableMovies() {
        // given
        val title: String? = null
        val genre: String? = null
        val now = LocalDate.now()

        // when
        val result = sut.getAvailableMovies(title, genre)

        // then
        assertThat(result).hasSize(1)
            .extracting("movieId", "title", "releaseDate")
            .containsExactly(
                tuple(1L, "영화 A", now.minusDays(1))
            )
    }

}