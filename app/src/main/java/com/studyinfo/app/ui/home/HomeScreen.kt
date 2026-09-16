package com.studyinfo.app.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.studyinfo.app.ui.components.StatCard

@Composable
fun HomeScreen(
    nav: NavHostController,
    vm: HomeViewModel = viewModel(),
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = "${ui.greeting}${if (ui.userName.isNotBlank()) ", ${ui.userName}" else ""}!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )

            // Quick actions
            QuickActionsRow(
                onAddError = { nav.navigate(Routes.ERROR_ADD) },
                onAddUnsolved = { nav.navigate(Routes.unsolvedAdd()) },
                onAddTask = { nav.navigate(Routes.TASK_ADD) },
                onStartRevision = { nav.navigate(Routes.REVISION) },
            )

            // Stats row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                StatCard(
                    title = "Errors",
                    value = ui.errorsCount.toString(),
                    modifier = Modifier.weight(1f),
                )
                StatCard(
                    title = "Unsolved",
                    value = ui.unsolvedCount.toString(),
                    modifier = Modifier.weight(1f),
                )
                StatCard(
                    title = "Streak",
                    value = "${ui.currentStreak}d",
                    modifier = Modifier.weight(1f),
                )
            }

            // Today's progress
            SectionHeader(
                title = "Today's Progress",
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "${ui.todayCompletedTasks}/${ui.todayTotalTasks} tasks completed",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(Modifier.height(8.dp))
                    ProgressBar(
                        percent = if (ui.todayTotalTasks == 0) 0 else (ui.todayCompletedTasks * 100 / ui.todayTotalTasks),
                        label = "Daily completion",
                    )
                }
            }

            // Subject progress
            SectionHeader(
                title = "Subject Progress",
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    ProgressBar(percent = ui.physicsPercent, label = "Physics")
                    ProgressBar(percent = ui.chemistryPercent, label = "Chemistry")
                    ProgressBar(percent = ui.mathematicsPercent, label = "Mathematics")
                    Divider()
                    ProgressBar(percent = ui.jeeMainPercent, label = "JEE Main")
                    ProgressBar(percent = ui.jeeAdvancedPercent, label = "JEE Advanced")
                }
            }

            // Overdue
            if (ui.overdueTasks.isNotEmpty()) {
                SectionHeader(
                    title = "Overdue",
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                ui.overdueTasks.take(3).forEach { task ->
                    ListItem(
                        headlineContent = { Text(task.title) },
                        supportingContent = { Text("${task.subject?.displayName ?: ""} — due ${java.text.SimpleDateFormat("MMM d", java.util.Locale.US).format(task.dueDate)}") },
                        leadingContent = { Icon(Icons.Filled.Schedule, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                        modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                    )
                }
            }

            if (ui.errorsCount == 0 && ui.unsolvedCount == 0 && ui.todayTotalTasks == 0) {
                EmptyStateCard(
                    title = "Welcome to PrepVault",
                    subtitle = "Add your first error, unsolved question, or task to begin building your revision pipeline.",
                    actionLabel = "Add an error",
                    onAction = { nav.navigate(Routes.ERROR_ADD) },
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun QuickActionsRow(
    onAddError: () -> Unit,
    onAddUnsolved: () -> Unit,
    onAddTask: () -> Unit,
    onStartRevision: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        QuickAction(label = "Add Error", icon = Icons.Filled.Bolt, onClick = onAddError, modifier = Modifier.weight(1f))
        QuickAction(label = "Add Unsolved", icon = Icons.Filled.Add, onClick = onAddUnsolved, modifier = Modifier.weight(1f))
        QuickAction(label = "To-Do", icon = Icons.Filled.Schedule, onClick = onAddTask, modifier = Modifier.weight(1f))
        QuickAction(label = "Revise", icon = Icons.Filled.AutoStories, onClick = onStartRevision, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun QuickAction(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FilledTonalIconButton(
            onClick = onClick,
            modifier = Modifier.size(48.dp),
            shape = RoundedCornerShape(12.dp),
        ) {
            Icon(icon, contentDescription = label)
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}
