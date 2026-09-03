package com.guidetrade.app.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.guidetrade.app.domain.model.ChatMessage
import com.guidetrade.app.domain.model.ChatSession
import com.guidetrade.app.domain.model.NotificationItem
import com.guidetrade.app.domain.model.ResearchNote
import com.guidetrade.app.domain.repository.ResearchRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ResearchRepositoryImpl : ResearchRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun observeChatSessions(uid: String): Flow<List<ChatSession>> = callbackFlow {
        val listener = firestore.collection("chat_sessions")
            .whereEqualTo("uid", uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                } else if (snapshot != null) {
                    val sessions = snapshot.toObjects(ChatSession::class.java)
                    trySend(sessions)
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun createChatSession(uid: String, title: String): Result<String> {
        return try {
            val session = ChatSession(
                uid = uid,
                title = title,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            val document = firestore.collection("chat_sessions")
                .add(session)
                .await()
            Result.success(document.id)
        } catch (e: Exception) {
            Log.e("ResearchRepositoryImpl", "Failed to create chat session", e)
            Result.failure(e)
        }
    }

    override fun observeChatMessages(sessionId: String): Flow<List<ChatMessage>> = callbackFlow {
        val listener = firestore.collection("chat_sessions")
            .document(sessionId)
            .collection("messages")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                } else if (snapshot != null) {
                    val messages = snapshot.toObjects(ChatMessage::class.java)
                    trySend(messages)
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun sendMessage(sessionId: String, message: ChatMessage): Result<Unit> {
        return try {
            firestore.collection("chat_sessions")
                .document(sessionId)
                .collection("messages")
                .add(message)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ResearchRepositoryImpl", "Failed to send message", e)
            Result.failure(e)
        }
    }

    override fun observeResearchNotes(uid: String): Flow<List<ResearchNote>> = callbackFlow {
        val listener = firestore.collection("research_notes")
            .whereEqualTo("uid", uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                } else if (snapshot != null) {
                    val notes = snapshot.toObjects(ResearchNote::class.java)
                    trySend(notes)
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun saveResearchNote(note: ResearchNote): Result<Unit> {
        return try {
            val docId = if (note.id.isBlank()) {
                null
            } else {
                note.id
            }
            if (docId != null) {
                firestore.collection("research_notes")
                    .document(docId)
                    .set(note.copy(updatedAt = System.currentTimeMillis()))
                    .await()
            } else {
                firestore.collection("research_notes")
                    .add(note.copy(updatedAt = System.currentTimeMillis()))
                    .await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ResearchRepositoryImpl", "Failed to save research note", e)
            Result.failure(e)
        }
    }

    override suspend fun getResearchNote(noteId: String): Result<ResearchNote> {
        return try {
            val document = firestore.collection("research_notes")
                .document(noteId)
                .get()
                .await()
            val note = document.toObject(ResearchNote::class.java)
            if (note != null) Result.success(note)
            else Result.failure(Exception("Research note not found"))
        } catch (e: Exception) {
            Log.e("ResearchRepositoryImpl", "Failed to get research note", e)
            Result.failure(e)
        }
    }

    override fun observeHistory(uid: String): Flow<List<ResearchNote>> = callbackFlow {
        val listener = firestore.collection("research_notes")
            .whereEqualTo("uid", uid)
            .whereEqualTo("status", "COMPLETED")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                } else if (snapshot != null) {
                    val notes = snapshot.toObjects(ResearchNote::class.java)
                    trySend(notes)
                }
            }
        awaitClose { listener.remove() }
    }

    override fun observeReports(uid: String): Flow<List<ResearchNote>> = callbackFlow {
        val listener = firestore.collection("research_notes")
            .whereEqualTo("uid", uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                } else if (snapshot != null) {
                    val notes = snapshot.toObjects(ResearchNote::class.java)
                    trySend(notes)
                }
            }
        awaitClose { listener.remove() }
    }

    override fun observeNotifications(uid: String): Flow<List<NotificationItem>> = callbackFlow {
        val listener = firestore.collection("notifications")
            .whereEqualTo("uid", uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                } else if (snapshot != null) {
                    val items = snapshot.toObjects(NotificationItem::class.java)
                    trySend(items)
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun markNotificationRead(uid: String, notificationId: String): Result<Unit> {
        return try {
            firestore.collection("notifications")
                .document(notificationId)
                .update("read", true)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ResearchRepositoryImpl", "Failed to mark notification read", e)
            Result.failure(e)
        }
    }
}
