package com.studyinfo.app.data.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.studyinfo.app.domain.model.*
import java.util.Date

/**
 * The Error Book entry.
 *
 * Source-specific fields (dppNumber, pyqYear, moduleName, testName, bookName, customSourceName)
 * are stored as columns rather than a JSON blob so that filtering by them is fast and indexed.
 */
@Entity(
    tableName = "errors",
    indices = [
        Index("subject"),
        Index("source"),
        Index("status"),
        Index("syncState"),
        Index("mistakeType"),
        Index("addedAt"),
        Index("favorite"),
    ],
)
data class ErrorEntryEntity(
    @PrimaryKey val id: String,
    val title: String,
    val questionText: String,
    val subject: Subject,
    val chapterName: String?,
    val topic: String?,
    val source: QuestionSource,
    val sourceRefId: String?,        // FK into custom_sources if source==CUSTOM; else null

    // Source-specific (only the relevant ones populated based on source)
    val dppNumber: String? = null,
    val chapterNumber: String? = null,
    val questionNumber: String? = null,
    val pyqYear: Int? = null,
    val examType: ExamType? = null,
    val shiftSession: String? = null,
    val instituteName: String? = null,
    val moduleNumber: String? = null,
    val exercise: String? = null,
    val testName: String? = null,
    val testNumber: String? = null,
    val testDate: Date? = null,
    val marks: String? = null,
    val bookName: String? = null,
    val customSourceName: String? = null,

    // Error metadata
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val mistakeType: MistakeType = MistakeType.OTHER,
    val attemptedSolution: String? = null,
    val correctSolution: String? = null,
    val explanation: String? = null,
    val lessonLearned: String? = null,
    val personalNotes: String? = null,
    val tags: List<String> = emptyList(),

    // Review / spaced-repetition
    val status: ErrorStatus = ErrorStatus.ACTIVE,
    val favorite: Boolean = false,
    val reviewCount: Int = 0,
    val lastReviewedAt: Date? = null,
    val nextReviewAt: Date? = null,

    // Sync metadata
    val addedAt: Date = Date(),
    val updatedAt: Date = Date(),
    val syncState: SyncState = SyncState.SYNCED,

    // Origin tracking - if this error was migrated from an unsolved question.
    val originUnsolvedId: String? = null,
)

/**
 * Unsolved question entry. Used by DPP / PYQ / Module / Mock / Book / Custom.
 *
 * Same source-specific column approach as errors.
 */
@Entity(
    tableName = "unsolved",
    indices = [
        Index("subject"),
        Index("source"),
        Index("status"),
        Index("syncState"),
        Index("addedAt"),
        Index("favorite"),
        Index("nextRetryAt"),
    ],
)
data class UnsolvedQuestionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val questionText: String,
    val subject: Subject,
    val chapterName: String?,
    val topic: String?,
    val source: QuestionSource,
    val sourceRefId: String?,

    // Source-specific (mirrors errors table)
    val dppNumber: String? = null,
    val chapterNumber: String? = null,
    val questionNumber: String? = null,
    val pyqYear: Int? = null,
    val examType: ExamType? = null,
    val shiftSession: String? = null,
    val instituteName: String? = null,
    val moduleNumber: String? = null,
    val exercise: String? = null,
    val testName: String? = null,
    val testNumber: String? = null,
    val testDate: Date? = null,
    val marks: String? = null,
    val bookName: String? = null,
    val customSourceName: String? = null,

    // Unsolved metadata
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val reasonNotSolved: String? = null,
    val personalNotes: String? = null,
    val tags: List<String> = emptyList(),

    val status: UnsolvedStatus = UnsolvedStatus.UNSOLVED,
    val favorite: Boolean = false,
    val retryCount: Int = 0,
    val lastRetriedAt: Date? = null,
    val nextRetryAt: Date? = null,

    val addedAt: Date = Date(),
    val updatedAt: Date = Date(),
    val syncState: SyncState = SyncState.SYNCED,

    // If this record has been moved to error book, the resulting errorId.
    val movedToErrorId: String? = null,
)
