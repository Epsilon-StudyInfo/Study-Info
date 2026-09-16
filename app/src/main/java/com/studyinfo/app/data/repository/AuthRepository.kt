package com.studyinfo.app.data.repository

import com.google.firebase.auth.FirebaseUser
import com.studyinfo.app.data.database.dao.UserProfileDao
import com.studyinfo.app.data.database.entity.UserProfileEntity
import com.studyinfo.app.data.firebase.FirebaseAuthDataSource
import com.studyinfo.app.domain.model.SyncState
import com.studyinfo.app.utils.AppResult
import com.studyinfo.app.utils.nowEpoch
import kotlinx.coroutines.flow.Flow
import java.util.Date

class AuthRepository(
    private val auth: FirebaseAuthDataSource,
    private val userProfileDao: UserProfileDao,
) {

    val currentUid: String? get() = auth.currentUser?.uid

    fun observeUserProfile(): Flow<UserProfileEntity?> = userProfileDao.observe()

    fun isSignedIn(): Boolean = auth.isSignedIn()

    suspend fun register(name: String, email: String, password: String): AppResult<FirebaseUser> {
        val result = auth.registerWithEmailPassword(name, email, password)
        if (result is AppResult.Success) {
            cacheUser(result.value, name)
        }
        return result
    }

    suspend fun login(email: String, password: String): AppResult<FirebaseUser> {
        val result = auth.signInWithEmailPassword(email, password)
        if (result is AppResult.Success) {
            cacheUser(result.value, result.value.displayName ?: "")
        }
        return result
    }

    suspend fun signInWithGoogle(idToken: String): AppResult<FirebaseUser> {
        val result = auth.signInWithGoogleCredential(idToken)
        if (result is AppResult.Success) {
            cacheUser(result.value, result.value.displayName ?: "")
        }
        return result
    }

    suspend fun sendPasswordReset(email: String): AppResult<Unit> =
        auth.sendPasswordResetEmail(email)

    suspend fun sendEmailVerification(): AppResult<Unit> = auth.sendEmailVerification()

    suspend fun reloadUser(): AppResult<FirebaseUser> {
        val r = auth.reload()
        if (r is AppResult.Success) cacheUser(r.value, r.value.displayName ?: "")
        return r
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun deleteAccount(): AppResult<Unit> {
        val r = auth.deleteAccount()
        if (r is AppResult.Success) userProfileDao.clear()
        return r
    }

    private suspend fun cacheUser(user: FirebaseUser, displayName: String) {
        val now = Date(nowEpoch())
        val existing = userProfileDao.getById(user.uid)
        val entity = UserProfileEntity(
            uid = user.uid,
            displayName = displayName.ifBlank { user.displayName ?: "" },
            email = user.email,
            photoUrl = user.photoUrl?.toString(),
            emailVerified = user.isEmailVerified,
            targetExam = existing?.targetExam ?: "JEE Main",
            preferredLanguage = existing?.preferredLanguage ?: "English",
            createdAt = existing?.createdAt ?: now,
            lastLoginAt = now,
            updatedAt = now,
            syncState = SyncState.SYNCED,
        )
        userProfileDao.upsert(entity)
    }
}
