package com.studyinfo.app.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.studyinfo.app.ServiceLocator
import com.studyinfo.app.data.database.entity.UserProfileEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _profile = MutableStateFlow<UserProfileEntity?>(null)
    val profile: StateFlow<UserProfileEntity?> = _profile.asStateFlow()

    init {
        viewModelScope.launch {
            ServiceLocator.authRepository.observeUserProfile().collect { _profile.value = it }
        }
    }
}

@Composable
fun ProfileScreen(
    nav: NavHostController,
    vm: ProfileViewModel = viewModel(),
) {
    val profile by vm.profile.collectAsStateWithLifecycle()
    Scaffold(topBar = { TopAppBar(title = { Text("Profile") }) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier.size(80.dp).clip(CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text((profile?.displayName?.firstOrNull() ?: "U").toString(), style = MaterialTheme.typography.headlineMedium)
            }
            Text(profile?.displayName ?: "—", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text(profile?.email ?: "—", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Target: ${profile?.targetExam ?: "JEE Main"}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(if (profile?.emailVerified == true) "Email verified" else "Email not verified", style = MaterialTheme.typography.labelMedium, color = if (profile?.emailVerified == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)

            Button(onClick = { nav.popBackStack() }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) { Text("Done") }
        }
    }
}
