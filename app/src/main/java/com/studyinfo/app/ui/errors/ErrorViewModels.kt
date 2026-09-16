package com.studyinfo.app.ui.errors

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studyinfo.app.ServiceLocator
import com.studyinfo.app.data.database.entity.ErrorEntryEntity
import com.studyinfo.app.data.database.entity.QuestionImageEntity
import com.studyinfo.app.data.repository.QuestionImageRepository
import com.studyinfo.app.domain.model.*
import com.studyinfo.app.utils.newId
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ErrorListUiState(
    val items: List<ErrorEntryEntity> = emptyList(),
    val filterSubject: Subject? = null,
    val filterStatus: ErrorStatus? = null,
    val query: String = "",
)

class ErrorListViewModel : ViewModel() {
    private val _ui = MutableStateFlow(ErrorListUiState())
    val ui: StateFlow<ErrorListUiState> = _ui.asStateFlow()

    init {
        viewModelScope.launch {
            ServiceLocator.errorRepository.observeAll().combine(_ui) { items, state ->
                items.filter { item ->
                    (state.filterSubject == null || item.subject == state.filterSubject) &&
                    (state.filterStatus == null || item.status == state.filterStatus) &&
                    (state.query.isBlank() ||
                        item.title.contains(state.query, true) ||
                        item.chapterName?.contains(state.query, true) == true ||
                        item.questionText.contains(state.query, true))
                }
            }.collect { filtered ->
                _ui.value = _ui.value.copy(items = filtered)
            }
        }
    }

    fun setQuery(q: String) { _ui.value = _ui.value.copy(query = q) }
    fun setSubject(s: Subject?) { _ui.value = _ui.value.copy(filterSubject = s) }
    fun setStatus(s: ErrorStatus?) { _ui.value = _ui.value.copy(filterStatus = s) }

    fun toggleFavorite(id: String, fav: Boolean) {
        viewModelScope.launch { ServiceLocator.errorRepository.setFavorite(id, fav) }
    }

    fun delete(id: String) {
        viewModelScope.launch { ServiceLocator.errorRepository.delete(id) }
    }
}

data class ErrorEditUiState(
    val id: String? = null,
    val isNew: Boolean = true,        // add-mode until an existing row is loaded
    val title: String = "",
    val questionText: String = "",
    val subject: Subject = Subject.PHYSICS,
    val chapterName: String = "",
    val topic: String = "",
    val source: QuestionSource = QuestionSource.CUSTOM,
    val dppNumber: String = "",
    val chapterNumber: String = "",
    val questionNumber: String = "",
    val pyqYear: String = "",
    val examType: ExamType = ExamType.JEE_MAIN,
    val shiftSession: String = "",
    val instituteName: String = "",
    val moduleNumber: String = "",
    val exercise: String = "",
    val testName: String = "",
    val testNumber: String = "",
    val bookName: String = "",
    val customSourceName: String = "",
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val mistakeType: MistakeType = MistakeType.OTHER,
    val attemptedSolution: String = "",
    val correctSolution: String = "",
    val explanation: String = "",
    val lessonLearned: String = "",
    val personalNotes: String = "",
    val favorite: Boolean = false,
    val tags: List<String> = emptyList(),
    val images: List<QuestionImageEntity> = emptyList(),
    val imageBusy: Boolean = false,
    val saving: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null,
)

class ErrorEditViewModel : ViewModel() {
    private val _ui = MutableStateFlow(ErrorEditUiState())
    val ui: StateFlow<ErrorEditUiState> = _ui.asStateFlow()

    /** Id whose images are currently observed (add-mode generates the id up-front). */
    private var observedImageId: String? = null

    fun load(id: String?) {
        if (id == null) return
        viewModelScope.launch {
            val e = ServiceLocator.errorRepository.getById(id) ?: return@launch
            _ui.value = ErrorEditUiState(
                id = e.id,
                isNew = false,
                title = e.title,
                questionText = e.questionText,
                subject = e.subject,
                chapterName = e.chapterName ?: "",
                topic = e.topic ?: "",
                source = e.source,
                dppNumber = e.dppNumber ?: "",
                chapterNumber = e.chapterNumber ?: "",
                questionNumber = e.questionNumber ?: "",
                pyqYear = e.pyqYear?.toString() ?: "",
                examType = e.examType ?: ExamType.JEE_MAIN,
                shiftSession = e.shiftSession ?: "",
                instituteName = e.instituteName ?: "",
                moduleNumber = e.moduleNumber ?: "",
                exercise = e.exercise ?: "",
                testName = e.testName ?: "",
                testNumber = e.testNumber ?: "",
                bookName = e.bookName ?: "",
                customSourceName = e.customSourceName ?: "",
                difficulty = e.difficulty,
                mistakeType = e.mistakeType,
                attemptedSolution = e.attemptedSolution ?: "",
                correctSolution = e.correctSolution ?: "",
                explanation = e.explanation ?: "",
                lessonLearned = e.lessonLearned ?: "",
                personalNotes = e.personalNotes ?: "",
                favorite = e.favorite,
                tags = e.tags,
            )
            observeImages(e.id)
        }
    }

    fun update(transform: (ErrorEditUiState) -> ErrorEditUiState) {
        _ui.value = transform(_ui.value)
    }

