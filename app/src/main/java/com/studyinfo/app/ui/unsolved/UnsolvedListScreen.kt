package com.studyinfo.app.ui.unsolved

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
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
import com.studyinfo.app.R
import com.studyinfo.app.domain.model.*
import com.studyinfo.app.navigation.Routes
import com.studyinfo.app.ui.components.EmptyStateCard
import com.studyinfo.app.ui.components.SubjectDot

@Composable
fun UnsolvedListScreen(
    nav: NavHostController,
    vm: UnsolvedListViewModel = viewModel(),
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResourceSafe(R.string.unsolved_title)) },
                actions = {
                    IconButton(onClick = { nav.navigate(Routes.SEARCH) }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { nav.navigate(Routes.unsolvedAdd(ui.activeSource?.name)) },
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text(stringResourceSafe(R.string.unsolved_add)) },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                FilterChip(selected = ui.activeSource == null, onClick = { vm.setSource(null) }, label = { Text("All") })
                QuestionSource.entries.forEach { source ->
                    FilterChip(
                        selected = ui.activeSource == source,
                        onClick = { vm.setSource(if (ui.activeSource == source) null else source) },
                        label = { Text(source.displayName) },
                    )
                }
            }
            OutlinedTextField(
                value = ui.query,
                onValueChange = vm::setQuery,
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                placeholder = { Text("Search unsolved...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
            )
            if (ui.items.isEmpty()) {
                EmptyStateCard(
                    title = "No unsolved questions",
                    subtitle = "When you can't solve a question, save it here. Pick a source and try again later.",
                    actionLabel = "Add Unsolved",
                    onAction = { nav.navigate(Routes.unsolvedAdd()) },
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(ui.items, key = { it.id }) { entry ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            onClick = { nav.navigate(Routes.unsolvedDetail(entry.id)) },
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    SubjectDot(entry.subject, modifier = Modifier.size(8.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text(entry.subject.displayName, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(Modifier.weight(1f))
                                    AssistChip(onClick = {}, label = { Text(entry.status.label, style = MaterialTheme.typography.labelSmall) })
                                }
                                Spacer(Modifier.height(8.dp))
                                Text(entry.title.ifBlank { entry.questionText.take(80) }, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, maxLines = 2)
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    listOfNotNull(entry.source.displayName, entry.chapterName, entry.questionNumber?.let { "Q $it" }).joinToString(" · "),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                )
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                    IconButton(onClick = { vm.markSolved(entry.id) }) {
                                        Icon(com.studyinfo.app.ui.icons.CheckCircle, contentDescription = "Mark Solved", tint = MaterialTheme.colorScheme.primary)
                                    }
                                    IconButton(onClick = { vm.toggleFavorite(entry.id, !entry.favorite) }) {
                                        Icon(
                                            if (entry.favorite) Icons.Filled.Star else Icons.Filled.StarBorder,
                                            contentDescription = "Favorite",
                                            tint = if (entry.favorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                    IconButton(onClick = { vm.delete(entry.id) }) {
                                        Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun stringResourceSafe(id: Int): String = androidx.compose.ui.res.stringResource(id)
