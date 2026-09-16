package com.studyinfo.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studyinfo.app.ServiceLocator
import com.studyinfo.app.utils.AppResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Which auth form the user is on — drives validation and the submit action. */
enum class AuthMode { LOGIN, REGISTER, FORGOT }

data class AuthUiState(
    val mode: AuthMode = AuthMode.LOGIN,
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val emailSent: Boolean = false,
)

sealed class AuthEvent {
    data class ModeChanged(val mode: AuthMode) : AuthEvent()
    data class NameChanged(val value: String) : AuthEvent()
    data class EmailChanged(val value: String) : AuthEvent()
    data class PasswordChanged(val value: String) : AuthEvent()
    data class ConfirmPasswordChanged(val value: String) : AuthEvent()
    data object TogglePasswordVisibility : AuthEvent()
    data object Submit : AuthEvent()
    data object ResetError : AuthEvent()
}

class AuthViewModel : ViewModel() {
    private val _ui = MutableStateFlow(AuthUiState())
    val ui: StateFlow<AuthUiState> = _ui.asStateFlow()

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.ModeChanged -> _ui.value = _ui.value.copy(
                mode = event.mode,
                error = null,
                // Keep email when switching login<->register (same user flow), clear secrets.
                password = "",
                confirmPassword = "",
            )
            is AuthEvent.NameChanged -> _ui.value = _ui.value.copy(name = event.value, error = null)
            is AuthEvent.EmailChanged -> _ui.value = _ui.value.copy(email = event.value, error = null)
            is AuthEvent.PasswordChanged -> _ui.value = _ui.value.copy(password = event.value, error = null)
            is AuthEvent.ConfirmPasswordChanged -> _ui.value = _ui.value.copy(confirmPassword = event.value, error = null)
            AuthEvent.TogglePasswordVisibility -> _ui.value = _ui.value.copy(passwordVisible = !_ui.value.passwordVisible)
            AuthEvent.ResetError -> _ui.value = _ui.value.copy(error = null)
            AuthEvent.Submit -> submit()
        }
    }

    private fun submit() {
        val state = _ui.value
        if (state.loading || state.success) return

        val validation = validate(state)
        if (validation != null) {
            _ui.value = state.copy(error = validation)
            return
        }

        _ui.value = state.copy(loading = true, error = null)
        viewModelScope.launch {
            val result: AppResult<*> = when (state.mode) {
                AuthMode.LOGIN ->
                    ServiceLocator.authRepository.login(state.email, state.password)
                AuthMode.REGISTER ->
                    ServiceLocator.authRepository.register(state.name, state.email, state.password)
                AuthMode.FORGOT ->
                    ServiceLocator.authRepository.sendPasswordReset(state.email)
            }
            when (result) {
                is AppResult.Success -> _ui.value = _ui.value.copy(
                    loading = false,
                    success = state.mode != AuthMode.FORGOT,
                    emailSent = state.mode == AuthMode.FORGOT,
                )
                is AppResult.Failure -> _ui.value = _ui.value.copy(loading = false, error = result.message)
            }
        }
    }

    private fun validate(state: AuthUiState): String? = when (state.mode) {
        AuthMode.LOGIN -> when {
            state.email.isBlank() -> "Please enter your email."
            state.password.isBlank() -> "Please enter your password."
            else -> null
        }
        AuthMode.REGISTER -> when {
            state.name.isBlank() -> "Please enter your name."
            state.email.isBlank() -> "Please enter your email."
            !EMAIL_REGEX.matches(state.email.trim()) -> "That email address doesn't look right."
            state.password.length < 6 -> "Password must be at least 6 characters."
            state.password != state.confirmPassword -> "Passwords don't match."
            else -> null
        }
        AuthMode.FORGOT -> when {
            state.email.isBlank() -> "Please enter your email."
            !EMAIL_REGEX.matches(state.email.trim()) -> "That email address doesn't look right."
            else -> null
        }
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}
