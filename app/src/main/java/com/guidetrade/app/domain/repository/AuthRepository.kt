package com.guidetrade.app.domain.repository

import com.guidetrade.app.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<User?>
    suspend fun signInWithEmail(email: String, password: String): Result<Unit>
    suspend fun createAccountWithEmail(email: String, password: String, displayName: String): Result<Unit>
    suspend fun signOut()
    suspend fun deleteAccount(): Result<Unit>
}
