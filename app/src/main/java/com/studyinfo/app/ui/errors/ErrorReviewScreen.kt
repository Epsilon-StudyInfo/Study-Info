package com.studyinfo.app.ui.errors

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.studyinfo.app.domain.model.ReviewOutcome

@Composable
fun ErrorReviewScreen(
    nav: NavHostController,
    errorId: String,
    vm: ErrorReviewViewModel = viewModel(),
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    LaunchedEffect(errorId) { vm.load(errorId) }
    LaunchedEffect(ui.saved) { if (ui.saved) nav.popBackStack() }

    val e = ui.error
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Review") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        if (e == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Question", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                    Text(e.questionText, style = MaterialTheme.typography.bodyLarge)
                }
            }

            Text("Reveal the solution, then choose how you feel about it.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

            if (!e.correctSolution.isNullOrBlank()) {
                Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Solution", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                        Text(e.correctSolution, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            Text("Reflection (optional)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            OutlinedTextField(
                value = ui.reflection,
                onValueChange = vm::setReflection,
                label = { Text("What did I do wrong? What should I remember?") },
                modifier = Modifier.fillMaxWidth().heightIn(min = 80.dp),
            )

            Text("Schedule next review", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                val options = listOf(1 to "Tomorrow", 3 to "3d", 7 to "7d", 14 to "14d", 30 to "30d")
                options.forEach { (days, label) ->
                    FilterChip(
                        selected = ui.scheduleDays == days,
                        onClick = { vm.setScheduleDays(days) },
                        label = { Text(label) },
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Text("Outcome", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { vm.submit(ReviewOutcome.UNDERSTOOD) },
                    enabled = !ui.saving,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                ) { Text("Understood") }
                FilledTonalButton(
                    onClick = { vm.submit(ReviewOutcome.NEEDS_REVISION) },
                    enabled = !ui.saving,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                ) { Text("Needs Revision") }
                OutlinedButton(
                    onClick = { vm.submit(ReviewOutcome.STILL_CONFUSED) },
                    enabled = !ui.saving,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                ) { Text("Still Confused") }
            }
        }
    }
}
