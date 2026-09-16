package com.studyinfo.app.data.repository

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.studyinfo.app.data.database.dao.*
import com.studyinfo.app.data.database.entity.*
import com.studyinfo.app.domain.model.*
import com.studyinfo.app.utils.AppResult
import com.studyinfo.app.utils.nowEpoch
import kotlinx.coroutines.flow.first
import java.util.Date

/**
 * JSON export / import of all user-owned data.
 *
 * Export: pulls every row from every user-owned table, writes to a single JSON file.
 * Dates are serialised as epoch-millis numbers so the round-trip is lossless.
 *
 * Import: validates the schema version, then merges (existing rows with the same id are
 * overwritten rather than duplicated — re-importing a file never spawns duplicates).
 */
class BackupRepository(
    private val userProfileDao: UserProfileDao,
    private val tagDao: TagDao,
    private val customSourceDao: CustomSourceDao,
    private val chapterDao: ChapterDao,
    private val topicDao: TopicDao,
    private val questionImageDao: QuestionImageDao,
    private val errorDao: ErrorDao,
    private val unsolvedDao: UnsolvedDao,
    private val taskDao: TaskDao,
    private val reviewDao: ReviewDao,
    private val progressDao: ProgressDao,
    private val streakDao: StreakDao,
) {

    private val moshi = Moshi.Builder().build()
    private val type = Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java)
    private val adapter = moshi.adapter<Map<String, Any?>>(type).indent("  ")

    companion object {
        const val SCHEMA_VERSION = 1L
    }

    // ---------------------------------------------------------------- export

    suspend fun exportAllTablesForBackup(uid: String): AppResult<String> = try {
        val payload: Map<String, Any?> = mapOf(
            "schemaVersion" to SCHEMA_VERSION,
            "exportedAt" to nowEpoch(),
            "uid" to uid,
            "profile" to userProfileDao.getById(uid)?.toMap(),
            "tags" to tagDao.observeAll().first().map { it.toMap() },
            "customSources" to customSourceDao.observeAll().first().map { it.toMap() },
            "chapters" to chapterDao.getAll().map { it.toMap() },
            "topics" to topicDao.getAll().map { it.toMap() },
            "errors" to errorDao.observeAll().first().map { it.toMap() },
            "unsolved" to unsolvedDao.observeAll().first().map { it.toMap() },
            "tasks" to taskDao.observeAll().first().map { it.toMap() },
            "reviews" to reviewDao.observeAll().first().map { it.toMap() },
            "progress" to progressDao.observeAll().first().map { it.toMap() },
            "streak" to streakDao.observeAll().first().map { it.toMap() },
            "questionImages" to questionImageDao.getAll().map { it.toMap() },
        )
        AppResult.success(adapter.toJson(payload))
    } catch (e: Exception) {
        AppResult.failure("Export failed: ${e.message}", e)
    }

    // ---------------------------------------------------------------- import

    suspend fun importFromJson(json: String): AppResult<ImportSummary> {
        return try {
            val payload = adapter.fromJson(json) ?: return AppResult.failure("Empty backup file")
            val schemaVersion = (payload["schemaVersion"] as? Number)?.toLong() ?: 1L
            if (schemaVersion > SCHEMA_VERSION) {
                return AppResult.failure("Unsupported schema version $schemaVersion. Please update the app.")
            }

            val summary = ImportSummary()

            // Merge by id (upsert) — documented behaviour, re-import never duplicates.
            payload["tags"].asEntityList { it.toTag() }?.let { tagDao.upsertAll(it); summary.tags = it.size }
            payload["customSources"].asEntityList { it.toCustomSource() }
                ?.let { customSourceDao.upsertAll(it); summary.customSources = it.size }
            payload["chapters"].asEntityList { it.toChapter() }
                ?.let { chapterDao.upsertAll(it); summary.chapters = it.size }
            payload["errors"].asEntityList { it.toError() }
                ?.let { errorDao.upsertAll(it); summary.errors = it.size }
            payload["unsolved"].asEntityList { it.toUnsolved() }
                ?.let { unsolvedDao.upsertAll(it); summary.unsolved = it.size }
            payload["tasks"].asEntityList { it.toTask() }
                ?.let { taskDao.upsertAll(it); summary.tasks = it.size }
            payload["reviews"].asEntityList { it.toReview() }
                ?.let { reviewDao.upsertAll(it); summary.reviews = it.size }
            payload["progress"].asEntityList { it.toProgress() }
                ?.let { progressDao.upsertAll(it); summary.progress = it.size }
            payload["streak"].asEntityList { it.toStreak() }
                ?.forEach { streakDao.upsert(it) }
            payload["questionImages"].asEntityList { it.toQuestionImage() }
                ?.let { questionImageDao.upsertAll(it) }

            AppResult.success(summary)
        } catch (e: Exception) {
            AppResult.failure("Import failed: ${e.message}", e)
        }
    }

    // ---------------------------------------------------------------- helpers

    @Suppress("UNCHECKED_CAST")
    private fun <T> Any?.asEntityList(mapper: (Map<String, Any?>) -> T?): List<T>? {
        val list = this as? List<Any?> ?: return null
        return list.mapNotNull { (it as? Map<String, Any?>)?.let(mapper) }
    }

    private fun date(v: Any?): Date? = when (v) {
        null -> null
        is Number -> Date(v.toLong())
        is String -> v.toLongOrNull()?.let { Date(it) }
        else -> null
    }

    private fun long(v: Any?): Long? = (v as? Number)?.toLong() ?: (v as? String)?.toLongOrNull()
    private fun int(v: Any?): Int? = long(v)?.toInt()
    private fun bool(v: Any?, default: Boolean = false): Boolean = v as? Boolean ?: default
    private fun strings(v: Any?): List<String> =
        (v as? List<*>)?.mapNotNull { it as? String } ?: emptyList()

    // ---------- serializers (dates -> epoch millis) ----------

    private fun Date.toMillis(): Long = time

    private fun UserProfileEntity.toMap() = mapOf(
        "uid" to uid, "displayName" to displayName, "email" to email, "photoUrl" to photoUrl,
        "emailVerified" to emailVerified, "targetExam" to targetExam,
        "preferredLanguage" to preferredLanguage,
        "createdAt" to createdAt.toMillis(), "lastLoginAt" to lastLoginAt.toMillis(),
        "updatedAt" to updatedAt.toMillis(),
    )

    private fun TagEntity.toMap() = mapOf(
        "id" to id, "name" to name, "color" to color,
        "createdAt" to createdAt.toMillis(), "updatedAt" to updatedAt.toMillis(),
    )

    private fun CustomSourceEntity.toMap() = mapOf(
        "id" to id, "name" to name, "description" to description,
        "createdAt" to createdAt.toMillis(), "updatedAt" to updatedAt.toMillis(),
    )

    private fun ChapterEntity.toMap() = mapOf(
        "id" to id, "subject" to subject, "name" to name, "examType" to examType,
        "chapterNumber" to chapterNumber, "displayOrder" to displayOrder, "isCustom" to isCustom,
        "createdAt" to createdAt.toMillis(), "updatedAt" to updatedAt.toMillis(),
    )

    private fun TopicEntity.toMap() = mapOf(
        "id" to id, "chapterId" to chapterId, "name" to name, "displayOrder" to displayOrder,
        "createdAt" to createdAt.toMillis(), "updatedAt" to updatedAt.toMillis(),
    )

    private fun ErrorEntryEntity.toMap() = mapOf(
        "id" to id, "title" to title, "questionText" to questionText, "subject" to subject.name,
        "chapterName" to chapterName, "topic" to topic, "source" to source.name, "sourceRefId" to sourceRefId,
        "dppNumber" to dppNumber, "chapterNumber" to chapterNumber, "questionNumber" to questionNumber,
        "pyqYear" to pyqYear, "examType" to examType?.name, "shiftSession" to shiftSession,
        "instituteName" to instituteName, "moduleNumber" to moduleNumber, "exercise" to exercise,
        "testName" to testName, "testNumber" to testNumber, "testDate" to testDate?.toMillis(),
        "marks" to marks, "bookName" to bookName, "customSourceName" to customSourceName,
        "difficulty" to difficulty.name, "mistakeType" to mistakeType.name,
        "attemptedSolution" to attemptedSolution, "correctSolution" to correctSolution,
        "explanation" to explanation, "lessonLearned" to lessonLearned, "personalNotes" to personalNotes,
        "tags" to tags, "status" to status.name, "favorite" to favorite, "reviewCount" to reviewCount,
        "lastReviewedAt" to lastReviewedAt?.toMillis(), "nextReviewAt" to nextReviewAt?.toMillis(),
        "addedAt" to addedAt.toMillis(), "updatedAt" to updatedAt.toMillis(),
        "originUnsolvedId" to originUnsolvedId,
    )

    private fun UnsolvedQuestionEntity.toMap() = mapOf(
        "id" to id, "title" to title, "questionText" to questionText, "subject" to subject.name,
        "chapterName" to chapterName, "topic" to topic, "source" to source.name, "sourceRefId" to sourceRefId,
        "dppNumber" to dppNumber, "chapterNumber" to chapterNumber, "questionNumber" to questionNumber,
        "pyqYear" to pyqYear, "examType" to examType?.name, "shiftSession" to shiftSession,
        "instituteName" to instituteName, "moduleNumber" to moduleNumber, "exercise" to exercise,
        "testName" to testName, "testNumber" to testNumber, "testDate" to testDate?.toMillis(),
        "marks" to marks, "bookName" to bookName, "customSourceName" to customSourceName,
        "difficulty" to difficulty.name, "reasonNotSolved" to reasonNotSolved,
        "personalNotes" to personalNotes, "tags" to tags, "status" to status.name,
        "favorite" to favorite, "retryCount" to retryCount, "lastRetriedAt" to lastRetriedAt?.toMillis(),
        "nextRetryAt" to nextRetryAt?.toMillis(), "addedAt" to addedAt.toMillis(),
        "updatedAt" to updatedAt.toMillis(), "movedToErrorId" to movedToErrorId,
    )

    private fun TaskEntity.toMap() = mapOf(
        "id" to id, "title" to title, "description" to description, "subject" to subject?.name,
        "chapterName" to chapterName, "topic" to topic,
        "dueDate" to dueDate.toMillis(), "dueTime" to dueTime?.toMillis(),
        "estimatedMinutes" to estimatedMinutes, "priority" to priority.name, "status" to status.name,
        "recurrence" to recurrence, "notes" to notes, "completedAt" to completedAt?.toMillis(),
        "createdAt" to createdAt.toMillis(), "updatedAt" to updatedAt.toMillis(),
    )

    private fun ReviewEntity.toMap() = mapOf(
        "id" to id, "subject" to subject?.name, "startedAt" to startedAt.toMillis(),
        "endedAt" to endedAt?.toMillis(), "reviewedCount" to reviewedCount,
        "understoodCount" to understoodCount, "stillConfusedCount" to stillConfusedCount,
        "needsRevisionCount" to needsRevisionCount, "notes" to notes,
        "createdAt" to createdAt.toMillis(), "updatedAt" to updatedAt.toMillis(),
    )

    private fun ProgressEntity.toMap() = mapOf(
        "id" to id, "chapterId" to chapterId, "subject" to subject.name, "examType" to examType.name,
        "chapterState" to chapterState.name, "percent" to percent,
        "questionsAttempted" to questionsAttempted, "questionsCorrect" to questionsCorrect,
        "notes" to notes, "updatedAt" to updatedAt.toMillis(), "createdAt" to createdAt.toMillis(),
    )

    private fun StreakActivityEntity.toMap() = mapOf(
        "dateKey" to dateKey, "activityCount" to activityCount,
        "lastActivityAt" to lastActivityAt.toMillis(), "updatedAt" to updatedAt.toMillis(),
    )

    private fun QuestionImageEntity.toMap() = mapOf(
        "id" to id, "questionRefType" to questionRefType, "questionRefId" to questionRefId,
        "localUri" to localUri, "remoteUrl" to remoteUrl, "storagePath" to storagePath,
        "uploadedAt" to uploadedAt?.toMillis(), "createdAt" to createdAt.toMillis(),
        "updatedAt" to updatedAt.toMillis(),
    )

    // ---------- deserializers ----------

    private fun Map<String, Any?>.toTag(): TagEntity? {
    return TagEntity(
        id = this["id"] as? String ?: return null,
        name = this["name"] as? String ?: "",
        color = this["color"] as? String,
        createdAt = date(this["createdAt"]) ?: Date(),
        updatedAt = date(this["updatedAt"]) ?: Date(),
        syncState = SyncState.PENDING_CREATE,
    )
}

    private fun Map<String, Any?>.toCustomSource(): CustomSourceEntity? {
    return CustomSourceEntity(
        id = this["id"] as? String ?: return null,
        name = this["name"] as? String ?: "",
        description = this["description"] as? String,
        createdAt = date(this["createdAt"]) ?: Date(),
        updatedAt = date(this["updatedAt"]) ?: Date(),
        syncState = SyncState.PENDING_CREATE,
    )
}

    private fun Map<String, Any?>.toChapter(): ChapterEntity? {
    return ChapterEntity(
        id = this["id"] as? String ?: return null,
        subject = this["subject"] as? String ?: "PHYSICS",
        name = this["name"] as? String ?: "",
        examType = this["examType"] as? String,
        chapterNumber = int(this["chapterNumber"]) ?: 0,
        displayOrder = int(this["displayOrder"]) ?: 0,
        isCustom = bool(this["isCustom"]),
        createdAt = date(this["createdAt"]) ?: Date(),
        updatedAt = date(this["updatedAt"]) ?: Date(),
        syncState = SyncState.PENDING_CREATE,
    )
}

    private fun Map<String, Any?>.toError(): ErrorEntryEntity? {
    return ErrorEntryEntity(
        id = this["id"] as? String ?: return null,
        title = this["title"] as? String ?: "",
        questionText = this["questionText"] as? String ?: "",
        subject = Subject.fromName(this["subject"] as? String),
        chapterName = this["chapterName"] as? String,
        topic = this["topic"] as? String,
        source = QuestionSource.fromName(this["source"] as? String),
        sourceRefId = this["sourceRefId"] as? String,
        dppNumber = this["dppNumber"] as? String,
        chapterNumber = this["chapterNumber"] as? String,
        questionNumber = this["questionNumber"] as? String,
        pyqYear = int(this["pyqYear"]),
        examType = (this["examType"] as? String)?.let { ExamType.fromName(it) },
        shiftSession = this["shiftSession"] as? String,
        instituteName = this["instituteName"] as? String,
        moduleNumber = this["moduleNumber"] as? String,
        exercise = this["exercise"] as? String,
        testName = this["testName"] as? String,
        testNumber = this["testNumber"] as? String,
        testDate = date(this["testDate"]),
        marks = this["marks"] as? String,
        bookName = this["bookName"] as? String,
        customSourceName = this["customSourceName"] as? String,
        difficulty = Difficulty.fromName(this["difficulty"] as? String),
        mistakeType = MistakeType.fromName(this["mistakeType"] as? String),
        attemptedSolution = this["attemptedSolution"] as? String,
        correctSolution = this["correctSolution"] as? String,
        explanation = this["explanation"] as? String,
        lessonLearned = this["lessonLearned"] as? String,
        personalNotes = this["personalNotes"] as? String,
        tags = strings(this["tags"]),
        status = ErrorStatus.fromName(this["status"] as? String),
        favorite = bool(this["favorite"]),
        reviewCount = int(this["reviewCount"]) ?: 0,
        lastReviewedAt = date(this["lastReviewedAt"]),
        nextReviewAt = date(this["nextReviewAt"]),
        addedAt = date(this["addedAt"]) ?: Date(),
        updatedAt = date(this["updatedAt"]) ?: Date(),
        syncState = SyncState.PENDING_CREATE,
        originUnsolvedId = this["originUnsolvedId"] as? String,
    )
}

    private fun Map<String, Any?>.toUnsolved(): UnsolvedQuestionEntity? {
    return UnsolvedQuestionEntity(
        id = this["id"] as? String ?: return null,
        title = this["title"] as? String ?: "",
        questionText = this["questionText"] as? String ?: "",
        subject = Subject.fromName(this["subject"] as? String),
        chapterName = this["chapterName"] as? String,
        topic = this["topic"] as? String,
        source = QuestionSource.fromName(this["source"] as? String),
        sourceRefId = this["sourceRefId"] as? String,
        dppNumber = this["dppNumber"] as? String,
        chapterNumber = this["chapterNumber"] as? String,
        questionNumber = this["questionNumber"] as? String,
        pyqYear = int(this["pyqYear"]),
        examType = (this["examType"] as? String)?.let { ExamType.fromName(it) },
        shiftSession = this["shiftSession"] as? String,
        instituteName = this["instituteName"] as? String,
        moduleNumber = this["moduleNumber"] as? String,
        exercise = this["exercise"] as? String,
        testName = this["testName"] as? String,
        testNumber = this["testNumber"] as? String,
        testDate = date(this["testDate"]),
        marks = this["marks"] as? String,
        bookName = this["bookName"] as? String,
        customSourceName = this["customSourceName"] as? String,
        difficulty = Difficulty.fromName(this["difficulty"] as? String),
        reasonNotSolved = this["reasonNotSolved"] as? String,
        personalNotes = this["personalNotes"] as? String,
        tags = strings(this["tags"]),
        status = UnsolvedStatus.fromName(this["status"] as? String),
        favorite = bool(this["favorite"]),
        retryCount = int(this["retryCount"]) ?: 0,
        lastRetriedAt = date(this["lastRetriedAt"]),
        nextRetryAt = date(this["nextRetryAt"]),
        addedAt = date(this["addedAt"]) ?: Date(),
        updatedAt = date(this["updatedAt"]) ?: Date(),
        syncState = SyncState.PENDING_CREATE,
        movedToErrorId = this["movedToErrorId"] as? String,
    )
}

    private fun Map<String, Any?>.toTask(): TaskEntity? {
    return TaskEntity(
        id = this["id"] as? String ?: return null,
        title = this["title"] as? String ?: "",
        description = this["description"] as? String,
        subject = (this["subject"] as? String)?.let { Subject.fromName(it) },
        chapterName = this["chapterName"] as? String,
        topic = this["topic"] as? String,
        dueDate = date(this["dueDate"]) ?: Date(),
        dueTime = date(this["dueTime"]),
        estimatedMinutes = int(this["estimatedMinutes"]),
        priority = TaskPriority.fromName(this["priority"] as? String),
        status = TaskStatus.fromName(this["status"] as? String),
        recurrence = this["recurrence"] as? String,
        notes = this["notes"] as? String,
        completedAt = date(this["completedAt"]),
        createdAt = date(this["createdAt"]) ?: Date(),
        updatedAt = date(this["updatedAt"]) ?: Date(),
        syncState = SyncState.PENDING_CREATE,
    )
}

    private fun Map<String, Any?>.toReview(): ReviewEntity? {
    return ReviewEntity(
        id = this["id"] as? String ?: return null,
        subject = (this["subject"] as? String)?.let { Subject.fromName(it) },
        startedAt = date(this["startedAt"]) ?: Date(),
        endedAt = date(this["endedAt"]),
        reviewedCount = int(this["reviewedCount"]) ?: 0,
        understoodCount = int(this["understoodCount"]) ?: 0,
        stillConfusedCount = int(this["stillConfusedCount"]) ?: 0,
        needsRevisionCount = int(this["needsRevisionCount"]) ?: 0,
        notes = this["notes"] as? String,
        createdAt = date(this["createdAt"]) ?: Date(),
        updatedAt = date(this["updatedAt"]) ?: Date(),
        syncState = SyncState.PENDING_CREATE,
    )
}

    private fun Map<String, Any?>.toProgress(): ProgressEntity? {
    return ProgressEntity(
        id = this["id"] as? String ?: return null,
        chapterId = this["chapterId"] as? String ?: "",
        subject = Subject.fromName(this["subject"] as? String),
        examType = ExamType.fromName(this["examType"] as? String),
        chapterState = ChapterState.fromName(this["chapterState"] as? String),
        percent = int(this["percent"]) ?: 0,
        questionsAttempted = int(this["questionsAttempted"]) ?: 0,
        questionsCorrect = int(this["questionsCorrect"]) ?: 0,
        notes = this["notes"] as? String,
        updatedAt = date(this["updatedAt"]) ?: Date(),
        createdAt = date(this["createdAt"]) ?: Date(),
        syncState = SyncState.PENDING_CREATE,
    )
}

    private fun Map<String, Any?>.toStreak(): StreakActivityEntity? {
    return StreakActivityEntity(
        dateKey = this["dateKey"] as? String ?: return null,
        activityCount = int(this["activityCount"]) ?: 0,
        lastActivityAt = date(this["lastActivityAt"]) ?: Date(),
        updatedAt = date(this["updatedAt"]) ?: Date(),
        syncState = SyncState.PENDING_CREATE,
    )
}

    private fun Map<String, Any?>.toQuestionImage(): QuestionImageEntity? {
    return QuestionImageEntity(
        id = this["id"] as? String ?: return null,
        questionRefType = this["questionRefType"] as? String ?: "error",
        questionRefId = this["questionRefId"] as? String ?: "",
        localUri = this["localUri"] as? String,
        remoteUrl = this["remoteUrl"] as? String,
        storagePath = this["storagePath"] as? String,
        uploadedAt = date(this["uploadedAt"]),
        createdAt = date(this["createdAt"]) ?: Date(),
        updatedAt = date(this["updatedAt"]) ?: Date(),
        syncState = SyncState.PENDING_CREATE,
    )
}
}

data class ImportSummary(
    var tags: Int = 0,
    var customSources: Int = 0,
    var chapters: Int = 0,
    var errors: Int = 0,
    var unsolved: Int = 0,
    var tasks: Int = 0,
    var reviews: Int = 0,
    var progress: Int = 0,
)
