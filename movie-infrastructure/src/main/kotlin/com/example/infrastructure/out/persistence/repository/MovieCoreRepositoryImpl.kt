package com.example.infrastructure.out.persistence.repository

import com.example.business.movie.domain.Movie
import com.example.business.movie.repository.MovieRepository
import com.example.infrastructure.out.persistence.entity.MovieEntity
import com.example.infrastructure.out.persistence.entity.QMovieEntity
import com.example.infrastructure.out.persistence.mapper.MovieMapper
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class MovieCoreRepositoryImpl(
    private val queryFactory: JPAQueryFactory
): MovieRepository {

    override fun getMoviesReleasedUntil(now: LocalDate): List<Movie> {
        val movie = QMovieEntity.movieEntity

        return queryFactory
            .select(
                Projections.constructor(MovieEntity::class.java,
                    movie.id,
                    movie.title,
                    movie.thumbnail,
                    movie.releaseDate,
                    movie.runTime,
                    movie.genre,
                    movie.rating
                )
            )
            .from(movie)
            .where(movie.releaseDate.loe(now))
            .orderBy(movie.releaseDate.asc())
            .fetch()
            .map { MovieMapper.toMovie(it) }
    }

}