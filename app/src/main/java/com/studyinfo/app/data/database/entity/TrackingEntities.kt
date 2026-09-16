package com.studyinfo.app.data.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.studyinfo.app.domain.model.*
import java.util.Date

@Entity(
    tableName = "tasks",
    indices = [
        Index("dueDate"),
        Index("subject"),
        Index("status"),
        Index("priority"),
        Index("syncState"),
        Index("completedAt"),
    ],
)
data class TaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String? = null,
    val subject: Subject? = null,
    val chapterName: String? = null,
    val topic: String? = null,
    val dueDate: Date,
    val dueTime: Date? = null,
    val estimatedMinutes: Int? = null,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val status: TaskStatus = TaskStatus.PENDING,
    val recurrence: String? = null,        // "DAILY", "WEEKLY", or null
    val notes: String? = null,
    val completedAt: Date? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val syncState: SyncState = SyncState.SYNCED,
)

/**
 * A revision/review session log.
 *
 * One review session may cover multiple questions; the question ids and outcome are stored
 * as a JSON-ish string in `reviewEntries`. We keep a denormalised count for fast charting.
 */
@Entity(
    tableName = "reviews",
    indices = [
        Index("syncState"),
        Index("startedAt"),
        Index("subject"),
    ],
)
data class ReviewEntity(
    @PrimaryKey val id: String,
    val subject: Subject? = null,
    val startedAt: Date,
    val endedAt: Date? = null,
    val reviewedCount: Int = 0,
    val understoodCount: Int = 0,
    val stillConfusedCount: Int = 0,
    val needsRevisionCount: Int = 0,
    val notes: String? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val syncState: SyncState = SyncState.SYNCED,
)

/**
 * Per-chapter progress.
 *
 * `percent` is a manual integer 0..100 set by the user.
 * The chapter itself is referenced by [chapterId] (FK into chapters table).
 */
@Entity(
    tableName = "progress",
    indices = [
        Index("chapterId"),
        Index("examType"),
        Index("syncState"),
    ],
)
data class ProgressEntity(
    @PrimaryKey val id: String,
    val chapterId: String,
    val subject: Subject,
    val examType: ExamType,
    val chapterState: ChapterState = ChapterState.NOT_STARTED,
    val percent: Int = 0,                    // 0..100
    val questionsAttempted: Int = 0,
    val questionsCorrect: Int = 0,
    val notes: String? = null,
    val updatedAt: Date = Date(),
    val createdAt: Date = Date(),
    val syncState: SyncState = SyncState.SYNCED,
)

/**
 * One day's streak activity - used to compute current/longest streak.
 */
@Entity(tableName = "streak_activity")
data class StreakActivityEntity(
    @PrimaryKey val dateKey: String,          // yyyy-MM-dd (local)
    val activityCount: Int = 0,
    val lastActivityAt: Date = Date(),
    val updatedAt: Date = Date(),
    val syncState: SyncState = SyncState.SYNCED,
)

/**
 * Lightweight queue for offline-pending writes that need Firebase upload.
 * This is *only* used for image uploads (the Firestore writes themselves are
 * tracked via each row's [SyncState] column).
 */
@Entity(tableName = "sync_queue")
data class SyncQueueEntity(
    @PrimaryKey val id: String,
    val type: String,                        // "IMAGE_UPLOAD" | "IMAGE_DELETE"
    val refType: String,                     // "error" | "unsolved"
    val refId: String,                       // question id
    val imageId: String?,                    // QuestionImageEntity.id
    val payload: String? = null,             // local uri / remote url depending on type
    val attempts: Int = 0,
    val lastError: String? = null,
    val createdAt: Date = Date(),
    val nextAttemptAt: Date = Date(),
)
