package com.studyinfo.app.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.studyinfo.app.domain.model.SyncState
import java.util.Date

/**
 * Snapshot of the Firebase user profile, cached locally.
 * The Firestore document `users/{uid}` is the source of truth on the cloud side;
 * this row is the local cache used by the UI when offline.
 */
@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val uid: String,
    val displayName: String?,
    val email: String?,
    val photoUrl: String?,
    val emailVerified: Boolean = false,
    val targetExam: String = "JEE Main",
    val preferredLanguage: String = "English",
    val createdAt: Date = Date(),
    val lastLoginAt: Date = Date(),
    val updatedAt: Date = Date(),
    val syncState: SyncState = SyncState.SYNCED,
)

@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey val id: String,
    val name: String,
    val color: String? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val syncState: SyncState = SyncState.SYNCED,
)

/**
 * User-defined custom question sources (e.g. "Coaching Sheet", "NCERT", "Revision Sheet").
 */
@Entity(tableName = "custom_sources")
data class CustomSourceEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val syncState: SyncState = SyncState.SYNCED,
)

/**
 * JEE chapter reference. Stored locally so the form's chapter picker is fast.
 * The `examType` column lets us partition JEE Main vs Advanced chapters.
 */
@Entity(tableName = "chapters")
data class ChapterEntity(
    @PrimaryKey val id: String,
    val subject: String,        // Subject.name
    val name: String,
    val examType: String?,      // ExamType.name or null for "both"
    val chapterNumber: Int = 0,
    val displayOrder: Int = 0,
    val isCustom: Boolean = false,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val syncState: SyncState = SyncState.SYNCED,
)

@Entity(tableName = "topics")
data class TopicEntity(
    @PrimaryKey val id: String,
    val chapterId: String,
    val name: String,
    val displayOrder: Int = 0,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val syncState: SyncState = SyncState.SYNCED,
)

/**
 * Image reference for a question (error or unsolved).
 *
 * The localUri is used while offline; remoteUrl is populated after Firebase Storage upload.
 * `questionRefType` is "error" or "unsolved" and `questionRefId` is the owning question id.
 */
@Entity(tableName = "question_images")
data class QuestionImageEntity(
    @PrimaryKey val id: String,
    val questionRefType: String,
    val questionRefId: String,
    val localUri: String?,        // content:// or file:// - used offline
    val remoteUrl: String?,       // gs:// or https:// URL once uploaded
    val storagePath: String?,     // e.g. users/{uid}/questions/{questionId}/{imageId}
    val uploadedAt: Date? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val syncState: SyncState = SyncState.SYNCED,
)
