package com.example.common.ratelimit

import com.example.common.annotation.LimitRequestPerTime
import com.example.common.model.RateLimitResponse
import io.github.bucket4j.Bucket
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

@Component("Bucket4jRateLimiter")
class Bucket4jRateLimiter: RateLimiter {

    private val cache = ConcurrentHashMap<String, Bucket>()

    override fun tryCall(key: String, limitRequestPerTime: LimitRequestPerTime): RateLimitResponse {
        val bucket = resolveBucket(key, limitRequestPerTime)

        return RateLimitResponse(
            bucket.tryConsume(1),
            limitRequestPerTime.limitCount,
            bucket.availableTokens
        )
    }

    private fun resolveBucket(key: String, limitRequestPerTime: LimitRequestPerTime): Bucket {
        return cache.computeIfAbsent(key) {
            Bucket.builder()
                .addLimit { limit ->
                    limit.capacity(limitRequestPerTime.limitCount)
                        .refillIntervally( // 1분마다 토큰 채움
                            limitRequestPerTime.limitCount,
                            toDuration(limitRequestPerTime.ttl, limitRequestPerTime.ttlTimeUnit)
                        )
                }
                .build()
        }
    }

    private fun toDuration(amount: Long, unit: TimeUnit): Duration {
        val millis = unit.toMillis(amount)
        return Duration.ofMillis(millis)
    }

}