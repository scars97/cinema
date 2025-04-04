package com.example.common

import io.github.bucket4j.Bucket
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap

class Bucket4jRateLimiterTest {

    private val cache = ConcurrentHashMap<String, Bucket>()

    @DisplayName("특정 값마다 요청이 제한된다.")
    @Test
    fun whenRequestSpecificValueThenRestricting() {
        // given
        val bucket1 = resolveBucket("TEST-IP-1")
        val bucket2 = resolveBucket("TEST-IP-2")

        // when
        val bucket1IsConsumed = CompletableFuture.supplyAsync { bucket1.tryConsume(10) }
        val bucket2IsConsumed = CompletableFuture.supplyAsync { bucket2.tryConsume(10) }

        // then
        assertThat(bucket1IsConsumed.get()).isTrue()
        assertThat(bucket2IsConsumed.get()).isTrue()
    }

    private fun resolveBucket(value: String): Bucket {
        return cache.computeIfAbsent(value) {
            Bucket.builder()
                .addLimit { limit ->
                    limit.capacity(10)
                        .refillGreedy(10, Duration.ofSeconds(5))
                }
                .build()
        }
    }

    @DisplayName("55번의 요청 중, 50번 요청은 성공하고 5번 요청은 실패한다.")
    @Test
    fun given55RequestsThen50SuccessfulAnd5Failed() {
        val requestLimit = 50L
        val bucket = Bucket.builder()
            .addLimit { limit ->
                limit.capacity(requestLimit)
                    .refillIntervally(requestLimit, Duration.ofSeconds(5))
            }
            .build()

        var successCount = 0
        var failureCount = 0
        repeat(55) {
            if (bucket.tryConsume(1)) {
                successCount++
            } else {
                failureCount++
            }
        }

        assertThat(successCount).isEqualTo(requestLimit)
        assertThat(failureCount).isEqualTo(5)
    }

}