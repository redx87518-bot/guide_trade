package com.guidetrade.app.domain.usecase

import com.guidetrade.app.domain.model.User
import com.guidetrade.app.domain.model.UserSettings
import com.guidetrade.app.domain.repository.AuthRepository

class SignInWithEmailUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> =
        authRepository.signInWithEmail(email, password)
}
