package com.studyinfo.app.ui.todo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.studyinfo.app.R
import com.studyinfo.app.navigation.Routes
import com.studyinfo.app.ui.components.EmptyStateCard
import com.studyinfo.app.ui.components.ProgressBar
import com.studyinfo.app.ui.components.SectionHeader

@Composable
fun TodoListScreen(
    nav: NavHostController,
    vm: TodoListViewModel = viewModel(),
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResourceSafe(R.string.todo_title)) }) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { nav.navigate(Routes.TASK_ADD) },
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("New Task") },
            )
        },
    ) { padding ->
        if (ui.today.isEmpty() && ui.overdue.isEmpty() && ui.upcoming.isEmpty() && ui.completed.isEmpty()) {
            EmptyStateCard(
                title = "No tasks for today",
                subtitle = "Plan your study day — add tasks, set their subject and priority, and track completion.",
                actionLabel = "Add a task",
                onAction = { nav.navigate(Routes.TASK_ADD) },
            )
            return@Scaffold
        }
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (ui.todayTotal > 0) {
                item {
                    SectionHeader(title = stringResourceSafe(R.string.todo_today))
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("${ui.todayCompleted}/${ui.todayTotal} completed", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            ProgressBar(percent = if (ui.todayTotal == 0) 0 else ui.todayCompleted * 100 / ui.todayTotal)
                        }
                    }
                }
                items(ui.today, key = { "today-${it.id}" }) { task -> TaskRow(task, overdue = false, onToggle = vm::toggleStatus, onDelete = vm::delete, onClick = { nav.navigate(Routes.taskEdit(task.id)) }) }
            }
            if (ui.overdue.isNotEmpty()) {
                item { SectionHeader(title = stringResourceSafe(R.string.todo_overdue)) }
                items(ui.overdue, key = { "over-${it.id}" }) { task -> TaskRow(task, overdue = true, onToggle = vm::toggleStatus, onDelete = vm::delete, onClick = { nav.navigate(Routes.taskEdit(task.id)) }) }
            }
            if (ui.upcoming.isNotEmpty()) {
                item { SectionHeader(title = stringResourceSafe(R.string.todo_upcoming)) }
                items(ui.upcoming, key = { "up-${it.id}" }) { task -> TaskRow(task, overdue = false, onToggle = vm::toggleStatus, onDelete = vm::delete, onClick = { nav.navigate(Routes.taskEdit(task.id)) }) }
            }
            if (ui.completed.isNotEmpty()) {
                item { SectionHeader(title = stringResourceSafe(R.string.todo_completed)) }
                items(ui.completed.take(15), key = { "done-${it.id}" }) { task -> TaskRow(task, overdue = false, onToggle = vm::toggleStatus, onDelete = vm::delete, onClick = { nav.navigate(Routes.taskEdit(task.id)) }) }
            }
        }
    }
}

@Composable
private fun TaskRow(
    task: com.studyinfo.app.data.database.entity.TaskEntity,
    overdue: Boolean,
    onToggle: (com.studyinfo.app.data.database.entity.TaskEntity) -> Unit,
    onDelete: (String) -> Unit,
    onClick: () -> Unit,
) {
    val titleColor = if (overdue) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), onClick = onClick) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = task.status == com.studyinfo.app.domain.model.TaskStatus.COMPLETED,
                onCheckedChange = { onToggle(task) },
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(task.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = titleColor)
                Text(
                    listOfNotNull(
                        task.subject?.displayName,
                        task.chapterName,
                        task.priority.label,
                        dueLabel(task),
                    ).joinToString(" · "),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (overdue) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            IconButton(onClick = { onDelete(task.id) }) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

/**
 * Human due label: "Today" / "Tomorrow" / "Yesterday" / "MMM d" (+ time when set),
 * so the list reads at a glance instead of making the user decode raw dates.
 */
private fun dueLabel(task: com.studyinfo.app.data.database.entity.TaskEntity): String {
    val today = java.util.Calendar.getInstance().apply {
        set(java.util.Calendar.HOUR_OF_DAY, 0); set(java.util.Calendar.MINUTE, 0)
        set(java.util.Calendar.SECOND, 0); set(java.util.Calendar.MILLISECOND, 0)
    }.timeInMillis
    val dayMs = 24L * 60L * 60L * 1000L
    val dueDay = task.dueDate.time
    val datePart = when {
        dueDay >= today && dueDay < today + dayMs -> "Today"
        dueDay >= today + dayMs && dueDay < today + 2 * dayMs -> "Tomorrow"
        dueDay >= today - dayMs && dueDay < today -> "Yesterday"
        else -> java.text.SimpleDateFormat("MMM d", java.util.Locale.US).format(task.dueDate)
    }
    val timePart = task.dueTime?.let {
        java.text.SimpleDateFormat("h:mm a", java.util.Locale.US).format(it)
    }
    return if (timePart != null) "$datePart · $timePart" else datePart
}

@Composable
private fun stringResourceSafe(id: Int): String = androidx.compose.ui.res.stringResource(id)
