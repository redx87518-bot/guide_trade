package com.guidetrade.app.domain.model

data class User(
    val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val avatarUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
