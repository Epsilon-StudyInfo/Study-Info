package com.studyinfo.app.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studyinfo.app.ServiceLocator
import com.studyinfo.app.domain.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class StatisticsUiState(
    val totalErrors: Int = 0,
    val totalUnsolved: Int = 0,
    val totalSolved: Int = 0,
    val totalTasksCompleted: Int = 0,
    val revisionSessions: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val errorsBySubject: Map<Subject, Int> = emptyMap(),
    val errorsByMistake: Map<MistakeType, Int> = emptyMap(),
    val unsolvedBySource: Map<QuestionSource, Int> = emptyMap(),
)

class StatisticsViewModel : ViewModel() {
    private val _ui = MutableStateFlow(StatisticsUiState())
    val ui: StateFlow<StatisticsUiState> = _ui.asStateFlow()

    init {
        viewModelScope.launch {
            ServiceLocator.errorRepository.observeCount().collect { total ->
                _ui.value = _ui.value.copy(totalErrors = total)
            }
        }
        viewModelScope.launch {
            ServiceLocator.unsolvedRepository.observeCountByStatus(UnsolvedStatus.UNSOLVED).collect {
                _ui.value = _ui.value.copy(totalUnsolved = it)
            }
        }
        viewModelScope.launch {
            ServiceLocator.unsolvedRepository.observeCountByStatus(UnsolvedStatus.SOLVED).collect {
                _ui.value = _ui.value.copy(totalSolved = it)
            }
        }
        viewModelScope.launch {
            ServiceLocator.taskRepository.observeByStatus(TaskStatus.COMPLETED).collect {
                _ui.value = _ui.value.copy(totalTasksCompleted = it.size)
            }
        }
        viewModelScope.launch {
            ServiceLocator.reviewRepository.observeCount().collect {
                _ui.value = _ui.value.copy(revisionSessions = it)
            }
        }
        viewModelScope.launch {
            val (cur, lon) = ServiceLocator.taskRepository.computeStreak()
            _ui.value = _ui.value.copy(currentStreak = cur, longestStreak = lon)
        }
        // Per-subject error counts
        Subject.entries.forEach { subject ->
            viewModelScope.launch {
                ServiceLocator.errorRepository.observeCountBySubject(subject).collect { count ->
                    _ui.value = _ui.value.copy(errorsBySubject = _ui.value.errorsBySubject + (subject to count))
                }
            }
        }
        // Per-mistake-type counts
        MistakeType.entries.forEach { mt ->
            viewModelScope.launch {
                ServiceLocator.errorRepository.observeCountByMistake(mt.name).collect { count ->
                    _ui.value = _ui.value.copy(errorsByMistake = _ui.value.errorsByMistake + (mt to count))
                }
            }
        }
        // Per-source counts (unsolved)
        QuestionSource.entries.forEach { src ->
            viewModelScope.launch {
                ServiceLocator.unsolvedRepository.observeCountBySource(src.name).collect { count ->
                    _ui.value = _ui.value.copy(unsolvedBySource = _ui.value.unsolvedBySource + (src to count))
                }
            }
        }
    }
}
