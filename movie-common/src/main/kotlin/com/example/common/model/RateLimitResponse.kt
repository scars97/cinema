package com.example.common.model

data class RateLimitResponse(
    val allowed: Boolean,
    val limit: Long,
    val remaining: Long
)