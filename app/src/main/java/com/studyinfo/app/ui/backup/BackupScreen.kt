package com.studyinfo.app.ui.backup

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class BackupUiState(
    val exporting: Boolean = false,
    val importing: Boolean = false,
    val message: String? = null,
    val error: Boolean = false,
)

class BackupViewModel : ViewModel() {
    private val _ui = MutableStateFlow(BackupUiState())
    val ui: StateFlow<BackupUiState> = _ui.asStateFlow()

    /** Produces the JSON for export (call before the SAF picker result is needed). */
    suspend fun buildExportJson(): AppResult<String> {
        val uid = ServiceLocator.authRepository.currentUid
            ?: return AppResult.failure("Not signed in.")
        return ServiceLocator.backupRepository.exportAllTablesForBackup(uid)
    }

    fun reportExportFailure(message: String) {
        _ui.value = _ui.value.copy(exporting = false, message = message, error = true)
    }

    fun exportToUri(context: Context, uri: Uri, json: String) {
        _ui.value = _ui.value.copy(exporting = true, message = null, error = false)
        viewModelScope.launch {
            val ok = withContext(Dispatchers.IO) {
                runCatching {
                    context.contentResolver.openOutputStream(uri)?.use { out ->
                        out.write(json.toByteArray(Charsets.UTF_8))
                    } ?: return@runCatching false
                    true
                }.getOrDefault(false)
            }
            _ui.value = if (ok) {
                _ui.value.copy(exporting = false, message = "Backup saved successfully.")
            } else {
                _ui.value.copy(exporting = false, message = "Could not write the backup file.", error = true)
            }
        }
    }

    fun importFromUri(context: Context, uri: Uri) {
        _ui.value = _ui.value.copy(importing = true, message = null, error = false)
        viewModelScope.launch {
            val content = withContext(Dispatchers.IO) {
                runCatching {
                    context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
                }.getOrNull()
            }
            if (content.isNullOrBlank()) {
                _ui.value = _ui.value.copy(importing = false, message = "Could not read the selected file.", error = true)
                return@launch
            }
            when (val r = ServiceLocator.backupRepository.importFromJson(content)) {
                is AppResult.Success -> {
                    val s = r.value
                    val parts = listOfNotNull(
                        "${s.errors} errors",
                        "${s.unsolved} unsolved",
                        "${s.tasks} tasks",
                        "${s.reviews} reviews",
                        "${s.progress} progress",
                        "${s.tags} tags",
                        "${s.customSources} sources",
                    ).joinToString(", ")
                    _ui.value = _ui.value.copy(importing = false, message = "Import complete: $parts.")
                }
                is AppResult.Failure ->
                    _ui.value = _ui.value.copy(importing = false, message = r.message, error = true)
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
    val scope = androidx.compose.runtime.rememberCoroutineScope()

    // Pending export JSON — produced first, then written wherever the user chooses.
    var pendingExportJson by remember { mutableStateOf<String?>(null) }
    var pendingExportName by remember { mutableStateOf("prepvault-backup.json") }

    val exportLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/json"),
    ) { uri ->
        val json = pendingExportJson
        if (uri != null && json != null) {
            vm.exportToUri(context, uri, json)
        }
        pendingExportJson = null
    }

    val importLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument(),
    ) { uri ->
        if (uri != null) vm.importFromUri(context, uri)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Backup & Restore") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Export backup", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Saves ALL your data — errors, unsolved questions, tasks, progress, reviews, streak, tags and custom sources — into a single JSON file you pick.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            // Build the JSON first, then ask the user where to save it.
                            scope.launch {
                                when (val result = vm.buildExportJson()) {
                                    is AppResult.Success -> {
                                        pendingExportJson = result.value
                                        val stamp = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(Date())
                                        pendingExportName = "prepvault-backup-$stamp.json"
                                        exportLauncher.launch(pendingExportName)
                                    }
                                    is AppResult.Failure ->
                                        vm.reportExportFailure(result.message)
                                }
                            }
                        },
                        enabled = !ui.exporting,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Icon(Icons.Filled.Download, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text(if (ui.exporting) "Exporting..." else "Export to file")
                    }
                }
            }
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Restore backup", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Pick a PrepVault backup JSON file. Existing entries with the same id are updated — nothing is duplicated.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { importLauncher.launch(arrayOf("application/json", "text/*", "application/octet-stream")) },
                        enabled = !ui.importing,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Icon(Icons.Filled.Upload, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text(if (ui.importing) "Importing..." else "Choose file & import")
                    }
                }
            }
            ui.message?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (ui.error) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                )
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
