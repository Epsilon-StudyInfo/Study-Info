package com.studyinfo.app.ui.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studyinfo.app.ServiceLocator
import com.studyinfo.app.data.database.entity.ChapterEntity
import com.studyinfo.app.data.database.entity.CustomSourceEntity
import com.studyinfo.app.data.database.entity.TagEntity
import com.studyinfo.app.domain.model.Subject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ManageSourcesUiState(val items: List<CustomSourceEntity> = emptyList())
class ManageSourcesViewModel : ViewModel() {
    private val _ui = MutableStateFlow(ManageSourcesUiState())
    val ui: StateFlow<ManageSourcesUiState> = _ui.asStateFlow()
    init {
        viewModelScope.launch {
            ServiceLocator.customSourceRepository.observeAll().collect { _ui.value = ManageSourcesUiState(it) }
        }
    }
    fun add(name: String) { viewModelScope.launch { ServiceLocator.customSourceRepository.create(name) } }
    fun rename(id: String, name: String) { viewModelScope.launch { ServiceLocator.customSourceRepository.rename(id, name) } }
    fun delete(id: String) { viewModelScope.launch { ServiceLocator.customSourceRepository.delete(id) } }
}

data class ManageChaptersUiState(val items: List<ChapterEntity> = emptyList())
class ManageChaptersViewModel : ViewModel() {
    private val _ui = MutableStateFlow(ManageChaptersUiState())
    val ui: StateFlow<ManageChaptersUiState> = _ui.asStateFlow()
    init {
        viewModelScope.launch {
            ServiceLocator.chapterRepository.observeAll().collect { _ui.value = ManageChaptersUiState(it) }
        }
    }
    fun add(subject: Subject, name: String) { viewModelScope.launch { ServiceLocator.chapterRepository.addCustomChapter(subject, name) } }
    fun rename(id: String, name: String) { viewModelScope.launch { ServiceLocator.chapterRepository.renameChapter(id, name) } }
    fun delete(id: String) { viewModelScope.launch { ServiceLocator.chapterRepository.deleteChapter(id) } }
}

data class ManageTagsUiState(val items: List<TagEntity> = emptyList())
class ManageTagsViewModel : ViewModel() {
    private val _ui = MutableStateFlow(ManageTagsUiState())
    val ui: StateFlow<ManageTagsUiState> = _ui.asStateFlow()
    init {
        viewModelScope.launch {
            ServiceLocator.tagRepository.observeAll().collect { _ui.value = ManageTagsUiState(it) }
        }
    }
    fun add(name: String) { viewModelScope.launch { ServiceLocator.tagRepository.create(name) } }
    fun rename(id: String, name: String) { viewModelScope.launch { ServiceLocator.tagRepository.rename(id, name) } }
    fun delete(id: String) { viewModelScope.launch { ServiceLocator.tagRepository.delete(id) } }
}
