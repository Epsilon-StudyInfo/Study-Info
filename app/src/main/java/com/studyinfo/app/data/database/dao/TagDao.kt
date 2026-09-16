package com.studyinfo.app.data.database.dao

import androidx.room.*
import com.studyinfo.app.data.database.entity.TagEntity
import com.studyinfo.app.domain.model.SyncState
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {
    @Query("SELECT * FROM tags ORDER BY name ASC")
    fun observeAll(): Flow<List<TagEntity>>

    @Query("SELECT * FROM tags WHERE id = :id")
    suspend fun getById(id: String): TagEntity?

    @Query("SELECT * FROM tags WHERE syncState != :synced")
    suspend fun pendingChanges(synced: SyncState = SyncState.SYNCED): List<TagEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(tag: TagEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(tags: List<TagEntity>)

    @Query("DELETE FROM tags WHERE id = :id")
    suspend fun delete(id: String)

    @Query("UPDATE tags SET syncState = :state WHERE id = :id")
    suspend fun markSync(id: String, state: SyncState = SyncState.SYNCED)

    @Query("DELETE FROM tags")
    suspend fun clear()
}
