package com.template.feature.atrust

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.template.core.ui.theme.AppTheme
import com.template.core.ui.vant.VanButton
import com.template.core.ui.vant.VanButtonSize
import com.template.core.ui.vant.VanButtonType
import com.template.core.ui.vant.VanCell
import com.template.core.ui.vant.VanImage
import com.template.core.ui.vant.VanImageFit
import com.template.core.ui.vant.VanInput
import com.template.core.ui.vant.VanInputType
import com.template.core.ui.vant.VanTypography
import com.template.core.ui.vant.VanTypographyType

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
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            VanImage(
                src = R.drawable.ic_launcher,
                width = 160.dp,
                height = 160.dp,
                fit = VanImageFit.Cover,
                radius = 4.dp
            )

            Text("本应用支持vpn登录", style = MaterialTheme.typography.headlineLarge)


            VanCell {
                VanInput(
                    value = username,
                    onValueChange = { username = it },
                    placeholder = "请输入账号",
                    clearable = true,
                    prefix = { Text("账号", fontSize = 16.sp) }, // 前缀 Emoji
                )
            }
            VanCell(
                valueComposable ={
                    VanInput(
                        value = password,
                        onValueChange = { password = it },
                        type = VanInputType.Password,
                        placeholder = "请输入密码",
                        clearable = true,
                        prefix = { Text("密码", fontSize = 16.sp) }, // 前缀 Emoji
                    )
                }
            )

            if (uiState.error != null) {
                VanTypography(
                    text = uiState.error,
                    type = VanTypographyType.Danger,
//                    color = MaterialTheme.colorScheme.error,
//                    style = MaterialTheme.typography.bodySmall
                )
            }

            VanButton(
                type = VanButtonType.Primary,
                block = true,
                onClick = {
                    onLoginClick(username, password)
                },
                disabled = uiState.isLoading,
                loading = uiState.isLoading,
                text = "登录")
//            Button(
//                onClick = { onLoginClick(username, password) },
//                enabled = !uiState.isLoading,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                if (uiState.isLoading) {
//                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
//                } else {
//                    Text("Log In")
//                }
//            }
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