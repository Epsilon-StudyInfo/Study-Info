package com.studyinfo.app.`data`.database.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.studyinfo.app.`data`.database.converter.Converters
import com.studyinfo.app.`data`.database.entity.SyncQueueEntity
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
public class SyncQueueDao_Impl(
  __db: RoomDatabase,
) : SyncQueueDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfSyncQueueEntity: EntityInsertAdapter<SyncQueueEntity>

  private val __converters: Converters = Converters()
  init {
    this.__db = __db
    this.__insertAdapterOfSyncQueueEntity = object : EntityInsertAdapter<SyncQueueEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `sync_queue` (`id`,`type`,`refType`,`refId`,`imageId`,`payload`,`attempts`,`lastError`,`createdAt`,`nextAttemptAt`) VALUES (?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: SyncQueueEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.type)
        statement.bindText(3, entity.refType)
        statement.bindText(4, entity.refId)
        val _tmpImageId: String? = entity.imageId
        if (_tmpImageId == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpImageId)
        }
        val _tmpPayload: String? = entity.payload
        if (_tmpPayload == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpPayload)
        }
        statement.bindLong(7, entity.attempts.toLong())
        val _tmpLastError: String? = entity.lastError
        if (_tmpLastError == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpLastError)
        }
        val _tmp: Long? = __converters.fromDate(entity.createdAt)
        if (_tmp == null) {
          statement.bindNull(9)
        } else {
          statement.bindLong(9, _tmp)
        }
        val _tmp_1: Long? = __converters.fromDate(entity.nextAttemptAt)
        if (_tmp_1 == null) {
          statement.bindNull(10)
        } else {
          statement.bindLong(10, _tmp_1)
        }
      }
    }
  }

  public override suspend fun upsert(item: SyncQueueEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfSyncQueueEntity.insert(_connection, item)
  }

  public override suspend fun pending(now: Date, limit: Int): List<SyncQueueEntity> {
    val _sql: String =
        "SELECT * FROM sync_queue WHERE nextAttemptAt <= ? ORDER BY createdAt ASC LIMIT ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: Long? = __converters.fromDate(now)
        if (_tmp == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindLong(_argIndex, _tmp)
        }
        _argIndex = 2
        _stmt.bindLong(_argIndex, limit.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfRefType: Int = getColumnIndexOrThrow(_stmt, "refType")
        val _columnIndexOfRefId: Int = getColumnIndexOrThrow(_stmt, "refId")
        val _columnIndexOfImageId: Int = getColumnIndexOrThrow(_stmt, "imageId")
        val _columnIndexOfPayload: Int = getColumnIndexOrThrow(_stmt, "payload")
        val _columnIndexOfAttempts: Int = getColumnIndexOrThrow(_stmt, "attempts")
        val _columnIndexOfLastError: Int = getColumnIndexOrThrow(_stmt, "lastError")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfNextAttemptAt: Int = getColumnIndexOrThrow(_stmt, "nextAttemptAt")
        val _result: MutableList<SyncQueueEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SyncQueueEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpType: String
          _tmpType = _stmt.getText(_columnIndexOfType)
          val _tmpRefType: String
          _tmpRefType = _stmt.getText(_columnIndexOfRefType)
          val _tmpRefId: String
          _tmpRefId = _stmt.getText(_columnIndexOfRefId)
          val _tmpImageId: String?
          if (_stmt.isNull(_columnIndexOfImageId)) {
            _tmpImageId = null
          } else {
            _tmpImageId = _stmt.getText(_columnIndexOfImageId)
          }
          val _tmpPayload: String?
          if (_stmt.isNull(_columnIndexOfPayload)) {
            _tmpPayload = null
          } else {
            _tmpPayload = _stmt.getText(_columnIndexOfPayload)
          }
          val _tmpAttempts: Int
          _tmpAttempts = _stmt.getLong(_columnIndexOfAttempts).toInt()
          val _tmpLastError: String?
          if (_stmt.isNull(_columnIndexOfLastError)) {
            _tmpLastError = null
          } else {
            _tmpLastError = _stmt.getText(_columnIndexOfLastError)
          }
          val _tmpCreatedAt: Date
          val _tmp_1: Long?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmp_1 = null
          } else {
            _tmp_1 = _stmt.getLong(_columnIndexOfCreatedAt)
          }
          val _tmp_2: Date? = __converters.toDate(_tmp_1)
          if (_tmp_2 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_2
          }
          val _tmpNextAttemptAt: Date
          val _tmp_3: Long?
          if (_stmt.isNull(_columnIndexOfNextAttemptAt)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getLong(_columnIndexOfNextAttemptAt)
          }
          val _tmp_4: Date? = __converters.toDate(_tmp_3)
          if (_tmp_4 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpNextAttemptAt = _tmp_4
          }
          _item =
              SyncQueueEntity(_tmpId,_tmpType,_tmpRefType,_tmpRefId,_tmpImageId,_tmpPayload,_tmpAttempts,_tmpLastError,_tmpCreatedAt,_tmpNextAttemptAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeCount(): Flow<Int> {
    val _sql: String = "SELECT COUNT(*) FROM sync_queue"
    return createFlow(__db, false, arrayOf("sync_queue")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
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

  public override suspend fun recordFailure(
    id: String,
    attempts: Int,
    error: String?,
    next: Date,
  ) {
    val _sql: String =
        "UPDATE sync_queue SET attempts = ?, lastError = ?, nextAttemptAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, attempts.toLong())
        _argIndex = 2
        if (error == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, error)
        }
        _argIndex = 3
        val _tmp: Long? = __converters.fromDate(next)
        if (_tmp == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindLong(_argIndex, _tmp)
        }
        _argIndex = 4
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun delete(id: String) {
    val _sql: String = "DELETE FROM sync_queue WHERE id = ?"
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
    val _sql: String = "DELETE FROM sync_queue"
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
