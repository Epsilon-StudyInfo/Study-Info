package com.studyinfo.app.data.repository

import com.studyinfo.app.data.database.dao.ChapterDao
import com.studyinfo.app.data.database.dao.TopicDao
import com.studyinfo.app.data.database.entity.ChapterEntity
import com.studyinfo.app.data.database.entity.TopicEntity
import com.studyinfo.app.data.firebase.FirestoreChaptersDataSource
import com.studyinfo.app.data.firebase.FirestoreTopicsDataSource
import com.studyinfo.app.domain.model.ExamType
import com.studyinfo.app.domain.model.Subject
import com.studyinfo.app.domain.model.SyncState
import com.studyinfo.app.utils.newId
import com.studyinfo.app.utils.nowEpoch
import kotlinx.coroutines.flow.Flow
import java.util.Date

class ChapterRepository(
    private val dao: ChapterDao,
    private val topicDao: TopicDao,
    private val remoteChapters: FirestoreChaptersDataSource,
    private val remoteTopics: FirestoreTopicsDataSource,
) {
    fun observeAll(): Flow<List<ChapterEntity>> = dao.observeAll()
    fun observeBySubject(subject: Subject): Flow<List<ChapterEntity>> =
        dao.observeBySubject(subject.name)

    suspend fun getById(id: String): ChapterEntity? = dao.getById(id)

    suspend fun getAll(): List<ChapterEntity> = dao.getAll()

    suspend fun addCustomChapter(
        subject: Subject,
        name: String,
        examType: ExamType? = null,
        chapterNumber: Int = 0,
    ): ChapterEntity {
        val now = Date(nowEpoch())
        val entity = ChapterEntity(
            id = newId(),
            subject = subject.name,
            name = name.trim(),
            examType = examType?.name,
            chapterNumber = chapterNumber,
            isCustom = true,
            createdAt = now,
            updatedAt = now,
            syncState = SyncState.PENDING_CREATE,
        )
        dao.upsert(entity)
        return entity
    }

    suspend fun renameChapter(id: String, newName: String) {
        val existing = dao.getById(id) ?: return
        dao.upsert(existing.copy(name = newName.trim(), updatedAt = Date(nowEpoch()), syncState = SyncState.PENDING_UPDATE))
    }

    suspend fun deleteChapter(id: String) {
        val existing = dao.getById(id) ?: return
        dao.upsert(existing.copy(updatedAt = Date(nowEpoch()), syncState = SyncState.PENDING_DELETE))
    }

    fun observeTopicsForChapter(chapterId: String): Flow<List<TopicEntity>> =
        topicDao.observeByChapter(chapterId)

    suspend fun addTopic(chapterId: String, name: String): TopicEntity {
        val now = Date(nowEpoch())
        val entity = TopicEntity(
            id = newId(),
            chapterId = chapterId,
            name = name.trim(),
            createdAt = now,
            updatedAt = now,
            syncState = SyncState.PENDING_CREATE,
        )
        topicDao.upsert(entity)
        return entity
    }

    suspend fun pushPending(): Int {
        var count = 0
        val pendingChapters = dao.pendingChanges()
        for (chapter in pendingChapters) {
            when (chapter.syncState) {
                SyncState.PENDING_CREATE, SyncState.PENDING_UPDATE ->
                    if (remoteChapters.put(chapter.id, chapter)) { dao.markSync(chapter.id); count++ }
                SyncState.PENDING_DELETE ->
                    if (remoteChapters.delete(chapter.id)) { dao.delete(chapter.id); count++ }
                else -> {}
            }
        }
        val pendingTopics = topicDao.pendingChanges()
        for (topic in pendingTopics) {
            when (topic.syncState) {
                SyncState.PENDING_CREATE, SyncState.PENDING_UPDATE ->
                    if (remoteTopics.put(topic.id, topic)) { topicDao.markSync(topic.id); count++ }
                SyncState.PENDING_DELETE ->
                    if (remoteTopics.delete(topic.id)) { topicDao.delete(topic.id); count++ }
                else -> {}
            }
        }
        return count
    }

    suspend fun pullAll(): Int {
        val chapters = remoteChapters.fetchAll()
        if (chapters.isNotEmpty()) dao.upsertAll(chapters)
        val topics = remoteTopics.fetchAll()
        if (topics.isNotEmpty()) topicDao.upsertAll(topics)
        return chapters.size + topics.size
    }

    suspend fun clear() {
        dao.clear()
        topicDao.clear()
    }
}
