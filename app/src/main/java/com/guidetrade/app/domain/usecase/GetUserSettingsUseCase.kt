package com.guidetrade.app.domain.usecase

import com.guidetrade.app.domain.model.UserSettings
import com.guidetrade.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUserSettingsUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(uid: String): Result<UserSettings> =
        userRepository.getUserSettings(uid)

    fun observe(uid: String): Flow<UserSettings?> =
        userRepository.observeUserSettings(uid)
}
