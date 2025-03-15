package com.example.api.movie.dto

import java.time.LocalDate

data class MovieResponse(
    val movieId: Long,
    val title: String,
    val releaseDate: LocalDate,
    val thumbnail: String,
    val runTime: Int,
    val genre: String,
    val rating: String,
    val theater: String,
    val screeningSchedule: MutableList<String> = mutableListOf()
)

/*- 영화 제목, 개봉일, 썸네일 이미지, 러닝 타임(분), 영화 장르
- 영상물 등급
- 상영관 이름, 상영 시간표*/