package com.studyinfo.app.data.database.dao

import androidx.room.*
import com.studyinfo.app.data.database.entity.ChapterEntity
import com.studyinfo.app.data.database.entity.TopicEntity
import com.studyinfo.app.domain.model.SyncState
import kotlinx.coroutines.flow.Flow

@Dao
interface ChapterDao {
    @Query("SELECT * FROM chapters WHERE syncState != 'PENDING_DELETE' ORDER BY displayOrder ASC, name ASC")
    fun observeAll(): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters WHERE syncState != 'PENDING_DELETE' AND subject = :subject ORDER BY displayOrder ASC, chapterNumber ASC")
    fun observeBySubject(subject: String): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters WHERE id = :id")
    suspend fun getById(id: String): ChapterEntity?

    @Query("SELECT * FROM chapters")
    suspend fun getAll(): List<ChapterEntity>

    @Query("SELECT * FROM chapters WHERE syncState != :synced")
    suspend fun pendingChanges(synced: SyncState = SyncState.SYNCED): List<ChapterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(chapter: ChapterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(chapters: List<ChapterEntity>)

    @Query("DELETE FROM chapters WHERE id = :id")
    suspend fun delete(id: String)

    @Query("UPDATE chapters SET syncState = :state WHERE id = :id")
    suspend fun markSync(id: String, state: SyncState = SyncState.SYNCED)

    @Query("DELETE FROM chapters")
    suspend fun clear()
}

@Dao
interface TopicDao {
    @Query("SELECT * FROM topics WHERE syncState != 'PENDING_DELETE' ORDER BY displayOrder ASC, name ASC")
    fun observeAll(): Flow<List<TopicEntity>>

    @Query("SELECT * FROM topics WHERE syncState != 'PENDING_DELETE' AND chapterId = :chapterId ORDER BY displayOrder ASC, name ASC")
    fun observeByChapter(chapterId: String): Flow<List<TopicEntity>>

    @Query("SELECT * FROM topics WHERE id = :id")
    suspend fun getById(id: String): TopicEntity?

    @Query("SELECT * FROM topics")
    suspend fun getAll(): List<TopicEntity>

    @Query("SELECT * FROM topics WHERE syncState != :synced")
    suspend fun pendingChanges(synced: SyncState = SyncState.SYNCED): List<TopicEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(topic: TopicEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(topics: List<TopicEntity>)

    @Query("DELETE FROM topics WHERE id = :id")
    suspend fun delete(id: String)

    @Query("UPDATE topics SET syncState = :state WHERE id = :id")
    suspend fun markSync(id: String, state: SyncState = SyncState.SYNCED)

    @Query("DELETE FROM topics")
    suspend fun clear()
}
