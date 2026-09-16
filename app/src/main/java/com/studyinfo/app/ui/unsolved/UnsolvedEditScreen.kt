package com.studyinfo.app.ui.unsolved

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
import com.studyinfo.app.domain.model.*
import com.studyinfo.app.ui.components.QuestionImageAttachments
import com.studyinfo.app.ui.errors.DropdownField

@Composable
fun UnsolvedEditScreen(
    nav: NavHostController,
    unsolvedId: String?,
    initialSource: QuestionSource? = null,
    vm: UnsolvedEditViewModel = viewModel(),
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    LaunchedEffect(unsolvedId) { vm.load(unsolvedId) }
    LaunchedEffect(initialSource) {
        if (unsolvedId == null && initialSource != null) {
            vm.update { it.copy(source = initialSource) }
        }
    }
    LaunchedEffect(ui.saved) { if (ui.saved) nav.popBackStack() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (unsolvedId == null) "Add Unsolved" else "Edit Unsolved") },
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
            OutlinedTextField(value = ui.title, onValueChange = { v -> vm.update { it.copy(title = v) } }, label = { Text("Title (optional)") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(
                value = ui.questionText,
                onValueChange = { v -> vm.update { it.copy(questionText = v) } },
                label = { Text("Question") },
                supportingText = { Text("Type the question — or attach an image below instead.") },
                modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp),
            )

            QuestionImageAttachments(
                images = ui.images,
                enabled = !ui.saving,
                onPicked = vm::addImages,
                onRemove = vm::removeImage,
            )
            if (ui.imageBusy) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            DropdownField(label = "Subject", value = ui.subject.displayName, options = Subject.entries.map { it.displayName }, onSelected = { name -> vm.update { it.copy(subject = Subject.fromName(name)) } })
            OutlinedTextField(value = ui.chapterName, onValueChange = { v -> vm.update { it.copy(chapterName = v) } }, label = { Text("Chapter") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = ui.topic, onValueChange = { v -> vm.update { it.copy(topic = v) } }, label = { Text("Topic") }, singleLine = true, modifier = Modifier.fillMaxWidth())

            DropdownField(label = "Source", value = ui.source.displayName, options = QuestionSource.entries.map { it.displayName }, onSelected = { name -> vm.update { it.copy(source = QuestionSource.fromLabel(name)) } })

            when (ui.source) {
                QuestionSource.DPP -> {
                    OutlinedTextField(value = ui.dppNumber, onValueChange = { v -> vm.update { it.copy(dppNumber = v) } }, label = { Text("DPP number") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = ui.chapterNumber, onValueChange = { v -> vm.update { it.copy(chapterNumber = v) } }, label = { Text("Chapter number") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                }
                QuestionSource.PYQ -> {
                    DropdownField(label = "Exam", value = ui.examType.label, options = ExamType.entries.map { it.label }, onSelected = { l -> vm.update { it.copy(examType = ExamType.fromLabel(l)) } })
                    OutlinedTextField(value = ui.pyqYear, onValueChange = { v -> vm.update { it.copy(pyqYear = v.filter { c -> c.isDigit() }) } }, label = { Text("Year") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = ui.shiftSession, onValueChange = { v -> vm.update { it.copy(shiftSession = v) } }, label = { Text("Shift / Session") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = ui.chapterNumber, onValueChange = { v -> vm.update { it.copy(chapterNumber = v) } }, label = { Text("Chapter number") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                }
                QuestionSource.MODULE -> {
                    OutlinedTextField(value = ui.instituteName, onValueChange = { v -> vm.update { it.copy(instituteName = v) } }, label = { Text("Institute") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = ui.moduleNumber, onValueChange = { v -> vm.update { it.copy(moduleNumber = v) } }, label = { Text("Module number") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = ui.exercise, onValueChange = { v -> vm.update { it.copy(exercise = v) } }, label = { Text("Exercise") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                }
                QuestionSource.MOCK_TEST -> {
                    OutlinedTextField(value = ui.testName, onValueChange = { v -> vm.update { it.copy(testName = v) } }, label = { Text("Test name") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = ui.testNumber, onValueChange = { v -> vm.update { it.copy(testNumber = v) } }, label = { Text("Test number") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                }
                QuestionSource.BOOK -> {
                    OutlinedTextField(value = ui.bookName, onValueChange = { v -> vm.update { it.copy(bookName = v) } }, label = { Text("Book name") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = ui.exercise, onValueChange = { v -> vm.update { it.copy(exercise = v) } }, label = { Text("Exercise") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                }
                QuestionSource.CUSTOM -> {
                    OutlinedTextField(value = ui.customSourceName, onValueChange = { v -> vm.update { it.copy(customSourceName = v) } }, label = { Text("Custom source") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                }
            }
            OutlinedTextField(value = ui.questionNumber, onValueChange = { v -> vm.update { it.copy(questionNumber = v) } }, label = { Text("Question number") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            DropdownField(label = "Difficulty", value = ui.difficulty.label, options = Difficulty.entries.map { it.label }, onSelected = { l -> vm.update { it.copy(difficulty = Difficulty.fromLabel(l)) } })
            OutlinedTextField(value = ui.reasonNotSolved, onValueChange = { v -> vm.update { it.copy(reasonNotSolved = v) } }, label = { Text("Reason not solved") }, modifier = Modifier.fillMaxWidth().heightIn(min = 80.dp))
            OutlinedTextField(value = ui.personalNotes, onValueChange = { v -> vm.update { it.copy(personalNotes = v) } }, label = { Text("Personal notes") }, modifier = Modifier.fillMaxWidth().heightIn(min = 80.dp))

            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Checkbox(checked = ui.favorite, onCheckedChange = { v -> vm.update { it.copy(favorite = v) } })
                Text("Mark as important")
            }

            if (ui.error != null) {
                Text(ui.error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Button(onClick = { vm.save() }, enabled = !ui.saving, modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(12.dp)) {
                if (ui.saving) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                } else {
                    Text("Save Unsolved")
                }
            }
        }
    }
}
