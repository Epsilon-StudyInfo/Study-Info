package com.studyinfo.app.data.auth

import android.content.Context
import android.content.SharedPreferences

/**
 * Holds the active local session + which account currently owns the data in Room.
 *
 * - [activeAccountId] — the signed-in local account, or null.
 * - [dataOwnerId] — the account that owns the rows currently in Room. When a DIFFERENT
 *   account signs in, all user-generated tables are wiped first (prevents cross-account
 *   data leaks). Signing out does NOT wipe: signing back in with the same account keeps data.
 *
 * SharedPreferences is used (rather than DataStore) because the sign-in gate is read
 * synchronously at startup before the first frame.
 */
class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences("prepvault_session", Context.MODE_PRIVATE)

    var activeAccountId: String?
        get() = prefs.getString(KEY_ACTIVE_ACCOUNT, null)
        private set(value) {
            prefs.edit().putString(KEY_ACTIVE_ACCOUNT, value).apply()
        }

    var activeAccountName: String
        get() = prefs.getString(KEY_ACTIVE_NAME, "").orEmpty()
        private set(value) {
            prefs.edit().putString(KEY_ACTIVE_NAME, value).apply()
        }

    var activeAccountEmail: String
        get() = prefs.getString(KEY_ACTIVE_EMAIL, "").orEmpty()
        private set(value) {
            prefs.edit().putString(KEY_ACTIVE_EMAIL, value).apply()
        }

    /** The account id that owns the data currently stored in Room (null = no data yet). */
    var dataOwnerId: String?
        get() = prefs.getString(KEY_DATA_OWNER, null)
        set(value) {
            prefs.edit().putString(KEY_DATA_OWNER, value).apply()
        }

    val isActive: Boolean get() = activeAccountId != null

    fun startSession(accountId: String, name: String, email: String) {
        activeAccountId = accountId
        activeAccountName = name
        activeAccountEmail = email
    }

    fun endSession() {
        // Keep dataOwnerId untouched: the data still belongs to that account.
        activeAccountId = null
        activeAccountName = ""
        activeAccountEmail = ""
    }

    /** True when the signing-in account differs from the owner of the current local data. */
    fun needsDataSwap(incomingAccountId: String): Boolean {
        val owner = dataOwnerId ?: return false   // no data yet -> nothing to wipe
        return owner != incomingAccountId
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }

    private companion object {
        const val KEY_ACTIVE_ACCOUNT = "active_account_id"
        const val KEY_ACTIVE_NAME = "active_account_name"
        const val KEY_ACTIVE_EMAIL = "active_account_email"
        const val KEY_DATA_OWNER = "data_owner_id"
    }
}
