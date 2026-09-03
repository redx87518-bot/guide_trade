package com.guidetrade.app.domain.repository

import com.guidetrade.app.domain.model.ChatMessage
import com.guidetrade.app.domain.model.ChatSession
import com.guidetrade.app.domain.model.NotificationItem
import com.guidetrade.app.domain.model.ResearchNote
import kotlinx.coroutines.flow.Flow

interface ResearchRepository {
    fun observeChatSessions(uid: String): Flow<List<ChatSession>>
    suspend fun createChatSession(uid: String, title: String): Result<String>
    fun observeChatMessages(sessionId: String): Flow<List<ChatMessage>>
    suspend fun sendMessage(sessionId: String, message: ChatMessage): Result<Unit>

    fun observeResearchNotes(uid: String): Flow<List<ResearchNote>>
    suspend fun saveResearchNote(note: ResearchNote): Result<Unit>
    suspend fun getResearchNote(noteId: String): Result<ResearchNote>

    fun observeHistory(uid: String): Flow<List<ResearchNote>>
    fun observeReports(uid: String): Flow<List<ResearchNote>>
    fun observeNotifications(uid: String): Flow<List<NotificationItem>>
    suspend fun markNotificationRead(uid: String, notificationId: String): Result<Unit>
}
