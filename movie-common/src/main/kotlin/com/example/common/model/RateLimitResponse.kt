package com.example.common.model

import java.time.LocalDateTime

data class RateLimitResponse(
    val allowed: Boolean,
    val limit: Long,
    val remaining: Long,
    val retryAfter: LocalDateTime? = null
) {

    companion object {
        fun success(allowed: Boolean, limit: Long, remaining: Long): RateLimitResponse {
            return RateLimitResponse(allowed, limit, remaining, null)
        }

        fun fail(allowed: Boolean, limit: Long, remaining: Long, retryAfter: LocalDateTime?): RateLimitResponse {
            return RateLimitResponse(allowed, limit, remaining, retryAfter)
        }
    }

}