package com.studyinfo.app.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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

/* ------------------------------------------------------------------ */
/* Splash                                                              */
/* ------------------------------------------------------------------ */

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF4F46E5), Color(0xFF4338CA), Color(0xFF312E81)),
                ),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(R.drawable.ic_brand_logo),
                contentDescription = "PrepVault",
                modifier = Modifier.size(96.dp),
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "PrepVault",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Your JEE error book & revision vault",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f),
            )
            Spacer(Modifier.height(28.dp))
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.5.dp,
                modifier = Modifier.size(28.dp),
            )
        }
    }
}

/* ------------------------------------------------------------------ */
/* Screens                                                             */
/* ------------------------------------------------------------------ */

@Composable
fun LoginScreen(
    nav: NavHostController,
    vm: AuthViewModel = viewModel(),
) {
    AuthScaffold(
        mode = AuthMode.LOGIN,
        vm = vm,
        nav = nav,
    )
}

@Composable
fun RegisterScreen(
    nav: NavHostController,
    vm: AuthViewModel = viewModel(),
) {
    AuthScaffold(
        mode = AuthMode.REGISTER,
        vm = vm,
        nav = nav,
    )
}

@Composable
fun ForgotPasswordScreen(
    nav: NavHostController,
    vm: AuthViewModel = viewModel(),
) {
    AuthScaffold(
        mode = AuthMode.FORGOT,
        vm = vm,
        nav = nav,
    )
}

@Composable
private fun AuthScaffold(
    mode: AuthMode,
    vm: AuthViewModel,
    nav: NavHostController,
) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    // Keep the ViewModel's mode in sync with the screen we're on.
    LaunchedEffect(mode) { vm.onEvent(AuthEvent.ModeChanged(mode)) }

    // Successful sign-in / registration -> enter the app with a clean back stack.
    LaunchedEffect(ui.success) {
        if (ui.success) {
            nav.navigate(Routes.HOME) {
                popUpTo(0) { inclusive = true }
            }
        }
    }
    // Reset email sent -> go back to login.
    LaunchedEffect(ui.emailSent) {
        if (ui.emailSent) {
            nav.popBackStack()
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(48.dp))

            // Brand header
            Image(
                painter = painterResource(R.drawable.ic_brand_logo),
                contentDescription = null,
                modifier = Modifier.size(84.dp),
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "PrepVault",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = when (mode) {
                    AuthMode.LOGIN -> "Welcome back! Log in to continue your prep."
                    AuthMode.REGISTER -> "Create your account and start improving."
                    AuthMode.FORGOT -> "We'll send password reset instructions."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(32.dp))

            // Card with the form
            Surface(
                shape = RoundedCornerShape(24.dp),
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Text(
                        text = when (mode) {
                            AuthMode.LOGIN -> "Log in"
                            AuthMode.REGISTER -> "Create account"
                            AuthMode.FORGOT -> "Reset password"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )

                    if (mode == AuthMode.REGISTER) {
                        AuthTextField(
                            value = ui.name,
                            onValueChange = { vm.onEvent(AuthEvent.NameChanged(it)) },
                            label = "Name",
                            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            onImeAction = { focusManager.moveFocus(FocusDirection.Down) },
                        )
                    }

                    AuthTextField(
                        value = ui.email,
                        onValueChange = { vm.onEvent(AuthEvent.EmailChanged(it)) },
                        label = "Email",
                        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next,
                        ),
                        onImeAction = { focusManager.moveFocus(FocusDirection.Down) },
                    )

                    if (mode != AuthMode.FORGOT) {
                        AuthTextField(
                            value = ui.password,
                            onValueChange = { vm.onEvent(AuthEvent.PasswordChanged(it)) },
                            label = if (mode == AuthMode.REGISTER) "Password (min 6 characters)" else "Password",
                            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                            visualTransformation = if (ui.passwordVisible) VisualTransformation.None
                            else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { vm.onEvent(AuthEvent.TogglePasswordVisibility) }) {
                                    Icon(
                                        if (ui.passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                        contentDescription = if (ui.passwordVisible) "Hide password" else "Show password",
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = if (mode == AuthMode.REGISTER) ImeAction.Next else ImeAction.Done,
                            ),
                            onImeAction = {
                                if (mode == AuthMode.REGISTER) focusManager.moveFocus(FocusDirection.Down)
                                else vm.onEvent(AuthEvent.Submit)
                            },
                        )
                    }

                    if (mode == AuthMode.REGISTER) {
                        AuthTextField(
                            value = ui.confirmPassword,
                            onValueChange = { vm.onEvent(AuthEvent.ConfirmPasswordChanged(it)) },
                            label = "Confirm password",
                            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                            visualTransformation = if (ui.passwordVisible) VisualTransformation.None
                            else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done,
                            ),
                            onImeAction = { vm.onEvent(AuthEvent.Submit) },
                        )
                    }

                    val errorMsg = ui.error
                    if (errorMsg != null) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.errorContainer,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = errorMsg,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            )
                        }
                    }

                    Button(
                        onClick = { vm.onEvent(AuthEvent.Submit) },
                        enabled = !ui.loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                    ) {
                        if (ui.loading) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp,
                            )
                        } else {
                            Text(
                                text = when (mode) {
                                    AuthMode.LOGIN -> "Log in"
                                    AuthMode.REGISTER -> "Create account"
                                    AuthMode.FORGOT -> "Send reset email"
                                },
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }

                    if (ui.emailSent) {
                        Text(
                            text = "If that email exists, reset instructions have been sent.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Switch modes
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = when (mode) {
                        AuthMode.LOGIN -> "New to PrepVault?"
                        AuthMode.REGISTER -> "Already have an account?"
                        AuthMode.FORGOT -> "Remembered it?"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                TextButton(onClick = {
                    when (mode) {
                        AuthMode.LOGIN -> nav.navigate(Routes.REGISTER)
                        AuthMode.REGISTER -> nav.popBackStack()
                        AuthMode.FORGOT -> nav.popBackStack()
                    }
                }) {
                    Text(
                        text = when (mode) {
                            AuthMode.LOGIN -> "Create account"
                            AuthMode.REGISTER -> "Log in"
                            AuthMode.FORGOT -> "Back to login"
                        },
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            if (mode == AuthMode.LOGIN) {
                TextButton(onClick = { nav.navigate(Routes.FORGOT) }) {
                    Text("Forgot password?", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            if (mode != AuthMode.LOGIN) {
                TextButton(onClick = { nav.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Back")
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable () -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onImeAction: () -> Unit = {},
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(onAny = { onImeAction() }),
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth(),
    )
}
