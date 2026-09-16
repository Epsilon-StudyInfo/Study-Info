package com.studyinfo.app.ui.about

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun AboutScreen(nav: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("PrepVault", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold)
            Text("Version 1.0.0", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                "PrepVault is an offline-first JEE preparation companion that helps you keep an error book, an unsolved-question book, plan tasks, and track your progress across Physics, Chemistry and Mathematics.",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text("Key Features", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(
                "• Error Book with spaced-repetition review\n" +
                "• Unsolved Book with DPP / PYQ / Module / Mock / Book / Custom sources\n" +
                "• Retry queue and revision sessions\n" +
                "• Daily To-Do with recurrence and streak tracking\n" +
                "• Subject / chapter / exam-type progress bars\n" +
                "• Statistics by subject, mistake type, and source\n" +
                "• Firebase Auth + Firestore + Storage with secure per-user rules\n" +
                "• Offline-first local Room database that syncs when online\n" +
                "• JSON backup and restore",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text("Privacy", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(
                "Your study data is private. Errors, notes, solutions, and tasks are stored only in your account. Crashlytics collects crash traces only; it never records question content, notes, or solutions.",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
