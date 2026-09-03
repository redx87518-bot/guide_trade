package com.guidetrade.app.ui.navigation

sealed class NavRoutes(val route: String) {
    data object Auth : NavRoutes("auth")
    data object Home : NavRoutes("home")
    data object ChatResults : NavRoutes("chat_results")
    data object Chat : NavRoutes("chat/{sessionId}") {
        fun createRoute(sessionId: String) = "chat/$sessionId"
    }
    data object Research : NavRoutes("research")
    data object ResearchResults : NavRoutes("research_results/{noteId}") {
        fun createRoute(noteId: String) = "research_results/$noteId"
    }
    data object Watchlist : NavRoutes("watchlist")
    data object History : NavRoutes("history")
    data object Reports : NavRoutes("reports")
    data object Settings : NavRoutes("settings")
    data object VoiceSettings : NavRoutes("settings/voice")
    data object TelegramSettings : NavRoutes("settings/telegram")
    data object DiscordSettings : NavRoutes("settings/discord")
    data object NotificationsSettings : NavRoutes("settings/notifications")
}
