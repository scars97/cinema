package com.example.infrastructure.out.persistence.repository.jpa

import com.example.infrastructure.out.persistence.entity.ReservationEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ReservationJpaRepository: JpaRepository<ReservationEntity, Long> {
}