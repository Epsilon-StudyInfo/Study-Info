package com.studyinfo.app.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.studyinfo.app.ServiceLocator
import com.studyinfo.app.sync.SyncWorker
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
    /** Cloud sync only applies to Firebase-backed accounts; local-only accounts skip it. */
    val cloudSyncAvailable: Boolean = false,
)

class SettingsViewModel(app: Application) : AndroidViewModel(app) {

    private val _ui = MutableStateFlow(
        SettingsUiState(cloudSyncAvailable = ServiceLocator.authRepository.isFirebaseConfigured),
    )
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
        val app = getApplication<Application>()
        if (ServiceLocator.authRepository.firebaseUid == null) {
            _ui.value = _ui.value.copy(
                message = "Cloud sync isn't active for this account (local-only mode). " +
                    "Your data is safely stored on this device.",
            )
            return
        }
        runCatching { SyncWorker.enqueueOneTime(app) }
        _ui.value = _ui.value.copy(message = "Sync started — it runs in the background.")
        viewModelScope.launch {
            kotlinx.coroutines.delay(4000)
            _ui.value = _ui.value.copy(message = null)
        }
    }

    fun dismissMessage() {
        _ui.value = _ui.value.copy(message = null, error = null)
    }
}
