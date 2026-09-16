package com.studyinfo.app.data.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

/**
 * A locally cached account.
 *
 * When Firebase is configured, Firebase Authentication is AUTHORITATIVE: this row is a
 * CACHE of the Firebase user's profile, and [id] is the Firebase UID (the canonical
 * account id — Room, session and Firestore `users/{uid}` all share it). The PBKDF2
 * [passwordHash] is a NON-AUTHORITATIVE fallback credential used only when the app runs
 * without Firebase (placeholder google-services.json); it can never bypass Firebase in a
 * Firebase-configured build because login is Firebase-first.
 *
 * Two sign-in providers are supported:
 *  - [AccountProvider.PASSWORD] — email + password (Firebase `createUserWithEmailAndPassword`).
 *  - [AccountProvider.GOOGLE] — "Continue with Google" (Credential Manager → Firebase).
 *
 * When Firebase is NOT configured the app falls back to LOCAL-ONLY auth: Room + PBKDF2,
 * with a locally generated UUID as [id]. Passwords are stored ONLY as
 * PBKDF2-HMAC-SHA256 hashes with a per-account random salt — never plaintext.
 */
object AccountProvider {
    const val PASSWORD = "PASSWORD"
    const val GOOGLE = "GOOGLE"
}

@Entity(
    tableName = "local_accounts",
    indices = [Index(value = ["email"], unique = true)],
)
data class LocalAccountEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,               // always stored lowercase-trimmed; unique
    val passwordHash: String,        // "salt:hash" hex pair from PasswordHasher ("" for Google accounts)
    val provider: String = AccountProvider.PASSWORD,
    val photoUrl: String? = null,    // Google profile picture (if any)
    val createdAt: Date = Date(),
    val lastLoginAt: Date? = null,
)
