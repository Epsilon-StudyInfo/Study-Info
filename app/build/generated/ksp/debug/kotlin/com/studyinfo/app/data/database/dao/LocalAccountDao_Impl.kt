package com.studyinfo.app.`data`.database.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.studyinfo.app.`data`.database.converter.Converters
import com.studyinfo.app.`data`.database.entity.LocalAccountEntity
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
public class LocalAccountDao_Impl(
  __db: RoomDatabase,
) : LocalAccountDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfLocalAccountEntity: EntityInsertAdapter<LocalAccountEntity>

  private val __converters: Converters = Converters()
  init {
    this.__db = __db
    this.__insertAdapterOfLocalAccountEntity = object : EntityInsertAdapter<LocalAccountEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `local_accounts` (`id`,`name`,`email`,`passwordHash`,`createdAt`,`lastLoginAt`) VALUES (?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: LocalAccountEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.email)
        statement.bindText(4, entity.passwordHash)
        val _tmp: Long? = __converters.fromDate(entity.createdAt)
        if (_tmp == null) {
          statement.bindNull(5)
        } else {
          statement.bindLong(5, _tmp)
        }
        val _tmpLastLoginAt: Date? = entity.lastLoginAt
        val _tmp_1: Long? = __converters.fromDate(_tmpLastLoginAt)
        if (_tmp_1 == null) {
          statement.bindNull(6)
        } else {
          statement.bindLong(6, _tmp_1)
        }
      }
    }
  }

  public override suspend fun insert(account: LocalAccountEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfLocalAccountEntity.insert(_connection, account)
  }

  public override suspend fun findByEmail(email: String): LocalAccountEntity? {
    val _sql: String = "SELECT * FROM local_accounts WHERE email = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, email)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfEmail: Int = getColumnIndexOrThrow(_stmt, "email")
        val _columnIndexOfPasswordHash: Int = getColumnIndexOrThrow(_stmt, "passwordHash")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfLastLoginAt: Int = getColumnIndexOrThrow(_stmt, "lastLoginAt")
        val _result: LocalAccountEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpEmail: String
          _tmpEmail = _stmt.getText(_columnIndexOfEmail)
          val _tmpPasswordHash: String
          _tmpPasswordHash = _stmt.getText(_columnIndexOfPasswordHash)
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
          val _tmpLastLoginAt: Date?
          val _tmp_2: Long?
          if (_stmt.isNull(_columnIndexOfLastLoginAt)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getLong(_columnIndexOfLastLoginAt)
          }
          _tmpLastLoginAt = __converters.toDate(_tmp_2)
          _result =
              LocalAccountEntity(_tmpId,_tmpName,_tmpEmail,_tmpPasswordHash,_tmpCreatedAt,_tmpLastLoginAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun findById(id: String): LocalAccountEntity? {
    val _sql: String = "SELECT * FROM local_accounts WHERE id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfEmail: Int = getColumnIndexOrThrow(_stmt, "email")
        val _columnIndexOfPasswordHash: Int = getColumnIndexOrThrow(_stmt, "passwordHash")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfLastLoginAt: Int = getColumnIndexOrThrow(_stmt, "lastLoginAt")
        val _result: LocalAccountEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpEmail: String
          _tmpEmail = _stmt.getText(_columnIndexOfEmail)
          val _tmpPasswordHash: String
          _tmpPasswordHash = _stmt.getText(_columnIndexOfPasswordHash)
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
          val _tmpLastLoginAt: Date?
          val _tmp_2: Long?
          if (_stmt.isNull(_columnIndexOfLastLoginAt)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getLong(_columnIndexOfLastLoginAt)
          }
          _tmpLastLoginAt = __converters.toDate(_tmp_2)
          _result =
              LocalAccountEntity(_tmpId,_tmpName,_tmpEmail,_tmpPasswordHash,_tmpCreatedAt,_tmpLastLoginAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeAll(): Flow<List<LocalAccountEntity>> {
    val _sql: String = "SELECT * FROM local_accounts ORDER BY createdAt ASC"
    return createFlow(__db, false, arrayOf("local_accounts")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfEmail: Int = getColumnIndexOrThrow(_stmt, "email")
        val _columnIndexOfPasswordHash: Int = getColumnIndexOrThrow(_stmt, "passwordHash")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfLastLoginAt: Int = getColumnIndexOrThrow(_stmt, "lastLoginAt")
        val _result: MutableList<LocalAccountEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: LocalAccountEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpEmail: String
          _tmpEmail = _stmt.getText(_columnIndexOfEmail)
          val _tmpPasswordHash: String
          _tmpPasswordHash = _stmt.getText(_columnIndexOfPasswordHash)
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
          val _tmpLastLoginAt: Date?
          val _tmp_2: Long?
          if (_stmt.isNull(_columnIndexOfLastLoginAt)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getLong(_columnIndexOfLastLoginAt)
          }
          _tmpLastLoginAt = __converters.toDate(_tmp_2)
          _item =
              LocalAccountEntity(_tmpId,_tmpName,_tmpEmail,_tmpPasswordHash,_tmpCreatedAt,_tmpLastLoginAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun count(): Int {
    val _sql: String = "SELECT COUNT(*) FROM local_accounts"
    return performSuspending(__db, true, false) { _connection ->
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

  public override suspend fun rename(id: String, name: String) {
    val _sql: String = "UPDATE local_accounts SET name = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, name)
        _argIndex = 2
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun touchLogin(id: String, at: Long) {
    val _sql: String = "UPDATE local_accounts SET lastLoginAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, at)
        _argIndex = 2
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updatePassword(id: String, hash: String) {
    val _sql: String = "UPDATE local_accounts SET passwordHash = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, hash)
        _argIndex = 2
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun delete(id: String) {
    val _sql: String = "DELETE FROM local_accounts WHERE id = ?"
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
    val _sql: String = "DELETE FROM local_accounts"
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
