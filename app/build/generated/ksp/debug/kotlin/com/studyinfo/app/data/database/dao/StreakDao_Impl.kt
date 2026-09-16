package com.studyinfo.app.`data`.database.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.studyinfo.app.`data`.database.converter.Converters
import com.studyinfo.app.`data`.database.entity.StreakActivityEntity
import com.studyinfo.app.domain.model.SyncState
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
public class StreakDao_Impl(
  __db: RoomDatabase,
) : StreakDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfStreakActivityEntity: EntityInsertAdapter<StreakActivityEntity>

  private val __converters: Converters = Converters()
  init {
    this.__db = __db
    this.__insertAdapterOfStreakActivityEntity = object :
        EntityInsertAdapter<StreakActivityEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `streak_activity` (`dateKey`,`activityCount`,`lastActivityAt`,`updatedAt`,`syncState`) VALUES (?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: StreakActivityEntity) {
        statement.bindText(1, entity.dateKey)
        statement.bindLong(2, entity.activityCount.toLong())
        val _tmp: Long? = __converters.fromDate(entity.lastActivityAt)
        if (_tmp == null) {
          statement.bindNull(3)
        } else {
          statement.bindLong(3, _tmp)
        }
        val _tmp_1: Long? = __converters.fromDate(entity.updatedAt)
        if (_tmp_1 == null) {
          statement.bindNull(4)
        } else {
          statement.bindLong(4, _tmp_1)
        }
        val _tmp_2: String? = __converters.fromSyncState(entity.syncState)
        if (_tmp_2 == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmp_2)
        }
      }
    }
  }

  public override suspend fun upsert(activity: StreakActivityEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfStreakActivityEntity.insert(_connection, activity)
  }

  public override fun observeAll(): Flow<List<StreakActivityEntity>> {
    val _sql: String = "SELECT * FROM streak_activity ORDER BY dateKey DESC"
    return createFlow(__db, false, arrayOf("streak_activity")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfDateKey: Int = getColumnIndexOrThrow(_stmt, "dateKey")
        val _columnIndexOfActivityCount: Int = getColumnIndexOrThrow(_stmt, "activityCount")
        val _columnIndexOfLastActivityAt: Int = getColumnIndexOrThrow(_stmt, "lastActivityAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<StreakActivityEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: StreakActivityEntity
          val _tmpDateKey: String
          _tmpDateKey = _stmt.getText(_columnIndexOfDateKey)
          val _tmpActivityCount: Int
          _tmpActivityCount = _stmt.getLong(_columnIndexOfActivityCount).toInt()
          val _tmpLastActivityAt: Date
          val _tmp: Long?
          if (_stmt.isNull(_columnIndexOfLastActivityAt)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(_columnIndexOfLastActivityAt)
          }
          val _tmp_1: Date? = __converters.toDate(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpLastActivityAt = _tmp_1
          }
          val _tmpUpdatedAt: Date
          val _tmp_2: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_3: Date? = __converters.toDate(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_3
          }
          val _tmpSyncState: SyncState
          val _tmp_4: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_5: SyncState? = __converters.toSyncState(_tmp_4)
          if (_tmp_5 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_5
          }
          _item =
              StreakActivityEntity(_tmpDateKey,_tmpActivityCount,_tmpLastActivityAt,_tmpUpdatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getByDate(dateKey: String): StreakActivityEntity? {
    val _sql: String = "SELECT * FROM streak_activity WHERE dateKey = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, dateKey)
        val _columnIndexOfDateKey: Int = getColumnIndexOrThrow(_stmt, "dateKey")
        val _columnIndexOfActivityCount: Int = getColumnIndexOrThrow(_stmt, "activityCount")
        val _columnIndexOfLastActivityAt: Int = getColumnIndexOrThrow(_stmt, "lastActivityAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: StreakActivityEntity?
        if (_stmt.step()) {
          val _tmpDateKey: String
          _tmpDateKey = _stmt.getText(_columnIndexOfDateKey)
          val _tmpActivityCount: Int
          _tmpActivityCount = _stmt.getLong(_columnIndexOfActivityCount).toInt()
          val _tmpLastActivityAt: Date
          val _tmp: Long?
          if (_stmt.isNull(_columnIndexOfLastActivityAt)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(_columnIndexOfLastActivityAt)
          }
          val _tmp_1: Date? = __converters.toDate(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpLastActivityAt = _tmp_1
          }
          val _tmpUpdatedAt: Date
          val _tmp_2: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_3: Date? = __converters.toDate(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_3
          }
          val _tmpSyncState: SyncState
          val _tmp_4: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_5: SyncState? = __converters.toSyncState(_tmp_4)
          if (_tmp_5 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_5
          }
          _result =
              StreakActivityEntity(_tmpDateKey,_tmpActivityCount,_tmpLastActivityAt,_tmpUpdatedAt,_tmpSyncState)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun recent(limit: Int): List<StreakActivityEntity> {
    val _sql: String = "SELECT * FROM streak_activity ORDER BY dateKey DESC LIMIT ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, limit.toLong())
        val _columnIndexOfDateKey: Int = getColumnIndexOrThrow(_stmt, "dateKey")
        val _columnIndexOfActivityCount: Int = getColumnIndexOrThrow(_stmt, "activityCount")
        val _columnIndexOfLastActivityAt: Int = getColumnIndexOrThrow(_stmt, "lastActivityAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<StreakActivityEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: StreakActivityEntity
          val _tmpDateKey: String
          _tmpDateKey = _stmt.getText(_columnIndexOfDateKey)
          val _tmpActivityCount: Int
          _tmpActivityCount = _stmt.getLong(_columnIndexOfActivityCount).toInt()
          val _tmpLastActivityAt: Date
          val _tmp: Long?
          if (_stmt.isNull(_columnIndexOfLastActivityAt)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(_columnIndexOfLastActivityAt)
          }
          val _tmp_1: Date? = __converters.toDate(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpLastActivityAt = _tmp_1
          }
          val _tmpUpdatedAt: Date
          val _tmp_2: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_3: Date? = __converters.toDate(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_3
          }
          val _tmpSyncState: SyncState
          val _tmp_4: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_5: SyncState? = __converters.toSyncState(_tmp_4)
          if (_tmp_5 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_5
          }
          _item =
              StreakActivityEntity(_tmpDateKey,_tmpActivityCount,_tmpLastActivityAt,_tmpUpdatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun activeDays(): List<StreakActivityEntity> {
    val _sql: String = "SELECT * FROM streak_activity WHERE activityCount > 0 ORDER BY dateKey DESC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfDateKey: Int = getColumnIndexOrThrow(_stmt, "dateKey")
        val _columnIndexOfActivityCount: Int = getColumnIndexOrThrow(_stmt, "activityCount")
        val _columnIndexOfLastActivityAt: Int = getColumnIndexOrThrow(_stmt, "lastActivityAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<StreakActivityEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: StreakActivityEntity
          val _tmpDateKey: String
          _tmpDateKey = _stmt.getText(_columnIndexOfDateKey)
          val _tmpActivityCount: Int
          _tmpActivityCount = _stmt.getLong(_columnIndexOfActivityCount).toInt()
          val _tmpLastActivityAt: Date
          val _tmp: Long?
          if (_stmt.isNull(_columnIndexOfLastActivityAt)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(_columnIndexOfLastActivityAt)
          }
          val _tmp_1: Date? = __converters.toDate(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpLastActivityAt = _tmp_1
          }
          val _tmpUpdatedAt: Date
          val _tmp_2: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_3: Date? = __converters.toDate(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_3
          }
          val _tmpSyncState: SyncState
          val _tmp_4: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_5: SyncState? = __converters.toSyncState(_tmp_4)
          if (_tmp_5 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_5
          }
          _item =
              StreakActivityEntity(_tmpDateKey,_tmpActivityCount,_tmpLastActivityAt,_tmpUpdatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun markSync(dateKey: String, state: SyncState) {
    val _sql: String = "UPDATE streak_activity SET syncState = ? WHERE dateKey = ?"
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
        _stmt.bindText(_argIndex, dateKey)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clear() {
    val _sql: String = "DELETE FROM streak_activity"
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
