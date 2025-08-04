package com.example.common.ratelimit

import com.example.common.annotation.LimitRequestPerTime
import com.example.common.model.RateLimitResponse
import com.example.common.util.TimeUtil
import io.github.bucket4j.Bucket
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

@Component("Bucket4jRateLimiter")
class Bucket4jRateLimiter: RateLimiter {

    private val bucketStorage = ConcurrentHashMap<String, Bucket>()
    private val retryTimeStorage = ConcurrentHashMap<String, LocalDateTime>()

    override fun tryCall(key: String, limitRequestPerTime: LimitRequestPerTime): RateLimitResponse {
        val bucket = resolveBucket(key, limitRequestPerTime)

        val isConsumed = bucket.tryConsume(1)

        return if (isConsumed) {
            RateLimitResponse.success(
                allowed = true,
                limit = limitRequestPerTime.limitCount,
                bucket.availableTokens
            )
        } else {
            RateLimitResponse.fail(
                allowed = false,
                limit = limitRequestPerTime.limitCount,
                0L,
                retryAfter = retryTimeStorage[key]
            )
        }
    }

    private fun resolveBucket(key: String, limitRequestPerTime: LimitRequestPerTime): Bucket {
        val ttl = TimeUtil.toDuration(limitRequestPerTime.ttl, limitRequestPerTime.ttlTimeUnit)

        retryTimeStorage.computeIfAbsent(key) {
            LocalDateTime.now().plus(ttl)
        }

        return bucketStorage.computeIfAbsent(key) {
            Bucket.builder()
                .addLimit { limit ->
                    limit.capacity(limitRequestPerTime.limitCount)
                        .refillIntervally( // 1분마다 토큰 채움
                            limitRequestPerTime.limitCount,
                            ttl
                        )
                }
                .build()
        }
    }

}