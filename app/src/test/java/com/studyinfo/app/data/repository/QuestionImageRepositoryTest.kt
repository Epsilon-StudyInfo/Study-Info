package com.studyinfo.app.data.repository

import com.studyinfo.app.data.database.dao.QuestionImageDao
import com.studyinfo.app.data.database.dao.SyncQueueDao
import com.studyinfo.app.data.database.entity.QuestionImageEntity
import com.studyinfo.app.data.database.entity.SyncQueueEntity
import com.studyinfo.app.data.firebase.FirestoreQuestionImagesDataSource
import com.studyinfo.app.domain.model.SyncState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Date

/**
 * Unit tests for the question-image repository: local registration + upload queue,
 * ownership transfer (unsolved -> error book) and deletion semantics.
 */
class QuestionImageRepositoryTest {

    // ---------- in-memory fakes (MockK-backed) ----------

    private fun imageDaoWith(rows: MutableList<QuestionImageEntity>): QuestionImageDao {
        val dao = mockk<QuestionImageDao>(relaxed = true)
        coEvery { dao.upsert(any()) } answers {
            val e = firstArg<QuestionImageEntity>()
            rows.removeAll { it.id == e.id }
            rows.add(e)
        }
        coEvery { dao.getById(any()) } answers {
            val id: String = firstArg()
            rows.firstOrNull { it.id == id }
        }
        coEvery { dao.delete(any()) } answers {
            val id: String = firstArg()
            rows.removeAll { it.id == id }
        }
        coEvery { dao.getForQuestion(any(), any()) } answers {
            val type: String = firstArg()
            val refId: String = secondArg()
            rows.filter { it.questionRefType == type && it.questionRefId == refId }
        }
        return dao
    }

    private fun queueDaoWith(queue: MutableList<SyncQueueEntity>): SyncQueueDao {
        val dao = mockk<SyncQueueDao>(relaxed = true)
        coEvery { dao.upsert(any()) } answers {
            val e = firstArg<SyncQueueEntity>()
            queue.removeAll { it.id == e.id }
            queue.add(e)
        }
        coEvery { dao.delete(any()) } answers {
            val id: String = firstArg()
            queue.removeAll { it.id == id }
        }
        return dao
    }

    private fun image(
        id: String,
        refType: String,
        refId: String,
        syncState: SyncState,
        remoteUrl: String? = null,
    ) = QuestionImageEntity(
        id = id,
        questionRefType = refType,
        questionRefId = refId,
        localUri = "file:///data/question_images/$id.jpg",
        remoteUrl = remoteUrl,
        storagePath = remoteUrl?.let { "users/u1/questions/$refId/$id" },
        createdAt = Date(),
        updatedAt = Date(),
        syncState = syncState,
    )

    // ---------- addLocal ----------

    @Test
    fun `addLocal registers row as PENDING_CREATE and enqueues an IMAGE_UPLOAD job`() = runTest {
        val rows = mutableListOf<QuestionImageEntity>()
        val queue = mutableListOf<SyncQueueEntity>()
        val repo = QuestionImageRepository(
            imageDaoWith(rows), null, null, queueDaoWith(queue),
        )

        val entity = repo.addLocal(
            questionRefType = QuestionImageRepository.REF_ERROR,
            questionRefId = "err-1",
            localUri = "file:///data/question_images/a.jpg",
        )

        assertEquals(SyncState.PENDING_CREATE, entity.syncState)
        assertEquals(QuestionImageRepository.REF_ERROR, entity.questionRefType)
        assertEquals("err-1", entity.questionRefId)
        assertTrue(rows.any { it.id == entity.id })

        val job = queue.single()
        assertEquals("IMAGE_UPLOAD", job.type)
        assertEquals(entity.id, job.imageId)
        assertEquals("err-1", job.refId)
    }

    // ---------- reassign (unsolved -> error book) ----------

