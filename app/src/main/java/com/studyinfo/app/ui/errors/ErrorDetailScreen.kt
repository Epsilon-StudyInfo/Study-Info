package com.studyinfo.app.ui.errors

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.studyinfo.app.ServiceLocator
import com.studyinfo.app.data.database.entity.ErrorEntryEntity
import com.studyinfo.app.data.database.entity.QuestionImageEntity
import com.studyinfo.app.data.repository.QuestionImageRepository
import com.studyinfo.app.navigation.Routes
import com.studyinfo.app.ui.components.QuestionImageGallery
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ErrorDetailUiState(
    val entry: ErrorEntryEntity? = null,
    val images: List<QuestionImageEntity> = emptyList(),
    val loaded: Boolean = false,
)

class ErrorDetailViewModel : ViewModel() {
    private val _ui = MutableStateFlow(ErrorDetailUiState())
    val ui: StateFlow<ErrorDetailUiState> = _ui.asStateFlow()

    private var observedId: String? = null

    /** Observes the row live so favorite / status / review updates render instantly. */
    fun load(id: String) {
        if (observedId == id) return
        observedId = id
        viewModelScope.launch {
            ServiceLocator.errorRepository.observeById(id).collect { entry ->
                _ui.value = _ui.value.copy(entry = entry, loaded = true)
            }
        }
        viewModelScope.launch {
            ServiceLocator.questionImageRepository
                .observeForQuestion(QuestionImageRepository.REF_ERROR, id)
                .collect { images -> _ui.value = _ui.value.copy(images = images) }
        }
    }
    fun toggleFavorite() {
        val e = _ui.value.entry ?: return
        viewModelScope.launch { ServiceLocator.errorRepository.setFavorite(e.id, !e.favorite) }
    }
}

@Composable
fun ErrorDetailScreen(
    nav: NavHostController,
    errorId: String,
    vm: ErrorDetailViewModel = viewModel(),
) {
    val uiState by vm.ui.collectAsStateWithLifecycle()
    val error = uiState.entry
    LaunchedEffect(errorId) { vm.load(errorId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Error Details") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    val e = error
                    if (e != null) {
                        IconButton(onClick = { vm.toggleFavorite() }) {
                            Icon(
                                if (e.favorite) Icons.Filled.Star else Icons.Filled.StarBorder,
                                contentDescription = "Favorite",
                                tint = if (e.favorite) MaterialTheme.colorScheme.primary
                                       else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            val e = error
            if (e != null) {
                ExtendedFloatingActionButton(
                    onClick = { nav.navigate(Routes.errorEdit(e.id)) },
                    icon = { Icon(Icons.Filled.Edit, contentDescription = null) },
                    text = { Text("Edit") },
                )
            }
        },
    ) { padding ->
        val e = error
        if (e == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                if (uiState.loaded) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("This entry no longer exists.", style = MaterialTheme.typography.titleMedium)
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
            Text(e.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
            AssistChip(onClick = {}, label = { Text("${e.subject.displayName} · ${e.source.displayName}") })
            if (e.chapterName != null) InfoLine("Chapter", e.chapterName)
            if (e.topic != null) InfoLine("Topic", e.topic)
            if (e.questionNumber != null) InfoLine("Q No.", e.questionNumber)
            InfoLine("Difficulty", e.difficulty.label)
            InfoLine("Mistake Type", e.mistakeType.label)
            InfoLine("Status", e.status.label)
            HorizontalDivider()

            SectionLabel("Question")
            if (e.questionText.isNotBlank()) {
                Text(e.questionText, style = MaterialTheme.typography.bodyLarge)
            }
            QuestionImageGallery(images = uiState.images)

            if (!e.attemptedSolution.isNullOrBlank()) {
                SectionLabel("Attempted Solution")
                Text(e.attemptedSolution, style = MaterialTheme.typography.bodyLarge)
            }
            if (!e.correctSolution.isNullOrBlank()) {
                SectionLabel("Correct Solution")
                Text(e.correctSolution, style = MaterialTheme.typography.bodyLarge)
            }
            if (!e.explanation.isNullOrBlank()) {
                SectionLabel("Explanation")
                Text(e.explanation, style = MaterialTheme.typography.bodyLarge)
            }
            if (!e.lessonLearned.isNullOrBlank()) {
                SectionLabel("Lesson Learned")
                Text(e.lessonLearned, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
            }
            if (!e.personalNotes.isNullOrBlank()) {
                SectionLabel("Notes")
                Text(e.personalNotes, style = MaterialTheme.typography.bodyMedium)
            }

            HorizontalDivider()
            InfoLine("Reviewed", "${e.reviewCount} times")
            if (e.lastReviewedAt != null) {
                InfoLine("Last reviewed", java.text.SimpleDateFormat("MMM d, yyyy", java.util.Locale.US).format(e.lastReviewedAt))
            }
            if (e.nextReviewAt != null) {
                InfoLine("Next review", java.text.SimpleDateFormat("MMM d, yyyy", java.util.Locale.US).format(e.nextReviewAt))
            }

            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { nav.navigate(Routes.errorReview(e.id)) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Icon(Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Review")
                }
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(text = text, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
}

@Composable
private fun InfoLine(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}
