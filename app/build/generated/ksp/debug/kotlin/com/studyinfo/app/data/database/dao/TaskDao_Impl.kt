package com.studyinfo.app.`data`.database.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.studyinfo.app.`data`.database.converter.Converters
import com.studyinfo.app.`data`.database.entity.TaskEntity
import com.studyinfo.app.domain.model.Subject
import com.studyinfo.app.domain.model.SyncState
import com.studyinfo.app.domain.model.TaskPriority
import com.studyinfo.app.domain.model.TaskStatus
import java.util.Date
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class TaskDao_Impl(
  __db: RoomDatabase,
) : TaskDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfTaskEntity: EntityInsertAdapter<TaskEntity>

  private val __converters: Converters = Converters()
  init {
    this.__db = __db
    this.__insertAdapterOfTaskEntity = object : EntityInsertAdapter<TaskEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `tasks` (`id`,`title`,`description`,`subject`,`chapterName`,`topic`,`dueDate`,`dueTime`,`estimatedMinutes`,`priority`,`status`,`recurrence`,`notes`,`completedAt`,`createdAt`,`updatedAt`,`syncState`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TaskEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.title)
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpDescription)
        }
        val _tmpSubject: Subject? = entity.subject
        val _tmp: String? = __converters.fromSubject(_tmpSubject)
        if (_tmp == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmp)
        }
        val _tmpChapterName: String? = entity.chapterName
        if (_tmpChapterName == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpChapterName)
        }
        val _tmpTopic: String? = entity.topic
        if (_tmpTopic == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpTopic)
        }
        val _tmp_1: Long? = __converters.fromDate(entity.dueDate)
        if (_tmp_1 == null) {
          statement.bindNull(7)
        } else {
          statement.bindLong(7, _tmp_1)
        }
        val _tmpDueTime: Date? = entity.dueTime
        val _tmp_2: Long? = __converters.fromDate(_tmpDueTime)
        if (_tmp_2 == null) {
          statement.bindNull(8)
        } else {
          statement.bindLong(8, _tmp_2)
        }
        val _tmpEstimatedMinutes: Int? = entity.estimatedMinutes
        if (_tmpEstimatedMinutes == null) {
          statement.bindNull(9)
        } else {
          statement.bindLong(9, _tmpEstimatedMinutes.toLong())
        }
        val _tmp_3: String? = __converters.fromTaskPriority(entity.priority)
        if (_tmp_3 == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmp_3)
        }
        val _tmp_4: String? = __converters.fromTaskStatus(entity.status)
        if (_tmp_4 == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmp_4)
        }
        val _tmpRecurrence: String? = entity.recurrence
        if (_tmpRecurrence == null) {
          statement.bindNull(12)
        } else {
          statement.bindText(12, _tmpRecurrence)
        }
        val _tmpNotes: String? = entity.notes
        if (_tmpNotes == null) {
          statement.bindNull(13)
        } else {
          statement.bindText(13, _tmpNotes)
        }
        val _tmpCompletedAt: Date? = entity.completedAt
        val _tmp_5: Long? = __converters.fromDate(_tmpCompletedAt)
        if (_tmp_5 == null) {
          statement.bindNull(14)
        } else {
          statement.bindLong(14, _tmp_5)
        }
        val _tmp_6: Long? = __converters.fromDate(entity.createdAt)
        if (_tmp_6 == null) {
          statement.bindNull(15)
        } else {
          statement.bindLong(15, _tmp_6)
        }
        val _tmp_7: Long? = __converters.fromDate(entity.updatedAt)
        if (_tmp_7 == null) {
          statement.bindNull(16)
        } else {
          statement.bindLong(16, _tmp_7)
        }
        val _tmp_8: String? = __converters.fromSyncState(entity.syncState)
        if (_tmp_8 == null) {
          statement.bindNull(17)
        } else {
          statement.bindText(17, _tmp_8)
        }
      }
    }
  }

  public override suspend fun upsert(task: TaskEntity): Unit = performSuspending(__db, false, true)
      { _connection ->
    __insertAdapterOfTaskEntity.insert(_connection, task)
  }

  public override suspend fun upsertAll(tasks: List<TaskEntity>): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfTaskEntity.insert(_connection, tasks)
  }

  public override fun observeAll(): Flow<List<TaskEntity>> {
    val _sql: String =
        "SELECT * FROM tasks WHERE syncState != 'PENDING_DELETE' ORDER BY dueDate ASC, CASE priority WHEN 'URGENT' THEN 4 WHEN 'HIGH' THEN 3 WHEN 'MEDIUM' THEN 2 ELSE 1 END DESC"
    return createFlow(__db, false, arrayOf("tasks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfChapterName: Int = getColumnIndexOrThrow(_stmt, "chapterName")
        val _columnIndexOfTopic: Int = getColumnIndexOrThrow(_stmt, "topic")
        val _columnIndexOfDueDate: Int = getColumnIndexOrThrow(_stmt, "dueDate")
        val _columnIndexOfDueTime: Int = getColumnIndexOrThrow(_stmt, "dueTime")
        val _columnIndexOfEstimatedMinutes: Int = getColumnIndexOrThrow(_stmt, "estimatedMinutes")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfRecurrence: Int = getColumnIndexOrThrow(_stmt, "recurrence")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfCompletedAt: Int = getColumnIndexOrThrow(_stmt, "completedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<TaskEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TaskEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpSubject: Subject?
          val _tmp: String?
          if (_stmt.isNull(_columnIndexOfSubject)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_columnIndexOfSubject)
          }
          _tmpSubject = __converters.toSubject(_tmp)
          val _tmpChapterName: String?
          if (_stmt.isNull(_columnIndexOfChapterName)) {
            _tmpChapterName = null
          } else {
            _tmpChapterName = _stmt.getText(_columnIndexOfChapterName)
          }
          val _tmpTopic: String?
          if (_stmt.isNull(_columnIndexOfTopic)) {
            _tmpTopic = null
          } else {
            _tmpTopic = _stmt.getText(_columnIndexOfTopic)
          }
          val _tmpDueDate: Date
          val _tmp_1: Long?
          if (_stmt.isNull(_columnIndexOfDueDate)) {
            _tmp_1 = null
          } else {
            _tmp_1 = _stmt.getLong(_columnIndexOfDueDate)
          }
          val _tmp_2: Date? = __converters.toDate(_tmp_1)
          if (_tmp_2 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpDueDate = _tmp_2
          }
          val _tmpDueTime: Date?
          val _tmp_3: Long?
          if (_stmt.isNull(_columnIndexOfDueTime)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getLong(_columnIndexOfDueTime)
          }
          _tmpDueTime = __converters.toDate(_tmp_3)
          val _tmpEstimatedMinutes: Int?
          if (_stmt.isNull(_columnIndexOfEstimatedMinutes)) {
            _tmpEstimatedMinutes = null
          } else {
            _tmpEstimatedMinutes = _stmt.getLong(_columnIndexOfEstimatedMinutes).toInt()
          }
          val _tmpPriority: TaskPriority
          val _tmp_4: String?
          if (_stmt.isNull(_columnIndexOfPriority)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getText(_columnIndexOfPriority)
          }
          val _tmp_5: TaskPriority? = __converters.toTaskPriority(_tmp_4)
          if (_tmp_5 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.TaskPriority', but it was NULL.")
          } else {
            _tmpPriority = _tmp_5
          }
          val _tmpStatus: TaskStatus
          val _tmp_6: String?
          if (_stmt.isNull(_columnIndexOfStatus)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getText(_columnIndexOfStatus)
          }
          val _tmp_7: TaskStatus? = __converters.toTaskStatus(_tmp_6)
          if (_tmp_7 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.TaskStatus', but it was NULL.")
          } else {
            _tmpStatus = _tmp_7
          }
          val _tmpRecurrence: String?
          if (_stmt.isNull(_columnIndexOfRecurrence)) {
            _tmpRecurrence = null
          } else {
            _tmpRecurrence = _stmt.getText(_columnIndexOfRecurrence)
          }
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpCompletedAt: Date?
          val _tmp_8: Long?
          if (_stmt.isNull(_columnIndexOfCompletedAt)) {
            _tmp_8 = null
          } else {
            _tmp_8 = _stmt.getLong(_columnIndexOfCompletedAt)
          }
          _tmpCompletedAt = __converters.toDate(_tmp_8)
          val _tmpCreatedAt: Date
          val _tmp_9: Long?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmp_9 = null
          } else {
            _tmp_9 = _stmt.getLong(_columnIndexOfCreatedAt)
          }
          val _tmp_10: Date? = __converters.toDate(_tmp_9)
          if (_tmp_10 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_10
          }
          val _tmpUpdatedAt: Date
          val _tmp_11: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_11 = null
          } else {
            _tmp_11 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_12: Date? = __converters.toDate(_tmp_11)
          if (_tmp_12 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_12
          }
          val _tmpSyncState: SyncState
          val _tmp_13: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_13 = null
          } else {
            _tmp_13 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_14: SyncState? = __converters.toSyncState(_tmp_13)
          if (_tmp_14 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_14
          }
          _item =
              TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpSubject,_tmpChapterName,_tmpTopic,_tmpDueDate,_tmpDueTime,_tmpEstimatedMinutes,_tmpPriority,_tmpStatus,_tmpRecurrence,_tmpNotes,_tmpCompletedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeForRange(start: Long, end: Long): Flow<List<TaskEntity>> {
    val _sql: String =
        "SELECT * FROM tasks WHERE syncState != 'PENDING_DELETE' AND dueDate BETWEEN ? AND ? ORDER BY dueDate ASC, CASE priority WHEN 'URGENT' THEN 4 WHEN 'HIGH' THEN 3 WHEN 'MEDIUM' THEN 2 ELSE 1 END DESC"
    return createFlow(__db, false, arrayOf("tasks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, start)
        _argIndex = 2
        _stmt.bindLong(_argIndex, end)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfChapterName: Int = getColumnIndexOrThrow(_stmt, "chapterName")
        val _columnIndexOfTopic: Int = getColumnIndexOrThrow(_stmt, "topic")
        val _columnIndexOfDueDate: Int = getColumnIndexOrThrow(_stmt, "dueDate")
        val _columnIndexOfDueTime: Int = getColumnIndexOrThrow(_stmt, "dueTime")
        val _columnIndexOfEstimatedMinutes: Int = getColumnIndexOrThrow(_stmt, "estimatedMinutes")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfRecurrence: Int = getColumnIndexOrThrow(_stmt, "recurrence")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfCompletedAt: Int = getColumnIndexOrThrow(_stmt, "completedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<TaskEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TaskEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpSubject: Subject?
          val _tmp: String?
          if (_stmt.isNull(_columnIndexOfSubject)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_columnIndexOfSubject)
          }
          _tmpSubject = __converters.toSubject(_tmp)
          val _tmpChapterName: String?
          if (_stmt.isNull(_columnIndexOfChapterName)) {
            _tmpChapterName = null
          } else {
            _tmpChapterName = _stmt.getText(_columnIndexOfChapterName)
          }
          val _tmpTopic: String?
          if (_stmt.isNull(_columnIndexOfTopic)) {
            _tmpTopic = null
          } else {
            _tmpTopic = _stmt.getText(_columnIndexOfTopic)
          }
          val _tmpDueDate: Date
          val _tmp_1: Long?
          if (_stmt.isNull(_columnIndexOfDueDate)) {
            _tmp_1 = null
          } else {
            _tmp_1 = _stmt.getLong(_columnIndexOfDueDate)
          }
          val _tmp_2: Date? = __converters.toDate(_tmp_1)
          if (_tmp_2 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpDueDate = _tmp_2
          }
          val _tmpDueTime: Date?
          val _tmp_3: Long?
          if (_stmt.isNull(_columnIndexOfDueTime)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getLong(_columnIndexOfDueTime)
          }
          _tmpDueTime = __converters.toDate(_tmp_3)
          val _tmpEstimatedMinutes: Int?
          if (_stmt.isNull(_columnIndexOfEstimatedMinutes)) {
            _tmpEstimatedMinutes = null
          } else {
            _tmpEstimatedMinutes = _stmt.getLong(_columnIndexOfEstimatedMinutes).toInt()
          }
          val _tmpPriority: TaskPriority
          val _tmp_4: String?
          if (_stmt.isNull(_columnIndexOfPriority)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getText(_columnIndexOfPriority)
          }
          val _tmp_5: TaskPriority? = __converters.toTaskPriority(_tmp_4)
          if (_tmp_5 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.TaskPriority', but it was NULL.")
          } else {
            _tmpPriority = _tmp_5
          }
          val _tmpStatus: TaskStatus
          val _tmp_6: String?
          if (_stmt.isNull(_columnIndexOfStatus)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getText(_columnIndexOfStatus)
          }
          val _tmp_7: TaskStatus? = __converters.toTaskStatus(_tmp_6)
          if (_tmp_7 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.TaskStatus', but it was NULL.")
          } else {
            _tmpStatus = _tmp_7
          }
          val _tmpRecurrence: String?
          if (_stmt.isNull(_columnIndexOfRecurrence)) {
            _tmpRecurrence = null
          } else {
            _tmpRecurrence = _stmt.getText(_columnIndexOfRecurrence)
          }
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpCompletedAt: Date?
          val _tmp_8: Long?
          if (_stmt.isNull(_columnIndexOfCompletedAt)) {
            _tmp_8 = null
          } else {
            _tmp_8 = _stmt.getLong(_columnIndexOfCompletedAt)
          }
          _tmpCompletedAt = __converters.toDate(_tmp_8)
          val _tmpCreatedAt: Date
          val _tmp_9: Long?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmp_9 = null
          } else {
            _tmp_9 = _stmt.getLong(_columnIndexOfCreatedAt)
          }
          val _tmp_10: Date? = __converters.toDate(_tmp_9)
          if (_tmp_10 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_10
          }
          val _tmpUpdatedAt: Date
          val _tmp_11: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_11 = null
          } else {
            _tmp_11 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_12: Date? = __converters.toDate(_tmp_11)
          if (_tmp_12 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_12
          }
          val _tmpSyncState: SyncState
          val _tmp_13: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_13 = null
          } else {
            _tmp_13 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_14: SyncState? = __converters.toSyncState(_tmp_13)
          if (_tmp_14 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_14
          }
          _item =
              TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpSubject,_tmpChapterName,_tmpTopic,_tmpDueDate,_tmpDueTime,_tmpEstimatedMinutes,_tmpPriority,_tmpStatus,_tmpRecurrence,_tmpNotes,_tmpCompletedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeOverdue(
    now: Long,
    completed: TaskStatus,
    skipped: TaskStatus,
  ): Flow<List<TaskEntity>> {
    val _sql: String =
        "SELECT * FROM tasks WHERE syncState != 'PENDING_DELETE' AND dueDate < ? AND status != ? AND status != ? ORDER BY dueDate ASC"
    return createFlow(__db, false, arrayOf("tasks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, now)
        _argIndex = 2
        val _tmp: String? = __converters.fromTaskStatus(completed)
        if (_tmp == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, _tmp)
        }
        _argIndex = 3
        val _tmp_1: String? = __converters.fromTaskStatus(skipped)
        if (_tmp_1 == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, _tmp_1)
        }
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfChapterName: Int = getColumnIndexOrThrow(_stmt, "chapterName")
        val _columnIndexOfTopic: Int = getColumnIndexOrThrow(_stmt, "topic")
        val _columnIndexOfDueDate: Int = getColumnIndexOrThrow(_stmt, "dueDate")
        val _columnIndexOfDueTime: Int = getColumnIndexOrThrow(_stmt, "dueTime")
        val _columnIndexOfEstimatedMinutes: Int = getColumnIndexOrThrow(_stmt, "estimatedMinutes")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfRecurrence: Int = getColumnIndexOrThrow(_stmt, "recurrence")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfCompletedAt: Int = getColumnIndexOrThrow(_stmt, "completedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<TaskEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TaskEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpSubject: Subject?
          val _tmp_2: String?
          if (_stmt.isNull(_columnIndexOfSubject)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_columnIndexOfSubject)
          }
          _tmpSubject = __converters.toSubject(_tmp_2)
          val _tmpChapterName: String?
          if (_stmt.isNull(_columnIndexOfChapterName)) {
            _tmpChapterName = null
          } else {
            _tmpChapterName = _stmt.getText(_columnIndexOfChapterName)
          }
          val _tmpTopic: String?
          if (_stmt.isNull(_columnIndexOfTopic)) {
            _tmpTopic = null
          } else {
            _tmpTopic = _stmt.getText(_columnIndexOfTopic)
          }
          val _tmpDueDate: Date
          val _tmp_3: Long?
          if (_stmt.isNull(_columnIndexOfDueDate)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getLong(_columnIndexOfDueDate)
          }
          val _tmp_4: Date? = __converters.toDate(_tmp_3)
          if (_tmp_4 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpDueDate = _tmp_4
          }
          val _tmpDueTime: Date?
          val _tmp_5: Long?
          if (_stmt.isNull(_columnIndexOfDueTime)) {
            _tmp_5 = null
          } else {
            _tmp_5 = _stmt.getLong(_columnIndexOfDueTime)
          }
          _tmpDueTime = __converters.toDate(_tmp_5)
          val _tmpEstimatedMinutes: Int?
          if (_stmt.isNull(_columnIndexOfEstimatedMinutes)) {
            _tmpEstimatedMinutes = null
          } else {
            _tmpEstimatedMinutes = _stmt.getLong(_columnIndexOfEstimatedMinutes).toInt()
          }
          val _tmpPriority: TaskPriority
          val _tmp_6: String?
          if (_stmt.isNull(_columnIndexOfPriority)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getText(_columnIndexOfPriority)
          }
          val _tmp_7: TaskPriority? = __converters.toTaskPriority(_tmp_6)
          if (_tmp_7 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.TaskPriority', but it was NULL.")
          } else {
            _tmpPriority = _tmp_7
          }
          val _tmpStatus: TaskStatus
          val _tmp_8: String?
          if (_stmt.isNull(_columnIndexOfStatus)) {
            _tmp_8 = null
          } else {
            _tmp_8 = _stmt.getText(_columnIndexOfStatus)
          }
          val _tmp_9: TaskStatus? = __converters.toTaskStatus(_tmp_8)
          if (_tmp_9 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.TaskStatus', but it was NULL.")
          } else {
            _tmpStatus = _tmp_9
          }
          val _tmpRecurrence: String?
          if (_stmt.isNull(_columnIndexOfRecurrence)) {
            _tmpRecurrence = null
          } else {
            _tmpRecurrence = _stmt.getText(_columnIndexOfRecurrence)
          }
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpCompletedAt: Date?
          val _tmp_10: Long?
          if (_stmt.isNull(_columnIndexOfCompletedAt)) {
            _tmp_10 = null
          } else {
            _tmp_10 = _stmt.getLong(_columnIndexOfCompletedAt)
          }
          _tmpCompletedAt = __converters.toDate(_tmp_10)
          val _tmpCreatedAt: Date
          val _tmp_11: Long?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmp_11 = null
          } else {
            _tmp_11 = _stmt.getLong(_columnIndexOfCreatedAt)
          }
          val _tmp_12: Date? = __converters.toDate(_tmp_11)
          if (_tmp_12 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_12
          }
          val _tmpUpdatedAt: Date
          val _tmp_13: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_13 = null
          } else {
            _tmp_13 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_14: Date? = __converters.toDate(_tmp_13)
          if (_tmp_14 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_14
          }
          val _tmpSyncState: SyncState
          val _tmp_15: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_15 = null
          } else {
            _tmp_15 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_16: SyncState? = __converters.toSyncState(_tmp_15)
          if (_tmp_16 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_16
          }
          _item =
              TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpSubject,_tmpChapterName,_tmpTopic,_tmpDueDate,_tmpDueTime,_tmpEstimatedMinutes,_tmpPriority,_tmpStatus,_tmpRecurrence,_tmpNotes,_tmpCompletedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeByStatus(status: TaskStatus): Flow<List<TaskEntity>> {
    val _sql: String =
        "SELECT * FROM tasks WHERE syncState != 'PENDING_DELETE' AND status = ? ORDER BY completedAt DESC"
    return createFlow(__db, false, arrayOf("tasks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: String? = __converters.fromTaskStatus(status)
        if (_tmp == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, _tmp)
        }
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfChapterName: Int = getColumnIndexOrThrow(_stmt, "chapterName")
        val _columnIndexOfTopic: Int = getColumnIndexOrThrow(_stmt, "topic")
        val _columnIndexOfDueDate: Int = getColumnIndexOrThrow(_stmt, "dueDate")
        val _columnIndexOfDueTime: Int = getColumnIndexOrThrow(_stmt, "dueTime")
        val _columnIndexOfEstimatedMinutes: Int = getColumnIndexOrThrow(_stmt, "estimatedMinutes")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfRecurrence: Int = getColumnIndexOrThrow(_stmt, "recurrence")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfCompletedAt: Int = getColumnIndexOrThrow(_stmt, "completedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<TaskEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TaskEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpSubject: Subject?
          val _tmp_1: String?
          if (_stmt.isNull(_columnIndexOfSubject)) {
            _tmp_1 = null
          } else {
            _tmp_1 = _stmt.getText(_columnIndexOfSubject)
          }
          _tmpSubject = __converters.toSubject(_tmp_1)
          val _tmpChapterName: String?
          if (_stmt.isNull(_columnIndexOfChapterName)) {
            _tmpChapterName = null
          } else {
            _tmpChapterName = _stmt.getText(_columnIndexOfChapterName)
          }
          val _tmpTopic: String?
          if (_stmt.isNull(_columnIndexOfTopic)) {
            _tmpTopic = null
          } else {
            _tmpTopic = _stmt.getText(_columnIndexOfTopic)
          }
          val _tmpDueDate: Date
          val _tmp_2: Long?
          if (_stmt.isNull(_columnIndexOfDueDate)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getLong(_columnIndexOfDueDate)
          }
          val _tmp_3: Date? = __converters.toDate(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpDueDate = _tmp_3
          }
          val _tmpDueTime: Date?
          val _tmp_4: Long?
          if (_stmt.isNull(_columnIndexOfDueTime)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getLong(_columnIndexOfDueTime)
          }
          _tmpDueTime = __converters.toDate(_tmp_4)
          val _tmpEstimatedMinutes: Int?
          if (_stmt.isNull(_columnIndexOfEstimatedMinutes)) {
            _tmpEstimatedMinutes = null
          } else {
            _tmpEstimatedMinutes = _stmt.getLong(_columnIndexOfEstimatedMinutes).toInt()
          }
          val _tmpPriority: TaskPriority
          val _tmp_5: String?
          if (_stmt.isNull(_columnIndexOfPriority)) {
            _tmp_5 = null
          } else {
            _tmp_5 = _stmt.getText(_columnIndexOfPriority)
          }
          val _tmp_6: TaskPriority? = __converters.toTaskPriority(_tmp_5)
          if (_tmp_6 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.TaskPriority', but it was NULL.")
          } else {
            _tmpPriority = _tmp_6
          }
          val _tmpStatus: TaskStatus
          val _tmp_7: String?
          if (_stmt.isNull(_columnIndexOfStatus)) {
            _tmp_7 = null
          } else {
            _tmp_7 = _stmt.getText(_columnIndexOfStatus)
          }
          val _tmp_8: TaskStatus? = __converters.toTaskStatus(_tmp_7)
          if (_tmp_8 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.TaskStatus', but it was NULL.")
          } else {
            _tmpStatus = _tmp_8
          }
          val _tmpRecurrence: String?
          if (_stmt.isNull(_columnIndexOfRecurrence)) {
            _tmpRecurrence = null
          } else {
            _tmpRecurrence = _stmt.getText(_columnIndexOfRecurrence)
          }
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpCompletedAt: Date?
          val _tmp_9: Long?
          if (_stmt.isNull(_columnIndexOfCompletedAt)) {
            _tmp_9 = null
          } else {
            _tmp_9 = _stmt.getLong(_columnIndexOfCompletedAt)
          }
          _tmpCompletedAt = __converters.toDate(_tmp_9)
          val _tmpCreatedAt: Date
          val _tmp_10: Long?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmp_10 = null
          } else {
            _tmp_10 = _stmt.getLong(_columnIndexOfCreatedAt)
          }
          val _tmp_11: Date? = __converters.toDate(_tmp_10)
          if (_tmp_11 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_11
          }
          val _tmpUpdatedAt: Date
          val _tmp_12: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_12 = null
          } else {
            _tmp_12 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_13: Date? = __converters.toDate(_tmp_12)
          if (_tmp_13 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_13
          }
          val _tmpSyncState: SyncState
          val _tmp_14: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_14 = null
          } else {
            _tmp_14 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_15: SyncState? = __converters.toSyncState(_tmp_14)
          if (_tmp_15 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_15
          }
          _item =
              TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpSubject,_tmpChapterName,_tmpTopic,_tmpDueDate,_tmpDueTime,_tmpEstimatedMinutes,_tmpPriority,_tmpStatus,_tmpRecurrence,_tmpNotes,_tmpCompletedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeById(id: String): Flow<TaskEntity?> {
    val _sql: String = "SELECT * FROM tasks WHERE id = ?"
    return createFlow(__db, false, arrayOf("tasks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfChapterName: Int = getColumnIndexOrThrow(_stmt, "chapterName")
        val _columnIndexOfTopic: Int = getColumnIndexOrThrow(_stmt, "topic")
        val _columnIndexOfDueDate: Int = getColumnIndexOrThrow(_stmt, "dueDate")
        val _columnIndexOfDueTime: Int = getColumnIndexOrThrow(_stmt, "dueTime")
        val _columnIndexOfEstimatedMinutes: Int = getColumnIndexOrThrow(_stmt, "estimatedMinutes")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfRecurrence: Int = getColumnIndexOrThrow(_stmt, "recurrence")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfCompletedAt: Int = getColumnIndexOrThrow(_stmt, "completedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: TaskEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpSubject: Subject?
          val _tmp: String?
          if (_stmt.isNull(_columnIndexOfSubject)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_columnIndexOfSubject)
          }
          _tmpSubject = __converters.toSubject(_tmp)
          val _tmpChapterName: String?
          if (_stmt.isNull(_columnIndexOfChapterName)) {
            _tmpChapterName = null
          } else {
            _tmpChapterName = _stmt.getText(_columnIndexOfChapterName)
          }
          val _tmpTopic: String?
          if (_stmt.isNull(_columnIndexOfTopic)) {
            _tmpTopic = null
          } else {
            _tmpTopic = _stmt.getText(_columnIndexOfTopic)
          }
          val _tmpDueDate: Date
          val _tmp_1: Long?
          if (_stmt.isNull(_columnIndexOfDueDate)) {
            _tmp_1 = null
          } else {
            _tmp_1 = _stmt.getLong(_columnIndexOfDueDate)
          }
          val _tmp_2: Date? = __converters.toDate(_tmp_1)
          if (_tmp_2 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpDueDate = _tmp_2
          }
          val _tmpDueTime: Date?
          val _tmp_3: Long?
          if (_stmt.isNull(_columnIndexOfDueTime)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getLong(_columnIndexOfDueTime)
          }
          _tmpDueTime = __converters.toDate(_tmp_3)
          val _tmpEstimatedMinutes: Int?
          if (_stmt.isNull(_columnIndexOfEstimatedMinutes)) {
            _tmpEstimatedMinutes = null
          } else {
            _tmpEstimatedMinutes = _stmt.getLong(_columnIndexOfEstimatedMinutes).toInt()
          }
          val _tmpPriority: TaskPriority
          val _tmp_4: String?
          if (_stmt.isNull(_columnIndexOfPriority)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getText(_columnIndexOfPriority)
          }
          val _tmp_5: TaskPriority? = __converters.toTaskPriority(_tmp_4)
          if (_tmp_5 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.TaskPriority', but it was NULL.")
          } else {
            _tmpPriority = _tmp_5
          }
          val _tmpStatus: TaskStatus
          val _tmp_6: String?
          if (_stmt.isNull(_columnIndexOfStatus)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getText(_columnIndexOfStatus)
          }
          val _tmp_7: TaskStatus? = __converters.toTaskStatus(_tmp_6)
          if (_tmp_7 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.TaskStatus', but it was NULL.")
          } else {
            _tmpStatus = _tmp_7
          }
          val _tmpRecurrence: String?
          if (_stmt.isNull(_columnIndexOfRecurrence)) {
            _tmpRecurrence = null
          } else {
            _tmpRecurrence = _stmt.getText(_columnIndexOfRecurrence)
          }
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpCompletedAt: Date?
          val _tmp_8: Long?
          if (_stmt.isNull(_columnIndexOfCompletedAt)) {
            _tmp_8 = null
          } else {
            _tmp_8 = _stmt.getLong(_columnIndexOfCompletedAt)
          }
          _tmpCompletedAt = __converters.toDate(_tmp_8)
          val _tmpCreatedAt: Date
          val _tmp_9: Long?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmp_9 = null
          } else {
            _tmp_9 = _stmt.getLong(_columnIndexOfCreatedAt)
          }
          val _tmp_10: Date? = __converters.toDate(_tmp_9)
          if (_tmp_10 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_10
          }
          val _tmpUpdatedAt: Date
          val _tmp_11: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_11 = null
          } else {
            _tmp_11 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_12: Date? = __converters.toDate(_tmp_11)
          if (_tmp_12 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_12
          }
          val _tmpSyncState: SyncState
          val _tmp_13: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_13 = null
          } else {
            _tmp_13 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_14: SyncState? = __converters.toSyncState(_tmp_13)
          if (_tmp_14 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_14
          }
          _result =
              TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpSubject,_tmpChapterName,_tmpTopic,_tmpDueDate,_tmpDueTime,_tmpEstimatedMinutes,_tmpPriority,_tmpStatus,_tmpRecurrence,_tmpNotes,_tmpCompletedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getById(id: String): TaskEntity? {
    val _sql: String = "SELECT * FROM tasks WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfChapterName: Int = getColumnIndexOrThrow(_stmt, "chapterName")
        val _columnIndexOfTopic: Int = getColumnIndexOrThrow(_stmt, "topic")
        val _columnIndexOfDueDate: Int = getColumnIndexOrThrow(_stmt, "dueDate")
        val _columnIndexOfDueTime: Int = getColumnIndexOrThrow(_stmt, "dueTime")
        val _columnIndexOfEstimatedMinutes: Int = getColumnIndexOrThrow(_stmt, "estimatedMinutes")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfRecurrence: Int = getColumnIndexOrThrow(_stmt, "recurrence")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfCompletedAt: Int = getColumnIndexOrThrow(_stmt, "completedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: TaskEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpSubject: Subject?
          val _tmp: String?
          if (_stmt.isNull(_columnIndexOfSubject)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_columnIndexOfSubject)
          }
          _tmpSubject = __converters.toSubject(_tmp)
          val _tmpChapterName: String?
          if (_stmt.isNull(_columnIndexOfChapterName)) {
            _tmpChapterName = null
          } else {
            _tmpChapterName = _stmt.getText(_columnIndexOfChapterName)
          }
          val _tmpTopic: String?
          if (_stmt.isNull(_columnIndexOfTopic)) {
            _tmpTopic = null
          } else {
            _tmpTopic = _stmt.getText(_columnIndexOfTopic)
          }
          val _tmpDueDate: Date
          val _tmp_1: Long?
          if (_stmt.isNull(_columnIndexOfDueDate)) {
            _tmp_1 = null
          } else {
            _tmp_1 = _stmt.getLong(_columnIndexOfDueDate)
          }
          val _tmp_2: Date? = __converters.toDate(_tmp_1)
          if (_tmp_2 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpDueDate = _tmp_2
          }
          val _tmpDueTime: Date?
          val _tmp_3: Long?
          if (_stmt.isNull(_columnIndexOfDueTime)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getLong(_columnIndexOfDueTime)
          }
          _tmpDueTime = __converters.toDate(_tmp_3)
          val _tmpEstimatedMinutes: Int?
          if (_stmt.isNull(_columnIndexOfEstimatedMinutes)) {
            _tmpEstimatedMinutes = null
          } else {
            _tmpEstimatedMinutes = _stmt.getLong(_columnIndexOfEstimatedMinutes).toInt()
          }
          val _tmpPriority: TaskPriority
          val _tmp_4: String?
          if (_stmt.isNull(_columnIndexOfPriority)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getText(_columnIndexOfPriority)
          }
          val _tmp_5: TaskPriority? = __converters.toTaskPriority(_tmp_4)
          if (_tmp_5 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.TaskPriority', but it was NULL.")
          } else {
            _tmpPriority = _tmp_5
          }
          val _tmpStatus: TaskStatus
          val _tmp_6: String?
          if (_stmt.isNull(_columnIndexOfStatus)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getText(_columnIndexOfStatus)
          }
          val _tmp_7: TaskStatus? = __converters.toTaskStatus(_tmp_6)
          if (_tmp_7 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.TaskStatus', but it was NULL.")
          } else {
            _tmpStatus = _tmp_7
          }
          val _tmpRecurrence: String?
          if (_stmt.isNull(_columnIndexOfRecurrence)) {
            _tmpRecurrence = null
          } else {
            _tmpRecurrence = _stmt.getText(_columnIndexOfRecurrence)
          }
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpCompletedAt: Date?
          val _tmp_8: Long?
          if (_stmt.isNull(_columnIndexOfCompletedAt)) {
            _tmp_8 = null
          } else {
            _tmp_8 = _stmt.getLong(_columnIndexOfCompletedAt)
          }
          _tmpCompletedAt = __converters.toDate(_tmp_8)
          val _tmpCreatedAt: Date
          val _tmp_9: Long?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmp_9 = null
          } else {
            _tmp_9 = _stmt.getLong(_columnIndexOfCreatedAt)
          }
          val _tmp_10: Date? = __converters.toDate(_tmp_9)
          if (_tmp_10 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_10
          }
          val _tmpUpdatedAt: Date
          val _tmp_11: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_11 = null
          } else {
            _tmp_11 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_12: Date? = __converters.toDate(_tmp_11)
          if (_tmp_12 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_12
          }
          val _tmpSyncState: SyncState
          val _tmp_13: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_13 = null
          } else {
            _tmp_13 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_14: SyncState? = __converters.toSyncState(_tmp_13)
          if (_tmp_14 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_14
          }
          _result =
              TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpSubject,_tmpChapterName,_tmpTopic,_tmpDueDate,_tmpDueTime,_tmpEstimatedMinutes,_tmpPriority,_tmpStatus,_tmpRecurrence,_tmpNotes,_tmpCompletedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeCompletedCountForRange(
    start: Long,
    end: Long,
    status: TaskStatus,
  ): Flow<Int> {
    val _sql: String =
        "SELECT COUNT(*) FROM tasks WHERE syncState != 'PENDING_DELETE' AND dueDate BETWEEN ? AND ? AND status = ?"
    return createFlow(__db, false, arrayOf("tasks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, start)
        _argIndex = 2
        _stmt.bindLong(_argIndex, end)
        _argIndex = 3
        val _tmp: String? = __converters.fromTaskStatus(status)
        if (_tmp == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, _tmp)
        }
        val _result: Int
        if (_stmt.step()) {
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(0).toInt()
          _result = _tmp_1
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeTotalCountForRange(start: Long, end: Long): Flow<Int> {
    val _sql: String =
        "SELECT COUNT(*) FROM tasks WHERE syncState != 'PENDING_DELETE' AND dueDate BETWEEN ? AND ?"
    return createFlow(__db, false, arrayOf("tasks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, start)
        _argIndex = 2
        _stmt.bindLong(_argIndex, end)
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun pendingChanges(synced: SyncState): List<TaskEntity> {
    val _sql: String = "SELECT * FROM tasks WHERE syncState != ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: String? = __converters.fromSyncState(synced)
        if (_tmp == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, _tmp)
        }
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfChapterName: Int = getColumnIndexOrThrow(_stmt, "chapterName")
        val _columnIndexOfTopic: Int = getColumnIndexOrThrow(_stmt, "topic")
        val _columnIndexOfDueDate: Int = getColumnIndexOrThrow(_stmt, "dueDate")
        val _columnIndexOfDueTime: Int = getColumnIndexOrThrow(_stmt, "dueTime")
        val _columnIndexOfEstimatedMinutes: Int = getColumnIndexOrThrow(_stmt, "estimatedMinutes")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfRecurrence: Int = getColumnIndexOrThrow(_stmt, "recurrence")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfCompletedAt: Int = getColumnIndexOrThrow(_stmt, "completedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<TaskEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TaskEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpSubject: Subject?
          val _tmp_1: String?
          if (_stmt.isNull(_columnIndexOfSubject)) {
            _tmp_1 = null
          } else {
            _tmp_1 = _stmt.getText(_columnIndexOfSubject)
          }
          _tmpSubject = __converters.toSubject(_tmp_1)
          val _tmpChapterName: String?
          if (_stmt.isNull(_columnIndexOfChapterName)) {
            _tmpChapterName = null
          } else {
            _tmpChapterName = _stmt.getText(_columnIndexOfChapterName)
          }
          val _tmpTopic: String?
          if (_stmt.isNull(_columnIndexOfTopic)) {
            _tmpTopic = null
          } else {
            _tmpTopic = _stmt.getText(_columnIndexOfTopic)
          }
          val _tmpDueDate: Date
          val _tmp_2: Long?
          if (_stmt.isNull(_columnIndexOfDueDate)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getLong(_columnIndexOfDueDate)
          }
          val _tmp_3: Date? = __converters.toDate(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpDueDate = _tmp_3
          }
          val _tmpDueTime: Date?
          val _tmp_4: Long?
          if (_stmt.isNull(_columnIndexOfDueTime)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getLong(_columnIndexOfDueTime)
          }
          _tmpDueTime = __converters.toDate(_tmp_4)
          val _tmpEstimatedMinutes: Int?
          if (_stmt.isNull(_columnIndexOfEstimatedMinutes)) {
            _tmpEstimatedMinutes = null
          } else {
            _tmpEstimatedMinutes = _stmt.getLong(_columnIndexOfEstimatedMinutes).toInt()
          }
          val _tmpPriority: TaskPriority
          val _tmp_5: String?
          if (_stmt.isNull(_columnIndexOfPriority)) {
            _tmp_5 = null
          } else {
            _tmp_5 = _stmt.getText(_columnIndexOfPriority)
          }
          val _tmp_6: TaskPriority? = __converters.toTaskPriority(_tmp_5)
          if (_tmp_6 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.TaskPriority', but it was NULL.")
          } else {
            _tmpPriority = _tmp_6
          }
          val _tmpStatus: TaskStatus
          val _tmp_7: String?
          if (_stmt.isNull(_columnIndexOfStatus)) {
            _tmp_7 = null
          } else {
            _tmp_7 = _stmt.getText(_columnIndexOfStatus)
          }
          val _tmp_8: TaskStatus? = __converters.toTaskStatus(_tmp_7)
          if (_tmp_8 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.TaskStatus', but it was NULL.")
          } else {
            _tmpStatus = _tmp_8
          }
          val _tmpRecurrence: String?
          if (_stmt.isNull(_columnIndexOfRecurrence)) {
            _tmpRecurrence = null
          } else {
            _tmpRecurrence = _stmt.getText(_columnIndexOfRecurrence)
          }
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpCompletedAt: Date?
          val _tmp_9: Long?
          if (_stmt.isNull(_columnIndexOfCompletedAt)) {
            _tmp_9 = null
          } else {
            _tmp_9 = _stmt.getLong(_columnIndexOfCompletedAt)
          }
          _tmpCompletedAt = __converters.toDate(_tmp_9)
          val _tmpCreatedAt: Date
          val _tmp_10: Long?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmp_10 = null
          } else {
            _tmp_10 = _stmt.getLong(_columnIndexOfCreatedAt)
          }
          val _tmp_11: Date? = __converters.toDate(_tmp_10)
          if (_tmp_11 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_11
          }
          val _tmpUpdatedAt: Date
          val _tmp_12: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_12 = null
          } else {
            _tmp_12 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_13: Date? = __converters.toDate(_tmp_12)
          if (_tmp_13 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_13
          }
          val _tmpSyncState: SyncState
          val _tmp_14: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_14 = null
          } else {
            _tmp_14 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_15: SyncState? = __converters.toSyncState(_tmp_14)
          if (_tmp_15 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_15
          }
          _item =
              TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpSubject,_tmpChapterName,_tmpTopic,_tmpDueDate,_tmpDueTime,_tmpEstimatedMinutes,_tmpPriority,_tmpStatus,_tmpRecurrence,_tmpNotes,_tmpCompletedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun setStatus(
    id: String,
    status: TaskStatus,
    completedAt: Long?,
    now: Long,
    pending: SyncState,
  ) {
    val _sql: String =
        "UPDATE tasks SET status = ?, completedAt = ?, updatedAt = ?, syncState = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: String? = __converters.fromTaskStatus(status)
        if (_tmp == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, _tmp)
        }
        _argIndex = 2
        if (completedAt == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindLong(_argIndex, completedAt)
        }
        _argIndex = 3
        _stmt.bindLong(_argIndex, now)
        _argIndex = 4
        val _tmp_1: String? = __converters.fromSyncState(pending)
        if (_tmp_1 == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, _tmp_1)
        }
        _argIndex = 5
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun markSync(id: String, state: SyncState) {
    val _sql: String = "UPDATE tasks SET syncState = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: String? = __converters.fromSyncState(state)
        if (_tmp == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, _tmp)
        }
        _argIndex = 2
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun delete(id: String) {
    val _sql: String = "DELETE FROM tasks WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun hardDelete(id: String) {
    val _sql: String = "DELETE FROM tasks WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clear() {
    val _sql: String = "DELETE FROM tasks"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
