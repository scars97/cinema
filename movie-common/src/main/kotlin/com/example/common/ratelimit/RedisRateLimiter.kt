package com.example.common.ratelimit

import com.example.common.annotation.LimitRequestPerTime
import com.example.common.model.RateLimitResponse
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component("RedisRateLimiter")
class RedisRateLimiter(
    private val redisTemplate: RedisTemplate<String, Any>
): RateLimiter {

    override fun tryCall(key: String, limitRequestPerTime: LimitRequestPerTime): RateLimitResponse {
        val previousCount = (redisTemplate.opsForValue().get(key) as? Number)?.toLong()

        if (previousCount != null && previousCount > limitRequestPerTime.limitCount) {
            return RateLimitResponse(
                false,
                limitRequestPerTime.limitCount,
                0L
            )
        }

        if (previousCount == null) {
            redisTemplate.opsForValue().set(key, 0, limitRequestPerTime.ttl, limitRequestPerTime.ttlTimeUnit)
        }

        val currentCount = redisTemplate.opsForValue().increment(key)!!

        return RateLimitResponse(
            true,
            limitRequestPerTime.limitCount,
            limitRequestPerTime.limitCount - currentCount
        )
    }

}