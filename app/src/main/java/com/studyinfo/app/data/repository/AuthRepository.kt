package com.studyinfo.app.data.repository

import com.studyinfo.app.data.auth.GoogleAuth
import com.studyinfo.app.data.auth.PasswordHasher
import com.studyinfo.app.data.auth.SessionManager
import com.studyinfo.app.data.database.dao.LocalAccountDao
import com.studyinfo.app.data.database.dao.UserProfileDao
import com.studyinfo.app.data.database.entity.AccountProvider
import com.studyinfo.app.data.database.entity.LocalAccountEntity
import com.studyinfo.app.data.database.entity.UserProfileEntity
import com.studyinfo.app.data.firebase.FirebaseAuthDataSource
import com.studyinfo.app.domain.model.SyncState
import com.studyinfo.app.utils.AppResult
import com.studyinfo.app.utils.newId
import com.studyinfo.app.utils.nowEpoch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Date

/**
 * Auth repository — LOCAL-FIRST.
 *
 * Accounts are stored in Room (passwords PBKDF2-hashed) so sign-up / sign-in always work,
 * even when Firebase is not configured (placeholder google-services.json) or offline.
 *
 * If a real Firebase project IS configured, the same credentials are mirrored to Firebase Auth
 * best-effort so cloud sync continues to work. A legacy user who previously registered through
 * Firebase is transparently migrated to a local account on their next login.
 *
 * Room data ownership: when a different account signs in, [onSwitchDataOwner] wipes all
 * user-generated tables (prevents cross-account data leaks). Signing out keeps data so the
 * same account gets it back on the next sign-in.
 */
