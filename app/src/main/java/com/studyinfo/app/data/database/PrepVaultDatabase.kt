package com.studyinfo.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class PrepVaultDatabase : RoomDatabase() {
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
    }
}
