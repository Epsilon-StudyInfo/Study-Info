package com.studyinfo.app.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.studyinfo.app.utils.AppResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import java.io.IOException
import java.util.Date

/**
 * Wraps all Firebase Authentication operations.
 *
 * AUTHORITATIVE provider: whenever this class is wired in, Firebase decides whether a
 * registration / login / deletion succeeds, and its failures are surfaced to the UI.
 *
 * Post-authentication side effects — display-name update, verification email, and the
 * Firestore `users/{uid}` profile document — run OFF the sign-in critical path in a
 * background [sideEffects] scope, each bounded by [SIDE_EFFECT_TIMEOUT_MS]:
 *
 *  - NON-FATAL: by the time they run the auth account already exists, so failing them
 *    would turn a successful sign-in into a reported failure (and a retry would then hit
 *    "email already in use"). The Firestore profile self-heals on the next sign-in via
 *    [ensureFirestoreUser].
 *  - NON-BLOCKING: awaiting a Firestore write inside the sign-in flow used to leave the
 *    UI spinner running forever whenever Firestore was unreachable (the write retries
 *    indefinitely offline) even though Firebase Auth had ALREADY succeeded — the exact
 *    "button keeps loading but I'm logged in after restart" bug. The auth result now
 *    returns as soon as the Firebase Auth call itself completes.
 */
