package com.example.common.ratelimit

interface RateLimiter {

    fun isAllowed(ip: String): Boolean

}