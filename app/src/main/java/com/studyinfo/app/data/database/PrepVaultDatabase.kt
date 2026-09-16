package com.studyinfo.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.studyinfo.app.data.database.converter.Converters
import com.studyinfo.app.data.database.dao.*
import com.studyinfo.app.data.database.entity.*

/**
 * Local source of truth for PrepVault.
 *
 * Designed to comfortably hold 10,000+ questions, 5,000+ tasks, and tens of thousands
 * of image references. The UI always reads from here; the sync layer pushes / pulls
 * changes to/from Firebase in the background.
 */
@Database(
    entities = [
        LocalAccountEntity::class,
        UserProfileEntity::class,
        TagEntity::class,
        CustomSourceEntity::class,
        ChapterEntity::class,
        TopicEntity::class,
        QuestionImageEntity::class,
        ErrorEntryEntity::class,
        UnsolvedQuestionEntity::class,
        TaskEntity::class,
        ReviewEntity::class,
        ProgressEntity::class,
        StreakActivityEntity::class,
        SyncQueueEntity::class,
    ],
    version = 3,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class PrepVaultDatabase : RoomDatabase() {
    abstract fun localAccountDao(): LocalAccountDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun tagDao(): TagDao
    abstract fun customSourceDao(): CustomSourceDao
    abstract fun chapterDao(): ChapterDao
    abstract fun topicDao(): TopicDao
    abstract fun questionImageDao(): QuestionImageDao
    abstract fun errorDao(): ErrorDao
    abstract fun unsolvedDao(): UnsolvedDao
    abstract fun taskDao(): TaskDao
    abstract fun reviewDao(): ReviewDao
    abstract fun progressDao(): ProgressDao
    abstract fun streakDao(): StreakDao
    abstract fun syncQueueDao(): SyncQueueDao

    companion object {
        const val NAME = "prepvault.db"

        /**
         * v2 -> v3 (v1.2.0): local_accounts gained `provider` and `photoUrl` so the same
         * account record can also be reached through "Continue with Google".
         * Existing rows keep their data — they simply default to the PASSWORD provider.
         */
        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE local_accounts ADD COLUMN provider TEXT NOT NULL DEFAULT 'PASSWORD'",
                )
                db.execSQL("ALTER TABLE local_accounts ADD COLUMN photoUrl TEXT")
            }
        }
    }
}
