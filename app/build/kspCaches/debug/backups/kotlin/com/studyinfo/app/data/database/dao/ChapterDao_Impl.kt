package com.studyinfo.app.`data`.database.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.studyinfo.app.`data`.database.converter.Converters
import com.studyinfo.app.`data`.database.entity.ChapterEntity
import com.studyinfo.app.domain.model.SyncState
import java.util.Date
import javax.`annotation`.processing.Generated
import kotlin.Boolean
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
public class ChapterDao_Impl(
  __db: RoomDatabase,
) : ChapterDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfChapterEntity: EntityInsertAdapter<ChapterEntity>

  private val __converters: Converters = Converters()
  init {
    this.__db = __db
    this.__insertAdapterOfChapterEntity = object : EntityInsertAdapter<ChapterEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `chapters` (`id`,`subject`,`name`,`examType`,`chapterNumber`,`displayOrder`,`isCustom`,`createdAt`,`updatedAt`,`syncState`) VALUES (?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ChapterEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.subject)
        statement.bindText(3, entity.name)
        val _tmpExamType: String? = entity.examType
        if (_tmpExamType == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpExamType)
        }
        statement.bindLong(5, entity.chapterNumber.toLong())
        statement.bindLong(6, entity.displayOrder.toLong())
        val _tmp: Int = if (entity.isCustom) 1 else 0
        statement.bindLong(7, _tmp.toLong())
        val _tmp_1: Long? = __converters.fromDate(entity.createdAt)
        if (_tmp_1 == null) {
          statement.bindNull(8)
        } else {
          statement.bindLong(8, _tmp_1)
        }
        val _tmp_2: Long? = __converters.fromDate(entity.updatedAt)
        if (_tmp_2 == null) {
          statement.bindNull(9)
        } else {
          statement.bindLong(9, _tmp_2)
        }
        val _tmp_3: String? = __converters.fromSyncState(entity.syncState)
        if (_tmp_3 == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmp_3)
        }
      }
    }
  }

  public override suspend fun upsert(chapter: ChapterEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfChapterEntity.insert(_connection, chapter)
  }

  public override suspend fun upsertAll(chapters: List<ChapterEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfChapterEntity.insert(_connection, chapters)
  }

  public override fun observeAll(): Flow<List<ChapterEntity>> {
    val _sql: String =
        "SELECT * FROM chapters WHERE syncState != 'PENDING_DELETE' ORDER BY displayOrder ASC, name ASC"
    return createFlow(__db, false, arrayOf("chapters")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfExamType: Int = getColumnIndexOrThrow(_stmt, "examType")
        val _columnIndexOfChapterNumber: Int = getColumnIndexOrThrow(_stmt, "chapterNumber")
        val _columnIndexOfDisplayOrder: Int = getColumnIndexOrThrow(_stmt, "displayOrder")
        val _columnIndexOfIsCustom: Int = getColumnIndexOrThrow(_stmt, "isCustom")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<ChapterEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ChapterEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpSubject: String
          _tmpSubject = _stmt.getText(_columnIndexOfSubject)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpExamType: String?
          if (_stmt.isNull(_columnIndexOfExamType)) {
            _tmpExamType = null
          } else {
            _tmpExamType = _stmt.getText(_columnIndexOfExamType)
          }
          val _tmpChapterNumber: Int
          _tmpChapterNumber = _stmt.getLong(_columnIndexOfChapterNumber).toInt()
          val _tmpDisplayOrder: Int
          _tmpDisplayOrder = _stmt.getLong(_columnIndexOfDisplayOrder).toInt()
          val _tmpIsCustom: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCustom).toInt()
          _tmpIsCustom = _tmp != 0
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
              ChapterEntity(_tmpId,_tmpSubject,_tmpName,_tmpExamType,_tmpChapterNumber,_tmpDisplayOrder,_tmpIsCustom,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeBySubject(subject: String): Flow<List<ChapterEntity>> {
    val _sql: String =
        "SELECT * FROM chapters WHERE syncState != 'PENDING_DELETE' AND subject = ? ORDER BY displayOrder ASC, chapterNumber ASC"
    return createFlow(__db, false, arrayOf("chapters")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, subject)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfExamType: Int = getColumnIndexOrThrow(_stmt, "examType")
        val _columnIndexOfChapterNumber: Int = getColumnIndexOrThrow(_stmt, "chapterNumber")
        val _columnIndexOfDisplayOrder: Int = getColumnIndexOrThrow(_stmt, "displayOrder")
        val _columnIndexOfIsCustom: Int = getColumnIndexOrThrow(_stmt, "isCustom")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<ChapterEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ChapterEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpSubject: String
          _tmpSubject = _stmt.getText(_columnIndexOfSubject)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpExamType: String?
          if (_stmt.isNull(_columnIndexOfExamType)) {
            _tmpExamType = null
          } else {
            _tmpExamType = _stmt.getText(_columnIndexOfExamType)
          }
          val _tmpChapterNumber: Int
          _tmpChapterNumber = _stmt.getLong(_columnIndexOfChapterNumber).toInt()
          val _tmpDisplayOrder: Int
          _tmpDisplayOrder = _stmt.getLong(_columnIndexOfDisplayOrder).toInt()
          val _tmpIsCustom: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCustom).toInt()
          _tmpIsCustom = _tmp != 0
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
              ChapterEntity(_tmpId,_tmpSubject,_tmpName,_tmpExamType,_tmpChapterNumber,_tmpDisplayOrder,_tmpIsCustom,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getById(id: String): ChapterEntity? {
    val _sql: String = "SELECT * FROM chapters WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfExamType: Int = getColumnIndexOrThrow(_stmt, "examType")
        val _columnIndexOfChapterNumber: Int = getColumnIndexOrThrow(_stmt, "chapterNumber")
        val _columnIndexOfDisplayOrder: Int = getColumnIndexOrThrow(_stmt, "displayOrder")
        val _columnIndexOfIsCustom: Int = getColumnIndexOrThrow(_stmt, "isCustom")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: ChapterEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpSubject: String
          _tmpSubject = _stmt.getText(_columnIndexOfSubject)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpExamType: String?
          if (_stmt.isNull(_columnIndexOfExamType)) {
            _tmpExamType = null
          } else {
            _tmpExamType = _stmt.getText(_columnIndexOfExamType)
          }
          val _tmpChapterNumber: Int
          _tmpChapterNumber = _stmt.getLong(_columnIndexOfChapterNumber).toInt()
          val _tmpDisplayOrder: Int
          _tmpDisplayOrder = _stmt.getLong(_columnIndexOfDisplayOrder).toInt()
          val _tmpIsCustom: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCustom).toInt()
          _tmpIsCustom = _tmp != 0
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
          _result =
              ChapterEntity(_tmpId,_tmpSubject,_tmpName,_tmpExamType,_tmpChapterNumber,_tmpDisplayOrder,_tmpIsCustom,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAll(): List<ChapterEntity> {
    val _sql: String = "SELECT * FROM chapters"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfExamType: Int = getColumnIndexOrThrow(_stmt, "examType")
        val _columnIndexOfChapterNumber: Int = getColumnIndexOrThrow(_stmt, "chapterNumber")
        val _columnIndexOfDisplayOrder: Int = getColumnIndexOrThrow(_stmt, "displayOrder")
        val _columnIndexOfIsCustom: Int = getColumnIndexOrThrow(_stmt, "isCustom")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<ChapterEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ChapterEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpSubject: String
          _tmpSubject = _stmt.getText(_columnIndexOfSubject)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpExamType: String?
          if (_stmt.isNull(_columnIndexOfExamType)) {
            _tmpExamType = null
          } else {
            _tmpExamType = _stmt.getText(_columnIndexOfExamType)
          }
          val _tmpChapterNumber: Int
          _tmpChapterNumber = _stmt.getLong(_columnIndexOfChapterNumber).toInt()
          val _tmpDisplayOrder: Int
          _tmpDisplayOrder = _stmt.getLong(_columnIndexOfDisplayOrder).toInt()
          val _tmpIsCustom: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCustom).toInt()
          _tmpIsCustom = _tmp != 0
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
              ChapterEntity(_tmpId,_tmpSubject,_tmpName,_tmpExamType,_tmpChapterNumber,_tmpDisplayOrder,_tmpIsCustom,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun pendingChanges(synced: SyncState): List<ChapterEntity> {
    val _sql: String = "SELECT * FROM chapters WHERE syncState != ?"
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
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfExamType: Int = getColumnIndexOrThrow(_stmt, "examType")
        val _columnIndexOfChapterNumber: Int = getColumnIndexOrThrow(_stmt, "chapterNumber")
        val _columnIndexOfDisplayOrder: Int = getColumnIndexOrThrow(_stmt, "displayOrder")
        val _columnIndexOfIsCustom: Int = getColumnIndexOrThrow(_stmt, "isCustom")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<ChapterEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ChapterEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpSubject: String
          _tmpSubject = _stmt.getText(_columnIndexOfSubject)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpExamType: String?
          if (_stmt.isNull(_columnIndexOfExamType)) {
            _tmpExamType = null
          } else {
            _tmpExamType = _stmt.getText(_columnIndexOfExamType)
          }
          val _tmpChapterNumber: Int
          _tmpChapterNumber = _stmt.getLong(_columnIndexOfChapterNumber).toInt()
          val _tmpDisplayOrder: Int
          _tmpDisplayOrder = _stmt.getLong(_columnIndexOfDisplayOrder).toInt()
          val _tmpIsCustom: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsCustom).toInt()
          _tmpIsCustom = _tmp_1 != 0
          val _tmpCreatedAt: Date
          val _tmp_2: Long?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getLong(_columnIndexOfCreatedAt)
          }
          val _tmp_3: Date? = __converters.toDate(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_3
          }
          val _tmpUpdatedAt: Date
          val _tmp_4: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_5: Date? = __converters.toDate(_tmp_4)
          if (_tmp_5 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_5
          }
          val _tmpSyncState: SyncState
          val _tmp_6: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_7: SyncState? = __converters.toSyncState(_tmp_6)
          if (_tmp_7 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_7
          }
          _item =
              ChapterEntity(_tmpId,_tmpSubject,_tmpName,_tmpExamType,_tmpChapterNumber,_tmpDisplayOrder,_tmpIsCustom,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun delete(id: String) {
    val _sql: String = "DELETE FROM chapters WHERE id = ?"
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
    val _sql: String = "UPDATE chapters SET syncState = ? WHERE id = ?"
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
    val _sql: String = "DELETE FROM chapters"
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
