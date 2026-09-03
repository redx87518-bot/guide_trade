package com.guidetrade.app.domain.repository

import com.guidetrade.app.domain.model.User
import com.guidetrade.app.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUser(uid: String): Result<User>
    suspend fun saveUser(user: User): Result<Unit>
    suspend fun getUserSettings(uid: String): Result<UserSettings>
    suspend fun saveUserSettings(settings: UserSettings): Result<Unit>
    fun observeUserSettings(uid: String): Flow<UserSettings?>
}
