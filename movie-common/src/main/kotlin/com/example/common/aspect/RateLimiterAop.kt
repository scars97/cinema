package com.example.common.aspect

import com.example.common.annotation.LimitRequestPerTime
import com.example.common.exception.RateLimitExceedException
import com.example.common.ratelimit.RateLimiter
import com.example.common.util.CustomSpringELParser
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Aspect
@Component
class RateLimiterAop(
    @Qualifier("RedisLuaRateLimiter") private val rateLimiter: RateLimiter
) {

    @Around("@annotation(com.example.common.annotation.LimitRequestPerTime)")
    @Throws(Throwable::class)
    fun rateLimit(joinPoint: ProceedingJoinPoint) {
        val limitRequestPerTime = getAnnotationInfo(joinPoint)

        val key = "RATE-LIMIT:" + getKey(joinPoint, limitRequestPerTime)

        if (!rateLimiter.tryCall(key, limitRequestPerTime)) {
            throw RateLimitExceedException(getMethodName(joinPoint))
        }

        joinPoint.proceed()
    }

    private fun getMethodName(joinPoint: ProceedingJoinPoint): String {
        val signature = joinPoint.signature as MethodSignature
        return signature.method.name
    }

    private fun getKey(
        joinPoint: ProceedingJoinPoint,
        limitRequestPerTime: LimitRequestPerTime
    ): Any? {
        val signature = joinPoint.signature as MethodSignature
        return CustomSpringELParser.getDynamicValue(
            signature.parameterNames,
            joinPoint.args,
            limitRequestPerTime.key
        )
    }

    private fun getAnnotationInfo(joinPoint: ProceedingJoinPoint): LimitRequestPerTime {
        val signature = joinPoint.signature as MethodSignature
        val method = signature.method
        return method.getAnnotation(LimitRequestPerTime::class.java)
    }

}