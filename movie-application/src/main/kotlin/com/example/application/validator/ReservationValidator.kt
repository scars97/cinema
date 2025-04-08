package com.example.application.validator

import com.example.application.dto.ReservationInfo
import com.example.business.theater.service.TheaterScheduleService
import com.example.business.user.service.UserService
import com.example.common.exception.BusinessException
import com.example.common.exception.ErrorCode.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ReservationValidator(
    private val userService: UserService,
    private val scheduleService: TheaterScheduleService
) {

    private val log = LoggerFactory.getLogger(ReservationValidator::class.java)

    fun validate(info: ReservationInfo) {
        if (!userService.isUserExists(info.userId)) {
            log.error("존재하지 않는 회원 ID: ${info.userId}")
            throw BusinessException(USER_NOT_FOUND)
        }

        if (!scheduleService.isScheduleExists(info.scheduleId)) {
            log.error("존재하지 않는 회원 ID: ${info.scheduleId}")
            throw BusinessException(SCHEDULE_NOT_FOUND)
        }
    }

}