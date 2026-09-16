package com.studyinfo.app.data.database.dao

import androidx.room.*
import com.studyinfo.app.data.database.entity.ReviewEntity
import com.studyinfo.app.data.database.entity.StreakActivityEntity
import com.studyinfo.app.data.database.entity.SyncQueueEntity
import com.studyinfo.app.data.database.entity.TaskEntity
import com.studyinfo.app.domain.model.SyncState
import com.studyinfo.app.domain.model.TaskStatus
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY dueDate ASC, priority DESC")
    fun observeAll(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE dueDate BETWEEN :start AND :end ORDER BY dueDate ASC, priority DESC")
    fun observeForRange(start: Long, end: Long): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE dueDate < :now AND status != :completed AND status != :skipped ORDER BY dueDate ASC")
    fun observeOverdue(now: Long, completed: TaskStatus = TaskStatus.COMPLETED, skipped: TaskStatus = TaskStatus.SKIPPED): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE status = :status ORDER BY completedAt DESC")
    fun observeByStatus(status: TaskStatus): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun observeById(id: String): Flow<TaskEntity?>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getById(id: String): TaskEntity?

    @Query("SELECT COUNT(*) FROM tasks WHERE dueDate BETWEEN :start AND :end AND status = :status")
    fun observeCompletedCountForRange(start: Long, end: Long, status: TaskStatus = TaskStatus.COMPLETED): Flow<Int>

    @Query("SELECT COUNT(*) FROM tasks WHERE dueDate BETWEEN :start AND :end")
    fun observeTotalCountForRange(start: Long, end: Long): Flow<Int>

    @Query("SELECT * FROM tasks WHERE syncState != :synced")
    suspend fun pendingChanges(synced: SyncState = SyncState.SYNCED): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(tasks: List<TaskEntity>)

    @Query("UPDATE tasks SET status = :status, completedAt = :completedAt, updatedAt = :now, syncState = :pending WHERE id = :id")
    suspend fun setStatus(id: String, status: TaskStatus, completedAt: Long?, now: Long, pending: SyncState = SyncState.PENDING_UPDATE)

    @Query("UPDATE tasks SET syncState = :state WHERE id = :id")
    suspend fun markSync(id: String, state: SyncState = SyncState.SYNCED)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM tasks")
    suspend fun clear()
}

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews ORDER BY startedAt DESC")
    fun observeAll(): Flow<List<ReviewEntity>>

    @Query("SELECT * FROM reviews WHERE startedAt BETWEEN :start AND :end ORDER BY startedAt DESC")
    fun observeForRange(start: Long, end: Long): Flow<List<ReviewEntity>>

    @Query("SELECT * FROM reviews WHERE id = :id")
    suspend fun getById(id: String): ReviewEntity?

    @Query("SELECT * FROM reviews WHERE syncState != :synced")
    suspend fun pendingChanges(synced: SyncState = SyncState.SYNCED): List<ReviewEntity>

    @Query("SELECT COUNT(*) FROM reviews")
    fun observeCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(review: ReviewEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(reviews: List<ReviewEntity>)

    @Query("UPDATE reviews SET syncState = :state WHERE id = :id")
    suspend fun markSync(id: String, state: SyncState = SyncState.SYNCED)

    @Query("DELETE FROM reviews WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM reviews")
    suspend fun clear()
}

@Dao
interface StreakDao {
    @Query("SELECT * FROM streak_activity ORDER BY dateKey DESC")
    fun observeAll(): Flow<List<StreakActivityEntity>>

    @Query("SELECT * FROM streak_activity WHERE dateKey = :dateKey")
    suspend fun getByDate(dateKey: String): StreakActivityEntity?

    @Query("SELECT * FROM streak_activity ORDER BY dateKey DESC LIMIT :limit")
    suspend fun recent(limit: Int = 60): List<StreakActivityEntity>

    @Query("SELECT * FROM streak_activity WHERE activityCount > 0 ORDER BY dateKey DESC")
    suspend fun activeDays(): List<StreakActivityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(activity: StreakActivityEntity)

    @Query("UPDATE streak_activity SET syncState = :state WHERE dateKey = :dateKey")
    suspend fun markSync(dateKey: String, state: SyncState = SyncState.SYNCED)

    @Query("DELETE FROM streak_activity")
    suspend fun clear()
}

@Dao
interface SyncQueueDao {
    @Query("SELECT * FROM sync_queue WHERE nextAttemptAt <= :now ORDER BY createdAt ASC LIMIT :limit")
    suspend fun pending(now: Date = Date(), limit: Int = 20): List<SyncQueueEntity>

    @Query("SELECT COUNT(*) FROM sync_queue")
    fun observeCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: SyncQueueEntity)

    @Query("UPDATE sync_queue SET attempts = :attempts, lastError = :error, nextAttemptAt = :next WHERE id = :id")
    suspend fun recordFailure(id: String, attempts: Int, error: String?, next: Date)

    @Query("DELETE FROM sync_queue WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM sync_queue")
    suspend fun clear()
}
