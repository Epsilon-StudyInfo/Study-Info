package com.studyinfo.app.ui.more

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.studyinfo.app.navigation.Routes

@Composable
fun MoreScreen(nav: NavHostController) {
    Scaffold(topBar = { TopAppBar(title = { Text("More") }) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                "Study tools",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 4.dp),
            )
            MoreItem(title = "Revision Session", subtitle = "Review due errors & retry queue", icon = Icons.Filled.Refresh) { nav.navigate(Routes.REVISION) }
            MoreItem(title = "Search", subtitle = "Find any error or question", icon = Icons.Filled.Search) { nav.navigate(Routes.SEARCH) }
            MoreItem(title = "Statistics", subtitle = "Streaks, subjects, mistake types", icon = Icons.Filled.BarChart) { nav.navigate(Routes.STATISTICS) }

            Text(
                "App",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 4.dp),
            )
            MoreItem(title = "Progress by Subject", icon = Icons.AutoMirrored.Filled.MenuBook) { nav.navigate(Routes.PROGRESS) }
            MoreItem(title = "Settings", subtitle = "Account, sync, manage data", icon = Icons.Filled.Settings) { nav.navigate(Routes.SETTINGS) }
            MoreItem(title = "Backup & Restore", subtitle = "Export or import your data", icon = Icons.Filled.Backup) { nav.navigate(Routes.BACKUP) }
            MoreItem(title = "About", icon = Icons.Filled.Info) { nav.navigate(Routes.ABOUT) }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun MoreItem(
    title: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = { Text(title, fontWeight = FontWeight.Medium) },
        supportingContent = subtitle?.let { { Text(it) } },
        leadingContent = icon?.let {
            { Icon(it, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
        },
        modifier = Modifier.clickable { onClick() },
    )
    HorizontalDivider()
}
