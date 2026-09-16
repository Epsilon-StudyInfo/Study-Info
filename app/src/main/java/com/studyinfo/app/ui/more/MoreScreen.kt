package com.studyinfo.app.ui.more

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            MoreItem(title = "Statistics") { nav.navigate(Routes.STATISTICS) }
            MoreItem(title = "Subjects") { nav.navigate(Routes.PROGRESS) }
            MoreItem(title = "Settings") { nav.navigate(Routes.SETTINGS) }
            MoreItem(title = "Backup & Restore") { nav.navigate(Routes.BACKUP) }
            MoreItem(title = "About") { nav.navigate(Routes.ABOUT) }
        }
    }
}

@Composable
private fun MoreItem(title: String, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(title, fontWeight = FontWeight.Medium) },
        modifier = Modifier.clickable { onClick() },
    )
    Divider()
}
