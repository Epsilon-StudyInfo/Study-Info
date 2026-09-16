package com.studyinfo.app.data.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

/**
 * A locally-stored account.
 *
 * PrepVault is offline-first: Room is the source of truth, so accounts live here too.
 * Passwords are stored ONLY as PBKDF2-HMAC-SHA256 hashes with a per-account random salt —
 * the plaintext password is never persisted anywhere.
 *
 * When the developer configures a real Firebase project, the same credentials are also
 * (best-effort) mirrored to Firebase Auth so cloud sync keeps working; but the app is fully
 * functional with local accounts alone.
 */
@Entity(
    tableName = "local_accounts",
    indices = [Index(value = ["email"], unique = true)],
)
data class LocalAccountEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,               // always stored lowercase-trimmed; unique
    val passwordHash: String,        // "salt:hash" hex pair from PasswordHasher
    val createdAt: Date = Date(),
    val lastLoginAt: Date? = null,
)
