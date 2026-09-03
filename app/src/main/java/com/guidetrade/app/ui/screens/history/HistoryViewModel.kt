package com.guidetrade.app.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guidetrade.app.domain.model.ResearchNote
import com.guidetrade.app.domain.repository.AuthRepository
import com.guidetrade.app.domain.repository.ResearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HistoryUiState(
    val notes: List<ResearchNote> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class HistoryViewModel(
    private val authRepository: AuthRepository = com.guidetrade.app.data.repository.AuthRepositoryImpl(),
    private val researchRepository: ResearchRepository = com.guidetrade.app.data.repository.ResearchRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        observeHistory()
    }

    private fun observeHistory() {
        viewModelScope.launch {
            authRepository.currentUser.collect { user ->
                if (user != null) {
                    researchRepository.observeHistory(user.uid).collect { notes ->
                        _uiState.value = _uiState.value.copy(
                            notes = notes,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}
