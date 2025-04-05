package com.example.common.ratelimit

import com.example.common.annotation.LimitRequestPerTime

interface RateLimiter {

    fun tryCall(key: String, limitRequestPerTime: LimitRequestPerTime): Boolean

}