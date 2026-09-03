package com.guidetrade.app.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.guidetrade.app.domain.model.User
import com.guidetrade.app.domain.model.UserSettings
import com.guidetrade.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl : UserRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override suspend fun getUser(uid: String): Result<User> {
        return try {
            val document = firestore.collection("users")
                .document(uid)
                .get()
                .await()
            val user = document.toObject(User::class.java)
            if (user != null) Result.success(user)
            else Result.failure(Exception("User not found"))
        } catch (e: Exception) {
            Log.e("UserRepositoryImpl", "Failed to get user", e)
            Result.failure(e)
        }
    }

    override suspend fun saveUser(user: User): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(user.uid)
                .set(user)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("UserRepositoryImpl", "Failed to save user", e)
            Result.failure(e)
        }
    }

    override suspend fun getUserSettings(uid: String): Result<UserSettings> {
        return try {
            val document = firestore.collection("user_settings")
                .document(uid)
                .get()
                .await()
            val settings = document.toObject(UserSettings::class.java)
            if (settings != null) {
                Result.success(settings.copy(uid = uid))
            } else {
                val default = UserSettings(uid = uid)
                saveUserSettings(default)
                Result.success(default)
            }
        } catch (e: Exception) {
            Log.e("UserRepositoryImpl", "Failed to get settings", e)
            Result.failure(e)
        }
    }

    override suspend fun saveUserSettings(settings: UserSettings): Result<Unit> {
        return try {
            firestore.collection("user_settings")
                .document(settings.uid)
                .set(settings)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("UserRepositoryImpl", "Failed to save settings", e)
            Result.failure(e)
        }
    }

    override fun observeUserSettings(uid: String): Flow<UserSettings?> {
        return callbackFlow {
            val listener = firestore.collection("user_settings")
                .document(uid)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                    } else if (snapshot != null && snapshot.exists()) {
                        trySend(snapshot.toObject(UserSettings::class.java)?.copy(uid = uid))
                    } else {
                        trySend(UserSettings(uid = uid))
                    }
                }
            awaitClose { listener.remove() }
        }
    }
}
