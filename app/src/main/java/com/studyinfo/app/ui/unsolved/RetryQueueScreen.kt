package com.studyinfo.app.ui.unsolved

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.studyinfo.app.ui.components.EmptyStateCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RetryQueueViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<com.studyinfo.app.data.database.entity.UnsolvedQuestionEntity>>(emptyList())
    val items: StateFlow<List<com.studyinfo.app.data.database.entity.UnsolvedQuestionEntity>> = _items.asStateFlow()

    init {
        viewModelScope.launch {
            ServiceLocator.unsolvedRepository.observeRetryQueue().collect { _items.value = it }
        }
    }

    fun scheduleRetry(id: String, days: Int) {
        viewModelScope.launch { ServiceLocator.unsolvedRepository.scheduleRetry(id, days) }
    }
    fun markSolved(id: String) {
        viewModelScope.launch {
            ServiceLocator.unsolvedRepository.setStatus(id, com.studyinfo.app.domain.model.UnsolvedStatus.SOLVED)
        }
    }
}

@Composable
fun RetryQueueScreen(
    nav: NavHostController,
    vm: RetryQueueViewModel = viewModel(),
) {
    val items by vm.items.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Retry Queue") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        if (items.isEmpty()) {
            EmptyStateCard(
                title = "Nothing in your retry queue",
                subtitle = "When you mark an unsolved question as 'Attempt Again' or schedule a retry, it'll show up here.",
            )
            return@Scaffold
        }
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(items, key = { it.id }) { entry ->
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), onClick = {
                    nav.navigate(com.studyinfo.app.navigation.Routes.unsolvedDetail(entry.id))
                }) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(entry.title.ifBlank { entry.questionText.take(60) }, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text("${entry.subject.displayName} · ${entry.source.displayName}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (entry.nextRetryAt != null) {
                            Text("Retry by ${java.text.SimpleDateFormat("MMM d", java.util.Locale.US).format(entry.nextRetryAt)}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(top = 8.dp)) {
                            FilledTonalButton(onClick = { vm.scheduleRetry(entry.id, 3) }, shape = RoundedCornerShape(8.dp)) { Text("+3d") }
                            FilledTonalButton(onClick = { vm.scheduleRetry(entry.id, 7) }, shape = RoundedCornerShape(8.dp)) { Text("+7d") }
                            Button(onClick = { vm.markSolved(entry.id) }, shape = RoundedCornerShape(8.dp)) { Text("Solved") }
                        }
                    }
                }
            }
        }
    }
}
