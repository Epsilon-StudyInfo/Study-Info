package com.studyinfo.app.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.studyinfo.app.data.database.entity.LocalAccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalAccountDao {
    @Query("SELECT * FROM local_accounts WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): LocalAccountEntity?

    @Query("SELECT * FROM local_accounts WHERE id = :id LIMIT 1")
    suspend fun findById(id: String): LocalAccountEntity?

    @Query("SELECT * FROM local_accounts ORDER BY createdAt ASC")
    fun observeAll(): Flow<List<LocalAccountEntity>>

    @Query("SELECT COUNT(*) FROM local_accounts")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(account: LocalAccountEntity)

    @Query("UPDATE local_accounts SET name = :name WHERE id = :id")
    suspend fun rename(id: String, name: String)

    @Query("UPDATE local_accounts SET lastLoginAt = :at WHERE id = :id")
    suspend fun touchLogin(id: String, at: Long)

    @Query("UPDATE local_accounts SET passwordHash = :hash WHERE id = :id")
    suspend fun updatePassword(id: String, hash: String)

    @Query("DELETE FROM local_accounts WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM local_accounts")
    suspend fun clear()
}
