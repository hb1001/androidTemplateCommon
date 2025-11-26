package com.template.feature.setting

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.template.core.ui.components.CommonTitleBar
import com.template.data.datastore.MapJumpOption
import com.template.data.datastore.UserSettings
import com.template.feature.setting.components.ProfileMenuItem
import com.template.feature.setting.components.ProfileScreen
import com.template.feature.setting.components.SettingsGroupData

// 这个页面，用于设置全部或者部分的UserSettings
@Composable
fun MapSettingScreen(
    navController: NavController,
    userSetting: UserSettings,
    setUserSetting: (UserSettings) -> Unit,
) {
    ProfileScreen(
        topBar = {
            CommonTitleBar(title = "地图设置")
        },
        navController = navController,
        groups = listOf(
            SettingsGroupData(
                items = listOf(
                    ProfileMenuItem.SwitchItem(
                        "自动导航",
                        checked = userSetting.autoLogin,
                        onChecked = {
                            setUserSetting(
                                userSetting.copy(
                                    autoLogin = it
                                )
                            )
                        }
                    ),
                    ProfileMenuItem.ItemPicker(
                        "地图跳转",
                        options = MapJumpOption.entries.map { it.label },
                        value = userSetting.mapJumpTo.label,
                        onSetValue = {
                            setUserSetting(
                                userSetting.copy(
                                    mapJumpTo = MapJumpOption.fromLabel(it)
                                )
                            )
                        }
                    ),
                    ProfileMenuItem.IntItem(
                        "地图默认大小",
                        value = userSetting.mapDefaultSize,
                        onSetValue = {
                            setUserSetting(
                                userSetting.copy(
                                    mapDefaultSize = it
                                )
                            )
                        }
                    )

                )
            ),
        ),
    )
}