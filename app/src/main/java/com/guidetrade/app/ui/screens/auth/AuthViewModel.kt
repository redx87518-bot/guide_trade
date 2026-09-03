package com.guidetrade.app.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guidetrade.app.domain.model.User
import com.guidetrade.app.domain.repository.AuthRepository
import com.guidetrade.app.domain.usecase.GetCurrentUserUseCase
import com.guidetrade.app.domain.usecase.SignInWithEmailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSignedIn: Boolean = false,
    val user: User? = null
)

class AuthViewModel(
    private val authRepository: AuthRepository = com.guidetrade.app.data.repository.AuthRepositoryImpl(),
    private val getCurrentUser: GetCurrentUserUseCase = GetCurrentUserUseCase(authRepository),
    private val signIn: SignInWithEmailUseCase = SignInWithEmailUseCase(authRepository)
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        observeCurrentUser()
    }

    private fun observeCurrentUser() {
        viewModelScope.launch {
            getCurrentUser().collect { user ->
                _uiState.value = _uiState.value.copy(
                    isSignedIn = user != null,
                    user = user
                )
            }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = signIn(email, password)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = result.getOrNull()?.toString(),
                isSignedIn = result.isSuccess
            )
        }
    }

    fun createAccount(email: String, password: String, displayName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = authRepository.createAccountWithEmail(email, password, displayName)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = result.exceptionOrNull()?.message,
                isSignedIn = result.isSuccess
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
