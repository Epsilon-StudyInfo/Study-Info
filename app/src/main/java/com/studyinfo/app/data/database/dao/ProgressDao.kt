package com.studyinfo.app.data.database.dao

import androidx.room.*
import com.studyinfo.app.data.database.entity.ProgressEntity
import com.studyinfo.app.domain.model.ExamType
import com.studyinfo.app.domain.model.Subject
import com.studyinfo.app.domain.model.SyncState
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {
    @Query("SELECT * FROM progress WHERE syncState != 'PENDING_DELETE' ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<ProgressEntity>>

    @Query("SELECT * FROM progress WHERE syncState != 'PENDING_DELETE' AND subject = :subject ORDER BY updatedAt DESC")
    fun observeBySubject(subject: Subject): Flow<List<ProgressEntity>>

    @Query("SELECT * FROM progress WHERE syncState != 'PENDING_DELETE' AND examType = :examType ORDER BY subject, updatedAt DESC")
    fun observeByExam(examType: ExamType): Flow<List<ProgressEntity>>

    @Query("SELECT * FROM progress WHERE syncState != 'PENDING_DELETE' AND chapterId = :chapterId")
    suspend fun getByChapter(chapterId: String): ProgressEntity?

    @Query("SELECT * FROM progress WHERE subject = :subject AND examType = :examType LIMIT 1")
    suspend fun firstForSubjectExam(subject: Subject, examType: ExamType): ProgressEntity?

    @Query("SELECT * FROM progress WHERE syncState != :synced")
    suspend fun pendingChanges(synced: SyncState = SyncState.SYNCED): List<ProgressEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(progress: ProgressEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<ProgressEntity>)

    @Query("UPDATE progress SET syncState = :state WHERE id = :id")
    suspend fun markSync(id: String, state: SyncState = SyncState.SYNCED)

    @Query("DELETE FROM progress WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM progress")
    suspend fun clear()
}
