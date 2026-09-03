package com.guidetrade.app.ui.screens.research

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guidetrade.app.domain.model.ResearchNote
import com.guidetrade.app.domain.model.ResearchStatus
import com.guidetrade.app.domain.repository.AuthRepository
import com.guidetrade.app.domain.repository.ResearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

data class ResearchUiState(
    val query: String = "",
    val isResearching: Boolean = false,
    val progress: Float = 0f,
    val progressStage: String = "",
    val error: String? = null,
    val result: ResearchNote? = null
)

class ResearchViewModel(
    private val authRepository: AuthRepository = com.guidetrade.app.data.repository.AuthRepositoryImpl(),
    private val researchRepository: ResearchRepository = com.guidetrade.app.data.repository.ResearchRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResearchUiState())
    val uiState: StateFlow<ResearchUiState> = _uiState.asStateFlow()

    fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
    }

    fun startResearch() {
        val query = _uiState.value.query.trim()
        if (query.isEmpty()) {
            _uiState.value = _uiState.value.copy(error = "Please enter a research query")
            return
        }
        viewModelScope.launch {
            val uid = authRepository.currentUser.firstOrNull()?.uid
            if (uid == null) {
                _uiState.value = _uiState.value.copy(error = "Not authenticated")
                return@launch
            }

            _uiState.value = ResearchUiState(
                query = query,
                isResearching = true,
                progress = 0f,
                progressStage = "Initializing research..."
            )

            val stages = listOf(
                "Identifying company",
                "Gathering financial data",
                "Checking recent news",
                "Analyzing evidence",
                "Preparing research"
            )

            stages.forEachIndexed { index, stage ->
                kotlinx.coroutines.delay(800)
                _uiState.value = _uiState.value.copy(
                    progress = (index + 1) / stages.size.toFloat(),
                    progressStage = stage
                )
            }

            val note = ResearchNote(
                uid = uid,
                symbol = query,
                companyName = query,
                summary = "Research in progress. Results will appear here when complete.",
                status = ResearchStatus.IN_PROGRESS
            )

            _uiState.value = _uiState.value.copy(
                isResearching = false,
                progress = 1f,
                result = note
            )
        }
    }
}
