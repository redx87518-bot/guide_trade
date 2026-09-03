package com.guidetrade.app.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guidetrade.app.domain.model.ChatMessage
import com.guidetrade.app.domain.model.MessageStatus
import com.guidetrade.app.domain.repository.ResearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val sessionId: String? = null
)

class ChatViewModel(
    private val researchRepository: ResearchRepository = com.guidetrade.app.data.repository.ResearchRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun observeMessages(sessionId: String) {
        _uiState.value = _uiState.value.copy(sessionId = sessionId)
        viewModelScope.launch {
            researchRepository.observeChatMessages(sessionId).collect { messages ->
                _uiState.value = _uiState.value.copy(
                    messages = messages.sortedBy { it.timestamp },
                    isLoading = false
                )
            }
        }
    }

    fun sendMessage(text: String, sessionId: String, uid: String) {
        val message = ChatMessage(
            text = text,
            isUser = true,
            timestamp = System.currentTimeMillis(),
            status = MessageStatus.Sending
        )
        viewModelScope.launch {
            researchRepository.sendMessage(sessionId, message)
        }
    }
}
