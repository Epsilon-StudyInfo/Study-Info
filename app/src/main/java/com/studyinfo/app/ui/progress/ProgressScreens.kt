package com.studyinfo.app.ui.progress

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.studyinfo.app.domain.model.ExamType
import com.studyinfo.app.domain.model.Subject
import com.studyinfo.app.navigation.Routes
import com.studyinfo.app.ui.components.ProgressBar
import com.studyinfo.app.ui.components.SectionHeader

@Composable
fun ProgressScreen(
    nav: NavHostController,
    vm: ProgressViewModel = viewModel(),
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    Scaffold(topBar = { TopAppBar(title = { Text("Progress") }) }) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("JEE Main", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        ProgressBar(percent = ui.jeeMainAverage)
                        Text("JEE Advanced", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        ProgressBar(percent = ui.jeeAdvancedAverage)
                    }
                }
            }
            item { SectionHeader(title = "Subjects") }
            items(Subject.entries.filter { it != Subject.OTHER }) { subject ->
                val percent = when (subject) {
                    Subject.PHYSICS -> ui.physicsAverage
                    Subject.CHEMISTRY -> ui.chemistryAverage
                    Subject.MATHEMATICS -> ui.mathematicsAverage
                    else -> 0
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    onClick = { nav.navigate(Routes.subjectProgress(subject.name)) },
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(subject.displayName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(8.dp))
                        ProgressBar(percent = percent)
                    }
                }
            }
        }
    }
}

@Composable
fun SubjectProgressScreen(
    nav: NavHostController,
    subjectName: String,
    vm: SubjectProgressViewModel = viewModel(),
) {
    val subject = Subject.fromName(subjectName)
    LaunchedEffect(subjectName) { vm.load(subject) }
    val ui by vm.ui.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${subject.displayName} Progress") },
                navigationIcon = {
                    TextButton(onClick = { nav.popBackStack() }) { Text("Back") }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(ui.chapters, key = { it.id }) { chapter ->
                val progress = ui.progressByChapter[chapter.id]
                var sliderValue by remember(chapter.id) {
                    mutableStateOf(progress?.percent?.toFloat() ?: 0f)
                }
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(chapter.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("${sliderValue.toInt()}%", style = MaterialTheme.typography.labelMedium, modifier = Modifier.width(48.dp))
                            Slider(
                                value = sliderValue,
                                onValueChange = { sliderValue = it },
                                onValueChangeFinished = {
                                    vm.setPercent(chapter.id, subject, ExamType.JEE_MAIN, sliderValue.toInt())
                                },
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }
        }
    }
}
