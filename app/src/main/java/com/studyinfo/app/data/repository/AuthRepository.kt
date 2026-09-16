package com.studyinfo.app.data.repository

import android.content.Context
import com.studyinfo.app.data.auth.GoogleAuth
import com.studyinfo.app.data.auth.PasswordHasher
import com.studyinfo.app.data.auth.SessionManager
import com.studyinfo.app.data.database.dao.LocalAccountDao
import com.studyinfo.app.data.database.dao.UserProfileDao
import com.studyinfo.app.data.database.entity.AccountProvider
import com.studyinfo.app.data.database.entity.LocalAccountEntity
import com.studyinfo.app.data.database.entity.UserProfileEntity
import com.studyinfo.app.data.firebase.FirebaseAuthService
import com.studyinfo.app.data.firebase.FirebaseUserInfo
import com.studyinfo.app.domain.model.SyncState
import com.studyinfo.app.utils.AppResult
import com.studyinfo.app.utils.newId
import com.studyinfo.app.utils.nowEpoch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Date

/**
 * Auth repository — FIREBASE-FIRST.
 *
 * When a Firebase project is configured ([firebaseAuth] != null), Firebase Authentication
 * is the AUTHORITATIVE identity provider:
 *  - registration, login and Google sign-in only report success after Firebase succeeded;
 *    Firebase failures are returned to the UI and never silently downgraded;
 *  - the Firebase UID is the canonical account id everywhere (Room rows, local session,
 *    Firestore `users/{uid}`) — there is no second, locally generated identity;
 *  - Room's `local_accounts` table is a CACHE of profile data. Its PBKDF2 password hash
 *    is a NON-AUTHORITATIVE fallback credential only (used in local-only mode), never a
 *    way to bypass Firebase when Firebase is configured.
 *
 * When Firebase is NOT configured (placeholder google-services.json), the repository
 * falls back to LOCAL-ONLY auth (Room + PBKDF2) so the app remains usable offline.
 *
 * Data ownership: Room holds one user's data at a time. When a DIFFERENT account signs
 * in, [onSwitchDataOwner] wipes all user-generated tables (no cross-account leaks).
 * Stale local accounts created by older app versions are MIGRATED to the Firebase UID
 * (matched by email) BEFORE that wipe check runs, so the user's study data is preserved.
 */
