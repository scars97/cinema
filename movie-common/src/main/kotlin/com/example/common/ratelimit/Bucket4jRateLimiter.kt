package com.example.common.ratelimit

import com.example.common.annotation.LimitRequestPerTime
import com.example.common.model.RateLimitResponse
import io.github.bucket4j.Bucket
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

@Component("Bucket4jRateLimiter")
class Bucket4jRateLimiter: RateLimiter {

    private val tokenStorage = ConcurrentHashMap<String, Bucket>()

    override fun tryCall(key: String, limitRequestPerTime: LimitRequestPerTime): RateLimitResponse {
        val bucket = resolveBucket(key, limitRequestPerTime)

        val probe = bucket.tryConsumeAndReturnRemaining(1)

        return if (probe.isConsumed) {
            RateLimitResponse.success(
                allowed = true,
                limit = limitRequestPerTime.limitCount,
                probe.remainingTokens
            )
        } else {
            val nanosToWait = probe.nanosToWaitForRefill
            val retryAfterTime = LocalDateTime.now().plusNanos(nanosToWait)

            RateLimitResponse.fail(
                allowed = false,
                limit = limitRequestPerTime.limitCount,
                0L,
                retryAfter = retryAfterTime
            )
        }
    }

    private fun resolveBucket(key: String, limitRequestPerTime: LimitRequestPerTime): Bucket {
        return tokenStorage.computeIfAbsent(key) {
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