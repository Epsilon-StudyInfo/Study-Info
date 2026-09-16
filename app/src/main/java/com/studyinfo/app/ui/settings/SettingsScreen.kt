package com.studyinfo.app.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
    LaunchedEffect(ui.loggedOut, ui.accountDeleted) {
        if (ui.loggedOut || ui.accountDeleted) {
            nav.navigate(Routes.LOGIN) {
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SettingsGroup(title = "Account") {
                ListItem(
                    headlineContent = { Text("Profile") },
                    modifier = Modifier.clickable { nav.navigate(Routes.PROFILE) },
                )
                ListItem(
                    headlineContent = { Text("Sync now") },
                    supportingContent = { Text("Push pending changes to cloud") },
                    modifier = Modifier.clickable { vm.syncNow() },
                )
            }
            SettingsGroup(title = "Customisation") {
                ListItem(headlineContent = { Text("Manage Custom Sources") }, modifier = Modifier.clickable { nav.navigate(Routes.MANAGE_SOURCES) })
                ListItem(headlineContent = { Text("Manage Chapters") }, modifier = Modifier.clickable { nav.navigate(Routes.MANAGE_CHAPTERS) })
                ListItem(headlineContent = { Text("Manage Tags") }, modifier = Modifier.clickable { nav.navigate(Routes.MANAGE_TAGS) })
            }
            SettingsGroup(title = "Data") {
                ListItem(headlineContent = { Text("Backup & Restore") }, modifier = Modifier.clickable { nav.navigate(Routes.BACKUP) })
            }
            SettingsGroup(title = "About") {
                ListItem(headlineContent = { Text("About PrepVault") }, modifier = Modifier.clickable { nav.navigate(Routes.ABOUT) })
            }
            SettingsGroup(title = "Account Actions") {
                FilledTonalButton(onClick = { vm.logout() }, modifier = Modifier.fillMaxWidth()) { Text("Log out") }
                Button(
                    onClick = { vm.deleteAccount() },
                    enabled = !ui.loading,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                ) { Text(if (ui.loading) "Deleting..." else "Delete Account") }
            }
            ui.message?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
            ui.error?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
        }
    }
}

@Composable
private fun SettingsGroup(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 4.dp))
        content()
        Divider()
    }
}
