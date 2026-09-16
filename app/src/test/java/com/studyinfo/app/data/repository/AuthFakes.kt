package com.studyinfo.app.data.repository

import com.studyinfo.app.data.auth.SessionManager
import com.studyinfo.app.data.database.dao.LocalAccountDao
import com.studyinfo.app.data.database.dao.UserProfileDao
import com.studyinfo.app.data.database.entity.AccountProvider
import com.studyinfo.app.data.database.entity.LocalAccountEntity
import com.studyinfo.app.data.database.entity.UserProfileEntity
import com.studyinfo.app.data.firebase.FirebaseAuthService
import com.studyinfo.app.data.firebase.FirebaseUserInfo
import com.studyinfo.app.utils.AppResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import java.util.Date

/**
 * Shared in-memory fakes for the AuthRepository unit tests.
 *
 *  - [FakeFirebaseAuthService] — configurable results + call recording for the Firebase contract.
 *  - [fakeLocalAccountDao] / [fakeUserProfileDao] — MockK-backed in-memory maps.
 *  - [newSessionManager] — a REAL SessionManager (Robolectric provides SharedPreferences),
 *    so data-ownership semantics are exercised for real.
 */

/** Hand-written fake of the Firebase auth contract. */
class FakeFirebaseAuthService : FirebaseAuthService {

    var registerResult: AppResult<FirebaseUserInfo> = success("fb-uid", "user@example.com", "User")
    var loginResult: AppResult<FirebaseUserInfo> = success("fb-uid", "user@example.com", "User")
    var googleResult: AppResult<FirebaseUserInfo> = success("fb-google-uid", "user@example.com", "Google User")
    var changePasswordResult: AppResult<Unit> = AppResult.success(Unit)
    var resetResult: AppResult<Unit> = AppResult.success(Unit)
    var verificationResult: AppResult<Unit> = AppResult.success(Unit)
    var reloadResult: AppResult<FirebaseUserInfo> = success("fb-uid", "user@example.com", "User")
    var deleteResult: AppResult<Unit> = AppResult.success(Unit)

    var registerCalls: List<Triple<String, String, String>> = emptyList()
    var loginCalls: List<Pair<String, String>> = emptyList()
    var googleTokenCalls: List<String> = emptyList()
    var changePasswordCalls: List<Pair<String, String>> = emptyList()
    var resetCalls: List<String> = emptyList()
    var deleteCalls = 0
    var signOutCalls = 0

    /** Mirrors real Firebase: signing in replaces the current user; signOut clears it. */
    var currentUserValue: FirebaseUserInfo? = null
        private set

    override val currentUser: FirebaseUserInfo? get() = currentUserValue

    override fun isSignedIn(): Boolean = currentUserValue != null

    override suspend fun registerWithEmailPassword(
        name: String,
        email: String,
        password: String,
    ): AppResult<FirebaseUserInfo> {
        registerCalls += Triple(name, email, password)
        registerResult.let { if (it is AppResult.Success) currentUserValue = it.value }
        return registerResult
    }

    override suspend fun signInWithEmailPassword(
        email: String,
        password: String,
    ): AppResult<FirebaseUserInfo> {
        loginCalls += email to password
        loginResult.let { if (it is AppResult.Success) currentUserValue = it.value }
        return loginResult
    }

    override suspend fun signInWithGoogleIdToken(idToken: String): AppResult<FirebaseUserInfo> {
        googleTokenCalls += idToken
        googleResult.let { if (it is AppResult.Success) currentUserValue = it.value }
        return googleResult
    }

    override suspend fun changePassword(oldPassword: String, newPassword: String): AppResult<Unit> {
        changePasswordCalls += oldPassword to newPassword
        return changePasswordResult
    }

    override suspend fun sendPasswordResetEmail(email: String): AppResult<Unit> {
        resetCalls += email
        return resetResult
    }

    override suspend fun sendEmailVerification(): AppResult<Unit> = verificationResult

    override suspend fun reload(): AppResult<FirebaseUserInfo> = reloadResult

    override fun signOut() {
        signOutCalls++
        currentUserValue = null
    }

    override suspend fun deleteAccount(): AppResult<Unit> {
        deleteCalls++
        if (deleteResult is AppResult.Success) currentUserValue = null
        return deleteResult
    }

    companion object {
        fun success(uid: String, email: String, name: String, verified: Boolean = false) =
            AppResult.success(
                FirebaseUserInfo(
                    uid = uid,
                    email = email,
                    displayName = name,
                    photoUrl = null,
                    emailVerified = verified,
                ),
            )
    }
}

/** In-memory LocalAccountDao. */
fun fakeLocalAccountDao(store: MutableMap<String, LocalAccountEntity>): LocalAccountDao = mockk {
    coEvery { insert(any()) } coAnswers {
        val acc = firstArg<LocalAccountEntity>()
        store[acc.id] = acc
    }
    coEvery { findByEmail(any()) } coAnswers {
        val email = firstArg<String>()
        store.values.firstOrNull { it.email == email }
    }
    coEvery { findById(any()) } coAnswers { store[firstArg<String>()] }
    coEvery { observeAll() } returns flowOf(store.values.toList())
    coEvery { count() } coAnswers { store.size }
    coEvery { rename(any(), any()) } coAnswers {
        val id: String = firstArg()
        store[id]?.let { store[id] = it.copy(name = secondArg()) }
    }
    coEvery { touchLogin(any(), any()) } coAnswers {
        val id: String = firstArg()
        store[id]?.let { store[id] = it.copy(lastLoginAt = Date(secondArg<Long>())) }
    }
    coEvery { updatePassword(any(), any()) } coAnswers {
        val id: String = firstArg()
        store[id]?.let { store[id] = it.copy(passwordHash = secondArg()) }
    }
    coEvery { updateProvider(any(), any(), any()) } coAnswers {
        val id: String = firstArg()
        val provider: String = secondArg()
        val photo: String? = thirdArg()
        store[id]?.let { store[id] = it.copy(provider = provider, photoUrl = photo) }
    }
    coEvery { delete(any()) } coAnswers { store.remove(firstArg<String>()) }
    coEvery { clear() } coAnswers { store.clear() }
}

/** In-memory UserProfileDao. */
fun fakeUserProfileDao(store: MutableMap<String, UserProfileEntity>): UserProfileDao = mockk {
    coEvery { getById(any()) } coAnswers { store[firstArg<String>()] }
    coEvery { upsert(any()) } coAnswers {
        val profile = firstArg<UserProfileEntity>()
        store[profile.uid] = profile
    }
    coEvery { delete(any()) } coAnswers { store.remove(firstArg<String>()) }
    coEvery { observe() } returns flowOf(store.values.firstOrNull())
    coEvery { clear() } coAnswers { store.clear() }
}

/** Convenience factory for a test account row. */
fun localAccount(
    id: String,
    email: String,
    passwordHash: String = "salt:hash",
    provider: String = AccountProvider.PASSWORD,
    name: String = "Local User",
): LocalAccountEntity = LocalAccountEntity(
    id = id,
    name = name,
    email = email,
    passwordHash = passwordHash,
    provider = provider,
    createdAt = Date(1_700_000_000_000),
    lastLoginAt = null,
)
