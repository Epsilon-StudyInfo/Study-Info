package com.studyinfo.app.`data`.database.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.studyinfo.app.`data`.database.converter.Converters
import com.studyinfo.app.`data`.database.entity.ProgressEntity
import com.studyinfo.app.domain.model.ChapterState
import com.studyinfo.app.domain.model.ExamType
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
public class ProgressDao_Impl(
  __db: RoomDatabase,
) : ProgressDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfProgressEntity: EntityInsertAdapter<ProgressEntity>

  private val __converters: Converters = Converters()
  init {
    this.__db = __db
    this.__insertAdapterOfProgressEntity = object : EntityInsertAdapter<ProgressEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `progress` (`id`,`chapterId`,`subject`,`examType`,`chapterState`,`percent`,`questionsAttempted`,`questionsCorrect`,`notes`,`updatedAt`,`createdAt`,`syncState`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ProgressEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.chapterId)
        val _tmp: String? = __converters.fromSubject(entity.subject)
        if (_tmp == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmp)
        }
        val _tmp_1: String? = __converters.fromExamType(entity.examType)
        if (_tmp_1 == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmp_1)
        }
        val _tmp_2: String? = __converters.fromChapterState(entity.chapterState)
        if (_tmp_2 == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmp_2)
        }
        statement.bindLong(6, entity.percent.toLong())
        statement.bindLong(7, entity.questionsAttempted.toLong())
        statement.bindLong(8, entity.questionsCorrect.toLong())
        val _tmpNotes: String? = entity.notes
        if (_tmpNotes == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpNotes)
        }
        val _tmp_3: Long? = __converters.fromDate(entity.updatedAt)
        if (_tmp_3 == null) {
          statement.bindNull(10)
        } else {
          statement.bindLong(10, _tmp_3)
        }
        val _tmp_4: Long? = __converters.fromDate(entity.createdAt)
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

  public override suspend fun upsert(progress: ProgressEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfProgressEntity.insert(_connection, progress)
  }

  public override suspend fun upsertAll(items: List<ProgressEntity>): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfProgressEntity.insert(_connection, items)
  }

  public override fun observeAll(): Flow<List<ProgressEntity>> {
    val _sql: String =
        "SELECT * FROM progress WHERE syncState != 'PENDING_DELETE' ORDER BY updatedAt DESC"
    return createFlow(__db, false, arrayOf("progress")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfChapterId: Int = getColumnIndexOrThrow(_stmt, "chapterId")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfExamType: Int = getColumnIndexOrThrow(_stmt, "examType")
        val _columnIndexOfChapterState: Int = getColumnIndexOrThrow(_stmt, "chapterState")
        val _columnIndexOfPercent: Int = getColumnIndexOrThrow(_stmt, "percent")
        val _columnIndexOfQuestionsAttempted: Int = getColumnIndexOrThrow(_stmt,
            "questionsAttempted")
        val _columnIndexOfQuestionsCorrect: Int = getColumnIndexOrThrow(_stmt, "questionsCorrect")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<ProgressEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ProgressEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpChapterId: String
          _tmpChapterId = _stmt.getText(_columnIndexOfChapterId)
          val _tmpSubject: Subject
          val _tmp: String?
          if (_stmt.isNull(_columnIndexOfSubject)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_columnIndexOfSubject)
          }
          val _tmp_1: Subject? = __converters.toSubject(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.Subject', but it was NULL.")
          } else {
            _tmpSubject = _tmp_1
          }
          val _tmpExamType: ExamType
          val _tmp_2: String?
          if (_stmt.isNull(_columnIndexOfExamType)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_columnIndexOfExamType)
          }
          val _tmp_3: ExamType? = __converters.toExamType(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.ExamType', but it was NULL.")
          } else {
            _tmpExamType = _tmp_3
          }
          val _tmpChapterState: ChapterState
          val _tmp_4: String?
          if (_stmt.isNull(_columnIndexOfChapterState)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getText(_columnIndexOfChapterState)
          }
          val _tmp_5: ChapterState? = __converters.toChapterState(_tmp_4)
          if (_tmp_5 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.ChapterState', but it was NULL.")
          } else {
            _tmpChapterState = _tmp_5
          }
          val _tmpPercent: Int
          _tmpPercent = _stmt.getLong(_columnIndexOfPercent).toInt()
          val _tmpQuestionsAttempted: Int
          _tmpQuestionsAttempted = _stmt.getLong(_columnIndexOfQuestionsAttempted).toInt()
          val _tmpQuestionsCorrect: Int
          _tmpQuestionsCorrect = _stmt.getLong(_columnIndexOfQuestionsCorrect).toInt()
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
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
          val _tmpCreatedAt: Date
          val _tmp_8: Long?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmp_8 = null
          } else {
            _tmp_8 = _stmt.getLong(_columnIndexOfCreatedAt)
          }
          val _tmp_9: Date? = __converters.toDate(_tmp_8)
          if (_tmp_9 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_9
          }
          val _tmpSyncState: SyncState
          val _tmp_10: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_10 = null
          } else {
            _tmp_10 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_11: SyncState? = __converters.toSyncState(_tmp_10)
          if (_tmp_11 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_11
          }
          _item =
              ProgressEntity(_tmpId,_tmpChapterId,_tmpSubject,_tmpExamType,_tmpChapterState,_tmpPercent,_tmpQuestionsAttempted,_tmpQuestionsCorrect,_tmpNotes,_tmpUpdatedAt,_tmpCreatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeBySubject(subject: Subject): Flow<List<ProgressEntity>> {
    val _sql: String =
        "SELECT * FROM progress WHERE syncState != 'PENDING_DELETE' AND subject = ? ORDER BY updatedAt DESC"
    return createFlow(__db, false, arrayOf("progress")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: String? = __converters.fromSubject(subject)
        if (_tmp == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, _tmp)
        }
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfChapterId: Int = getColumnIndexOrThrow(_stmt, "chapterId")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfExamType: Int = getColumnIndexOrThrow(_stmt, "examType")
        val _columnIndexOfChapterState: Int = getColumnIndexOrThrow(_stmt, "chapterState")
        val _columnIndexOfPercent: Int = getColumnIndexOrThrow(_stmt, "percent")
        val _columnIndexOfQuestionsAttempted: Int = getColumnIndexOrThrow(_stmt,
            "questionsAttempted")
        val _columnIndexOfQuestionsCorrect: Int = getColumnIndexOrThrow(_stmt, "questionsCorrect")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<ProgressEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ProgressEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpChapterId: String
          _tmpChapterId = _stmt.getText(_columnIndexOfChapterId)
          val _tmpSubject: Subject
          val _tmp_1: String?
          if (_stmt.isNull(_columnIndexOfSubject)) {
            _tmp_1 = null
          } else {
            _tmp_1 = _stmt.getText(_columnIndexOfSubject)
          }
          val _tmp_2: Subject? = __converters.toSubject(_tmp_1)
          if (_tmp_2 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.Subject', but it was NULL.")
          } else {
            _tmpSubject = _tmp_2
          }
          val _tmpExamType: ExamType
          val _tmp_3: String?
          if (_stmt.isNull(_columnIndexOfExamType)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getText(_columnIndexOfExamType)
          }
          val _tmp_4: ExamType? = __converters.toExamType(_tmp_3)
          if (_tmp_4 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.ExamType', but it was NULL.")
          } else {
            _tmpExamType = _tmp_4
          }
          val _tmpChapterState: ChapterState
          val _tmp_5: String?
          if (_stmt.isNull(_columnIndexOfChapterState)) {
            _tmp_5 = null
          } else {
            _tmp_5 = _stmt.getText(_columnIndexOfChapterState)
          }
          val _tmp_6: ChapterState? = __converters.toChapterState(_tmp_5)
          if (_tmp_6 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.ChapterState', but it was NULL.")
          } else {
            _tmpChapterState = _tmp_6
          }
          val _tmpPercent: Int
          _tmpPercent = _stmt.getLong(_columnIndexOfPercent).toInt()
          val _tmpQuestionsAttempted: Int
          _tmpQuestionsAttempted = _stmt.getLong(_columnIndexOfQuestionsAttempted).toInt()
          val _tmpQuestionsCorrect: Int
          _tmpQuestionsCorrect = _stmt.getLong(_columnIndexOfQuestionsCorrect).toInt()
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
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
          val _tmpSyncState: SyncState
          val _tmp_11: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_11 = null
          } else {
            _tmp_11 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_12: SyncState? = __converters.toSyncState(_tmp_11)
          if (_tmp_12 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_12
          }
          _item =
              ProgressEntity(_tmpId,_tmpChapterId,_tmpSubject,_tmpExamType,_tmpChapterState,_tmpPercent,_tmpQuestionsAttempted,_tmpQuestionsCorrect,_tmpNotes,_tmpUpdatedAt,_tmpCreatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeByExam(examType: ExamType): Flow<List<ProgressEntity>> {
    val _sql: String =
        "SELECT * FROM progress WHERE syncState != 'PENDING_DELETE' AND examType = ? ORDER BY subject, updatedAt DESC"
    return createFlow(__db, false, arrayOf("progress")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: String? = __converters.fromExamType(examType)
        if (_tmp == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, _tmp)
        }
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfChapterId: Int = getColumnIndexOrThrow(_stmt, "chapterId")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfExamType: Int = getColumnIndexOrThrow(_stmt, "examType")
        val _columnIndexOfChapterState: Int = getColumnIndexOrThrow(_stmt, "chapterState")
        val _columnIndexOfPercent: Int = getColumnIndexOrThrow(_stmt, "percent")
        val _columnIndexOfQuestionsAttempted: Int = getColumnIndexOrThrow(_stmt,
            "questionsAttempted")
        val _columnIndexOfQuestionsCorrect: Int = getColumnIndexOrThrow(_stmt, "questionsCorrect")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<ProgressEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ProgressEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpChapterId: String
          _tmpChapterId = _stmt.getText(_columnIndexOfChapterId)
          val _tmpSubject: Subject
          val _tmp_1: String?
          if (_stmt.isNull(_columnIndexOfSubject)) {
            _tmp_1 = null
          } else {
            _tmp_1 = _stmt.getText(_columnIndexOfSubject)
          }
          val _tmp_2: Subject? = __converters.toSubject(_tmp_1)
          if (_tmp_2 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.Subject', but it was NULL.")
          } else {
            _tmpSubject = _tmp_2
          }
          val _tmpExamType: ExamType
          val _tmp_3: String?
          if (_stmt.isNull(_columnIndexOfExamType)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getText(_columnIndexOfExamType)
          }
          val _tmp_4: ExamType? = __converters.toExamType(_tmp_3)
          if (_tmp_4 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.ExamType', but it was NULL.")
          } else {
            _tmpExamType = _tmp_4
          }
          val _tmpChapterState: ChapterState
          val _tmp_5: String?
          if (_stmt.isNull(_columnIndexOfChapterState)) {
            _tmp_5 = null
          } else {
            _tmp_5 = _stmt.getText(_columnIndexOfChapterState)
          }
          val _tmp_6: ChapterState? = __converters.toChapterState(_tmp_5)
          if (_tmp_6 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.ChapterState', but it was NULL.")
          } else {
            _tmpChapterState = _tmp_6
          }
          val _tmpPercent: Int
          _tmpPercent = _stmt.getLong(_columnIndexOfPercent).toInt()
          val _tmpQuestionsAttempted: Int
          _tmpQuestionsAttempted = _stmt.getLong(_columnIndexOfQuestionsAttempted).toInt()
          val _tmpQuestionsCorrect: Int
          _tmpQuestionsCorrect = _stmt.getLong(_columnIndexOfQuestionsCorrect).toInt()
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
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
          val _tmpSyncState: SyncState
          val _tmp_11: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_11 = null
          } else {
            _tmp_11 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_12: SyncState? = __converters.toSyncState(_tmp_11)
          if (_tmp_12 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_12
          }
          _item =
              ProgressEntity(_tmpId,_tmpChapterId,_tmpSubject,_tmpExamType,_tmpChapterState,_tmpPercent,_tmpQuestionsAttempted,_tmpQuestionsCorrect,_tmpNotes,_tmpUpdatedAt,_tmpCreatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getByChapter(chapterId: String): ProgressEntity? {
    val _sql: String =
        "SELECT * FROM progress WHERE syncState != 'PENDING_DELETE' AND chapterId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, chapterId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfChapterId: Int = getColumnIndexOrThrow(_stmt, "chapterId")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfExamType: Int = getColumnIndexOrThrow(_stmt, "examType")
        val _columnIndexOfChapterState: Int = getColumnIndexOrThrow(_stmt, "chapterState")
        val _columnIndexOfPercent: Int = getColumnIndexOrThrow(_stmt, "percent")
        val _columnIndexOfQuestionsAttempted: Int = getColumnIndexOrThrow(_stmt,
            "questionsAttempted")
        val _columnIndexOfQuestionsCorrect: Int = getColumnIndexOrThrow(_stmt, "questionsCorrect")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: ProgressEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpChapterId: String
          _tmpChapterId = _stmt.getText(_columnIndexOfChapterId)
          val _tmpSubject: Subject
          val _tmp: String?
          if (_stmt.isNull(_columnIndexOfSubject)) {
            _tmp = null
          } else {
            _tmp = _stmt.getText(_columnIndexOfSubject)
          }
          val _tmp_1: Subject? = __converters.toSubject(_tmp)
          if (_tmp_1 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.Subject', but it was NULL.")
          } else {
            _tmpSubject = _tmp_1
          }
          val _tmpExamType: ExamType
          val _tmp_2: String?
          if (_stmt.isNull(_columnIndexOfExamType)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_columnIndexOfExamType)
          }
          val _tmp_3: ExamType? = __converters.toExamType(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.ExamType', but it was NULL.")
          } else {
            _tmpExamType = _tmp_3
          }
          val _tmpChapterState: ChapterState
          val _tmp_4: String?
          if (_stmt.isNull(_columnIndexOfChapterState)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getText(_columnIndexOfChapterState)
          }
          val _tmp_5: ChapterState? = __converters.toChapterState(_tmp_4)
          if (_tmp_5 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.ChapterState', but it was NULL.")
          } else {
            _tmpChapterState = _tmp_5
          }
          val _tmpPercent: Int
          _tmpPercent = _stmt.getLong(_columnIndexOfPercent).toInt()
          val _tmpQuestionsAttempted: Int
          _tmpQuestionsAttempted = _stmt.getLong(_columnIndexOfQuestionsAttempted).toInt()
          val _tmpQuestionsCorrect: Int
          _tmpQuestionsCorrect = _stmt.getLong(_columnIndexOfQuestionsCorrect).toInt()
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
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
          val _tmpCreatedAt: Date
          val _tmp_8: Long?
          if (_stmt.isNull(_columnIndexOfCreatedAt)) {
            _tmp_8 = null
          } else {
            _tmp_8 = _stmt.getLong(_columnIndexOfCreatedAt)
          }
          val _tmp_9: Date? = __converters.toDate(_tmp_8)
          if (_tmp_9 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpCreatedAt = _tmp_9
          }
          val _tmpSyncState: SyncState
          val _tmp_10: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_10 = null
          } else {
            _tmp_10 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_11: SyncState? = __converters.toSyncState(_tmp_10)
          if (_tmp_11 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_11
          }
          _result =
              ProgressEntity(_tmpId,_tmpChapterId,_tmpSubject,_tmpExamType,_tmpChapterState,_tmpPercent,_tmpQuestionsAttempted,_tmpQuestionsCorrect,_tmpNotes,_tmpUpdatedAt,_tmpCreatedAt,_tmpSyncState)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun firstForSubjectExam(subject: Subject, examType: ExamType):
      ProgressEntity? {
    val _sql: String = "SELECT * FROM progress WHERE subject = ? AND examType = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: String? = __converters.fromSubject(subject)
        if (_tmp == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, _tmp)
        }
        _argIndex = 2
        val _tmp_1: String? = __converters.fromExamType(examType)
        if (_tmp_1 == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, _tmp_1)
        }
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfChapterId: Int = getColumnIndexOrThrow(_stmt, "chapterId")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfExamType: Int = getColumnIndexOrThrow(_stmt, "examType")
        val _columnIndexOfChapterState: Int = getColumnIndexOrThrow(_stmt, "chapterState")
        val _columnIndexOfPercent: Int = getColumnIndexOrThrow(_stmt, "percent")
        val _columnIndexOfQuestionsAttempted: Int = getColumnIndexOrThrow(_stmt,
            "questionsAttempted")
        val _columnIndexOfQuestionsCorrect: Int = getColumnIndexOrThrow(_stmt, "questionsCorrect")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: ProgressEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpChapterId: String
          _tmpChapterId = _stmt.getText(_columnIndexOfChapterId)
          val _tmpSubject: Subject
          val _tmp_2: String?
          if (_stmt.isNull(_columnIndexOfSubject)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_columnIndexOfSubject)
          }
          val _tmp_3: Subject? = __converters.toSubject(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.Subject', but it was NULL.")
          } else {
            _tmpSubject = _tmp_3
          }
          val _tmpExamType: ExamType
          val _tmp_4: String?
          if (_stmt.isNull(_columnIndexOfExamType)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getText(_columnIndexOfExamType)
          }
          val _tmp_5: ExamType? = __converters.toExamType(_tmp_4)
          if (_tmp_5 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.ExamType', but it was NULL.")
          } else {
            _tmpExamType = _tmp_5
          }
          val _tmpChapterState: ChapterState
          val _tmp_6: String?
          if (_stmt.isNull(_columnIndexOfChapterState)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getText(_columnIndexOfChapterState)
          }
          val _tmp_7: ChapterState? = __converters.toChapterState(_tmp_6)
          if (_tmp_7 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.ChapterState', but it was NULL.")
          } else {
            _tmpChapterState = _tmp_7
          }
          val _tmpPercent: Int
          _tmpPercent = _stmt.getLong(_columnIndexOfPercent).toInt()
          val _tmpQuestionsAttempted: Int
          _tmpQuestionsAttempted = _stmt.getLong(_columnIndexOfQuestionsAttempted).toInt()
          val _tmpQuestionsCorrect: Int
          _tmpQuestionsCorrect = _stmt.getLong(_columnIndexOfQuestionsCorrect).toInt()
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          val _tmpUpdatedAt: Date
          val _tmp_8: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_8 = null
          } else {
            _tmp_8 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_9: Date? = __converters.toDate(_tmp_8)
          if (_tmp_9 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_9
          }
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
          val _tmpSyncState: SyncState
          val _tmp_12: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_12 = null
          } else {
            _tmp_12 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_13: SyncState? = __converters.toSyncState(_tmp_12)
          if (_tmp_13 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_13
          }
          _result =
              ProgressEntity(_tmpId,_tmpChapterId,_tmpSubject,_tmpExamType,_tmpChapterState,_tmpPercent,_tmpQuestionsAttempted,_tmpQuestionsCorrect,_tmpNotes,_tmpUpdatedAt,_tmpCreatedAt,_tmpSyncState)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun pendingChanges(synced: SyncState): List<ProgressEntity> {
    val _sql: String = "SELECT * FROM progress WHERE syncState != ?"
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
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfExamType: Int = getColumnIndexOrThrow(_stmt, "examType")
        val _columnIndexOfChapterState: Int = getColumnIndexOrThrow(_stmt, "chapterState")
        val _columnIndexOfPercent: Int = getColumnIndexOrThrow(_stmt, "percent")
        val _columnIndexOfQuestionsAttempted: Int = getColumnIndexOrThrow(_stmt,
            "questionsAttempted")
        val _columnIndexOfQuestionsCorrect: Int = getColumnIndexOrThrow(_stmt, "questionsCorrect")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: MutableList<ProgressEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ProgressEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpChapterId: String
          _tmpChapterId = _stmt.getText(_columnIndexOfChapterId)
          val _tmpSubject: Subject
          val _tmp_1: String?
          if (_stmt.isNull(_columnIndexOfSubject)) {
            _tmp_1 = null
          } else {
            _tmp_1 = _stmt.getText(_columnIndexOfSubject)
          }
          val _tmp_2: Subject? = __converters.toSubject(_tmp_1)
          if (_tmp_2 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.Subject', but it was NULL.")
          } else {
            _tmpSubject = _tmp_2
          }
          val _tmpExamType: ExamType
          val _tmp_3: String?
          if (_stmt.isNull(_columnIndexOfExamType)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getText(_columnIndexOfExamType)
          }
          val _tmp_4: ExamType? = __converters.toExamType(_tmp_3)
          if (_tmp_4 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.ExamType', but it was NULL.")
          } else {
            _tmpExamType = _tmp_4
          }
          val _tmpChapterState: ChapterState
          val _tmp_5: String?
          if (_stmt.isNull(_columnIndexOfChapterState)) {
            _tmp_5 = null
          } else {
            _tmp_5 = _stmt.getText(_columnIndexOfChapterState)
          }
          val _tmp_6: ChapterState? = __converters.toChapterState(_tmp_5)
          if (_tmp_6 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.ChapterState', but it was NULL.")
          } else {
            _tmpChapterState = _tmp_6
          }
          val _tmpPercent: Int
          _tmpPercent = _stmt.getLong(_columnIndexOfPercent).toInt()
          val _tmpQuestionsAttempted: Int
          _tmpQuestionsAttempted = _stmt.getLong(_columnIndexOfQuestionsAttempted).toInt()
          val _tmpQuestionsCorrect: Int
          _tmpQuestionsCorrect = _stmt.getLong(_columnIndexOfQuestionsCorrect).toInt()
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
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
          val _tmpSyncState: SyncState
          val _tmp_11: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_11 = null
          } else {
            _tmp_11 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_12: SyncState? = __converters.toSyncState(_tmp_11)
          if (_tmp_12 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_12
          }
          _item =
              ProgressEntity(_tmpId,_tmpChapterId,_tmpSubject,_tmpExamType,_tmpChapterState,_tmpPercent,_tmpQuestionsAttempted,_tmpQuestionsCorrect,_tmpNotes,_tmpUpdatedAt,_tmpCreatedAt,_tmpSyncState)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun markSync(id: String, state: SyncState) {
    val _sql: String = "UPDATE progress SET syncState = ? WHERE id = ?"
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
    val _sql: String = "DELETE FROM progress WHERE id = ?"
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
    val _sql: String = "DELETE FROM progress"
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
