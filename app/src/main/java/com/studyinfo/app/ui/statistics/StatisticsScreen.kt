package com.studyinfo.app.ui.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.studyinfo.app.ui.components.ProgressBar
import com.studyinfo.app.ui.components.SectionHeader
import com.studyinfo.app.ui.components.StatCard

@Composable
fun StatisticsScreen(
    nav: NavHostController,
    vm: StatisticsViewModel = viewModel(),
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    Scaffold(topBar = { TopAppBar(title = { Text("Statistics") }) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                StatCard(title = "Errors", value = ui.totalErrors.toString(), modifier = Modifier.weight(1f))
                StatCard(title = "Unsolved", value = ui.totalUnsolved.toString(), modifier = Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                StatCard(title = "Solved", value = ui.totalSolved.toString(), modifier = Modifier.weight(1f))
                StatCard(title = "Tasks Done", value = ui.totalTasksCompleted.toString(), modifier = Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                StatCard(title = "Revisions", value = ui.revisionSessions.toString(), modifier = Modifier.weight(1f))
                StatCard(title = "Streak", value = "${ui.currentStreak}d / ${ui.longestStreak}d", modifier = Modifier.weight(1f))
            }

            SectionHeader(title = "Errors by Subject")
            val maxSubject = ui.errorsBySubject.values.maxOrNull() ?: 1
            ui.errorsBySubject.forEach { (subject, count) ->
                ProgressBar(percent = if (maxSubject == 0) 0 else count * 100 / maxSubject, label = "${subject.displayName} ($count)")
            }

            SectionHeader(title = "Errors by Mistake Type")
            val maxMistake = ui.errorsByMistake.values.maxOrNull() ?: 1
            ui.errorsByMistake.forEach { (mistake, count) ->
                ProgressBar(percent = if (maxMistake == 0) 0 else count * 100 / maxMistake, label = "${mistake.label} ($count)")
            }

            SectionHeader(title = "Unsolved by Source")
            val maxSource = ui.unsolvedBySource.values.maxOrNull() ?: 1
            ui.unsolvedBySource.forEach { (source, count) ->
                ProgressBar(percent = if (maxSource == 0) 0 else count * 100 / maxSource, label = "${source.displayName} ($count)")
            }
        }
    }
}
