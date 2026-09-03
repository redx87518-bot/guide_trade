package com.guidetrade.app.ui.screens.research

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guidetrade.app.domain.model.ResearchNote
import com.guidetrade.app.domain.repository.ResearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ResearchResultsUiState(
    val note: ResearchNote? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class ResearchResultsViewModel(
    private val researchRepository: ResearchRepository = com.guidetrade.app.data.repository.ResearchRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResearchResultsUiState())
    val uiState: StateFlow<ResearchResultsUiState> = _uiState.asStateFlow()

    fun loadNote(noteId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = researchRepository.getResearchNote(noteId)
            _uiState.value = if (result.isSuccess) {
                _uiState.value.copy(
                    note = result.getOrNull(),
                    isLoading = false
                )
            } else {
                _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }
}
