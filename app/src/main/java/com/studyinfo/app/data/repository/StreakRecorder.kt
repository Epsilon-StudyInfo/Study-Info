package com.studyinfo.app.data.repository

import com.studyinfo.app.data.database.dao.StreakDao
import com.studyinfo.app.data.database.entity.StreakActivityEntity
import com.studyinfo.app.domain.model.SyncState
import com.studyinfo.app.utils.nowEpoch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Records one unit of "study activity" for today. Any meaningful action — completing a
 * task, adding an error, adding an unsolved question, finishing a revision — keeps the
 * daily streak alive (not just task completions).
 */
class StreakRecorder(private val streakDao: StreakDao) {

    suspend fun record() {
        val dateKey = todayKey()
        val existing = streakDao.getByDate(dateKey)
        val now = Date(nowEpoch())
        if (existing == null) {
            streakDao.upsert(
                StreakActivityEntity(
                    dateKey = dateKey,
                    activityCount = 1,
                    lastActivityAt = now,
                    updatedAt = now,
                    syncState = SyncState.PENDING_CREATE,
                ),
            )
        } else {
            streakDao.upsert(
                existing.copy(
                    activityCount = existing.activityCount + 1,
                    lastActivityAt = now,
                    updatedAt = now,
                    syncState = SyncState.PENDING_UPDATE,
                ),
            )
        }
    }

    private fun todayKey(): String {
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply { timeZone = TimeZone.getDefault() }
        return fmt.format(Date())
    }
}
