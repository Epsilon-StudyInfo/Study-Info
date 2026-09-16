package com.studyinfo.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studyinfo.app.ServiceLocator
import com.studyinfo.app.utils.AppResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Tracks whether the user is currently signed in. Polled by the splash screen.
 */
class SplashViewModel : ViewModel() {
    val isSignedIn: StateFlow<Boolean> = MutableStateFlow(false).also { flow ->
        viewModelScope.launch {
            flow.value = ServiceLocator.authRepository.isSignedIn()
        }
    }.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            (isSignedIn as MutableStateFlow).value = ServiceLocator.authRepository.isSignedIn()
        }
    }
}

data class AuthUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val emailSent: Boolean = false,
)

sealed class AuthEvent {
    data class NameChanged(val value: String) : AuthEvent()
    data class EmailChanged(val value: String) : AuthEvent()
    data class PasswordChanged(val value: String) : AuthEvent()
    data object Submit : AuthEvent()
    data object SendReset : AuthEvent()
    data object ResetError : AuthEvent()
}

class AuthViewModel : ViewModel() {
    private val _ui = MutableStateFlow(AuthUiState())
    val ui: StateFlow<AuthUiState> = _ui.asStateFlow()

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.NameChanged -> _ui.value = _ui.value.copy(name = event.value, error = null)
            is AuthEvent.EmailChanged -> _ui.value = _ui.value.copy(email = event.value, error = null)
            is AuthEvent.PasswordChanged -> _ui.value = _ui.value.copy(password = event.value, error = null)
            AuthEvent.ResetError -> _ui.value = _ui.value.copy(error = null)
            AuthEvent.Submit -> submit()
            AuthEvent.SendReset -> sendReset()
        }
    }

    private fun submit() {
        val state = _ui.value
        if (state.loading) return
        if (state.email.isBlank() || state.password.isBlank()) {
            _ui.value = state.copy(error = "Please fill in all fields.")
            return
        }
        _ui.value = state.copy(loading = true, error = null)
        viewModelScope.launch {
            val result: AppResult<*> = if (state.name.isBlank()) {
                ServiceLocator.authRepository.login(state.email, state.password)
            } else {
                ServiceLocator.authRepository.register(state.name, state.email, state.password)
            }
            when (result) {
                is AppResult.Success -> _ui.value = _ui.value.copy(loading = false, success = true)
                is AppResult.Failure -> _ui.value = _ui.value.copy(loading = false, error = result.message)
            }
        }
    }

    private fun sendReset() {
        val state = _ui.value
        if (state.email.isBlank()) {
            _ui.value = state.copy(error = "Please enter your email first.")
            return
        }
        _ui.value = state.copy(loading = true, error = null)
        viewModelScope.launch {
            when (val r = ServiceLocator.authRepository.sendPasswordReset(state.email)) {
                is AppResult.Success -> _ui.value = _ui.value.copy(loading = false, emailSent = true)
                is AppResult.Failure -> _ui.value = _ui.value.copy(loading = false, error = r.message)
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        _ui.value = _ui.value.copy(loading = true, error = null)
        viewModelScope.launch {
            when (val r = ServiceLocator.authRepository.signInWithGoogle(idToken)) {
                is AppResult.Success -> _ui.value = _ui.value.copy(loading = false, success = true)
                is AppResult.Failure -> _ui.value = _ui.value.copy(loading = false, error = r.message)
            }
        }
    }
}
