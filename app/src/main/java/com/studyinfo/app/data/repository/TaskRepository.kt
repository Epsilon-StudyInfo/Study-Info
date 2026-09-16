package com.studyinfo.app.data.repository

import com.studyinfo.app.data.database.dao.StreakDao
import com.studyinfo.app.data.database.dao.TaskDao
import com.studyinfo.app.data.database.entity.StreakActivityEntity
import com.studyinfo.app.data.database.entity.TaskEntity
import com.studyinfo.app.data.firebase.FirestoreStreakDataSource
import com.studyinfo.app.data.firebase.FirestoreTasksDataSource
import com.studyinfo.app.domain.model.SyncState
import com.studyinfo.app.domain.model.TaskPriority
import com.studyinfo.app.domain.model.TaskStatus
import com.studyinfo.app.utils.newId
import com.studyinfo.app.utils.nowEpoch
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class TaskRepository(
    private val dao: TaskDao,
    private val remote: FirestoreTasksDataSource?,
    private val streakDao: StreakDao,
    private val streakRemote: FirestoreStreakDataSource?,
    private val streakRecorder: StreakRecorder? = null,
) {

    fun observeAll(): Flow<List<TaskEntity>> = dao.observeAll()
    fun observeById(id: String): Flow<TaskEntity?> = dao.observeById(id)
    fun observeOverdue(): Flow<List<TaskEntity>> = dao.observeOverdue(System.currentTimeMillis())
    fun observeByStatus(status: TaskStatus): Flow<List<TaskEntity>> = dao.observeByStatus(status)

    fun observeForToday(): Flow<List<TaskEntity>> {
        val (start, end) = todayRange()
        return dao.observeForRange(start.time, end.time)
    }

    fun observeUpcoming(): Flow<List<TaskEntity>> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0); cal.clear(Calendar.MINUTE); cal.clear(Calendar.SECOND); cal.clear(Calendar.MILLISECOND)
        val start = cal.timeInMillis + 24L * 60L * 60L * 1000L // tomorrow
        val end = start + 30L * 24L * 60L * 60L * 1000L // +30d
        return dao.observeForRange(start, end)
    }

    fun observeTodayCompletion(): Flow<Pair<Int, Int>> {
        val (start, end) = todayRange()
        return kotlinx.coroutines.flow.combine(
            dao.observeTotalCountForRange(start.time, end.time),
            dao.observeCompletedCountForRange(start.time, end.time),
        ) { total, completed -> total to completed }
    }

    suspend fun getById(id: String): TaskEntity? = dao.getById(id)

    suspend fun add(task: TaskEntity): TaskEntity {
        val now = Date(nowEpoch())
        val withMeta = task.copy(
            id = task.id.ifBlank { newId() },
            createdAt = now,
            updatedAt = now,
            syncState = SyncState.PENDING_CREATE,
        )
        dao.upsert(withMeta)
        return withMeta
    }

    suspend fun update(task: TaskEntity) {
        dao.upsert(task.copy(updatedAt = Date(nowEpoch()), syncState = SyncState.PENDING_UPDATE))
    }

    suspend fun setStatus(id: String, status: TaskStatus) {
        val completedAt = if (status == TaskStatus.COMPLETED) nowEpoch() else null
        dao.setStatus(id, status, completedAt, nowEpoch())
        if (status == TaskStatus.COMPLETED) {
            runCatching { streakRecorder?.record() }
            val task = dao.getById(id) ?: return
            // Auto-create next recurrence if needed
            task.recurrence?.let { recurrence ->
                val nextDue = nextRecurrence(task.dueDate, recurrence) ?: return@let
                dao.upsert(task.copy(
                    id = newId(),
                    dueDate = nextDue,
                    status = TaskStatus.PENDING,
                    completedAt = null,
                    createdAt = Date(nowEpoch()),
                    updatedAt = Date(nowEpoch()),
                    syncState = SyncState.PENDING_CREATE,
                ))
            }
        }
    }

    suspend fun delete(id: String) {
        val existing = dao.getById(id) ?: return
        dao.upsert(existing.copy(updatedAt = Date(nowEpoch()), syncState = SyncState.PENDING_DELETE))
    }

    suspend fun hardDelete(id: String) = dao.delete(id)

    // ---------- Sync ----------
    suspend fun pushPending(): Int {
        val pending = dao.pendingChanges()
        if (pending.isEmpty()) return 0
        var count = 0
        for (task in pending) {
            when (task.syncState) {
                SyncState.PENDING_CREATE, SyncState.PENDING_UPDATE ->
                    if (remote?.put(task.id, task) == true) { dao.markSync(task.id); count++ }
                SyncState.PENDING_DELETE ->
                    if (remote?.delete(task.id) == true) { dao.hardDelete(task.id); count++ }
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

    // ---------- Streak ----------

    /**
     * Compute the current and longest streaks based on activity rows.
     *
     * Streak: consecutive days (ending today or yesterday) with activityCount > 0.
     */
    suspend fun computeStreak(): Pair<Int, Int> {
        val active = streakDao.activeDays().map { it.dateKey }.toSet()
        if (active.isEmpty()) return 0 to 0
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply { timeZone = TimeZone.getDefault() }
        val cal = Calendar.getInstance(TimeZone.getDefault())
        // current streak
        var currentStreak = 0
        cal.time = Date()
        // allow "today OR yesterday" to count so a user who hasn't done today's activity yet
        // doesn't see "0 streak" early in the morning.
        if (!active.contains(fmt.format(cal.time))) cal.add(Calendar.DAY_OF_YEAR, -1)
        while (active.contains(fmt.format(cal.time))) {
            currentStreak++
            cal.add(Calendar.DAY_OF_YEAR, -1)
        }
        // longest streak - iterate active days sorted ascending
        val sortedActive = active.sorted()
        var longest = 0
        var run = 0
        var prev: Calendar? = null
        for (key in sortedActive) {
            val d = fmt.parse(key) ?: continue
            val c = Calendar.getInstance(TimeZone.getDefault()).apply { time = d }
            if (prev != null) {
                val diff = (c.timeInMillis - prev!!.timeInMillis) / (24L * 60L * 60L * 1000L)
                if (diff == 1L) run++ else run = 1
            } else run = 1
            if (run > longest) longest = run
            prev = c
        }
        return currentStreak to longest
    }

    suspend fun pushStreakPending(): Int {
        val recent = streakDao.recent(365)
        var count = 0
        for (item in recent) {
            if (item.syncState != SyncState.SYNCED) {
                if (streakRemote?.put(item.dateKey, item) == true) { streakDao.markSync(item.dateKey); count++ }
            }
        }
        return count
    }

    suspend fun pullStreakAll(): Int {
        val remoteList = streakRemote?.fetchAll() ?: emptyList()
        for (item in remoteList) streakDao.upsert(item)
        return remoteList.size
    }

    // ---------- Helpers ----------
    private fun todayRange(): Pair<Date, Date> {
        val cal = Calendar.getInstance(TimeZone.getDefault()).apply {
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }
        val start = cal.time
        cal.add(Calendar.DAY_OF_YEAR, 1)
        return start to cal.time
    }

    fun todayKey(): String {
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply { timeZone = TimeZone.getDefault() }
        return fmt.format(Date())
    }

    private fun nextRecurrence(due: Date, recurrence: String): Date? {
        val cal = Calendar.getInstance(TimeZone.getDefault()).apply { time = due }
        when (recurrence.uppercase()) {
            "DAILY" -> cal.add(Calendar.DAY_OF_YEAR, 1)
            "WEEKLY" -> cal.add(Calendar.WEEK_OF_YEAR, 1)
            else -> return null
        }
        return cal.time
    }
}
