package com.example.common.ratelimit

import com.example.common.annotation.LimitRequestPerTime
import com.example.common.model.RateLimitResponse
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@Component("RedisRateLimiter")
class RedisRateLimiter(
    private val redisTemplate: RedisTemplate<String, Any>
): RateLimiter {

    override fun tryCall(key: String, limitRequestPerTime: LimitRequestPerTime): RateLimitResponse {
        val previousCount = (redisTemplate.opsForValue().get(key) as? Number)?.toLong()

        if (previousCount != null && previousCount >= limitRequestPerTime.limitCount) {
            val ttlSeconds = redisTemplate.getExpire(key, TimeUnit.SECONDS)

            val retryAfter = if (ttlSeconds > 0) {
                LocalDateTime.now().plusSeconds(ttlSeconds)
            } else {
                null
            }

            return RateLimitResponse.fail(
                false,
                limitRequestPerTime.limitCount,
                0L,
                retryAfter
            )
        }

        if (previousCount == null) {
            redisTemplate.opsForValue().set(key, 0, limitRequestPerTime.ttl, limitRequestPerTime.ttlTimeUnit)
        }

        val currentCount = redisTemplate.opsForValue().increment(key)!!

        return RateLimitResponse.success(
            true,
            limitRequestPerTime.limitCount,
            limitRequestPerTime.limitCount - currentCount
        )
    }

}