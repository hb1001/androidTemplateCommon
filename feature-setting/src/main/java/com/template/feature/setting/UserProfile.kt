package com.template.feature.setting

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
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
import com.template.core.ui.vant.DialogOptions
import com.template.core.ui.vant.LocalVanDialog
import com.template.core.ui.vant.VanButton
import com.template.core.ui.vant.VanButtonType
import com.template.core.ui.vant.VanDialog
import com.template.core.ui.vant.VanDialogMessageAlign
import com.template.core.ui.vant.VanDialogTheme
import com.template.feature.setting.components.ProfileInfo
import com.template.feature.setting.components.ProfileMenuItem
import com.template.feature.setting.components.ProfileScreen
import com.template.feature.setting.components.SettingsGroupData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    var enableNotify by remember { mutableStateOf(true) }
    var userName by remember { mutableStateOf("张三") }
    var cacheSize by remember { mutableStateOf(10) }
    var currentTheme by remember { mutableStateOf("") }

    val dialogController = LocalVanDialog.current
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
                    // NormalItem
                    ProfileMenuItem.NormalItem(
                        title = "WebView 设置",
                        desc = "跳转到 WebView",
                        onClick = { onClickSystemSetting() }
                    ),

                    ProfileMenuItem.NormalItem(
                        title = "地图设置",
                        onClick = { onClickMapSetting() }
                    ),

                    // SwitchItem
                    ProfileMenuItem.SwitchItem(
                        title = "开启通知",
                        desc = "是否允许推送通知",
                        checked = enableNotify,
                        onChecked = { enableNotify = it }
                    ),

                    // TextItem
                    ProfileMenuItem.TextItem(
                        title = "用户名",
                        value = userName,
                        onSetValue = { userName = it }
                    ),

                    // IntItem
                    ProfileMenuItem.IntItem(
                        title = "缓存大小",
                        desc = "单位 MB",
                        value = cacheSize,
                        onSetValue = { cacheSize = it }
                    ),

                    // ItemPicker
                    ProfileMenuItem.ItemPicker(
                        title = "主题模式",
                        options = listOf("跟随系统", "深色", "浅色"),
                        value = currentTheme,
                        onSetValue = {currentTheme = it }
                    )
                )
            )
        ),

        footerSlot = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                VanButton(
                    type = VanButtonType.Danger,
                    text = "注销登录",
                    block = true,
                    onClick = {
                        dialogController.show(
                            DialogOptions(
                                title = "提示",
                                message = "确定要注销登录吗？",
                                showCancelButton = true,
//                                dismissOnAction = false, // 只有手动 dismiss 才关闭
                                onConfirm = {
//                                    dialogController.dismiss()
                                    onClickLogout()
                                },
//                                onCancel = {
//                                    dialogController.dismiss()
//                                }
                            )
                        )
                    }
                )

//                VanButton(
//                    type = VanButtonType.Danger,
//                    text = "注销登录",
//                    block = true,
//                    onClick = {
//                        showDialogLogoutConfirm = true
//                    }
//                )
//                VanDialog(
//                    visible = showDialogLogoutConfirm,
//                    onDismissRequest = { showDialogLogoutConfirm = false },
//                    title = "提示",
//                    onCancel = {
//                        showDialogLogoutConfirm = false
//                    },
//                    onConfirm = {
//                        showDialogLogoutConfirm = false
//                        onClickLogout()
//                    },
//                    message = "确定要注销登录吗？",
//                    showConfirmButton = true,
//                    showCancelButton = true,
//                    closeable = true,
//                )

            }

        }
    )
}