package com.guidetrade.app.domain.model

data class ResearchNote(
    val id: String = "",
    val symbol: String = "",
    val companyName: String = "",
    val summary: String = "",
    val bullishFactors: List<String> = emptyList(),
    val bearishFactors: List<String> = emptyList(),
    val risks: List<String> = emptyList(),
    val outlook: String = "",
    val confidence: ConfidenceLevel = ConfidenceLevel.MEDIUM,
    val sources: List<Source> = emptyList(),
    val disclaimer: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val status: ResearchStatus = ResearchStatus.DRAFT,
    val uid: String = ""
)

enum class ResearchStatus {
    DRAFT, IN_PROGRESS, COMPLETED
}

enum class ConfidenceLevel {
    LOW, MEDIUM, HIGH
}

data class Source(
    val title: String = "",
    val url: String = "",
    val provider: String = "",
    val publishedTimestamp: Long = 0L,
    val retrievedTimestamp: Long = 0L,
    val excerpt: String = "",
    val freshness: DataFreshness = DataFreshness.UNKNOWN
)

enum class DataFreshness {
    REAL_TIME,
    DELAYED,
    END_OF_DAY,
    HISTORICAL,
    UNKNOWN
}

data class WatchlistItem(
    val symbol: String = "",
    val companyName: String = "",
    val lastPrice: Double? = null,
    val change: Double? = null,
    val addedAt: Long = System.currentTimeMillis()
)

data class NotificationItem(
    val id: String = "",
    val type: String = "",
    val title: String = "",
    val message: String = "",
    val read: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val uid: String = ""
)
