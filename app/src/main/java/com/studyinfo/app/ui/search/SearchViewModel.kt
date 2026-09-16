package com.studyinfo.app.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studyinfo.app.ServiceLocator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SearchResult(
    val id: String,
    val type: String,            // "error" | "unsolved" | "task"
    val title: String,
    val subtitle: String,
)

data class SearchUiState(
    val query: String = "",
    val results: List<SearchResult> = emptyList(),
    val loading: Boolean = false,
)

class SearchViewModel : ViewModel() {
    private val _ui = MutableStateFlow(SearchUiState())
    val ui: StateFlow<SearchUiState> = _ui.asStateFlow()

    private var searchJob: Job? = null

    fun setQuery(q: String) {
        _ui.value = _ui.value.copy(query = q, loading = q.isNotBlank())
        // Debounce: cancel the in-flight search so rapid typing never shows stale,
        // out-of-order results.
        searchJob?.cancel()
        if (q.isBlank()) {
            _ui.value = _ui.value.copy(results = emptyList(), loading = false)
            return
        }
        searchJob = viewModelScope.launch {
            delay(300)
            val errors = ServiceLocator.errorRepository.search(q).map {
                SearchResult(it.id, "error", it.title.ifBlank { it.questionText.take(40) }, "Error · ${it.subject.displayName}")
            }
            val unsolved = ServiceLocator.unsolvedRepository.search(q).map {
                SearchResult(it.id, "unsolved", it.title.ifBlank { it.questionText.take(40) }, "Unsolved · ${it.source.displayName}")
            }
            _ui.value = _ui.value.copy(results = errors + unsolved, loading = false)
        }
    }
}
