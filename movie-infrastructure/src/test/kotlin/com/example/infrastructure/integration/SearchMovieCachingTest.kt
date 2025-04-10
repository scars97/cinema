package com.example.infrastructure.integration

import com.example.application.usecase.MovieUseCase
import com.example.business.movie.domain.Movie
import com.example.business.movie.service.MovieService
import com.example.business.theater.domain.Theater
import com.example.business.theater.domain.TheaterSchedule
import com.example.business.theater.service.TheaterScheduleService
import com.example.business.theater.service.TheaterService
import com.example.infrastructure.config.IntegrationTestSupport
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean
import java.time.LocalDate
import java.time.LocalTime

class SearchMovieCachingTest: IntegrationTestSupport() {

    @Autowired
    private lateinit var sut: MovieUseCase
    
    @MockitoSpyBean
    private lateinit var movieService: MovieService
    
    @MockitoBean
    private lateinit var theaterService: TheaterService
    
    @MockitoBean
    private lateinit var scheduleService: TheaterScheduleService

    @DisplayName("영화 목록 조회 시, 캐시가 동작하고 로직은 한 번만 호출되어야 한다.")
    @Test
    fun whenRetrievingMovies_thenCacheIsTriggeredAndLogicShouldBeOnlyCalledOnce() {
        // given
        val title: String? = null
        val genre: String? = null
        behaviors(title, genre)

        // when
        val firstCall = sut.getAvailableMovies(title, genre)
        val secondCall = sut.getAvailableMovies(title, genre)

        // then
        assertThat(firstCall).isEqualTo(secondCall)
        verify(movieService, times(1)).getAvailableMovies(title, genre)
    }

    @DisplayName("title, genre 둘 중 하나라도 값이 있는 경우 반드시 로직이 실행되어야 한다.")
    @Test
    fun whenAnyOfTheValuesArePresent_thenLogicMustBeExecuted() {
        // given
        val title: String? = null
        val genre = "액션"
        behaviors(title, genre)

        // when
        val firstCall = sut.getAvailableMovies(title, genre)
        val secondCall = sut.getAvailableMovies(title, genre)

        // then
        assertThat(firstCall).isEqualTo(secondCall)
        verify(movieService, times(2)).getAvailableMovies(title, genre)
    }

    private fun behaviors(title: String?, genre: String?) {
        val movie = Movie(1L, "영화 A", "test.png", LocalDate.now(), 120, "액션", "전체 이용가")
        whenever(movieService.getAvailableMovies(title, genre)).thenReturn(listOf(movie))

        val theater = Theater(1L, "A관")
        whenever(theaterService.getTheaters(any())).thenReturn(listOf(theater))

        val schedule = TheaterSchedule(1L, 1L, 1L, LocalDate.now(), LocalTime.now(), LocalTime.now().plusHours(2))
        whenever(scheduleService.getSchedules(any())).thenReturn(listOf(schedule))
    }

}