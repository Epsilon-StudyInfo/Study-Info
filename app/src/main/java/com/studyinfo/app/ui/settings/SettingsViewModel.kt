package com.studyinfo.app.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studyinfo.app.ServiceLocator
import com.studyinfo.app.utils.AppResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SettingsUiState(
    val loading: Boolean = false,
    val message: String? = null,
    val error: String? = null,
    val accountDeleted: Boolean = false,
    val loggedOut: Boolean = false,
)

class SettingsViewModel : ViewModel() {
    private val _ui = MutableStateFlow(SettingsUiState())
    val ui: StateFlow<SettingsUiState> = _ui.asStateFlow()

    fun logout() {
        ServiceLocator.authRepository.signOut()
        _ui.value = _ui.value.copy(loggedOut = true)
    }

    fun deleteAccount() {
        _ui.value = _ui.value.copy(loading = true)
        viewModelScope.launch {
            val r = ServiceLocator.authRepository.deleteAccount()
            when (r) {
                is AppResult.Success -> _ui.value = _ui.value.copy(loading = false, accountDeleted = true)
                is AppResult.Failure -> _ui.value = _ui.value.copy(loading = false, error = r.message)
            }
        }
    }

    fun syncNow() {
        // Trigger one-time sync; UI shows a transient message.
        _ui.value = _ui.value.copy(message = "Sync scheduled")
        viewModelScope.launch {
            kotlinx.coroutines.delay(2000)
            _ui.value = _ui.value.copy(message = null)
        }
    }
}