    @Test
    fun `reassign moves images to the new question and re-marks synced rows for push`() = runTest {
        val rows = mutableListOf(
            image("img-1", QuestionImageRepository.REF_UNSOLVED, "uns-1", SyncState.SYNCED, remoteUrl = "https://cdn/x"),
            image("img-2", QuestionImageRepository.REF_UNSOLVED, "uns-1", SyncState.PENDING_CREATE),
            image("img-3", QuestionImageRepository.REF_ERROR, "err-9", SyncState.SYNCED, remoteUrl = "https://cdn/y"),
        )
        val repo = QuestionImageRepository(
            imageDaoWith(rows), null, null, queueDaoWith(mutableListOf()),
        )

        val moved = repo.reassign(
            fromRefType = QuestionImageRepository.REF_UNSOLVED,
            fromRefId = "uns-1",
            toRefType = QuestionImageRepository.REF_ERROR,
            toRefId = "err-42",
        )

        assertEquals(2, moved)
        // Synced row -> must be re-pushed under the new owner.
        assertEquals(SyncState.PENDING_UPDATE, rows.first { it.id == "img-1" }.syncState)
        assertEquals("err-42", rows.first { it.id == "img-1" }.questionRefId)
        assertEquals(QuestionImageRepository.REF_ERROR, rows.first { it.id == "img-1" }.questionRefType)
        // Never-pushed row keeps waiting for its first push.
        assertEquals(SyncState.PENDING_CREATE, rows.first { it.id == "img-2" }.syncState)
        assertEquals("err-42", rows.first { it.id == "img-2" }.questionRefId)
        // Other questions are untouched.
        assertEquals("err-9", rows.first { it.id == "img-3" }.questionRefId)
    }

    @Test
    fun `reassign to the same question is a no-op`() = runTest {
        val rows = mutableListOf(
            image("img-1", QuestionImageRepository.REF_ERROR, "err-1", SyncState.SYNCED),
        )
        val dao = imageDaoWith(rows)
        val repo = QuestionImageRepository(dao, null, null, queueDaoWith(mutableListOf()))

        val moved = repo.reassign(
            fromRefType = QuestionImageRepository.REF_ERROR,
            fromRefId = "err-1",
            toRefType = QuestionImageRepository.REF_ERROR,
            toRefId = "err-1",
        )

        assertEquals(0, moved)
        coVerify(exactly = 0) { dao.upsert(any()) }
    }

    // ---------- delete ----------

    @Test
    fun `delete removes the row locally and from the remote metadata`() = runTest {
        val rows = mutableListOf(
            image("img-1", QuestionImageRepository.REF_ERROR, "err-1", SyncState.SYNCED, remoteUrl = "https://cdn/x"),
        )
        val dao = imageDaoWith(rows)
        val remote = mockk<FirestoreQuestionImagesDataSource>(relaxed = true)
        coEvery { remote.delete("img-1") } returns true
        val repo = QuestionImageRepository(dao, remote, null, queueDaoWith(mutableListOf()))

        repo.delete("img-1")

        assertTrue(rows.isEmpty())
        coVerify(exactly = 1) { remote.delete("img-1") }
    }

    @Test
    fun `deleteForQuestion removes every image of that question only`() = runTest {
        val rows = mutableListOf(
            image("img-1", QuestionImageRepository.REF_UNSOLVED, "uns-1", SyncState.SYNCED),
            image("img-2", QuestionImageRepository.REF_UNSOLVED, "uns-1", SyncState.PENDING_CREATE),
            image("img-3", QuestionImageRepository.REF_UNSOLVED, "uns-2", SyncState.SYNCED),
        )
        val repo = QuestionImageRepository(
            imageDaoWith(rows), null, null, queueDaoWith(mutableListOf()),
        )

        repo.deleteForQuestion(QuestionImageRepository.REF_UNSOLVED, "uns-1")

        assertEquals(listOf("img-3"), rows.map { it.id })
    }

    // ---------- upload queue processing (local-only mode) ----------

    @Test
    fun `processQueuedUpload drops the job when cloud storage is not configured`() = runTest {
        val rows = mutableListOf(
            image("img-1", QuestionImageRepository.REF_ERROR, "err-1", SyncState.PENDING_CREATE),
        )
        val queue = mutableListOf(
            SyncQueueEntity(
                id = "job-1",
                type = "IMAGE_UPLOAD",
                refType = QuestionImageRepository.REF_ERROR,
                refId = "err-1",
                imageId = "img-1",
                payload = rows.first().localUri,
                createdAt = Date(),
                nextAttemptAt = Date(),
            ),
        )
        val repo = QuestionImageRepository(
            imageDaoWith(rows), null, null, queueDaoWith(queue),
        )

        val ok = repo.processQueuedUpload(queue.first())

        assertTrue(ok)
        assertTrue(queue.isEmpty())   // dropped instead of retrying forever offline
        assertNotNull(rows.first { it.id == "img-1" })  // local copy survives
    }
}
