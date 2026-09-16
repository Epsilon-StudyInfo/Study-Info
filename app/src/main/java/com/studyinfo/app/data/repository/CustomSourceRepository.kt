package com.studyinfo.app.data.repository

import com.studyinfo.app.data.database.dao.CustomSourceDao
import com.studyinfo.app.data.database.entity.CustomSourceEntity
import com.studyinfo.app.data.firebase.FirestoreCustomSourcesDataSource
import com.studyinfo.app.domain.model.SyncState
import com.studyinfo.app.utils.newId
import com.studyinfo.app.utils.nowEpoch
import kotlinx.coroutines.flow.Flow
import java.util.Date

class CustomSourceRepository(
    private val dao: CustomSourceDao,
    private val remote: FirestoreCustomSourcesDataSource?,
) {
    fun observeAll(): Flow<List<CustomSourceEntity>> = dao.observeAll()

    suspend fun getById(id: String): CustomSourceEntity? = dao.getById(id)

    suspend fun create(name: String, description: String? = null): CustomSourceEntity {
        val now = Date(nowEpoch())
        val source = CustomSourceEntity(
            id = newId(),
            name = name.trim(),
            description = description,
            createdAt = now,
            updatedAt = now,
            syncState = SyncState.PENDING_CREATE,
        )
        dao.upsert(source)
        return source
    }

    suspend fun rename(id: String, name: String, description: String? = null) {
        val existing = dao.getById(id) ?: return
        dao.upsert(existing.copy(name = name.trim(), description = description, updatedAt = Date(nowEpoch()), syncState = SyncState.PENDING_UPDATE))
    }

    suspend fun delete(id: String) {
        val existing = dao.getById(id) ?: return
        dao.upsert(existing.copy(updatedAt = Date(nowEpoch()), syncState = SyncState.PENDING_DELETE))
    }

    suspend fun pushPending(): Int {
        val pending = dao.pendingChanges()
        if (pending.isEmpty()) return 0
        var count = 0
        for (source in pending) {
            when (source.syncState) {
                SyncState.PENDING_CREATE, SyncState.PENDING_UPDATE -> {
                    if (remote?.put(source.id, source) == true) { dao.markSync(source.id); count++ }
                }
                SyncState.PENDING_DELETE -> {
                    if (remote?.delete(source.id) == true) { dao.delete(source.id); count++ }
                }
                else -> {}
            }
        }
        return count
    }

    suspend fun pullAll(): Int {
        val remoteList = remote?.fetchAll() ?: emptyList()
        if (remoteList.isNotEmpty()) dao.upsertAll(remoteList)
        return remoteList.size
    }

    suspend fun clear() = dao.clear()
}
