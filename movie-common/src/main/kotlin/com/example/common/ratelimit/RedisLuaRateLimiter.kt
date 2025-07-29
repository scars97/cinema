package com.example.common.ratelimit

import com.example.common.annotation.LimitRequestPerTime
import com.example.common.model.RateLimitResponse
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component("RedisLuaRateLimiter")
class RedisLuaRateLimiter(
    private val rateLimitScript: RedisScript<List<*>>,
    private val redisTemplate: RedisTemplate<String, Any>
): RateLimiter {

    override fun tryCall(key: String, limitRequestPerTime: LimitRequestPerTime): RateLimitResponse {
        val result = redisTemplate.execute(
            rateLimitScript,
            listOf(key),
            limitRequestPerTime.limitCount,
            TimeUnit.SECONDS.convert(limitRequestPerTime.ttl, limitRequestPerTime.ttlTimeUnit)
        )

        return RateLimitResponse(
            allowed = (result[0] as Long) == 1L,
            limit = (result[1] as Long),
            remaining = (result[2] as Long)
        )
    }

}