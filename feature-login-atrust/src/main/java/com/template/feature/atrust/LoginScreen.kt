package com.template.feature.atrust

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.template.core.ui.theme.AppTheme

@Composable
fun LoginWithVpnScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is LoginEvent.LoginSuccess -> onLoginSuccess()
            }
        }
    }

    LoginContent(
        uiState = uiState,
        onLoginClick = viewModel::login
    )
}

@Composable
fun LoginContent(
    uiState: LoginUiState,
    onLoginClick: (String, String) -> Unit
) {
    var username by remember { mutableStateOf("admin") }
    var password by remember { mutableStateOf("123456") }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("本应用支持vpn登录", style = MaterialTheme.typography.headlineLarge)

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            if (uiState.error != null) {
                Text(
                    text = uiState.error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(
                onClick = { onLoginClick(username, password) },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Log In")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginContentPreview() {
    AppTheme {
        LoginContent(uiState = LoginUiState(isLoading = false, error = null)) { _, _ -> }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginContentLoadingPreview() {
    AppTheme {
        LoginContent(uiState = LoginUiState(isLoading = true, error = null)) { _, _ -> }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginContentErrorPreview() {
    AppTheme {
        LoginContent(uiState = LoginUiState(isLoading = false, error = "Invalid credentials")) { _, _ -> }
    }
}