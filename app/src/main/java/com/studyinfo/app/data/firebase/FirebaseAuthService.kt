package com.studyinfo.app.data.firebase

import com.studyinfo.app.utils.AppResult

/**
 * Immutable snapshot of the Firebase user — the only user fields PrepVault needs.
 * Detached from `com.google.firebase.auth.FirebaseUser` so repositories and tests
 * can work with a plain value type.
 */
data class FirebaseUserInfo(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String?,
    val emailVerified: Boolean,
)

/**
 * Contract for Firebase Authentication.
 *
 * Whenever an implementation of this service is present, Firebase Authentication is the
 * AUTHORITATIVE identity provider:
 *  - every method reports Firebase's real outcome — failures are returned, never swallowed;
 *  - [FirebaseUserInfo.uid] is the canonical account id used by Room, the local session
 *    and Firestore (`users/{uid}`);
 *  - the local `local_accounts` Room table is a CACHE of profile data, never a second
 *    authentication server.
 *
 * Post-authentication side effects (display-name update, verification email, Firestore
 * profile document) are intentionally NON-FATAL inside the implementation: by the time
 * they run the auth account already exists, so failing them would report a successful
 * registration as failed and cause an "email already in use" error on retry. Each such
 * swallow is logged and documented in the implementation.
 */
interface FirebaseAuthService {

    /** Snapshot of the currently signed-in Firebase user, or null when signed out. */
    val currentUser: FirebaseUserInfo?

    fun isSignedIn(): Boolean

    /**
     * Creates the Firebase account. Fails with a user-friendly message when Firebase
     * rejects the registration (duplicate email, weak password, network, …) — the caller
     * MUST treat that as a failed registration.
     */
    suspend fun registerWithEmailPassword(
        name: String,
        email: String,
        password: String,
    ): AppResult<FirebaseUserInfo>

    /**
     * Signs in with email + password. A failure means Firebase rejected the credentials —
     * there is deliberately no local fallback.
     */
    suspend fun signInWithEmailPassword(
        email: String,
        password: String,
    ): AppResult<FirebaseUserInfo>

    /**
     * Signs in with a Google ID token (obtained via Android Credential Manager) using
     * `GoogleAuthProvider.getCredential(...)` + `signInWithCredential(...)`.
     * A failure means Firebase rejected the Google credential — the caller MUST NOT
     * report a successful login.
     */
    suspend fun signInWithGoogleIdToken(idToken: String): AppResult<FirebaseUserInfo>

    /**
     * Changes the password of the signed-in user. Re-verifies the current password via
     * Firebase (which also satisfies Firebase's recent-login requirement).
     */
    suspend fun changePassword(oldPassword: String, newPassword: String): AppResult<Unit>

    suspend fun sendPasswordResetEmail(email: String): AppResult<Unit>

    suspend fun sendEmailVerification(): AppResult<Unit>

    /** Re-fetches the user's profile from Firebase (display name, verified flag, …). */
    suspend fun reload(): AppResult<FirebaseUserInfo>

    fun signOut()

    /**
     * Deletes the Firebase Auth account (authoritative). Cloud data cleanup is
     * best-effort; a failure to delete the AUTH user is returned as a failure.
     */
    suspend fun deleteAccount(): AppResult<Unit>
}
