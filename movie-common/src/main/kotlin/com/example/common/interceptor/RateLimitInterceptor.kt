package com.example.common.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class RateLimitInterceptor: HandlerInterceptor {

    private val log = LoggerFactory.getLogger(RateLimitInterceptor::class.java)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        log.info("Received request: " +
                "Address=${request.localAddr} " +
                "URI=${request.requestURI}")

        return true
    }

}