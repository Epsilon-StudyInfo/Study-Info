package com.studyinfo.app.data.repository

import com.studyinfo.app.data.database.dao.ErrorDao
import com.studyinfo.app.data.database.dao.UnsolvedDao
import com.studyinfo.app.data.database.entity.UnsolvedQuestionEntity
import com.studyinfo.app.data.firebase.FirestoreErrorsDataSource
import com.studyinfo.app.data.firebase.FirestoreUnsolvedDataSource
import com.studyinfo.app.domain.model.*
import com.studyinfo.app.utils.newId
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import java.util.Date

class UnsolvedToErrorMigrationTest {

    @Test fun `moveToErrorBook copies source metadata into a new ErrorEntryEntity and marks original SOLVED`() = runTest {
        val unsolvedId = newId()
        val original = UnsolvedQuestionEntity(
            id = unsolvedId,
            title = "Q on projectile motion",
            questionText = "A ball is thrown at 30 degrees with v=20 m/s. Find range.",
            subject = Subject.PHYSICS,
            chapterName = "Kinematics",
            topic = "Projectile Motion",
            source = QuestionSource.PYQ,
            sourceRefId = null,
            pyqYear = 2023,
            examType = ExamType.JEE_MAIN,
            shiftSession = "Jan 24 Shift 1",
            chapterNumber = "3",
            questionNumber = "12",
            difficulty = Difficulty.HARD,
            reasonNotSolved = "Could not derive time of flight correctly",
            tags = listOf("projectile", "kinematics"),
            addedAt = Date(1700000000000L),
        )

        val unsolvedDao = mockk<UnsolvedDao>(relaxed = true)
        coEvery { unsolvedDao.getById(unsolvedId) } returns original
        val errorDao = mockk<ErrorDao>(relaxed = true)
        val unsolvedRemote = mockk<FirestoreUnsolvedDataSource>(relaxed = true)
        val errorsRemote = mockk<FirestoreErrorsDataSource>(relaxed = true)

        val repo = UnsolvedRepository(unsolvedDao, unsolvedRemote, errorsRemote, errorDao)

        val createdError = repo.moveToErrorBook(
            unsolvedId = unsolvedId,
            mistakeType = MistakeType.FORMULA,
            attemptedSolution = "I used v^2 = u^2 + 2as incorrectly",
            correctSolution = "Use T = 2u sin(theta) / g and R = u^2 sin(2*theta)/g",
            explanation = "Time of flight needs vertical component only.",
            lessonLearned = "Always decompose into vertical and horizontal components separately.",
        )

        assertNotNull(createdError)
        assertEquals(original.title, createdError!!.title)
        assertEquals(original.questionText, createdError.questionText)
        assertEquals(original.subject, createdError.subject)
        assertEquals(original.chapterName, createdError.chapterName)
        assertEquals(original.topic, createdError.topic)
        assertEquals(original.source, createdError.source)
        assertEquals(original.pyqYear, createdError.pyqYear)
        assertEquals(original.examType, createdError.examType)
        assertEquals(original.shiftSession, createdError.shiftSession)
        assertEquals(original.chapterNumber, createdError.chapterNumber)
        assertEquals(original.questionNumber, createdError.questionNumber)
        assertEquals(original.difficulty, createdError.difficulty)
        assertEquals(original.tags, createdError.tags)
        assertEquals(MistakeType.FORMULA, createdError.mistakeType)
        assertEquals(unsolvedId, createdError.originUnsolvedId)
        assertEquals(ErrorStatus.ACTIVE, createdError.status)
        assertEquals(SyncState.PENDING_CREATE, createdError.syncState)

        coVerify {
            unsolvedDao.markMovedToError(
                id = unsolvedId,
                errorId = createdError.id,
                now = any(),
                status = UnsolvedStatus.SOLVED,
                pending = SyncState.PENDING_UPDATE,
            )
        }
    }

    @Test fun `moveToErrorBook returns null when unsolved entry is missing`() = runTest {
        val unsolvedDao = mockk<UnsolvedDao>(relaxed = true)
        coEvery { unsolvedDao.getById(any()) } returns null
        val errorDao = mockk<ErrorDao>(relaxed = true)
        val repo = UnsolvedRepository(
            unsolvedDao,
            mockk(relaxed = true),
            mockk(relaxed = true),
            errorDao,
        )
        val result = repo.moveToErrorBook("missing-id")
        assertNull(result)
    }
}
