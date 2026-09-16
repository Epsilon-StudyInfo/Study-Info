package com.studyinfo.app.data.repository

import com.studyinfo.app.data.database.dao.ReviewDao
import com.studyinfo.app.data.database.entity.ReviewEntity
import com.studyinfo.app.data.firebase.FirestoreReviewsDataSource
import com.studyinfo.app.domain.model.ReviewOutcome
import com.studyinfo.app.domain.model.Subject
import com.studyinfo.app.domain.model.SyncState
import com.studyinfo.app.utils.newId
import com.studyinfo.app.utils.nowEpoch
import kotlinx.coroutines.flow.Flow
import java.util.Date

class ReviewRepository(
    private val dao: ReviewDao,
    private val remote: FirestoreReviewsDataSource?,
) {
    fun observeAll(): Flow<List<ReviewEntity>> = dao.observeAll()
    fun observeForRange(startMs: Long, endMs: Long): Flow<List<ReviewEntity>> =
        dao.observeForRange(startMs, endMs)
    fun observeCount(): Flow<Int> = dao.observeCount()

    suspend fun start(
        subject: Subject? = null,
        notes: String? = null,
    ): ReviewEntity {
        val now = Date(nowEpoch())
        val review = ReviewEntity(
            id = newId(),
            subject = subject,
            startedAt = now,
            endedAt = null,
            notes = notes,
            createdAt = now,
            updatedAt = now,
            syncState = SyncState.PENDING_CREATE,
        )
        dao.upsert(review)
        return review
    }

    suspend fun record(reviewId: String, outcome: ReviewOutcome) {
        val existing = dao.getById(reviewId) ?: return
        val updated = when (outcome) {
            ReviewOutcome.UNDERSTOOD -> existing.copy(understoodCount = existing.understoodCount + 1, reviewedCount = existing.reviewedCount + 1)
            ReviewOutcome.STILL_CONFUSED -> existing.copy(stillConfusedCount = existing.stillConfusedCount + 1, reviewedCount = existing.reviewedCount + 1)
            ReviewOutcome.NEEDS_REVISION -> existing.copy(needsRevisionCount = existing.needsRevisionCount + 1, reviewedCount = existing.reviewedCount + 1)
        }
        dao.upsert(updated.copy(updatedAt = Date(nowEpoch()), syncState = SyncState.PENDING_UPDATE))
    }

    suspend fun end(reviewId: String) {
        val existing = dao.getById(reviewId) ?: return
        dao.upsert(existing.copy(endedAt = Date(nowEpoch()), updatedAt = Date(nowEpoch()), syncState = SyncState.PENDING_UPDATE))
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
