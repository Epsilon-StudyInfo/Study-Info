package com.studyinfo.app.ui.revision

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.studyinfo.app.domain.model.ReviewOutcome
import com.studyinfo.app.ui.components.EmptyStateCard
import com.studyinfo.app.ui.components.StatCard

@Composable
fun RevisionScreen(
    nav: NavHostController,
    vm: RevisionViewModel = viewModel(),
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    Scaffold(topBar = { TopAppBar(title = { Text("Revision") }) }) { padding ->
        if (ui.finished) {
            Column(
                modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text("Session complete", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatCard(title = "Reviewed", value = ui.reviewedCount.toString(), modifier = Modifier.weight(1f))
                    StatCard(title = "Understood", value = ui.understood.toString(), modifier = Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatCard(title = "Needs Revision", value = ui.needsRevision.toString(), modifier = Modifier.weight(1f))
                    StatCard(title = "Confused", value = ui.confused.toString(), modifier = Modifier.weight(1f))
                }
            }
            return@Scaffold
        }
        if (!ui.started) {
            Column(
                modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text("Start a revision session", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
                Text(
                    "We'll walk through your due errors and retry queue one by one. Mark each as understood, needs revision, or still confused.",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatCard(title = "Errors due", value = ui.dueErrors.size.toString(), modifier = Modifier.weight(1f))
                    StatCard(title = "Unsolved due", value = ui.dueUnsolved.size.toString(), modifier = Modifier.weight(1f))
                }
                Button(onClick = { vm.start() }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Text("Start Session")
                }
            }
            return@Scaffold
        }
        // Active session: show the next error to review
        val current = ui.dueErrors.firstOrNull()
        if (current == null) {
            Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("All caught up!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
                Text("You reviewed ${ui.reviewedCount} items this session.", style = MaterialTheme.typography.bodyMedium)
                Button(onClick = { vm.end() }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Text("Finish Session")
                }
            }
            return@Scaffold
        }
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                Text("Reviewed so far: ${ui.reviewedCount}", style = MaterialTheme.typography.labelMedium)
            }
            item {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("${current.subject.displayName} · ${current.mistakeType.label}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                        Text(current.title.ifBlank { current.questionText.take(80) }, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        if (!current.questionText.isBlank()) {
                            Text(current.questionText, style = MaterialTheme.typography.bodyMedium)
                        }
                        if (!current.correctSolution.isNullOrBlank()) {
                            Text("Solution", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                            Text(current.correctSolution, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Button(onClick = { vm.recordError(current.id, ReviewOutcome.UNDERSTOOD) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                        Text("Understood")
                    }
                    FilledTonalButton(onClick = { vm.recordError(current.id, ReviewOutcome.NEEDS_REVISION) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                        Text("Needs Revision")
                    }
                    OutlinedButton(onClick = { vm.recordError(current.id, ReviewOutcome.STILL_CONFUSED) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                        Text("Still Confused")
                    }
                }
            }
        }
    }
}
