package com.example.common.exception

class BusinessException(
    val errorCode: ErrorCode
): RuntimeException() {
}