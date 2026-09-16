package com.studyinfo.app.`data`.database.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.studyinfo.app.`data`.database.converter.Converters
import com.studyinfo.app.`data`.database.entity.UserProfileEntity
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
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class UserProfileDao_Impl(
  __db: RoomDatabase,
) : UserProfileDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfUserProfileEntity: EntityInsertAdapter<UserProfileEntity>

  private val __converters: Converters = Converters()
  init {
    this.__db = __db
    this.__insertAdapterOfUserProfileEntity = object : EntityInsertAdapter<UserProfileEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `user_profile` (`uid`,`displayName`,`email`,`photoUrl`,`emailVerified`,`targetExam`,`preferredLanguage`,`createdAt`,`lastLoginAt`,`updatedAt`,`syncState`) VALUES (?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: UserProfileEntity) {
        statement.bindText(1, entity.uid)
        val _tmpDisplayName: String? = entity.displayName
        if (_tmpDisplayName == null) {
          statement.bindNull(2)
        } else {
          statement.bindText(2, _tmpDisplayName)
        }
        val _tmpEmail: String? = entity.email
        if (_tmpEmail == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpEmail)
        }
        val _tmpPhotoUrl: String? = entity.photoUrl
        if (_tmpPhotoUrl == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpPhotoUrl)
        }
        val _tmp: Int = if (entity.emailVerified) 1 else 0
        statement.bindLong(5, _tmp.toLong())
        statement.bindText(6, entity.targetExam)
        statement.bindText(7, entity.preferredLanguage)
        val _tmp_1: Long? = __converters.fromDate(entity.createdAt)
        if (_tmp_1 == null) {
          statement.bindNull(8)
        } else {
          statement.bindLong(8, _tmp_1)
        }
        val _tmp_2: Long? = __converters.fromDate(entity.lastLoginAt)
        if (_tmp_2 == null) {
          statement.bindNull(9)
        } else {
          statement.bindLong(9, _tmp_2)
        }
        val _tmp_3: Long? = __converters.fromDate(entity.updatedAt)
        if (_tmp_3 == null) {
          statement.bindNull(10)
        } else {
          statement.bindLong(10, _tmp_3)
        }
        val _tmp_4: String? = __converters.fromSyncState(entity.syncState)
        if (_tmp_4 == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmp_4)
        }
      }
    }
  }

  public override suspend fun upsert(entity: UserProfileEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfUserProfileEntity.insert(_connection, entity)
  }

  public override fun observe(): Flow<UserProfileEntity?> {
    val _sql: String = "SELECT * FROM user_profile LIMIT 1"
    return createFlow(__db, false, arrayOf("user_profile")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfUid: Int = getColumnIndexOrThrow(_stmt, "uid")
        val _columnIndexOfDisplayName: Int = getColumnIndexOrThrow(_stmt, "displayName")
        val _columnIndexOfEmail: Int = getColumnIndexOrThrow(_stmt, "email")
        val _columnIndexOfPhotoUrl: Int = getColumnIndexOrThrow(_stmt, "photoUrl")
        val _columnIndexOfEmailVerified: Int = getColumnIndexOrThrow(_stmt, "emailVerified")
        val _columnIndexOfTargetExam: Int = getColumnIndexOrThrow(_stmt, "targetExam")
        val _columnIndexOfPreferredLanguage: Int = getColumnIndexOrThrow(_stmt, "preferredLanguage")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfLastLoginAt: Int = getColumnIndexOrThrow(_stmt, "lastLoginAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: UserProfileEntity?
        if (_stmt.step()) {
          val _tmpUid: String
          _tmpUid = _stmt.getText(_columnIndexOfUid)
          val _tmpDisplayName: String?
          if (_stmt.isNull(_columnIndexOfDisplayName)) {
            _tmpDisplayName = null
          } else {
            _tmpDisplayName = _stmt.getText(_columnIndexOfDisplayName)
          }
          val _tmpEmail: String?
          if (_stmt.isNull(_columnIndexOfEmail)) {
            _tmpEmail = null
          } else {
            _tmpEmail = _stmt.getText(_columnIndexOfEmail)
          }
          val _tmpPhotoUrl: String?
          if (_stmt.isNull(_columnIndexOfPhotoUrl)) {
            _tmpPhotoUrl = null
          } else {
            _tmpPhotoUrl = _stmt.getText(_columnIndexOfPhotoUrl)
          }
          val _tmpEmailVerified: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfEmailVerified).toInt()
          _tmpEmailVerified = _tmp != 0
          val _tmpTargetExam: String
          _tmpTargetExam = _stmt.getText(_columnIndexOfTargetExam)
          val _tmpPreferredLanguage: String
          _tmpPreferredLanguage = _stmt.getText(_columnIndexOfPreferredLanguage)
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
          val _tmpLastLoginAt: Date
          val _tmp_3: Long?
          if (_stmt.isNull(_columnIndexOfLastLoginAt)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getLong(_columnIndexOfLastLoginAt)
          }
          val _tmp_4: Date? = __converters.toDate(_tmp_3)
          if (_tmp_4 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpLastLoginAt = _tmp_4
          }
          val _tmpUpdatedAt: Date
          val _tmp_5: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_5 = null
          } else {
            _tmp_5 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_6: Date? = __converters.toDate(_tmp_5)
          if (_tmp_6 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_6
          }
          val _tmpSyncState: SyncState
          val _tmp_7: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_7 = null
          } else {
            _tmp_7 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_8: SyncState? = __converters.toSyncState(_tmp_7)
          if (_tmp_8 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_8
          }
          _result =
              UserProfileEntity(_tmpUid,_tmpDisplayName,_tmpEmail,_tmpPhotoUrl,_tmpEmailVerified,_tmpTargetExam,_tmpPreferredLanguage,_tmpCreatedAt,_tmpLastLoginAt,_tmpUpdatedAt,_tmpSyncState)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getById(uid: String): UserProfileEntity? {
    val _sql: String = "SELECT * FROM user_profile WHERE uid = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, uid)
        val _columnIndexOfUid: Int = getColumnIndexOrThrow(_stmt, "uid")
        val _columnIndexOfDisplayName: Int = getColumnIndexOrThrow(_stmt, "displayName")
        val _columnIndexOfEmail: Int = getColumnIndexOrThrow(_stmt, "email")
        val _columnIndexOfPhotoUrl: Int = getColumnIndexOrThrow(_stmt, "photoUrl")
        val _columnIndexOfEmailVerified: Int = getColumnIndexOrThrow(_stmt, "emailVerified")
        val _columnIndexOfTargetExam: Int = getColumnIndexOrThrow(_stmt, "targetExam")
        val _columnIndexOfPreferredLanguage: Int = getColumnIndexOrThrow(_stmt, "preferredLanguage")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfLastLoginAt: Int = getColumnIndexOrThrow(_stmt, "lastLoginAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSyncState: Int = getColumnIndexOrThrow(_stmt, "syncState")
        val _result: UserProfileEntity?
        if (_stmt.step()) {
          val _tmpUid: String
          _tmpUid = _stmt.getText(_columnIndexOfUid)
          val _tmpDisplayName: String?
          if (_stmt.isNull(_columnIndexOfDisplayName)) {
            _tmpDisplayName = null
          } else {
            _tmpDisplayName = _stmt.getText(_columnIndexOfDisplayName)
          }
          val _tmpEmail: String?
          if (_stmt.isNull(_columnIndexOfEmail)) {
            _tmpEmail = null
          } else {
            _tmpEmail = _stmt.getText(_columnIndexOfEmail)
          }
          val _tmpPhotoUrl: String?
          if (_stmt.isNull(_columnIndexOfPhotoUrl)) {
            _tmpPhotoUrl = null
          } else {
            _tmpPhotoUrl = _stmt.getText(_columnIndexOfPhotoUrl)
          }
          val _tmpEmailVerified: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfEmailVerified).toInt()
          _tmpEmailVerified = _tmp != 0
          val _tmpTargetExam: String
          _tmpTargetExam = _stmt.getText(_columnIndexOfTargetExam)
          val _tmpPreferredLanguage: String
          _tmpPreferredLanguage = _stmt.getText(_columnIndexOfPreferredLanguage)
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
          val _tmpLastLoginAt: Date
          val _tmp_3: Long?
          if (_stmt.isNull(_columnIndexOfLastLoginAt)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getLong(_columnIndexOfLastLoginAt)
          }
          val _tmp_4: Date? = __converters.toDate(_tmp_3)
          if (_tmp_4 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpLastLoginAt = _tmp_4
          }
          val _tmpUpdatedAt: Date
          val _tmp_5: Long?
          if (_stmt.isNull(_columnIndexOfUpdatedAt)) {
            _tmp_5 = null
          } else {
            _tmp_5 = _stmt.getLong(_columnIndexOfUpdatedAt)
          }
          val _tmp_6: Date? = __converters.toDate(_tmp_5)
          if (_tmp_6 == null) {
            error("Expected NON-NULL 'java.util.Date', but it was NULL.")
          } else {
            _tmpUpdatedAt = _tmp_6
          }
          val _tmpSyncState: SyncState
          val _tmp_7: String?
          if (_stmt.isNull(_columnIndexOfSyncState)) {
            _tmp_7 = null
          } else {
            _tmp_7 = _stmt.getText(_columnIndexOfSyncState)
          }
          val _tmp_8: SyncState? = __converters.toSyncState(_tmp_7)
          if (_tmp_8 == null) {
            error("Expected NON-NULL 'com.studyinfo.app.domain.model.SyncState', but it was NULL.")
          } else {
            _tmpSyncState = _tmp_8
          }
          _result =
              UserProfileEntity(_tmpUid,_tmpDisplayName,_tmpEmail,_tmpPhotoUrl,_tmpEmailVerified,_tmpTargetExam,_tmpPreferredLanguage,_tmpCreatedAt,_tmpLastLoginAt,_tmpUpdatedAt,_tmpSyncState)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clear() {
    val _sql: String = "DELETE FROM user_profile"
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