    /** Adds picked images; the question id is generated on first attach (add-mode). */
    fun addImages(uris: List<Uri>) {
        if (uris.isEmpty()) return
        val questionId = ensureQuestionId()
        _ui.value = _ui.value.copy(imageBusy = true, error = null)
        viewModelScope.launch {
            var failed = 0
            for (uri in uris) {
                val added = ServiceLocator.questionImageRepository.addFromPicker(
                    QuestionImageRepository.REF_ERROR, questionId, uri,
                )
                if (added == null) failed++
            }
            _ui.value = _ui.value.copy(
                imageBusy = false,
                error = if (failed > 0) "Could not add $failed of the picked image(s). Please try again." else _ui.value.error,
            )
        }
    }

    fun removeImage(imageId: String) {
        viewModelScope.launch { ServiceLocator.questionImageRepository.delete(imageId) }
    }

    /** Id used for a brand-new question so attachments can be stored before the save. */
    private fun ensureQuestionId(): String {
        _ui.value.id?.let { return it }
        val id = newId()
        _ui.value = _ui.value.copy(id = id)
        observeImages(id)
        return id
    }

    private fun observeImages(questionId: String) {
        if (observedImageId == questionId) return
        observedImageId = questionId
        viewModelScope.launch {
            ServiceLocator.questionImageRepository
                .observeForQuestion(QuestionImageRepository.REF_ERROR, questionId)
                .collect { imgs -> _ui.value = _ui.value.copy(images = imgs) }
        }
    }

    fun save() {
        val s = _ui.value
        if (s.saving || s.saved) return
        if (s.questionText.isBlank() && s.title.isBlank() && s.images.isEmpty()) {
            _ui.value = s.copy(error = "Add the question text or attach an image.")
            return
        }
        _ui.value = s.copy(saving = true, error = null)
        viewModelScope.launch {
            val now = java.util.Date(com.studyinfo.app.utils.nowEpoch())
            val entity = ErrorEntryEntity(
                id = s.id ?: newId(),
                title = s.title.ifBlank { s.questionText.ifBlank { "Question image" }.take(40) },
                questionText = s.questionText,
                subject = s.subject,
                chapterName = s.chapterName.ifBlank { null },
                topic = s.topic.ifBlank { null },
                source = s.source,
                sourceRefId = null,
                dppNumber = s.dppNumber.ifBlank { null },
                chapterNumber = s.chapterNumber.ifBlank { null },
                questionNumber = s.questionNumber.ifBlank { null },
                pyqYear = s.pyqYear.toIntOrNull(),
                examType = if (s.source == QuestionSource.PYQ) s.examType else null,
                shiftSession = s.shiftSession.ifBlank { null },
                instituteName = s.instituteName.ifBlank { null },
                moduleNumber = s.moduleNumber.ifBlank { null },
                exercise = s.exercise.ifBlank { null },
                testName = s.testName.ifBlank { null },
                testNumber = s.testNumber.ifBlank { null },
                bookName = s.bookName.ifBlank { null },
                customSourceName = s.customSourceName.ifBlank { null },
                difficulty = s.difficulty,
                mistakeType = s.mistakeType,
                attemptedSolution = s.attemptedSolution.ifBlank { null },
                correctSolution = s.correctSolution.ifBlank { null },
                explanation = s.explanation.ifBlank { null },
                lessonLearned = s.lessonLearned.ifBlank { null },
                personalNotes = s.personalNotes.ifBlank { null },
                tags = s.tags,
                favorite = s.favorite,
            )
            if (s.isNew) {
                ServiceLocator.errorRepository.add(entity)
            } else {
                val existing = s.id?.let { ServiceLocator.errorRepository.getById(it) }
                if (existing == null) {
                    // Row was deleted while editing (e.g. from the list or by a sync run).
                    _ui.value = _ui.value.copy(saving = false, error = "This entry no longer exists.")
                    return@launch
                }
                ServiceLocator.errorRepository.update(entity.copy(
                    addedAt = existing.addedAt,
                    reviewCount = existing.reviewCount,
                    lastReviewedAt = existing.lastReviewedAt,
                    nextReviewAt = existing.nextReviewAt,
                    status = existing.status,
                    originUnsolvedId = existing.originUnsolvedId,
                ))
            }
            _ui.value = _ui.value.copy(saving = false, saved = true)
        }
    }
}

data class ErrorReviewUiState(
    val error: ErrorEntryEntity? = null,
    val reflection: String = "",
    val scheduleDays: Int = 3,
    val saving: Boolean = false,
    val saved: Boolean = false,
)

class ErrorReviewViewModel : ViewModel() {
    private val _ui = MutableStateFlow(ErrorReviewUiState())
    val ui: StateFlow<ErrorReviewUiState> = _ui.asStateFlow()

    fun load(id: String) {
        viewModelScope.launch {
            _ui.value = _ui.value.copy(error = ServiceLocator.errorRepository.getById(id))
        }
    }

    fun setReflection(s: String) { _ui.value = _ui.value.copy(reflection = s) }
    fun setScheduleDays(days: Int) { _ui.value = _ui.value.copy(scheduleDays = days) }

    fun submit(outcome: ReviewOutcome) {
        val s = _ui.value
        val error = s.error ?: return
        _ui.value = s.copy(saving = true)
        viewModelScope.launch {
            ServiceLocator.errorRepository.recordReview(
                id = error.id,
                outcome = outcome,
                reflection = s.reflection.ifBlank { null },
                scheduleDays = s.scheduleDays,
            )
            _ui.value = _ui.value.copy(saving = false, saved = true)
        }
    }
}
