package com.studyinfo.app.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.studyinfo.app.R
import com.studyinfo.app.navigation.Routes

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.16f),
                modifier = Modifier.size(72.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "PV",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = "PrepVault",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.height(8.dp))
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
fun LoginScreen(
    nav: NavHostController,
    vm: AuthViewModel = viewModel(),
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    LaunchedEffect(ui.success) {
        if (ui.success) {
            nav.navigate(Routes.HOME) {
                popUpTo(Routes.SPLASH) { inclusive = true }
            }
        }
    }
    AuthForm(
        ui = ui,
        isRegister = false,
        onEvent = vm::onEvent,
        onForgotPassword = { nav.navigate(Routes.FORGOT) },
        onRegister = { nav.navigate(Routes.REGISTER) },
        onGoogle = { /* Google sign-in requires Activity result APIs; documented in README */ },
    )
}

@Composable
fun RegisterScreen(
    nav: NavHostController,
    vm: AuthViewModel = viewModel(),
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    LaunchedEffect(ui.success) {
        if (ui.success) {
            nav.navigate(Routes.HOME) {
                popUpTo(Routes.SPLASH) { inclusive = true }
            }
        }
    }
    AuthForm(
        ui = ui,
        isRegister = true,
        onEvent = vm::onEvent,
        onForgotPassword = null,
        onRegister = { nav.popBackStack() },
        onGoogle = {},
    )
}

@Composable
fun ForgotPasswordScreen(
    nav: NavHostController,
    vm: AuthViewModel = viewModel(),
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    LaunchedEffect(ui.emailSent) {
        if (ui.emailSent) nav.popBackStack()
    }
    AuthForm(
        ui = ui,
        isRegister = false,
        onEvent = vm::onEvent,
        onForgotPassword = null,
        onRegister = null,
        isForgot = true,
        onBack = { nav.popBackStack() },
        onGoogle = null,
    )
}

@Composable
private fun AuthForm(
    ui: AuthUiState,
    isRegister: Boolean,
    onEvent: (AuthEvent) -> Unit,
    onForgotPassword: (() -> Unit)?,
    onRegister: (() -> Unit)?,
    onGoogle: (() -> Unit)?,
    isForgot: Boolean = false,
    onBack: (() -> Unit)? = null,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            if (onBack != null) {
                TopAppBar(
                    title = { Text(stringResourceSafe(R.string.auth_forgot)) },
                    navigationIcon = {
                        TextButton(onClick = onBack) { Text(stringResourceSafe(R.string.action_back)) }
                    },
                )
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = if (isForgot) "Reset password"
                else if (isRegister) stringResourceSafe(R.string.auth_register)
                else stringResourceSafe(R.string.auth_login),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "PrepVault keeps an offline-first record of every mistake and every unsolved question so you can revise and improve.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            if (isRegister || !isForgot) {
                if (isRegister) {
                    OutlinedTextField(
                        value = ui.name,
                        onValueChange = { onEvent(AuthEvent.NameChanged(it)) },
                        label = { Text(stringResourceSafe(R.string.auth_name)) },
                        leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                OutlinedTextField(
                    value = ui.email,
                    onValueChange = { onEvent(AuthEvent.EmailChanged(it)) },
                    label = { Text(stringResourceSafe(R.string.auth_email)) },
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = ui.password,
                    onValueChange = { onEvent(AuthEvent.PasswordChanged(it)) },
                    label = { Text(stringResourceSafe(R.string.auth_password)) },
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            } else {
                OutlinedTextField(
                    value = ui.email,
                    onValueChange = { onEvent(AuthEvent.EmailChanged(it)) },
                    label = { Text(stringResourceSafe(R.string.auth_email)) },
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            if (ui.error != null) {
                Text(
                    text = ui.error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Button(
                onClick = { onEvent(if (isForgot) AuthEvent.SendReset else AuthEvent.Submit) },
                enabled = !ui.loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
            ) {
                if (ui.loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text(
                        text = if (isForgot) stringResourceSafe(R.string.auth_send_reset)
                        else if (isRegister) stringResourceSafe(R.string.auth_register)
                        else stringResourceSafe(R.string.auth_login),
                    )
                }
            }

            if (onGoogle != null) {
                OutlinedButton(
                    onClick = onGoogle,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(stringResourceSafe(R.string.auth_google))
                }
            }

            if (onForgotPassword != null && !isRegister) {
                TextButton(onClick = onForgotPassword, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResourceSafe(R.string.auth_forgot), textAlign = TextAlign.Center)
                }
            }

            if (onRegister != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (isRegister) {
                        TextButton(onClick = onRegister) { Text(stringResourceSafe(R.string.auth_login)) }
                    } else {
                        Text("Don't have an account?")
                        TextButton(onClick = onRegister) { Text(stringResourceSafe(R.string.auth_register)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun stringResourceSafe(id: Int): String = androidx.compose.ui.res.stringResource(id)
