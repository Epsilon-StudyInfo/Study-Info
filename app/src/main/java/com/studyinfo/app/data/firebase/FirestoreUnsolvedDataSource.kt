package com.studyinfo.app.data.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.studyinfo.app.data.database.entity.UnsolvedQuestionEntity
import com.studyinfo.app.domain.model.*
import java.util.Date

class FirestoreUnsolvedDataSource(
    db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    uidProvider: () -> String?,
) : FirestoreCollectionDataSource<UnsolvedQuestionEntity>(
    db = db,
    uidProvider = uidProvider,
    collectionName = "unsolved",
    mapper = { doc -> doc.toUnsolvedEntity() },
    serializer = { e -> e.toFirestoreMap() },
)

internal fun DocumentSnapshot.toUnsolvedEntity(): UnsolvedQuestionEntity? {
    val data = data ?: return null
    return UnsolvedQuestionEntity(
        id = id,
        title = data["title"] as? String ?: "",
        questionText = data["questionText"] as? String ?: "",
        subject = Subject.fromName(data["subject"] as? String),
        chapterName = data["chapterName"] as? String,
        topic = data["topic"] as? String,
        source = QuestionSource.fromName(data["source"] as? String),
        sourceRefId = data["sourceRefId"] as? String,
        dppNumber = data["dppNumber"] as? String,
        chapterNumber = data["chapterNumber"] as? String,
        questionNumber = data["questionNumber"] as? String,
        pyqYear = (data["pyqYear"] as? Long)?.toInt(),
        examType = (data["examType"] as? String)?.let { ExamType.fromName(it) },
        shiftSession = data["shiftSession"] as? String,
        instituteName = data["instituteName"] as? String,
        moduleNumber = data["moduleNumber"] as? String,
        exercise = data["exercise"] as? String,
        testName = data["testName"] as? String,
        testNumber = data["testNumber"] as? String,
        testDate = (data["testDate"] as? com.google.firebase.Timestamp)?.toDate(),
        marks = data["marks"] as? String,
        bookName = data["bookName"] as? String,
        customSourceName = data["customSourceName"] as? String,
        difficulty = Difficulty.fromName(data["difficulty"] as? String),
        reasonNotSolved = data["reasonNotSolved"] as? String,
        personalNotes = data["personalNotes"] as? String,
        tags = (data["tags"] as? List<*>)?.mapNotNull { it as? String } ?: emptyList(),
        status = UnsolvedStatus.fromName(data["status"] as? String),
        favorite = data["favorite"] as? Boolean ?: false,
        retryCount = (data["retryCount"] as? Long)?.toInt() ?: 0,
        lastRetriedAt = (data["lastRetriedAt"] as? com.google.firebase.Timestamp)?.toDate(),
        nextRetryAt = (data["nextRetryAt"] as? com.google.firebase.Timestamp)?.toDate(),
        addedAt = (data["addedAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date(),
        updatedAt = (data["updatedAt"] as? com.google.firebase.Timestamp)?.toDate() ?: Date(),
        syncState = SyncState.SYNCED,
        movedToErrorId = data["movedToErrorId"] as? String,
    )
}

internal fun UnsolvedQuestionEntity.toFirestoreMap(): Map<String, Any?> = mapOf(
    "title" to title,
    "questionText" to questionText,
    "subject" to subject.name,
    "chapterName" to chapterName,
    "topic" to topic,
    "source" to source.name,
    "sourceRefId" to sourceRefId,
    "dppNumber" to dppNumber,
    "chapterNumber" to chapterNumber,
    "questionNumber" to questionNumber,
    "pyqYear" to pyqYear,
    "examType" to examType?.name,
    "shiftSession" to shiftSession,
    "instituteName" to instituteName,
    "moduleNumber" to moduleNumber,
    "exercise" to exercise,
    "testName" to testName,
    "testNumber" to testNumber,
    "testDate" to testDate,
    "marks" to marks,
    "bookName" to bookName,
    "customSourceName" to customSourceName,
    "difficulty" to difficulty.name,
    "reasonNotSolved" to reasonNotSolved,
    "personalNotes" to personalNotes,
    "tags" to tags,
    "status" to status.name,
    "favorite" to favorite,
    "retryCount" to retryCount,
    "lastRetriedAt" to lastRetriedAt,
    "nextRetryAt" to nextRetryAt,
    "addedAt" to addedAt,
    "updatedAt" to updatedAt,
    "movedToErrorId" to movedToErrorId,
)
