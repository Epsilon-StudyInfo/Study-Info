package com.studyinfo.app.data.repository

import com.studyinfo.app.data.auth.GoogleAuth
import com.studyinfo.app.data.auth.SessionManager
import com.studyinfo.app.data.database.dao.LocalAccountDao
import com.studyinfo.app.data.database.dao.UserProfileDao
import com.studyinfo.app.data.database.entity.AccountProvider
import com.studyinfo.app.data.database.entity.LocalAccountEntity
import com.studyinfo.app.data.database.entity.UserProfileEntity
import com.studyinfo.app.utils.AppResult
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Date

/**
 * Regression tests for "Continue with Google" (local-first sign-in):
 *  - a brand-new Google user gets a local account with a deterministic id,
 *  - an existing password account with the same email is LINKED (id + data preserved),
 *  - password login / changePassword behave correctly for Google-linked accounts.
 */
class GoogleSignInTest {

    private lateinit var store: MutableMap<String, LocalAccountEntity>
    private lateinit var dao: LocalAccountDao
    private lateinit var repo: AuthRepository

    private val profile = GoogleAuth.GoogleProfile(
        id = "google-account-123",
        email = "JEE.Aspirant@Gmail.com ",
        displayName = "JEE Aspirant",
        photoUrl = "https://lh3.googleusercontent.com/photo",
        idToken = "header.payload.signature",
    )

    @Before
    fun setUp() {
        store = mutableMapOf()
        dao = mockk {
            coEvery { insert(any()) } coAnswers {
                val acc = firstArg<LocalAccountEntity>()
                store[acc.id] = acc
            }
            coEvery { findByEmail(any()) } coAnswers {
                store.values.firstOrNull { it.email == firstArg<String>() }
            }
            coEvery { findById(any()) } coAnswers { store[firstArg<String>()] }
            coEvery { updateProvider(any(), any(), any()) } coAnswers {
                val id: String = firstArg()
                val provider: String = secondArg()
                val photo: String? = thirdArg()
                store[id] = requireNotNull(store[id]).copy(provider = provider, photoUrl = photo)
            }
            coEvery { touchLogin(any(), any()) } just Runs
        }
        val profileDao = mockk<UserProfileDao> {
            coEvery { getById(any()) } returns null
            coEvery { upsert(any()) } just Runs
            coEvery { observe() } returns flowOf(null as UserProfileEntity?)
            coEvery { clear() } just Runs
        }
        val session = mockk<SessionManager>(relaxed = true) {
            every { isActive } returns false
        }
        repo = AuthRepository(
            firebaseAuth = null,             // local-only mode — the whole point
            localAccounts = dao,
            userProfileDao = profileDao,
            session = session,
            onSwitchDataOwner = { },
        )
    }

    @Test fun `new google user gets a local account with deterministic id`() = runTest {
        val r = repo.signInWithGoogle(profile)
        assertTrue(r is AppResult.Success)
        val account = (r as AppResult.Success).value
        assertEquals(GoogleAuth.localAccountIdFor(profile.id), account.id)
        assertEquals("jee.aspirant@gmail.com", account.email)      // normalized
        assertEquals(AccountProvider.GOOGLE, account.provider)
        assertEquals("", account.passwordHash)                      // no password login
        assertEquals(1, store.size)
    }

    @Test fun `same google id maps to the same local account id`() {
        val a = GoogleAuth.localAccountIdFor("stable-google-id")
        val b = GoogleAuth.localAccountIdFor("stable-google-id")
        val c = GoogleAuth.localAccountIdFor("other-google-id")
        assertEquals(a, b)
        assertTrue(a != c)
        assertTrue(a.startsWith("google_"))
    }

    @Test fun `existing password account with same email is linked not duplicated`() = runTest {
        val existing = LocalAccountEntity(
            id = "uuid-existing",
            name = "Old Name",
            email = "jee.aspirant@gmail.com",
            passwordHash = "salt:hash",
            provider = AccountProvider.PASSWORD,
        )
        store[existing.id] = existing

        val r = repo.signInWithGoogle(profile)
        assertTrue(r is AppResult.Success)
        val account = (r as AppResult.Success).value
        assertEquals("uuid-existing", account.id)                    // data owner unchanged
        assertEquals(AccountProvider.GOOGLE, store["uuid-existing"]!!.provider)
        assertEquals(profile.photoUrl, store["uuid-existing"]!!.photoUrl)
        assertEquals(1, store.size)                                  // no duplicate row
    }

    @Test fun `password login on a google account shows a helpful error`() = runTest {
        store["g1"] = LocalAccountEntity(
            id = "g1",
            name = "JEE Aspirant",
            email = "jee.aspirant@gmail.com",
            passwordHash = "",
            provider = AccountProvider.GOOGLE,
        )
        val r = repo.login("jee.aspirant@gmail.com", "whatever")
        assertTrue(r is AppResult.Failure)
        assertTrue(
            (r as AppResult.Failure).message.contains("Google", ignoreCase = true),
        )
    }

    @Test fun `changePassword on a google account is rejected`() = runTest {
        val session = mockk<SessionManager>(relaxed = true) {
            every { activeAccountId } returns "g1"
        }
        val repo2 = AuthRepository(null, dao, mockk(relaxed = true), session) { }
        store["g1"] = LocalAccountEntity(
            id = "g1",
            name = "JEE Aspirant",
            email = "jee.aspirant@gmail.com",
            passwordHash = "",
            provider = AccountProvider.GOOGLE,
            lastLoginAt = Date(),
        )
        val r = repo2.changePassword("old", "newpass")
        assertTrue(r is AppResult.Failure)
    }
}
