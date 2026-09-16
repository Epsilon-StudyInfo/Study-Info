package com.studyinfo.app.data.repository

import com.studyinfo.app.data.database.dao.TagDao
import com.studyinfo.app.data.database.entity.TagEntity
import com.studyinfo.app.data.firebase.FirestoreTagsDataSource
import com.studyinfo.app.domain.model.SyncState
import com.studyinfo.app.utils.newId
import com.studyinfo.app.utils.nowEpoch
import kotlinx.coroutines.flow.Flow
import java.util.Date

class TagRepository(
    private val dao: TagDao,
    private val remote: FirestoreTagsDataSource,
) {
    fun observeAll(): Flow<List<TagEntity>> = dao.observeAll()

    suspend fun getById(id: String): TagEntity? = dao.getById(id)

    suspend fun create(name: String, color: String? = null): TagEntity {
        val now = Date(nowEpoch())
        val tag = TagEntity(
            id = newId(),
            name = name.trim(),
            color = color,
            createdAt = now,
            updatedAt = now,
            syncState = SyncState.PENDING_CREATE,
        )
        dao.upsert(tag)
        return tag
    }

    suspend fun rename(id: String, name: String) {
        val existing = dao.getById(id) ?: return
        dao.upsert(existing.copy(name = name.trim(), updatedAt = Date(nowEpoch()), syncState = SyncState.PENDING_UPDATE))
    }

    suspend fun delete(id: String) {
        // Mark for deletion so sync can remove from cloud
        val existing = dao.getById(id) ?: return
        dao.upsert(existing.copy(updatedAt = Date(nowEpoch()), syncState = SyncState.PENDING_DELETE))
    }

    suspend fun pushPending(): Int {
        val pending = dao.pendingChanges()
        if (pending.isEmpty()) return 0
        var count = 0
        for (tag in pending) {
            when (tag.syncState) {
                SyncState.PENDING_CREATE, SyncState.PENDING_UPDATE -> {
                    val ok = remote.put(tag.id, tag)
                    if (ok) { dao.markSync(tag.id); count++ }
                }
                SyncState.PENDING_DELETE -> {
                    val ok = remote.delete(tag.id)
                    if (ok) { dao.delete(tag.id); count++ }
                }
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
}
