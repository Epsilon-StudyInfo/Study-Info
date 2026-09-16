package com.studyinfo.app.ui.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studyinfo.app.ServiceLocator
import com.studyinfo.app.data.database.entity.TaskEntity
import com.studyinfo.app.domain.model.Subject
import com.studyinfo.app.domain.model.TaskPriority
import com.studyinfo.app.domain.model.TaskStatus
import com.studyinfo.app.utils.newId
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

data class TodoListUiState(
    val today: List<TaskEntity> = emptyList(),
    val upcoming: List<TaskEntity> = emptyList(),
    val overdue: List<TaskEntity> = emptyList(),
    val completed: List<TaskEntity> = emptyList(),
    val todayCompleted: Int = 0,
    val todayTotal: Int = 0,
)

class TodoListViewModel : ViewModel() {
    private val _ui = MutableStateFlow(TodoListUiState())
    val ui: StateFlow<TodoListUiState> = _ui.asStateFlow()

    init {
        viewModelScope.launch {
            ServiceLocator.taskRepository.observeForToday().collect { list ->
                _ui.value = _ui.value.copy(
                    today = list.filter { it.status != TaskStatus.COMPLETED && it.status != TaskStatus.SKIPPED },
                    todayCompleted = list.count { it.status == TaskStatus.COMPLETED },
                    todayTotal = list.size,
                )
            }
        }
        viewModelScope.launch {
            ServiceLocator.taskRepository.observeUpcoming().collect { list ->
                _ui.value = _ui.value.copy(upcoming = list.filter { it.status != TaskStatus.COMPLETED })
            }
        }
        viewModelScope.launch {
            ServiceLocator.taskRepository.observeOverdue().collect { list ->
                _ui.value = _ui.value.copy(overdue = list)
            }
        }
        viewModelScope.launch {
            ServiceLocator.taskRepository.observeByStatus(TaskStatus.COMPLETED).collect { list ->
                _ui.value = _ui.value.copy(completed = list)
            }
        }
    }

    fun toggleStatus(task: TaskEntity) {
        val newStatus = if (task.status == TaskStatus.COMPLETED) TaskStatus.PENDING else TaskStatus.COMPLETED
        viewModelScope.launch { ServiceLocator.taskRepository.setStatus(task.id, newStatus) }
    }
    fun delete(id: String) { viewModelScope.launch { ServiceLocator.taskRepository.delete(id) } }
}

data class TaskEditUiState(
    val id: String? = null,
    val title: String = "",
    val description: String = "",
    val subject: Subject? = Subject.PHYSICS,
    val chapterName: String = "",
    val topic: String = "",
    val dueDate: Date = Date(),
    val dueTime: Date? = null,
    val estimatedMinutes: String = "",
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val status: TaskStatus = TaskStatus.PENDING,
    val recurrence: String? = null,
    val notes: String = "",
    val saving: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null,
)

class TaskEditViewModel : ViewModel() {
    private val _ui = MutableStateFlow(TaskEditUiState())
    val ui: StateFlow<TaskEditUiState> = _ui.asStateFlow()

    fun load(id: String?) {
        if (id == null) return
        viewModelScope.launch {
            val t = ServiceLocator.taskRepository.getById(id) ?: return@launch
            _ui.value = TaskEditUiState(
                id = t.id,
                title = t.title,
                description = t.description ?: "",
                subject = t.subject,
                chapterName = t.chapterName ?: "",
                topic = t.topic ?: "",
                dueDate = t.dueDate,
                dueTime = t.dueTime,
                estimatedMinutes = t.estimatedMinutes?.toString() ?: "",
                priority = t.priority,
                status = t.status,
                recurrence = t.recurrence,
                notes = t.notes ?: "",
            )
        }
    }

    fun update(transform: (TaskEditUiState) -> TaskEditUiState) { _ui.value = transform(_ui.value) }

    fun save() {
        val s = _ui.value
        if (s.saving || s.saved) return
        if (s.title.isBlank()) {
            _ui.value = s.copy(error = "Title is required.")
            return
        }
        _ui.value = s.copy(saving = true, error = null)
        viewModelScope.launch {
            val entity = TaskEntity(
                id = s.id ?: newId(),
                title = s.title,
                description = s.description.ifBlank { null },
                subject = s.subject,
                chapterName = s.chapterName.ifBlank { null },
                topic = s.topic.ifBlank { null },
                dueDate = s.dueDate,
                dueTime = s.dueTime,
                estimatedMinutes = s.estimatedMinutes.toIntOrNull(),
                priority = s.priority,
                status = s.status,
                recurrence = s.recurrence,
                notes = s.notes.ifBlank { null },
            )
            if (s.id == null) ServiceLocator.taskRepository.add(entity)
            else ServiceLocator.taskRepository.update(entity.copy(
                createdAt = ServiceLocator.taskRepository.getById(s.id)?.createdAt ?: Date(),
            ))
            _ui.value = _ui.value.copy(saving = false, saved = true)
        }
    }
}
