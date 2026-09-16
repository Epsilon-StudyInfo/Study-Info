package com.studyinfo.app.ui.todo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.studyinfo.app.domain.model.Subject
import com.studyinfo.app.domain.model.TaskPriority
import com.studyinfo.app.ui.errors.DropdownField
import java.util.Calendar
import java.util.Date

@Composable
fun TaskEditScreen(
    nav: NavHostController,
    taskId: String?,
    vm: TaskEditViewModel = viewModel(),
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    LaunchedEffect(taskId) { vm.load(taskId) }
    LaunchedEffect(ui.saved) { if (ui.saved) nav.popBackStack() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (taskId == null) "Add Task" else "Edit Task") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { vm.save() }, enabled = !ui.saving) {
                        Icon(Icons.Filled.Save, contentDescription = "Save")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            OutlinedTextField(
                value = ui.title,
                onValueChange = { v -> vm.update { it.copy(title = v) } },
                label = { Text("Title *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = ui.description,
                onValueChange = { v -> vm.update { it.copy(description = v) } },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth().heightIn(min = 80.dp),
            )
            DropdownField(
                label = "Subject",
                value = ui.subject?.displayName ?: "—",
                options = listOf("—") + Subject.entries.map { it.displayName },
                onSelected = { name -> vm.update { it.copy(subject = if (name == "—") null else Subject.fromName(name)) } },
            )
            OutlinedTextField(
                value = ui.chapterName,
                onValueChange = { v -> vm.update { it.copy(chapterName = v) } },
                label = { Text("Chapter") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = ui.topic,
                onValueChange = { v -> vm.update { it.copy(topic = v) } },
                label = { Text("Topic") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            // Due date pickers (simple)
            val duePicker = remember { mutableStateOf(ui.dueDate) }
            val cal = remember { Calendar.getInstance().apply { time = duePicker.value } }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = "${cal.get(Calendar.YEAR)}-${(cal.get(Calendar.MONTH) + 1).toString().padStart(2, '0')}-${cal.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')}",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Due date") },
                    modifier = Modifier.weight(1f),
                )
                Button(
                    onClick = {
                        cal.add(Calendar.DAY_OF_MONTH, 1)
                        vm.update { it.copy(dueDate = Date(cal.timeInMillis)) }
                    },
                    modifier = Modifier.weight(0.4f),
                ) { Text("+1d") }
            }
            OutlinedTextField(
                value = ui.estimatedMinutes,
                onValueChange = { v -> vm.update { it.copy(estimatedMinutes = v.filter { c -> c.isDigit() }) } },
                label = { Text("Estimated minutes") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            DropdownField(
                label = "Priority",
                value = ui.priority.label,
                options = TaskPriority.entries.map { it.label },
                onSelected = { l -> vm.update { it.copy(priority = TaskPriority.fromLabel(l)) } },
            )
            DropdownField(
                label = "Recurrence",
                value = ui.recurrence ?: "None",
                options = listOf("None", "DAILY", "WEEKLY"),
                onSelected = { v -> vm.update { it.copy(recurrence = if (v == "None") null else v) } },
            )
            OutlinedTextField(
                value = ui.notes,
                onValueChange = { v -> vm.update { it.copy(notes = v) } },
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth().heightIn(min = 80.dp),
            )

            if (ui.error != null) {
                Text(ui.error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Button(
                onClick = { vm.save() },
                enabled = !ui.saving,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
            ) {
                if (ui.saving) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                } else {
                    Text("Save Task")
                }
            }
        }
    }
}
