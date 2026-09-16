package com.studyinfo.app.ui.unsolved

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studyinfo.app.ServiceLocator
import com.studyinfo.app.data.database.entity.QuestionImageEntity
import com.studyinfo.app.data.database.entity.UnsolvedQuestionEntity
import com.studyinfo.app.data.repository.QuestionImageRepository
import com.studyinfo.app.domain.model.*
import com.studyinfo.app.utils.newId
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class UnsolvedListUiState(
    val activeSource: QuestionSource? = null,
    val items: List<UnsolvedQuestionEntity> = emptyList(),
    val query: String = "",
)

class UnsolvedListViewModel : ViewModel() {
    private val _ui = MutableStateFlow(UnsolvedListUiState())
    val ui: StateFlow<UnsolvedListUiState> = _ui.asStateFlow()

    init {
        viewModelScope.launch {
            ServiceLocator.unsolvedRepository.observeAll().combine(_ui) { items, state ->
                items.filter { entry ->
                    (state.activeSource == null || entry.source == state.activeSource) &&
                    (state.query.isBlank() || entry.title.contains(state.query, true) || entry.questionText.contains(state.query, true) || entry.chapterName?.contains(state.query, true) == true)
                }
            }.collect { filtered ->
                _ui.value = _ui.value.copy(items = filtered)
            }
        }
    }

    fun setSource(s: QuestionSource?) { _ui.value = _ui.value.copy(activeSource = s) }
    fun setQuery(q: String) { _ui.value = _ui.value.copy(query = q) }
    fun toggleFavorite(id: String, fav: Boolean) {
        viewModelScope.launch { ServiceLocator.unsolvedRepository.setFavorite(id, fav) }
    }
    fun delete(id: String) {
        viewModelScope.launch { ServiceLocator.unsolvedRepository.delete(id) }
    }
    fun markSolved(id: String) {
        viewModelScope.launch { ServiceLocator.unsolvedRepository.setStatus(id, UnsolvedStatus.SOLVED) }
    }
}

data class UnsolvedEditUiState(
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
    val reasonNotSolved: String = "",
    val personalNotes: String = "",
    val favorite: Boolean = false,
    val tags: List<String> = emptyList(),
    val images: List<QuestionImageEntity> = emptyList(),
    val imageBusy: Boolean = false,
    val saving: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null,
)

class UnsolvedEditViewModel : ViewModel() {
    private val _ui = MutableStateFlow(UnsolvedEditUiState())
    val ui: StateFlow<UnsolvedEditUiState> = _ui.asStateFlow()

    /** Id whose images are currently observed (add-mode generates the id up-front). */
    private var observedImageId: String? = null

    fun load(id: String?) {
        if (id == null) return
        viewModelScope.launch {
            val e = ServiceLocator.unsolvedRepository.getById(id) ?: return@launch
            _ui.value = UnsolvedEditUiState(
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
                reasonNotSolved = e.reasonNotSolved ?: "",
                personalNotes = e.personalNotes ?: "",
                favorite = e.favorite,
                tags = e.tags,
            )
            observeImages(e.id)
        }
    }

    fun update(transform: (UnsolvedEditUiState) -> UnsolvedEditUiState) {
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
                    QuestionImageRepository.REF_UNSOLVED, questionId, uri,
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
                .observeForQuestion(QuestionImageRepository.REF_UNSOLVED, questionId)
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
            val entity = UnsolvedQuestionEntity(
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
                reasonNotSolved = s.reasonNotSolved.ifBlank { null },
                personalNotes = s.personalNotes.ifBlank { null },
                tags = s.tags,
                favorite = s.favorite,
            )
            if (s.isNew) ServiceLocator.unsolvedRepository.add(entity)
            else {
                val existing = s.id?.let { ServiceLocator.unsolvedRepository.getById(it) }
                if (existing == null) {
                    _ui.value = _ui.value.copy(saving = false, error = "This entry no longer exists.")
                    return@launch
                }
                ServiceLocator.unsolvedRepository.update(entity.copy(
                    addedAt = existing.addedAt,
                    retryCount = existing.retryCount,
                    lastRetriedAt = existing.lastRetriedAt,
                    nextRetryAt = existing.nextRetryAt,
                    status = existing.status,
                    movedToErrorId = existing.movedToErrorId,
                ))
            }
            _ui.value = _ui.value.copy(saving = false, saved = true)
        }
    }
}

data class UnsolvedDetailUiState(
    val entry: UnsolvedQuestionEntity? = null,
    val images: List<QuestionImageEntity> = emptyList(),
    val loaded: Boolean = false,
    val moveToErrorLoading: Boolean = false,
    val movedToErrorId: String? = null,
)

class UnsolvedDetailViewModel : ViewModel() {
    private val _ui = MutableStateFlow(UnsolvedDetailUiState())
    val ui: StateFlow<UnsolvedDetailUiState> = _ui.asStateFlow()

    private var observedId: String? = null

    /** Observes the row live: favorite toggles, status changes and edits refresh instantly. */
    fun load(id: String) {
        if (observedId == id) return
        observedId = id
        viewModelScope.launch {
            ServiceLocator.unsolvedRepository.observeById(id).collect { entry ->
                _ui.value = _ui.value.copy(entry = entry, loaded = true)
            }
        }
        viewModelScope.launch {
            ServiceLocator.questionImageRepository
                .observeForQuestion(QuestionImageRepository.REF_UNSOLVED, id)
                .collect { images -> _ui.value = _ui.value.copy(images = images) }
        }
    }
    fun toggleFavorite() {
        val e = _ui.value.entry ?: return
        viewModelScope.launch { ServiceLocator.unsolvedRepository.setFavorite(e.id, !e.favorite) }
    }
    fun markSolved() {
        val e = _ui.value.entry ?: return
        viewModelScope.launch { ServiceLocator.unsolvedRepository.setStatus(e.id, UnsolvedStatus.SOLVED) }
    }
    fun retryNow() {
        val e = _ui.value.entry ?: return
        viewModelScope.launch { ServiceLocator.unsolvedRepository.setStatus(e.id, UnsolvedStatus.ATTEMPT_AGAIN) }
    }
    fun moveToError(mistakeType: MistakeType = MistakeType.OTHER, attempted: String = "", correct: String = "", explanation: String = "", lesson: String = "") {
        val e = _ui.value.entry ?: return
        _ui.value = _ui.value.copy(moveToErrorLoading = true)
        viewModelScope.launch {
            val errorId = ServiceLocator.unsolvedRepository.moveToErrorBook(
                unsolvedId = e.id,
                mistakeType = mistakeType,
                attemptedSolution = attempted.ifBlank { null },
                correctSolution = correct.ifBlank { null },
                explanation = explanation.ifBlank { null },
                lessonLearned = lesson.ifBlank { null },
            )
            _ui.value = _ui.value.copy(moveToErrorLoading = false, movedToErrorId = errorId?.id)
        }
    }
}
