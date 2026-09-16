package com.studyinfo.app.data.repository

import com.studyinfo.app.data.database.dao.ProgressDao
import com.studyinfo.app.data.database.entity.ProgressEntity
import com.studyinfo.app.data.firebase.FirestoreProgressDataSource
import com.studyinfo.app.domain.model.ChapterState
import com.studyinfo.app.domain.model.ExamType
import com.studyinfo.app.domain.model.Subject
import com.studyinfo.app.domain.model.SyncState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.util.Date

class ProgressRepositoryTest {

    @Test fun `setPercent clamps to 0-100 range`() = runTest {
        val dao = mockk<ProgressDao>(relaxed = true)
        coEvery { dao.getByChapter("ch1") } returns null
        val repo = ProgressRepository(dao, mockk(relaxed = true))

        repo.setPercent("ch1", Subject.PHYSICS, ExamType.JEE_MAIN, percent = 150)
        coVerify {
            dao.upsert(match<ProgressEntity> {
                it.percent == 100 && it.chapterState == ChapterState.COMPLETED &&
                it.subject == Subject.PHYSICS && it.syncState == SyncState.PENDING_CREATE
            })
        }
    }

    @Test fun `setPercent marks chapter as NOT_STARTED when set to 0`() = runTest {
        val dao = mockk<ProgressDao>(relaxed = true)
        coEvery { dao.getByChapter("ch1") } returns null
        val repo = ProgressRepository(dao, mockk(relaxed = true))

        repo.setPercent("ch1", Subject.CHEMISTRY, ExamType.JEE_MAIN, percent = 0)
        coVerify {
            dao.upsert(match<ProgressEntity> {
                it.percent == 0 && it.chapterState == ChapterState.NOT_STARTED &&
                it.subject == Subject.CHEMISTRY
            })
        }
    }

    @Test fun `setPercent updates existing entity instead of creating a new one`() = runTest {
        val dao = mockk<ProgressDao>(relaxed = true)
        val existing = ProgressEntity(
            id = "p1",
            chapterId = "ch1",
            subject = Subject.PHYSICS,
            examType = ExamType.JEE_MAIN,
            chapterState = ChapterState.LEARNING,
            percent = 30,
            createdAt = Date(),
            updatedAt = Date(),
            syncState = SyncState.SYNCED,
        )
        coEvery { dao.getByChapter("ch1") } returns existing
        val repo = ProgressRepository(dao, mockk(relaxed = true))

        repo.setPercent("ch1", Subject.PHYSICS, ExamType.JEE_MAIN, percent = 75)
        coVerify {
            dao.upsert(match<ProgressEntity> {
                it.id == "p1" && it.percent == 75 && it.chapterState == ChapterState.LEARNING &&
                it.syncState == SyncState.PENDING_UPDATE
            })
        }
    }

    @Test fun `setPercent with explicit chapterState overrides derived state`() = runTest {
        val dao = mockk<ProgressDao>(relaxed = true)
        coEvery { dao.getByChapter("ch2") } returns null
        val repo = ProgressRepository(dao, mockk(relaxed = true))

        repo.setPercent("ch2", Subject.MATHEMATICS, ExamType.JEE_MAIN, percent = 50, chapterState = ChapterState.NEEDS_REVISION)
        coVerify {
            dao.upsert(match<ProgressEntity> {
                it.percent == 50 && it.chapterState == ChapterState.NEEDS_REVISION
            })
        }
    }
}
