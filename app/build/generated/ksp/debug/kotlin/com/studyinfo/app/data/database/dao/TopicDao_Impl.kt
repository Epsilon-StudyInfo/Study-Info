package com.studyinfo.app.`data`.database.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.studyinfo.app.`data`.database.converter.Converters
import com.studyinfo.app.`data`.database.entity.TopicEntity
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
public class TopicDao_Impl(
  __db: RoomDatabase,
) : TopicDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfTopicEntity: EntityInsertAdapter<TopicEntity>

  private val __converters: Converters = Converters()
  init {
    this.__db = __db
    this.__insertAdapterOfTopicEntity = object : EntityInsertAdapter<TopicEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `topics` (`id`,`chapterId`,`name`,`displayOrder`,`createdAt`,`updatedAt`,`syncState`) VALUES (?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TopicEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.chapterId)
        statement.bindText(3, entity.name)
        statement.bindLong(4, entity.displayOrder.toLong())
        val _tmp: Long? = __converters.fromDate(entity.createdAt)
        if (_tmp == null) {
          statement.bindNull(5)
        } else {
          statement.bindLong(5, _tmp)
        }
        val _tmp_1: Long? = __converters.fromDate(entity.updatedAt)
        if (_tmp_1 == null) {
          statement.bindNull(6)
        } else {
          statement.bindLong(6, _tmp_1)
        }
        val _tmp_2: String? = __converters.fromSyncState(entity.syncState)
        if (_tmp_2 == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmp_2)
        }
      }
    }
  }

  public override suspend fun upsert(topic: TopicEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfTopicEntity.insert(_connection, topic)
  }

  public override suspend fun upsertAll(topics: List<TopicEntity>): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfTopicEntity.insert(_connection, topics)
  }

  public override fun observeAll(): Flow<List<TopicEntity>> {
    val _sql: String =
        "SELECT * FROM topics WHERE syncState != 'PENDING_DELETE' ORDER BY displayOrder ASC, name ASC"
    return createFlow(__db, false, arrayOf("topics")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfChapterId: Int = getColumnIndexOrThrow(_stmt, "chapterId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDisplayOrder: Int = getColumnIndexOrThrow(_stmt, "displayOrder")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<TopicEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TopicEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpChapterId: String
          _tmpChapterId = _stmt.getText(_columnIndexOfChapterId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDisplayOrder: Int
          _tmpDisplayOrder = _stmt.getLong(_columnIndexOfDisplayOrder).toInt()
          val _tmpCreatedAt: Date
          val _tmp: Long?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(_columnIndexOfCreatedAt)
          }
          val _tmp_1: Date? = __converters.toDate(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_1
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
              TopicEntity(_tmpId,_tmpChapterId,_tmpName,_tmpDisplayOrder,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeByChapter(chapterId: String): Flow<List<TopicEntity>> {
    val _sql: String =
        "SELECT * FROM topics WHERE syncState != 'PENDING_DELETE' AND chapterId = ? ORDER BY displayOrder ASC, name ASC"
    return createFlow(__db, false, arrayOf("topics")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, chapterId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfChapterId: Int = getColumnIndexOrThrow(_stmt, "chapterId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDisplayOrder: Int = getColumnIndexOrThrow(_stmt, "displayOrder")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<TopicEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TopicEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpChapterId: String
          _tmpChapterId = _stmt.getText(_columnIndexOfChapterId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDisplayOrder: Int
          _tmpDisplayOrder = _stmt.getLong(_columnIndexOfDisplayOrder).toInt()
          val _tmpCreatedAt: Date
          val _tmp: Long?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(_columnIndexOfCreatedAt)
          }
          val _tmp_1: Date? = __converters.toDate(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_1
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
              TopicEntity(_tmpId,_tmpChapterId,_tmpName,_tmpDisplayOrder,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getById(id: String): TopicEntity? {
    val _sql: String = "SELECT * FROM topics WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfChapterId: Int = getColumnIndexOrThrow(_stmt, "chapterId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDisplayOrder: Int = getColumnIndexOrThrow(_stmt, "displayOrder")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: TopicEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpChapterId: String
          _tmpChapterId = _stmt.getText(_columnIndexOfChapterId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDisplayOrder: Int
          _tmpDisplayOrder = _stmt.getLong(_columnIndexOfDisplayOrder).toInt()
          val _tmpCreatedAt: Date
          val _tmp: Long?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(_columnIndexOfCreatedAt)
          }
          val _tmp_1: Date? = __converters.toDate(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_1
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
              TopicEntity(_tmpId,_tmpChapterId,_tmpName,_tmpDisplayOrder,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAll(): List<TopicEntity> {
    val _sql: String = "SELECT * FROM topics"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfChapterId: Int = getColumnIndexOrThrow(_stmt, "chapterId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDisplayOrder: Int = getColumnIndexOrThrow(_stmt, "displayOrder")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<TopicEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TopicEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpChapterId: String
          _tmpChapterId = _stmt.getText(_columnIndexOfChapterId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDisplayOrder: Int
          _tmpDisplayOrder = _stmt.getLong(_columnIndexOfDisplayOrder).toInt()
          val _tmpCreatedAt: Date
          val _tmp: Long?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(_columnIndexOfCreatedAt)
          }
          val _tmp_1: Date? = __converters.toDate(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_1
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
              TopicEntity(_tmpId,_tmpChapterId,_tmpName,_tmpDisplayOrder,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun pendingChanges(synced: SyncState): List<TopicEntity> {
    val _sql: String = "SELECT * FROM topics WHERE syncState != ?"
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
        val _columnIndexOfChapterId: Int = getColumnIndexOrThrow(_stmt, "chapterId")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDisplayOrder: Int = getColumnIndexOrThrow(_stmt, "displayOrder")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<TopicEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TopicEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpChapterId: String
          _tmpChapterId = _stmt.getText(_columnIndexOfChapterId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDisplayOrder: Int
          _tmpDisplayOrder = _stmt.getLong(_columnIndexOfDisplayOrder).toInt()
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
          val _tmpUpdatedAt: Date
          val _tmp_3: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_4: Date? = __converters.toDate(_tmp_3)
          if (_tmp_4 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_4
          }
          val _tmpSyncState: SyncState
          val _tmp_5: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_5 = null
          } else {
            _tmp_5 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_6: SyncState? = __converters.toSyncState(_tmp_5)
          if (_tmp_6 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_6
          }
          _item =
              TopicEntity(_tmpId,_tmpChapterId,_tmpName,_tmpDisplayOrder,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun delete(id: String) {
    val _sql: String = "DELETE FROM topics WHERE id = ?"
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

  public override suspend fun markSync(id: String, state: SyncState) {
    val _sql: String = "UPDATE topics SET syncState = ? WHERE id = ?"
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

  public override suspend fun clear() {
    val _sql: String = "DELETE FROM topics"
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
