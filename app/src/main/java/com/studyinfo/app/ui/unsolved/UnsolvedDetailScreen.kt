package com.studyinfo.app.ui.unsolved

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.studyinfo.app.domain.model.MistakeType
import com.studyinfo.app.domain.model.UnsolvedStatus
import com.studyinfo.app.navigation.Routes
import com.studyinfo.app.ui.components.QuestionImageGallery

@Composable
fun UnsolvedDetailScreen(
    nav: NavHostController,
    unsolvedId: String,
    vm: UnsolvedDetailViewModel = viewModel(),
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    LaunchedEffect(unsolvedId) { vm.load(unsolvedId) }
    LaunchedEffect(ui.movedToErrorId) {
        ui.movedToErrorId?.let { errorId ->
            nav.navigate(Routes.errorDetail(errorId)) {
                popUpTo(Routes.UNSOLVED_LIST) { inclusive = false }
            }
        }
    }
    val entry = ui.entry
    var showMoveDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Unsolved Details") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (entry != null) {
                        IconButton(onClick = { vm.toggleFavorite() }) {
                            Icon(
                                if (entry.favorite) Icons.Filled.Star else Icons.Filled.StarBorder,
                                contentDescription = "Favorite",
                                tint = if (entry.favorite) MaterialTheme.colorScheme.primary
                                       else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            val e = entry
            if (e != null) {
                ExtendedFloatingActionButton(
                    onClick = { nav.navigate(Routes.unsolvedEdit(e.id)) },
                    icon = { Icon(Icons.Filled.Edit, contentDescription = null) },
                    text = { Text("Edit") },
                )
            }
        },
    ) { padding ->
        if (entry == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                if (ui.loaded) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("This question no longer exists.", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        TextButton(onClick = { nav.popBackStack() }) { Text("Go back") }
                    }
                } else {
                    CircularProgressIndicator()
                }
            }
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(entry.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
            AssistChip(onClick = {}, label = { Text("${entry.subject.displayName} · ${entry.source.displayName} · ${entry.status.label}") })
            if (entry.chapterName != null) InfoLine("Chapter", entry.chapterName)
            if (entry.topic != null) InfoLine("Topic", entry.topic)
            if (entry.questionNumber != null) InfoLine("Q No.", entry.questionNumber)
            InfoLine("Difficulty", entry.difficulty.label)
            HorizontalDivider()
            Text("Question", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            if (entry.questionText.isNotBlank()) {
                Text(entry.questionText, style = MaterialTheme.typography.bodyLarge)
            }
            QuestionImageGallery(images = ui.images)
            if (!entry.reasonNotSolved.isNullOrBlank()) {
                Text("Reason not solved", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text(entry.reasonNotSolved, style = MaterialTheme.typography.bodyLarge)
            }
            if (!entry.personalNotes.isNullOrBlank()) {
                Text("Notes", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text(entry.personalNotes, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (entry.status != UnsolvedStatus.SOLVED) {
                    Button(
                        onClick = { vm.markSolved() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                    ) { Text("Mark Solved") }
                }
                FilledTonalButton(
                    onClick = { vm.retryNow() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                ) { Text("Retry Now") }
            }
            OutlinedButton(
                onClick = { showMoveDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = !ui.moveToErrorLoading,
            ) {
                Icon(Icons.Filled.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text(if (ui.moveToErrorLoading) "Moving..." else "Move to Error Book")
            }
        }
    }

    if (showMoveDialog) {
        MoveToErrorDialog(
            onDismiss = { showMoveDialog = false },
            onConfirm = { mistakeType, attempted, correct, explanation, lesson ->
                vm.moveToError(mistakeType, attempted, correct, explanation, lesson)
                showMoveDialog = false
            },
        )
    }
}

@Composable
private fun InfoLine(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun MoveToErrorDialog(
    onDismiss: () -> Unit,
    onConfirm: (MistakeType, String, String, String, String) -> Unit,
) {
    var mistakeType by remember { mutableStateOf(MistakeType.OTHER) }
    var attempted by remember { mutableStateOf("") }
    var correct by remember { mutableStateOf("") }
    var explanation by remember { mutableStateOf("") }
    var lesson by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Move to Error Book") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Mistake type", style = MaterialTheme.typography.labelMedium)
                com.studyinfo.app.ui.errors.DropdownField(
                    label = "Mistake Type",
                    value = mistakeType.label,
                    options = MistakeType.entries.map { it.label },
                    onSelected = { l -> mistakeType = MistakeType.fromLabel(l) },
                )
                OutlinedTextField(value = attempted, onValueChange = { attempted = it }, label = { Text("Attempted solution") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = correct, onValueChange = { correct = it }, label = { Text("Correct solution") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = explanation, onValueChange = { explanation = it }, label = { Text("Explanation") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = lesson, onValueChange = { lesson = it }, label = { Text("Lesson learned") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(mistakeType, attempted, correct, explanation, lesson) }) {
                Text("Move")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}
