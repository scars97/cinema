package com.example.common.exception

class RateLimitExceedException(
    val methodName: String
): RuntimeException() {
}