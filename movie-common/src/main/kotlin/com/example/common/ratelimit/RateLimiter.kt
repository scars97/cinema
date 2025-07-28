package com.example.common.ratelimit

import com.example.common.annotation.LimitRequestPerTime
import com.example.common.model.RateLimitResponse

interface RateLimiter {

    fun tryCall(key: String, limitRequestPerTime: LimitRequestPerTime): RateLimitResponse

}