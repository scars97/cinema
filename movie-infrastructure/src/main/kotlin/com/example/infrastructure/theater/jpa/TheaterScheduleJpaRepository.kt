package com.example.infrastructure.theater.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface TheaterScheduleJpaRepository: JpaRepository<TheaterScheduleEntity, Long> {

    fun findByMovieIdIn(movieIds: List<Long>): List<TheaterScheduleEntity>

}