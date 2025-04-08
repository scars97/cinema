package com.example.business.fake

import com.example.business.theater.domain.Theater
import com.example.business.theater.repository.TheaterRepository

class FakeTheaterRepository: TheaterRepository {

    override fun getTheatersBy(theaterIds: Set<Long>): List<Theater> {
        return listOf(
            Theater(theaterId = 1L, name = "A관"),
            Theater(theaterId = 2L, name = "B관")
        )
    }

}