package com.guidetrade.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guidetrade.app.domain.model.User
import com.guidetrade.app.domain.model.UserSettings
import com.guidetrade.app.domain.repository.AuthRepository
import com.guidetrade.app.domain.repository.UserRepository
import com.guidetrade.app.domain.usecase.GetCurrentUserUseCase
import com.guidetrade.app.domain.usecase.GetUserSettingsUseCase
import com.guidetrade.app.domain.usecase.SignOutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val user: User? = null,
    val settings: UserSettings? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

class HomeViewModel(
    private val authRepository: AuthRepository = com.guidetrade.app.data.repository.AuthRepositoryImpl(),
    private val userRepository: UserRepository = com.guidetrade.app.data.repository.UserRepositoryImpl(),
    private val getCurrentUser: GetCurrentUserUseCase = GetCurrentUserUseCase(
        com.guidetrade.app.data.repository.AuthRepositoryImpl()
    ),
    private val getUserSettings: GetUserSettingsUseCase = GetUserSettingsUseCase(
        com.guidetrade.app.data.repository.UserRepositoryImpl()
    ),
    private val signOut: SignOutUseCase = SignOutUseCase(
        com.guidetrade.app.data.repository.AuthRepositoryImpl()
    )
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeCurrentUser()
    }

    private fun observeCurrentUser() {
        viewModelScope.launch {
            getCurrentUser().collect { user ->
                _uiState.value = _uiState.value.copy(user = user, isLoading = false)
                if (user != null) {
                    observeSettings(user.uid)
                }
            }
        }
    }

    private fun observeSettings(uid: String) {
        viewModelScope.launch {
            getUserSettings.observe(uid).collect { settings ->
                _uiState.value = _uiState.value.copy(
                    settings = settings,
                    isLoading = false
                )
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            signOut()
        }
    }
}
