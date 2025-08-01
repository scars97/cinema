package com.example.common.ratelimit

import com.example.common.annotation.LimitRequestPerTime
import com.example.common.model.RateLimitResponse
import com.example.common.util.TimeUtil
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component("RedisRateLimiter")
class RedisRateLimiter(
    private val redisTemplate: RedisTemplate<String, Any>
): RateLimiter {

    override fun tryCall(key: String, limitRequestPerTime: LimitRequestPerTime): RateLimitResponse {
        val ops = redisTemplate.opsForHash<String, String>()

        val remainingStr = ops.get(key, "remaining")
        if (remainingStr == "0") {
            val retryAfterStr = ops.get(key, "retryAfter")

            return RateLimitResponse.fail(
                false,
                limitRequestPerTime.limitCount,
                0L,
                retryAfterStr?.let { LocalDateTime.parse(it) } ?: LocalDateTime.now()
            )
        }

        // 없으면 초기화
        if (remainingStr == null) {
            initializeRateLimitKeyIfAbsent(key, limitRequestPerTime, ops)
        }

        // 요청 제한 수 감소
        val currentCount = ops.get(key, "remaining")?.toLong()?.minus(1L) ?: 0L
        ops.put(key, "remaining", currentCount.toString())

        return RateLimitResponse.success(
            true,
            limitRequestPerTime.limitCount,
            currentCount
        )
    }

    private fun initializeRateLimitKeyIfAbsent(
        key: String,
        limitRequestPerTime: LimitRequestPerTime,
        ops: HashOperations<String, String, String>
    ) {
        val ttl = TimeUtil.toDuration(limitRequestPerTime.ttl, limitRequestPerTime.ttlTimeUnit)
        val remaining = limitRequestPerTime.limitCount
        val retryAfter = LocalDateTime.now().plus(ttl)

        ops.put(key, "remaining", remaining.toString())
        ops.put(key, "retryAfter", retryAfter.toString())

        redisTemplate.expire(key, ttl)
    }

}