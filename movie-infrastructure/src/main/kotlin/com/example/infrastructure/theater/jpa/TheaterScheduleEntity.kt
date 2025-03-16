package com.example.infrastructure.theater.jpa

import com.example.infrastructure.common.BaseEntity
import com.example.infrastructure.movie.jpa.MovieEntity
import jakarta.persistence.*
import java.time.LocalDate

@Entity
class TheaterScheduleEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    val id: Long = 0,

    val screeningDate: LocalDate,

    val startTime: String,

    val endTime: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false, foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val movie: MovieEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false, foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val theater: TheaterEntity
): BaseEntity()
