package com.example.business.fake

import com.example.business.movie.domain.Movie
import com.example.business.movie.repository.MovieRepository
import java.time.LocalDate

class FakeMovieRepository: MovieRepository {

    override fun getMoviesReleasedUntil(title: String?, genre: String?): List<Movie> {
        return listOf(
            Movie(
                movieId = 1L,
                title = "영화 A",
                thumbnail = "url",
                releaseDate = LocalDate.now().minusDays(1),
                runTime = 120,
                genre = "액션",
                rating = "전체 이용가"
            )
        )
    }

}