class FirebaseAuthDataSource(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : FirebaseAuthService {

    /** Background scope for post-auth side effects. SupervisorJob: one failed/killed side effect never cancels the others. */
    private val sideEffects = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override val currentUser: FirebaseUserInfo?
        get() = auth.currentUser?.toInfo()

    override fun isSignedIn(): Boolean = auth.currentUser != null

    // ---------------------------------------------------------------- register

    override suspend fun registerWithEmailPassword(
        name: String,
        email: String,
        password: String,
    ): AppResult<FirebaseUserInfo> = try {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user
            ?: return AppResult.failure("Registration failed. Please try again.")

        // Post-auth side effects run in the BACKGROUND: the account already exists, so
        // these must neither fail nor DELAY the reported registration outcome.
        sideEffects.launch {
            // Display name (bounded; non-fatal — the local cache row keeps the entered name
            // and this is retried on later sign-ins).
            runCatching {
                withTimeout(SIDE_EFFECT_TIMEOUT_MS) {
                    user.updateProfile(
                        UserProfileChangeRequest.Builder().setDisplayName(name).build(),
                    ).await()
                }
            }.onFailure { Log.w(TAG, "register: updateProfile failed (non-fatal)", it) }

            // Verification email (non-fatal — the user can request a new one from Settings).
            runCatching {
                withTimeout(SIDE_EFFECT_TIMEOUT_MS) { user.sendEmailVerification().await() }
            }.onFailure { Log.w(TAG, "register: sendEmailVerification failed (non-fatal)", it) }

            // Firestore profile (non-fatal — re-created on the next sign-in).
            runCatching {
                withTimeout(SIDE_EFFECT_TIMEOUT_MS) { createFirestoreUser(user, displayName = name) }
            }.onFailure { Log.w(TAG, "register: Firestore profile creation failed (non-fatal)", it) }
        }

        AppResult.success(user.toInfo())
    } catch (e: FirebaseAuthUserCollisionException) {
        AppResult.failure("An account with this email already exists.")
    } catch (e: FirebaseAuthWeakPasswordException) {
        AppResult.failure("Password must be at least 6 characters.")
    } catch (e: FirebaseAuthInvalidCredentialsException) {
        AppResult.failure("Please enter a valid email address.")
    } catch (e: Exception) {
        AppResult.failure(friendlyMessage(e, "Registration failed."), e)
    }

    // ---------------------------------------------------------------- login

    override suspend fun signInWithEmailPassword(
        email: String,
        password: String,
    ): AppResult<FirebaseUserInfo> = try {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val user = result.user
            ?: return AppResult.failure("Login failed. Please try again.")
        // Background + bounded: profile sync must neither fail nor DELAY the login.
        sideEffects.launch {
            runCatching {
                withTimeout(SIDE_EFFECT_TIMEOUT_MS) { ensureFirestoreUser(user) }
            }.onFailure { Log.w(TAG, "login: Firestore profile sync failed (non-fatal)", it) }
        }
        AppResult.success(user.toInfo())
    } catch (e: FirebaseAuthInvalidUserException) {
        AppResult.failure("No account found with this email.")
    } catch (e: FirebaseAuthInvalidCredentialsException) {
        AppResult.failure("Incorrect email or password.")
    } catch (e: Exception) {
        AppResult.failure(friendlyMessage(e, "Login failed."), e)
    }

    // ---------------------------------------------------------------- google

    override suspend fun signInWithGoogleIdToken(idToken: String): AppResult<FirebaseUserInfo> = try {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(credential).await()
        val user = result.user
            ?: return AppResult.failure("Google sign-in failed. Please try again.")

        // Background + bounded: the Firestore profile must never delay the sign-in result
        // (an unreachable Firestore used to leave the button spinning forever here even
        // though Firebase Auth had already succeeded).
        sideEffects.launch {
            runCatching {
                withTimeout(SIDE_EFFECT_TIMEOUT_MS) {
                    if (result.additionalUserInfo?.isNewUser == true) {
                        createFirestoreUser(user, displayName = user.displayName.orEmpty())
                    } else {
                        updateLastLogin(user)
                    }
                }
            }.onFailure { Log.w(TAG, "google: Firestore profile sync failed (non-fatal)", it) }
        }

        AppResult.success(user.toInfo())
    } catch (e: FirebaseAuthUserCollisionException) {
        // Same email already exists with a different provider (e.g. password).
        // Automatic linking is NOT safe here (the user is not signed in with the other
        // credential), so we explain the situation instead of merging accounts.
        AppResult.failure(
            "An account with this email already exists and uses a different sign-in " +
                "method. Log in with your email and password instead.",
        )
    } catch (e: FirebaseAuthInvalidCredentialsException) {
        val raw = e.message.orEmpty()
        if (raw.contains("expired", ignoreCase = true)) {
            AppResult.failure("Your Google sign-in session expired. Please try again.")
        } else {
            AppResult.failure(
                "Google sign-in was rejected by Firebase. This usually means this APK's " +
                    "SHA-1 fingerprint is not registered in Firebase Console → Project " +
                    "settings → Your apps. See the README for the one-time setup.",
            )
        }
    } catch (e: Exception) {
        AppResult.failure(friendlyMessage(e, "Google sign-in failed."), e)
    }

    // ---------------------------------------------------------------- password

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String,
    ): AppResult<Unit> = try {
        val email = auth.currentUser?.email
            ?: return AppResult.failure("Not signed in.")
        // Re-verify the CURRENT password via a fresh sign-in. This both rejects a wrong
        // "current password" and satisfies Firebase's recent-login requirement.
        auth.signInWithEmailAndPassword(email, oldPassword).await()
        val user = auth.currentUser
            ?: return AppResult.failure("Not signed in.")
        user.updatePassword(newPassword).await()
        AppResult.success(Unit)
    } catch (e: FirebaseAuthWeakPasswordException) {
        AppResult.failure("New password must be at least 6 characters.")
    } catch (e: FirebaseAuthInvalidCredentialsException) {
        AppResult.failure("Current password is incorrect.")
    } catch (e: Exception) {
        AppResult.failure(friendlyMessage(e, "Password change failed."), e)
    }

    override suspend fun sendPasswordResetEmail(email: String): AppResult<Unit> = try {
        auth.sendPasswordResetEmail(email).await()
        AppResult.success(Unit)
    } catch (e: FirebaseAuthInvalidUserException) {
        AppResult.failure("No account found with this email.")
    } catch (e: Exception) {
        AppResult.failure(friendlyMessage(e, "Could not send reset email."), e)
    }

    override suspend fun sendEmailVerification(): AppResult<Unit> = try {
        val user = auth.currentUser
            ?: return AppResult.failure("Not signed in.")
        user.sendEmailVerification().await()
        AppResult.success(Unit)
    } catch (e: Exception) {
        AppResult.failure(friendlyMessage(e, "Could not send verification email."), e)
    }

    override suspend fun reload(): AppResult<FirebaseUserInfo> = try {
        val user = auth.currentUser
            ?: return AppResult.failure("Not signed in.")
        user.reload().await()
        AppResult.success(user.toInfo())
    } catch (e: Exception) {
        AppResult.failure(friendlyMessage(e, "Could not refresh your profile."), e)
    }

    // ---------------------------------------------------------------- account

    override fun signOut() = auth.signOut()

    override suspend fun deleteAccount(): AppResult<Unit> {
        val user = auth.currentUser
            ?: return AppResult.failure("Not signed in.")
        return try {
            // Bounded best-effort cloud cleanup: it must never hang the deletion. If it
            // times out the leftover documents are still protected by the per-user
            // security rules (nobody — not even their owner — can read a deleted user's
            // data without a matching auth uid), so this is safe to abandon.
            runCatching {
                withTimeoutOrNull(CLOUD_CLEANUP_TIMEOUT_MS) {
                    FirestorePaths.deleteUserTree(firestore, user.uid)
                }
            }.onFailure { Log.w(TAG, "deleteAccount: Firestore cleanup failed (non-fatal)", it) }

            // Authoritative deletion of the Auth account.
            user.delete().await()
            AppResult.success(Unit)
        } catch (e: FirebaseAuthInvalidUserException) {
            AppResult.failure("You are not signed in. Please log in and try again.")
        } catch (e: Exception) {
            // ERROR_REQUIRES_RECENT_LOGIN and friends are mapped to a friendly message.
            AppResult.failure(friendlyMessage(e, "Account deletion failed."), e)
        }
    }

    // ---------------------------------------------------------------- helpers

    private fun FirebaseUser.toInfo() = FirebaseUserInfo(
        uid = uid,
        email = email,
        displayName = displayName,
        photoUrl = photoUrl?.toString(),
        emailVerified = isEmailVerified,
    )

    /**
     * Creates the `users/{uid}` document when missing, or refreshes `lastLoginAt` when it
     * exists. Serves as the retry path for a profile creation that failed at registration.
     */
    private suspend fun ensureFirestoreUser(user: FirebaseUser) {
        val ref = firestore.collection("users").document(user.uid)
        val snapshot = runCatching { ref.get().await() }.getOrNull()
        if (snapshot == null || !snapshot.exists()) {
            createFirestoreUser(user, displayName = user.displayName.orEmpty())
        } else {
            updateLastLogin(user)
        }
    }

    private suspend fun createFirestoreUser(user: FirebaseUser, displayName: String) {
        val ref = firestore.collection("users").document(user.uid)
        val data = mapOf(
            "uid" to user.uid,
            "displayName" to displayName,
            "email" to (user.email ?: ""),
            "photoUrl" to (user.photoUrl?.toString() ?: ""),
            "emailVerified" to user.isEmailVerified,
            "targetExam" to "JEE Main",
            "preferredLanguage" to "English",
            "createdAt" to Date(),
            "lastLoginAt" to Date(),
            "settings" to mapOf(
                "theme" to "system",
                "notifications" to true,
                "defaultSubject" to "PHYSICS",
                "defaultDifficulty" to "MEDIUM",
                "dailyReminderTime" to "20:00",
            ),
        )
        ref.set(data).await()
    }

    private suspend fun updateLastLogin(user: FirebaseUser) {
        try {
            firestore.collection("users").document(user.uid)
                .update(mapOf("lastLoginAt" to Date())).await()
        } catch (_: Exception) { /* best-effort, documented above */ }
    }

    /**
     * Maps Firebase Auth exceptions to stable, user-friendly messages.
     * Anything unknown falls back to Firebase's own message (never a generic "success").
     */
    private fun friendlyMessage(e: Exception, fallback: String): String {
        val code = (e as? FirebaseAuthException)?.errorCode.orEmpty()
        val raw = e.message.orEmpty()
        return when {
            code == "ERROR_NETWORK_REQUEST_FAILED" || e is IOException ->
                "Check your internet connection and try again."
            code == "ERROR_TOO_MANY_REQUESTS" ->
                "Too many attempts. Please wait a moment and try again."
            code == "ERROR_OPERATION_NOT_ALLOWED" ->
                "This sign-in method is disabled. Enable it in Firebase Console → " +
                    "Authentication → Sign-in method."
            code == "ERROR_REQUIRES_RECENT_LOGIN" ->
                "Please log in again, then retry this action."
            code == "ERROR_API_KEY_DISABLED" ||
                raw.contains("API key", ignoreCase = true) ||
                raw.contains("DEVELOPER_ERROR", ignoreCase = true) ->
                "Firebase rejected this app's configuration. Check that " +
                    "app/google-services.json matches the Firebase project and that this " +
                    "APK's SHA-1 fingerprint is registered there (see README)."
            else -> raw.ifBlank { fallback }
        }
    }

    private companion object {
        const val TAG = "FirebaseAuthDS"

        /** Per-operation cap for background post-auth side effects (Firestore/Auth profile writes). */
        const val SIDE_EFFECT_TIMEOUT_MS = 20_000L

        /** Cap for the cloud cleanup that precedes account deletion. */
        const val CLOUD_CLEANUP_TIMEOUT_MS = 10_000L
    }
}
