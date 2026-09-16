package com.studyinfo.app.data.database.converter

import androidx.room.TypeConverter
import com.studyinfo.app.domain.model.*
import java.util.Date

/**
 * Centralised Room type converters.
 *
 * Enums are stored as their name (string). Dates are stored as epoch millis.
 */
class Converters {
    @TypeConverter fun fromDate(value: Date?): Long? = value?.time
    @TypeConverter fun toDate(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter fun fromSubject(value: Subject?): String? = value?.name
    @TypeConverter fun toSubject(value: String?): Subject? = value?.let { Subject.fromName(it) }

    @TypeConverter fun fromQuestionSource(value: QuestionSource?): String? = value?.name
    @TypeConverter fun toQuestionSource(value: String?): QuestionSource? =
        value?.let { QuestionSource.fromName(it) }

    @TypeConverter fun fromDifficulty(value: Difficulty?): String? = value?.name
    @TypeConverter fun toDifficulty(value: String?): Difficulty? =
        value?.let { Difficulty.fromLabel(it) }

    @TypeConverter fun fromMistakeType(value: MistakeType?): String? = value?.name
    @TypeConverter fun toMistakeType(value: String?): MistakeType? =
        value?.let { MistakeType.fromLabel(it) }

    @TypeConverter fun fromErrorStatus(value: ErrorStatus?): String? = value?.name
    @TypeConverter fun toErrorStatus(value: String?): ErrorStatus? =
        value?.let { ErrorStatus.fromLabel(it) }

    @TypeConverter fun fromUnsolvedStatus(value: UnsolvedStatus?): String? = value?.name
    @TypeConverter fun toUnsolvedStatus(value: String?): UnsolvedStatus? =
        value?.let { UnsolvedStatus.fromLabel(it) }

    @TypeConverter fun fromTaskStatus(value: TaskStatus?): String? = value?.name
    @TypeConverter fun toTaskStatus(value: String?): TaskStatus? =
        value?.let { TaskStatus.fromLabel(it) }

    @TypeConverter fun fromTaskPriority(value: TaskPriority?): String? = value?.name
    @TypeConverter fun toTaskPriority(value: String?): TaskPriority? =
        value?.let { TaskPriority.fromLabel(it) }

    @TypeConverter fun fromChapterState(value: ChapterState?): String? = value?.name
    @TypeConverter fun toChapterState(value: String?): ChapterState? =
        value?.let { ChapterState.fromLabel(it) }

    @TypeConverter fun fromReviewOutcome(value: ReviewOutcome?): String? = value?.name
    @TypeConverter fun toReviewOutcome(value: String?): ReviewOutcome? =
        value?.let { ReviewOutcome.fromLabel(it) }

    @TypeConverter fun fromExamType(value: ExamType?): String? = value?.name
    @TypeConverter fun toExamType(value: String?): ExamType? =
        value?.let { ExamType.fromLabel(it) }

    @TypeConverter fun fromSyncState(value: SyncState?): String? = value?.name
    @TypeConverter fun toSyncState(value: String?): SyncState? =
        value?.let { SyncState.valueOf(it) }

    /**
     * String list is stored as a single '\u0001'-delimited string (never appears in user text).
     * Null/empty -> null. Used for tags, image URLs, etc.
     */
    @TypeConverter
    fun fromStringList(value: List<String>?): String? =
        value?.takeIf { it.isNotEmpty() }?.joinToString("\u0001")

    @TypeConverter
    fun toStringList(value: String?): List<String>? =
        value?.takeIf { it.isNotEmpty() }?.split("\u0001")
}
