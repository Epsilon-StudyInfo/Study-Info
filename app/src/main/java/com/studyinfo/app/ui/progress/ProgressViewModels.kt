package com.studyinfo.app.ui.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studyinfo.app.ServiceLocator
import com.studyinfo.app.data.database.entity.ChapterEntity
import com.studyinfo.app.data.database.entity.ProgressEntity
import com.studyinfo.app.domain.model.ChapterState
import com.studyinfo.app.domain.model.ExamType
import com.studyinfo.app.domain.model.Subject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ProgressUiState(
    val chapters: List<ChapterEntity> = emptyList(),
    val progressByChapter: Map<String, ProgressEntity> = emptyMap(),
    val physicsAverage: Int = 0,
    val chemistryAverage: Int = 0,
    val mathematicsAverage: Int = 0,
    val jeeMainAverage: Int = 0,
    val jeeAdvancedAverage: Int = 0,
)

class ProgressViewModel : ViewModel() {
    private val _ui = MutableStateFlow(ProgressUiState())
    val ui: StateFlow<ProgressUiState> = _ui.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                ServiceLocator.chapterRepository.observeAll(),
                ServiceLocator.progressRepository.observeAll(),
            ) { chapters, progress ->
                val byChapter = progress.associateBy { it.chapterId }
                ProgressUiState(
                    chapters = chapters,
                    progressByChapter = byChapter,
                    physicsAverage = avgBySubject(progress, Subject.PHYSICS),
                    chemistryAverage = avgBySubject(progress, Subject.CHEMISTRY),
                    mathematicsAverage = avgBySubject(progress, Subject.MATHEMATICS),
                    jeeMainAverage = avgByExam(progress, ExamType.JEE_MAIN),
                    jeeAdvancedAverage = avgByExam(progress, ExamType.JEE_ADVANCED),
                )
            }.collect { _ui.value = it }
        }
    }

    private fun avgBySubject(items: List<ProgressEntity>, subject: Subject): Int {
        val filtered = items.filter { it.subject == subject }
        if (filtered.isEmpty()) return 0
        return filtered.map { it.percent }.average().toInt()
    }
    private fun avgByExam(items: List<ProgressEntity>, examType: ExamType): Int {
        val filtered = items.filter { it.examType == examType }
        if (filtered.isEmpty()) return 0
        return filtered.map { it.percent }.average().toInt()
    }

    fun setPercent(chapterId: String, subject: Subject, examType: ExamType, percent: Int) {
        viewModelScope.launch {
            ServiceLocator.progressRepository.setPercent(chapterId, subject, examType, percent)
        }
    }
    fun setChapterState(chapterId: String, state: ChapterState) {
        viewModelScope.launch { ServiceLocator.progressRepository.setChapterState(chapterId, state) }
    }
}

data class SubjectProgressUiState(
    val subject: Subject,
    val selectedExam: ExamType = ExamType.JEE_MAIN,
    val chapters: List<ChapterEntity> = emptyList(),
    /** Progress for the currently selected exam only, keyed by chapter id. */
    val progressByChapter: Map<String, ProgressEntity> = emptyMap(),
)

class SubjectProgressViewModel : ViewModel() {
    private val _ui = MutableStateFlow(SubjectProgressUiState(subject = Subject.PHYSICS))
    val ui: StateFlow<SubjectProgressUiState> = _ui.asStateFlow()

    fun load(subject: Subject) {
        viewModelScope.launch {
            combine(
                ServiceLocator.chapterRepository.observeBySubject(subject),
                ServiceLocator.progressRepository.observeBySubject(subject),
                _ui,
            ) { chapters, progress, state ->
                SubjectProgressUiState(
                    subject = subject,
                    selectedExam = state.selectedExam,
                    chapters = chapters,
                    progressByChapter = progress
                        .filter { it.examType == state.selectedExam }
                        .associateBy { it.chapterId },
                )
            }.collect { _ui.value = it }
        }
    }

    fun selectExam(exam: ExamType) {
        _ui.value = _ui.value.copy(selectedExam = exam)
    }

    fun setPercent(chapterId: String, subject: Subject, examType: ExamType, percent: Int) {
        viewModelScope.launch {
            ServiceLocator.progressRepository.setPercent(chapterId, subject, examType, percent)
        }
    }
}
