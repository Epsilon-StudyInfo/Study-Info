package com.studyinfo.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studyinfo.app.ServiceLocator
import com.studyinfo.app.data.database.entity.ErrorEntryEntity
import com.studyinfo.app.data.database.entity.ProgressEntity
import com.studyinfo.app.data.database.entity.TaskEntity
import com.studyinfo.app.data.database.entity.UnsolvedQuestionEntity
import com.studyinfo.app.domain.model.Subject
import com.studyinfo.app.domain.model.UnsolvedStatus
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeUiState(
    val greeting: String = "Welcome",
    val userName: String = "",
    val todayTotalTasks: Int = 0,
    val todayCompletedTasks: Int = 0,
    val errorsCount: Int = 0,
    val unsolvedCount: Int = 0,
    val solvedCount: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val physicsPercent: Int = 0,
    val chemistryPercent: Int = 0,
    val mathematicsPercent: Int = 0,
    val jeeMainPercent: Int = 0,
    val jeeAdvancedPercent: Int = 0,
    val overdueTasks: List<TaskEntity> = emptyList(),
    val recentErrors: List<ErrorEntryEntity> = emptyList(),
    val recentUnsolved: List<UnsolvedQuestionEntity> = emptyList(),
    val loading: Boolean = true,
)

class HomeViewModel : ViewModel() {
    private val _ui = MutableStateFlow(HomeUiState())
    val ui: StateFlow<HomeUiState> = _ui.asStateFlow()

    init {
        val taskRepo = ServiceLocator.taskRepository
        val errorRepo = ServiceLocator.errorRepository
        val unsolvedRepo = ServiceLocator.unsolvedRepository
        val progressRepo = ServiceLocator.progressRepository

        // Today's tasks + error count + solved count + overdue
        combine(
            taskRepo.observeTodayCompletion(),
            errorRepo.observeCount(),
            unsolvedRepo.observeCountByStatus(UnsolvedStatus.SOLVED),
            taskRepo.observeOverdue(),
        ) { today, errorCount, solvedCount, overdue ->
            val (total, completed) = today
            _ui.value = _ui.value.copy(
                todayTotalTasks = total,
                todayCompletedTasks = completed,
                errorsCount = errorCount,
                solvedCount = solvedCount,
                overdueTasks = overdue,
                loading = false,
            )
        }.launchIn(viewModelScope)

        // Unsolved count
        viewModelScope.launch {
            unsolvedRepo.observeCountByStatus(UnsolvedStatus.UNSOLVED).collect {
                _ui.value = _ui.value.copy(unsolvedCount = it)
            }
        }

        // Progress
        viewModelScope.launch {
            progressRepo.observeAll().collect { progressList ->
                val bySubject = progressList.groupBy { it.subject }
                _ui.value = _ui.value.copy(
                    physicsPercent = avg(bySubject[Subject.PHYSICS]),
                    chemistryPercent = avg(bySubject[Subject.CHEMISTRY]),
                    mathematicsPercent = avg(bySubject[Subject.MATHEMATICS]),
                    jeeMainPercent = avg(progressList.filter { it.examType == com.studyinfo.app.domain.model.ExamType.JEE_MAIN }),
                    jeeAdvancedPercent = avg(progressList.filter { it.examType == com.studyinfo.app.domain.model.ExamType.JEE_ADVANCED }),
                )
            }
        }

        // Streak + user profile
        viewModelScope.launch {
            val (cur, lon) = taskRepo.computeStreak()
            _ui.value = _ui.value.copy(currentStreak = cur, longestStreak = lon)
        }
        viewModelScope.launch {
            ServiceLocator.authRepository.observeUserProfile().collect { p ->
                _ui.value = _ui.value.copy(
                    userName = p?.displayName ?: "",
                    greeting = greeting(),
                )
            }
        }
    }

    private fun avg(items: List<*>?): Int {
        if (items.isNullOrEmpty()) return 0
        @Suppress("UNCHECKED_CAST")
        val percents = items.map { (it as ProgressEntity).percent }
        return percents.average().toInt()
    }

    private fun greeting(): String {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 0..11 -> "Good morning"
            in 12..17 -> "Good afternoon"
            else -> "Good evening"
        }
    }
}
