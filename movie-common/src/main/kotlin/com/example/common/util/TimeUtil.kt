package com.example.common.util

import java.time.Duration
import java.util.concurrent.TimeUnit

class TimeUtil {

    companion object {
        fun toDuration(amount: Long, unit: TimeUnit): Duration =
            when (unit) {
                TimeUnit.SECONDS -> Duration.ofSeconds(amount)
                TimeUnit.MINUTES -> Duration.ofMinutes(amount)
                TimeUnit.MILLISECONDS -> Duration.ofMillis(amount)
                TimeUnit.HOURS -> Duration.ofHours(amount)
                TimeUnit.DAYS -> Duration.ofDays(amount)
                else -> throw IllegalArgumentException("Unsupported TimeUnit: $unit")
            }
    }

}