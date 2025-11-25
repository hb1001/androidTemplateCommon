package com.template.feature.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.template.core.ui.components.CommonTitleBar
import com.template.core.ui.components.CustomLoadingDialog
import com.template.core.ui.components.LogoutDialog
import com.template.core.ui.components.PaymentBottomSheet
import com.template.feature.setting.components.ProfileInfo
import com.template.feature.setting.components.ProfileMenuItem
import com.template.feature.setting.components.ProfileScreen
import com.template.feature.setting.components.SettingsGroupData
import timber.log.Timber

@Composable
fun ProfilePage(
    navController: NavController,
    onClickProfileInfo: () -> Unit,
    onClickLogout: () -> Unit,
    onClickSystemSetting: () -> Unit,
) {

    var showDialog1 by remember { mutableStateOf(false) }
    var showDialog2 by remember { mutableStateOf(false) }
    var showDialog3 by remember { mutableStateOf(false) }
    navController.listenResult<String>("settingResult") { newNickName ->
        Timber.d("newNickName: $newNickName")
    }
    ProfileScreen(
        profileInfo = ProfileInfo(
            name = "张三",
            email = "03931",
            onClick = {
                onClickProfileInfo()
            }
        ),

        groups = listOf(
            SettingsGroupData(
                title = "账户中心",
                items = listOf(
                    ProfileMenuItem(Icons.Default.Person, "个人信息") { showDialog2 = true },
                    ProfileMenuItem(Icons.Default.Notifications, "消息通知") { showDialog3 = true }
                )
            ),
            SettingsGroupData(
                title = "通用设置",
                items = listOf(
                    ProfileMenuItem(Icons.Default.Settings, "系统设置") {
                        onClickSystemSetting()
                    },
                    ProfileMenuItem(Icons.Default.AccountCircle, "切换账号") {},
                    ProfileMenuItem(Icons.Outlined.Info, "关于我们") {}
                )
            )
        ),

        footerSlot = {
            Button(
                onClick = { showDialog1 = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("退出登录")
            }
        },

        dialogsSlot = {
            if (showDialog1) {
                LogoutDialog(
                    onConfirm = {
                        onClickLogout()
                    },
                    onDismiss = { showDialog1 = false }
                )
            }
            if (showDialog2) {
                CustomLoadingDialog(
                    onDismiss = { showDialog2 = false }
                )
            }
            if (showDialog3) {
                PaymentBottomSheet(
                    onDismiss = { showDialog3 = false }
                )
            }
        }
    )
}

@Composable
fun <T> NavController.listenResult(
    key: String,
    onResult: (T) -> Unit
) {
    val savedStateHandle = currentBackStackEntry?.savedStateHandle
    val resultFlow = savedStateHandle?.getStateFlow<T?>(key, null)
    val result by resultFlow?.collectAsState() ?: remember { mutableStateOf(null) }

    LaunchedEffect(result) {
        result?.let {
            onResult(it)
            // 使用后清除，避免重复触发
            savedStateHandle?.set(key, null)
        }
    }
}