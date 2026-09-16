package com.studyinfo.app.ui.backup

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.studyinfo.app.ServiceLocator
import com.studyinfo.app.utils.AppResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BackupUiState(
    val exporting: Boolean = false,
    val importing: Boolean = false,
    val message: String? = null,
)

class BackupViewModel : ViewModel() {
    private val _ui = MutableStateFlow(BackupUiState())
    val ui: StateFlow<BackupUiState> = _ui.asStateFlow()

    fun export(context: Context) {
        val uid = ServiceLocator.authRepository.currentUid ?: run {
            _ui.value = _ui.value.copy(message = "Not signed in.")
            return
        }
        _ui.value = _ui.value.copy(exporting = true, message = null)
        viewModelScope.launch {
            val result = ServiceLocator.backupRepository.exportAllTablesForBackup(uid)
            when (result) {
                is AppResult.Success -> {
                    val file = java.io.File(context.filesDir, "backups/prepvault-${System.currentTimeMillis()}.json").apply {
                        parentFile?.mkdirs()
                        writeText(result.value)
                    }
                    _ui.value = _ui.value.copy(exporting = false, message = "Exported ${file.name}")
                }
                is AppResult.Failure -> _ui.value = _ui.value.copy(exporting = false, message = result.message)
            }
        }
    }

    fun import(context: Context, content: String) {
        _ui.value = _ui.value.copy(importing = true, message = null)
        viewModelScope.launch {
            val r = ServiceLocator.backupRepository.importFromJson(content)
            when (r) {
                is AppResult.Success -> _ui.value = _ui.value.copy(importing = false, message = "Import complete.")
                is AppResult.Failure -> _ui.value = _ui.value.copy(importing = false, message = r.message)
            }
        }
    }
}

@Composable
fun BackupScreen(
    nav: NavHostController,
    vm: BackupViewModel = viewModel(),
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Backup & Restore") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Export backup", style = MaterialTheme.typography.titleMedium)
                    Text("Saves all errors, unsolved questions, tasks, progress, reviews, tags, custom sources, and settings into a single JSON file.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { vm.export(context) },
                        enabled = !ui.exporting,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Icon(Icons.Filled.Download, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text(if (ui.exporting) "Exporting..." else "Export JSON")
                    }
                }
            }
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Import backup", style = MaterialTheme.typography.titleMedium)
                    Text("Replaces tags and custom sources from a JSON file. Other tables (errors, unsolved, etc.) require a matching schema version.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(8.dp))
                    var text by remember { mutableStateOf("") }
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("Paste JSON content") },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp),
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { vm.import(context, text) },
                        enabled = !ui.importing && text.isNotBlank(),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Icon(Icons.Filled.Upload, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text(if (ui.importing) "Importing..." else "Import")
                    }
                }
            }
            ui.message?.let { Text(it, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary) }
        }
    }
}
