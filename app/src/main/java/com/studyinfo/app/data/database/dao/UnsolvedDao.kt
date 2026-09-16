package com.studyinfo.app.data.database.dao

import androidx.room.*
import com.studyinfo.app.data.database.entity.UnsolvedQuestionEntity
import com.studyinfo.app.domain.model.SyncState
import com.studyinfo.app.domain.model.UnsolvedStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface UnsolvedDao {
    @Query("SELECT * FROM unsolved ORDER BY addedAt DESC")
    fun observeAll(): Flow<List<UnsolvedQuestionEntity>>

    @Query("SELECT * FROM unsolved WHERE source = :source ORDER BY addedAt DESC")
    fun observeBySource(source: String): Flow<List<UnsolvedQuestionEntity>>

    @Query("SELECT * FROM unsolved WHERE id = :id")
    fun observeById(id: String): Flow<UnsolvedQuestionEntity?>

    @Query("SELECT * FROM unsolved WHERE id = :id")
    suspend fun getById(id: String): UnsolvedQuestionEntity?

    @Query("SELECT * FROM unsolved WHERE syncState != :synced")
    suspend fun pendingChanges(synced: SyncState = SyncState.SYNCED): List<UnsolvedQuestionEntity>

    @Query("SELECT * FROM unsolved WHERE status != :skip AND status != :solved AND nextRetryAt IS NOT NULL AND nextRetryAt <= :now ORDER BY nextRetryAt ASC")
    fun observeRetryQueue(
        now: Long,
        skip: UnsolvedStatus = UnsolvedStatus.SKIP_PERMANENTLY,
        solved: UnsolvedStatus = UnsolvedStatus.SOLVED,
    ): Flow<List<UnsolvedQuestionEntity>>

    @Query("SELECT COUNT(*) FROM unsolved WHERE status = :status")
    fun observeCountByStatus(status: UnsolvedStatus): Flow<Int>

    @Query("SELECT COUNT(*) FROM unsolved WHERE source = :source")
    fun observeCountBySource(source: String): Flow<Int>

    @Query("SELECT * FROM unsolved WHERE title LIKE '%' || :q || '%' OR questionText LIKE '%' || :q || '%' OR chapterName LIKE '%' || :q || '%' ORDER BY addedAt DESC LIMIT :limit")
    suspend fun search(q: String, limit: Int = 50): List<UnsolvedQuestionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: UnsolvedQuestionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(entries: List<UnsolvedQuestionEntity>)

    @Query("UPDATE unsolved SET syncState = :state WHERE id = :id")
    suspend fun markSync(id: String, state: SyncState = SyncState.SYNCED)

    @Query("UPDATE unsolved SET status = :status, updatedAt = :now, syncState = :pending WHERE id = :id")
    suspend fun setStatus(id: String, status: UnsolvedStatus, now: Long, pending: SyncState = SyncState.PENDING_UPDATE)

    @Query("UPDATE unsolved SET favorite = :fav, updatedAt = :now, syncState = :pending WHERE id = :id")
    suspend fun setFavorite(id: String, fav: Boolean, now: Long, pending: SyncState = SyncState.PENDING_UPDATE)

    @Query("UPDATE unsolved SET movedToErrorId = :errorId, status = :status, updatedAt = :now, syncState = :pending WHERE id = :id")
    suspend fun markMovedToError(id: String, errorId: String?, now: Long, status: UnsolvedStatus = UnsolvedStatus.SOLVED, pending: SyncState = SyncState.PENDING_UPDATE)

    @Query("DELETE FROM unsolved WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM unsolved")
    suspend fun clear()
}
