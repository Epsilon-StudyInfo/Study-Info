package com.studyinfo.app.data.database.dao

import androidx.room.*
import com.studyinfo.app.data.database.entity.CustomSourceEntity
import com.studyinfo.app.domain.model.SyncState
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomSourceDao {
    @Query("SELECT * FROM custom_sources ORDER BY name ASC")
    fun observeAll(): Flow<List<CustomSourceEntity>>

    @Query("SELECT * FROM custom_sources WHERE id = :id")
    suspend fun getById(id: String): CustomSourceEntity?

    @Query("SELECT * FROM custom_sources WHERE syncState != :synced")
    suspend fun pendingChanges(synced: SyncState = SyncState.SYNCED): List<CustomSourceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(source: CustomSourceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(sources: List<CustomSourceEntity>)

    @Query("DELETE FROM custom_sources WHERE id = :id")
    suspend fun delete(id: String)

    @Query("UPDATE custom_sources SET syncState = :state WHERE id = :id")
    suspend fun markSync(id: String, state: SyncState = SyncState.SYNCED)

    @Query("DELETE FROM custom_sources")
    suspend fun clear()
}
