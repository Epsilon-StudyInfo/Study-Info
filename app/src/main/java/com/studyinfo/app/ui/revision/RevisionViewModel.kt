package com.studyinfo.app.ui.revision

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studyinfo.app.ServiceLocator
import com.studyinfo.app.data.database.entity.ErrorEntryEntity
import com.studyinfo.app.data.database.entity.UnsolvedQuestionEntity
import com.studyinfo.app.domain.model.ErrorStatus
import com.studyinfo.app.domain.model.ReviewOutcome
import com.studyinfo.app.domain.model.UnsolvedStatus
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class RevisionUiState(
    val dueErrors: List<ErrorEntryEntity> = emptyList(),
    val dueUnsolved: List<UnsolvedQuestionEntity> = emptyList(),
    val favoriteErrors: List<ErrorEntryEntity> = emptyList(),
    val started: Boolean = false,
    val currentSessionId: String? = null,
    val reviewedCount: Int = 0,
    val understood: Int = 0,
    val confused: Int = 0,
    val needsRevision: Int = 0,
    val finished: Boolean = false,
)

class RevisionViewModel : ViewModel() {
    private val _ui = MutableStateFlow(RevisionUiState())
    val ui: StateFlow<RevisionUiState> = _ui.asStateFlow()

    init {
        viewModelScope.launch {
            ServiceLocator.errorRepository.observeDueForReview().collect { _ui.value = _ui.value.copy(dueErrors = it) }
        }
        viewModelScope.launch {
            ServiceLocator.errorRepository.observeFavorites().collect { _ui.value = _ui.value.copy(favoriteErrors = it) }
        }
        viewModelScope.launch {
            ServiceLocator.unsolvedRepository.observeRetryQueue().collect { _ui.value = _ui.value.copy(dueUnsolved = it) }
        }
    }

    fun start() {
        viewModelScope.launch {
            val session = ServiceLocator.reviewRepository.start(notes = null)
            _ui.value = _ui.value.copy(started = true, currentSessionId = session.id)
        }
    }

    fun recordError(errorId: String, outcome: ReviewOutcome) {
        val sessionId = _ui.value.currentSessionId ?: return
        viewModelScope.launch {
            ServiceLocator.errorRepository.recordReview(errorId, outcome)
            ServiceLocator.reviewRepository.record(sessionId, outcome)
            _ui.value = _ui.value.copy(
                reviewedCount = _ui.value.reviewedCount + 1,
                understood = _ui.value.understood + (if (outcome == ReviewOutcome.UNDERSTOOD) 1 else 0),
                confused = _ui.value.confused + (if (outcome == ReviewOutcome.STILL_CONFUSED) 1 else 0),
                needsRevision = _ui.value.needsRevision + (if (outcome == ReviewOutcome.NEEDS_REVISION) 1 else 0),
                dueErrors = _ui.value.dueErrors.filterNot { it.id == errorId },
            )
        }
    }

    /** Reviews one unsolved retry-queue item: understood -> solved, otherwise re-scheduled. */
    fun recordUnsolved(unsolvedId: String, outcome: ReviewOutcome) {
        val sessionId = _ui.value.currentSessionId ?: return
        viewModelScope.launch {
            when (outcome) {
                ReviewOutcome.UNDERSTOOD ->
                    ServiceLocator.unsolvedRepository.setStatus(unsolvedId, UnsolvedStatus.SOLVED)
                ReviewOutcome.NEEDS_REVISION ->
                    ServiceLocator.unsolvedRepository.scheduleRetry(unsolvedId, days = 3)
                ReviewOutcome.STILL_CONFUSED ->
                    ServiceLocator.unsolvedRepository.scheduleRetry(unsolvedId, days = 1)
            }
            ServiceLocator.reviewRepository.record(sessionId, outcome)
            _ui.value = _ui.value.copy(
                reviewedCount = _ui.value.reviewedCount + 1,
                understood = _ui.value.understood + (if (outcome == ReviewOutcome.UNDERSTOOD) 1 else 0),
                confused = _ui.value.confused + (if (outcome == ReviewOutcome.STILL_CONFUSED) 1 else 0),
                needsRevision = _ui.value.needsRevision + (if (outcome == ReviewOutcome.NEEDS_REVISION) 1 else 0),
                dueUnsolved = _ui.value.dueUnsolved.filterNot { it.id == unsolvedId },
            )
        }
    }

    fun end() {
        val sessionId = _ui.value.currentSessionId ?: return
        viewModelScope.launch {
            ServiceLocator.reviewRepository.end(sessionId)
            _ui.value = _ui.value.copy(finished = true)
        }
    }
}
