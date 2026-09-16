package com.studyinfo.app.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.studyinfo.app.ui.errors.DropdownField
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val profile: UserProfileEntity? = null,
    val saving: Boolean = false,
    val saved: Boolean = false,
)

class ProfileViewModel : ViewModel() {
    private val _ui = MutableStateFlow(ProfileUiState())
    val ui: StateFlow<ProfileUiState> = _ui.asStateFlow()

    init {
        viewModelScope.launch {
            ServiceLocator.authRepository.observeUserProfile().collect { profile ->
                _ui.value = _ui.value.copy(profile = profile)
            }
        }
    }

    fun save(name: String, targetExam: String) {
        if (_ui.value.saving) return
        _ui.value = _ui.value.copy(saving = true)
        viewModelScope.launch {
            ServiceLocator.authRepository.updateProfileName(name)
            ServiceLocator.authRepository.updateTargetExam(targetExam)
            _ui.value = _ui.value.copy(saving = false, saved = true)
        }
    }
}

@Composable
fun ProfileScreen(
    nav: NavHostController,
    vm: ProfileViewModel = viewModel(),
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    val profile = ui.profile

    // Local editable state, seeded once the profile row arrives.
    var name by remember { mutableStateOf("") }
    var targetExam by remember { mutableStateOf("JEE Main") }
    var seeded by remember { mutableStateOf(false) }
    LaunchedEffect(profile?.uid) {
        if (!seeded && profile != null) {
            name = profile.displayName.orEmpty()
            targetExam = profile.targetExam
            seeded = true
        }
    }
    LaunchedEffect(ui.saved) { if (ui.saved) nav.popBackStack() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
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
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(84.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    (name.ifBlank { profile?.displayName } ?.firstOrNull() ?: "U").toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            DropdownField(
                label = "Target exam",
                value = targetExam,
                options = listOf("JEE Main", "JEE Advanced", "Both"),
                onSelected = { targetExam = it },
            )

            Text(
                profile?.email ?: "—",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            if (ServiceLocator.authRepository.isFirebaseConfigured) {
                Text(
                    if (profile?.emailVerified == true) "Email verified" else "Email not verified (cloud)",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (profile?.emailVerified == true) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                Text(
                    "Local account — data lives on this device",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Button(
                onClick = { vm.save(name.trim(), targetExam) },
                enabled = !ui.saving && name.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(if (ui.saving) "Saving..." else "Save")
            }
        }
    }
}
