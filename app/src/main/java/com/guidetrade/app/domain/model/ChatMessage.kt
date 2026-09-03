package com.guidetrade.app.domain.model

data class ChatMessage(
    val id: String = "",
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val status: MessageStatus = MessageStatus.Sent
)

enum class MessageStatus {
    Sending, Sent, Error
}

data class ChatSession(
    val id: String = "",
    val title: String = "",
    val uid: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
