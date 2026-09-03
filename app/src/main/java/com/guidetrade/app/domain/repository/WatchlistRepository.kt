package com.guidetrade.app.domain.repository

import com.guidetrade.app.domain.model.WatchlistItem
import kotlinx.coroutines.flow.Flow

interface WatchlistRepository {
    fun observeWatchlist(uid: String): Flow<List<WatchlistItem>>
    suspend fun addSymbol(uid: String, item: WatchlistItem): Result<Unit>
    suspend fun removeSymbol(uid: String, symbol: String): Result<Unit>
    suspend fun getWatchlist(uid: String): Result<List<WatchlistItem>>
}
