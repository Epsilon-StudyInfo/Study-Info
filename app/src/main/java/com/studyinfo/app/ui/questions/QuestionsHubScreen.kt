package com.studyinfo.app.ui.questions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.studyinfo.app.navigation.Routes

@Composable
fun QuestionsHubScreen(nav: NavHostController) {
    Scaffold(topBar = { TopAppBar(title = { Text("Questions") }) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            HubCard(
                title = "Error Book",
                subtitle = "Mistakes you want to learn from. Add, review, and revise with spaced repetition.",
                icon = Icons.Filled.Bolt,
                onClick = { nav.navigate(Routes.ERRORS_LIST) },
            )
            HubCard(
                title = "Unsolved Book",
                subtitle = "Questions you couldn't solve. DPP / PYQ / Module / Mock / Book / Custom sources.",
                icon = Icons.Filled.Help,
                onClick = { nav.navigate(Routes.UNSOLVED_LIST) },
            )
            HubCard(
                title = "Retry Queue",
                subtitle = "Questions you've scheduled to attempt again, with the next due date.",
                icon = Icons.Filled.Refresh,
                onClick = { nav.navigate(Routes.RETRY_QUEUE) },
            )
            HubCard(
                title = "Revision",
                subtitle = "Walk through your due errors and retry queue in one focused session.",
                icon = Icons.Filled.AutoStories,
                onClick = { nav.navigate(Routes.REVISION) },
            )
        }
    }
}

@Composable
private fun HubCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
