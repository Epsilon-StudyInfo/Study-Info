package com.studyinfo.app.`data`.database.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.studyinfo.app.`data`.database.converter.Converters
import com.studyinfo.app.`data`.database.entity.ReviewEntity
import com.studyinfo.app.domain.model.Subject
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
public class ReviewDao_Impl(
  __db: RoomDatabase,
) : ReviewDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfReviewEntity: EntityInsertAdapter<ReviewEntity>

  private val __converters: Converters = Converters()
  init {
    this.__db = __db
    this.__insertAdapterOfReviewEntity = object : EntityInsertAdapter<ReviewEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `reviews` (`id`,`subject`,`startedAt`,`endedAt`,`reviewedCount`,`understoodCount`,`stillConfusedCount`,`needsRevisionCount`,`notes`,`createdAt`,`updatedAt`,`syncState`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ReviewEntity) {
        statement.bindText(1, entity.id)
        val _tmpSubject: Subject? = entity.subject
        val _tmp: String? = __converters.fromSubject(_tmpSubject)
        if (_tmp == null) {
          statement.bindNull(2)
        } else {
          statement.bindText(2, _tmp)
        }
        val _tmp_1: Long? = __converters.fromDate(entity.startedAt)
        if (_tmp_1 == null) {
          statement.bindNull(3)
        } else {
          statement.bindLong(3, _tmp_1)
        }
        val _tmpEndedAt: Date? = entity.endedAt
        val _tmp_2: Long? = __converters.fromDate(_tmpEndedAt)
        if (_tmp_2 == null) {
          statement.bindNull(4)
        } else {
          statement.bindLong(4, _tmp_2)
        }
        statement.bindLong(5, entity.reviewedCount.toLong())
        statement.bindLong(6, entity.understoodCount.toLong())
        statement.bindLong(7, entity.stillConfusedCount.toLong())
        statement.bindLong(8, entity.needsRevisionCount.toLong())
        val _tmpNotes: String? = entity.notes
        if (_tmpNotes == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpNotes)
        }
        val _tmp_3: Long? = __converters.fromDate(entity.createdAt)
        if (_tmp_3 == null) {
          statement.bindNull(10)
        } else {
          statement.bindLong(10, _tmp_3)
        }
        val _tmp_4: Long? = __converters.fromDate(entity.updatedAt)
        if (_tmp_4 == null) {
          statement.bindNull(11)
        } else {
          statement.bindLong(11, _tmp_4)
        }
        val _tmp_5: String? = __converters.fromSyncState(entity.syncState)
        if (_tmp_5 == null) {
          statement.bindNull(12)
        } else {
          statement.bindText(12, _tmp_5)
        }
      }
    }
  }

  public override suspend fun upsert(review: ReviewEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfReviewEntity.insert(_connection, review)
  }

  public override suspend fun upsertAll(reviews: List<ReviewEntity>): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfReviewEntity.insert(_connection, reviews)
  }

  public override fun observeAll(): Flow<List<ReviewEntity>> {
    val _sql: String = "SELECT * FROM reviews ORDER BY startedAt DESC"
    return createFlow(__db, false, arrayOf("reviews")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfStartedAt: Int = getColumnIndexOrThrow(_stmt, "startedAt")
        val _columnIndexOfEndedAt: Int = getColumnIndexOrThrow(_stmt, "endedAt")
        val _columnIndexOfReviewedCount: Int = getColumnIndexOrThrow(_stmt, "reviewedCount")
        val _columnIndexOfUnderstoodCount: Int = getColumnIndexOrThrow(_stmt, "understoodCount")
        val _columnIndexOfStillConfusedCount: Int = getColumnIndexOrThrow(_stmt,
            "stillConfusedCount")
        val _columnIndexOfNeedsRevisionCount: Int = getColumnIndexOrThrow(_stmt,
            "needsRevisionCount")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<ReviewEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ReviewEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpSubject: Subject?
          val _tmp: String?
          if (_stmt.isNull(_columnIndexOfSubject)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_columnIndexOfSubject)
          }
          _tmpSubject = __converters.toSubject(_tmp)
          val _tmpStartedAt: Date
          val _tmp_1: Long?
          if (_stmt.isNull(_columnIndexOfStartedAt)) {
            _tmp_1 = null
          } else {
            _tmp_1 = _stmt.getLong(_columnIndexOfStartedAt)
          }
          val _tmp_2: Date? = __converters.toDate(_tmp_1)
          if (_tmp_2 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpStartedAt = _tmp_2
          }
          val _tmpEndedAt: Date?
          val _tmp_3: Long?
          if (_stmt.isNull(_columnIndexOfEndedAt)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getLong(_columnIndexOfEndedAt)
          }
          _tmpEndedAt = __converters.toDate(_tmp_3)
          val _tmpReviewedCount: Int
          _tmpReviewedCount = _stmt.getLong(_columnIndexOfReviewedCount).toInt()
          val _tmpUnderstoodCount: Int
          _tmpUnderstoodCount = _stmt.getLong(_columnIndexOfUnderstoodCount).toInt()
          val _tmpStillConfusedCount: Int
          _tmpStillConfusedCount = _stmt.getLong(_columnIndexOfStillConfusedCount).toInt()
          val _tmpNeedsRevisionCount: Int
          _tmpNeedsRevisionCount = _stmt.getLong(_columnIndexOfNeedsRevisionCount).toInt()
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpCreatedAt: Date
          val _tmp_4: Long?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getLong(_columnIndexOfCreatedAt)
          }
          val _tmp_5: Date? = __converters.toDate(_tmp_4)
          if (_tmp_5 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_5
          }
          val _tmpUpdatedAt: Date
          val _tmp_6: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_7: Date? = __converters.toDate(_tmp_6)
          if (_tmp_7 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_7
          }
          val _tmpSyncState: SyncState
          val _tmp_8: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_8 = null
          } else {
            _tmp_8 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_9: SyncState? = __converters.toSyncState(_tmp_8)
          if (_tmp_9 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_9
          }
          _item =
              ReviewEntity(_tmpId,_tmpSubject,_tmpStartedAt,_tmpEndedAt,_tmpReviewedCount,_tmpUnderstoodCount,_tmpStillConfusedCount,_tmpNeedsRevisionCount,_tmpNotes,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeForRange(start: Long, end: Long): Flow<List<ReviewEntity>> {
    val _sql: String =
        "SELECT * FROM reviews WHERE startedAt BETWEEN ? AND ? ORDER BY startedAt DESC"
    return createFlow(__db, false, arrayOf("reviews")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, start)
        _argIndex = 2
        _stmt.bindLong(_argIndex, end)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfStartedAt: Int = getColumnIndexOrThrow(_stmt, "startedAt")
        val _columnIndexOfEndedAt: Int = getColumnIndexOrThrow(_stmt, "endedAt")
        val _columnIndexOfReviewedCount: Int = getColumnIndexOrThrow(_stmt, "reviewedCount")
        val _columnIndexOfUnderstoodCount: Int = getColumnIndexOrThrow(_stmt, "understoodCount")
        val _columnIndexOfStillConfusedCount: Int = getColumnIndexOrThrow(_stmt,
            "stillConfusedCount")
        val _columnIndexOfNeedsRevisionCount: Int = getColumnIndexOrThrow(_stmt,
            "needsRevisionCount")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<ReviewEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ReviewEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpSubject: Subject?
          val _tmp: String?
          if (_stmt.isNull(_columnIndexOfSubject)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_columnIndexOfSubject)
          }
          _tmpSubject = __converters.toSubject(_tmp)
          val _tmpStartedAt: Date
          val _tmp_1: Long?
          if (_stmt.isNull(_columnIndexOfStartedAt)) {
            _tmp_1 = null
          } else {
            _tmp_1 = _stmt.getLong(_columnIndexOfStartedAt)
          }
          val _tmp_2: Date? = __converters.toDate(_tmp_1)
          if (_tmp_2 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpStartedAt = _tmp_2
          }
          val _tmpEndedAt: Date?
          val _tmp_3: Long?
          if (_stmt.isNull(_columnIndexOfEndedAt)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getLong(_columnIndexOfEndedAt)
          }
          _tmpEndedAt = __converters.toDate(_tmp_3)
          val _tmpReviewedCount: Int
          _tmpReviewedCount = _stmt.getLong(_columnIndexOfReviewedCount).toInt()
          val _tmpUnderstoodCount: Int
          _tmpUnderstoodCount = _stmt.getLong(_columnIndexOfUnderstoodCount).toInt()
          val _tmpStillConfusedCount: Int
          _tmpStillConfusedCount = _stmt.getLong(_columnIndexOfStillConfusedCount).toInt()
          val _tmpNeedsRevisionCount: Int
          _tmpNeedsRevisionCount = _stmt.getLong(_columnIndexOfNeedsRevisionCount).toInt()
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpCreatedAt: Date
          val _tmp_4: Long?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getLong(_columnIndexOfCreatedAt)
          }
          val _tmp_5: Date? = __converters.toDate(_tmp_4)
          if (_tmp_5 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_5
          }
          val _tmpUpdatedAt: Date
          val _tmp_6: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_7: Date? = __converters.toDate(_tmp_6)
          if (_tmp_7 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_7
          }
          val _tmpSyncState: SyncState
          val _tmp_8: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_8 = null
          } else {
            _tmp_8 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_9: SyncState? = __converters.toSyncState(_tmp_8)
          if (_tmp_9 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_9
          }
          _item =
              ReviewEntity(_tmpId,_tmpSubject,_tmpStartedAt,_tmpEndedAt,_tmpReviewedCount,_tmpUnderstoodCount,_tmpStillConfusedCount,_tmpNeedsRevisionCount,_tmpNotes,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getById(id: String): ReviewEntity? {
    val _sql: String = "SELECT * FROM reviews WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfStartedAt: Int = getColumnIndexOrThrow(_stmt, "startedAt")
        val _columnIndexOfEndedAt: Int = getColumnIndexOrThrow(_stmt, "endedAt")
        val _columnIndexOfReviewedCount: Int = getColumnIndexOrThrow(_stmt, "reviewedCount")
        val _columnIndexOfUnderstoodCount: Int = getColumnIndexOrThrow(_stmt, "understoodCount")
        val _columnIndexOfStillConfusedCount: Int = getColumnIndexOrThrow(_stmt,
            "stillConfusedCount")
        val _columnIndexOfNeedsRevisionCount: Int = getColumnIndexOrThrow(_stmt,
            "needsRevisionCount")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: ReviewEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpSubject: Subject?
          val _tmp: String?
          if (_stmt.isNull(_columnIndexOfSubject)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_columnIndexOfSubject)
          }
          _tmpSubject = __converters.toSubject(_tmp)
          val _tmpStartedAt: Date
          val _tmp_1: Long?
          if (_stmt.isNull(_columnIndexOfStartedAt)) {
            _tmp_1 = null
          } else {
            _tmp_1 = _stmt.getLong(_columnIndexOfStartedAt)
          }
          val _tmp_2: Date? = __converters.toDate(_tmp_1)
          if (_tmp_2 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpStartedAt = _tmp_2
          }
          val _tmpEndedAt: Date?
          val _tmp_3: Long?
          if (_stmt.isNull(_columnIndexOfEndedAt)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getLong(_columnIndexOfEndedAt)
          }
          _tmpEndedAt = __converters.toDate(_tmp_3)
          val _tmpReviewedCount: Int
          _tmpReviewedCount = _stmt.getLong(_columnIndexOfReviewedCount).toInt()
          val _tmpUnderstoodCount: Int
          _tmpUnderstoodCount = _stmt.getLong(_columnIndexOfUnderstoodCount).toInt()
          val _tmpStillConfusedCount: Int
          _tmpStillConfusedCount = _stmt.getLong(_columnIndexOfStillConfusedCount).toInt()
          val _tmpNeedsRevisionCount: Int
          _tmpNeedsRevisionCount = _stmt.getLong(_columnIndexOfNeedsRevisionCount).toInt()
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpCreatedAt: Date
          val _tmp_4: Long?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getLong(_columnIndexOfCreatedAt)
          }
          val _tmp_5: Date? = __converters.toDate(_tmp_4)
          if (_tmp_5 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_5
          }
          val _tmpUpdatedAt: Date
          val _tmp_6: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_7: Date? = __converters.toDate(_tmp_6)
          if (_tmp_7 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_7
          }
          val _tmpSyncState: SyncState
          val _tmp_8: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_8 = null
          } else {
            _tmp_8 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_9: SyncState? = __converters.toSyncState(_tmp_8)
          if (_tmp_9 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_9
          }
          _result =
              ReviewEntity(_tmpId,_tmpSubject,_tmpStartedAt,_tmpEndedAt,_tmpReviewedCount,_tmpUnderstoodCount,_tmpStillConfusedCount,_tmpNeedsRevisionCount,_tmpNotes,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun pendingChanges(synced: SyncState): List<ReviewEntity> {
    val _sql: String = "SELECT * FROM reviews WHERE syncState != ?"
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
        val _columnIndexOfStartedAt: Int = getColumnIndexOrThrow(_stmt, "startedAt")
        val _columnIndexOfEndedAt: Int = getColumnIndexOrThrow(_stmt, "endedAt")
        val _columnIndexOfReviewedCount: Int = getColumnIndexOrThrow(_stmt, "reviewedCount")
        val _columnIndexOfUnderstoodCount: Int = getColumnIndexOrThrow(_stmt, "understoodCount")
        val _columnIndexOfStillConfusedCount: Int = getColumnIndexOrThrow(_stmt,
            "stillConfusedCount")
        val _columnIndexOfNeedsRevisionCount: Int = getColumnIndexOrThrow(_stmt,
            "needsRevisionCount")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<ReviewEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ReviewEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpSubject: Subject?
          val _tmp_1: String?
          if (_stmt.isNull(_columnIndexOfSubject)) {
            _tmp_1 = null
          } else {
            _tmp_1 = _stmt.getText(_columnIndexOfSubject)
          }
          _tmpSubject = __converters.toSubject(_tmp_1)
          val _tmpStartedAt: Date
          val _tmp_2: Long?
          if (_stmt.isNull(_columnIndexOfStartedAt)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getLong(_columnIndexOfStartedAt)
          }
          val _tmp_3: Date? = __converters.toDate(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpStartedAt = _tmp_3
          }
          val _tmpEndedAt: Date?
          val _tmp_4: Long?
          if (_stmt.isNull(_columnIndexOfEndedAt)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getLong(_columnIndexOfEndedAt)
          }
          _tmpEndedAt = __converters.toDate(_tmp_4)
          val _tmpReviewedCount: Int
          _tmpReviewedCount = _stmt.getLong(_columnIndexOfReviewedCount).toInt()
          val _tmpUnderstoodCount: Int
          _tmpUnderstoodCount = _stmt.getLong(_columnIndexOfUnderstoodCount).toInt()
          val _tmpStillConfusedCount: Int
          _tmpStillConfusedCount = _stmt.getLong(_columnIndexOfStillConfusedCount).toInt()
          val _tmpNeedsRevisionCount: Int
          _tmpNeedsRevisionCount = _stmt.getLong(_columnIndexOfNeedsRevisionCount).toInt()
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpCreatedAt: Date
          val _tmp_5: Long?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmp_5 = null
          } else {
            _tmp_5 = _stmt.getLong(_columnIndexOfCreatedAt)
          }
          val _tmp_6: Date? = __converters.toDate(_tmp_5)
          if (_tmp_6 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_6
          }
          val _tmpUpdatedAt: Date
          val _tmp_7: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_7 = null
          } else {
            _tmp_7 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_8: Date? = __converters.toDate(_tmp_7)
          if (_tmp_8 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_8
          }
          val _tmpSyncState: SyncState
          val _tmp_9: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_9 = null
          } else {
            _tmp_9 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_10: SyncState? = __converters.toSyncState(_tmp_9)
          if (_tmp_10 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_10
          }
          _item =
              ReviewEntity(_tmpId,_tmpSubject,_tmpStartedAt,_tmpEndedAt,_tmpReviewedCount,_tmpUnderstoodCount,_tmpStillConfusedCount,_tmpNeedsRevisionCount,_tmpNotes,_tmpCreatedAt,_tmpUpdatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeCount(): Flow<Int> {
    val _sql: String = "SELECT COUNT(*) FROM reviews"
    return createFlow(__db, false, arrayOf("reviews")) { _connection ->
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

  public override suspend fun markSync(id: String, state: SyncState) {
    val _sql: String = "UPDATE reviews SET syncState = ? WHERE id = ?"
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
    val _sql: String = "DELETE FROM reviews WHERE id = ?"
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
    val _sql: String = "DELETE FROM reviews"
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
