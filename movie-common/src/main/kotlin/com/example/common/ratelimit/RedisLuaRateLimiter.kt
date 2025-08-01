package com.example.common.ratelimit

import com.example.common.annotation.LimitRequestPerTime
import com.example.common.model.RateLimitResponse
import com.example.common.util.TimeUtil
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@Component("RedisLuaRateLimiter")
class RedisLuaRateLimiter(
    private val rateLimitScript: RedisScript<List<*>>,
    private val redisTemplate: RedisTemplate<String, String>
): RateLimiter {

    override fun tryCall(key: String, limitRequestPerTime: LimitRequestPerTime): RateLimitResponse {
        val result = redisTemplate.execute(
            rateLimitScript,
            listOf(key),
            limitRequestPerTime.limitCount,
            LocalDateTime.now().plus(TimeUtil.toDuration(limitRequestPerTime.ttl, limitRequestPerTime.ttlTimeUnit)).toString(),
            TimeUnit.SECONDS.convert(limitRequestPerTime.ttl, limitRequestPerTime.ttlTimeUnit)
        )
        .map { it.toString() }

        return RateLimitResponse(
            allowed = result[0] == "1",
            limit = limitRequestPerTime.limitCount,
            remaining = result[1].toLong(),
            retryAfter = if (result[2] == "0") {
                null
            } else {
                LocalDateTime.parse(result[2])
            }
        )
    }

}