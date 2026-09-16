package com.studyinfo.app.data.repository

import com.studyinfo.app.data.database.dao.ErrorDao
import com.studyinfo.app.data.database.entity.ErrorEntryEntity
import com.studyinfo.app.data.firebase.FirestoreErrorsDataSource
import com.studyinfo.app.domain.model.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.util.Date

class ErrorRepositoryTest {

    @Test fun `add stamps entity with PENDING_CREATE sync state`() = runTest {
        val dao = mockk<ErrorDao>(relaxed = true)
        val remote = mockk<FirestoreErrorsDataSource>(relaxed = true)
        val repo = ErrorRepository(dao, remote)

        val input = ErrorEntryEntity(
            id = "",
            title = "Conceptual error in rotational motion",
            questionText = "What is the moment of inertia of a thin rod about its end?",
            subject = Subject.PHYSICS,
            chapterName = "Rotational Motion",
            topic = "Moment of Inertia",
            source = QuestionSource.MODULE,
            sourceRefId = null,
            moduleNumber = "M3",
            exercise = "Exercise 4",
            questionNumber = "Q7",
            difficulty = Difficulty.HARD,
            mistakeType = MistakeType.CONCEPTUAL,
            tags = emptyList(),
        )
        val saved = repo.add(input)

        assertEquals(SyncState.PENDING_CREATE, saved.syncState)
        assertNotNull(saved.id)
        assertEquals("Conceptual error in rotational motion", saved.title)
        coVerify { dao.upsert(saved) }
    }

    @Test fun `recordReview with UNDERSTOOD sets status and schedules next review`() = runTest {
        val dao = mockk<ErrorDao>(relaxed = true)
        val existing = ErrorEntryEntity(
            id = "err-1",
            title = "t",
            questionText = "q",
            subject = Subject.PHYSICS,
            chapterName = null,
            topic = null,
            source = QuestionSource.CUSTOM,
            sourceRefId = null,
            difficulty = Difficulty.MEDIUM,
            mistakeType = MistakeType.OTHER,
            status = ErrorStatus.ACTIVE,
            reviewCount = 2,
            tags = emptyList(),
            addedAt = Date(),
        )
        coEvery { dao.getById("err-1") } returns existing

        val remote = mockk<FirestoreErrorsDataSource>(relaxed = true)
        val repo = ErrorRepository(dao, remote)

        repo.recordReview("err-1", ReviewOutcome.UNDERSTOOD, reflection = "I forgot to apply Newton's second law for rotation.")

        coVerify {
            dao.upsert(match {
                it.id == "err-1" &&
                it.reviewCount == 3 &&
                it.status == ErrorStatus.UNDERSTOOD &&
                it.nextReviewAt != null &&
                it.personalNotes?.contains("[Review]") == true &&
                it.syncState == SyncState.PENDING_UPDATE
            })
        }
    }

    @Test fun `pushPending marks SYNCED after successful remote put`() = runTest {
        val dao = mockk<ErrorDao>(relaxed = true)
        val pending = ErrorEntryEntity(
            id = "err-pending",
            title = "t",
            questionText = "q",
            subject = Subject.CHEMISTRY,
            chapterName = null,
            topic = null,
            source = QuestionSource.CUSTOM,
            sourceRefId = null,
            difficulty = Difficulty.MEDIUM,
            mistakeType = MistakeType.OTHER,
            syncState = SyncState.PENDING_CREATE,
            tags = emptyList(),
            addedAt = Date(),
        )
        coEvery { dao.pendingChanges() } returns listOf(pending)
        coEvery { dao.markSync("err-pending", SyncState.SYNCED) } returns Unit
        val remote = mockk<FirestoreErrorsDataSource>(relaxed = true)
        coEvery { remote.put("err-pending", pending) } returns true

        val repo = ErrorRepository(dao, remote)
        val count = repo.pushPending()
        assertEquals(1, count)
        coVerify { remote.put("err-pending", pending) }
        coVerify { dao.markSync("err-pending", SyncState.SYNCED) }
    }

    @Test fun `pushPending hard-deletes after successful remote delete`() = runTest {
        val dao = mockk<ErrorDao>(relaxed = true)
        val pending = ErrorEntryEntity(
            id = "err-deleted",
            title = "t",
            questionText = "q",
            subject = Subject.MATHEMATICS,
            chapterName = null,
            topic = null,
            source = QuestionSource.CUSTOM,
            sourceRefId = null,
            difficulty = Difficulty.MEDIUM,
            mistakeType = MistakeType.OTHER,
            syncState = SyncState.PENDING_DELETE,
            tags = emptyList(),
            addedAt = Date(),
        )
        coEvery { dao.pendingChanges() } returns listOf(pending)
        coEvery { dao.hardDelete("err-deleted") } returns Unit
        val remote = mockk<FirestoreErrorsDataSource>(relaxed = true)
        coEvery { remote.delete("err-deleted") } returns true

        val repo = ErrorRepository(dao, remote)
        val count = repo.pushPending()
        assertEquals(1, count)
        coVerify { remote.delete("err-deleted") }
        coVerify { dao.hardDelete("err-deleted") }
    }
}
