package com.studyinfo.app.`data`.database.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.studyinfo.app.`data`.database.converter.Converters
import com.studyinfo.app.`data`.database.entity.ErrorEntryEntity
import com.studyinfo.app.domain.model.Difficulty
import com.studyinfo.app.domain.model.ErrorStatus
import com.studyinfo.app.domain.model.ExamType
import com.studyinfo.app.domain.model.MistakeType
import com.studyinfo.app.domain.model.QuestionSource
import com.studyinfo.app.domain.model.Subject
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
public class ErrorDao_Impl(
  __db: RoomDatabase,
) : ErrorDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfErrorEntryEntity: EntityInsertAdapter<ErrorEntryEntity>

  private val __converters: Converters = Converters()
  init {
    this.__db = __db
    this.__insertAdapterOfErrorEntryEntity = object : EntityInsertAdapter<ErrorEntryEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `errors` (`id`,`title`,`questionText`,`subject`,`chapterName`,`topic`,`source`,`sourceRefId`,`dppNumber`,`chapterNumber`,`questionNumber`,`pyqYear`,`examType`,`shiftSession`,`instituteName`,`moduleNumber`,`exercise`,`testName`,`testNumber`,`testDate`,`marks`,`bookName`,`customSourceName`,`difficulty`,`mistakeType`,`attemptedSolution`,`correctSolution`,`explanation`,`lessonLearned`,`personalNotes`,`tags`,`status`,`favorite`,`reviewCount`,`lastReviewedAt`,`nextReviewAt`,`addedAt`,`updatedAt`,`syncState`,`originUnsolvedId`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ErrorEntryEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.questionText)
        val _tmp: String? = __converters.fromSubject(entity.subject)
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
        val _tmp_1: String? = __converters.fromQuestionSource(entity.source)
        if (_tmp_1 == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmp_1)
        }
        val _tmpSourceRefId: String? = entity.sourceRefId
        if (_tmpSourceRefId == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpSourceRefId)
        }
        val _tmpDppNumber: String? = entity.dppNumber
        if (_tmpDppNumber == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpDppNumber)
        }
        val _tmpChapterNumber: String? = entity.chapterNumber
        if (_tmpChapterNumber == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmpChapterNumber)
        }
        val _tmpQuestionNumber: String? = entity.questionNumber
        if (_tmpQuestionNumber == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmpQuestionNumber)
        }
        val _tmpPyqYear: Int? = entity.pyqYear
        if (_tmpPyqYear == null) {
          statement.bindNull(12)
        } else {
          statement.bindLong(12, _tmpPyqYear.toLong())
        }
        val _tmpExamType: ExamType? = entity.examType
        val _tmp_2: String? = __converters.fromExamType(_tmpExamType)
        if (_tmp_2 == null) {
          statement.bindNull(13)
        } else {
          statement.bindText(13, _tmp_2)
        }
        val _tmpShiftSession: String? = entity.shiftSession
        if (_tmpShiftSession == null) {
          statement.bindNull(14)
        } else {
          statement.bindText(14, _tmpShiftSession)
        }
        val _tmpInstituteName: String? = entity.instituteName
        if (_tmpInstituteName == null) {
          statement.bindNull(15)
        } else {
          statement.bindText(15, _tmpInstituteName)
        }
        val _tmpModuleNumber: String? = entity.moduleNumber
        if (_tmpModuleNumber == null) {
          statement.bindNull(16)
        } else {
          statement.bindText(16, _tmpModuleNumber)
        }
        val _tmpExercise: String? = entity.exercise
        if (_tmpExercise == null) {
          statement.bindNull(17)
        } else {
          statement.bindText(17, _tmpExercise)
        }
        val _tmpTestName: String? = entity.testName
        if (_tmpTestName == null) {
          statement.bindNull(18)
        } else {
          statement.bindText(18, _tmpTestName)
        }
        val _tmpTestNumber: String? = entity.testNumber
        if (_tmpTestNumber == null) {
          statement.bindNull(19)
        } else {
          statement.bindText(19, _tmpTestNumber)
        }
        val _tmpTestDate: Date? = entity.testDate
        val _tmp_3: Long? = __converters.fromDate(_tmpTestDate)
        if (_tmp_3 == null) {
          statement.bindNull(20)
        } else {
          statement.bindLong(20, _tmp_3)
        }
        val _tmpMarks: String? = entity.marks
        if (_tmpMarks == null) {
          statement.bindNull(21)
        } else {
          statement.bindText(21, _tmpMarks)
        }
        val _tmpBookName: String? = entity.bookName
        if (_tmpBookName == null) {
          statement.bindNull(22)
        } else {
          statement.bindText(22, _tmpBookName)
        }
        val _tmpCustomSourceName: String? = entity.customSourceName
        if (_tmpCustomSourceName == null) {
          statement.bindNull(23)
        } else {
          statement.bindText(23, _tmpCustomSourceName)
        }
        val _tmp_4: String? = __converters.fromDifficulty(entity.difficulty)
        if (_tmp_4 == null) {
          statement.bindNull(24)
        } else {
          statement.bindText(24, _tmp_4)
        }
        val _tmp_5: String? = __converters.fromMistakeType(entity.mistakeType)
        if (_tmp_5 == null) {
          statement.bindNull(25)
        } else {
          statement.bindText(25, _tmp_5)
        }
        val _tmpAttemptedSolution: String? = entity.attemptedSolution
        if (_tmpAttemptedSolution == null) {
          statement.bindNull(26)
        } else {
          statement.bindText(26, _tmpAttemptedSolution)
        }
        val _tmpCorrectSolution: String? = entity.correctSolution
        if (_tmpCorrectSolution == null) {
          statement.bindNull(27)
        } else {
          statement.bindText(27, _tmpCorrectSolution)
        }
        val _tmpExplanation: String? = entity.explanation
        if (_tmpExplanation == null) {
          statement.bindNull(28)
        } else {
          statement.bindText(28, _tmpExplanation)
        }
        val _tmpLessonLearned: String? = entity.lessonLearned
        if (_tmpLessonLearned == null) {
          statement.bindNull(29)
        } else {
          statement.bindText(29, _tmpLessonLearned)
        }
        val _tmpPersonalNotes: String? = entity.personalNotes
        if (_tmpPersonalNotes == null) {
          statement.bindNull(30)
        } else {
          statement.bindText(30, _tmpPersonalNotes)
        }
        val _tmp_6: String? = __converters.fromStringList(entity.tags)
        if (_tmp_6 == null) {
          statement.bindNull(31)
        } else {
          statement.bindText(31, _tmp_6)
        }
        val _tmp_7: String? = __converters.fromErrorStatus(entity.status)
        if (_tmp_7 == null) {
          statement.bindNull(32)
        } else {
          statement.bindText(32, _tmp_7)
        }
        val _tmp_8: Int = if (entity.favorite) 1 else 0
        statement.bindLong(33, _tmp_8.toLong())
        statement.bindLong(34, entity.reviewCount.toLong())
        val _tmpLastReviewedAt: Date? = entity.lastReviewedAt
        val _tmp_9: Long? = __converters.fromDate(_tmpLastReviewedAt)
        if (_tmp_9 == null) {
          statement.bindNull(35)
        } else {
          statement.bindLong(35, _tmp_9)
        }
        val _tmpNextReviewAt: Date? = entity.nextReviewAt
        val _tmp_10: Long? = __converters.fromDate(_tmpNextReviewAt)
        if (_tmp_10 == null) {
          statement.bindNull(36)
        } else {
          statement.bindLong(36, _tmp_10)
        }
        val _tmp_11: Long? = __converters.fromDate(entity.addedAt)
        if (_tmp_11 == null) {
          statement.bindNull(37)
        } else {
          statement.bindLong(37, _tmp_11)
        }
        val _tmp_12: Long? = __converters.fromDate(entity.updatedAt)
        if (_tmp_12 == null) {
          statement.bindNull(38)
        } else {
          statement.bindLong(38, _tmp_12)
        }
        val _tmp_13: String? = __converters.fromSyncState(entity.syncState)
        if (_tmp_13 == null) {
          statement.bindNull(39)
        } else {
          statement.bindText(39, _tmp_13)
        }
        val _tmpOriginUnsolvedId: String? = entity.originUnsolvedId
        if (_tmpOriginUnsolvedId == null) {
          statement.bindNull(40)
        } else {
          statement.bindText(40, _tmpOriginUnsolvedId)
        }
      }
    }
  }

  public override suspend fun upsert(error: ErrorEntryEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfErrorEntryEntity.insert(_connection, error)
  }

  public override suspend fun upsertAll(errors: List<ErrorEntryEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfErrorEntryEntity.insert(_connection, errors)
  }

  public override fun observeAll(): Flow<List<ErrorEntryEntity>> {
    val _sql: String =
        "SELECT * FROM errors WHERE syncState != 'PENDING_DELETE' ORDER BY addedAt DESC"
    return createFlow(__db, false, arrayOf("errors")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfQuestionText: Int = getColumnIndexOrThrow(_stmt, "questionText")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfChapterName: Int = getColumnIndexOrThrow(_stmt, "chapterName")
        val _columnIndexOfTopic: Int = getColumnIndexOrThrow(_stmt, "topic")
        val _columnIndexOfSource: Int = getColumnIndexOrThrow(_stmt, "source")
        val _columnIndexOfSourceRefId: Int = getColumnIndexOrThrow(_stmt, "sourceRefId")
        val _columnIndexOfDppNumber: Int = getColumnIndexOrThrow(_stmt, "dppNumber")
        val _columnIndexOfChapterNumber: Int = getColumnIndexOrThrow(_stmt, "chapterNumber")
        val _columnIndexOfQuestionNumber: Int = getColumnIndexOrThrow(_stmt, "questionNumber")
        val _columnIndexOfPyqYear: Int = getColumnIndexOrThrow(_stmt, "pyqYear")
        val _columnIndexOfExamType: Int = getColumnIndexOrThrow(_stmt, "examType")
        val _columnIndexOfShiftSession: Int = getColumnIndexOrThrow(_stmt, "shiftSession")
        val _columnIndexOfInstituteName: Int = getColumnIndexOrThrow(_stmt, "instituteName")
        val _columnIndexOfModuleNumber: Int = getColumnIndexOrThrow(_stmt, "moduleNumber")
        val _columnIndexOfExercise: Int = getColumnIndexOrThrow(_stmt, "exercise")
        val _columnIndexOfTestName: Int = getColumnIndexOrThrow(_stmt, "testName")
        val _columnIndexOfTestNumber: Int = getColumnIndexOrThrow(_stmt, "testNumber")
        val _columnIndexOfTestDate: Int = getColumnIndexOrThrow(_stmt, "testDate")
        val _columnIndexOfMarks: Int = getColumnIndexOrThrow(_stmt, "marks")
        val _columnIndexOfBookName: Int = getColumnIndexOrThrow(_stmt, "bookName")
        val _columnIndexOfCustomSourceName: Int = getColumnIndexOrThrow(_stmt, "customSourceName")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfMistakeType: Int = getColumnIndexOrThrow(_stmt, "mistakeType")
        val _columnIndexOfAttemptedSolution: Int = getColumnIndexOrThrow(_stmt, "attemptedSolution")
        val _columnIndexOfCorrectSolution: Int = getColumnIndexOrThrow(_stmt, "correctSolution")
        val _columnIndexOfExplanation: Int = getColumnIndexOrThrow(_stmt, "explanation")
        val _columnIndexOfLessonLearned: Int = getColumnIndexOrThrow(_stmt, "lessonLearned")
        val _columnIndexOfPersonalNotes: Int = getColumnIndexOrThrow(_stmt, "personalNotes")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfFavorite: Int = getColumnIndexOrThrow(_stmt, "favorite")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfNextReviewAt: Int = getColumnIndexOrThrow(_stmt, "nextReviewAt")
        val _columnIndexOfAddedAt: Int = getColumnIndexOrThrow(_stmt, "addedAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _columnIndexOfOriginUnsolvedId: Int = getColumnIndexOrThrow(_stmt, "originUnsolvedId")
        val _result: MutableList<ErrorEntryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ErrorEntryEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpQuestionText: String
          _tmpQuestionText = _stmt.getText(_columnIndexOfQuestionText)
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
          val _tmpSource: QuestionSource
          val _tmp_2: String?
          if (_stmt.isNull(_columnIndexOfSource)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_columnIndexOfSource)
          }
          val _tmp_3: QuestionSource? = __converters.toQuestionSource(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.QuestionSource', but it was NULL.")
          } else {
            _tmpSource = _tmp_3
          }
          val _tmpSourceRefId: String?
          if (_stmt.isNull(_columnIndexOfSourceRefId)) {
            _tmpSourceRefId = null
          } else {
            _tmpSourceRefId = _stmt.getText(_columnIndexOfSourceRefId)
          }
          val _tmpDppNumber: String?
          if (_stmt.isNull(_columnIndexOfDppNumber)) {
            _tmpDppNumber = null
          } else {
            _tmpDppNumber = _stmt.getText(_columnIndexOfDppNumber)
          }
          val _tmpChapterNumber: String?
          if (_stmt.isNull(_columnIndexOfChapterNumber)) {
            _tmpChapterNumber = null
          } else {
            _tmpChapterNumber = _stmt.getText(_columnIndexOfChapterNumber)
          }
          val _tmpQuestionNumber: String?
          if (_stmt.isNull(_columnIndexOfQuestionNumber)) {
            _tmpQuestionNumber = null
          } else {
            _tmpQuestionNumber = _stmt.getText(_columnIndexOfQuestionNumber)
          }
          val _tmpPyqYear: Int?
          if (_stmt.isNull(_columnIndexOfPyqYear)) {
            _tmpPyqYear = null
          } else {
            _tmpPyqYear = _stmt.getLong(_columnIndexOfPyqYear).toInt()
          }
          val _tmpExamType: ExamType?
          val _tmp_4: String?
          if (_stmt.isNull(_columnIndexOfExamType)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getText(_columnIndexOfExamType)
          }
          _tmpExamType = __converters.toExamType(_tmp_4)
          val _tmpShiftSession: String?
          if (_stmt.isNull(_columnIndexOfShiftSession)) {
            _tmpShiftSession = null
          } else {
            _tmpShiftSession = _stmt.getText(_columnIndexOfShiftSession)
          }
          val _tmpInstituteName: String?
          if (_stmt.isNull(_columnIndexOfInstituteName)) {
            _tmpInstituteName = null
          } else {
            _tmpInstituteName = _stmt.getText(_columnIndexOfInstituteName)
          }
          val _tmpModuleNumber: String?
          if (_stmt.isNull(_columnIndexOfModuleNumber)) {
            _tmpModuleNumber = null
          } else {
            _tmpModuleNumber = _stmt.getText(_columnIndexOfModuleNumber)
          }
          val _tmpExercise: String?
          if (_stmt.isNull(_columnIndexOfExercise)) {
            _tmpExercise = null
          } else {
            _tmpExercise = _stmt.getText(_columnIndexOfExercise)
          }
          val _tmpTestName: String?
          if (_stmt.isNull(_columnIndexOfTestName)) {
            _tmpTestName = null
          } else {
            _tmpTestName = _stmt.getText(_columnIndexOfTestName)
          }
          val _tmpTestNumber: String?
          if (_stmt.isNull(_columnIndexOfTestNumber)) {
            _tmpTestNumber = null
          } else {
            _tmpTestNumber = _stmt.getText(_columnIndexOfTestNumber)
          }
          val _tmpTestDate: Date?
          val _tmp_5: Long?
          if (_stmt.isNull(_columnIndexOfTestDate)) {
            _tmp_5 = null
          } else {
            _tmp_5 = _stmt.getLong(_columnIndexOfTestDate)
          }
          _tmpTestDate = __converters.toDate(_tmp_5)
          val _tmpMarks: String?
          if (_stmt.isNull(_columnIndexOfMarks)) {
            _tmpMarks = null
          } else {
            _tmpMarks = _stmt.getText(_columnIndexOfMarks)
          }
          val _tmpBookName: String?
          if (_stmt.isNull(_columnIndexOfBookName)) {
            _tmpBookName = null
          } else {
            _tmpBookName = _stmt.getText(_columnIndexOfBookName)
          }
          val _tmpCustomSourceName: String?
          if (_stmt.isNull(_columnIndexOfCustomSourceName)) {
            _tmpCustomSourceName = null
          } else {
            _tmpCustomSourceName = _stmt.getText(_columnIndexOfCustomSourceName)
          }
          val _tmpDifficulty: Difficulty
          val _tmp_6: String?
          if (_stmt.isNull(_columnIndexOfDifficulty)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getText(_columnIndexOfDifficulty)
          }
          val _tmp_7: Difficulty? = __converters.toDifficulty(_tmp_6)
          if (_tmp_7 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.Difficulty', but it was NULL.")
          } else {
            _tmpDifficulty = _tmp_7
          }
          val _tmpMistakeType: MistakeType
          val _tmp_8: String?
          if (_stmt.isNull(_columnIndexOfMistakeType)) {
            _tmp_8 = null
          } else {
            _tmp_8 = _stmt.getText(_columnIndexOfMistakeType)
          }
          val _tmp_9: MistakeType? = __converters.toMistakeType(_tmp_8)
          if (_tmp_9 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.MistakeType', but it was NULL.")
          } else {
            _tmpMistakeType = _tmp_9
          }
          val _tmpAttemptedSolution: String?
          if (_stmt.isNull(_columnIndexOfAttemptedSolution)) {
            _tmpAttemptedSolution = null
          } else {
            _tmpAttemptedSolution = _stmt.getText(_columnIndexOfAttemptedSolution)
          }
          val _tmpCorrectSolution: String?
          if (_stmt.isNull(_columnIndexOfCorrectSolution)) {
            _tmpCorrectSolution = null
          } else {
            _tmpCorrectSolution = _stmt.getText(_columnIndexOfCorrectSolution)
          }
          val _tmpExplanation: String?
          if (_stmt.isNull(_columnIndexOfExplanation)) {
            _tmpExplanation = null
          } else {
            _tmpExplanation = _stmt.getText(_columnIndexOfExplanation)
          }
          val _tmpLessonLearned: String?
          if (_stmt.isNull(_columnIndexOfLessonLearned)) {
            _tmpLessonLearned = null
          } else {
            _tmpLessonLearned = _stmt.getText(_columnIndexOfLessonLearned)
          }
          val _tmpPersonalNotes: String?
          if (_stmt.isNull(_columnIndexOfPersonalNotes)) {
            _tmpPersonalNotes = null
          } else {
            _tmpPersonalNotes = _stmt.getText(_columnIndexOfPersonalNotes)
          }
          val _tmpTags: List<String>
          val _tmp_10: String?
          if (_stmt.isNull(_columnIndexOfTags)) {
            _tmp_10 = null
          } else {
            _tmp_10 = _stmt.getText(_columnIndexOfTags)
          }
          val _tmp_11: List<String>? = __converters.toStringList(_tmp_10)
          if (_tmp_11 == null) {
            error("Expected NON-NULL 'kotlin.collections.List<kotlin.String>', but it was NULL.")
          } else {
            _tmpTags = _tmp_11
          }
          val _tmpStatus: ErrorStatus
          val _tmp_12: String?
          if (_stmt.isNull(_columnIndexOfStatus)) {
            _tmp_12 = null
          } else {
            _tmp_12 = _stmt.getText(_columnIndexOfStatus)
          }
          val _tmp_13: ErrorStatus? = __converters.toErrorStatus(_tmp_12)
          if (_tmp_13 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.ErrorStatus', but it was NULL.")
          } else {
            _tmpStatus = _tmp_13
          }
          val _tmpFavorite: Boolean
          val _tmp_14: Int
          _tmp_14 = _stmt.getLong(_columnIndexOfFavorite).toInt()
          _tmpFavorite = _tmp_14 != 0
          val _tmpReviewCount: Int
          _tmpReviewCount = _stmt.getLong(_columnIndexOfReviewCount).toInt()
          val _tmpLastReviewedAt: Date?
          val _tmp_15: Long?
          if (_stmt.isNull(_columnIndexOfLastReviewedAt)) {
            _tmp_15 = null
          } else {
            _tmp_15 = _stmt.getLong(_columnIndexOfLastReviewedAt)
          }
          _tmpLastReviewedAt = __converters.toDate(_tmp_15)
          val _tmpNextReviewAt: Date?
          val _tmp_16: Long?
          if (_stmt.isNull(_columnIndexOfNextReviewAt)) {
            _tmp_16 = null
          } else {
            _tmp_16 = _stmt.getLong(_columnIndexOfNextReviewAt)
          }
          _tmpNextReviewAt = __converters.toDate(_tmp_16)
          val _tmpAddedAt: Date
          val _tmp_17: Long?
          if (_stmt.isNull(_columnIndexOfAddedAt)) {
            _tmp_17 = null
          } else {
            _tmp_17 = _stmt.getLong(_columnIndexOfAddedAt)
          }
          val _tmp_18: Date? = __converters.toDate(_tmp_17)
          if (_tmp_18 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpAddedAt = _tmp_18
          }
          val _tmpUpdatedAt: Date
          val _tmp_19: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_19 = null
          } else {
            _tmp_19 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_20: Date? = __converters.toDate(_tmp_19)
          if (_tmp_20 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_20
          }
          val _tmpSyncState: SyncState
          val _tmp_21: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_21 = null
          } else {
            _tmp_21 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_22: SyncState? = __converters.toSyncState(_tmp_21)
          if (_tmp_22 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_22
          }
          val _tmpOriginUnsolvedId: String?
          if (_stmt.isNull(_columnIndexOfOriginUnsolvedId)) {
            _tmpOriginUnsolvedId = null
          } else {
            _tmpOriginUnsolvedId = _stmt.getText(_columnIndexOfOriginUnsolvedId)
          }
          _item =
              ErrorEntryEntity(_tmpId,_tmpTitle,_tmpQuestionText,_tmpSubject,_tmpChapterName,_tmpTopic,_tmpSource,_tmpSourceRefId,_tmpDppNumber,_tmpChapterNumber,_tmpQuestionNumber,_tmpPyqYear,_tmpExamType,_tmpShiftSession,_tmpInstituteName,_tmpModuleNumber,_tmpExercise,_tmpTestName,_tmpTestNumber,_tmpTestDate,_tmpMarks,_tmpBookName,_tmpCustomSourceName,_tmpDifficulty,_tmpMistakeType,_tmpAttemptedSolution,_tmpCorrectSolution,_tmpExplanation,_tmpLessonLearned,_tmpPersonalNotes,_tmpTags,_tmpStatus,_tmpFavorite,_tmpReviewCount,_tmpLastReviewedAt,_tmpNextReviewAt,_tmpAddedAt,_tmpUpdatedAt,_tmpSyncState,_tmpOriginUnsolvedId)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeById(id: String): Flow<ErrorEntryEntity?> {
    val _sql: String = "SELECT * FROM errors WHERE id = ?"
    return createFlow(__db, false, arrayOf("errors")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfQuestionText: Int = getColumnIndexOrThrow(_stmt, "questionText")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfChapterName: Int = getColumnIndexOrThrow(_stmt, "chapterName")
        val _columnIndexOfTopic: Int = getColumnIndexOrThrow(_stmt, "topic")
        val _columnIndexOfSource: Int = getColumnIndexOrThrow(_stmt, "source")
        val _columnIndexOfSourceRefId: Int = getColumnIndexOrThrow(_stmt, "sourceRefId")
        val _columnIndexOfDppNumber: Int = getColumnIndexOrThrow(_stmt, "dppNumber")
        val _columnIndexOfChapterNumber: Int = getColumnIndexOrThrow(_stmt, "chapterNumber")
        val _columnIndexOfQuestionNumber: Int = getColumnIndexOrThrow(_stmt, "questionNumber")
        val _columnIndexOfPyqYear: Int = getColumnIndexOrThrow(_stmt, "pyqYear")
        val _columnIndexOfExamType: Int = getColumnIndexOrThrow(_stmt, "examType")
        val _columnIndexOfShiftSession: Int = getColumnIndexOrThrow(_stmt, "shiftSession")
        val _columnIndexOfInstituteName: Int = getColumnIndexOrThrow(_stmt, "instituteName")
        val _columnIndexOfModuleNumber: Int = getColumnIndexOrThrow(_stmt, "moduleNumber")
        val _columnIndexOfExercise: Int = getColumnIndexOrThrow(_stmt, "exercise")
        val _columnIndexOfTestName: Int = getColumnIndexOrThrow(_stmt, "testName")
        val _columnIndexOfTestNumber: Int = getColumnIndexOrThrow(_stmt, "testNumber")
        val _columnIndexOfTestDate: Int = getColumnIndexOrThrow(_stmt, "testDate")
        val _columnIndexOfMarks: Int = getColumnIndexOrThrow(_stmt, "marks")
        val _columnIndexOfBookName: Int = getColumnIndexOrThrow(_stmt, "bookName")
        val _columnIndexOfCustomSourceName: Int = getColumnIndexOrThrow(_stmt, "customSourceName")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfMistakeType: Int = getColumnIndexOrThrow(_stmt, "mistakeType")
        val _columnIndexOfAttemptedSolution: Int = getColumnIndexOrThrow(_stmt, "attemptedSolution")
        val _columnIndexOfCorrectSolution: Int = getColumnIndexOrThrow(_stmt, "correctSolution")
        val _columnIndexOfExplanation: Int = getColumnIndexOrThrow(_stmt, "explanation")
        val _columnIndexOfLessonLearned: Int = getColumnIndexOrThrow(_stmt, "lessonLearned")
        val _columnIndexOfPersonalNotes: Int = getColumnIndexOrThrow(_stmt, "personalNotes")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfFavorite: Int = getColumnIndexOrThrow(_stmt, "favorite")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfNextReviewAt: Int = getColumnIndexOrThrow(_stmt, "nextReviewAt")
        val _columnIndexOfAddedAt: Int = getColumnIndexOrThrow(_stmt, "addedAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _columnIndexOfOriginUnsolvedId: Int = getColumnIndexOrThrow(_stmt, "originUnsolvedId")
        val _result: ErrorEntryEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpQuestionText: String
          _tmpQuestionText = _stmt.getText(_columnIndexOfQuestionText)
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
          val _tmpSource: QuestionSource
          val _tmp_2: String?
          if (_stmt.isNull(_columnIndexOfSource)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_columnIndexOfSource)
          }
          val _tmp_3: QuestionSource? = __converters.toQuestionSource(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.QuestionSource', but it was NULL.")
          } else {
            _tmpSource = _tmp_3
          }
          val _tmpSourceRefId: String?
          if (_stmt.isNull(_columnIndexOfSourceRefId)) {
            _tmpSourceRefId = null
          } else {
            _tmpSourceRefId = _stmt.getText(_columnIndexOfSourceRefId)
          }
          val _tmpDppNumber: String?
          if (_stmt.isNull(_columnIndexOfDppNumber)) {
            _tmpDppNumber = null
          } else {
            _tmpDppNumber = _stmt.getText(_columnIndexOfDppNumber)
          }
          val _tmpChapterNumber: String?
          if (_stmt.isNull(_columnIndexOfChapterNumber)) {
            _tmpChapterNumber = null
          } else {
            _tmpChapterNumber = _stmt.getText(_columnIndexOfChapterNumber)
          }
          val _tmpQuestionNumber: String?
          if (_stmt.isNull(_columnIndexOfQuestionNumber)) {
            _tmpQuestionNumber = null
          } else {
            _tmpQuestionNumber = _stmt.getText(_columnIndexOfQuestionNumber)
          }
          val _tmpPyqYear: Int?
          if (_stmt.isNull(_columnIndexOfPyqYear)) {
            _tmpPyqYear = null
          } else {
            _tmpPyqYear = _stmt.getLong(_columnIndexOfPyqYear).toInt()
          }
          val _tmpExamType: ExamType?
          val _tmp_4: String?
          if (_stmt.isNull(_columnIndexOfExamType)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getText(_columnIndexOfExamType)
          }
          _tmpExamType = __converters.toExamType(_tmp_4)
          val _tmpShiftSession: String?
          if (_stmt.isNull(_columnIndexOfShiftSession)) {
            _tmpShiftSession = null
          } else {
            _tmpShiftSession = _stmt.getText(_columnIndexOfShiftSession)
          }
          val _tmpInstituteName: String?
          if (_stmt.isNull(_columnIndexOfInstituteName)) {
            _tmpInstituteName = null
          } else {
            _tmpInstituteName = _stmt.getText(_columnIndexOfInstituteName)
          }
          val _tmpModuleNumber: String?
          if (_stmt.isNull(_columnIndexOfModuleNumber)) {
            _tmpModuleNumber = null
          } else {
            _tmpModuleNumber = _stmt.getText(_columnIndexOfModuleNumber)
          }
          val _tmpExercise: String?
          if (_stmt.isNull(_columnIndexOfExercise)) {
            _tmpExercise = null
          } else {
            _tmpExercise = _stmt.getText(_columnIndexOfExercise)
          }
          val _tmpTestName: String?
          if (_stmt.isNull(_columnIndexOfTestName)) {
            _tmpTestName = null
          } else {
            _tmpTestName = _stmt.getText(_columnIndexOfTestName)
          }
          val _tmpTestNumber: String?
          if (_stmt.isNull(_columnIndexOfTestNumber)) {
            _tmpTestNumber = null
          } else {
            _tmpTestNumber = _stmt.getText(_columnIndexOfTestNumber)
          }
          val _tmpTestDate: Date?
          val _tmp_5: Long?
          if (_stmt.isNull(_columnIndexOfTestDate)) {
            _tmp_5 = null
          } else {
            _tmp_5 = _stmt.getLong(_columnIndexOfTestDate)
          }
          _tmpTestDate = __converters.toDate(_tmp_5)
          val _tmpMarks: String?
          if (_stmt.isNull(_columnIndexOfMarks)) {
            _tmpMarks = null
          } else {
            _tmpMarks = _stmt.getText(_columnIndexOfMarks)
          }
          val _tmpBookName: String?
          if (_stmt.isNull(_columnIndexOfBookName)) {
            _tmpBookName = null
          } else {
            _tmpBookName = _stmt.getText(_columnIndexOfBookName)
          }
          val _tmpCustomSourceName: String?
          if (_stmt.isNull(_columnIndexOfCustomSourceName)) {
            _tmpCustomSourceName = null
          } else {
            _tmpCustomSourceName = _stmt.getText(_columnIndexOfCustomSourceName)
          }
          val _tmpDifficulty: Difficulty
          val _tmp_6: String?
          if (_stmt.isNull(_columnIndexOfDifficulty)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getText(_columnIndexOfDifficulty)
          }
          val _tmp_7: Difficulty? = __converters.toDifficulty(_tmp_6)
          if (_tmp_7 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.Difficulty', but it was NULL.")
          } else {
            _tmpDifficulty = _tmp_7
          }
          val _tmpMistakeType: MistakeType
          val _tmp_8: String?
          if (_stmt.isNull(_columnIndexOfMistakeType)) {
            _tmp_8 = null
          } else {
            _tmp_8 = _stmt.getText(_columnIndexOfMistakeType)
          }
          val _tmp_9: MistakeType? = __converters.toMistakeType(_tmp_8)
          if (_tmp_9 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.MistakeType', but it was NULL.")
          } else {
            _tmpMistakeType = _tmp_9
          }
          val _tmpAttemptedSolution: String?
          if (_stmt.isNull(_columnIndexOfAttemptedSolution)) {
            _tmpAttemptedSolution = null
          } else {
            _tmpAttemptedSolution = _stmt.getText(_columnIndexOfAttemptedSolution)
          }
          val _tmpCorrectSolution: String?
          if (_stmt.isNull(_columnIndexOfCorrectSolution)) {
            _tmpCorrectSolution = null
          } else {
            _tmpCorrectSolution = _stmt.getText(_columnIndexOfCorrectSolution)
          }
          val _tmpExplanation: String?
          if (_stmt.isNull(_columnIndexOfExplanation)) {
            _tmpExplanation = null
          } else {
            _tmpExplanation = _stmt.getText(_columnIndexOfExplanation)
          }
          val _tmpLessonLearned: String?
          if (_stmt.isNull(_columnIndexOfLessonLearned)) {
            _tmpLessonLearned = null
          } else {
            _tmpLessonLearned = _stmt.getText(_columnIndexOfLessonLearned)
          }
          val _tmpPersonalNotes: String?
          if (_stmt.isNull(_columnIndexOfPersonalNotes)) {
            _tmpPersonalNotes = null
          } else {
            _tmpPersonalNotes = _stmt.getText(_columnIndexOfPersonalNotes)
          }
          val _tmpTags: List<String>
          val _tmp_10: String?
          if (_stmt.isNull(_columnIndexOfTags)) {
            _tmp_10 = null
          } else {
            _tmp_10 = _stmt.getText(_columnIndexOfTags)
          }
          val _tmp_11: List<String>? = __converters.toStringList(_tmp_10)
          if (_tmp_11 == null) {
            error("Expected NON-NULL 'kotlin.collections.List<kotlin.String>', but it was NULL.")
          } else {
            _tmpTags = _tmp_11
          }
          val _tmpStatus: ErrorStatus
          val _tmp_12: String?
          if (_stmt.isNull(_columnIndexOfStatus)) {
            _tmp_12 = null
          } else {
            _tmp_12 = _stmt.getText(_columnIndexOfStatus)
          }
          val _tmp_13: ErrorStatus? = __converters.toErrorStatus(_tmp_12)
          if (_tmp_13 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.ErrorStatus', but it was NULL.")
          } else {
            _tmpStatus = _tmp_13
          }
          val _tmpFavorite: Boolean
          val _tmp_14: Int
          _tmp_14 = _stmt.getLong(_columnIndexOfFavorite).toInt()
          _tmpFavorite = _tmp_14 != 0
          val _tmpReviewCount: Int
          _tmpReviewCount = _stmt.getLong(_columnIndexOfReviewCount).toInt()
          val _tmpLastReviewedAt: Date?
          val _tmp_15: Long?
          if (_stmt.isNull(_columnIndexOfLastReviewedAt)) {
            _tmp_15 = null
          } else {
            _tmp_15 = _stmt.getLong(_columnIndexOfLastReviewedAt)
          }
          _tmpLastReviewedAt = __converters.toDate(_tmp_15)
          val _tmpNextReviewAt: Date?
          val _tmp_16: Long?
          if (_stmt.isNull(_columnIndexOfNextReviewAt)) {
            _tmp_16 = null
          } else {
            _tmp_16 = _stmt.getLong(_columnIndexOfNextReviewAt)
          }
          _tmpNextReviewAt = __converters.toDate(_tmp_16)
          val _tmpAddedAt: Date
          val _tmp_17: Long?
          if (_stmt.isNull(_columnIndexOfAddedAt)) {
            _tmp_17 = null
          } else {
            _tmp_17 = _stmt.getLong(_columnIndexOfAddedAt)
          }
          val _tmp_18: Date? = __converters.toDate(_tmp_17)
          if (_tmp_18 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpAddedAt = _tmp_18
          }
          val _tmpUpdatedAt: Date
          val _tmp_19: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_19 = null
          } else {
            _tmp_19 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_20: Date? = __converters.toDate(_tmp_19)
          if (_tmp_20 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_20
          }
          val _tmpSyncState: SyncState
          val _tmp_21: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_21 = null
          } else {
            _tmp_21 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_22: SyncState? = __converters.toSyncState(_tmp_21)
          if (_tmp_22 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_22
          }
          val _tmpOriginUnsolvedId: String?
          if (_stmt.isNull(_columnIndexOfOriginUnsolvedId)) {
            _tmpOriginUnsolvedId = null
          } else {
            _tmpOriginUnsolvedId = _stmt.getText(_columnIndexOfOriginUnsolvedId)
          }
          _result =
              ErrorEntryEntity(_tmpId,_tmpTitle,_tmpQuestionText,_tmpSubject,_tmpChapterName,_tmpTopic,_tmpSource,_tmpSourceRefId,_tmpDppNumber,_tmpChapterNumber,_tmpQuestionNumber,_tmpPyqYear,_tmpExamType,_tmpShiftSession,_tmpInstituteName,_tmpModuleNumber,_tmpExercise,_tmpTestName,_tmpTestNumber,_tmpTestDate,_tmpMarks,_tmpBookName,_tmpCustomSourceName,_tmpDifficulty,_tmpMistakeType,_tmpAttemptedSolution,_tmpCorrectSolution,_tmpExplanation,_tmpLessonLearned,_tmpPersonalNotes,_tmpTags,_tmpStatus,_tmpFavorite,_tmpReviewCount,_tmpLastReviewedAt,_tmpNextReviewAt,_tmpAddedAt,_tmpUpdatedAt,_tmpSyncState,_tmpOriginUnsolvedId)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getById(id: String): ErrorEntryEntity? {
    val _sql: String = "SELECT * FROM errors WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfQuestionText: Int = getColumnIndexOrThrow(_stmt, "questionText")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfChapterName: Int = getColumnIndexOrThrow(_stmt, "chapterName")
        val _columnIndexOfTopic: Int = getColumnIndexOrThrow(_stmt, "topic")
        val _columnIndexOfSource: Int = getColumnIndexOrThrow(_stmt, "source")
        val _columnIndexOfSourceRefId: Int = getColumnIndexOrThrow(_stmt, "sourceRefId")
        val _columnIndexOfDppNumber: Int = getColumnIndexOrThrow(_stmt, "dppNumber")
        val _columnIndexOfChapterNumber: Int = getColumnIndexOrThrow(_stmt, "chapterNumber")
        val _columnIndexOfQuestionNumber: Int = getColumnIndexOrThrow(_stmt, "questionNumber")
        val _columnIndexOfPyqYear: Int = getColumnIndexOrThrow(_stmt, "pyqYear")
        val _columnIndexOfExamType: Int = getColumnIndexOrThrow(_stmt, "examType")
        val _columnIndexOfShiftSession: Int = getColumnIndexOrThrow(_stmt, "shiftSession")
        val _columnIndexOfInstituteName: Int = getColumnIndexOrThrow(_stmt, "instituteName")
        val _columnIndexOfModuleNumber: Int = getColumnIndexOrThrow(_stmt, "moduleNumber")
        val _columnIndexOfExercise: Int = getColumnIndexOrThrow(_stmt, "exercise")
        val _columnIndexOfTestName: Int = getColumnIndexOrThrow(_stmt, "testName")
        val _columnIndexOfTestNumber: Int = getColumnIndexOrThrow(_stmt, "testNumber")
        val _columnIndexOfTestDate: Int = getColumnIndexOrThrow(_stmt, "testDate")
        val _columnIndexOfMarks: Int = getColumnIndexOrThrow(_stmt, "marks")
        val _columnIndexOfBookName: Int = getColumnIndexOrThrow(_stmt, "bookName")
        val _columnIndexOfCustomSourceName: Int = getColumnIndexOrThrow(_stmt, "customSourceName")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfMistakeType: Int = getColumnIndexOrThrow(_stmt, "mistakeType")
        val _columnIndexOfAttemptedSolution: Int = getColumnIndexOrThrow(_stmt, "attemptedSolution")
        val _columnIndexOfCorrectSolution: Int = getColumnIndexOrThrow(_stmt, "correctSolution")
        val _columnIndexOfExplanation: Int = getColumnIndexOrThrow(_stmt, "explanation")
        val _columnIndexOfLessonLearned: Int = getColumnIndexOrThrow(_stmt, "lessonLearned")
        val _columnIndexOfPersonalNotes: Int = getColumnIndexOrThrow(_stmt, "personalNotes")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfFavorite: Int = getColumnIndexOrThrow(_stmt, "favorite")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfNextReviewAt: Int = getColumnIndexOrThrow(_stmt, "nextReviewAt")
        val _columnIndexOfAddedAt: Int = getColumnIndexOrThrow(_stmt, "addedAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _columnIndexOfOriginUnsolvedId: Int = getColumnIndexOrThrow(_stmt, "originUnsolvedId")
        val _result: ErrorEntryEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpQuestionText: String
          _tmpQuestionText = _stmt.getText(_columnIndexOfQuestionText)
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
          val _tmpSource: QuestionSource
          val _tmp_2: String?
          if (_stmt.isNull(_columnIndexOfSource)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_columnIndexOfSource)
          }
          val _tmp_3: QuestionSource? = __converters.toQuestionSource(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.QuestionSource', but it was NULL.")
          } else {
            _tmpSource = _tmp_3
          }
          val _tmpSourceRefId: String?
          if (_stmt.isNull(_columnIndexOfSourceRefId)) {
            _tmpSourceRefId = null
          } else {
            _tmpSourceRefId = _stmt.getText(_columnIndexOfSourceRefId)
          }
          val _tmpDppNumber: String?
          if (_stmt.isNull(_columnIndexOfDppNumber)) {
            _tmpDppNumber = null
          } else {
            _tmpDppNumber = _stmt.getText(_columnIndexOfDppNumber)
          }
          val _tmpChapterNumber: String?
          if (_stmt.isNull(_columnIndexOfChapterNumber)) {
            _tmpChapterNumber = null
          } else {
            _tmpChapterNumber = _stmt.getText(_columnIndexOfChapterNumber)
          }
          val _tmpQuestionNumber: String?
          if (_stmt.isNull(_columnIndexOfQuestionNumber)) {
            _tmpQuestionNumber = null
          } else {
            _tmpQuestionNumber = _stmt.getText(_columnIndexOfQuestionNumber)
          }
          val _tmpPyqYear: Int?
          if (_stmt.isNull(_columnIndexOfPyqYear)) {
            _tmpPyqYear = null
          } else {
            _tmpPyqYear = _stmt.getLong(_columnIndexOfPyqYear).toInt()
          }
          val _tmpExamType: ExamType?
          val _tmp_4: String?
          if (_stmt.isNull(_columnIndexOfExamType)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getText(_columnIndexOfExamType)
          }
          _tmpExamType = __converters.toExamType(_tmp_4)
          val _tmpShiftSession: String?
          if (_stmt.isNull(_columnIndexOfShiftSession)) {
            _tmpShiftSession = null
          } else {
            _tmpShiftSession = _stmt.getText(_columnIndexOfShiftSession)
          }
          val _tmpInstituteName: String?
          if (_stmt.isNull(_columnIndexOfInstituteName)) {
            _tmpInstituteName = null
          } else {
            _tmpInstituteName = _stmt.getText(_columnIndexOfInstituteName)
          }
          val _tmpModuleNumber: String?
          if (_stmt.isNull(_columnIndexOfModuleNumber)) {
            _tmpModuleNumber = null
          } else {
            _tmpModuleNumber = _stmt.getText(_columnIndexOfModuleNumber)
          }
          val _tmpExercise: String?
          if (_stmt.isNull(_columnIndexOfExercise)) {
            _tmpExercise = null
          } else {
            _tmpExercise = _stmt.getText(_columnIndexOfExercise)
          }
          val _tmpTestName: String?
          if (_stmt.isNull(_columnIndexOfTestName)) {
            _tmpTestName = null
          } else {
            _tmpTestName = _stmt.getText(_columnIndexOfTestName)
          }
          val _tmpTestNumber: String?
          if (_stmt.isNull(_columnIndexOfTestNumber)) {
            _tmpTestNumber = null
          } else {
            _tmpTestNumber = _stmt.getText(_columnIndexOfTestNumber)
          }
          val _tmpTestDate: Date?
          val _tmp_5: Long?
          if (_stmt.isNull(_columnIndexOfTestDate)) {
            _tmp_5 = null
          } else {
            _tmp_5 = _stmt.getLong(_columnIndexOfTestDate)
          }
          _tmpTestDate = __converters.toDate(_tmp_5)
          val _tmpMarks: String?
          if (_stmt.isNull(_columnIndexOfMarks)) {
            _tmpMarks = null
          } else {
            _tmpMarks = _stmt.getText(_columnIndexOfMarks)
          }
          val _tmpBookName: String?
          if (_stmt.isNull(_columnIndexOfBookName)) {
            _tmpBookName = null
          } else {
            _tmpBookName = _stmt.getText(_columnIndexOfBookName)
          }
          val _tmpCustomSourceName: String?
          if (_stmt.isNull(_columnIndexOfCustomSourceName)) {
            _tmpCustomSourceName = null
          } else {
            _tmpCustomSourceName = _stmt.getText(_columnIndexOfCustomSourceName)
          }
          val _tmpDifficulty: Difficulty
          val _tmp_6: String?
          if (_stmt.isNull(_columnIndexOfDifficulty)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getText(_columnIndexOfDifficulty)
          }
          val _tmp_7: Difficulty? = __converters.toDifficulty(_tmp_6)
          if (_tmp_7 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.Difficulty', but it was NULL.")
          } else {
            _tmpDifficulty = _tmp_7
          }
          val _tmpMistakeType: MistakeType
          val _tmp_8: String?
          if (_stmt.isNull(_columnIndexOfMistakeType)) {
            _tmp_8 = null
          } else {
            _tmp_8 = _stmt.getText(_columnIndexOfMistakeType)
          }
          val _tmp_9: MistakeType? = __converters.toMistakeType(_tmp_8)
          if (_tmp_9 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.MistakeType', but it was NULL.")
          } else {
            _tmpMistakeType = _tmp_9
          }
          val _tmpAttemptedSolution: String?
          if (_stmt.isNull(_columnIndexOfAttemptedSolution)) {
            _tmpAttemptedSolution = null
          } else {
            _tmpAttemptedSolution = _stmt.getText(_columnIndexOfAttemptedSolution)
          }
          val _tmpCorrectSolution: String?
          if (_stmt.isNull(_columnIndexOfCorrectSolution)) {
            _tmpCorrectSolution = null
          } else {
            _tmpCorrectSolution = _stmt.getText(_columnIndexOfCorrectSolution)
          }
          val _tmpExplanation: String?
          if (_stmt.isNull(_columnIndexOfExplanation)) {
            _tmpExplanation = null
          } else {
            _tmpExplanation = _stmt.getText(_columnIndexOfExplanation)
          }
          val _tmpLessonLearned: String?
          if (_stmt.isNull(_columnIndexOfLessonLearned)) {
            _tmpLessonLearned = null
          } else {
            _tmpLessonLearned = _stmt.getText(_columnIndexOfLessonLearned)
          }
          val _tmpPersonalNotes: String?
          if (_stmt.isNull(_columnIndexOfPersonalNotes)) {
            _tmpPersonalNotes = null
          } else {
            _tmpPersonalNotes = _stmt.getText(_columnIndexOfPersonalNotes)
          }
          val _tmpTags: List<String>
          val _tmp_10: String?
          if (_stmt.isNull(_columnIndexOfTags)) {
            _tmp_10 = null
          } else {
            _tmp_10 = _stmt.getText(_columnIndexOfTags)
          }
          val _tmp_11: List<String>? = __converters.toStringList(_tmp_10)
          if (_tmp_11 == null) {
            error("Expected NON-NULL 'kotlin.collections.List<kotlin.String>', but it was NULL.")
          } else {
            _tmpTags = _tmp_11
          }
          val _tmpStatus: ErrorStatus
          val _tmp_12: String?
          if (_stmt.isNull(_columnIndexOfStatus)) {
            _tmp_12 = null
          } else {
            _tmp_12 = _stmt.getText(_columnIndexOfStatus)
          }
          val _tmp_13: ErrorStatus? = __converters.toErrorStatus(_tmp_12)
          if (_tmp_13 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.ErrorStatus', but it was NULL.")
          } else {
            _tmpStatus = _tmp_13
          }
          val _tmpFavorite: Boolean
          val _tmp_14: Int
          _tmp_14 = _stmt.getLong(_columnIndexOfFavorite).toInt()
          _tmpFavorite = _tmp_14 != 0
          val _tmpReviewCount: Int
          _tmpReviewCount = _stmt.getLong(_columnIndexOfReviewCount).toInt()
          val _tmpLastReviewedAt: Date?
          val _tmp_15: Long?
          if (_stmt.isNull(_columnIndexOfLastReviewedAt)) {
            _tmp_15 = null
          } else {
            _tmp_15 = _stmt.getLong(_columnIndexOfLastReviewedAt)
          }
          _tmpLastReviewedAt = __converters.toDate(_tmp_15)
          val _tmpNextReviewAt: Date?
          val _tmp_16: Long?
          if (_stmt.isNull(_columnIndexOfNextReviewAt)) {
            _tmp_16 = null
          } else {
            _tmp_16 = _stmt.getLong(_columnIndexOfNextReviewAt)
          }
          _tmpNextReviewAt = __converters.toDate(_tmp_16)
          val _tmpAddedAt: Date
          val _tmp_17: Long?
          if (_stmt.isNull(_columnIndexOfAddedAt)) {
            _tmp_17 = null
          } else {
            _tmp_17 = _stmt.getLong(_columnIndexOfAddedAt)
          }
          val _tmp_18: Date? = __converters.toDate(_tmp_17)
          if (_tmp_18 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpAddedAt = _tmp_18
          }
          val _tmpUpdatedAt: Date
          val _tmp_19: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_19 = null
          } else {
            _tmp_19 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_20: Date? = __converters.toDate(_tmp_19)
          if (_tmp_20 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_20
          }
          val _tmpSyncState: SyncState
          val _tmp_21: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_21 = null
          } else {
            _tmp_21 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_22: SyncState? = __converters.toSyncState(_tmp_21)
          if (_tmp_22 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_22
          }
          val _tmpOriginUnsolvedId: String?
          if (_stmt.isNull(_columnIndexOfOriginUnsolvedId)) {
            _tmpOriginUnsolvedId = null
          } else {
            _tmpOriginUnsolvedId = _stmt.getText(_columnIndexOfOriginUnsolvedId)
          }
          _result =
              ErrorEntryEntity(_tmpId,_tmpTitle,_tmpQuestionText,_tmpSubject,_tmpChapterName,_tmpTopic,_tmpSource,_tmpSourceRefId,_tmpDppNumber,_tmpChapterNumber,_tmpQuestionNumber,_tmpPyqYear,_tmpExamType,_tmpShiftSession,_tmpInstituteName,_tmpModuleNumber,_tmpExercise,_tmpTestName,_tmpTestNumber,_tmpTestDate,_tmpMarks,_tmpBookName,_tmpCustomSourceName,_tmpDifficulty,_tmpMistakeType,_tmpAttemptedSolution,_tmpCorrectSolution,_tmpExplanation,_tmpLessonLearned,_tmpPersonalNotes,_tmpTags,_tmpStatus,_tmpFavorite,_tmpReviewCount,_tmpLastReviewedAt,_tmpNextReviewAt,_tmpAddedAt,_tmpUpdatedAt,_tmpSyncState,_tmpOriginUnsolvedId)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun pendingChanges(synced: SyncState): List<ErrorEntryEntity> {
    val _sql: String = "SELECT * FROM errors WHERE syncState != ?"
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
        val _columnIndexOfQuestionText: Int = getColumnIndexOrThrow(_stmt, "questionText")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfChapterName: Int = getColumnIndexOrThrow(_stmt, "chapterName")
        val _columnIndexOfTopic: Int = getColumnIndexOrThrow(_stmt, "topic")
        val _columnIndexOfSource: Int = getColumnIndexOrThrow(_stmt, "source")
        val _columnIndexOfSourceRefId: Int = getColumnIndexOrThrow(_stmt, "sourceRefId")
        val _columnIndexOfDppNumber: Int = getColumnIndexOrThrow(_stmt, "dppNumber")
        val _columnIndexOfChapterNumber: Int = getColumnIndexOrThrow(_stmt, "chapterNumber")
        val _columnIndexOfQuestionNumber: Int = getColumnIndexOrThrow(_stmt, "questionNumber")
        val _columnIndexOfPyqYear: Int = getColumnIndexOrThrow(_stmt, "pyqYear")
        val _columnIndexOfExamType: Int = getColumnIndexOrThrow(_stmt, "examType")
        val _columnIndexOfShiftSession: Int = getColumnIndexOrThrow(_stmt, "shiftSession")
        val _columnIndexOfInstituteName: Int = getColumnIndexOrThrow(_stmt, "instituteName")
        val _columnIndexOfModuleNumber: Int = getColumnIndexOrThrow(_stmt, "moduleNumber")
        val _columnIndexOfExercise: Int = getColumnIndexOrThrow(_stmt, "exercise")
        val _columnIndexOfTestName: Int = getColumnIndexOrThrow(_stmt, "testName")
        val _columnIndexOfTestNumber: Int = getColumnIndexOrThrow(_stmt, "testNumber")
        val _columnIndexOfTestDate: Int = getColumnIndexOrThrow(_stmt, "testDate")
        val _columnIndexOfMarks: Int = getColumnIndexOrThrow(_stmt, "marks")
        val _columnIndexOfBookName: Int = getColumnIndexOrThrow(_stmt, "bookName")
        val _columnIndexOfCustomSourceName: Int = getColumnIndexOrThrow(_stmt, "customSourceName")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfMistakeType: Int = getColumnIndexOrThrow(_stmt, "mistakeType")
        val _columnIndexOfAttemptedSolution: Int = getColumnIndexOrThrow(_stmt, "attemptedSolution")
        val _columnIndexOfCorrectSolution: Int = getColumnIndexOrThrow(_stmt, "correctSolution")
        val _columnIndexOfExplanation: Int = getColumnIndexOrThrow(_stmt, "explanation")
        val _columnIndexOfLessonLearned: Int = getColumnIndexOrThrow(_stmt, "lessonLearned")
        val _columnIndexOfPersonalNotes: Int = getColumnIndexOrThrow(_stmt, "personalNotes")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfFavorite: Int = getColumnIndexOrThrow(_stmt, "favorite")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfNextReviewAt: Int = getColumnIndexOrThrow(_stmt, "nextReviewAt")
        val _columnIndexOfAddedAt: Int = getColumnIndexOrThrow(_stmt, "addedAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _columnIndexOfOriginUnsolvedId: Int = getColumnIndexOrThrow(_stmt, "originUnsolvedId")
        val _result: MutableList<ErrorEntryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ErrorEntryEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpQuestionText: String
          _tmpQuestionText = _stmt.getText(_columnIndexOfQuestionText)
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
          val _tmpSource: QuestionSource
          val _tmp_3: String?
          if (_stmt.isNull(_columnIndexOfSource)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getText(_columnIndexOfSource)
          }
          val _tmp_4: QuestionSource? = __converters.toQuestionSource(_tmp_3)
          if (_tmp_4 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.QuestionSource', but it was NULL.")
          } else {
            _tmpSource = _tmp_4
          }
          val _tmpSourceRefId: String?
          if (_stmt.isNull(_columnIndexOfSourceRefId)) {
            _tmpSourceRefId = null
          } else {
            _tmpSourceRefId = _stmt.getText(_columnIndexOfSourceRefId)
          }
          val _tmpDppNumber: String?
          if (_stmt.isNull(_columnIndexOfDppNumber)) {
            _tmpDppNumber = null
          } else {
            _tmpDppNumber = _stmt.getText(_columnIndexOfDppNumber)
          }
          val _tmpChapterNumber: String?
          if (_stmt.isNull(_columnIndexOfChapterNumber)) {
            _tmpChapterNumber = null
          } else {
            _tmpChapterNumber = _stmt.getText(_columnIndexOfChapterNumber)
          }
          val _tmpQuestionNumber: String?
          if (_stmt.isNull(_columnIndexOfQuestionNumber)) {
            _tmpQuestionNumber = null
          } else {
            _tmpQuestionNumber = _stmt.getText(_columnIndexOfQuestionNumber)
          }
          val _tmpPyqYear: Int?
          if (_stmt.isNull(_columnIndexOfPyqYear)) {
            _tmpPyqYear = null
          } else {
            _tmpPyqYear = _stmt.getLong(_columnIndexOfPyqYear).toInt()
          }
          val _tmpExamType: ExamType?
          val _tmp_5: String?
          if (_stmt.isNull(_columnIndexOfExamType)) {
            _tmp_5 = null
          } else {
            _tmp_5 = _stmt.getText(_columnIndexOfExamType)
          }
          _tmpExamType = __converters.toExamType(_tmp_5)
          val _tmpShiftSession: String?
          if (_stmt.isNull(_columnIndexOfShiftSession)) {
            _tmpShiftSession = null
          } else {
            _tmpShiftSession = _stmt.getText(_columnIndexOfShiftSession)
          }
          val _tmpInstituteName: String?
          if (_stmt.isNull(_columnIndexOfInstituteName)) {
            _tmpInstituteName = null
          } else {
            _tmpInstituteName = _stmt.getText(_columnIndexOfInstituteName)
          }
          val _tmpModuleNumber: String?
          if (_stmt.isNull(_columnIndexOfModuleNumber)) {
            _tmpModuleNumber = null
          } else {
            _tmpModuleNumber = _stmt.getText(_columnIndexOfModuleNumber)
          }
          val _tmpExercise: String?
          if (_stmt.isNull(_columnIndexOfExercise)) {
            _tmpExercise = null
          } else {
            _tmpExercise = _stmt.getText(_columnIndexOfExercise)
          }
          val _tmpTestName: String?
          if (_stmt.isNull(_columnIndexOfTestName)) {
            _tmpTestName = null
          } else {
            _tmpTestName = _stmt.getText(_columnIndexOfTestName)
          }
          val _tmpTestNumber: String?
          if (_stmt.isNull(_columnIndexOfTestNumber)) {
            _tmpTestNumber = null
          } else {
            _tmpTestNumber = _stmt.getText(_columnIndexOfTestNumber)
          }
          val _tmpTestDate: Date?
          val _tmp_6: Long?
          if (_stmt.isNull(_columnIndexOfTestDate)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getLong(_columnIndexOfTestDate)
          }
          _tmpTestDate = __converters.toDate(_tmp_6)
          val _tmpMarks: String?
          if (_stmt.isNull(_columnIndexOfMarks)) {
            _tmpMarks = null
          } else {
            _tmpMarks = _stmt.getText(_columnIndexOfMarks)
          }
          val _tmpBookName: String?
          if (_stmt.isNull(_columnIndexOfBookName)) {
            _tmpBookName = null
          } else {
            _tmpBookName = _stmt.getText(_columnIndexOfBookName)
          }
          val _tmpCustomSourceName: String?
          if (_stmt.isNull(_columnIndexOfCustomSourceName)) {
            _tmpCustomSourceName = null
          } else {
            _tmpCustomSourceName = _stmt.getText(_columnIndexOfCustomSourceName)
          }
          val _tmpDifficulty: Difficulty
          val _tmp_7: String?
          if (_stmt.isNull(_columnIndexOfDifficulty)) {
            _tmp_7 = null
          } else {
            _tmp_7 = _stmt.getText(_columnIndexOfDifficulty)
          }
          val _tmp_8: Difficulty? = __converters.toDifficulty(_tmp_7)
          if (_tmp_8 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.Difficulty', but it was NULL.")
          } else {
            _tmpDifficulty = _tmp_8
          }
          val _tmpMistakeType: MistakeType
          val _tmp_9: String?
          if (_stmt.isNull(_columnIndexOfMistakeType)) {
            _tmp_9 = null
          } else {
            _tmp_9 = _stmt.getText(_columnIndexOfMistakeType)
          }
          val _tmp_10: MistakeType? = __converters.toMistakeType(_tmp_9)
          if (_tmp_10 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.MistakeType', but it was NULL.")
          } else {
            _tmpMistakeType = _tmp_10
          }
          val _tmpAttemptedSolution: String?
          if (_stmt.isNull(_columnIndexOfAttemptedSolution)) {
            _tmpAttemptedSolution = null
          } else {
            _tmpAttemptedSolution = _stmt.getText(_columnIndexOfAttemptedSolution)
          }
          val _tmpCorrectSolution: String?
          if (_stmt.isNull(_columnIndexOfCorrectSolution)) {
            _tmpCorrectSolution = null
          } else {
            _tmpCorrectSolution = _stmt.getText(_columnIndexOfCorrectSolution)
          }
          val _tmpExplanation: String?
          if (_stmt.isNull(_columnIndexOfExplanation)) {
            _tmpExplanation = null
          } else {
            _tmpExplanation = _stmt.getText(_columnIndexOfExplanation)
          }
          val _tmpLessonLearned: String?
          if (_stmt.isNull(_columnIndexOfLessonLearned)) {
            _tmpLessonLearned = null
          } else {
            _tmpLessonLearned = _stmt.getText(_columnIndexOfLessonLearned)
          }
          val _tmpPersonalNotes: String?
          if (_stmt.isNull(_columnIndexOfPersonalNotes)) {
            _tmpPersonalNotes = null
          } else {
            _tmpPersonalNotes = _stmt.getText(_columnIndexOfPersonalNotes)
          }
          val _tmpTags: List<String>
          val _tmp_11: String?
          if (_stmt.isNull(_columnIndexOfTags)) {
            _tmp_11 = null
          } else {
            _tmp_11 = _stmt.getText(_columnIndexOfTags)
          }
          val _tmp_12: List<String>? = __converters.toStringList(_tmp_11)
          if (_tmp_12 == null) {
            error("Expected NON-NULL 'kotlin.collections.List<kotlin.String>', but it was NULL.")
          } else {
            _tmpTags = _tmp_12
          }
          val _tmpStatus: ErrorStatus
          val _tmp_13: String?
          if (_stmt.isNull(_columnIndexOfStatus)) {
            _tmp_13 = null
          } else {
            _tmp_13 = _stmt.getText(_columnIndexOfStatus)
          }
          val _tmp_14: ErrorStatus? = __converters.toErrorStatus(_tmp_13)
          if (_tmp_14 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.ErrorStatus', but it was NULL.")
          } else {
            _tmpStatus = _tmp_14
          }
          val _tmpFavorite: Boolean
          val _tmp_15: Int
          _tmp_15 = _stmt.getLong(_columnIndexOfFavorite).toInt()
          _tmpFavorite = _tmp_15 != 0
          val _tmpReviewCount: Int
          _tmpReviewCount = _stmt.getLong(_columnIndexOfReviewCount).toInt()
          val _tmpLastReviewedAt: Date?
          val _tmp_16: Long?
          if (_stmt.isNull(_columnIndexOfLastReviewedAt)) {
            _tmp_16 = null
          } else {
            _tmp_16 = _stmt.getLong(_columnIndexOfLastReviewedAt)
          }
          _tmpLastReviewedAt = __converters.toDate(_tmp_16)
          val _tmpNextReviewAt: Date?
          val _tmp_17: Long?
          if (_stmt.isNull(_columnIndexOfNextReviewAt)) {
            _tmp_17 = null
          } else {
            _tmp_17 = _stmt.getLong(_columnIndexOfNextReviewAt)
          }
          _tmpNextReviewAt = __converters.toDate(_tmp_17)
          val _tmpAddedAt: Date
          val _tmp_18: Long?
          if (_stmt.isNull(_columnIndexOfAddedAt)) {
            _tmp_18 = null
          } else {
            _tmp_18 = _stmt.getLong(_columnIndexOfAddedAt)
          }
          val _tmp_19: Date? = __converters.toDate(_tmp_18)
          if (_tmp_19 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpAddedAt = _tmp_19
          }
          val _tmpUpdatedAt: Date
          val _tmp_20: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_20 = null
          } else {
            _tmp_20 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_21: Date? = __converters.toDate(_tmp_20)
          if (_tmp_21 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_21
          }
          val _tmpSyncState: SyncState
          val _tmp_22: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_22 = null
          } else {
            _tmp_22 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_23: SyncState? = __converters.toSyncState(_tmp_22)
          if (_tmp_23 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_23
          }
          val _tmpOriginUnsolvedId: String?
          if (_stmt.isNull(_columnIndexOfOriginUnsolvedId)) {
            _tmpOriginUnsolvedId = null
          } else {
            _tmpOriginUnsolvedId = _stmt.getText(_columnIndexOfOriginUnsolvedId)
          }
          _item =
              ErrorEntryEntity(_tmpId,_tmpTitle,_tmpQuestionText,_tmpSubject,_tmpChapterName,_tmpTopic,_tmpSource,_tmpSourceRefId,_tmpDppNumber,_tmpChapterNumber,_tmpQuestionNumber,_tmpPyqYear,_tmpExamType,_tmpShiftSession,_tmpInstituteName,_tmpModuleNumber,_tmpExercise,_tmpTestName,_tmpTestNumber,_tmpTestDate,_tmpMarks,_tmpBookName,_tmpCustomSourceName,_tmpDifficulty,_tmpMistakeType,_tmpAttemptedSolution,_tmpCorrectSolution,_tmpExplanation,_tmpLessonLearned,_tmpPersonalNotes,_tmpTags,_tmpStatus,_tmpFavorite,_tmpReviewCount,_tmpLastReviewedAt,_tmpNextReviewAt,_tmpAddedAt,_tmpUpdatedAt,_tmpSyncState,_tmpOriginUnsolvedId)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeDueForReview(now: Long, archived: ErrorStatus):
      Flow<List<ErrorEntryEntity>> {
    val _sql: String =
        "SELECT * FROM errors WHERE syncState != 'PENDING_DELETE' AND status != ? AND nextReviewAt IS NOT NULL AND nextReviewAt <= ? ORDER BY nextReviewAt ASC"
    return createFlow(__db, false, arrayOf("errors")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: String? = __converters.fromErrorStatus(archived)
        if (_tmp == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, _tmp)
        }
        _argIndex = 2
        _stmt.bindLong(_argIndex, now)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfQuestionText: Int = getColumnIndexOrThrow(_stmt, "questionText")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfChapterName: Int = getColumnIndexOrThrow(_stmt, "chapterName")
        val _columnIndexOfTopic: Int = getColumnIndexOrThrow(_stmt, "topic")
        val _columnIndexOfSource: Int = getColumnIndexOrThrow(_stmt, "source")
        val _columnIndexOfSourceRefId: Int = getColumnIndexOrThrow(_stmt, "sourceRefId")
        val _columnIndexOfDppNumber: Int = getColumnIndexOrThrow(_stmt, "dppNumber")
        val _columnIndexOfChapterNumber: Int = getColumnIndexOrThrow(_stmt, "chapterNumber")
        val _columnIndexOfQuestionNumber: Int = getColumnIndexOrThrow(_stmt, "questionNumber")
        val _columnIndexOfPyqYear: Int = getColumnIndexOrThrow(_stmt, "pyqYear")
        val _columnIndexOfExamType: Int = getColumnIndexOrThrow(_stmt, "examType")
        val _columnIndexOfShiftSession: Int = getColumnIndexOrThrow(_stmt, "shiftSession")
        val _columnIndexOfInstituteName: Int = getColumnIndexOrThrow(_stmt, "instituteName")
        val _columnIndexOfModuleNumber: Int = getColumnIndexOrThrow(_stmt, "moduleNumber")
        val _columnIndexOfExercise: Int = getColumnIndexOrThrow(_stmt, "exercise")
        val _columnIndexOfTestName: Int = getColumnIndexOrThrow(_stmt, "testName")
        val _columnIndexOfTestNumber: Int = getColumnIndexOrThrow(_stmt, "testNumber")
        val _columnIndexOfTestDate: Int = getColumnIndexOrThrow(_stmt, "testDate")
        val _columnIndexOfMarks: Int = getColumnIndexOrThrow(_stmt, "marks")
        val _columnIndexOfBookName: Int = getColumnIndexOrThrow(_stmt, "bookName")
        val _columnIndexOfCustomSourceName: Int = getColumnIndexOrThrow(_stmt, "customSourceName")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfMistakeType: Int = getColumnIndexOrThrow(_stmt, "mistakeType")
        val _columnIndexOfAttemptedSolution: Int = getColumnIndexOrThrow(_stmt, "attemptedSolution")
        val _columnIndexOfCorrectSolution: Int = getColumnIndexOrThrow(_stmt, "correctSolution")
        val _columnIndexOfExplanation: Int = getColumnIndexOrThrow(_stmt, "explanation")
        val _columnIndexOfLessonLearned: Int = getColumnIndexOrThrow(_stmt, "lessonLearned")
        val _columnIndexOfPersonalNotes: Int = getColumnIndexOrThrow(_stmt, "personalNotes")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfFavorite: Int = getColumnIndexOrThrow(_stmt, "favorite")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfNextReviewAt: Int = getColumnIndexOrThrow(_stmt, "nextReviewAt")
        val _columnIndexOfAddedAt: Int = getColumnIndexOrThrow(_stmt, "addedAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _columnIndexOfOriginUnsolvedId: Int = getColumnIndexOrThrow(_stmt, "originUnsolvedId")
        val _result: MutableList<ErrorEntryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ErrorEntryEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpQuestionText: String
          _tmpQuestionText = _stmt.getText(_columnIndexOfQuestionText)
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
          val _tmpSource: QuestionSource
          val _tmp_3: String?
          if (_stmt.isNull(_columnIndexOfSource)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getText(_columnIndexOfSource)
          }
          val _tmp_4: QuestionSource? = __converters.toQuestionSource(_tmp_3)
          if (_tmp_4 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.QuestionSource', but it was NULL.")
          } else {
            _tmpSource = _tmp_4
          }
          val _tmpSourceRefId: String?
          if (_stmt.isNull(_columnIndexOfSourceRefId)) {
            _tmpSourceRefId = null
          } else {
            _tmpSourceRefId = _stmt.getText(_columnIndexOfSourceRefId)
          }
          val _tmpDppNumber: String?
          if (_stmt.isNull(_columnIndexOfDppNumber)) {
            _tmpDppNumber = null
          } else {
            _tmpDppNumber = _stmt.getText(_columnIndexOfDppNumber)
          }
          val _tmpChapterNumber: String?
          if (_stmt.isNull(_columnIndexOfChapterNumber)) {
            _tmpChapterNumber = null
          } else {
            _tmpChapterNumber = _stmt.getText(_columnIndexOfChapterNumber)
          }
          val _tmpQuestionNumber: String?
          if (_stmt.isNull(_columnIndexOfQuestionNumber)) {
            _tmpQuestionNumber = null
          } else {
            _tmpQuestionNumber = _stmt.getText(_columnIndexOfQuestionNumber)
          }
          val _tmpPyqYear: Int?
          if (_stmt.isNull(_columnIndexOfPyqYear)) {
            _tmpPyqYear = null
          } else {
            _tmpPyqYear = _stmt.getLong(_columnIndexOfPyqYear).toInt()
          }
          val _tmpExamType: ExamType?
          val _tmp_5: String?
          if (_stmt.isNull(_columnIndexOfExamType)) {
            _tmp_5 = null
          } else {
            _tmp_5 = _stmt.getText(_columnIndexOfExamType)
          }
          _tmpExamType = __converters.toExamType(_tmp_5)
          val _tmpShiftSession: String?
          if (_stmt.isNull(_columnIndexOfShiftSession)) {
            _tmpShiftSession = null
          } else {
            _tmpShiftSession = _stmt.getText(_columnIndexOfShiftSession)
          }
          val _tmpInstituteName: String?
          if (_stmt.isNull(_columnIndexOfInstituteName)) {
            _tmpInstituteName = null
          } else {
            _tmpInstituteName = _stmt.getText(_columnIndexOfInstituteName)
          }
          val _tmpModuleNumber: String?
          if (_stmt.isNull(_columnIndexOfModuleNumber)) {
            _tmpModuleNumber = null
          } else {
            _tmpModuleNumber = _stmt.getText(_columnIndexOfModuleNumber)
          }
          val _tmpExercise: String?
          if (_stmt.isNull(_columnIndexOfExercise)) {
            _tmpExercise = null
          } else {
            _tmpExercise = _stmt.getText(_columnIndexOfExercise)
          }
          val _tmpTestName: String?
          if (_stmt.isNull(_columnIndexOfTestName)) {
            _tmpTestName = null
          } else {
            _tmpTestName = _stmt.getText(_columnIndexOfTestName)
          }
          val _tmpTestNumber: String?
          if (_stmt.isNull(_columnIndexOfTestNumber)) {
            _tmpTestNumber = null
          } else {
            _tmpTestNumber = _stmt.getText(_columnIndexOfTestNumber)
          }
          val _tmpTestDate: Date?
          val _tmp_6: Long?
          if (_stmt.isNull(_columnIndexOfTestDate)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getLong(_columnIndexOfTestDate)
          }
          _tmpTestDate = __converters.toDate(_tmp_6)
          val _tmpMarks: String?
          if (_stmt.isNull(_columnIndexOfMarks)) {
            _tmpMarks = null
          } else {
            _tmpMarks = _stmt.getText(_columnIndexOfMarks)
          }
          val _tmpBookName: String?
          if (_stmt.isNull(_columnIndexOfBookName)) {
            _tmpBookName = null
          } else {
            _tmpBookName = _stmt.getText(_columnIndexOfBookName)
          }
          val _tmpCustomSourceName: String?
          if (_stmt.isNull(_columnIndexOfCustomSourceName)) {
            _tmpCustomSourceName = null
          } else {
            _tmpCustomSourceName = _stmt.getText(_columnIndexOfCustomSourceName)
          }
          val _tmpDifficulty: Difficulty
          val _tmp_7: String?
          if (_stmt.isNull(_columnIndexOfDifficulty)) {
            _tmp_7 = null
          } else {
            _tmp_7 = _stmt.getText(_columnIndexOfDifficulty)
          }
          val _tmp_8: Difficulty? = __converters.toDifficulty(_tmp_7)
          if (_tmp_8 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.Difficulty', but it was NULL.")
          } else {
            _tmpDifficulty = _tmp_8
          }
          val _tmpMistakeType: MistakeType
          val _tmp_9: String?
          if (_stmt.isNull(_columnIndexOfMistakeType)) {
            _tmp_9 = null
          } else {
            _tmp_9 = _stmt.getText(_columnIndexOfMistakeType)
          }
          val _tmp_10: MistakeType? = __converters.toMistakeType(_tmp_9)
          if (_tmp_10 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.MistakeType', but it was NULL.")
          } else {
            _tmpMistakeType = _tmp_10
          }
          val _tmpAttemptedSolution: String?
          if (_stmt.isNull(_columnIndexOfAttemptedSolution)) {
            _tmpAttemptedSolution = null
          } else {
            _tmpAttemptedSolution = _stmt.getText(_columnIndexOfAttemptedSolution)
          }
          val _tmpCorrectSolution: String?
          if (_stmt.isNull(_columnIndexOfCorrectSolution)) {
            _tmpCorrectSolution = null
          } else {
            _tmpCorrectSolution = _stmt.getText(_columnIndexOfCorrectSolution)
          }
          val _tmpExplanation: String?
          if (_stmt.isNull(_columnIndexOfExplanation)) {
            _tmpExplanation = null
          } else {
            _tmpExplanation = _stmt.getText(_columnIndexOfExplanation)
          }
          val _tmpLessonLearned: String?
          if (_stmt.isNull(_columnIndexOfLessonLearned)) {
            _tmpLessonLearned = null
          } else {
            _tmpLessonLearned = _stmt.getText(_columnIndexOfLessonLearned)
          }
          val _tmpPersonalNotes: String?
          if (_stmt.isNull(_columnIndexOfPersonalNotes)) {
            _tmpPersonalNotes = null
          } else {
            _tmpPersonalNotes = _stmt.getText(_columnIndexOfPersonalNotes)
          }
          val _tmpTags: List<String>
          val _tmp_11: String?
          if (_stmt.isNull(_columnIndexOfTags)) {
            _tmp_11 = null
          } else {
            _tmp_11 = _stmt.getText(_columnIndexOfTags)
          }
          val _tmp_12: List<String>? = __converters.toStringList(_tmp_11)
          if (_tmp_12 == null) {
            error("Expected NON-NULL 'kotlin.collections.List<kotlin.String>', but it was NULL.")
          } else {
            _tmpTags = _tmp_12
          }
          val _tmpStatus: ErrorStatus
          val _tmp_13: String?
          if (_stmt.isNull(_columnIndexOfStatus)) {
            _tmp_13 = null
          } else {
            _tmp_13 = _stmt.getText(_columnIndexOfStatus)
          }
          val _tmp_14: ErrorStatus? = __converters.toErrorStatus(_tmp_13)
          if (_tmp_14 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.ErrorStatus', but it was NULL.")
          } else {
            _tmpStatus = _tmp_14
          }
          val _tmpFavorite: Boolean
          val _tmp_15: Int
          _tmp_15 = _stmt.getLong(_columnIndexOfFavorite).toInt()
          _tmpFavorite = _tmp_15 != 0
          val _tmpReviewCount: Int
          _tmpReviewCount = _stmt.getLong(_columnIndexOfReviewCount).toInt()
          val _tmpLastReviewedAt: Date?
          val _tmp_16: Long?
          if (_stmt.isNull(_columnIndexOfLastReviewedAt)) {
            _tmp_16 = null
          } else {
            _tmp_16 = _stmt.getLong(_columnIndexOfLastReviewedAt)
          }
          _tmpLastReviewedAt = __converters.toDate(_tmp_16)
          val _tmpNextReviewAt: Date?
          val _tmp_17: Long?
          if (_stmt.isNull(_columnIndexOfNextReviewAt)) {
            _tmp_17 = null
          } else {
            _tmp_17 = _stmt.getLong(_columnIndexOfNextReviewAt)
          }
          _tmpNextReviewAt = __converters.toDate(_tmp_17)
          val _tmpAddedAt: Date
          val _tmp_18: Long?
          if (_stmt.isNull(_columnIndexOfAddedAt)) {
            _tmp_18 = null
          } else {
            _tmp_18 = _stmt.getLong(_columnIndexOfAddedAt)
          }
          val _tmp_19: Date? = __converters.toDate(_tmp_18)
          if (_tmp_19 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpAddedAt = _tmp_19
          }
          val _tmpUpdatedAt: Date
          val _tmp_20: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_20 = null
          } else {
            _tmp_20 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_21: Date? = __converters.toDate(_tmp_20)
          if (_tmp_21 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_21
          }
          val _tmpSyncState: SyncState
          val _tmp_22: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_22 = null
          } else {
            _tmp_22 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_23: SyncState? = __converters.toSyncState(_tmp_22)
          if (_tmp_23 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_23
          }
          val _tmpOriginUnsolvedId: String?
          if (_stmt.isNull(_columnIndexOfOriginUnsolvedId)) {
            _tmpOriginUnsolvedId = null
          } else {
            _tmpOriginUnsolvedId = _stmt.getText(_columnIndexOfOriginUnsolvedId)
          }
          _item =
              ErrorEntryEntity(_tmpId,_tmpTitle,_tmpQuestionText,_tmpSubject,_tmpChapterName,_tmpTopic,_tmpSource,_tmpSourceRefId,_tmpDppNumber,_tmpChapterNumber,_tmpQuestionNumber,_tmpPyqYear,_tmpExamType,_tmpShiftSession,_tmpInstituteName,_tmpModuleNumber,_tmpExercise,_tmpTestName,_tmpTestNumber,_tmpTestDate,_tmpMarks,_tmpBookName,_tmpCustomSourceName,_tmpDifficulty,_tmpMistakeType,_tmpAttemptedSolution,_tmpCorrectSolution,_tmpExplanation,_tmpLessonLearned,_tmpPersonalNotes,_tmpTags,_tmpStatus,_tmpFavorite,_tmpReviewCount,_tmpLastReviewedAt,_tmpNextReviewAt,_tmpAddedAt,_tmpUpdatedAt,_tmpSyncState,_tmpOriginUnsolvedId)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeBySubject(subject: Subject): Flow<List<ErrorEntryEntity>> {
    val _sql: String =
        "SELECT * FROM errors WHERE syncState != 'PENDING_DELETE' AND subject = ? ORDER BY addedAt DESC"
    return createFlow(__db, false, arrayOf("errors")) { _connection ->
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
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfQuestionText: Int = getColumnIndexOrThrow(_stmt, "questionText")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfChapterName: Int = getColumnIndexOrThrow(_stmt, "chapterName")
        val _columnIndexOfTopic: Int = getColumnIndexOrThrow(_stmt, "topic")
        val _columnIndexOfSource: Int = getColumnIndexOrThrow(_stmt, "source")
        val _columnIndexOfSourceRefId: Int = getColumnIndexOrThrow(_stmt, "sourceRefId")
        val _columnIndexOfDppNumber: Int = getColumnIndexOrThrow(_stmt, "dppNumber")
        val _columnIndexOfChapterNumber: Int = getColumnIndexOrThrow(_stmt, "chapterNumber")
        val _columnIndexOfQuestionNumber: Int = getColumnIndexOrThrow(_stmt, "questionNumber")
        val _columnIndexOfPyqYear: Int = getColumnIndexOrThrow(_stmt, "pyqYear")
        val _columnIndexOfExamType: Int = getColumnIndexOrThrow(_stmt, "examType")
        val _columnIndexOfShiftSession: Int = getColumnIndexOrThrow(_stmt, "shiftSession")
        val _columnIndexOfInstituteName: Int = getColumnIndexOrThrow(_stmt, "instituteName")
        val _columnIndexOfModuleNumber: Int = getColumnIndexOrThrow(_stmt, "moduleNumber")
        val _columnIndexOfExercise: Int = getColumnIndexOrThrow(_stmt, "exercise")
        val _columnIndexOfTestName: Int = getColumnIndexOrThrow(_stmt, "testName")
        val _columnIndexOfTestNumber: Int = getColumnIndexOrThrow(_stmt, "testNumber")
        val _columnIndexOfTestDate: Int = getColumnIndexOrThrow(_stmt, "testDate")
        val _columnIndexOfMarks: Int = getColumnIndexOrThrow(_stmt, "marks")
        val _columnIndexOfBookName: Int = getColumnIndexOrThrow(_stmt, "bookName")
        val _columnIndexOfCustomSourceName: Int = getColumnIndexOrThrow(_stmt, "customSourceName")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfMistakeType: Int = getColumnIndexOrThrow(_stmt, "mistakeType")
        val _columnIndexOfAttemptedSolution: Int = getColumnIndexOrThrow(_stmt, "attemptedSolution")
        val _columnIndexOfCorrectSolution: Int = getColumnIndexOrThrow(_stmt, "correctSolution")
        val _columnIndexOfExplanation: Int = getColumnIndexOrThrow(_stmt, "explanation")
        val _columnIndexOfLessonLearned: Int = getColumnIndexOrThrow(_stmt, "lessonLearned")
        val _columnIndexOfPersonalNotes: Int = getColumnIndexOrThrow(_stmt, "personalNotes")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfFavorite: Int = getColumnIndexOrThrow(_stmt, "favorite")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfNextReviewAt: Int = getColumnIndexOrThrow(_stmt, "nextReviewAt")
        val _columnIndexOfAddedAt: Int = getColumnIndexOrThrow(_stmt, "addedAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _columnIndexOfOriginUnsolvedId: Int = getColumnIndexOrThrow(_stmt, "originUnsolvedId")
        val _result: MutableList<ErrorEntryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ErrorEntryEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpQuestionText: String
          _tmpQuestionText = _stmt.getText(_columnIndexOfQuestionText)
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
          val _tmpSource: QuestionSource
          val _tmp_3: String?
          if (_stmt.isNull(_columnIndexOfSource)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getText(_columnIndexOfSource)
          }
          val _tmp_4: QuestionSource? = __converters.toQuestionSource(_tmp_3)
          if (_tmp_4 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.QuestionSource', but it was NULL.")
          } else {
            _tmpSource = _tmp_4
          }
          val _tmpSourceRefId: String?
          if (_stmt.isNull(_columnIndexOfSourceRefId)) {
            _tmpSourceRefId = null
          } else {
            _tmpSourceRefId = _stmt.getText(_columnIndexOfSourceRefId)
          }
          val _tmpDppNumber: String?
          if (_stmt.isNull(_columnIndexOfDppNumber)) {
            _tmpDppNumber = null
          } else {
            _tmpDppNumber = _stmt.getText(_columnIndexOfDppNumber)
          }
          val _tmpChapterNumber: String?
          if (_stmt.isNull(_columnIndexOfChapterNumber)) {
            _tmpChapterNumber = null
          } else {
            _tmpChapterNumber = _stmt.getText(_columnIndexOfChapterNumber)
          }
          val _tmpQuestionNumber: String?
          if (_stmt.isNull(_columnIndexOfQuestionNumber)) {
            _tmpQuestionNumber = null
          } else {
            _tmpQuestionNumber = _stmt.getText(_columnIndexOfQuestionNumber)
          }
          val _tmpPyqYear: Int?
          if (_stmt.isNull(_columnIndexOfPyqYear)) {
            _tmpPyqYear = null
          } else {
            _tmpPyqYear = _stmt.getLong(_columnIndexOfPyqYear).toInt()
          }
          val _tmpExamType: ExamType?
          val _tmp_5: String?
          if (_stmt.isNull(_columnIndexOfExamType)) {
            _tmp_5 = null
          } else {
            _tmp_5 = _stmt.getText(_columnIndexOfExamType)
          }
          _tmpExamType = __converters.toExamType(_tmp_5)
          val _tmpShiftSession: String?
          if (_stmt.isNull(_columnIndexOfShiftSession)) {
            _tmpShiftSession = null
          } else {
            _tmpShiftSession = _stmt.getText(_columnIndexOfShiftSession)
          }
          val _tmpInstituteName: String?
          if (_stmt.isNull(_columnIndexOfInstituteName)) {
            _tmpInstituteName = null
          } else {
            _tmpInstituteName = _stmt.getText(_columnIndexOfInstituteName)
          }
          val _tmpModuleNumber: String?
          if (_stmt.isNull(_columnIndexOfModuleNumber)) {
            _tmpModuleNumber = null
          } else {
            _tmpModuleNumber = _stmt.getText(_columnIndexOfModuleNumber)
          }
          val _tmpExercise: String?
          if (_stmt.isNull(_columnIndexOfExercise)) {
            _tmpExercise = null
          } else {
            _tmpExercise = _stmt.getText(_columnIndexOfExercise)
          }
          val _tmpTestName: String?
          if (_stmt.isNull(_columnIndexOfTestName)) {
            _tmpTestName = null
          } else {
            _tmpTestName = _stmt.getText(_columnIndexOfTestName)
          }
          val _tmpTestNumber: String?
          if (_stmt.isNull(_columnIndexOfTestNumber)) {
            _tmpTestNumber = null
          } else {
            _tmpTestNumber = _stmt.getText(_columnIndexOfTestNumber)
          }
          val _tmpTestDate: Date?
          val _tmp_6: Long?
          if (_stmt.isNull(_columnIndexOfTestDate)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getLong(_columnIndexOfTestDate)
          }
          _tmpTestDate = __converters.toDate(_tmp_6)
          val _tmpMarks: String?
          if (_stmt.isNull(_columnIndexOfMarks)) {
            _tmpMarks = null
          } else {
            _tmpMarks = _stmt.getText(_columnIndexOfMarks)
          }
          val _tmpBookName: String?
          if (_stmt.isNull(_columnIndexOfBookName)) {
            _tmpBookName = null
          } else {
            _tmpBookName = _stmt.getText(_columnIndexOfBookName)
          }
          val _tmpCustomSourceName: String?
          if (_stmt.isNull(_columnIndexOfCustomSourceName)) {
            _tmpCustomSourceName = null
          } else {
            _tmpCustomSourceName = _stmt.getText(_columnIndexOfCustomSourceName)
          }
          val _tmpDifficulty: Difficulty
          val _tmp_7: String?
          if (_stmt.isNull(_columnIndexOfDifficulty)) {
            _tmp_7 = null
          } else {
            _tmp_7 = _stmt.getText(_columnIndexOfDifficulty)
          }
          val _tmp_8: Difficulty? = __converters.toDifficulty(_tmp_7)
          if (_tmp_8 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.Difficulty', but it was NULL.")
          } else {
            _tmpDifficulty = _tmp_8
          }
          val _tmpMistakeType: MistakeType
          val _tmp_9: String?
          if (_stmt.isNull(_columnIndexOfMistakeType)) {
            _tmp_9 = null
          } else {
            _tmp_9 = _stmt.getText(_columnIndexOfMistakeType)
          }
          val _tmp_10: MistakeType? = __converters.toMistakeType(_tmp_9)
          if (_tmp_10 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.MistakeType', but it was NULL.")
          } else {
            _tmpMistakeType = _tmp_10
          }
          val _tmpAttemptedSolution: String?
          if (_stmt.isNull(_columnIndexOfAttemptedSolution)) {
            _tmpAttemptedSolution = null
          } else {
            _tmpAttemptedSolution = _stmt.getText(_columnIndexOfAttemptedSolution)
          }
          val _tmpCorrectSolution: String?
          if (_stmt.isNull(_columnIndexOfCorrectSolution)) {
            _tmpCorrectSolution = null
          } else {
            _tmpCorrectSolution = _stmt.getText(_columnIndexOfCorrectSolution)
          }
          val _tmpExplanation: String?
          if (_stmt.isNull(_columnIndexOfExplanation)) {
            _tmpExplanation = null
          } else {
            _tmpExplanation = _stmt.getText(_columnIndexOfExplanation)
          }
          val _tmpLessonLearned: String?
          if (_stmt.isNull(_columnIndexOfLessonLearned)) {
            _tmpLessonLearned = null
          } else {
            _tmpLessonLearned = _stmt.getText(_columnIndexOfLessonLearned)
          }
          val _tmpPersonalNotes: String?
          if (_stmt.isNull(_columnIndexOfPersonalNotes)) {
            _tmpPersonalNotes = null
          } else {
            _tmpPersonalNotes = _stmt.getText(_columnIndexOfPersonalNotes)
          }
          val _tmpTags: List<String>
          val _tmp_11: String?
          if (_stmt.isNull(_columnIndexOfTags)) {
            _tmp_11 = null
          } else {
            _tmp_11 = _stmt.getText(_columnIndexOfTags)
          }
          val _tmp_12: List<String>? = __converters.toStringList(_tmp_11)
          if (_tmp_12 == null) {
            error("Expected NON-NULL 'kotlin.collections.List<kotlin.String>', but it was NULL.")
          } else {
            _tmpTags = _tmp_12
          }
          val _tmpStatus: ErrorStatus
          val _tmp_13: String?
          if (_stmt.isNull(_columnIndexOfStatus)) {
            _tmp_13 = null
          } else {
            _tmp_13 = _stmt.getText(_columnIndexOfStatus)
          }
          val _tmp_14: ErrorStatus? = __converters.toErrorStatus(_tmp_13)
          if (_tmp_14 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.ErrorStatus', but it was NULL.")
          } else {
            _tmpStatus = _tmp_14
          }
          val _tmpFavorite: Boolean
          val _tmp_15: Int
          _tmp_15 = _stmt.getLong(_columnIndexOfFavorite).toInt()
          _tmpFavorite = _tmp_15 != 0
          val _tmpReviewCount: Int
          _tmpReviewCount = _stmt.getLong(_columnIndexOfReviewCount).toInt()
          val _tmpLastReviewedAt: Date?
          val _tmp_16: Long?
          if (_stmt.isNull(_columnIndexOfLastReviewedAt)) {
            _tmp_16 = null
          } else {
            _tmp_16 = _stmt.getLong(_columnIndexOfLastReviewedAt)
          }
          _tmpLastReviewedAt = __converters.toDate(_tmp_16)
          val _tmpNextReviewAt: Date?
          val _tmp_17: Long?
          if (_stmt.isNull(_columnIndexOfNextReviewAt)) {
            _tmp_17 = null
          } else {
            _tmp_17 = _stmt.getLong(_columnIndexOfNextReviewAt)
          }
          _tmpNextReviewAt = __converters.toDate(_tmp_17)
          val _tmpAddedAt: Date
          val _tmp_18: Long?
          if (_stmt.isNull(_columnIndexOfAddedAt)) {
            _tmp_18 = null
          } else {
            _tmp_18 = _stmt.getLong(_columnIndexOfAddedAt)
          }
          val _tmp_19: Date? = __converters.toDate(_tmp_18)
          if (_tmp_19 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpAddedAt = _tmp_19
          }
          val _tmpUpdatedAt: Date
          val _tmp_20: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_20 = null
          } else {
            _tmp_20 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_21: Date? = __converters.toDate(_tmp_20)
          if (_tmp_21 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_21
          }
          val _tmpSyncState: SyncState
          val _tmp_22: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_22 = null
          } else {
            _tmp_22 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_23: SyncState? = __converters.toSyncState(_tmp_22)
          if (_tmp_23 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_23
          }
          val _tmpOriginUnsolvedId: String?
          if (_stmt.isNull(_columnIndexOfOriginUnsolvedId)) {
            _tmpOriginUnsolvedId = null
          } else {
            _tmpOriginUnsolvedId = _stmt.getText(_columnIndexOfOriginUnsolvedId)
          }
          _item =
              ErrorEntryEntity(_tmpId,_tmpTitle,_tmpQuestionText,_tmpSubject,_tmpChapterName,_tmpTopic,_tmpSource,_tmpSourceRefId,_tmpDppNumber,_tmpChapterNumber,_tmpQuestionNumber,_tmpPyqYear,_tmpExamType,_tmpShiftSession,_tmpInstituteName,_tmpModuleNumber,_tmpExercise,_tmpTestName,_tmpTestNumber,_tmpTestDate,_tmpMarks,_tmpBookName,_tmpCustomSourceName,_tmpDifficulty,_tmpMistakeType,_tmpAttemptedSolution,_tmpCorrectSolution,_tmpExplanation,_tmpLessonLearned,_tmpPersonalNotes,_tmpTags,_tmpStatus,_tmpFavorite,_tmpReviewCount,_tmpLastReviewedAt,_tmpNextReviewAt,_tmpAddedAt,_tmpUpdatedAt,_tmpSyncState,_tmpOriginUnsolvedId)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeFavorites(): Flow<List<ErrorEntryEntity>> {
    val _sql: String =
        "SELECT * FROM errors WHERE syncState != 'PENDING_DELETE' AND favorite = 1 ORDER BY addedAt DESC"
    return createFlow(__db, false, arrayOf("errors")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfQuestionText: Int = getColumnIndexOrThrow(_stmt, "questionText")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfChapterName: Int = getColumnIndexOrThrow(_stmt, "chapterName")
        val _columnIndexOfTopic: Int = getColumnIndexOrThrow(_stmt, "topic")
        val _columnIndexOfSource: Int = getColumnIndexOrThrow(_stmt, "source")
        val _columnIndexOfSourceRefId: Int = getColumnIndexOrThrow(_stmt, "sourceRefId")
        val _columnIndexOfDppNumber: Int = getColumnIndexOrThrow(_stmt, "dppNumber")
        val _columnIndexOfChapterNumber: Int = getColumnIndexOrThrow(_stmt, "chapterNumber")
        val _columnIndexOfQuestionNumber: Int = getColumnIndexOrThrow(_stmt, "questionNumber")
        val _columnIndexOfPyqYear: Int = getColumnIndexOrThrow(_stmt, "pyqYear")
        val _columnIndexOfExamType: Int = getColumnIndexOrThrow(_stmt, "examType")
        val _columnIndexOfShiftSession: Int = getColumnIndexOrThrow(_stmt, "shiftSession")
        val _columnIndexOfInstituteName: Int = getColumnIndexOrThrow(_stmt, "instituteName")
        val _columnIndexOfModuleNumber: Int = getColumnIndexOrThrow(_stmt, "moduleNumber")
        val _columnIndexOfExercise: Int = getColumnIndexOrThrow(_stmt, "exercise")
        val _columnIndexOfTestName: Int = getColumnIndexOrThrow(_stmt, "testName")
        val _columnIndexOfTestNumber: Int = getColumnIndexOrThrow(_stmt, "testNumber")
        val _columnIndexOfTestDate: Int = getColumnIndexOrThrow(_stmt, "testDate")
        val _columnIndexOfMarks: Int = getColumnIndexOrThrow(_stmt, "marks")
        val _columnIndexOfBookName: Int = getColumnIndexOrThrow(_stmt, "bookName")
        val _columnIndexOfCustomSourceName: Int = getColumnIndexOrThrow(_stmt, "customSourceName")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfMistakeType: Int = getColumnIndexOrThrow(_stmt, "mistakeType")
        val _columnIndexOfAttemptedSolution: Int = getColumnIndexOrThrow(_stmt, "attemptedSolution")
        val _columnIndexOfCorrectSolution: Int = getColumnIndexOrThrow(_stmt, "correctSolution")
        val _columnIndexOfExplanation: Int = getColumnIndexOrThrow(_stmt, "explanation")
        val _columnIndexOfLessonLearned: Int = getColumnIndexOrThrow(_stmt, "lessonLearned")
        val _columnIndexOfPersonalNotes: Int = getColumnIndexOrThrow(_stmt, "personalNotes")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfFavorite: Int = getColumnIndexOrThrow(_stmt, "favorite")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfNextReviewAt: Int = getColumnIndexOrThrow(_stmt, "nextReviewAt")
        val _columnIndexOfAddedAt: Int = getColumnIndexOrThrow(_stmt, "addedAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _columnIndexOfOriginUnsolvedId: Int = getColumnIndexOrThrow(_stmt, "originUnsolvedId")
        val _result: MutableList<ErrorEntryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ErrorEntryEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpQuestionText: String
          _tmpQuestionText = _stmt.getText(_columnIndexOfQuestionText)
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
          val _tmpSource: QuestionSource
          val _tmp_2: String?
          if (_stmt.isNull(_columnIndexOfSource)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_columnIndexOfSource)
          }
          val _tmp_3: QuestionSource? = __converters.toQuestionSource(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.QuestionSource', but it was NULL.")
          } else {
            _tmpSource = _tmp_3
          }
          val _tmpSourceRefId: String?
          if (_stmt.isNull(_columnIndexOfSourceRefId)) {
            _tmpSourceRefId = null
          } else {
            _tmpSourceRefId = _stmt.getText(_columnIndexOfSourceRefId)
          }
          val _tmpDppNumber: String?
          if (_stmt.isNull(_columnIndexOfDppNumber)) {
            _tmpDppNumber = null
          } else {
            _tmpDppNumber = _stmt.getText(_columnIndexOfDppNumber)
          }
          val _tmpChapterNumber: String?
          if (_stmt.isNull(_columnIndexOfChapterNumber)) {
            _tmpChapterNumber = null
          } else {
            _tmpChapterNumber = _stmt.getText(_columnIndexOfChapterNumber)
          }
          val _tmpQuestionNumber: String?
          if (_stmt.isNull(_columnIndexOfQuestionNumber)) {
            _tmpQuestionNumber = null
          } else {
            _tmpQuestionNumber = _stmt.getText(_columnIndexOfQuestionNumber)
          }
          val _tmpPyqYear: Int?
          if (_stmt.isNull(_columnIndexOfPyqYear)) {
            _tmpPyqYear = null
          } else {
            _tmpPyqYear = _stmt.getLong(_columnIndexOfPyqYear).toInt()
          }
          val _tmpExamType: ExamType?
          val _tmp_4: String?
          if (_stmt.isNull(_columnIndexOfExamType)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getText(_columnIndexOfExamType)
          }
          _tmpExamType = __converters.toExamType(_tmp_4)
          val _tmpShiftSession: String?
          if (_stmt.isNull(_columnIndexOfShiftSession)) {
            _tmpShiftSession = null
          } else {
            _tmpShiftSession = _stmt.getText(_columnIndexOfShiftSession)
          }
          val _tmpInstituteName: String?
          if (_stmt.isNull(_columnIndexOfInstituteName)) {
            _tmpInstituteName = null
          } else {
            _tmpInstituteName = _stmt.getText(_columnIndexOfInstituteName)
          }
          val _tmpModuleNumber: String?
          if (_stmt.isNull(_columnIndexOfModuleNumber)) {
            _tmpModuleNumber = null
          } else {
            _tmpModuleNumber = _stmt.getText(_columnIndexOfModuleNumber)
          }
          val _tmpExercise: String?
          if (_stmt.isNull(_columnIndexOfExercise)) {
            _tmpExercise = null
          } else {
            _tmpExercise = _stmt.getText(_columnIndexOfExercise)
          }
          val _tmpTestName: String?
          if (_stmt.isNull(_columnIndexOfTestName)) {
            _tmpTestName = null
          } else {
            _tmpTestName = _stmt.getText(_columnIndexOfTestName)
          }
          val _tmpTestNumber: String?
          if (_stmt.isNull(_columnIndexOfTestNumber)) {
            _tmpTestNumber = null
          } else {
            _tmpTestNumber = _stmt.getText(_columnIndexOfTestNumber)
          }
          val _tmpTestDate: Date?
          val _tmp_5: Long?
          if (_stmt.isNull(_columnIndexOfTestDate)) {
            _tmp_5 = null
          } else {
            _tmp_5 = _stmt.getLong(_columnIndexOfTestDate)
          }
          _tmpTestDate = __converters.toDate(_tmp_5)
          val _tmpMarks: String?
          if (_stmt.isNull(_columnIndexOfMarks)) {
            _tmpMarks = null
          } else {
            _tmpMarks = _stmt.getText(_columnIndexOfMarks)
          }
          val _tmpBookName: String?
          if (_stmt.isNull(_columnIndexOfBookName)) {
            _tmpBookName = null
          } else {
            _tmpBookName = _stmt.getText(_columnIndexOfBookName)
          }
          val _tmpCustomSourceName: String?
          if (_stmt.isNull(_columnIndexOfCustomSourceName)) {
            _tmpCustomSourceName = null
          } else {
            _tmpCustomSourceName = _stmt.getText(_columnIndexOfCustomSourceName)
          }
          val _tmpDifficulty: Difficulty
          val _tmp_6: String?
          if (_stmt.isNull(_columnIndexOfDifficulty)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getText(_columnIndexOfDifficulty)
          }
          val _tmp_7: Difficulty? = __converters.toDifficulty(_tmp_6)
          if (_tmp_7 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.Difficulty', but it was NULL.")
          } else {
            _tmpDifficulty = _tmp_7
          }
          val _tmpMistakeType: MistakeType
          val _tmp_8: String?
          if (_stmt.isNull(_columnIndexOfMistakeType)) {
            _tmp_8 = null
          } else {
            _tmp_8 = _stmt.getText(_columnIndexOfMistakeType)
          }
          val _tmp_9: MistakeType? = __converters.toMistakeType(_tmp_8)
          if (_tmp_9 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.MistakeType', but it was NULL.")
          } else {
            _tmpMistakeType = _tmp_9
          }
          val _tmpAttemptedSolution: String?
          if (_stmt.isNull(_columnIndexOfAttemptedSolution)) {
            _tmpAttemptedSolution = null
          } else {
            _tmpAttemptedSolution = _stmt.getText(_columnIndexOfAttemptedSolution)
          }
          val _tmpCorrectSolution: String?
          if (_stmt.isNull(_columnIndexOfCorrectSolution)) {
            _tmpCorrectSolution = null
          } else {
            _tmpCorrectSolution = _stmt.getText(_columnIndexOfCorrectSolution)
          }
          val _tmpExplanation: String?
          if (_stmt.isNull(_columnIndexOfExplanation)) {
            _tmpExplanation = null
          } else {
            _tmpExplanation = _stmt.getText(_columnIndexOfExplanation)
          }
          val _tmpLessonLearned: String?
          if (_stmt.isNull(_columnIndexOfLessonLearned)) {
            _tmpLessonLearned = null
          } else {
            _tmpLessonLearned = _stmt.getText(_columnIndexOfLessonLearned)
          }
          val _tmpPersonalNotes: String?
          if (_stmt.isNull(_columnIndexOfPersonalNotes)) {
            _tmpPersonalNotes = null
          } else {
            _tmpPersonalNotes = _stmt.getText(_columnIndexOfPersonalNotes)
          }
          val _tmpTags: List<String>
          val _tmp_10: String?
          if (_stmt.isNull(_columnIndexOfTags)) {
            _tmp_10 = null
          } else {
            _tmp_10 = _stmt.getText(_columnIndexOfTags)
          }
          val _tmp_11: List<String>? = __converters.toStringList(_tmp_10)
          if (_tmp_11 == null) {
            error("Expected NON-NULL 'kotlin.collections.List<kotlin.String>', but it was NULL.")
          } else {
            _tmpTags = _tmp_11
          }
          val _tmpStatus: ErrorStatus
          val _tmp_12: String?
          if (_stmt.isNull(_columnIndexOfStatus)) {
            _tmp_12 = null
          } else {
            _tmp_12 = _stmt.getText(_columnIndexOfStatus)
          }
          val _tmp_13: ErrorStatus? = __converters.toErrorStatus(_tmp_12)
          if (_tmp_13 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.ErrorStatus', but it was NULL.")
          } else {
            _tmpStatus = _tmp_13
          }
          val _tmpFavorite: Boolean
          val _tmp_14: Int
          _tmp_14 = _stmt.getLong(_columnIndexOfFavorite).toInt()
          _tmpFavorite = _tmp_14 != 0
          val _tmpReviewCount: Int
          _tmpReviewCount = _stmt.getLong(_columnIndexOfReviewCount).toInt()
          val _tmpLastReviewedAt: Date?
          val _tmp_15: Long?
          if (_stmt.isNull(_columnIndexOfLastReviewedAt)) {
            _tmp_15 = null
          } else {
            _tmp_15 = _stmt.getLong(_columnIndexOfLastReviewedAt)
          }
          _tmpLastReviewedAt = __converters.toDate(_tmp_15)
          val _tmpNextReviewAt: Date?
          val _tmp_16: Long?
          if (_stmt.isNull(_columnIndexOfNextReviewAt)) {
            _tmp_16 = null
          } else {
            _tmp_16 = _stmt.getLong(_columnIndexOfNextReviewAt)
          }
          _tmpNextReviewAt = __converters.toDate(_tmp_16)
          val _tmpAddedAt: Date
          val _tmp_17: Long?
          if (_stmt.isNull(_columnIndexOfAddedAt)) {
            _tmp_17 = null
          } else {
            _tmp_17 = _stmt.getLong(_columnIndexOfAddedAt)
          }
          val _tmp_18: Date? = __converters.toDate(_tmp_17)
          if (_tmp_18 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpAddedAt = _tmp_18
          }
          val _tmpUpdatedAt: Date
          val _tmp_19: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_19 = null
          } else {
            _tmp_19 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_20: Date? = __converters.toDate(_tmp_19)
          if (_tmp_20 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_20
          }
          val _tmpSyncState: SyncState
          val _tmp_21: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_21 = null
          } else {
            _tmp_21 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_22: SyncState? = __converters.toSyncState(_tmp_21)
          if (_tmp_22 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_22
          }
          val _tmpOriginUnsolvedId: String?
          if (_stmt.isNull(_columnIndexOfOriginUnsolvedId)) {
            _tmpOriginUnsolvedId = null
          } else {
            _tmpOriginUnsolvedId = _stmt.getText(_columnIndexOfOriginUnsolvedId)
          }
          _item =
              ErrorEntryEntity(_tmpId,_tmpTitle,_tmpQuestionText,_tmpSubject,_tmpChapterName,_tmpTopic,_tmpSource,_tmpSourceRefId,_tmpDppNumber,_tmpChapterNumber,_tmpQuestionNumber,_tmpPyqYear,_tmpExamType,_tmpShiftSession,_tmpInstituteName,_tmpModuleNumber,_tmpExercise,_tmpTestName,_tmpTestNumber,_tmpTestDate,_tmpMarks,_tmpBookName,_tmpCustomSourceName,_tmpDifficulty,_tmpMistakeType,_tmpAttemptedSolution,_tmpCorrectSolution,_tmpExplanation,_tmpLessonLearned,_tmpPersonalNotes,_tmpTags,_tmpStatus,_tmpFavorite,_tmpReviewCount,_tmpLastReviewedAt,_tmpNextReviewAt,_tmpAddedAt,_tmpUpdatedAt,_tmpSyncState,_tmpOriginUnsolvedId)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeCount(): Flow<Int> {
    val _sql: String = "SELECT COUNT(*) FROM errors WHERE syncState != 'PENDING_DELETE'"
    return createFlow(__db, false, arrayOf("errors")) { _connection ->
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

  public override fun observeCountBySubject(subject: Subject): Flow<Int> {
    val _sql: String =
        "SELECT COUNT(*) FROM errors WHERE syncState != 'PENDING_DELETE' AND subject = ?"
    return createFlow(__db, false, arrayOf("errors")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: String? = __converters.fromSubject(subject)
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

  public override fun observeCountByMistake(mistake: String): Flow<Int> {
    val _sql: String =
        "SELECT COUNT(*) FROM errors WHERE syncState != 'PENDING_DELETE' AND mistakeType = ?"
    return createFlow(__db, false, arrayOf("errors")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, mistake)
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

  public override fun observeCountByStatus(status: ErrorStatus): Flow<Int> {
    val _sql: String =
        "SELECT COUNT(*) FROM errors WHERE syncState != 'PENDING_DELETE' AND status = ?"
    return createFlow(__db, false, arrayOf("errors")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: String? = __converters.fromErrorStatus(status)
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

  public override suspend fun search(q: String, limit: Int): List<ErrorEntryEntity> {
    val _sql: String =
        "SELECT * FROM errors WHERE syncState != 'PENDING_DELETE' AND title LIKE '%' || ? || '%' OR questionText LIKE '%' || ? || '%' OR chapterName LIKE '%' || ? || '%' OR topic LIKE '%' || ? || '%' ORDER BY addedAt DESC LIMIT ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, q)
        _argIndex = 2
        _stmt.bindText(_argIndex, q)
        _argIndex = 3
        _stmt.bindText(_argIndex, q)
        _argIndex = 4
        _stmt.bindText(_argIndex, q)
        _argIndex = 5
        _stmt.bindLong(_argIndex, limit.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfQuestionText: Int = getColumnIndexOrThrow(_stmt, "questionText")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfChapterName: Int = getColumnIndexOrThrow(_stmt, "chapterName")
        val _columnIndexOfTopic: Int = getColumnIndexOrThrow(_stmt, "topic")
        val _columnIndexOfSource: Int = getColumnIndexOrThrow(_stmt, "source")
        val _columnIndexOfSourceRefId: Int = getColumnIndexOrThrow(_stmt, "sourceRefId")
        val _columnIndexOfDppNumber: Int = getColumnIndexOrThrow(_stmt, "dppNumber")
        val _columnIndexOfChapterNumber: Int = getColumnIndexOrThrow(_stmt, "chapterNumber")
        val _columnIndexOfQuestionNumber: Int = getColumnIndexOrThrow(_stmt, "questionNumber")
        val _columnIndexOfPyqYear: Int = getColumnIndexOrThrow(_stmt, "pyqYear")
        val _columnIndexOfExamType: Int = getColumnIndexOrThrow(_stmt, "examType")
        val _columnIndexOfShiftSession: Int = getColumnIndexOrThrow(_stmt, "shiftSession")
        val _columnIndexOfInstituteName: Int = getColumnIndexOrThrow(_stmt, "instituteName")
        val _columnIndexOfModuleNumber: Int = getColumnIndexOrThrow(_stmt, "moduleNumber")
        val _columnIndexOfExercise: Int = getColumnIndexOrThrow(_stmt, "exercise")
        val _columnIndexOfTestName: Int = getColumnIndexOrThrow(_stmt, "testName")
        val _columnIndexOfTestNumber: Int = getColumnIndexOrThrow(_stmt, "testNumber")
        val _columnIndexOfTestDate: Int = getColumnIndexOrThrow(_stmt, "testDate")
        val _columnIndexOfMarks: Int = getColumnIndexOrThrow(_stmt, "marks")
        val _columnIndexOfBookName: Int = getColumnIndexOrThrow(_stmt, "bookName")
        val _columnIndexOfCustomSourceName: Int = getColumnIndexOrThrow(_stmt, "customSourceName")
        val _columnIndexOfDifficulty: Int = getColumnIndexOrThrow(_stmt, "difficulty")
        val _columnIndexOfMistakeType: Int = getColumnIndexOrThrow(_stmt, "mistakeType")
        val _columnIndexOfAttemptedSolution: Int = getColumnIndexOrThrow(_stmt, "attemptedSolution")
        val _columnIndexOfCorrectSolution: Int = getColumnIndexOrThrow(_stmt, "correctSolution")
        val _columnIndexOfExplanation: Int = getColumnIndexOrThrow(_stmt, "explanation")
        val _columnIndexOfLessonLearned: Int = getColumnIndexOrThrow(_stmt, "lessonLearned")
        val _columnIndexOfPersonalNotes: Int = getColumnIndexOrThrow(_stmt, "personalNotes")
        val _columnIndexOfTags: Int = getColumnIndexOrThrow(_stmt, "tags")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfFavorite: Int = getColumnIndexOrThrow(_stmt, "favorite")
        val _columnIndexOfReviewCount: Int = getColumnIndexOrThrow(_stmt, "reviewCount")
        val _columnIndexOfLastReviewedAt: Int = getColumnIndexOrThrow(_stmt, "lastReviewedAt")
        val _columnIndexOfNextReviewAt: Int = getColumnIndexOrThrow(_stmt, "nextReviewAt")
        val _columnIndexOfAddedAt: Int = getColumnIndexOrThrow(_stmt, "addedAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _columnIndexOfOriginUnsolvedId: Int = getColumnIndexOrThrow(_stmt, "originUnsolvedId")
        val _result: MutableList<ErrorEntryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ErrorEntryEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpQuestionText: String
          _tmpQuestionText = _stmt.getText(_columnIndexOfQuestionText)
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
          val _tmpSource: QuestionSource
          val _tmp_2: String?
          if (_stmt.isNull(_columnIndexOfSource)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getText(_columnIndexOfSource)
          }
          val _tmp_3: QuestionSource? = __converters.toQuestionSource(_tmp_2)
          if (_tmp_3 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.QuestionSource', but it was NULL.")
          } else {
            _tmpSource = _tmp_3
          }
          val _tmpSourceRefId: String?
          if (_stmt.isNull(_columnIndexOfSourceRefId)) {
            _tmpSourceRefId = null
          } else {
            _tmpSourceRefId = _stmt.getText(_columnIndexOfSourceRefId)
          }
          val _tmpDppNumber: String?
          if (_stmt.isNull(_columnIndexOfDppNumber)) {
            _tmpDppNumber = null
          } else {
            _tmpDppNumber = _stmt.getText(_columnIndexOfDppNumber)
          }
          val _tmpChapterNumber: String?
          if (_stmt.isNull(_columnIndexOfChapterNumber)) {
            _tmpChapterNumber = null
          } else {
            _tmpChapterNumber = _stmt.getText(_columnIndexOfChapterNumber)
          }
          val _tmpQuestionNumber: String?
          if (_stmt.isNull(_columnIndexOfQuestionNumber)) {
            _tmpQuestionNumber = null
          } else {
            _tmpQuestionNumber = _stmt.getText(_columnIndexOfQuestionNumber)
          }
          val _tmpPyqYear: Int?
          if (_stmt.isNull(_columnIndexOfPyqYear)) {
            _tmpPyqYear = null
          } else {
            _tmpPyqYear = _stmt.getLong(_columnIndexOfPyqYear).toInt()
          }
          val _tmpExamType: ExamType?
          val _tmp_4: String?
          if (_stmt.isNull(_columnIndexOfExamType)) {
            _tmp_4 = null
          } else {
            _tmp_4 = _stmt.getText(_columnIndexOfExamType)
          }
          _tmpExamType = __converters.toExamType(_tmp_4)
          val _tmpShiftSession: String?
          if (_stmt.isNull(_columnIndexOfShiftSession)) {
            _tmpShiftSession = null
          } else {
            _tmpShiftSession = _stmt.getText(_columnIndexOfShiftSession)
          }
          val _tmpInstituteName: String?
          if (_stmt.isNull(_columnIndexOfInstituteName)) {
            _tmpInstituteName = null
          } else {
            _tmpInstituteName = _stmt.getText(_columnIndexOfInstituteName)
          }
          val _tmpModuleNumber: String?
          if (_stmt.isNull(_columnIndexOfModuleNumber)) {
            _tmpModuleNumber = null
          } else {
            _tmpModuleNumber = _stmt.getText(_columnIndexOfModuleNumber)
          }
          val _tmpExercise: String?
          if (_stmt.isNull(_columnIndexOfExercise)) {
            _tmpExercise = null
          } else {
            _tmpExercise = _stmt.getText(_columnIndexOfExercise)
          }
          val _tmpTestName: String?
          if (_stmt.isNull(_columnIndexOfTestName)) {
            _tmpTestName = null
          } else {
            _tmpTestName = _stmt.getText(_columnIndexOfTestName)
          }
          val _tmpTestNumber: String?
          if (_stmt.isNull(_columnIndexOfTestNumber)) {
            _tmpTestNumber = null
          } else {
            _tmpTestNumber = _stmt.getText(_columnIndexOfTestNumber)
          }
          val _tmpTestDate: Date?
          val _tmp_5: Long?
          if (_stmt.isNull(_columnIndexOfTestDate)) {
            _tmp_5 = null
          } else {
            _tmp_5 = _stmt.getLong(_columnIndexOfTestDate)
          }
          _tmpTestDate = __converters.toDate(_tmp_5)
          val _tmpMarks: String?
          if (_stmt.isNull(_columnIndexOfMarks)) {
            _tmpMarks = null
          } else {
            _tmpMarks = _stmt.getText(_columnIndexOfMarks)
          }
          val _tmpBookName: String?
          if (_stmt.isNull(_columnIndexOfBookName)) {
            _tmpBookName = null
          } else {
            _tmpBookName = _stmt.getText(_columnIndexOfBookName)
          }
          val _tmpCustomSourceName: String?
          if (_stmt.isNull(_columnIndexOfCustomSourceName)) {
            _tmpCustomSourceName = null
          } else {
            _tmpCustomSourceName = _stmt.getText(_columnIndexOfCustomSourceName)
          }
          val _tmpDifficulty: Difficulty
          val _tmp_6: String?
          if (_stmt.isNull(_columnIndexOfDifficulty)) {
            _tmp_6 = null
          } else {
            _tmp_6 = _stmt.getText(_columnIndexOfDifficulty)
          }
          val _tmp_7: Difficulty? = __converters.toDifficulty(_tmp_6)
          if (_tmp_7 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.Difficulty', but it was NULL.")
          } else {
            _tmpDifficulty = _tmp_7
          }
          val _tmpMistakeType: MistakeType
          val _tmp_8: String?
          if (_stmt.isNull(_columnIndexOfMistakeType)) {
            _tmp_8 = null
          } else {
            _tmp_8 = _stmt.getText(_columnIndexOfMistakeType)
          }
          val _tmp_9: MistakeType? = __converters.toMistakeType(_tmp_8)
          if (_tmp_9 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.MistakeType', but it was NULL.")
          } else {
            _tmpMistakeType = _tmp_9
          }
          val _tmpAttemptedSolution: String?
          if (_stmt.isNull(_columnIndexOfAttemptedSolution)) {
            _tmpAttemptedSolution = null
          } else {
            _tmpAttemptedSolution = _stmt.getText(_columnIndexOfAttemptedSolution)
          }
          val _tmpCorrectSolution: String?
          if (_stmt.isNull(_columnIndexOfCorrectSolution)) {
            _tmpCorrectSolution = null
          } else {
            _tmpCorrectSolution = _stmt.getText(_columnIndexOfCorrectSolution)
          }
          val _tmpExplanation: String?
          if (_stmt.isNull(_columnIndexOfExplanation)) {
            _tmpExplanation = null
          } else {
            _tmpExplanation = _stmt.getText(_columnIndexOfExplanation)
          }
          val _tmpLessonLearned: String?
          if (_stmt.isNull(_columnIndexOfLessonLearned)) {
            _tmpLessonLearned = null
          } else {
            _tmpLessonLearned = _stmt.getText(_columnIndexOfLessonLearned)
          }
          val _tmpPersonalNotes: String?
          if (_stmt.isNull(_columnIndexOfPersonalNotes)) {
            _tmpPersonalNotes = null
          } else {
            _tmpPersonalNotes = _stmt.getText(_columnIndexOfPersonalNotes)
          }
          val _tmpTags: List<String>
          val _tmp_10: String?
          if (_stmt.isNull(_columnIndexOfTags)) {
            _tmp_10 = null
          } else {
            _tmp_10 = _stmt.getText(_columnIndexOfTags)
          }
          val _tmp_11: List<String>? = __converters.toStringList(_tmp_10)
          if (_tmp_11 == null) {
            error("Expected NON-NULL 'kotlin.collections.List<kotlin.String>', but it was NULL.")
          } else {
            _tmpTags = _tmp_11
          }
          val _tmpStatus: ErrorStatus
          val _tmp_12: String?
          if (_stmt.isNull(_columnIndexOfStatus)) {
            _tmp_12 = null
          } else {
            _tmp_12 = _stmt.getText(_columnIndexOfStatus)
          }
          val _tmp_13: ErrorStatus? = __converters.toErrorStatus(_tmp_12)
          if (_tmp_13 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.ErrorStatus', but it was NULL.")
          } else {
            _tmpStatus = _tmp_13
          }
          val _tmpFavorite: Boolean
          val _tmp_14: Int
          _tmp_14 = _stmt.getLong(_columnIndexOfFavorite).toInt()
          _tmpFavorite = _tmp_14 != 0
          val _tmpReviewCount: Int
          _tmpReviewCount = _stmt.getLong(_columnIndexOfReviewCount).toInt()
          val _tmpLastReviewedAt: Date?
          val _tmp_15: Long?
          if (_stmt.isNull(_columnIndexOfLastReviewedAt)) {
            _tmp_15 = null
          } else {
            _tmp_15 = _stmt.getLong(_columnIndexOfLastReviewedAt)
          }
          _tmpLastReviewedAt = __converters.toDate(_tmp_15)
          val _tmpNextReviewAt: Date?
          val _tmp_16: Long?
          if (_stmt.isNull(_columnIndexOfNextReviewAt)) {
            _tmp_16 = null
          } else {
            _tmp_16 = _stmt.getLong(_columnIndexOfNextReviewAt)
          }
          _tmpNextReviewAt = __converters.toDate(_tmp_16)
          val _tmpAddedAt: Date
          val _tmp_17: Long?
          if (_stmt.isNull(_columnIndexOfAddedAt)) {
            _tmp_17 = null
          } else {
            _tmp_17 = _stmt.getLong(_columnIndexOfAddedAt)
          }
          val _tmp_18: Date? = __converters.toDate(_tmp_17)
          if (_tmp_18 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpAddedAt = _tmp_18
          }
          val _tmpUpdatedAt: Date
          val _tmp_19: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_19 = null
          } else {
            _tmp_19 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_20: Date? = __converters.toDate(_tmp_19)
          if (_tmp_20 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_20
          }
          val _tmpSyncState: SyncState
          val _tmp_21: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_21 = null
          } else {
            _tmp_21 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_22: SyncState? = __converters.toSyncState(_tmp_21)
          if (_tmp_22 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_22
          }
          val _tmpOriginUnsolvedId: String?
          if (_stmt.isNull(_columnIndexOfOriginUnsolvedId)) {
            _tmpOriginUnsolvedId = null
          } else {
            _tmpOriginUnsolvedId = _stmt.getText(_columnIndexOfOriginUnsolvedId)
          }
          _item =
              ErrorEntryEntity(_tmpId,_tmpTitle,_tmpQuestionText,_tmpSubject,_tmpChapterName,_tmpTopic,_tmpSource,_tmpSourceRefId,_tmpDppNumber,_tmpChapterNumber,_tmpQuestionNumber,_tmpPyqYear,_tmpExamType,_tmpShiftSession,_tmpInstituteName,_tmpModuleNumber,_tmpExercise,_tmpTestName,_tmpTestNumber,_tmpTestDate,_tmpMarks,_tmpBookName,_tmpCustomSourceName,_tmpDifficulty,_tmpMistakeType,_tmpAttemptedSolution,_tmpCorrectSolution,_tmpExplanation,_tmpLessonLearned,_tmpPersonalNotes,_tmpTags,_tmpStatus,_tmpFavorite,_tmpReviewCount,_tmpLastReviewedAt,_tmpNextReviewAt,_tmpAddedAt,_tmpUpdatedAt,_tmpSyncState,_tmpOriginUnsolvedId)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun markSync(id: String, state: SyncState) {
    val _sql: String = "UPDATE errors SET syncState = ? WHERE id = ?"
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

  public override suspend fun setFavorite(
    id: String,
    fav: Boolean,
    now: Long,
    pending: SyncState,
  ) {
    val _sql: String = "UPDATE errors SET favorite = ?, updatedAt = ?, syncState = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: Int = if (fav) 1 else 0
        _stmt.bindLong(_argIndex, _tmp.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, now)
        _argIndex = 3
        val _tmp_1: String? = __converters.fromSyncState(pending)
        if (_tmp_1 == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, _tmp_1)
        }
        _argIndex = 4
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun setStatus(
    id: String,
    status: ErrorStatus,
    now: Long,
    pending: SyncState,
  ) {
    val _sql: String = "UPDATE errors SET status = ?, updatedAt = ?, syncState = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: String? = __converters.fromErrorStatus(status)
        if (_tmp == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, _tmp)
        }
        _argIndex = 2
        _stmt.bindLong(_argIndex, now)
        _argIndex = 3
        val _tmp_1: String? = __converters.fromSyncState(pending)
        if (_tmp_1 == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, _tmp_1)
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
    val _sql: String = "DELETE FROM errors WHERE id = ?"
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
    val _sql: String = "DELETE FROM errors WHERE id = ?"
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
    val _sql: String = "DELETE FROM errors"
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