class AuthRepository(
    private val firebaseAuth: FirebaseAuthService?,
    private val localAccounts: LocalAccountDao,
    private val userProfileDao: UserProfileDao,
    private val session: SessionManager,
    private val onSwitchDataOwner: suspend () -> Unit,
) {

    val currentUid: String?
        get() = session.activeAccountId ?: firebaseAuth?.currentUser?.uid

    /** Firebase uid only — used to gate cloud sync (local-only accounts skip it entirely). */
    val firebaseUid: String?
        get() = firebaseAuth?.currentUser?.uid

    val isFirebaseConfigured: Boolean
        get() = firebaseAuth != null

    fun observeUserProfile(): Flow<UserProfileEntity?> =
        userProfileDao.observe().map { profile ->
            // After an account switch the table is wiped, so at most one row remains;
            // still, ignore a stale row that does not belong to the active account.
            profile?.takeIf { it.uid == session.activeAccountId || !session.isActive }
        }

    fun isSignedIn(): Boolean = session.isActive || firebaseAuth?.isSignedIn() == true

    // ---------------------------------------------------------------- register

    /**
     * Email + password registration.
     *
     * Firebase-configured build: Firebase creates the account (authoritative) → on
     * success the local cache row is created with the Firebase UID and the session
     * starts. On failure the Firebase error is returned — no local account, no session.
     *
     * Local-only build: Room + PBKDF2 (explicit fallback).
     */
    suspend fun register(name: String, email: String, password: String): AppResult<LocalAccountEntity> {
        val normalizedName = name.trim()
        val normalizedEmail = email.trim().lowercase()

        // Fast local validation before hitting the network.
        if (normalizedName.isEmpty()) return AppResult.failure("Please enter your name.")
        if (!EMAIL_REGEX.matches(normalizedEmail)) return AppResult.failure("Please enter a valid email address.")
        if (password.length < 6) return AppResult.failure("Password must be at least 6 characters.")

        val fb = firebaseAuth ?: return registerLocalOnly(normalizedName, normalizedEmail, password)

        // Firebase is authoritative: its result decides the outcome.
        val fbResult = fb.registerWithEmailPassword(normalizedName, normalizedEmail, password)
        if (fbResult is AppResult.Failure) return fbResult
        val user = (fbResult as AppResult.Success).value

        val account = syncLocalAccountToFirebaseUser(
            user = user,
            provider = AccountProvider.PASSWORD,
            fallbackName = normalizedName,
        )
        // Cache the password as the NON-AUTHORITATIVE fallback credential (local-only
        // degradation). The user has just proven it against Firebase, so this is a sync,
        // not a credential change.
        localAccounts.updatePassword(account.id, PasswordHasher.hash(password))
        activateAccount(account)
        return AppResult.success(account)
    }

    // ---------------------------------------------------------------- login

    /**
     * Email + password login.
     *
     * Firebase-configured build: Firebase authenticates (authoritative) → on success the
     * local cache/session uses the Firebase UID (migrating any stale Room account).
     * On failure the Firebase error is returned — there is NO local-password fallback.
     *
     * Local-only build: Room + PBKDF2 (explicit fallback).
     */
    suspend fun login(email: String, password: String): AppResult<LocalAccountEntity> {
        val normalizedEmail = email.trim().lowercase()
        if (normalizedEmail.isEmpty()) return AppResult.failure("Please enter your email.")
        if (password.isEmpty()) return AppResult.failure("Please enter your password.")

        val fb = firebaseAuth ?: return loginLocalOnly(normalizedEmail, password)

        val fbResult = fb.signInWithEmailPassword(normalizedEmail, password)
        if (fbResult is AppResult.Failure) return fbResult
        val user = (fbResult as AppResult.Success).value

        val account = syncLocalAccountToFirebaseUser(
            user = user,
            provider = AccountProvider.PASSWORD,
            fallbackName = normalizedEmail.substringBefore("@"),
        )
        // Refresh the non-authoritative fallback credential (see register).
        localAccounts.updatePassword(account.id, PasswordHasher.hash(password))
        localAccounts.touchLogin(account.id, nowEpoch())
        activateAccount(account)
        return AppResult.success(account)
    }

    // ---------------------------------------------------------------- google

    /**
     * "Continue with Google":
     *
     * Credential Manager (account picker) → Google ID token → [FirebaseAuthService.
     * signInWithGoogleIdToken] (`GoogleAuthProvider.getCredential` + `signInWithCredential`)
     * → Firebase UID → local Room cache row keyed by that UID → session.
     *
     * A Firebase failure (collision with a password account, disabled provider, SHA-1
     * mismatch, network) is returned to the UI — no local account is created and the
     * sign-in is NOT reported as successful.
     */
    suspend fun signInWithGoogle(profile: GoogleAuth.GoogleProfile): AppResult<LocalAccountEntity> {
        val email = profile.email.trim().lowercase()
        if (email.isEmpty()) return AppResult.failure("Google did not return an email address.")

        val fb = firebaseAuth ?: return AppResult.failure(
            "Google Sign-In requires Firebase. Check the app's Firebase configuration " +
                "(google-services.json).",
        )

        val fbResult = fb.signInWithGoogleIdToken(profile.idToken)
        if (fbResult is AppResult.Failure) return fbResult
        val user = (fbResult as AppResult.Success).value

        val account = syncLocalAccountToFirebaseUser(
            user = user,
            provider = AccountProvider.GOOGLE,
            fallbackName = profile.displayName,
        )
        localAccounts.touchLogin(account.id, nowEpoch())
        activateAccount(account)
        return AppResult.success(account)
    }

    // ---------------------------------------------------------------- local cache sync

    /**
     * Creates or updates the local cache row for a Firebase user so that its id IS the
     * Firebase UID (one canonical identity):
     *
     *  1. A row with this UID already exists → refresh name / provider label / photo.
     *  2. A row with the same EMAIL but a legacy local id exists (created by an older
     *     app version) → MIGRATE it: the row is re-keyed to the Firebase UID, the cached
     *     profile row follows, and — critically — data ownership is transferred BEFORE
     *     [activateAccount]'s swap check runs, so the user's study data is preserved
     *     instead of being wiped. The stored password hash is intentionally PRESERVED:
     *     it is the non-authoritative fallback credential, and credentials are never
     *     overwritten during a migration.
     *  3. Nothing matches → insert a fresh cache row keyed by the Firebase UID.
     */
    private suspend fun syncLocalAccountToFirebaseUser(
        user: FirebaseUserInfo,
        provider: String,
        fallbackName: String,
    ): LocalAccountEntity {
        val email = user.email?.trim()?.lowercase().orEmpty()
        val name = user.displayName?.trim()?.takeIf { it.isNotEmpty() }
            ?: fallbackName.trim().takeIf { it.isNotEmpty() }
            ?: email.ifEmpty { "Student" }.substringBefore("@")

        localAccounts.findById(user.uid)?.let { existing ->
            if (existing.name != name) localAccounts.rename(user.uid, name)
            localAccounts.updateProvider(user.uid, provider, user.photoUrl)
            return existing.copy(name = name, provider = provider, photoUrl = user.photoUrl)
        }

        if (email.isNotEmpty()) {
            localAccounts.findByEmail(email)?.let { stale ->
                val migrated = stale.copy(
                    id = user.uid,
                    name = name,
                    provider = provider,
                    photoUrl = user.photoUrl,
                )
                localAccounts.delete(stale.id)
                localAccounts.insert(migrated)
                rekeyProfileRow(stale.id, user.uid)
                if (session.dataOwnerId == stale.id) {
                    // Transfer ownership before the swap check so data is NOT wiped.
                    session.dataOwnerId = user.uid
                }
                return migrated
            }
        }

        val created = LocalAccountEntity(
            id = user.uid,
            name = name,
            email = email,
            passwordHash = "",
            provider = provider,
            photoUrl = user.photoUrl,
            createdAt = Date(nowEpoch()),
            lastLoginAt = Date(nowEpoch()),
        )
        localAccounts.insert(created)
        return created
    }

    /** Moves the cached profile row from a legacy local id to the Firebase UID. */
    private suspend fun rekeyProfileRow(oldId: String, newId: String) {
        val profile = userProfileDao.getById(oldId) ?: return
        userProfileDao.delete(oldId)
        userProfileDao.upsert(profile.copy(uid = newId))
    }

    // ---------------------------------------------------------------- local-only fallback

    /** Explicit fallback path for builds without Firebase — Room is the authority here. */
    private suspend fun registerLocalOnly(
        name: String,
        email: String,
        password: String,
    ): AppResult<LocalAccountEntity> {
        if (localAccounts.findByEmail(email) != null) {
            return AppResult.failure("An account with this email already exists. Try logging in instead.")
        }
        val account = LocalAccountEntity(
            id = newId(),
            name = name,
            email = email,
            passwordHash = PasswordHasher.hash(password),
            createdAt = Date(nowEpoch()),
        )
        localAccounts.insert(account)
        activateAccount(account)
        return AppResult.success(account)
    }

    /** Explicit fallback path for builds without Firebase — Room is the authority here. */
    private suspend fun loginLocalOnly(
        email: String,
        password: String,
    ): AppResult<LocalAccountEntity> {
        val local = localAccounts.findByEmail(email)
            ?: return AppResult.failure("No account found with this email. Create one first.")
        if (local.provider == AccountProvider.GOOGLE) {
            return AppResult.failure(
                "This email is signed up with Google. Please use “Continue with Google”.",
            )
        }
        if (!PasswordHasher.verify(password, local.passwordHash)) {
            return AppResult.failure("Incorrect email or password.")
        }
        localAccounts.touchLogin(local.id, nowEpoch())
        activateAccount(local)
        return AppResult.success(local)
    }

    // ---------------------------------------------------------------- session

    private suspend fun activateAccount(account: LocalAccountEntity) {
        if (session.needsDataSwap(account.id)) {
            onSwitchDataOwner()
        }
        session.dataOwnerId = account.id
        session.startSession(account.id, account.name, account.email)
        cacheProfile(account)
    }

    /**
     * Sign-out:
     *  1. signs out Firebase Auth,
     *  2. clears the Credential Manager state (Google account picker),
     *  3. clears the local active session,
     *  4. PRESERVES local study data for the same Firebase UID (dataOwnerId untouched).
     */
    fun signOut() {
        firebaseAuth?.signOut()
        session.endSession()
    }

    /** See [signOut]. The context variant additionally resets Credential Manager. */
    fun signOut(context: Context) {
        // Fire-and-forget and intentionally non-fatal: the Credential Manager reset only
        // affects which accounts the picker pre-selects; a failure there must not block
        // the sign-out itself. (GoogleAuth.clearCredentialState catches internally.)
        CoroutineScope(Dispatchers.IO).launch {
            GoogleAuth.clearCredentialState(context)
        }
        signOut()
    }

    // ---------------------------------------------------------------- account

    suspend fun updateProfileName(name: String) {
        val id = session.activeAccountId ?: return
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return
        localAccounts.rename(id, trimmed)
        session.startSession(id, trimmed, session.activeAccountEmail)
        userProfileDao.getById(id)?.let { existing ->
            userProfileDao.upsert(
                existing.copy(
                    displayName = trimmed,
                    updatedAt = Date(nowEpoch()),
                    syncState = SyncState.PENDING_UPDATE,
                ),
            )
        }
    }

    suspend fun updateTargetExam(targetExam: String) {
        val id = session.activeAccountId ?: return
        val existing = userProfileDao.getById(id) ?: return
        userProfileDao.upsert(
            existing.copy(
                targetExam = targetExam,
                updatedAt = Date(nowEpoch()),
                syncState = SyncState.PENDING_UPDATE,
            ),
        )
    }

    /**
     * Changes the password. Firebase-configured build: Firebase verifies the current
     * password and applies the change (authoritative); the local hash is then refreshed
     * as the non-authoritative fallback. Local-only build: the stored hash is verified.
     */
    suspend fun changePassword(oldPassword: String, newPassword: String): AppResult<Unit> {
        val id = session.activeAccountId
            ?: return AppResult.failure("Not signed in.")
        val account = localAccounts.findById(id)
            ?: return AppResult.failure("Account not found.")
        if (account.provider == AccountProvider.GOOGLE) {
            return AppResult.failure(
                "This account signs in with Google — there is no password to change.",
            )
        }
        if (newPassword.length < 6) {
            return AppResult.failure("New password must be at least 6 characters.")
        }

        val fb = firebaseAuth
        if (fb == null) {
            if (!PasswordHasher.verify(oldPassword, account.passwordHash)) {
                return AppResult.failure("Current password is incorrect.")
            }
        } else {
            when (val result = fb.changePassword(oldPassword, newPassword)) {
                is AppResult.Failure -> return result
                is AppResult.Success -> { /* fall through to refresh the local fallback hash */ }
            }
        }

        localAccounts.updatePassword(id, PasswordHasher.hash(newPassword))
        return AppResult.success(Unit)
    }

    /**
     * Deletes the account. Firebase-configured build: Firebase deletion is authoritative
     * — if it fails the deletion is reported as failed (no fake success). Only after
     * Firebase succeeds is the local cache cleaned up and the session ended.
     * Local-only build: the Room account is removed directly.
     */
    suspend fun deleteAccount(): AppResult<Unit> {
        val id = session.activeAccountId ?: return AppResult.failure("Not signed in.")

        val fb = firebaseAuth
        if (fb != null) {
            when (val result = fb.deleteAccount()) {
                is AppResult.Failure -> return result
                is AppResult.Success -> { /* proceed to local cleanup */ }
            }
        }

        // Best-effort cache cleanup — intentionally non-fatal: at this point the
        // authoritative account is already gone, and a leftover cache row must never
        // keep the session alive. The session is cleared below regardless.
        runCatching { localAccounts.delete(id) }
            .onFailure { /* non-fatal: stale cache row is inert without a session */ }
        onSwitchDataOwner()
        session.dataOwnerId = null
        session.endSession()
        return AppResult.success(Unit)
    }

    /** Email verification is a Firebase concept; local-only accounts are always trusted. */
    fun isEmailVerified(): Boolean {
        val fb = firebaseAuth ?: return session.isActive
        return fb.currentUser?.emailVerified ?: false
    }

    suspend fun sendPasswordReset(email: String): AppResult<Unit> {
        val fb = firebaseAuth ?: return AppResult.failure(
            "Cloud reset is unavailable. Accounts are stored on this device.",
        )
        return fb.sendPasswordResetEmail(email.trim().lowercase())
    }

    // ---------------------------------------------------------------- helpers

    private suspend fun cacheProfile(account: LocalAccountEntity) {
        val now = Date(nowEpoch())
        val existing = userProfileDao.getById(account.id)
        userProfileDao.upsert(
            UserProfileEntity(
                uid = account.id,
                displayName = account.name,
                email = account.email,
                photoUrl = account.photoUrl ?: existing?.photoUrl,
                emailVerified = isEmailVerified(),
                targetExam = existing?.targetExam ?: "JEE Main",
                preferredLanguage = existing?.preferredLanguage ?: "English",
                createdAt = existing?.createdAt ?: now,
                lastLoginAt = now,
                updatedAt = now,
                syncState = SyncState.SYNCED,
            ),
        )
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}
