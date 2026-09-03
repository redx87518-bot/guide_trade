package com.guidetrade.app.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.guidetrade.app.domain.model.WatchlistItem
import com.guidetrade.app.domain.repository.WatchlistRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class WatchlistRepositoryImpl : WatchlistRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun observeWatchlist(uid: String): Flow<List<WatchlistItem>> = callbackFlow {
        val listener = firestore.collection("users")
            .document(uid)
            .collection("watchlist")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                } else if (snapshot != null) {
                    val items = snapshot.toObjects(WatchlistItem::class.java)
                    trySend(items)
                }
            }
        awaitClose { listener.remove() }
    }.map { list -> list.sortedBy { it.symbol } }

    override suspend fun addSymbol(uid: String, item: WatchlistItem): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(uid)
                .collection("watchlist")
                .document(item.symbol)
                .set(item.copy(symbol = item.symbol.uppercase()))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("WatchlistRepositoryImpl", "Failed to add symbol", e)
            Result.failure(e)
        }
    }

    override suspend fun removeSymbol(uid: String, symbol: String): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(uid)
                .collection("watchlist")
                .document(symbol.uppercase())
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("WatchlistRepositoryImpl", "Failed to remove symbol", e)
            Result.failure(e)
        }
    }

    override suspend fun getWatchlist(uid: String): Result<List<WatchlistItem>> {
        return try {
            val snapshot = firestore.collection("users")
                .document(uid)
                .collection("watchlist")
                .get()
                .await()
            Result.success(snapshot.toObjects(WatchlistItem::class.java))
        } catch (e: Exception) {
            Log.e("WatchlistRepositoryImpl", "Failed to get watchlist", e)
            Result.failure(e)
        }
    }
}
