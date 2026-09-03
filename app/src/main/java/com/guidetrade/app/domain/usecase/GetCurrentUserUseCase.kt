package com.guidetrade.app.domain.usecase

import com.guidetrade.app.domain.model.User
import com.guidetrade.app.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentUserUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<User?> = authRepository.currentUser
}
