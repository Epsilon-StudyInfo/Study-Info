package com.studyinfo.app.data.repository

import android.net.Uri
import com.studyinfo.app.data.database.dao.QuestionImageDao
import com.studyinfo.app.data.database.dao.SyncQueueDao
import com.studyinfo.app.data.database.entity.QuestionImageEntity
import com.studyinfo.app.data.database.entity.SyncQueueEntity
import com.studyinfo.app.data.firebase.FirebaseStorageDataSource
import com.studyinfo.app.data.firebase.FirestoreQuestionImagesDataSource
import com.studyinfo.app.data.images.QuestionImageStore
import com.studyinfo.app.domain.model.SyncState
import com.studyinfo.app.utils.newId
import com.studyinfo.app.utils.nowEpoch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.Date

/**
 * Repository for question images.
 *
 * Image metadata lives in Room; the binary lives in Firebase Storage (uploaded by the
 * sync queue) and — between pick and upload — in app-private storage via [store].
 *
 * Workflow:
 *   1. User picks/captures an image → [addFromPicker] copies it into PRIVATE app storage
 *      (photo-picker grants are transient) and stores that `file://` uri in
 *      [QuestionImageEntity] with syncState=PENDING_CREATE, enqueuing an IMAGE_UPLOAD job
 *      in [SyncQueueDao].
 *   2. SyncManager picks up the queue entry, uploads to Storage, updates the row's
 *      remoteUrl/storagePath, then marks it SYNCED and pushes the metadata doc to Firestore.
 */
class QuestionImageRepository(
    private val dao: QuestionImageDao,
    private val remote: FirestoreQuestionImagesDataSource?,
    private val storage: FirebaseStorageDataSource?,
    private val syncQueueDao: SyncQueueDao,
    private val store: QuestionImageStore? = null,
) {

    /** Named question-ref types (which table an image belongs to). */
    companion object {
        const val REF_ERROR = "error"
        const val REF_UNSOLVED = "unsolved"
    }

    fun observeForQuestion(refType: String, refId: String): Flow<List<QuestionImageEntity>> =
        dao.observeForQuestion(refType, refId)

    suspend fun getForQuestion(refType: String, refId: String): List<QuestionImageEntity> =
        dao.getForQuestion(refType, refId)

    /**
     * Entry point for the photo picker: imports the picked image into private storage
     * (compressing it under the Storage-rule size ceiling) and registers it for upload.
     * Returns null when the source could not be read — the UI shows a friendly error.
     */
    suspend fun addFromPicker(
        questionRefType: String,
        questionRefId: String,
        pickedUri: Uri,
    ): QuestionImageEntity? {
        val localUri = withContext(Dispatchers.IO) {
            store?.importToPrivateStorage(pickedUri)
        } ?: return null
        return addLocal(questionRefType, questionRefId, localUri)
    }

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
        // No cloud storage configured (local-only mode): keep the image offline-only and
        // drop the queue entry instead of retrying forever.
        val storage = storage ?: return true.also { syncQueueDao.delete(item.id) }
        return try {
            val urlResult = storage.uploadFromUri(
                questionId = image.questionRefId,
                imageId = image.id,
                sourceUri = android.net.Uri.parse(localUri),
            )
            val url = urlResult.getOrThrow()
            val updated = image.copy(
                remoteUrl = url,
                // Path must match the upload layout exactly: users/{uid}/questions/{qId}/{imageId}.
                storagePath = storage.pathFor(image.questionRefId, image.id),
                uploadedAt = Date(nowEpoch()),
                updatedAt = Date(nowEpoch()),
                syncState = SyncState.PENDING_UPDATE,
            )
            dao.upsert(updated)
            // Push metadata to Firestore
            remote?.put(updated.id, updated)
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
        existing.remoteUrl?.let { runCatching { storage?.deleteByRemoteUrl(it) } }
        existing.storagePath?.let { runCatching { storage?.deleteByPath(it) } }
        remote?.delete(imageId)
        dao.delete(imageId)
        // The private on-device copy goes away with the row.
        withContext(Dispatchers.IO) { store?.deleteByUri(existing.localUri) }
    }

    suspend fun deleteForQuestion(refType: String, refId: String) {
        val images = dao.getForQuestion(refType, refId)
        for (img in images) delete(img.id)
    }

    /**
     * Moves every image of one question to another question (same or different ref type).
     * Used when an unsolved question is moved into the error book: the binaries keep
     * their existing remote location ([storagePath]/[remoteUrl] stay valid) and only the
     * ownership metadata is re-pointed, then pushed on the next sync.
     *
     * @return how many images were moved.
     */
    suspend fun reassign(
        fromRefType: String,
        fromRefId: String,
        toRefType: String,
        toRefId: String,
    ): Int {
        if (fromRefType == toRefType && fromRefId == toRefId) return 0
        val images = dao.getForQuestion(fromRefType, fromRefId)
        var moved = 0
        for (img in images) {
            val nextSyncState = when (img.syncState) {
                // Not yet pushed anywhere — keep waiting for its first push.
                SyncState.PENDING_CREATE, SyncState.PENDING_DELETE -> SyncState.PENDING_CREATE
                // Already (partially) in the cloud — the re-pointed doc must be re-pushed.
                else -> SyncState.PENDING_UPDATE
            }
            dao.upsert(
                img.copy(
                    questionRefType = toRefType,
                    questionRefId = toRefId,
                    updatedAt = Date(nowEpoch()),
                    syncState = nextSyncState,
                ),
            )
            moved++
        }
        return moved
    }

    suspend fun pushPending(): Int {
        val pending = dao.pendingChanges()
        var count = 0
        for (img in pending) {
            when (img.syncState) {
                SyncState.PENDING_CREATE, SyncState.PENDING_UPDATE -> {
                    if (img.remoteUrl != null && remote?.put(img.id, img) == true) { dao.markSync(img.id); count++ }
                }
                SyncState.PENDING_DELETE -> { if (remote?.delete(img.id) == true) { dao.delete(img.id); count++ } }
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
