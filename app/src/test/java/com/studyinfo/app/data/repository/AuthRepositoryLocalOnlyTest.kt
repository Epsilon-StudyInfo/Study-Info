package com.studyinfo.app.data.repository

import androidx.test.core.app.ApplicationProvider
import com.studyinfo.app.data.auth.GoogleAuth
import com.studyinfo.app.data.auth.PasswordHasher
import com.studyinfo.app.data.auth.SessionManager
import com.studyinfo.app.data.database.entity.AccountProvider
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
 * LOCAL-ONLY mode (firebaseAuth == null — the explicit fallback for builds without a
 * real Firebase configuration): Room + PBKDF2 remain fully functional.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class AuthRepositoryLocalOnlyTest {

    private lateinit var store: MutableMap<String, com.studyinfo.app.data.database.entity.LocalAccountEntity>
    private lateinit var profileStore: MutableMap<String, com.studyinfo.app.data.database.entity.UserProfileEntity>
    private lateinit var session: SessionManager
    private var wipes = 0

    @Before
    fun setUp() {
        store = mutableMapOf()
        profileStore = mutableMapOf()
        session = SessionManager(ApplicationProvider.getApplicationContext())
        wipes = 0
    }

    private fun repo() = AuthRepository(
        firebaseAuth = null,          // local-only mode — the whole point
        localAccounts = fakeLocalAccountDao(store),
        userProfileDao = fakeUserProfileDao(profileStore),
        session = session,
        onSwitchDataOwner = { wipes++ },
    )

    @Test
    fun `local registration and login roundtrip works`() = runTest {
        val repo = repo()

        val reg = repo.register("Local User", "local@example.com", "password123")
        assertTrue(reg is AppResult.Success)
        val account = (reg as AppResult.Success).value
        assertTrue(account.id.isNotBlank())
        assertTrue(account.id != "fb-anything")                  // locally generated id
        assertTrue(PasswordHasher.verify("password123", account.passwordHash))
        assertTrue(session.isActive)
        assertEquals(account.id, session.dataOwnerId)

        repo.signOut()
        assertFalse(session.isActive)

        val login = repo.login("LOCAL@EXAMPLE.COM", "password123")  // case-insensitive email
        assertTrue(login is AppResult.Success)
        assertEquals(account.id, (login as AppResult.Success).value.id)
        assertTrue(session.isActive)
    }

    @Test
    fun `local duplicate registration fails`() = runTest {
        val repo = repo()
        repo.register("Local User", "local@example.com", "password123")

        val second = repo.register("Other", "local@example.com", "password456")

        assertTrue(second is AppResult.Failure)
        assertTrue((second as AppResult.Failure).message.contains("already exists"))
        assertEquals(1, store.size)
    }

    @Test
    fun `local login with wrong password fails`() = runTest {
        val repo = repo()
        repo.register("Local User", "local@example.com", "password123")
        repo.signOut()

        val r = repo.login("local@example.com", "WRONG")

        assertTrue(r is AppResult.Failure)
        assertEquals("Incorrect email or password.", (r as AppResult.Failure).message)
        assertFalse(session.isActive)
    }

    @Test
    fun `local login with unknown email fails`() = runTest {
        val r = repo().login("nobody@example.com", "password123")

        assertTrue(r is AppResult.Failure)
        assertTrue((r as AppResult.Failure).message.contains("No account found"))
    }

    @Test
    fun `password login on a google account shows a helpful error`() = runTest {
        store["g1"] = localAccount("g1", "jee.aspirant@gmail.com", "", provider = AccountProvider.GOOGLE)

        val r = repo().login("jee.aspirant@gmail.com", "whatever")

        assertTrue(r is AppResult.Failure)
        assertTrue((r as AppResult.Failure).message.contains("Google", ignoreCase = true))
    }

    @Test
    fun `google sign in without firebase reports a clear configuration error`() = runTest {
        val profile = GoogleAuth.GoogleProfile(
            id = "google-sub-123",
            email = "jee.aspirant@gmail.com",
            displayName = "JEE Aspirant",
            photoUrl = null,
            idToken = "header.payload.signature",
        )

        val r = repo().signInWithGoogle(profile)

        assertTrue(r is AppResult.Failure)
        assertTrue((r as AppResult.Failure).message.contains("requires Firebase"))
        assertTrue(store.isEmpty())
        assertFalse(session.isActive)
    }

    @Test
    fun `changePassword on a google account is rejected`() = runTest {
        session.startSession("g1", "JEE Aspirant", "jee.aspirant@gmail.com")
        store["g1"] = localAccount("g1", "jee.aspirant@gmail.com", "", provider = AccountProvider.GOOGLE)

        val r = repo().changePassword("old", "newpass")

        assertTrue(r is AppResult.Failure)
    }

    @Test
    fun `changePassword locally verifies the old password and updates the hash`() = runTest {
        val repo = repo()
        repo.register("Local User", "local@example.com", "password123")

        val wrong = repo.changePassword("WRONG", "newpass456")
        assertTrue(wrong is AppResult.Failure)

        val right = repo.changePassword("password123", "newpass456")
        assertTrue(right is AppResult.Success)
        assertTrue(PasswordHasher.verify("newpass456", store.values.first().passwordHash))
    }

    @Test
    fun `sign out preserves data owner and cached account`() = runTest {
        val repo = repo()
        val reg = repo.register("Local User", "local@example.com", "password123")
        val id = (reg as AppResult.Success).value.id

        repo.signOut()

        assertFalse(session.isActive)
        assertEquals(id, session.dataOwnerId)
        assertNotNull(store[id])
        assertEquals(0, wipes)
    }

    @Test
    fun `local account deletion clears session and data`() = runTest {
        val repo = repo()
        val reg = repo.register("Local User", "local@example.com", "password123")
        val id = (reg as AppResult.Success).value.id

        val r = repo.deleteAccount()

        assertTrue(r is AppResult.Success)
        assertTrue(store.isEmpty())
        assertFalse(session.isActive)
        assertNull(session.dataOwnerId)
        assertEquals(1, wipes)
    }

    @Test
    fun `signing in with a different local account wipes user data`() = runTest {
        val repo = repo()
        val first = repo.register("First", "first@example.com", "password123")
        val firstId = (first as AppResult.Success).value.id
        repo.signOut()

        val second = repo.register("Second", "second@example.com", "password456")

        assertTrue(second is AppResult.Success)
        assertEquals(1, wipes)                                   // cross-account data wipe
        assertEquals((second as AppResult.Success).value.id, session.dataOwnerId)
    }

    @Test
    fun `email verification is always true for local accounts`() = runTest {
        val repo = repo()
        assertFalse(repo.isEmailVerified())                      // signed out
        repo.register("Local User", "local@example.com", "password123")
        assertTrue(repo.isEmailVerified())                       // local accounts are trusted
    }

    @Test
    fun `password reset explains that cloud reset is unavailable`() = runTest {
        val r = repo().sendPasswordReset("local@example.com")

        assertTrue(r is AppResult.Failure)
        assertTrue((r as AppResult.Failure).message.contains("unavailable"))
    }
}
