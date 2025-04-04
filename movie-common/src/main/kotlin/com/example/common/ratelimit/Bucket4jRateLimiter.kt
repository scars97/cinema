package com.example.common.ratelimit

import io.github.bucket4j.Bucket
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

@Component("Bucket4jRateLimiter")
class Bucket4jRateLimiter: RateLimiter {

    private val cache = ConcurrentHashMap<String, Bucket>()

    override fun isAllowed(ip: String): Boolean {
        return resolveBucket(ip).tryConsume(1)
    }

    private fun resolveBucket(value: String): Bucket {
        return cache.computeIfAbsent(value) {
            Bucket.builder()
                .addLimit { limit ->
                    limit.capacity(50)
                        .refillGreedy(50, Duration.ofMinutes(1))
                }
                .build()
        }
    }

}