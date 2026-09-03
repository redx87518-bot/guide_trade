package com.guidetrade.app.ui.screens.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guidetrade.app.domain.model.WatchlistItem
import com.guidetrade.app.domain.repository.AuthRepository
import com.guidetrade.app.domain.repository.WatchlistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WatchlistUiState(
    val items: List<WatchlistItem> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val uid: String? = null
)

class WatchlistViewModel(
    private val authRepository: AuthRepository = com.guidetrade.app.data.repository.AuthRepositoryImpl(),
    private val watchlistRepository: WatchlistRepository = com.guidetrade.app.data.repository.WatchlistRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(WatchlistUiState())
    val uiState: StateFlow<WatchlistUiState> = _uiState.asStateFlow()

    init {
        observeUser()
    }

    private fun observeUser() {
        viewModelScope.launch {
            authRepository.currentUser.collect { user ->
                if (user != null) {
                    _uiState.value = _uiState.value.copy(uid = user.uid)
                    observeWatchlist(user.uid)
                } else {
                    _uiState.value = WatchlistUiState()
                }
            }
        }
    }

    private fun observeWatchlist(uid: String) {
        _uiState.value = _uiState.value.copy(uid = uid, isLoading = true)
        viewModelScope.launch {
            watchlistRepository.observeWatchlist(uid).collect { items ->
                _uiState.value = _uiState.value.copy(
                    items = items,
                    isLoading = false
                )
            }
        }
    }

    fun addSymbol(symbol: String, companyName: String) {
        val uid = _uiState.value.uid ?: return
        viewModelScope.launch {
            val item = WatchlistItem(
                symbol = symbol.uppercase(),
                companyName = companyName,
                addedAt = System.currentTimeMillis()
            )
            watchlistRepository.addSymbol(uid, item)
        }
    }

    fun removeSymbol(symbol: String) {
        val uid = _uiState.value.uid ?: return
        viewModelScope.launch {
            watchlistRepository.removeSymbol(uid, symbol)
        }
    }
}
