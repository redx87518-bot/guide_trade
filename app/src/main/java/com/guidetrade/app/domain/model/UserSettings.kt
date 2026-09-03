package com.guidetrade.app.domain.model

data class UserSettings(
    val uid: String = "",
    val displayName: String = "",
    val voiceEnabled: Boolean = false,
    val autoReadResearch: Boolean = false,
    val elevenLabsVoiceId: String = "",
    val telegramEnabled: Boolean = false,
    val telegramBotToken: String = "",
    val telegramChatId: String = "",
    val discordEnabled: Boolean = false,
    val discordWebhookUrl: String = "",
    val researchDepth: ResearchDepth = ResearchDepth.STANDARD,
    val defaultMarket: String = "US",
    val researchStyle: String = "concise",
    val notificationsEnabled: Boolean = true,
    val pushNotifications: Boolean = true,
    val emailNotifications: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class ResearchDepth {
    QUICK, STANDARD, DEEP
}
