package com.studyinfo.app.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studyinfo.app.ServiceLocator
import com.studyinfo.app.data.auth.GoogleAuth
import com.studyinfo.app.utils.AppResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

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
    data class GoogleSignIn(val activityContext: Context) : AuthEvent()
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
            is AuthEvent.GoogleSignIn -> signInWithGoogle(event.activityContext)
        }
    }

    /**
     * True when "Continue with Google" can work: the WEB OAuth client id must be present
     * in the merged google-services.json AND Firebase must be configured in this build
     * (Firebase authenticates the Google ID token — it is authoritative).
     */
    fun isGoogleAvailable(context: Context): Boolean =
        GoogleAuth.isConfigured(context) &&
            ServiceLocator.isInitialised() &&
            ServiceLocator.authRepository.isFirebaseConfigured

    /**
     * "Continue with Google": the Credential Manager picker must be launched from an
     * Activity context (passed in from the composable); everything after that runs off
     * the main thread as usual.
     */
    private fun signInWithGoogle(activityContext: Context) {
        val state = _ui.value
        if (state.loading || state.success) return
        _ui.value = state.copy(loading = true, error = null)
        viewModelScope.launch {
            // The account picker is user-paced (NOT a hang) — no watchdog here.
            val profile = withContext(Dispatchers.Main) {
                GoogleAuth.signIn(activityContext)
            }
            when (profile) {
                is AppResult.Success -> {
                    // Watchdog only around the network/backend part: if it ever stalls
                    // (pathological network), surface a clear error instead of an
                    // eternal spinner. A late completion is still honoured — the splash
                    // gate repairs the session on the next launch.
                    val result = withAuthWatchdog {
                        withContext(Dispatchers.IO) {
                            ServiceLocator.authRepository.signInWithGoogle(profile.value)
                        }
                    }
                    when (result) {
                        is AppResult.Success -> _ui.value = _ui.value.copy(loading = false, success = true)
                        is AppResult.Failure -> _ui.value = _ui.value.copy(
                            loading = false,
                            error = result.message.takeIf { it.isNotBlank() },
                        )
                    }
                }
                is AppResult.Failure -> _ui.value = _ui.value.copy(
                    loading = false,
                    error = profile.message.takeIf { it.isNotBlank() },
                )
            }
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
            val result = withAuthWatchdog {
                when (state.mode) {
                    AuthMode.LOGIN ->
                        ServiceLocator.authRepository.login(state.email, state.password)
                    AuthMode.REGISTER ->
                        ServiceLocator.authRepository.register(state.name, state.email, state.password)
                    AuthMode.FORGOT ->
                        ServiceLocator.authRepository.sendPasswordReset(state.email)
                }
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

    /**
     * Hard 45s ceiling on any single auth operation so the button can never spin forever,
     * whatever the network does. The underlying work is not aborted — if it completes
     * later the success state still lands (and the splash gate self-heals the session
     * on the next launch).
     */
    private suspend fun <T> withAuthWatchdog(block: suspend () -> AppResult<T>): AppResult<T> {
        val result = withTimeoutOrNull(AUTH_TIMEOUT_MS) { block() }
        return result ?: AppResult.failure(
            "This is taking unusually long — check your internet connection and try again.",
        )
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

        /** Ceiling for a single auth operation (see [withAuthWatchdog]). */
        private const val AUTH_TIMEOUT_MS = 45_000L
    }
}
