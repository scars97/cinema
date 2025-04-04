package com.example.common.interceptor

import com.example.common.model.ErrorResponse
import com.example.common.ratelimit.RateLimiter
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class RateLimitInterceptor(
    @Qualifier("Bucket4jRateLimiter") private val rateLimiter: RateLimiter
): HandlerInterceptor {

    private val log = LoggerFactory.getLogger(RateLimitInterceptor::class.java)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        log.info("Received request: " +
                "Address=${request.localAddr} " +
                "URI=${request.requestURI}")

        if (!rateLimiter.isAllowed(request.localAddr)) {
            return errorResponse(
                response,
                HttpStatus.TOO_MANY_REQUESTS,
                "요청이 너무 많습니다. 1분에 최대 50회까지 요청할 수 있습니다. 잠시 후 다시 시도해주세요."
            )
        }

        return true
    }

    private fun errorResponse(response: HttpServletResponse, status: HttpStatus, message: String): Boolean {
        log.error("Responding with error: Status={}, Message={}", status, message)
        response.characterEncoding = "UTF-8"
        response.contentType = "text/plain;charset=UTF-8"
        response.status = status.value()
        response.writer.write(
            ObjectMapper().writeValueAsString(ErrorResponse(status, message))
        )
        return false
    }

}