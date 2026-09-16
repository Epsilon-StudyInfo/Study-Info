package com.studyinfo.app.data.repository

import com.studyinfo.app.data.database.dao.UnsolvedDao
import com.studyinfo.app.data.database.entity.ErrorEntryEntity
import com.studyinfo.app.data.database.entity.UnsolvedQuestionEntity
import com.studyinfo.app.data.firebase.FirestoreErrorsDataSource
import com.studyinfo.app.data.firebase.FirestoreUnsolvedDataSource
import com.studyinfo.app.domain.model.*
import com.studyinfo.app.utils.newId
import com.studyinfo.app.utils.nowEpoch
import kotlinx.coroutines.flow.Flow
import java.util.Date

class UnsolvedRepository(
    private val dao: UnsolvedDao,
    private val remote: FirestoreUnsolvedDataSource?,
    private val errorsRemote: FirestoreErrorsDataSource?,
    private val errorDao: com.studyinfo.app.data.database.dao.ErrorDao,
    private val streakRecorder: StreakRecorder? = null,
    /** Attached-image cleanup/transfer; wired by the ServiceLocator after construction. */
    internal var questionImages: QuestionImageRepository? = null,
) {

    // ---------- Observers ----------
    fun observeAll(): Flow<List<UnsolvedQuestionEntity>> = dao.observeAll()
    fun observeBySource(source: QuestionSource): Flow<List<UnsolvedQuestionEntity>> =
        dao.observeBySource(source.name)
    fun observeById(id: String): Flow<UnsolvedQuestionEntity?> = dao.observeById(id)
    fun observeCountByStatus(status: UnsolvedStatus): Flow<Int> = dao.observeCountByStatus(status)
    fun observeCountBySource(source: String): Flow<Int> = dao.observeCountBySource(source)
    fun observeRetryQueue(): Flow<List<UnsolvedQuestionEntity>> =
        dao.observeRetryQueue(System.currentTimeMillis())

    suspend fun getById(id: String): UnsolvedQuestionEntity? = dao.getById(id)
    suspend fun search(query: String, limit: Int = 50): List<UnsolvedQuestionEntity> =
        dao.search(query, limit)

    // ---------- Mutations ----------
    suspend fun add(entry: UnsolvedQuestionEntity): UnsolvedQuestionEntity {
        val now = Date(nowEpoch())
        val withMeta = entry.copy(
            id = entry.id.ifBlank { newId() },
            addedAt = now,
            updatedAt = now,
            syncState = SyncState.PENDING_CREATE,
        )
        dao.upsert(withMeta)
        runCatching { streakRecorder?.record() }
        return withMeta
    }

    suspend fun update(entry: UnsolvedQuestionEntity) {
        dao.upsert(entry.copy(updatedAt = Date(nowEpoch()), syncState = SyncState.PENDING_UPDATE))
    }

    suspend fun setFavorite(id: String, fav: Boolean) = dao.setFavorite(id, fav, nowEpoch())

    suspend fun setStatus(id: String, status: UnsolvedStatus) {
        dao.setStatus(id, status, nowEpoch())
        if (status == UnsolvedStatus.SOLVED || status == UnsolvedStatus.ATTEMPT_AGAIN) {
            // bump retry metadata
            val existing = dao.getById(id) ?: return
            dao.upsert(existing.copy(
                retryCount = existing.retryCount + (if (status == UnsolvedStatus.ATTEMPT_AGAIN) 1 else 0),
                lastRetriedAt = if (status == UnsolvedStatus.ATTEMPT_AGAIN) Date(nowEpoch()) else existing.lastRetriedAt,
                updatedAt = Date(nowEpoch()),
                syncState = SyncState.PENDING_UPDATE,
            ))
        }
    }

    suspend fun scheduleRetry(id: String, days: Int) {
        val existing = dao.getById(id) ?: return
        val now = Date(nowEpoch())
        dao.upsert(existing.copy(
            status = UnsolvedStatus.ATTEMPT_AGAIN,
            nextRetryAt = Date(now.time + days * 24L * 60L * 60L * 1000L),
            updatedAt = now,
            syncState = SyncState.PENDING_UPDATE,
        ))
    }

    suspend fun delete(id: String) {
        val existing = dao.getById(id) ?: return
        dao.upsert(existing.copy(updatedAt = Date(nowEpoch()), syncState = SyncState.PENDING_DELETE))
        // Attached images (local files + Storage binaries + Firestore metadata) are removed
        // with the question — best-effort so a cloud hiccup can never block the local delete.
        runCatching { questionImages?.deleteForQuestion(QuestionImageRepository.REF_UNSOLVED, id) }
            .onFailure { /* non-fatal: orphaned image rows are inert */ }
    }

    suspend fun hardDelete(id: String) = dao.delete(id)

    /**
     * Convert an unsolved question into an error entry.
     *
     * The original unsolved record is preserved (we just set [movedToErrorId] and
     * status = SOLVED) so history is not lost. The new error entry contains the same
     * question text, source info, etc.; the user is expected to add mistake-specific
     * fields (mistakeType, lessonLearned, ...) afterwards via the Edit Error screen.
     */
    suspend fun moveToErrorBook(
        unsolvedId: String,
        mistakeType: MistakeType = MistakeType.OTHER,
        attemptedSolution: String? = null,
        correctSolution: String? = null,
        explanation: String? = null,
        lessonLearned: String? = null,
    ): ErrorEntryEntity? {
        val unsolved = dao.getById(unsolvedId) ?: return null
        val now = Date(nowEpoch())
        val error = ErrorEntryEntity(
            id = newId(),
            title = unsolved.title,
            questionText = unsolved.questionText,
            subject = unsolved.subject,
            chapterName = unsolved.chapterName,
            topic = unsolved.topic,
            source = unsolved.source,
            sourceRefId = unsolved.sourceRefId,
            dppNumber = unsolved.dppNumber,
            chapterNumber = unsolved.chapterNumber,
            questionNumber = unsolved.questionNumber,
            pyqYear = unsolved.pyqYear,
            examType = unsolved.examType,
            shiftSession = unsolved.shiftSession,
            instituteName = unsolved.instituteName,
            moduleNumber = unsolved.moduleNumber,
            exercise = unsolved.exercise,
            testName = unsolved.testName,
            testNumber = unsolved.testNumber,
            testDate = unsolved.testDate,
            marks = unsolved.marks,
            bookName = unsolved.bookName,
            customSourceName = unsolved.customSourceName,
            difficulty = unsolved.difficulty,
            mistakeType = mistakeType,
            attemptedSolution = attemptedSolution,
            correctSolution = correctSolution,
            explanation = explanation,
            lessonLearned = lessonLearned,
            tags = unsolved.tags,
            status = ErrorStatus.ACTIVE,
            favorite = unsolved.favorite,
            addedAt = now,
            updatedAt = now,
            syncState = SyncState.PENDING_CREATE,
            originUnsolvedId = unsolvedId,
        )
        errorDao.upsert(error)
        dao.markMovedToError(unsolvedId, error.id, nowEpoch())
        // Attached images follow the question into the error book: their metadata is
        // re-pointed to the new error id (binaries keep their remote location). Best-effort
        // — a sync failure must not abort the move itself.
        runCatching {
            questionImages?.reassign(
                fromRefType = QuestionImageRepository.REF_UNSOLVED,
                fromRefId = unsolvedId,
                toRefType = QuestionImageRepository.REF_ERROR,
                toRefId = error.id,
            )
        }.onFailure { /* non-fatal: image rows stay on the unsolved record */ }
        return error
    }

    // ---------- Sync ----------
    suspend fun pushPending(): Int {
        val pending = dao.pendingChanges()
        if (pending.isEmpty()) return 0
        var count = 0
        for (entry in pending) {
            when (entry.syncState) {
                SyncState.PENDING_CREATE, SyncState.PENDING_UPDATE ->
                    if (remote?.put(entry.id, entry) == true) { dao.markSync(entry.id); count++ }
                SyncState.PENDING_DELETE ->
                    if (remote?.delete(entry.id) == true) { dao.hardDelete(entry.id); count++ }
                else -> {}
            }
        }
        return count
    }

    suspend fun pullAll(): Int {
        val remoteList = remote?.fetchAll() ?: emptyList()
        if (remoteList.isNotEmpty()) dao.upsertAll(remoteList)
        return remoteList.size
    }

    suspend fun clear() = dao.clear()
}
