package com.studyinfo.app.data.repository

import com.studyinfo.app.data.database.dao.QuestionImageDao
import com.studyinfo.app.data.database.dao.SyncQueueDao
import com.studyinfo.app.data.database.entity.QuestionImageEntity
import com.studyinfo.app.data.database.entity.SyncQueueEntity
import com.studyinfo.app.data.firebase.FirebaseStorageDataSource
import com.studyinfo.app.data.firebase.FirestoreQuestionImagesDataSource
import com.studyinfo.app.domain.model.SyncState
import com.studyinfo.app.utils.newId
import com.studyinfo.app.utils.nowEpoch
import kotlinx.coroutines.flow.Flow
import java.util.Date

/**
 * Repository for question images.
 *
 * Image metadata lives in Room; the binary lives in Firebase Storage.
 *
 * Workflow:
 *   1. User picks/captures image → we store its localUri in [QuestionImageEntity] with
 *      syncState=PENDING_CREATE and enqueue an IMAGE_UPLOAD job in [SyncQueueDao].
 *   2. SyncManager picks up the queue entry, compresses the image, uploads to Storage,
 *      updates the row's remoteUrl/storagePath, then marks it SYNCED and pushes the
 *      metadata doc to Firestore.
 */
class QuestionImageRepository(
    private val dao: QuestionImageDao,
    private val remote: FirestoreQuestionImagesDataSource,
    private val storage: FirebaseStorageDataSource,
    private val syncQueueDao: SyncQueueDao,
) {

    fun observeForQuestion(refType: String, refId: String): Flow<List<QuestionImageEntity>> =
        dao.observeForQuestion(refType, refId)

    suspend fun getForQuestion(refType: String, refId: String): List<QuestionImageEntity> =
        dao.getForQuestion(refType, refId)

    suspend fun addLocal(
        questionRefType: String,
        questionRefId: String,
        localUri: String,
    ): QuestionImageEntity {
        val now = Date(nowEpoch())
        val entity = QuestionImageEntity(
            id = newId(),
            questionRefType = questionRefType,
            questionRefId = questionRefId,
            localUri = localUri,
            remoteUrl = null,
            storagePath = null,
            createdAt = now,
            updatedAt = now,
            syncState = SyncState.PENDING_CREATE,
        )
        dao.upsert(entity)
        syncQueueDao.upsert(SyncQueueEntity(
            id = newId(),
            type = "IMAGE_UPLOAD",
            refType = questionRefType,
            refId = questionRefId,
            imageId = entity.id,
            payload = localUri,
            createdAt = now,
            nextAttemptAt = now,
        ))
        return entity
    }

    /**
     * Called by SyncManager. Uploads one queued image, updates the row, and pushes the
     * metadata document to Firestore.
     */
    suspend fun processQueuedUpload(item: SyncQueueEntity): Boolean {
        val imageId = item.imageId ?: return true.also { syncQueueDao.delete(item.id) }
        val image = dao.getById(imageId) ?: return true.also { syncQueueDao.delete(item.id) }
        val localUri = image.localUri ?: return false.also {
            syncQueueDao.recordFailure(item.id, item.attempts + 1, "no local uri", Date(nowEpoch()))
        }
        return try {
            val urlResult = storage.uploadFromUri(
                questionId = image.questionRefId,
                imageId = image.id,
                sourceUri = android.net.Uri.parse(localUri),
            )
            val url = urlResult.getOrThrow()
            val updated = image.copy(
                remoteUrl = url,
                // Reconstruct path; matches the layout used inside FirebaseStorageDataSource.
                storagePath = "users/questions/${image.questionRefId}/${image.id}",
                uploadedAt = Date(nowEpoch()),
                updatedAt = Date(nowEpoch()),
                syncState = SyncState.PENDING_UPDATE,
            )
            dao.upsert(updated)
            // Push metadata to Firestore
            remote.put(updated.id, updated)
            dao.markSync(updated.id)
            syncQueueDao.delete(item.id)
            true
        } catch (e: Exception) {
            syncQueueDao.recordFailure(item.id, item.attempts + 1, e.message ?: "upload failed", Date(nowEpoch() + 60_000L))
            false
        }
    }

    suspend fun delete(imageId: String) {
        val existing = dao.getById(imageId) ?: return
        // best-effort remote delete
        existing.remoteUrl?.let { runCatching { storage.deleteByRemoteUrl(it) } }
        existing.storagePath?.let { runCatching { storage.deleteByPath(it) } }
        remote.delete(imageId)
        dao.delete(imageId)
    }

    suspend fun deleteForQuestion(refType: String, refId: String) {
        val images = dao.getForQuestion(refType, refId)
        for (img in images) delete(img.id)
    }

    suspend fun pushPending(): Int {
        val pending = dao.pendingChanges()
        var count = 0
        for (img in pending) {
            when (img.syncState) {
                SyncState.PENDING_CREATE, SyncState.PENDING_UPDATE -> {
                    if (img.remoteUrl != null && remote.put(img.id, img)) { dao.markSync(img.id); count++ }
                }
                SyncState.PENDING_DELETE -> { if (remote.delete(img.id)) { dao.delete(img.id); count++ } }
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
