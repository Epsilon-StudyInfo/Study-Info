package com.studyinfo.app.data.repository

import com.studyinfo.app.data.database.dao.ErrorDao
import com.studyinfo.app.data.database.entity.ErrorEntryEntity
import com.studyinfo.app.data.firebase.FirestoreErrorsDataSource
import com.studyinfo.app.domain.model.*
import com.studyinfo.app.utils.newId
import com.studyinfo.app.utils.nowEpoch
import kotlinx.coroutines.flow.Flow
import java.util.Date

/**
 * Repository for the Error Book.
 *
 * Every mutating method writes through to Room with a [SyncState] that marks the row as
 * pending remote sync. The SyncManager worker periodically calls [pushPending] / [pullAll].
 *
 * The UI never touches Firebase directly - all Firebase calls go through this class.
 */
class ErrorRepository(
    private val dao: ErrorDao,
    private val remote: FirestoreErrorsDataSource,
) {

    // ---------- Observers ----------
    fun observeAll(): Flow<List<ErrorEntryEntity>> = dao.observeAll()
    fun observeById(id: String): Flow<ErrorEntryEntity?> = dao.observeById(id)
    fun observeBySubject(subject: Subject): Flow<List<ErrorEntryEntity>> = dao.observeBySubject(subject)
    fun observeFavorites(): Flow<List<ErrorEntryEntity>> = dao.observeFavorites()
    fun observeCount(): Flow<Int> = dao.observeCount()
    fun observeCountBySubject(subject: Subject): Flow<Int> = dao.observeCountBySubject(subject)
    fun observeCountByMistake(mistake: String): Flow<Int> = dao.observeCountByMistake(mistake)
    fun observeCountByStatus(status: ErrorStatus): Flow<Int> = dao.observeCountByStatus(status)
    fun observeDueForReview(): Flow<List<ErrorEntryEntity>> =
        dao.observeDueForReview(System.currentTimeMillis())

    // ---------- Direct access ----------
    suspend fun getById(id: String): ErrorEntryEntity? = dao.getById(id)

    suspend fun search(query: String, limit: Int = 50): List<ErrorEntryEntity> =
        dao.search(query, limit)

    // ---------- Mutations ----------
    suspend fun add(entry: ErrorEntryEntity): ErrorEntryEntity {
        val now = Date(nowEpoch())
        val withMeta = entry.copy(
            id = entry.id.ifBlank { newId() },
            addedAt = now,
            updatedAt = now,
            syncState = SyncState.PENDING_CREATE,
        )
        dao.upsert(withMeta)
        return withMeta
    }

    suspend fun update(entry: ErrorEntryEntity) {
        dao.upsert(entry.copy(updatedAt = Date(nowEpoch()), syncState = SyncState.PENDING_UPDATE))
    }

    suspend fun setFavorite(id: String, fav: Boolean) =
        dao.setFavorite(id, fav, nowEpoch())

    suspend fun setStatus(id: String, status: ErrorStatus) =
        dao.setStatus(id, status, nowEpoch())

    /**
     * Record a review pass for the error.
     *
     * - Increments reviewCount, sets lastReviewedAt, optionally schedules nextReviewAt
     *   based on the outcome (understood = longer interval, needs_revision = shorter).
     * - If outcome is UNDERSTOOD, status moves to UNDERSTOOD.
     */
    suspend fun recordReview(
        id: String,
        outcome: ReviewOutcome,
        reflection: String? = null,
        scheduleDays: Int? = null,
    ) {
        val existing = dao.getById(id) ?: return
        val now = Date(nowEpoch())
        val nextReview = scheduleDays?.let { Date(nowEpoch() + it * 24L * 60L * 60L * 1000L) }
            ?: outcome.defaultNextReview(now)
        val newStatus = when (outcome) {
            ReviewOutcome.UNDERSTOOD -> ErrorStatus.UNDERSTOOD
            ReviewOutcome.NEEDS_REVISION -> ErrorStatus.NEEDS_REVISION
            ReviewOutcome.STILL_CONFUSED -> ErrorStatus.ACTIVE
        }
        val newNotes = if (reflection.isNullOrBlank()) existing.personalNotes
        else listOfNotNull(existing.personalNotes, "[Review] $reflection").joinToString("\n")
        dao.upsert(
            existing.copy(
                reviewCount = existing.reviewCount + 1,
                lastReviewedAt = now,
                nextReviewAt = nextReview,
                status = newStatus,
                personalNotes = newNotes,
                updatedAt = now,
                syncState = SyncState.PENDING_UPDATE,
            ),
        )
    }

    suspend fun delete(id: String) {
        val existing = dao.getById(id) ?: return
        dao.upsert(existing.copy(updatedAt = Date(nowEpoch()), syncState = SyncState.PENDING_DELETE))
    }

    suspend fun hardDelete(id: String) = dao.delete(id)

    // ---------- Sync ----------
    suspend fun pushPending(): Int {
        val pending = dao.pendingChanges()
        if (pending.isEmpty()) return 0
        var count = 0
        for (entry in pending) {
            when (entry.syncState) {
                SyncState.PENDING_CREATE, SyncState.PENDING_UPDATE ->
                    if (remote.put(entry.id, entry)) { dao.markSync(entry.id); count++ }
                SyncState.PENDING_DELETE ->
                    if (remote.delete(entry.id)) { dao.hardDelete(id = entry.id); count++ }
                else -> {}
            }
        }
        return count
    }

    suspend fun pullAll(): Int {
        val remoteList = remote.fetchAll()
        if (remoteList.isNotEmpty()) dao.upsertAll(remoteList)
        return remoteList.size
    }

    suspend fun clear() = dao.clear()

    // ---------- Helpers ----------
    private fun ReviewOutcome.defaultNextReview(now: Date): Date {
        val days = when (this) {
            ReviewOutcome.UNDERSTOOD -> 30
            ReviewOutcome.NEEDS_REVISION -> 3
            ReviewOutcome.STILL_CONFUSED -> 1
        }
        return Date(now.time + days * 24L * 60L * 60L * 1000L)
    }
}
