package com.example.infrastructure.out.persistence.repository.core

import com.example.business.user.repository.UserRepository
import com.example.infrastructure.out.persistence.repository.jpa.UserJpaRepository
import org.springframework.stereotype.Repository

@Repository
class UserCoreRepositoryImpl(
    private val jpaRepository: UserJpaRepository
): UserRepository {

    override fun existsBy(userId: Long): Boolean {
        return jpaRepository.existsById(userId)
    }
}