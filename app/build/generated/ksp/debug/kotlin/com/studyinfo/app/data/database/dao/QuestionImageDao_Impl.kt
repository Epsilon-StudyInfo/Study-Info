package com.studyinfo.app.`data`.database.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.studyinfo.app.`data`.database.converter.Converters
import com.studyinfo.app.`data`.database.entity.QuestionImageEntity
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
public class QuestionImageDao_Impl(
  __db: RoomDatabase,
) : QuestionImageDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfQuestionImageEntity: EntityInsertAdapter<QuestionImageEntity>

  private val __converters: Converters = Converters()
  init {
    this.__db = __db
    this.__insertAdapterOfQuestionImageEntity = object : EntityInsertAdapter<QuestionImageEntity>()
        {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `question_images` (`id`,`questionRefType`,`questionRefId`,`localUri`,`remoteUrl`,`storagePath`,`uploadedAt`,`createdAt`,`updatedAt`,`syncState`) VALUES (?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: QuestionImageEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.questionRefType)
        statement.bindText(3, entity.questionRefId)
        val _tmpLocalUri: String? = entity.localUri
        if (_tmpLocalUri == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpLocalUri)
        }
        val _tmpRemoteUrl: String? = entity.remoteUrl
        if (_tmpRemoteUrl == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpRemoteUrl)
        }
        val _tmpStoragePath: String? = entity.storagePath
        if (_tmpStoragePath == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpStoragePath)
        }
        val _tmpUploadedAt: Date? = entity.uploadedAt
        val _tmp: Long? = __converters.fromDate(_tmpUploadedAt)
        if (_tmp == null) {
          statement.bindNull(7)
        } else {
          statement.bindLong(7, _tmp)
        }
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

  public override suspend fun upsert(image: QuestionImageEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfQuestionImageEntity.insert(_connection, image)
  }

  public override suspend fun upsertAll(images: List<QuestionImageEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfQuestionImageEntity.insert(_connection, images)
  }

  public override fun observeForQuestion(refType: String, refId: String):
      Flow<List<QuestionImageEntity>> {
    val _sql: String =
        "SELECT * FROM question_images WHERE syncState != 'PENDING_DELETE' AND questionRefType = ? AND questionRefId = ? ORDER BY createdAt ASC"
    return createFlow(__db, false, arrayOf("question_images")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, refType)
        _argIndex = 2
        _stmt.bindText(_argIndex, refId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfQuestionRefType: Int = getColumnIndexOrThrow(_stmt, "questionRefType")
        val _columnIndexOfQuestionRefId: Int = getColumnIndexOrThrow(_stmt, "questionRefId")
        val _columnIndexOfLocalUri: Int = getColumnIndexOrThrow(_stmt, "localUri")
        val _columnIndexOfRemoteUrl: Int = getColumnIndexOrThrow(_stmt, "remoteUrl")
        val _columnIndexOfStoragePath: Int = getColumnIndexOrThrow(_stmt, "storagePath")
        val _columnIndexOfUploadedAt: Int = getColumnIndexOrThrow(_stmt, "uploadedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<QuestionImageEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuestionImageEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpQuestionRefType: String
          _tmpQuestionRefType = _stmt.getText(_columnIndexOfQuestionRefType)
          val _tmpQuestionRefId: String
          _tmpQuestionRefId = _stmt.getText(_columnIndexOfQuestionRefId)
          val _tmpLocalUri: String?
          if (_stmt.isNull(_columnIndexOfLocalUri)) {
            _tmpLocalUri = null
          } else {
            _tmpLocalUri = _stmt.getText(_columnIndexOfLocalUri)
          }
          val _tmpRemoteUrl: String?
          if (_stmt.isNull(_columnIndexOfRemoteUrl)) {
            _tmpRemoteUrl = null
          } else {
            _tmpRemoteUrl = _stmt.getText(_columnIndexOfRemoteUrl)
          }
          val _tmpStoragePath: String?
          if (_stmt.isNull(_columnIndexOfStoragePath)) {
            _tmpStoragePath = null
          } else {
            _tmpStoragePath = _stmt.getText(_columnIndexOfStoragePath)
          }
          val _tmpUploadedAt: Date?
          val _tmp: Long?
          if (_stmt.isNull(_columnIndexOfUploadedAt)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(_columnIndexOfUploadedAt)
          }
          _tmpUploadedAt = __converters.toDate(_tmp)
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
              QuestionImageEntity(_tmpId,_tmpQuestionRefType,_tmpQuestionRefId,_tmpLocalUri,_tmpRemoteUrl,_tmpStoragePath,_tmpUploadedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getForQuestion(refType: String, refId: String):
      List<QuestionImageEntity> {
    val _sql: String =
        "SELECT * FROM question_images WHERE questionRefType = ? AND questionRefId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, refType)
        _argIndex = 2
        _stmt.bindText(_argIndex, refId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfQuestionRefType: Int = getColumnIndexOrThrow(_stmt, "questionRefType")
        val _columnIndexOfQuestionRefId: Int = getColumnIndexOrThrow(_stmt, "questionRefId")
        val _columnIndexOfLocalUri: Int = getColumnIndexOrThrow(_stmt, "localUri")
        val _columnIndexOfRemoteUrl: Int = getColumnIndexOrThrow(_stmt, "remoteUrl")
        val _columnIndexOfStoragePath: Int = getColumnIndexOrThrow(_stmt, "storagePath")
        val _columnIndexOfUploadedAt: Int = getColumnIndexOrThrow(_stmt, "uploadedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<QuestionImageEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuestionImageEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpQuestionRefType: String
          _tmpQuestionRefType = _stmt.getText(_columnIndexOfQuestionRefType)
          val _tmpQuestionRefId: String
          _tmpQuestionRefId = _stmt.getText(_columnIndexOfQuestionRefId)
          val _tmpLocalUri: String?
          if (_stmt.isNull(_columnIndexOfLocalUri)) {
            _tmpLocalUri = null
          } else {
            _tmpLocalUri = _stmt.getText(_columnIndexOfLocalUri)
          }
          val _tmpRemoteUrl: String?
          if (_stmt.isNull(_columnIndexOfRemoteUrl)) {
            _tmpRemoteUrl = null
          } else {
            _tmpRemoteUrl = _stmt.getText(_columnIndexOfRemoteUrl)
          }
          val _tmpStoragePath: String?
          if (_stmt.isNull(_columnIndexOfStoragePath)) {
            _tmpStoragePath = null
          } else {
            _tmpStoragePath = _stmt.getText(_columnIndexOfStoragePath)
          }
          val _tmpUploadedAt: Date?
          val _tmp: Long?
          if (_stmt.isNull(_columnIndexOfUploadedAt)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(_columnIndexOfUploadedAt)
          }
          _tmpUploadedAt = __converters.toDate(_tmp)
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
              QuestionImageEntity(_tmpId,_tmpQuestionRefType,_tmpQuestionRefId,_tmpLocalUri,_tmpRemoteUrl,_tmpStoragePath,_tmpUploadedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getById(id: String): QuestionImageEntity? {
    val _sql: String = "SELECT * FROM question_images WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfQuestionRefType: Int = getColumnIndexOrThrow(_stmt, "questionRefType")
        val _columnIndexOfQuestionRefId: Int = getColumnIndexOrThrow(_stmt, "questionRefId")
        val _columnIndexOfLocalUri: Int = getColumnIndexOrThrow(_stmt, "localUri")
        val _columnIndexOfRemoteUrl: Int = getColumnIndexOrThrow(_stmt, "remoteUrl")
        val _columnIndexOfStoragePath: Int = getColumnIndexOrThrow(_stmt, "storagePath")
        val _columnIndexOfUploadedAt: Int = getColumnIndexOrThrow(_stmt, "uploadedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: QuestionImageEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpQuestionRefType: String
          _tmpQuestionRefType = _stmt.getText(_columnIndexOfQuestionRefType)
          val _tmpQuestionRefId: String
          _tmpQuestionRefId = _stmt.getText(_columnIndexOfQuestionRefId)
          val _tmpLocalUri: String?
          if (_stmt.isNull(_columnIndexOfLocalUri)) {
            _tmpLocalUri = null
          } else {
            _tmpLocalUri = _stmt.getText(_columnIndexOfLocalUri)
          }
          val _tmpRemoteUrl: String?
          if (_stmt.isNull(_columnIndexOfRemoteUrl)) {
            _tmpRemoteUrl = null
          } else {
            _tmpRemoteUrl = _stmt.getText(_columnIndexOfRemoteUrl)
          }
          val _tmpStoragePath: String?
          if (_stmt.isNull(_columnIndexOfStoragePath)) {
            _tmpStoragePath = null
          } else {
            _tmpStoragePath = _stmt.getText(_columnIndexOfStoragePath)
          }
          val _tmpUploadedAt: Date?
          val _tmp: Long?
          if (_stmt.isNull(_columnIndexOfUploadedAt)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(_columnIndexOfUploadedAt)
          }
          _tmpUploadedAt = __converters.toDate(_tmp)
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
              QuestionImageEntity(_tmpId,_tmpQuestionRefType,_tmpQuestionRefId,_tmpLocalUri,_tmpRemoteUrl,_tmpStoragePath,_tmpUploadedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAllForRefType(refType: String): List<QuestionImageEntity> {
    val _sql: String = "SELECT * FROM question_images WHERE questionRefType = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, refType)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfQuestionRefType: Int = getColumnIndexOrThrow(_stmt, "questionRefType")
        val _columnIndexOfQuestionRefId: Int = getColumnIndexOrThrow(_stmt, "questionRefId")
        val _columnIndexOfLocalUri: Int = getColumnIndexOrThrow(_stmt, "localUri")
        val _columnIndexOfRemoteUrl: Int = getColumnIndexOrThrow(_stmt, "remoteUrl")
        val _columnIndexOfStoragePath: Int = getColumnIndexOrThrow(_stmt, "storagePath")
        val _columnIndexOfUploadedAt: Int = getColumnIndexOrThrow(_stmt, "uploadedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<QuestionImageEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuestionImageEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpQuestionRefType: String
          _tmpQuestionRefType = _stmt.getText(_columnIndexOfQuestionRefType)
          val _tmpQuestionRefId: String
          _tmpQuestionRefId = _stmt.getText(_columnIndexOfQuestionRefId)
          val _tmpLocalUri: String?
          if (_stmt.isNull(_columnIndexOfLocalUri)) {
            _tmpLocalUri = null
          } else {
            _tmpLocalUri = _stmt.getText(_columnIndexOfLocalUri)
          }
          val _tmpRemoteUrl: String?
          if (_stmt.isNull(_columnIndexOfRemoteUrl)) {
            _tmpRemoteUrl = null
          } else {
            _tmpRemoteUrl = _stmt.getText(_columnIndexOfRemoteUrl)
          }
          val _tmpStoragePath: String?
          if (_stmt.isNull(_columnIndexOfStoragePath)) {
            _tmpStoragePath = null
          } else {
            _tmpStoragePath = _stmt.getText(_columnIndexOfStoragePath)
          }
          val _tmpUploadedAt: Date?
          val _tmp: Long?
          if (_stmt.isNull(_columnIndexOfUploadedAt)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(_columnIndexOfUploadedAt)
          }
          _tmpUploadedAt = __converters.toDate(_tmp)
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
              QuestionImageEntity(_tmpId,_tmpQuestionRefType,_tmpQuestionRefId,_tmpLocalUri,_tmpRemoteUrl,_tmpStoragePath,_tmpUploadedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAll(): List<QuestionImageEntity> {
    val _sql: String = "SELECT * FROM question_images"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfQuestionRefType: Int = getColumnIndexOrThrow(_stmt, "questionRefType")
        val _columnIndexOfQuestionRefId: Int = getColumnIndexOrThrow(_stmt, "questionRefId")
        val _columnIndexOfLocalUri: Int = getColumnIndexOrThrow(_stmt, "localUri")
        val _columnIndexOfRemoteUrl: Int = getColumnIndexOrThrow(_stmt, "remoteUrl")
        val _columnIndexOfStoragePath: Int = getColumnIndexOrThrow(_stmt, "storagePath")
        val _columnIndexOfUploadedAt: Int = getColumnIndexOrThrow(_stmt, "uploadedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<QuestionImageEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuestionImageEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpQuestionRefType: String
          _tmpQuestionRefType = _stmt.getText(_columnIndexOfQuestionRefType)
          val _tmpQuestionRefId: String
          _tmpQuestionRefId = _stmt.getText(_columnIndexOfQuestionRefId)
          val _tmpLocalUri: String?
          if (_stmt.isNull(_columnIndexOfLocalUri)) {
            _tmpLocalUri = null
          } else {
            _tmpLocalUri = _stmt.getText(_columnIndexOfLocalUri)
          }
          val _tmpRemoteUrl: String?
          if (_stmt.isNull(_columnIndexOfRemoteUrl)) {
            _tmpRemoteUrl = null
          } else {
            _tmpRemoteUrl = _stmt.getText(_columnIndexOfRemoteUrl)
          }
          val _tmpStoragePath: String?
          if (_stmt.isNull(_columnIndexOfStoragePath)) {
            _tmpStoragePath = null
          } else {
            _tmpStoragePath = _stmt.getText(_columnIndexOfStoragePath)
          }
          val _tmpUploadedAt: Date?
          val _tmp: Long?
          if (_stmt.isNull(_columnIndexOfUploadedAt)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(_columnIndexOfUploadedAt)
          }
          _tmpUploadedAt = __converters.toDate(_tmp)
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
              QuestionImageEntity(_tmpId,_tmpQuestionRefType,_tmpQuestionRefId,_tmpLocalUri,_tmpRemoteUrl,_tmpStoragePath,_tmpUploadedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun pendingChanges(synced: SyncState): List<QuestionImageEntity> {
    val _sql: String = "SELECT * FROM question_images WHERE syncState != ?"
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
        val _columnIndexOfQuestionRefType: Int = getColumnIndexOrThrow(_stmt, "questionRefType")
        val _columnIndexOfQuestionRefId: Int = getColumnIndexOrThrow(_stmt, "questionRefId")
        val _columnIndexOfLocalUri: Int = getColumnIndexOrThrow(_stmt, "localUri")
        val _columnIndexOfRemoteUrl: Int = getColumnIndexOrThrow(_stmt, "remoteUrl")
        val _columnIndexOfStoragePath: Int = getColumnIndexOrThrow(_stmt, "storagePath")
        val _columnIndexOfUploadedAt: Int = getColumnIndexOrThrow(_stmt, "uploadedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<QuestionImageEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QuestionImageEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpQuestionRefType: String
          _tmpQuestionRefType = _stmt.getText(_columnIndexOfQuestionRefType)
          val _tmpQuestionRefId: String
          _tmpQuestionRefId = _stmt.getText(_columnIndexOfQuestionRefId)
          val _tmpLocalUri: String?
          if (_stmt.isNull(_columnIndexOfLocalUri)) {
            _tmpLocalUri = null
          } else {
            _tmpLocalUri = _stmt.getText(_columnIndexOfLocalUri)
          }
          val _tmpRemoteUrl: String?
          if (_stmt.isNull(_columnIndexOfRemoteUrl)) {
            _tmpRemoteUrl = null
          } else {
            _tmpRemoteUrl = _stmt.getText(_columnIndexOfRemoteUrl)
          }
          val _tmpStoragePath: String?
          if (_stmt.isNull(_columnIndexOfStoragePath)) {
            _tmpStoragePath = null
          } else {
            _tmpStoragePath = _stmt.getText(_columnIndexOfStoragePath)
          }
          val _tmpUploadedAt: Date?
          val _tmp_1: Long?
          if (_stmt.isNull(_columnIndexOfUploadedAt)) {
            _tmp_1 = null
          } else {
            _tmp_1 = _stmt.getLong(_columnIndexOfUploadedAt)
          }
          _tmpUploadedAt = __converters.toDate(_tmp_1)
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
              QuestionImageEntity(_tmpId,_tmpQuestionRefType,_tmpQuestionRefId,_tmpLocalUri,_tmpRemoteUrl,_tmpStoragePath,_tmpUploadedAt,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun delete(id: String) {
    val _sql: String = "DELETE FROM question_images WHERE id = ?"
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

  public override suspend fun deleteForQuestion(refType: String, refId: String) {
    val _sql: String = "DELETE FROM question_images WHERE questionRefType = ? AND questionRefId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, refType)
        _argIndex = 2
        _stmt.bindText(_argIndex, refId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun markSync(id: String, state: SyncState) {
    val _sql: String = "UPDATE question_images SET syncState = ? WHERE id = ?"
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
    val _sql: String = "DELETE FROM question_images"
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
