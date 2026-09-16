package com.studyinfo.app.data.database.dao

import androidx.room.*
import com.studyinfo.app.data.database.entity.QuestionImageEntity
import com.studyinfo.app.domain.model.SyncState
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionImageDao {
    @Query("SELECT * FROM question_images WHERE questionRefType = :refType AND questionRefId = :refId ORDER BY createdAt ASC")
    fun observeForQuestion(refType: String, refId: String): Flow<List<QuestionImageEntity>>

    @Query("SELECT * FROM question_images WHERE questionRefType = :refType AND questionRefId = :refId")
    suspend fun getForQuestion(refType: String, refId: String): List<QuestionImageEntity>

    @Query("SELECT * FROM question_images WHERE id = :id")
    suspend fun getById(id: String): QuestionImageEntity?

    @Query("SELECT * FROM question_images WHERE questionRefType = :refType")
    suspend fun getAllForRefType(refType: String): List<QuestionImageEntity>

    @Query("SELECT * FROM question_images WHERE syncState != :synced")
    suspend fun pendingChanges(synced: SyncState = SyncState.SYNCED): List<QuestionImageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(image: QuestionImageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(images: List<QuestionImageEntity>)

    @Query("DELETE FROM question_images WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM question_images WHERE questionRefType = :refType AND questionRefId = :refId")
    suspend fun deleteForQuestion(refType: String, refId: String)

    @Query("UPDATE question_images SET syncState = :state WHERE id = :id")
    suspend fun markSync(id: String, state: SyncState = SyncState.SYNCED)

    @Query("DELETE FROM question_images")
    suspend fun clear()
}
