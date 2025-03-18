package com.example.infrastructure.`in`

import com.example.application.usecase.MovieUseCase
import com.example.application.dto.MovieResult
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/movie")
class MovieController(
    private val movieUseCase: MovieUseCase
) {

    @GetMapping("")
    fun getMovies() : ResponseEntity<List<MovieResult>> {
        return ResponseEntity.ok(movieUseCase.getAvailableMovies())
    }

}