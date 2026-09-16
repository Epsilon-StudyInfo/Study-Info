package com.studyinfo.app.ui.manage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.studyinfo.app.domain.model.Subject

@Composable
fun ManageSourcesScreen(
    nav: NavHostController,
    vm: ManageSourcesViewModel = viewModel(),
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    var newName by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Custom Sources") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("New source name") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                Button(
                    onClick = {
                        if (newName.isNotBlank()) { vm.add(newName); newName = "" }
                    },
                    modifier = Modifier.padding(start = 8.dp),
                ) { Text("Add") }
            }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                items(ui.items, key = { it.id }) { source ->
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Text(source.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                            IconButton(onClick = { vm.delete(source.id) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ManageChaptersScreen(
    nav: NavHostController,
    vm: ManageChaptersViewModel = viewModel(),
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    var newSubject by remember { mutableStateOf(Subject.PHYSICS) }
    var newName by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chapters") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row {
                com.studyinfo.app.ui.errors.DropdownField(
                    label = "Subject",
                    value = newSubject.displayName,
                    options = Subject.entries.filter { it != Subject.OTHER }.map { it.displayName },
                    onSelected = { newSubject = Subject.fromName(it) },
                    modifier = Modifier.weight(0.4f),
                )
                Spacer(Modifier.width(8.dp))
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("New chapter") },
                    singleLine = true,
                    modifier = Modifier.weight(0.6f),
                )
            }
            Button(
                onClick = {
                    if (newName.isNotBlank()) { vm.add(newSubject, newName); newName = "" }
                },
                modifier = Modifier.fillMaxWidth(),
            ) { Text("Add Chapter") }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                items(ui.items, key = { it.id }) { chapter ->
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Text("${chapter.subject} · ${chapter.name}", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                            IconButton(onClick = { vm.delete(chapter.id) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ManageTagsScreen(
    nav: NavHostController,
    vm: ManageTagsViewModel = viewModel(),
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    var newName by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tags") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("New tag") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                Button(
                    onClick = {
                        if (newName.isNotBlank()) { vm.add(newName); newName = "" }
                    },
                    modifier = Modifier.padding(start = 8.dp),
                ) { Text("Add") }
            }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                items(ui.items, key = { it.id }) { tag ->
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Text(tag.name, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                            IconButton(onClick = { vm.delete(tag.id) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}
