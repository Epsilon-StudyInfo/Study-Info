package com.studyinfo.app.data.repository

import com.studyinfo.app.data.database.dao.ProgressDao
import com.studyinfo.app.data.database.entity.ProgressEntity
import com.studyinfo.app.data.firebase.FirestoreProgressDataSource
import com.studyinfo.app.domain.model.ChapterState
import com.studyinfo.app.domain.model.ExamType
import com.studyinfo.app.domain.model.Subject
import com.studyinfo.app.domain.model.SyncState
import com.studyinfo.app.utils.newId
import com.studyinfo.app.utils.nowEpoch
import kotlinx.coroutines.flow.Flow
import java.util.Date

class ProgressRepository(
    private val dao: ProgressDao,
    private val remote: FirestoreProgressDataSource?,
) {
    fun observeAll(): Flow<List<ProgressEntity>> = dao.observeAll()
    fun observeBySubject(subject: Subject): Flow<List<ProgressEntity>> = dao.observeBySubject(subject)
    fun observeByExam(examType: ExamType): Flow<List<ProgressEntity>> = dao.observeByExam(examType)

    suspend fun getByChapter(chapterId: String): ProgressEntity? = dao.getByChapter(chapterId)

    suspend fun setPercent(
        chapterId: String,
        subject: Subject,
        examType: ExamType,
        percent: Int,
        chapterState: ChapterState? = null,
    ) {
        val clamped = percent.coerceIn(0, 100)
        val existing = dao.getByChapter(chapterId)
        val now = Date(nowEpoch())
        val resolvedState = chapterState ?: when {
            clamped == 0 -> ChapterState.NOT_STARTED
            clamped >= 100 -> ChapterState.COMPLETED
            existing?.chapterState == ChapterState.COMPLETED -> ChapterState.NEEDS_REVISION
            else -> existing?.chapterState ?: ChapterState.LEARNING
        }
        if (existing == null) {
            dao.upsert(ProgressEntity(
                id = newId(),
                chapterId = chapterId,
                subject = subject,
                examType = examType,
                chapterState = resolvedState,
                percent = clamped,
                createdAt = now,
                updatedAt = now,
                syncState = SyncState.PENDING_CREATE,
            ))
        } else {
            dao.upsert(existing.copy(
                percent = clamped,
                chapterState = resolvedState,
                subject = subject,
                examType = examType,
                updatedAt = now,
                syncState = SyncState.PENDING_UPDATE,
            ))
        }
    }

    suspend fun setChapterState(chapterId: String, state: ChapterState) {
        val existing = dao.getByChapter(chapterId) ?: return
        dao.upsert(existing.copy(chapterState = state, updatedAt = Date(nowEpoch()), syncState = SyncState.PENDING_UPDATE))
    }

    suspend fun incrementAttempted(chapterId: String, correct: Boolean) {
        val existing = dao.getByChapter(chapterId) ?: return
        dao.upsert(existing.copy(
            questionsAttempted = existing.questionsAttempted + 1,
            questionsCorrect = existing.questionsCorrect + (if (correct) 1 else 0),
            updatedAt = Date(nowEpoch()),
            syncState = SyncState.PENDING_UPDATE,
        ))
    }

    suspend fun pushPending(): Int {
        val pending = dao.pendingChanges()
        if (pending.isEmpty()) return 0
        var count = 0
        for (entry in pending) {
            when (entry.syncState) {
                SyncState.PENDING_CREATE, SyncState.PENDING_UPDATE ->
                    if (remote?.put(entry.id, entry) == true) { dao.markSync(entry.id); count++ }
                SyncState.PENDING_DELETE ->
                    if (remote?.delete(entry.id) == true) { dao.delete(entry.id); count++ }
                else -> {}
            }
        }
        return count
    }

    suspend fun pullAll(): Int {
        val remoteList = remote?.fetchAll() ?: emptyList()
        if (remoteList.isNotEmpty()) dao.upsertAll(remoteList)
        return remoteList.size
    }

    suspend fun clear() = dao.clear()
}
