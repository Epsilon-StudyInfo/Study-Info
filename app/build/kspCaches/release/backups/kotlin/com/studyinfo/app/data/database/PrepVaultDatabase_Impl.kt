package com.studyinfo.app.`data`.database

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.studyinfo.app.`data`.database.dao.ChapterDao
import com.studyinfo.app.`data`.database.dao.ChapterDao_Impl
import com.studyinfo.app.`data`.database.dao.CustomSourceDao
import com.studyinfo.app.`data`.database.dao.CustomSourceDao_Impl
import com.studyinfo.app.`data`.database.dao.ErrorDao
import com.studyinfo.app.`data`.database.dao.ErrorDao_Impl
import com.studyinfo.app.`data`.database.dao.LocalAccountDao
import com.studyinfo.app.`data`.database.dao.LocalAccountDao_Impl
import com.studyinfo.app.`data`.database.dao.ProgressDao
import com.studyinfo.app.`data`.database.dao.ProgressDao_Impl
import com.studyinfo.app.`data`.database.dao.QuestionImageDao
import com.studyinfo.app.`data`.database.dao.QuestionImageDao_Impl
import com.studyinfo.app.`data`.database.dao.ReviewDao
import com.studyinfo.app.`data`.database.dao.ReviewDao_Impl
import com.studyinfo.app.`data`.database.dao.StreakDao
import com.studyinfo.app.`data`.database.dao.StreakDao_Impl
import com.studyinfo.app.`data`.database.dao.SyncQueueDao
import com.studyinfo.app.`data`.database.dao.SyncQueueDao_Impl
import com.studyinfo.app.`data`.database.dao.TagDao
import com.studyinfo.app.`data`.database.dao.TagDao_Impl
import com.studyinfo.app.`data`.database.dao.TaskDao
import com.studyinfo.app.`data`.database.dao.TaskDao_Impl
import com.studyinfo.app.`data`.database.dao.TopicDao
import com.studyinfo.app.`data`.database.dao.TopicDao_Impl
import com.studyinfo.app.`data`.database.dao.UnsolvedDao
import com.studyinfo.app.`data`.database.dao.UnsolvedDao_Impl
import com.studyinfo.app.`data`.database.dao.UserProfileDao
import com.studyinfo.app.`data`.database.dao.UserProfileDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class PrepVaultDatabase_Impl : PrepVaultDatabase() {
  private val _localAccountDao: Lazy<LocalAccountDao> = lazy {
    LocalAccountDao_Impl(this)
  }

  private val _userProfileDao: Lazy<UserProfileDao> = lazy {
    UserProfileDao_Impl(this)
  }

  private val _tagDao: Lazy<TagDao> = lazy {
    TagDao_Impl(this)
  }

  private val _customSourceDao: Lazy<CustomSourceDao> = lazy {
    CustomSourceDao_Impl(this)
  }

  private val _chapterDao: Lazy<ChapterDao> = lazy {
    ChapterDao_Impl(this)
  }

  private val _topicDao: Lazy<TopicDao> = lazy {
    TopicDao_Impl(this)
  }

  private val _questionImageDao: Lazy<QuestionImageDao> = lazy {
    QuestionImageDao_Impl(this)
  }

  private val _errorDao: Lazy<ErrorDao> = lazy {
    ErrorDao_Impl(this)
  }

  private val _unsolvedDao: Lazy<UnsolvedDao> = lazy {
    UnsolvedDao_Impl(this)
  }

  private val _taskDao: Lazy<TaskDao> = lazy {
    TaskDao_Impl(this)
  }

  private val _reviewDao: Lazy<ReviewDao> = lazy {
    ReviewDao_Impl(this)
  }

  private val _progressDao: Lazy<ProgressDao> = lazy {
    ProgressDao_Impl(this)
  }

  private val _streakDao: Lazy<StreakDao> = lazy {
    StreakDao_Impl(this)
  }

  private val _syncQueueDao: Lazy<SyncQueueDao> = lazy {
    SyncQueueDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(2,
        "421ed4783f597082f20a5c3001092136", "2fbe5092ef0442f589552ba0d3b9242a") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `local_accounts` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `email` TEXT NOT NULL, `passwordHash` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `lastLoginAt` INTEGER, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_local_accounts_email` ON `local_accounts` (`email`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `user_profile` (`uid` TEXT NOT NULL, `displayName` TEXT, `email` TEXT, `photoUrl` TEXT, `emailVerified` INTEGER NOT NULL, `targetExam` TEXT NOT NULL, `preferredLanguage` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `lastLoginAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `syncState` TEXT NOT NULL, PRIMARY KEY(`uid`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `tags` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `color` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `syncState` TEXT NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `custom_sources` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `description` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `syncState` TEXT NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `chapters` (`id` TEXT NOT NULL, `subject` TEXT NOT NULL, `name` TEXT NOT NULL, `examType` TEXT, `chapterNumber` INTEGER NOT NULL, `displayOrder` INTEGER NOT NULL, `isCustom` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `syncState` TEXT NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `topics` (`id` TEXT NOT NULL, `chapterId` TEXT NOT NULL, `name` TEXT NOT NULL, `displayOrder` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `syncState` TEXT NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `question_images` (`id` TEXT NOT NULL, `questionRefType` TEXT NOT NULL, `questionRefId` TEXT NOT NULL, `localUri` TEXT, `remoteUrl` TEXT, `storagePath` TEXT, `uploadedAt` INTEGER, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `syncState` TEXT NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `errors` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `questionText` TEXT NOT NULL, `subject` TEXT NOT NULL, `chapterName` TEXT, `topic` TEXT, `source` TEXT NOT NULL, `sourceRefId` TEXT, `dppNumber` TEXT, `chapterNumber` TEXT, `questionNumber` TEXT, `pyqYear` INTEGER, `examType` TEXT, `shiftSession` TEXT, `instituteName` TEXT, `moduleNumber` TEXT, `exercise` TEXT, `testName` TEXT, `testNumber` TEXT, `testDate` INTEGER, `marks` TEXT, `bookName` TEXT, `customSourceName` TEXT, `difficulty` TEXT NOT NULL, `mistakeType` TEXT NOT NULL, `attemptedSolution` TEXT, `correctSolution` TEXT, `explanation` TEXT, `lessonLearned` TEXT, `personalNotes` TEXT, `tags` TEXT NOT NULL, `status` TEXT NOT NULL, `favorite` INTEGER NOT NULL, `reviewCount` INTEGER NOT NULL, `lastReviewedAt` INTEGER, `nextReviewAt` INTEGER, `addedAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `syncState` TEXT NOT NULL, `originUnsolvedId` TEXT, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_errors_subject` ON `errors` (`subject`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_errors_source` ON `errors` (`source`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_errors_status` ON `errors` (`status`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_errors_syncState` ON `errors` (`syncState`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_errors_mistakeType` ON `errors` (`mistakeType`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_errors_addedAt` ON `errors` (`addedAt`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_errors_favorite` ON `errors` (`favorite`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `unsolved` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `questionText` TEXT NOT NULL, `subject` TEXT NOT NULL, `chapterName` TEXT, `topic` TEXT, `source` TEXT NOT NULL, `sourceRefId` TEXT, `dppNumber` TEXT, `chapterNumber` TEXT, `questionNumber` TEXT, `pyqYear` INTEGER, `examType` TEXT, `shiftSession` TEXT, `instituteName` TEXT, `moduleNumber` TEXT, `exercise` TEXT, `testName` TEXT, `testNumber` TEXT, `testDate` INTEGER, `marks` TEXT, `bookName` TEXT, `customSourceName` TEXT, `difficulty` TEXT NOT NULL, `reasonNotSolved` TEXT, `personalNotes` TEXT, `tags` TEXT NOT NULL, `status` TEXT NOT NULL, `favorite` INTEGER NOT NULL, `retryCount` INTEGER NOT NULL, `lastRetriedAt` INTEGER, `nextRetryAt` INTEGER, `addedAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `syncState` TEXT NOT NULL, `movedToErrorId` TEXT, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_unsolved_subject` ON `unsolved` (`subject`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_unsolved_source` ON `unsolved` (`source`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_unsolved_status` ON `unsolved` (`status`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_unsolved_syncState` ON `unsolved` (`syncState`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_unsolved_addedAt` ON `unsolved` (`addedAt`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_unsolved_favorite` ON `unsolved` (`favorite`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_unsolved_nextRetryAt` ON `unsolved` (`nextRetryAt`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `tasks` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `description` TEXT, `subject` TEXT, `chapterName` TEXT, `topic` TEXT, `dueDate` INTEGER NOT NULL, `dueTime` INTEGER, `estimatedMinutes` INTEGER, `priority` TEXT NOT NULL, `status` TEXT NOT NULL, `recurrence` TEXT, `notes` TEXT, `completedAt` INTEGER, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `syncState` TEXT NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_tasks_dueDate` ON `tasks` (`dueDate`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_tasks_subject` ON `tasks` (`subject`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_tasks_status` ON `tasks` (`status`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_tasks_priority` ON `tasks` (`priority`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_tasks_syncState` ON `tasks` (`syncState`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_tasks_completedAt` ON `tasks` (`completedAt`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `reviews` (`id` TEXT NOT NULL, `subject` TEXT, `startedAt` INTEGER NOT NULL, `endedAt` INTEGER, `reviewedCount` INTEGER NOT NULL, `understoodCount` INTEGER NOT NULL, `stillConfusedCount` INTEGER NOT NULL, `needsRevisionCount` INTEGER NOT NULL, `notes` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `syncState` TEXT NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_reviews_syncState` ON `reviews` (`syncState`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_reviews_startedAt` ON `reviews` (`startedAt`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_reviews_subject` ON `reviews` (`subject`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `progress` (`id` TEXT NOT NULL, `chapterId` TEXT NOT NULL, `subject` TEXT NOT NULL, `examType` TEXT NOT NULL, `chapterState` TEXT NOT NULL, `percent` INTEGER NOT NULL, `questionsAttempted` INTEGER NOT NULL, `questionsCorrect` INTEGER NOT NULL, `notes` TEXT, `updatedAt` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `syncState` TEXT NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_progress_chapterId` ON `progress` (`chapterId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_progress_examType` ON `progress` (`examType`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_progress_syncState` ON `progress` (`syncState`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `streak_activity` (`dateKey` TEXT NOT NULL, `activityCount` INTEGER NOT NULL, `lastActivityAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `syncState` TEXT NOT NULL, PRIMARY KEY(`dateKey`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `sync_queue` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `refType` TEXT NOT NULL, `refId` TEXT NOT NULL, `imageId` TEXT, `payload` TEXT, `attempts` INTEGER NOT NULL, `lastError` TEXT, `createdAt` INTEGER NOT NULL, `nextAttemptAt` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '421ed4783f597082f20a5c3001092136')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `local_accounts`")
        connection.execSQL("DROP TABLE IF EXISTS `user_profile`")
        connection.execSQL("DROP TABLE IF EXISTS `tags`")
        connection.execSQL("DROP TABLE IF EXISTS `custom_sources`")
        connection.execSQL("DROP TABLE IF EXISTS `chapters`")
        connection.execSQL("DROP TABLE IF EXISTS `topics`")
        connection.execSQL("DROP TABLE IF EXISTS `question_images`")
        connection.execSQL("DROP TABLE IF EXISTS `errors`")
        connection.execSQL("DROP TABLE IF EXISTS `unsolved`")
        connection.execSQL("DROP TABLE IF EXISTS `tasks`")
        connection.execSQL("DROP TABLE IF EXISTS `reviews`")
        connection.execSQL("DROP TABLE IF EXISTS `progress`")
        connection.execSQL("DROP TABLE IF EXISTS `streak_activity`")
        connection.execSQL("DROP TABLE IF EXISTS `sync_queue`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsLocalAccounts: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsLocalAccounts.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsLocalAccounts.put("name", TableInfo.Column("name", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsLocalAccounts.put("email", TableInfo.Column("email", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsLocalAccounts.put("passwordHash", TableInfo.Column("passwordHash", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsLocalAccounts.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsLocalAccounts.put("lastLoginAt", TableInfo.Column("lastLoginAt", "INTEGER", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysLocalAccounts: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesLocalAccounts: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesLocalAccounts.add(TableInfo.Index("index_local_accounts_email", true,
            listOf("email"), listOf("ASC")))
        val _infoLocalAccounts: TableInfo = TableInfo("local_accounts", _columnsLocalAccounts,
            _foreignKeysLocalAccounts, _indicesLocalAccounts)
        val _existingLocalAccounts: TableInfo = read(connection, "local_accounts")
        if (!_infoLocalAccounts.equals(_existingLocalAccounts)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |local_accounts(com.studyinfo.app.data.database.entity.LocalAccountEntity).
              | Expected:
              |""".trimMargin() + _infoLocalAccounts + """
              |
              | Found:
              |""".trimMargin() + _existingLocalAccounts)
        }
        val _columnsUserProfile: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsUserProfile.put("uid", TableInfo.Column("uid", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("displayName", TableInfo.Column("displayName", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("email", TableInfo.Column("email", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("photoUrl", TableInfo.Column("photoUrl", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("emailVerified", TableInfo.Column("emailVerified", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("targetExam", TableInfo.Column("targetExam", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("preferredLanguage", TableInfo.Column("preferredLanguage", "TEXT",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("lastLoginAt", TableInfo.Column("lastLoginAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserProfile.put("syncState", TableInfo.Column("syncState", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysUserProfile: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesUserProfile: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoUserProfile: TableInfo = TableInfo("user_profile", _columnsUserProfile,
            _foreignKeysUserProfile, _indicesUserProfile)
        val _existingUserProfile: TableInfo = read(connection, "user_profile")
        if (!_infoUserProfile.equals(_existingUserProfile)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |user_profile(com.studyinfo.app.data.database.entity.UserProfileEntity).
              | Expected:
              |""".trimMargin() + _infoUserProfile + """
              |
              | Found:
              |""".trimMargin() + _existingUserProfile)
        }
        val _columnsTags: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTags.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTags.put("name", TableInfo.Column("name", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTags.put("color", TableInfo.Column("color", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTags.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTags.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTags.put("syncState", TableInfo.Column("syncState", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTags: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesTags: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoTags: TableInfo = TableInfo("tags", _columnsTags, _foreignKeysTags, _indicesTags)
        val _existingTags: TableInfo = read(connection, "tags")
        if (!_infoTags.equals(_existingTags)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |tags(com.studyinfo.app.data.database.entity.TagEntity).
              | Expected:
              |""".trimMargin() + _infoTags + """
              |
              | Found:
              |""".trimMargin() + _existingTags)
        }
        val _columnsCustomSources: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsCustomSources.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomSources.put("name", TableInfo.Column("name", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomSources.put("description", TableInfo.Column("description", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomSources.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomSources.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsCustomSources.put("syncState", TableInfo.Column("syncState", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysCustomSources: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesCustomSources: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoCustomSources: TableInfo = TableInfo("custom_sources", _columnsCustomSources,
            _foreignKeysCustomSources, _indicesCustomSources)
        val _existingCustomSources: TableInfo = read(connection, "custom_sources")
        if (!_infoCustomSources.equals(_existingCustomSources)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |custom_sources(com.studyinfo.app.data.database.entity.CustomSourceEntity).
              | Expected:
              |""".trimMargin() + _infoCustomSources + """
              |
              | Found:
              |""".trimMargin() + _existingCustomSources)
        }
        val _columnsChapters: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsChapters.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsChapters.put("subject", TableInfo.Column("subject", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsChapters.put("name", TableInfo.Column("name", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsChapters.put("examType", TableInfo.Column("examType", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsChapters.put("chapterNumber", TableInfo.Column("chapterNumber", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsChapters.put("displayOrder", TableInfo.Column("displayOrder", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsChapters.put("isCustom", TableInfo.Column("isCustom", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsChapters.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsChapters.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsChapters.put("syncState", TableInfo.Column("syncState", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysChapters: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesChapters: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoChapters: TableInfo = TableInfo("chapters", _columnsChapters, _foreignKeysChapters,
            _indicesChapters)
        val _existingChapters: TableInfo = read(connection, "chapters")
        if (!_infoChapters.equals(_existingChapters)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |chapters(com.studyinfo.app.data.database.entity.ChapterEntity).
              | Expected:
              |""".trimMargin() + _infoChapters + """
              |
              | Found:
              |""".trimMargin() + _existingChapters)
        }
        val _columnsTopics: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTopics.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTopics.put("chapterId", TableInfo.Column("chapterId", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTopics.put("name", TableInfo.Column("name", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTopics.put("displayOrder", TableInfo.Column("displayOrder", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTopics.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTopics.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTopics.put("syncState", TableInfo.Column("syncState", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTopics: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesTopics: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoTopics: TableInfo = TableInfo("topics", _columnsTopics, _foreignKeysTopics,
            _indicesTopics)
        val _existingTopics: TableInfo = read(connection, "topics")
        if (!_infoTopics.equals(_existingTopics)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |topics(com.studyinfo.app.data.database.entity.TopicEntity).
              | Expected:
              |""".trimMargin() + _infoTopics + """
              |
              | Found:
              |""".trimMargin() + _existingTopics)
        }
        val _columnsQuestionImages: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsQuestionImages.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionImages.put("questionRefType", TableInfo.Column("questionRefType", "TEXT",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionImages.put("questionRefId", TableInfo.Column("questionRefId", "TEXT", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionImages.put("localUri", TableInfo.Column("localUri", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionImages.put("remoteUrl", TableInfo.Column("remoteUrl", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionImages.put("storagePath", TableInfo.Column("storagePath", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionImages.put("uploadedAt", TableInfo.Column("uploadedAt", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionImages.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionImages.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQuestionImages.put("syncState", TableInfo.Column("syncState", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysQuestionImages: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesQuestionImages: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoQuestionImages: TableInfo = TableInfo("question_images", _columnsQuestionImages,
            _foreignKeysQuestionImages, _indicesQuestionImages)
        val _existingQuestionImages: TableInfo = read(connection, "question_images")
        if (!_infoQuestionImages.equals(_existingQuestionImages)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |question_images(com.studyinfo.app.data.database.entity.QuestionImageEntity).
              | Expected:
              |""".trimMargin() + _infoQuestionImages + """
              |
              | Found:
              |""".trimMargin() + _existingQuestionImages)
        }
        val _columnsErrors: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsErrors.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("questionText", TableInfo.Column("questionText", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("subject", TableInfo.Column("subject", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("chapterName", TableInfo.Column("chapterName", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("topic", TableInfo.Column("topic", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("source", TableInfo.Column("source", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("sourceRefId", TableInfo.Column("sourceRefId", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("dppNumber", TableInfo.Column("dppNumber", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("chapterNumber", TableInfo.Column("chapterNumber", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("questionNumber", TableInfo.Column("questionNumber", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("pyqYear", TableInfo.Column("pyqYear", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("examType", TableInfo.Column("examType", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("shiftSession", TableInfo.Column("shiftSession", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("instituteName", TableInfo.Column("instituteName", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("moduleNumber", TableInfo.Column("moduleNumber", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("exercise", TableInfo.Column("exercise", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("testName", TableInfo.Column("testName", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("testNumber", TableInfo.Column("testNumber", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("testDate", TableInfo.Column("testDate", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("marks", TableInfo.Column("marks", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("bookName", TableInfo.Column("bookName", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("customSourceName", TableInfo.Column("customSourceName", "TEXT", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("difficulty", TableInfo.Column("difficulty", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("mistakeType", TableInfo.Column("mistakeType", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("attemptedSolution", TableInfo.Column("attemptedSolution", "TEXT", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("correctSolution", TableInfo.Column("correctSolution", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("explanation", TableInfo.Column("explanation", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("lessonLearned", TableInfo.Column("lessonLearned", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("personalNotes", TableInfo.Column("personalNotes", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("tags", TableInfo.Column("tags", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("status", TableInfo.Column("status", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("favorite", TableInfo.Column("favorite", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("reviewCount", TableInfo.Column("reviewCount", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("lastReviewedAt", TableInfo.Column("lastReviewedAt", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("nextReviewAt", TableInfo.Column("nextReviewAt", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("addedAt", TableInfo.Column("addedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("syncState", TableInfo.Column("syncState", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsErrors.put("originUnsolvedId", TableInfo.Column("originUnsolvedId", "TEXT", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysErrors: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesErrors: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesErrors.add(TableInfo.Index("index_errors_subject", false, listOf("subject"),
            listOf("ASC")))
        _indicesErrors.add(TableInfo.Index("index_errors_source", false, listOf("source"),
            listOf("ASC")))
        _indicesErrors.add(TableInfo.Index("index_errors_status", false, listOf("status"),
            listOf("ASC")))
        _indicesErrors.add(TableInfo.Index("index_errors_syncState", false, listOf("syncState"),
            listOf("ASC")))
        _indicesErrors.add(TableInfo.Index("index_errors_mistakeType", false, listOf("mistakeType"),
            listOf("ASC")))
        _indicesErrors.add(TableInfo.Index("index_errors_addedAt", false, listOf("addedAt"),
            listOf("ASC")))
        _indicesErrors.add(TableInfo.Index("index_errors_favorite", false, listOf("favorite"),
            listOf("ASC")))
        val _infoErrors: TableInfo = TableInfo("errors", _columnsErrors, _foreignKeysErrors,
            _indicesErrors)
        val _existingErrors: TableInfo = read(connection, "errors")
        if (!_infoErrors.equals(_existingErrors)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |errors(com.studyinfo.app.data.database.entity.ErrorEntryEntity).
              | Expected:
              |""".trimMargin() + _infoErrors + """
              |
              | Found:
              |""".trimMargin() + _existingErrors)
        }
        val _columnsUnsolved: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsUnsolved.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("questionText", TableInfo.Column("questionText", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("subject", TableInfo.Column("subject", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("chapterName", TableInfo.Column("chapterName", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("topic", TableInfo.Column("topic", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("source", TableInfo.Column("source", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("sourceRefId", TableInfo.Column("sourceRefId", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("dppNumber", TableInfo.Column("dppNumber", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("chapterNumber", TableInfo.Column("chapterNumber", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("questionNumber", TableInfo.Column("questionNumber", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("pyqYear", TableInfo.Column("pyqYear", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("examType", TableInfo.Column("examType", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("shiftSession", TableInfo.Column("shiftSession", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("instituteName", TableInfo.Column("instituteName", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("moduleNumber", TableInfo.Column("moduleNumber", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("exercise", TableInfo.Column("exercise", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("testName", TableInfo.Column("testName", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("testNumber", TableInfo.Column("testNumber", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("testDate", TableInfo.Column("testDate", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("marks", TableInfo.Column("marks", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("bookName", TableInfo.Column("bookName", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("customSourceName", TableInfo.Column("customSourceName", "TEXT", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("difficulty", TableInfo.Column("difficulty", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("reasonNotSolved", TableInfo.Column("reasonNotSolved", "TEXT", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("personalNotes", TableInfo.Column("personalNotes", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("tags", TableInfo.Column("tags", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("status", TableInfo.Column("status", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("favorite", TableInfo.Column("favorite", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("retryCount", TableInfo.Column("retryCount", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("lastRetriedAt", TableInfo.Column("lastRetriedAt", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("nextRetryAt", TableInfo.Column("nextRetryAt", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("addedAt", TableInfo.Column("addedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("syncState", TableInfo.Column("syncState", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUnsolved.put("movedToErrorId", TableInfo.Column("movedToErrorId", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysUnsolved: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesUnsolved: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesUnsolved.add(TableInfo.Index("index_unsolved_subject", false, listOf("subject"),
            listOf("ASC")))
        _indicesUnsolved.add(TableInfo.Index("index_unsolved_source", false, listOf("source"),
            listOf("ASC")))
        _indicesUnsolved.add(TableInfo.Index("index_unsolved_status", false, listOf("status"),
            listOf("ASC")))
        _indicesUnsolved.add(TableInfo.Index("index_unsolved_syncState", false, listOf("syncState"),
            listOf("ASC")))
        _indicesUnsolved.add(TableInfo.Index("index_unsolved_addedAt", false, listOf("addedAt"),
            listOf("ASC")))
        _indicesUnsolved.add(TableInfo.Index("index_unsolved_favorite", false, listOf("favorite"),
            listOf("ASC")))
        _indicesUnsolved.add(TableInfo.Index("index_unsolved_nextRetryAt", false,
            listOf("nextRetryAt"), listOf("ASC")))
        val _infoUnsolved: TableInfo = TableInfo("unsolved", _columnsUnsolved, _foreignKeysUnsolved,
            _indicesUnsolved)
        val _existingUnsolved: TableInfo = read(connection, "unsolved")
        if (!_infoUnsolved.equals(_existingUnsolved)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |unsolved(com.studyinfo.app.data.database.entity.UnsolvedQuestionEntity).
              | Expected:
              |""".trimMargin() + _infoUnsolved + """
              |
              | Found:
              |""".trimMargin() + _existingUnsolved)
        }
        val _columnsTasks: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTasks.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("description", TableInfo.Column("description", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("subject", TableInfo.Column("subject", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("chapterName", TableInfo.Column("chapterName", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("topic", TableInfo.Column("topic", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("dueDate", TableInfo.Column("dueDate", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("dueTime", TableInfo.Column("dueTime", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("estimatedMinutes", TableInfo.Column("estimatedMinutes", "INTEGER", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("priority", TableInfo.Column("priority", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("status", TableInfo.Column("status", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("recurrence", TableInfo.Column("recurrence", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("notes", TableInfo.Column("notes", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("completedAt", TableInfo.Column("completedAt", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("syncState", TableInfo.Column("syncState", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTasks: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesTasks: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesTasks.add(TableInfo.Index("index_tasks_dueDate", false, listOf("dueDate"),
            listOf("ASC")))
        _indicesTasks.add(TableInfo.Index("index_tasks_subject", false, listOf("subject"),
            listOf("ASC")))
        _indicesTasks.add(TableInfo.Index("index_tasks_status", false, listOf("status"),
            listOf("ASC")))
        _indicesTasks.add(TableInfo.Index("index_tasks_priority", false, listOf("priority"),
            listOf("ASC")))
        _indicesTasks.add(TableInfo.Index("index_tasks_syncState", false, listOf("syncState"),
            listOf("ASC")))
        _indicesTasks.add(TableInfo.Index("index_tasks_completedAt", false, listOf("completedAt"),
            listOf("ASC")))
        val _infoTasks: TableInfo = TableInfo("tasks", _columnsTasks, _foreignKeysTasks,
            _indicesTasks)
        val _existingTasks: TableInfo = read(connection, "tasks")
        if (!_infoTasks.equals(_existingTasks)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |tasks(com.studyinfo.app.data.database.entity.TaskEntity).
              | Expected:
              |""".trimMargin() + _infoTasks + """
              |
              | Found:
              |""".trimMargin() + _existingTasks)
        }
        val _columnsReviews: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsReviews.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsReviews.put("subject", TableInfo.Column("subject", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsReviews.put("startedAt", TableInfo.Column("startedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsReviews.put("endedAt", TableInfo.Column("endedAt", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsReviews.put("reviewedCount", TableInfo.Column("reviewedCount", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReviews.put("understoodCount", TableInfo.Column("understoodCount", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReviews.put("stillConfusedCount", TableInfo.Column("stillConfusedCount", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReviews.put("needsRevisionCount", TableInfo.Column("needsRevisionCount", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReviews.put("notes", TableInfo.Column("notes", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsReviews.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsReviews.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsReviews.put("syncState", TableInfo.Column("syncState", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysReviews: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesReviews: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesReviews.add(TableInfo.Index("index_reviews_syncState", false, listOf("syncState"),
            listOf("ASC")))
        _indicesReviews.add(TableInfo.Index("index_reviews_startedAt", false, listOf("startedAt"),
            listOf("ASC")))
        _indicesReviews.add(TableInfo.Index("index_reviews_subject", false, listOf("subject"),
            listOf("ASC")))
        val _infoReviews: TableInfo = TableInfo("reviews", _columnsReviews, _foreignKeysReviews,
            _indicesReviews)
        val _existingReviews: TableInfo = read(connection, "reviews")
        if (!_infoReviews.equals(_existingReviews)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |reviews(com.studyinfo.app.data.database.entity.ReviewEntity).
              | Expected:
              |""".trimMargin() + _infoReviews + """
              |
              | Found:
              |""".trimMargin() + _existingReviews)
        }
        val _columnsProgress: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsProgress.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProgress.put("chapterId", TableInfo.Column("chapterId", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProgress.put("subject", TableInfo.Column("subject", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProgress.put("examType", TableInfo.Column("examType", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProgress.put("chapterState", TableInfo.Column("chapterState", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProgress.put("percent", TableInfo.Column("percent", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProgress.put("questionsAttempted", TableInfo.Column("questionsAttempted", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProgress.put("questionsCorrect", TableInfo.Column("questionsCorrect", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProgress.put("notes", TableInfo.Column("notes", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProgress.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProgress.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProgress.put("syncState", TableInfo.Column("syncState", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysProgress: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesProgress: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesProgress.add(TableInfo.Index("index_progress_chapterId", false, listOf("chapterId"),
            listOf("ASC")))
        _indicesProgress.add(TableInfo.Index("index_progress_examType", false, listOf("examType"),
            listOf("ASC")))
        _indicesProgress.add(TableInfo.Index("index_progress_syncState", false, listOf("syncState"),
            listOf("ASC")))
        val _infoProgress: TableInfo = TableInfo("progress", _columnsProgress, _foreignKeysProgress,
            _indicesProgress)
        val _existingProgress: TableInfo = read(connection, "progress")
        if (!_infoProgress.equals(_existingProgress)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |progress(com.studyinfo.app.data.database.entity.ProgressEntity).
              | Expected:
              |""".trimMargin() + _infoProgress + """
              |
              | Found:
              |""".trimMargin() + _existingProgress)
        }
        val _columnsStreakActivity: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsStreakActivity.put("dateKey", TableInfo.Column("dateKey", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsStreakActivity.put("activityCount", TableInfo.Column("activityCount", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsStreakActivity.put("lastActivityAt", TableInfo.Column("lastActivityAt", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsStreakActivity.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsStreakActivity.put("syncState", TableInfo.Column("syncState", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysStreakActivity: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesStreakActivity: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoStreakActivity: TableInfo = TableInfo("streak_activity", _columnsStreakActivity,
            _foreignKeysStreakActivity, _indicesStreakActivity)
        val _existingStreakActivity: TableInfo = read(connection, "streak_activity")
        if (!_infoStreakActivity.equals(_existingStreakActivity)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |streak_activity(com.studyinfo.app.data.database.entity.StreakActivityEntity).
              | Expected:
              |""".trimMargin() + _infoStreakActivity + """
              |
              | Found:
              |""".trimMargin() + _existingStreakActivity)
        }
        val _columnsSyncQueue: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsSyncQueue.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSyncQueue.put("type", TableInfo.Column("type", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSyncQueue.put("refType", TableInfo.Column("refType", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSyncQueue.put("refId", TableInfo.Column("refId", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSyncQueue.put("imageId", TableInfo.Column("imageId", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSyncQueue.put("payload", TableInfo.Column("payload", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSyncQueue.put("attempts", TableInfo.Column("attempts", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSyncQueue.put("lastError", TableInfo.Column("lastError", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSyncQueue.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSyncQueue.put("nextAttemptAt", TableInfo.Column("nextAttemptAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysSyncQueue: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesSyncQueue: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoSyncQueue: TableInfo = TableInfo("sync_queue", _columnsSyncQueue,
            _foreignKeysSyncQueue, _indicesSyncQueue)
        val _existingSyncQueue: TableInfo = read(connection, "sync_queue")
        if (!_infoSyncQueue.equals(_existingSyncQueue)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |sync_queue(com.studyinfo.app.data.database.entity.SyncQueueEntity).
              | Expected:
              |""".trimMargin() + _infoSyncQueue + """
              |
              | Found:
              |""".trimMargin() + _existingSyncQueue)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "local_accounts",
        "user_profile", "tags", "custom_sources", "chapters", "topics", "question_images", "errors",
        "unsolved", "tasks", "reviews", "progress", "streak_activity", "sync_queue")
  }

  public override fun clearAllTables() {
    super.performClear(false, "local_accounts", "user_profile", "tags", "custom_sources",
        "chapters", "topics", "question_images", "errors", "unsolved", "tasks", "reviews",
        "progress", "streak_activity", "sync_queue")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(LocalAccountDao::class, LocalAccountDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(UserProfileDao::class, UserProfileDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(TagDao::class, TagDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(CustomSourceDao::class, CustomSourceDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(ChapterDao::class, ChapterDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(TopicDao::class, TopicDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(QuestionImageDao::class, QuestionImageDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(ErrorDao::class, ErrorDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(UnsolvedDao::class, UnsolvedDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(TaskDao::class, TaskDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(ReviewDao::class, ReviewDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(ProgressDao::class, ProgressDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(StreakDao::class, StreakDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(SyncQueueDao::class, SyncQueueDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun localAccountDao(): LocalAccountDao = _localAccountDao.value

  public override fun userProfileDao(): UserProfileDao = _userProfileDao.value

  public override fun tagDao(): TagDao = _tagDao.value

  public override fun customSourceDao(): CustomSourceDao = _customSourceDao.value

  public override fun chapterDao(): ChapterDao = _chapterDao.value

  public override fun topicDao(): TopicDao = _topicDao.value

  public override fun questionImageDao(): QuestionImageDao = _questionImageDao.value

  public override fun errorDao(): ErrorDao = _errorDao.value

  public override fun unsolvedDao(): UnsolvedDao = _unsolvedDao.value

  public override fun taskDao(): TaskDao = _taskDao.value

  public override fun reviewDao(): ReviewDao = _reviewDao.value

  public override fun progressDao(): ProgressDao = _progressDao.value

  public override fun streakDao(): StreakDao = _streakDao.value

  public override fun syncQueueDao(): SyncQueueDao = _syncQueueDao.value
}
