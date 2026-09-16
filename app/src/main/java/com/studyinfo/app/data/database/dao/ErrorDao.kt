package com.studyinfo.app.data.database.dao

import androidx.room.*
import com.studyinfo.app.data.database.entity.ErrorEntryEntity
import com.studyinfo.app.domain.model.ErrorStatus
import com.studyinfo.app.domain.model.QuestionSource
import com.studyinfo.app.domain.model.Subject
import com.studyinfo.app.domain.model.SyncState
import kotlinx.coroutines.flow.Flow

@Dao
interface ErrorDao {
    @Query("SELECT * FROM errors ORDER BY addedAt DESC")
    fun observeAll(): Flow<List<ErrorEntryEntity>>

    @Query("SELECT * FROM errors WHERE id = :id")
    fun observeById(id: String): Flow<ErrorEntryEntity?>

    @Query("SELECT * FROM errors WHERE id = :id")
    suspend fun getById(id: String): ErrorEntryEntity?

    @Query("SELECT * FROM errors WHERE syncState != :synced")
    suspend fun pendingChanges(synced: SyncState = SyncState.SYNCED): List<ErrorEntryEntity>

    @Query("SELECT * FROM errors WHERE status != :archived AND nextReviewAt IS NOT NULL AND nextReviewAt <= :now ORDER BY nextReviewAt ASC")
    fun observeDueForReview(now: Long, archived: ErrorStatus = ErrorStatus.ARCHIVED): Flow<List<ErrorEntryEntity>>

    @Query("SELECT * FROM errors WHERE subject = :subject ORDER BY addedAt DESC")
    fun observeBySubject(subject: Subject): Flow<List<ErrorEntryEntity>>

    @Query("SELECT * FROM errors WHERE favorite = 1 ORDER BY addedAt DESC")
    fun observeFavorites(): Flow<List<ErrorEntryEntity>>

    @Query("SELECT COUNT(*) FROM errors")
    fun observeCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM errors WHERE subject = :subject")
    fun observeCountBySubject(subject: Subject): Flow<Int>

    @Query("SELECT COUNT(*) FROM errors WHERE mistakeType = :mistake")
    fun observeCountByMistake(mistake: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM errors WHERE status = :status")
    fun observeCountByStatus(status: ErrorStatus): Flow<Int>

    @Query("SELECT * FROM errors WHERE title LIKE '%' || :q || '%' OR questionText LIKE '%' || :q || '%' OR chapterName LIKE '%' || :q || '%' OR topic LIKE '%' || :q || '%' ORDER BY addedAt DESC LIMIT :limit")
    suspend fun search(q: String, limit: Int = 50): List<ErrorEntryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(error: ErrorEntryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(errors: List<ErrorEntryEntity>)

    @Query("UPDATE errors SET syncState = :state WHERE id = :id")
    suspend fun markSync(id: String, state: SyncState = SyncState.SYNCED)

    @Query("UPDATE errors SET favorite = :fav, updatedAt = :now, syncState = :pending WHERE id = :id")
    suspend fun setFavorite(id: String, fav: Boolean, now: Long, pending: SyncState = SyncState.PENDING_UPDATE)

    @Query("UPDATE errors SET status = :status, updatedAt = :now, syncState = :pending WHERE id = :id")
    suspend fun setStatus(id: String, status: ErrorStatus, now: Long, pending: SyncState = SyncState.PENDING_UPDATE)

    @Query("DELETE FROM errors WHERE id = :id")
    suspend fun delete(id: String)

    /** Immediate hard delete (no sync-state flip). Used by SyncManager after a remote delete succeeds. */
    @Query("DELETE FROM errors WHERE id = :id")
    suspend fun hardDelete(id: String)

    @Query("DELETE FROM errors")
    suspend fun clear()
}
