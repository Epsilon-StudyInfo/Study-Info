package com.studyinfo.app.data.firebase

import android.net.Uri
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.studyinfo.app.domain.model.SyncState
import com.studyinfo.app.utils.AppResult
import com.studyinfo.app.utils.fold
import kotlinx.coroutines.tasks.await
import java.util.Date

/**
 * Wraps all Firebase Authentication operations.
 *
 * The app NEVER stores passwords; we rely entirely on Firebase Auth.
 */
class FirebaseAuthDataSource(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) {

    val currentUser: FirebaseUser? get() = auth.currentUser

    fun isSignedIn(): Boolean = auth.currentUser != null

    suspend fun registerWithEmailPassword(
        name: String,
        email: String,
        password: String,
    ): AppResult<FirebaseUser> = try {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user ?: return AppResult.failure("Registration failed")
        // Update profile
        user.updateProfile(
            UserProfileChangeRequest.Builder().setDisplayName(name).build(),
        ).await()
        // Send email verification
        user.sendEmailVerification().await()
        // Create Firestore profile document
        createFirestoreUser(user, displayName = name)
        AppResult.success(user)
    } catch (e: FirebaseAuthUserCollisionException) {
        AppResult.failure("An account with this email already exists.")
    } catch (e: FirebaseAuthInvalidCredentialsException) {
        AppResult.failure("Invalid email or weak password (min 6 characters).")
    } catch (e: Exception) {
        AppResult.failure(e.message ?: "Registration failed.")
    }

    suspend fun signInWithEmailPassword(
        email: String,
        password: String,
    ): AppResult<FirebaseUser> = try {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val user = result.user ?: return AppResult.failure("Login failed")
        updateLastLogin(user)
        AppResult.success(user)
    } catch (e: FirebaseAuthInvalidUserException) {
        AppResult.failure("No account found with this email.")
    } catch (e: FirebaseAuthInvalidCredentialsException) {
        AppResult.failure("Incorrect email or password.")
    } catch (e: Exception) {
        AppResult.failure(e.message ?: "Login failed.")
    }

    suspend fun signInWithGoogleCredential(idToken: String): AppResult<FirebaseUser> = try {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(credential).await()
        val user = result.user ?: return AppResult.failure("Google sign-in failed")
        if (result.additionalUserInfo?.isNewUser == true) {
            createFirestoreUser(user, displayName = user.displayName ?: "")
        } else {
            updateLastLogin(user)
        }
        AppResult.success(user)
    } catch (e: Exception) {
        AppResult.failure(e.message ?: "Google sign-in failed.")
    }

    suspend fun sendPasswordResetEmail(email: String): AppResult<Unit> = try {
        auth.sendPasswordResetEmail(email).await()
        AppResult.success(Unit)
    } catch (e: FirebaseAuthInvalidUserException) {
        AppResult.failure("No account found with this email.")
    } catch (e: Exception) {
        AppResult.failure(e.message ?: "Could not send reset email.")
    }

    suspend fun sendEmailVerification(): AppResult<Unit> = try {
        auth.currentUser?.sendEmailVerification()?.await()
        AppResult.success(Unit)
    } catch (e: Exception) {
        AppResult.failure(e.message ?: "Could not send verification email.")
    }

    suspend fun reload(): AppResult<FirebaseUser> = try {
        auth.currentUser?.reload()?.await()
        AppResult.success(auth.currentUser!!)
    } catch (e: Exception) {
        AppResult.failure(e.message ?: "Could not refresh user.")
    }

    fun signOut() = auth.signOut()

    /**
     * Account deletion: removes the Firebase Auth user, Firestore user-doc + sub-collections,
     * and storage files. Performed in stages - even if Firestore writes succeed but Auth
     * delete fails, we leave a clean state and prompt the user to re-login and retry.
     */
    suspend fun deleteAccount(): AppResult<Unit> {
        val user = auth.currentUser ?: return AppResult.failure("Not signed in.")
        return try {
            val uid = user.uid
            // 1. Firestore user document + sub-collections
            FirestorePaths.deleteUserTree(firestore, uid)

            // 2. Storage tree (best-effort; we use StorageDataSource separately for listing,
            // but a delete-all on the user's prefix is acceptable here)
            // Storage delete is invoked by the caller via FirebaseStorageDataSource.

            // 3. Auth account
            user.delete().await()
            AppResult.success(Unit)
        } catch (e: Exception) {
            AppResult.failure(e.message ?: "Account deletion failed.")
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
        } catch (_: Exception) { /* best-effort */ }
    }
}
