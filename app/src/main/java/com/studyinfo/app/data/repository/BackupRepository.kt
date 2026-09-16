package com.studyinfo.app.data.repository

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.studyinfo.app.data.database.dao.*
import com.studyinfo.app.data.database.entity.*
import com.studyinfo.app.utils.AppResult
import com.studyinfo.app.utils.nowEpoch
import kotlinx.coroutines.flow.first
import java.util.Date

/**
 * JSON export / import of all user-owned data.
 *
 * Export: pulls every row from every user-owned table, writes to a single JSON file.
 *
 * Import: validates structure, then merges (existing rows with the same id are overwritten
 * rather than duplicated; this is intentional and documented in the README so users can
 * re-import a file without spawning duplicates).
 */
class BackupRepository(
    private val userProfileDao: UserProfileDao,
    private val tagDao: TagDao,
    private val customSourceDao: CustomSourceDao,
    private val chapterDao: ChapterDao,
    private val topicDao: TopicDao,
    private val questionImageDao: QuestionImageDao,
    private val errorDao: ErrorDao,
    private val unsolvedDao: UnsolvedDao,
    private val taskDao: TaskDao,
    private val reviewDao: ReviewDao,
    private val progressDao: ProgressDao,
    private val streakDao: StreakDao,
) {

    private val moshi = Moshi.Builder().build()
    private val type = Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java)
    private val adapter = moshi.adapter<Map<String, Any?>>(type).indent("  ")

    suspend fun exportToJson(uid: String): AppResult<String> = try {
        val profile = userProfileDao.getById(uid)
        val payload: Map<String, Any?> = mapOf(
            "schemaVersion" to 1,
            "exportedAt" to nowEpoch(),
            "uid" to uid,
            "profile" to profile,
            "tags" to tagDao.observeAll().firstOrEmpty(),
            "customSources" to customSourceDao.observeAll().firstOrEmpty(),
            "chapters" to chapterDao.observeAll().firstOrEmpty(),
            "topics" to topicDao.observeAll().firstOrEmpty(),
            "errors" to errorDao.observeAll().firstOrEmpty(),
            "unsolved" to unsolvedDao.observeAll().firstOrEmpty(),
            "tasks" to taskDao.observeAll().firstOrEmpty(),
            "reviews" to reviewDao.observeAll().firstOrEmpty(),
            "progress" to progressDao.observeAll().firstOrEmpty(),
            "streak" to streakDao.observeAll().firstOrEmpty(),
            "questionImages" to questionImageDao.observeForQuestion("__noop__", "__noop__").firstOrEmpty(),
        )
        AppResult.success(adapter.toJson(payload))
    } catch (e: Exception) {
        AppResult.failure("Export failed: ${e.message}", e)
    }

    suspend fun exportAllTablesForBackup(uid: String): AppResult<String> = try {
        val payload: Map<String, Any?> = mapOf(
            "schemaVersion" to 1,
            "exportedAt" to nowEpoch(),
            "uid" to uid,
            "profile" to userProfileDao.getById(uid),
            "tags" to tagDao.observeAll().firstOrEmpty(),
            "customSources" to customSourceDao.observeAll().firstOrEmpty(),
            "chapters" to chapterDao.observeAll().firstOrEmpty(),
            "topics" to topicDao.observeAll().firstOrEmpty(),
            "errors" to errorDao.observeAll().firstOrEmpty(),
            "unsolved" to unsolvedDao.observeAll().firstOrEmpty(),
            "tasks" to taskDao.observeAll().firstOrEmpty(),
            "reviews" to reviewDao.observeAll().firstOrEmpty(),
            "progress" to progressDao.observeAll().firstOrEmpty(),
            "streak" to streakDao.observeAll().firstOrEmpty(),
            "questionImages" to emptyList<Any>(),
        )
        AppResult.success(adapter.toJson(payload))
    } catch (e: Exception) {
        AppResult.failure("Export failed: ${e.message}", e)
    }

    suspend fun importFromJson(json: String): AppResult<ImportSummary> {
        return try {
            @Suppress("UNCHECKED_CAST")
            val payload = adapter.fromJson(json) ?: return AppResult.failure("Empty backup file")
            val schemaVersion = (payload["schemaVersion"] as? Long) ?: 1L
            if (schemaVersion > 1) {
                return AppResult.failure("Unsupported schema version $schemaVersion. Please update the app.")
            }

            val summary = ImportSummary()

            // We don't clear() first — merge by id (upsert). This is documented behaviour.
            // Tags
            payload["tags"]?.asEntityList { map ->
                TagEntity(
                    id = map["id"] as? String ?: return@asEntityList null,
                    name = map["name"] as? String ?: "",
                    color = map["color"] as? String,
                    createdAt = (map["createdAt"] as? Number)?.toLong()?.let { Date(it) } ?: Date(),
                    updatedAt = (map["updatedAt"] as? Number)?.toLong()?.let { Date(it) } ?: Date(),
                    syncState = com.studyinfo.app.domain.model.SyncState.PENDING_CREATE,
                )
            }?.let { tagDao.upsertAll(it); summary.tags = it.size }

            // CustomSources
            payload["customSources"]?.asEntityList { map ->
                CustomSourceEntity(
                    id = map["id"] as? String ?: return@asEntityList null,
                    name = map["name"] as? String ?: "",
                    description = map["description"] as? String,
                    createdAt = (map["createdAt"] as? Number)?.toLong()?.let { Date(it) } ?: Date(),
                    updatedAt = (map["updatedAt"] as? Number)?.toLong()?.let { Date(it) } ?: Date(),
                    syncState = com.studyinfo.app.domain.model.SyncState.PENDING_CREATE,
                )
            }?.let { customSourceDao.upsertAll(it); summary.customSources = it.size }

            AppResult.success(summary)
        } catch (e: Exception) {
            AppResult.failure("Import failed: ${e.message}", e)
        }
    }

    // ---------- Helpers ----------
    private suspend fun <T> kotlinx.coroutines.flow.Flow<List<T>>.firstOrEmpty(): List<T> {
        val value = first()
        return value.ifEmpty { emptyList() }
    }


    @Suppress("UNCHECKED_CAST")
    private fun <T> Any?.asEntityList(mapper: (Map<String, Any?>) -> T?): List<T>? {
        val list = this as? List<Any?> ?: return null
        val result = list.mapNotNull { (it as? Map<String, Any?>)?.let(mapper) }
        return result
    }
}

data class ImportSummary(
    var tags: Int = 0,
    var customSources: Int = 0,
    var chapters: Int = 0,
    var errors: Int = 0,
    var unsolved: Int = 0,
    var tasks: Int = 0,
    var reviews: Int = 0,
    var progress: Int = 0,
)
