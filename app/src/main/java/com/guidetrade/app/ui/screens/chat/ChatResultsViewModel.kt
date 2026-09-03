package com.guidetrade.app.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guidetrade.app.domain.model.ChatSession
import com.guidetrade.app.domain.repository.AuthRepository
import com.guidetrade.app.domain.repository.ResearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatResultsUiState(
    val sessions: List<ChatSession> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val uid: String? = null
)

class ChatResultsViewModel(
    private val authRepository: AuthRepository = com.guidetrade.app.data.repository.AuthRepositoryImpl(),
    private val researchRepository: ResearchRepository = com.guidetrade.app.data.repository.ResearchRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatResultsUiState())
    val uiState: StateFlow<ChatResultsUiState> = _uiState.asStateFlow()

    init {
        observeSessions()
    }

    private fun observeSessions() {
        viewModelScope.launch {
            authRepository.currentUser.collect { user ->
                if (user != null) {
                    _uiState.value = _uiState.value.copy(uid = user.uid)
                    researchRepository.observeChatSessions(user.uid).collect { sessions ->
                        _uiState.value = _uiState.value.copy(
                            sessions = sessions.sortedByDescending { it.createdAt },
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun createNewSession(onSessionCreated: (String) -> Unit) {
        viewModelScope.launch {
            val uid = _uiState.value.uid ?: return@launch
            val result = researchRepository.createChatSession(uid, "New Conversation")
            if (result.isSuccess) {
                onSessionCreated(result.getOrNull() ?: "")
            } else {
                _uiState.value = _uiState.value.copy(
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }
}
