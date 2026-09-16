package com.studyinfo.app.data.repository

import com.studyinfo.app.data.database.dao.*
import com.studyinfo.app.data.database.entity.*
import com.studyinfo.app.utils.AppResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupExportTest {

    @Test fun `export produces a JSON payload containing all top-level keys`() = runTest {
        val profileDao = mockk<UserProfileDao>(relaxed = true)
        val tagDao = mockk<TagDao>(relaxed = true)
        val customSourceDao = mockk<CustomSourceDao>(relaxed = true)
        val chapterDao = mockk<ChapterDao>(relaxed = true)
        val topicDao = mockk<TopicDao>(relaxed = true)
        val questionImageDao = mockk<QuestionImageDao>(relaxed = true)
        val errorDao = mockk<ErrorDao>(relaxed = true)
        val unsolvedDao = mockk<UnsolvedDao>(relaxed = true)
        val taskDao = mockk<TaskDao>(relaxed = true)
        val reviewDao = mockk<ReviewDao>(relaxed = true)
        val progressDao = mockk<ProgressDao>(relaxed = true)
        val streakDao = mockk<StreakDao>(relaxed = true)

        coEvery { profileDao.getById("uid-1") } returns null
        coEvery { tagDao.observeAll() } returns flowOf(emptyList())
        coEvery { customSourceDao.observeAll() } returns flowOf(emptyList())
        coEvery { chapterDao.observeAll() } returns flowOf(emptyList())
        coEvery { topicDao.observeAll() } returns flowOf(emptyList())
        coEvery { errorDao.observeAll() } returns flowOf(emptyList())
        coEvery { unsolvedDao.observeAll() } returns flowOf(emptyList())
        coEvery { taskDao.observeAll() } returns flowOf(emptyList())
        coEvery { reviewDao.observeAll() } returns flowOf(emptyList())
        coEvery { progressDao.observeAll() } returns flowOf(emptyList())
        coEvery { streakDao.observeAll() } returns flowOf(emptyList())

        val repo = BackupRepository(
            profileDao, tagDao, customSourceDao, chapterDao, topicDao, questionImageDao,
            errorDao, unsolvedDao, taskDao, reviewDao, progressDao, streakDao,
        )

        val result = repo.exportAllTablesForBackup("uid-1")
        assertTrue(result is AppResult.Success)
        val json = (result as AppResult.Success).value
        listOf("schemaVersion", "exportedAt", "uid", "tags", "customSources", "chapters",
            "topics", "errors", "unsolved", "tasks", "reviews", "progress", "streak")
            .forEach { key ->
                assertTrue("Backup JSON must contain key: $key", json.contains("\"$key\""))
            }
    }
}
