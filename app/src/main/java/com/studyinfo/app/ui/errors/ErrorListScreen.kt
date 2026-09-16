package com.studyinfo.app.ui.errors

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
import com.studyinfo.app.ui.components.SectionHeader
import com.studyinfo.app.ui.components.SubjectDot

@Composable
fun ErrorListScreen(
    nav: NavHostController,
    vm: ErrorListViewModel = viewModel(),
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResourceSafe(R.string.errors_title)) },
                actions = {
                    IconButton(onClick = { nav.navigate(Routes.SEARCH) }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { nav.navigate(Routes.ERROR_ADD) },
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text(stringResourceSafe(R.string.errors_add)) },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            // Filters
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                FilterChip(
                    selected = ui.filterSubject == null,
                    onClick = { vm.setSubject(null) },
                    label = { Text("All") },
                )
                Subject.entries.forEach { subject ->
                    FilterChip(
                        selected = ui.filterSubject == subject,
                        onClick = { vm.setSubject(if (ui.filterSubject == subject) null else subject) },
                        label = { Text(subject.displayName.take(3)) },
                    )
                }
            }
            OutlinedTextField(
                value = ui.query,
                onValueChange = vm::setQuery,
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                placeholder = { Text("Search errors...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
            )

            if (ui.items.isEmpty()) {
                EmptyStateCard(
                    title = "No mistakes recorded yet",
                    subtitle = "Keep practicing — when you make a mistake, save it here to revise and improve.",
                    actionLabel = "Add your first error",
                    onAction = { nav.navigate(Routes.ERROR_ADD) },
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(ui.items, key = { it.id }) { error ->
                        ErrorRow(
                            error = error,
                            onClick = { nav.navigate(Routes.errorDetail(error.id)) },
                            onFavorite = { vm.toggleFavorite(error.id, !error.favorite) },
                            onDelete = { vm.delete(error.id) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorRow(
    error: com.studyinfo.app.data.database.entity.ErrorEntryEntity,
    onClick: () -> Unit,
    onFavorite: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SubjectDot(error.subject, modifier = Modifier.size(8.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    text = error.subject.displayName,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = error.status.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (error.status == ErrorStatus.UNDERSTOOD) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.error,
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = error.title.ifBlank { error.questionText.take(80) },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = listOfNotNull(
                    error.chapterName,
                    error.topic,
                    error.mistakeType.label,
                ).joinToString(" · "),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onFavorite) {
                    Icon(
                        if (error.favorite) Icons.Filled.Star else Icons.Filled.StarBorder,
                        contentDescription = "Favorite",
                        tint = if (error.favorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
private fun stringResourceSafe(id: Int): String = androidx.compose.ui.res.stringResource(id)
