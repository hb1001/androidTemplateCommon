package com.template.feature.setting

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import com.template.core.ui.components.LogoutDialog
import com.template.feature.setting.components.ProfileInfo
import com.template.feature.setting.components.ProfileMenuItem
import com.template.feature.setting.components.ProfileScreen
import com.template.feature.setting.components.SettingsGroupData

@Composable
fun ProfilePage(
    navController: NavController,
    onClickProfileInfo: () -> Unit,
    onClickLogout: () -> Unit,
    onClickSystemSetting: () -> Unit,
    onClickMapSetting: () -> Unit
) {
//
//    navController.listenResult<String>("settingResult") { newNickName ->
//        Timber.d("newNickName: $newNickName")
//    }
    var showDialogLogoutConfirm by remember { mutableStateOf(false) }

    ProfileScreen(
        navController = navController,
        profileInfo = ProfileInfo(
            name = "张三",
            email = "03931",
            onClick = {
                onClickProfileInfo()
            }
        ),

        groups = listOf(
            SettingsGroupData(
                title = "通用设置",
                items = listOf(
                    ProfileMenuItem.NormalItem("webview的设置", onClick = {
                        onClickSystemSetting()
                    }, desc = "跳转到webview"),
                    ProfileMenuItem.NormalItem("地图设置", onClick =  {
                        onClickMapSetting()
                    })
                )
            )
        ),

        footerSlot = {
            Button(
                onClick = { showDialogLogoutConfirm = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("退出登录")
                if (showDialogLogoutConfirm) {
                    LogoutDialog(
                        onConfirm = {
                            onClickLogout()
                        },
                        onDismiss = { showDialogLogoutConfirm = false }
                    )
                }
            }
        }
    )
}