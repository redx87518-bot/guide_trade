package com.guidetrade.app.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guidetrade.app.domain.model.UserSettings
import com.guidetrade.app.domain.repository.AuthRepository
import com.guidetrade.app.domain.repository.UserRepository
import com.guidetrade.app.domain.usecase.GetUserSettingsUseCase
import com.guidetrade.app.domain.usecase.SaveUserSettingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SettingsUiState(
    val settings: UserSettings? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false
)

class SettingsViewModel(
    private val authRepository: AuthRepository = com.guidetrade.app.data.repository.AuthRepositoryImpl(),
    private val userRepository: UserRepository = com.guidetrade.app.data.repository.UserRepositoryImpl(),
    private val getUserSettings: GetUserSettingsUseCase = GetUserSettingsUseCase(
        com.guidetrade.app.data.repository.UserRepositoryImpl()
    ),
    private val saveUserSettings: SaveUserSettingsUseCase = SaveUserSettingsUseCase(
        com.guidetrade.app.data.repository.UserRepositoryImpl()
    )
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        observeSettings()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            authRepository.currentUser.collect { user ->
                if (user != null) {
                    getUserSettings.observe(user.uid).collect { settings ->
                        _uiState.value = _uiState.value.copy(
                            settings = settings,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun saveSettings(settings: UserSettings) {
        val uid = settings.uid
        if (uid.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)
            val result = saveUserSettings(settings)
            _uiState.value = _uiState.value.copy(
                isSaving = false,
                error = result.exceptionOrNull()?.message,
                saveSuccess = result.isSuccess
            )
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }

    fun clearSaveResult() {
        _uiState.value = _uiState.value.copy(saveSuccess = false, error = null)
    }
}
