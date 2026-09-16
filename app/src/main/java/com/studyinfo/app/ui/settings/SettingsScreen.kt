package com.studyinfo.app.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.studyinfo.app.navigation.Routes

@Composable
fun SettingsScreen(
    nav: NavHostController,
    vm: SettingsViewModel = viewModel(),
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(ui.loggedOut, ui.accountDeleted) {
        if (ui.loggedOut || ui.accountDeleted) {
            nav.navigate(com.studyinfo.app.navigation.Routes.LOGIN) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SettingsGroup(title = "Account") {
                ListItem(
                    headlineContent = { Text("Profile") },
                    supportingContent = { Text("Name, exam target, language") },
                    modifier = Modifier.clickable { nav.navigate(Routes.PROFILE) },
                )
                if (ui.cloudSyncAvailable) {
                    ListItem(
                        headlineContent = { Text("Sync now") },
                        supportingContent = { Text("Push pending changes to cloud") },
                        modifier = Modifier.clickable { vm.syncNow() },
                    )
                }
            }
            SettingsGroup(title = "Customisation") {
                ListItem(headlineContent = { Text("Manage Custom Sources") }, supportingContent = { Text("Add or rename question sources") }, modifier = Modifier.clickable { nav.navigate(Routes.MANAGE_SOURCES) })
                ListItem(headlineContent = { Text("Manage Chapters") }, supportingContent = { Text("Add or rename chapters") }, modifier = Modifier.clickable { nav.navigate(Routes.MANAGE_CHAPTERS) })
                ListItem(headlineContent = { Text("Manage Tags") }, supportingContent = { Text("Add or rename tags") }, modifier = Modifier.clickable { nav.navigate(Routes.MANAGE_TAGS) })
            }
            SettingsGroup(title = "Data") {
                ListItem(headlineContent = { Text("Backup & Restore") }, supportingContent = { Text("Export or import all your data as JSON") }, modifier = Modifier.clickable { nav.navigate(Routes.BACKUP) })
            }
            SettingsGroup(title = "About") {
                ListItem(headlineContent = { Text("About PrepVault") }, modifier = Modifier.clickable { nav.navigate(Routes.ABOUT) })
            }
            SettingsGroup(title = "Account Actions") {
                FilledTonalButton(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("Log out") }
                Button(
                    onClick = { showDeleteDialog = true },
                    enabled = !ui.loading,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                ) { Text(if (ui.loading) "Deleting..." else "Delete Account") }
            }
            ui.message?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            }
            ui.error?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
            }
            Spacer(Modifier.height(24.dp))
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Log out?") },
            text = { Text("Your data stays on this device and will be waiting when you log back in with the same account.") },
            confirmButton = {
                TextButton(onClick = { showLogoutDialog = false; vm.logout() }) { Text("Log out") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel") }
            },
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete account?") },
            text = { Text("This permanently removes your account and ALL your data on this device — errors, unsolved questions, tasks, progress and streaks. This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = { showDeleteDialog = false; vm.deleteAccount() }) {
                    Text("Delete everything", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            },
        )
    }
}

@Composable
private fun SettingsGroup(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 4.dp),
        )
        content()
        HorizontalDivider()
    }
}
