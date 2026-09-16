package com.studyinfo.app.data.repository

import androidx.test.core.app.ApplicationProvider
import com.studyinfo.app.data.auth.GoogleAuth
import com.studyinfo.app.data.auth.PasswordHasher
import com.studyinfo.app.data.auth.SessionManager
import com.studyinfo.app.data.database.entity.AccountProvider
import com.studyinfo.app.data.database.entity.UserProfileEntity
import com.studyinfo.app.utils.AppResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Firebase-first authentication flows (the authoritative path):
 *
 *  1. email registration succeeds → local account uses the Firebase UID
 *  2. email registration fails → failure returned, no fake success session
 *  3. email login succeeds → local cache/session uses the Firebase UID
 *  4. email login fails → NO local-password fallback
 *  5. stale Room account (legacy local id) is migrated safely to the Firebase UID
 *  6. Google Firebase auth succeeds → local account uses the Firebase UID
 *  7. Google Firebase auth fails → login fails visibly, no local account
 *  8. same email / repeated sign-ins do not create duplicate accounts
 *  9. sign-out clears Firebase + local session and preserves the data owner
 * 10. account deletion requires Firebase success; local cleanup follows
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class AuthRepositoryFirebaseTest {

    private lateinit var store: MutableMap<String, com.studyinfo.app.data.database.entity.LocalAccountEntity>
    private lateinit var profileStore: MutableMap<String, UserProfileEntity>
    private lateinit var session: SessionManager
    private lateinit var firebase: FakeFirebaseAuthService
    private var wipes = 0

    @Before
    fun setUp() {
        store = mutableMapOf()
        profileStore = mutableMapOf()
        session = SessionManager(ApplicationProvider.getApplicationContext())
        firebase = FakeFirebaseAuthService()
        wipes = 0
    }

    private fun repo() = AuthRepository(
        firebaseAuth = firebase,
        localAccounts = fakeLocalAccountDao(store),
        userProfileDao = fakeUserProfileDao(profileStore),
        session = session,
        onSwitchDataOwner = { wipes++ },
    )

    private val googleProfile = GoogleAuth.GoogleProfile(
        id = "google-sub-123",
        email = "JEE.Aspirant@Gmail.com ",
        displayName = "JEE Aspirant",
        photoUrl = "https://lh3.googleusercontent.com/photo",
        idToken = "header.payload.signature",
    )

    // ---------------------------------------------------------------- 1. registration

    @Test
    fun `firebase registration success uses the firebase uid for the local account and session`() = runTest {
        firebase.registerResult = FakeFirebaseAuthService.success("fb-uid-1", "user@example.com", "Firebase User")

        val r = repo().register("User", "user@example.com", "secret123")

        assertTrue(r is AppResult.Success)
        val account = (r as AppResult.Success).value
        assertEquals("fb-uid-1", account.id)                    // Firebase UID is canonical
        assertEquals("user@example.com", account.email)
        assertEquals(AccountProvider.PASSWORD, account.provider)
        assertEquals("fb-uid-1", store["fb-uid-1"]?.id)          // cache row keyed by UID
        assertEquals("fb-uid-1", session.activeAccountId)        // session uses the UID
        assertTrue(session.isActive)
        assertEquals("fb-uid-1", session.dataOwnerId)            // owns the data
        assertEquals(0, wipes)                                   // no wipe on first sign-in
        assertEquals(1, firebase.registerCalls.size)             // Firebase actually called
    }

    @Test
    fun `firebase registration success caches a verifiable non-authoritative fallback credential`() = runTest {
        firebase.registerResult = FakeFirebaseAuthService.success("fb-uid-1", "user@example.com", "User")

        repo().register("User", "user@example.com", "secret123")

        val hash = store["fb-uid-1"]?.passwordHash.orEmpty()
        assertTrue(hash.isNotBlank())
        assertTrue(PasswordHasher.verify("secret123", hash))     // fallback credential synced
    }

    @Test
    fun `firebase registration failure returns the firebase error and creates no session`() = runTest {
        firebase.registerResult = AppResult.failure("An account with this email already exists.")

        val r = repo().register("User", "user@example.com", "secret123")

        assertTrue(r is AppResult.Failure)
        assertEquals("An account with this email already exists.", (r as AppResult.Failure).message)
        assertTrue(store.isEmpty())                              // NO local account created
        assertFalse(session.isActive)                            // NO fake success session
        assertNull(session.activeAccountId)
        assertNull(session.dataOwnerId)
        assertEquals(0, wipes)
    }

    @Test
    fun `firebase registration failure leaves an existing stale row untouched`() = runTest {
        store["local-1"] = localAccount("local-1", "user@example.com")
        firebase.registerResult = AppResult.failure("An account with this email already exists.")

        val r = repo().register("User", "user@example.com", "secret123")

        assertTrue(r is AppResult.Failure)
        assertEquals(1, store.size)                              // stale row unchanged
        assertEquals("local-1", store["local-1"]?.id)
        assertFalse(session.isActive)
    }

    // ---------------------------------------------------------------- 2. login

    @Test
    fun `firebase login success uses the firebase uid for the local cache and session`() = runTest {
        firebase.loginResult = FakeFirebaseAuthService.success("fb-uid-2", "user@example.com", "Firebase User")

        val r = repo().login("user@example.com", "secret123")

        assertTrue(r is AppResult.Success)
        val account = (r as AppResult.Success).value
        assertEquals("fb-uid-2", account.id)                     // Firebase UID, not a local UUID
        assertEquals("fb-uid-2", session.activeAccountId)
        assertEquals("fb-uid-2", session.dataOwnerId)
        assertNotNull(store["fb-uid-2"])                         // cache row created
        assertEquals(1, firebase.loginCalls.size)
    }

    @Test
    fun `firebase login failure shows the real firebase error and never falls back to the local password`() = runTest {
        // A perfectly valid LOCAL account exists (old app version)…
        store["local-1"] = localAccount("local-1", "user@example.com", PasswordHasher.hash("local-password"))
        session.dataOwnerId = "local-1"
        // …but Firebase (the authority) rejects the login.
        firebase.loginResult = AppResult.failure("Incorrect email or password.")

        val r = repo().login("user@example.com", "local-password")

        assertTrue(r is AppResult.Failure)
        assertEquals("Incorrect email or password.", (r as AppResult.Failure).message)
        assertFalse(session.isActive)                            // not signed in
        assertEquals("local-1", store["local-1"]?.id)            // row NOT migrated
        assertEquals("local-1", session.dataOwnerId)             // ownership unchanged
        assertEquals(0, wipes)                                   // no data wipe
    }

    // ---------------------------------------------------------------- 3. stale account migration

    @Test
    fun `stale room account with a legacy local id is migrated to the firebase uid preserving data`() = runTest {
        // Old-version account: local UUID id, owns the Room data.
        store["local-legacy"] = localAccount("local-legacy", "user@example.com", PasswordHasher.hash("old-pass"))
        profileStore["local-legacy"] = UserProfileEntity(
            uid = "local-legacy",
            displayName = "Local User",
            email = "user@example.com",
            photoUrl = null,
            emailVerified = true,
            targetExam = "JEE Advanced",                          // user preference must survive
            preferredLanguage = "English",
            createdAt = java.util.Date(1),
            lastLoginAt = java.util.Date(1),
            updatedAt = java.util.Date(1),
        )
        session.dataOwnerId = "local-legacy"
        firebase.loginResult = FakeFirebaseAuthService.success("fb-uid-9", "user@example.com", "Local User")

        val r = repo().login("user@example.com", "old-pass")

        assertTrue(r is AppResult.Success)
        val account = (r as AppResult.Success).value
        assertEquals("fb-uid-9", account.id)                     // re-keyed to Firebase UID
        assertNull(store["local-legacy"])                        // legacy row replaced
        assertEquals("fb-uid-9", store["fb-uid-9"]?.id)
        assertEquals("fb-uid-9", session.dataOwnerId)            // ownership transferred…
        assertEquals(0, wipes)                                   // …so the data was NOT wiped
        assertEquals("JEE Advanced", profileStore["fb-uid-9"]?.targetExam)  // profile re-keyed
    }

    // ---------------------------------------------------------------- 4. google

    @Test
    fun `google sign in success uses the firebase uid`() = runTest {
        firebase.googleResult = FakeFirebaseAuthService.success("fb-google-7", "jee.aspirant@gmail.com", "JEE Aspirant")

        val r = repo().signInWithGoogle(googleProfile)

        assertTrue(r is AppResult.Success)
        val account = (r as AppResult.Success).value
        assertEquals("fb-google-7", account.id)                  // Firebase UID — NOT a google_hash id
        assertTrue(account.id != "google_" + "anything")
        assertEquals(AccountProvider.GOOGLE, account.provider)
        assertEquals("jee.aspirant@gmail.com", account.email)    // normalized
        assertEquals("fb-google-7", session.activeAccountId)
        assertEquals(1, firebase.googleTokenCalls.size)          // token sent to Firebase
        assertEquals(googleProfile.idToken, firebase.googleTokenCalls.first())
    }

    @Test
    fun `google firebase failure returns the real error and creates no local account`() = runTest {
        firebase.googleResult = AppResult.failure(
            "An account with this email already exists and uses a different sign-in method. " +
                "Log in with your email and password instead.",
        )

        val r = repo().signInWithGoogle(googleProfile)

        assertTrue(r is AppResult.Failure)
        assertTrue(
            (r as AppResult.Failure).message.contains("different sign-in method"),
        )
        assertTrue(store.isEmpty())                              // NO local account
        assertFalse(session.isActive)                            // NOT logged in
        assertEquals(0, wipes)
    }

    // ---------------------------------------------------------------- 5. no duplicates

    @Test
    fun `repeated google sign in with the same email reuses a single local account`() = runTest {
        val repo = repo()
        firebase.googleResult = FakeFirebaseAuthService.success("fb-google-7", "jee.aspirant@gmail.com", "JEE Aspirant")

        repo.signInWithGoogle(googleProfile)
        repo.signOut()
        repo.signInWithGoogle(googleProfile)

        assertEquals(1, store.size)                              // single row, no duplicate
        assertEquals("fb-google-7", store.keys.first())
    }

    @Test
    fun `register then login with the same email keeps a single account row`() = runTest {
        val repo = repo()
        firebase.registerResult = FakeFirebaseAuthService.success("fb-uid-5", "user@example.com", "User")
        firebase.loginResult = FakeFirebaseAuthService.success("fb-uid-5", "user@example.com", "User")

        repo.register("User", "user@example.com", "secret123")
        repo.signOut()
        repo.login("user@example.com", "secret123")

        assertEquals(1, store.size)
        assertEquals("fb-uid-5", store.keys.first())
        assertEquals("fb-uid-5", session.activeAccountId)
    }

    @Test
    fun `google sign in for an email that has a password account surfaces a linking error and keeps the row intact`() = runTest {
        store["pw-1"] = localAccount("pw-1", "jee.aspirant@gmail.com", PasswordHasher.hash("secret123"))
        session.dataOwnerId = "pw-1"
        firebase.googleResult = AppResult.failure(
            "An account with this email already exists and uses a different sign-in method. " +
                "Log in with your email and password instead.",
        )

        val r = repo().signInWithGoogle(googleProfile)

        assertTrue(r is AppResult.Failure)
        assertEquals(1, store.size)                              // no duplicate created
        assertEquals(AccountProvider.PASSWORD, store["pw-1"]?.provider)  // credentials untouched
        assertEquals("pw-1", store["pw-1"]?.id)                  // data owner unchanged
        assertFalse(session.isActive)
        assertEquals(0, wipes)
    }

    // ---------------------------------------------------------------- 6. sign-out

    @Test
    fun `sign out signs out firebase clears the session and preserves the data owner`() = runTest {
        firebase.loginResult = FakeFirebaseAuthService.success("fb-uid-3", "user@example.com", "User")
        val repo = repo()
        repo.login("user@example.com", "secret123")
        assertTrue(session.isActive)

        repo.signOut()

        assertEquals(1, firebase.signOutCalls)                   // Firebase signed out
        assertEquals(null, firebase.currentUser)                 // current user cleared
        assertFalse(session.isActive)                            // local session cleared
        assertNull(session.activeAccountId)
        assertEquals("fb-uid-3", session.dataOwnerId)            // study data PRESERVED
        assertEquals(0, wipes)
        assertNotNull(store["fb-uid-3"])                         // cache row kept
    }

    // ---------------------------------------------------------------- 7. deletion

    @Test
    fun `account deletion fails visibly when firebase deletion fails`() = runTest {
        firebase.loginResult = FakeFirebaseAuthService.success("fb-uid-4", "user@example.com", "User")
        val repo = repo()
        repo.login("user@example.com", "secret123")

        firebase.deleteResult = AppResult.failure("Please log in again, then retry this action.")
        val r = repo.deleteAccount()

        assertTrue(r is AppResult.Failure)                       // no fake success
        assertTrue(session.isActive)                             // session untouched
        assertEquals(1, store.size)                              // local account kept
        assertEquals(1, firebase.deleteCalls)
    }

    @Test
    fun `account deletion succeeds end to end when firebase succeeds`() = runTest {
        firebase.loginResult = FakeFirebaseAuthService.success("fb-uid-4", "user@example.com", "User")
        val repo = repo()
        repo.login("user@example.com", "secret123")

        val r = repo.deleteAccount()

        assertTrue(r is AppResult.Success)
        assertEquals(1, firebase.deleteCalls)                    // Firebase deleted first
        assertTrue(store.isEmpty())                              // local cache removed after
        assertFalse(session.isActive)
        assertNull(session.dataOwnerId)
        assertEquals(1, wipes)                                   // user data wiped
    }

    // ---------------------------------------------------------------- 8. misc semantics

    @Test
    fun `email verification state comes from the firebase user`() = runTest {
        firebase.loginResult = FakeFirebaseAuthService.success("fb-uid-v", "user@example.com", "User", verified = true)
        val repo = repo()
        assertFalse(repo.isEmailVerified())                      // signed out → false

        repo.login("user@example.com", "secret123")
        assertTrue(repo.isEmailVerified())                       // Firebase-verified
    }

    @Test
    fun `password change goes through firebase and refreshes the fallback hash`() = runTest {
        firebase.registerResult = FakeFirebaseAuthService.success("fb-uid-p", "user@example.com", "User")
        val repo = repo()
        repo.register("User", "user@example.com", "old-secret")

        val r = repo.changePassword("old-secret", "new-secret")

        assertTrue(r is AppResult.Success)
        assertEquals(1, firebase.changePasswordCalls.size)       // Firebase verified + changed
        assertEquals("old-secret" to "new-secret", firebase.changePasswordCalls.first())
        assertTrue(PasswordHasher.verify("new-secret", store["fb-uid-p"]?.passwordHash.orEmpty()))
    }

    @Test
    fun `password change surfaces the firebase error for a wrong current password`() = runTest {
        firebase.registerResult = FakeFirebaseAuthService.success("fb-uid-p", "user@example.com", "User")
        val repo = repo()
        repo.register("User", "user@example.com", "old-secret")

        firebase.changePasswordResult = AppResult.failure("Current password is incorrect.")
        val r = repo.changePassword("WRONG", "new-secret")

        assertTrue(r is AppResult.Failure)
        assertEquals("Current password is incorrect.", (r as AppResult.Failure).message)
        assertTrue(PasswordHasher.verify("old-secret", store["fb-uid-p"]?.passwordHash.orEmpty()))
    }

    @Test
    fun `local validation errors short-circuit before firebase is called`() = runTest {
        val repo = repo()

        assertTrue(repo.register("", "user@example.com", "secret123") is AppResult.Failure)
        assertTrue(repo.register("User", "not-an-email", "secret123") is AppResult.Failure)
        assertTrue(repo.register("User", "user@example.com", "123") is AppResult.Failure)
        assertTrue(repo.login("user@example.com", "") is AppResult.Failure)

        assertEquals(0, firebase.registerCalls.size)
        assertEquals(0, firebase.loginCalls.size)
    }
}