class AuthRepository(
    private val firebaseAuth: FirebaseAuthDataSource?,
    private val localAccounts: LocalAccountDao,
    private val userProfileDao: UserProfileDao,
    private val session: SessionManager,
    private val onSwitchDataOwner: suspend () -> Unit,
) {

    val currentUid: String?
        get() = session.activeAccountId ?: firebaseAuth?.currentUser?.uid

    /** Firebase uid only — used to gate cloud sync (local-only accounts skip it entirely). */
    val firebaseUid: String?
        get() = runCatching { firebaseAuth?.currentUser?.uid }.getOrNull()

    val isFirebaseConfigured: Boolean
        get() = firebaseAuth != null

    fun observeUserProfile(): Flow<UserProfileEntity?> =
        userProfileDao.observe().map { profile ->
            // After an account switch the table is wiped, so at most one row remains;
            // still, ignore a stale row that does not belong to the active account.
            profile?.takeIf { it.uid == session.activeAccountId || !session.isActive }
        }

    fun isSignedIn(): Boolean = session.isActive ||
        runCatching { firebaseAuth?.isSignedIn() == true }.getOrDefault(false)

    // ---------------------------------------------------------------- register

    suspend fun register(name: String, email: String, password: String): AppResult<LocalAccountEntity> {
        val normalizedName = name.trim()
        val normalizedEmail = email.trim().lowercase()

        if (normalizedName.isEmpty()) return AppResult.failure("Please enter your name.")
        if (!EMAIL_REGEX.matches(normalizedEmail)) return AppResult.failure("Please enter a valid email address.")
        if (password.length < 6) return AppResult.failure("Password must be at least 6 characters.")

        if (localAccounts.findByEmail(normalizedEmail) != null) {
            return AppResult.failure(
                "An account with this email already exists. Try logging in instead.",
            )
        }

        val account = LocalAccountEntity(
            id = newId(),
            name = normalizedName,
            email = normalizedEmail,
            passwordHash = PasswordHasher.hash(password),
            createdAt = Date(nowEpoch()),
        )
        localAccounts.insert(account)

        // Best-effort mirror to Firebase so cloud sync works when it is configured.
        runCatching {
            firebaseAuth?.registerWithEmailPassword(normalizedName, normalizedEmail, password)
        }

        activateAccount(account)
        return AppResult.success(account)
    }

    // ---------------------------------------------------------------- login

    suspend fun login(email: String, password: String): AppResult<LocalAccountEntity> {
        val normalizedEmail = email.trim().lowercase()
        if (normalizedEmail.isEmpty()) return AppResult.failure("Please enter your email.")
        if (password.isEmpty()) return AppResult.failure("Please enter your password.")

        val local = localAccounts.findByEmail(normalizedEmail)
        if (local != null) {
            if (local.provider == AccountProvider.GOOGLE) {
                return AppResult.failure(
                    "This email is signed up with Google. Please use “Continue with Google”.",
                )
            }
            if (!PasswordHasher.verify(password, local.passwordHash)) {
                return AppResult.failure("Incorrect email or password.")
            }
            localAccounts.touchLogin(local.id, nowEpoch())
            // Best-effort Firebase login (keeps cloud credentials aligned).
            runCatching { firebaseAuth?.signInWithEmailPassword(normalizedEmail, password) }
            activateAccount(local)
            return AppResult.success(local)
        }

        // No local account: maybe a legacy Firebase-only user (registered before local auth,
        // with a real Firebase project). Try Firebase, then back-fill a local account.
        val fbResult = runCatching {
            firebaseAuth?.signInWithEmailPassword(normalizedEmail, password)
        }.getOrNull()
        if (fbResult is AppResult.Success) {
            val user = fbResult.value
            val account = LocalAccountEntity(
                id = user.uid,             // keep ids aligned with the cloud account
                name = user.displayName?.takeIf { it.isNotBlank() } ?: normalizedEmail.substringBefore("@"),
                email = normalizedEmail,
                passwordHash = PasswordHasher.hash(password),
                createdAt = Date(nowEpoch()),
                lastLoginAt = Date(nowEpoch()),
            )
            runCatching { localAccounts.insert(account) } // may collide if email registered meanwhile
            activateAccount(account)
            return AppResult.success(account)
        }

        return AppResult.failure("No account found with this email. Create one first.")
    }

    // ---------------------------------------------------------------- google

    /**
     * "Continue with Google" — local-first, same philosophy as [register]/[login]:
     *
     *  - If a local account already exists for the Google email (registered earlier with a
     *    password), it is LINKED to Google: same id, same data, no duplicate account.
     *  - Otherwise a new local account is created with a deterministic id derived from the
     *    Google account id, so the same Google user maps to the same data owner everywhere.
     *  - The Google ID token is best-effort mirrored to Firebase Auth (when configured) so
     *    cloud sync works; being offline never blocks sign-in.
     */
    suspend fun signInWithGoogle(profile: GoogleAuth.GoogleProfile): AppResult<LocalAccountEntity> {
        val email = profile.email.trim().lowercase()
        if (email.isEmpty()) return AppResult.failure("Google did not return an email address.")

        val existing = localAccounts.findByEmail(email)
        val account = if (existing != null) {
            // Link the existing account (keep id + data), refresh provider + avatar.
            localAccounts.updateProvider(existing.id, AccountProvider.GOOGLE, profile.photoUrl)
            existing.copy(provider = AccountProvider.GOOGLE, photoUrl = profile.photoUrl)
        } else {
            val created = LocalAccountEntity(
                id = GoogleAuth.localAccountIdFor(profile.id),
                name = profile.displayName.ifBlank { email.substringBefore("@") },
                email = email,
                passwordHash = "",   // no password login for Google accounts
                provider = AccountProvider.GOOGLE,
                photoUrl = profile.photoUrl,
                createdAt = Date(nowEpoch()),
                lastLoginAt = Date(nowEpoch()),
            )
            val inserted = runCatching { localAccounts.insert(created) }.isSuccess
            if (!inserted) {
                // Extremely unlikely: unique email collided between writes.
                return AppResult.failure("Could not sign you in. Please try again.")
            }
            created
        }

        localAccounts.touchLogin(account.id, nowEpoch())

        // Best-effort mirror to Firebase (enables cloud sync when it is configured).
        runCatching { firebaseAuth?.signInWithGoogleCredential(profile.idToken) }

        activateAccount(account)
        return AppResult.success(account)
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

    fun signOut() {
        runCatching { firebaseAuth?.signOut() }
        session.endSession()
    }

    /** Sign-out that also resets the Credential Manager state (Google account picker). */
    fun signOut(context: android.content.Context) {
        // Fire-and-forget: Credential Manager reset runs off the main thread.
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
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
        if (!PasswordHasher.verify(oldPassword, account.passwordHash)) {
            return AppResult.failure("Current password is incorrect.")
        }
        if (newPassword.length < 6) {
            return AppResult.failure("New password must be at least 6 characters.")
        }
        localAccounts.updatePassword(id, PasswordHasher.hash(newPassword))
        runCatching {
            firebaseAuth?.currentUser?.updatePassword(newPassword)
        }
        return AppResult.success(Unit)
    }

    suspend fun deleteAccount(): AppResult<Unit> {
        val id = session.activeAccountId ?: return AppResult.failure("Not signed in.")
        runCatching { localAccounts.delete(id) }
        runCatching { firebaseAuth?.deleteAccount() }
        onSwitchDataOwner()
        session.dataOwnerId = null
        session.endSession()
        return AppResult.success(Unit)
    }

    /** Email verification only exists for Firebase-mirrored accounts; local ones are always "verified". */
    fun isEmailVerified(): Boolean {
        if (session.isActive && firebaseAuth == null) return true
        return runCatching { firebaseAuth?.currentUser?.isEmailVerified == true }.getOrDefault(true)
    }

    suspend fun sendPasswordReset(email: String): AppResult<Unit> {
        val ds = firebaseAuth ?: return AppResult.failure(
            "Cloud reset is unavailable. Accounts are stored on this device.",
        )
        return ds.sendPasswordResetEmail(email.trim().lowercase())
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
