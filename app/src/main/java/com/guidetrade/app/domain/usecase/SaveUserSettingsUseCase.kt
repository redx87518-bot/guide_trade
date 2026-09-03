package com.guidetrade.app.domain.usecase

import com.guidetrade.app.domain.model.UserSettings
import com.guidetrade.app.domain.repository.UserRepository

class SaveUserSettingsUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(settings: UserSettings): Result<Unit> =
        userRepository.saveUserSettings(settings)
}
