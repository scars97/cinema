package com.example.common.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val message: String?
) {
    // 좌석
    ALREADY_RESERVED(HttpStatus.CONFLICT, "예약된 좌석입니다."),

    // 예약
    SEAT_EXCEED_LIMIT(HttpStatus.BAD_REQUEST, "최대 5개의 좌석을 예약할 수 있습니다."),
    NOT_SAME_ROW(HttpStatus.BAD_REQUEST, "2개 이상의 좌석을 예약할 때는 같은 행이어야 합니다."),
    NOT_CONTINUOUS_SEAT(HttpStatus.BAD_REQUEST, "각 행의 좌석은 연속되어야 합니다."),

    // 회원
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원 ID 입니다."),

    // 상영일정
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상영 일정 ID 입니다."),

    // 처리율 제한
    REQUEST_EXCEED_LIMIT(HttpStatus.TOO_MANY_REQUESTS, "요청 제한을 초과했습니다. 잠시 후 다시 시도해주세요.")
}