package com.studyinfo.app.data.repository

import com.studyinfo.app.data.database.dao.StreakDao
import com.studyinfo.app.data.database.dao.TaskDao
import com.studyinfo.app.data.database.entity.StreakActivityEntity
import com.studyinfo.app.data.firebase.FirestoreStreakDataSource
import com.studyinfo.app.data.firebase.FirestoreTasksDataSource
import com.studyinfo.app.utils.nowEpoch
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class StreakComputationTest {

    private val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply { timeZone = TimeZone.getDefault() }

    private fun dateKey(offsetDays: Int): String {
        val cal = Calendar.getInstance(TimeZone.getDefault()).apply {
            add(Calendar.DAY_OF_YEAR, offsetDays)
        }
        return fmt.format(cal.time)
    }

    private fun mkActivity(dateKey: String): StreakActivityEntity =
        StreakActivityEntity(dateKey, activityCount = 1, lastActivityAt = Date(nowEpoch()), updatedAt = Date(nowEpoch()))

    private fun activeDays(vararg keys: String): List<StreakActivityEntity> =
        keys.map { mkActivity(it) }.sortedBy { it.dateKey }

    @Test fun `streak is zero when no activity`() = runTest {
        val taskDao = mockk<TaskDao>(relaxed = true)
        val streakDao = mockk<StreakDao>(relaxed = true)
        coEvery { streakDao.activeDays() } returns emptyList()
        val repo = TaskRepository(taskDao, mockk(relaxed = true), streakDao, mockk(relaxed = true))
        val (current, longest) = repo.computeStreak()
        assertEquals(0, current)
        assertEquals(0, longest)
    }

    @Test fun `consecutive days including today produce a streak`() = runTest {
        val taskDao = mockk<TaskDao>(relaxed = true)
        val streakDao = mockk<StreakDao>(relaxed = true)
        coEvery { streakDao.activeDays() } returns activeDays(dateKey(-3), dateKey(-2), dateKey(-1), dateKey(0))
        val repo = TaskRepository(taskDao, mockk(relaxed = true), streakDao, mockk(relaxed = true))
        val (current, longest) = repo.computeStreak()
        assertEquals(4, current)
        assertEquals(4, longest)
    }

    @Test fun `streak includes today OR yesterday as the latest day`() = runTest {
        val taskDao = mockk<TaskDao>(relaxed = true)
        val streakDao = mockk<StreakDao>(relaxed = true)
        coEvery { streakDao.activeDays() } returns activeDays(dateKey(-3), dateKey(-2), dateKey(-1))
        val repo = TaskRepository(taskDao, mockk(relaxed = true), streakDao, mockk(relaxed = true))
        val (current, longest) = repo.computeStreak()
        assertEquals(3, current)
        assertEquals(3, longest)
    }

    @Test fun `gap in activity breaks the streak but longest streak tracks the best run`() = runTest {
        val taskDao = mockk<TaskDao>(relaxed = true)
        val streakDao = mockk<StreakDao>(relaxed = true)
        coEvery { streakDao.activeDays() } returns activeDays(
            dateKey(-10), dateKey(-9), dateKey(-8),
            dateKey(-1), dateKey(0),
        )
        val repo = TaskRepository(taskDao, mockk(relaxed = true), streakDao, mockk(relaxed = true))
        val (current, longest) = repo.computeStreak()
        assertEquals(2, current)
        assertEquals(3, longest)
    }
}
