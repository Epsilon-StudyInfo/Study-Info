package com.studyinfo.app.data.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.studyinfo.app.data.database.entity.*
import com.studyinfo.app.domain.model.*
import java.util.Date

// ---------- Tasks ----------
class FirestoreTasksDataSource(
    db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    uidProvider: () -> String?,
) : FirestoreCollectionDataSource<TaskEntity>(
    db, uidProvider, "tasks",
    { doc -> doc.toTaskEntity() },
    { e -> e.toFirestoreMap() },
)

internal fun DocumentSnapshot.toTaskEntity(): TaskEntity? {
    val data = data ?: return null
    return TaskEntity(
        id = id,
        title = data["title"] as? String ?: "",
        description = data["description"] as? String,
        subject = (data["subject"] as? String)?.let { Subject.fromName(it) },
        chapterName = data["chapterName"] as? String,
        topic = data["topic"] as? String,
        dueDate = (data["dueDate"] as? com.google.firebase.Timestamp)?.toDate() ?: Date(),
        dueTime = (data["dueTime"] as? com.google.firebase.Timestamp)?.toDate(),
        estimatedMinutes = (data["estimatedMinutes"] as? Long)?.toInt(),
        priority = (data["priority"] as? String)?.let { TaskPriority.fromLabel(it) } ?: TaskPriority.MEDIUM,
        status = (data["status"] as? String)?.let { TaskStatus.fromLabel(it) } ?: TaskStatus.PENDING,
        recurrence = data["recurrence"] as? String,
        notes = data["notes"] as? String,
        completedAt = (data["completedAt"] as? com.google.firebase.Timestamp)?.toDate(),
        createdAt = (data["createdAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date(),
        updatedAt = (data["updatedAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date(),
        syncState = SyncState.SYNCED,
    )
}

internal fun TaskEntity.toFirestoreMap(): Map<String, Any?> = mapOf(
    "title" to title,
    "description" to description,
    "subject" to subject?.name,
    "chapterName" to chapterName,
    "topic" to topic,
    "dueDate" to dueDate,
    "dueTime" to dueTime,
    "estimatedMinutes" to estimatedMinutes,
    "priority" to priority.name,
    "status" to status.name,
    "recurrence" to recurrence,
    "notes" to notes,
    "completedAt" to completedAt,
    "createdAt" to createdAt,
    "updatedAt" to updatedAt,
)

// ---------- Reviews ----------
class FirestoreReviewsDataSource(
    db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    uidProvider: () -> String?,
) : FirestoreCollectionDataSource<ReviewEntity>(
    db, uidProvider, "reviews",
    { doc -> doc.toReviewEntity() },
    { e -> e.toFirestoreMap() },
)

internal fun DocumentSnapshot.toReviewEntity(): ReviewEntity? {
    val data = data ?: return null
    return ReviewEntity(
        id = id,
        subject = (data["subject"] as? String)?.let { Subject.fromName(it) },
        startedAt = (data["startedAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date(),
        endedAt = (data["endedAt"] as? com.google.firebase.Timestamp)?.toDate(),
        reviewedCount = (data["reviewedCount"] as? Long)?.toInt() ?: 0,
        understoodCount = (data["understoodCount"] as? Long)?.toInt() ?: 0,
        stillConfusedCount = (data["stillConfusedCount"] as? Long)?.toInt() ?: 0,
        needsRevisionCount = (data["needsRevisionCount"] as? Long)?.toInt() ?: 0,
        notes = data["notes"] as? String,
        createdAt = (data["createdAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date(),
        updatedAt = (data["updatedAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date(),
        syncState = SyncState.SYNCED,
    )
}

internal fun ReviewEntity.toFirestoreMap(): Map<String, Any?> = mapOf(
    "subject" to subject?.name,
    "startedAt" to startedAt,
    "endedAt" to endedAt,
    "reviewedCount" to reviewedCount,
    "understoodCount" to understoodCount,
    "stillConfusedCount" to stillConfusedCount,
    "needsRevisionCount" to needsRevisionCount,
    "notes" to notes,
    "createdAt" to createdAt,
    "updatedAt" to updatedAt,
)

// ---------- Progress ----------
class FirestoreProgressDataSource(
    db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    uidProvider: () -> String?,
) : FirestoreCollectionDataSource<ProgressEntity>(
    db, uidProvider, "progress",
    { doc -> doc.toProgressEntity() },
    { e -> e.toFirestoreMap() },
)

internal fun DocumentSnapshot.toProgressEntity(): ProgressEntity? {
    val data = data ?: return null
    return ProgressEntity(
        id = id,
        chapterId = data["chapterId"] as? String ?: "",
        subject = Subject.fromName(data["subject"] as? String),
        examType = (data["examType"] as? String)?.let { ExamType.fromLabel(it) } ?: ExamType.JEE_MAIN,
        chapterState = (data["chapterState"] as? String)?.let { ChapterState.fromLabel(it) } ?: ChapterState.NOT_STARTED,
        percent = (data["percent"] as? Long)?.toInt() ?: 0,
        questionsAttempted = (data["questionsAttempted"] as? Long)?.toInt() ?: 0,
        questionsCorrect = (data["questionsCorrect"] as? Long)?.toInt() ?: 0,
        notes = data["notes"] as? String,
        updatedAt = (data["updatedAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date(),
        createdAt = (data["createdAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date(),
        syncState = SyncState.SYNCED,
    )
}

internal fun ProgressEntity.toFirestoreMap(): Map<String, Any?> = mapOf(
    "chapterId" to chapterId,
    "subject" to subject.name,
    "examType" to examType.name,
    "chapterState" to chapterState.name,
    "percent" to percent,
    "questionsAttempted" to questionsAttempted,
    "questionsCorrect" to questionsCorrect,
    "notes" to notes,
    "updatedAt" to updatedAt,
    "createdAt" to createdAt,
)

// ---------- Tags ----------
class FirestoreTagsDataSource(
    db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    uidProvider: () -> String?,
) : FirestoreCollectionDataSource<TagEntity>(
    db, uidProvider, "tags",
    { doc -> doc.toTagEntity() },
    { e -> e.toFirestoreMap() },
)

internal fun DocumentSnapshot.toTagEntity(): TagEntity? {
    val data = data ?: return null
    return TagEntity(
        id = id,
        name = data["name"] as? String ?: "",
        color = data["color"] as? String,
        createdAt = (data["createdAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date(),
        updatedAt = (data["updatedAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date(),
        syncState = SyncState.SYNCED,
    )
}

internal fun TagEntity.toFirestoreMap(): Map<String, Any?> = mapOf(
    "name" to name,
    "color" to color,
    "createdAt" to createdAt,
    "updatedAt" to updatedAt,
)

// ---------- Custom Sources ----------
class FirestoreCustomSourcesDataSource(
    db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    uidProvider: () -> String?,
) : FirestoreCollectionDataSource<CustomSourceEntity>(
    db, uidProvider, "customSources",
    { doc -> doc.toCustomSourceEntity() },
    { e -> e.toFirestoreMap() },
)

internal fun DocumentSnapshot.toCustomSourceEntity(): CustomSourceEntity? {
    val data = data ?: return null
    return CustomSourceEntity(
        id = id,
        name = data["name"] as? String ?: "",
        description = data["description"] as? String,
        createdAt = (data["createdAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date(),
        updatedAt = (data["updatedAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date(),
        syncState = SyncState.SYNCED,
    )
}

internal fun CustomSourceEntity.toFirestoreMap(): Map<String, Any?> = mapOf(
    "name" to name,
    "description" to description,
    "createdAt" to createdAt,
    "updatedAt" to updatedAt,
)

// ---------- Chapters / Topics ----------
class FirestoreChaptersDataSource(
    db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    uidProvider: () -> String?,
) : FirestoreCollectionDataSource<ChapterEntity>(
    db, uidProvider, "chapters",
    { doc -> doc.toChapterEntity() },
    { e -> e.toFirestoreMap() },
)

internal fun DocumentSnapshot.toChapterEntity(): ChapterEntity? {
    val data = data ?: return null
    return ChapterEntity(
        id = id,
        subject = data["subject"] as? String ?: "PHYSICS",
        name = data["name"] as? String ?: "",
        examType = data["examType"] as? String,
        chapterNumber = (data["chapterNumber"] as? Long)?.toInt() ?: 0,
        displayOrder = (data["displayOrder"] as? Long)?.toInt() ?: 0,
        isCustom = data["isCustom"] as? Boolean ?: false,
        createdAt = (data["createdAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date(),
        updatedAt = (data["updatedAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date(),
        syncState = SyncState.SYNCED,
    )
}

internal fun ChapterEntity.toFirestoreMap(): Map<String, Any?> = mapOf(
    "subject" to subject,
    "name" to name,
    "examType" to examType,
    "chapterNumber" to chapterNumber,
    "displayOrder" to displayOrder,
    "isCustom" to isCustom,
    "createdAt" to createdAt,
    "updatedAt" to updatedAt,
)

class FirestoreTopicsDataSource(
    db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    uidProvider: () -> String?,
) : FirestoreCollectionDataSource<TopicEntity>(
    db, uidProvider, "topics",
    { doc -> doc.toTopicEntity() },
    { e -> e.toFirestoreMap() },
)

internal fun DocumentSnapshot.toTopicEntity(): TopicEntity? {
    val data = data ?: return null
    return TopicEntity(
        id = id,
        chapterId = data["chapterId"] as? String ?: "",
        name = data["name"] as? String ?: "",
        displayOrder = (data["displayOrder"] as? Long)?.toInt() ?: 0,
        createdAt = (data["createdAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date(),
        updatedAt = (data["updatedAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date(),
        syncState = SyncState.SYNCED,
    )
}

internal fun TopicEntity.toFirestoreMap(): Map<String, Any?> = mapOf(
    "chapterId" to chapterId,
    "name" to name,
    "displayOrder" to displayOrder,
    "createdAt" to createdAt,
    "updatedAt" to updatedAt,
)

// ---------- Question Images (metadata) ----------
class FirestoreQuestionImagesDataSource(
    db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    uidProvider: () -> String?,
) : FirestoreCollectionDataSource<QuestionImageEntity>(
    db, uidProvider, "questionImages",
    { doc -> doc.toQuestionImageEntity() },
    { e -> e.toFirestoreMap() },
)

internal fun DocumentSnapshot.toQuestionImageEntity(): QuestionImageEntity? {
    val data = data ?: return null
    return QuestionImageEntity(
        id = id,
        questionRefType = data["questionRefType"] as? String ?: "error",
        questionRefId = data["questionRefId"] as? String ?: "",
        localUri = data["localUri"] as? String,
        remoteUrl = data["remoteUrl"] as? String,
        storagePath = data["storagePath"] as? String,
        uploadedAt = (data["uploadedAt"] as? com.google.firebase.Timestamp)?.toDate(),
        createdAt = (data["createdAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date(),
        updatedAt = (data["updatedAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date(),
        syncState = SyncState.SYNCED,
    )
}

internal fun QuestionImageEntity.toFirestoreMap(): Map<String, Any?> = mapOf(
    "questionRefType" to questionRefType,
    "questionRefId" to questionRefId,
    "localUri" to localUri,
    "remoteUrl" to remoteUrl,
    "storagePath" to storagePath,
    "uploadedAt" to uploadedAt,
    "createdAt" to createdAt,
    "updatedAt" to updatedAt,
)

// ---------- Streak ----------
class FirestoreStreakDataSource(
    db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    uidProvider: () -> String?,
) : FirestoreCollectionDataSource<StreakActivityEntity>(
    db, uidProvider, "streak",
    { doc -> doc.toStreakEntity() },
    { e -> e.toFirestoreMap() },
)

internal fun DocumentSnapshot.toStreakEntity(): StreakActivityEntity? {
    val data = data ?: return null
    return StreakActivityEntity(
        dateKey = id,
        activityCount = (data["activityCount"] as? Long)?.toInt() ?: 0,
        lastActivityAt = (data["lastActivityAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date(),
        updatedAt = (data["updatedAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date(),
        syncState = SyncState.SYNCED,
    )
}

internal fun StreakActivityEntity.toFirestoreMap(): Map<String, Any?> = mapOf(
    "activityCount" to activityCount,
    "lastActivityAt" to lastActivityAt,
    "updatedAt" to updatedAt,
)
