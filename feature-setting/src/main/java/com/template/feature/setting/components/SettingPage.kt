package com.template.feature.setting.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.template.core.ui.vant.VanCell

data class ProfileInfo(
    val name: String,
    val email: String,
    val onClick: () -> Unit
)

data class SettingsGroupData(
    val title: String? = null,
    val items: List<ProfileMenuItem>
)

@Composable
fun ProfileScreen(
    topBar: @Composable () -> Unit = {},
    navController: NavController,
    profileInfo: ProfileInfo? = null,
    groups: List<SettingsGroupData>,
    footerSlot: @Composable () -> Unit = {},
    dialogsSlot: @Composable () -> Unit = {}  // 用来放各种弹窗
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = topBar,
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(MaterialTheme.colorScheme.surface)
        ) {

            // 个人信息头部
            if (profileInfo != null){
                ProfileHeader(
                    name = profileInfo.name,
                    email = profileInfo.email,
                    onClick = profileInfo.onClick
                )
                Spacer(Modifier.height(8.dp))
                HorizontalDivider(
                    thickness = 8.dp,
                    color = MaterialTheme.colorScheme.surfaceContainerLow
                )
            }



            // 动态多个 SettingsGroup
            groups.forEachIndexed { index, group ->
                SettingsGroup(
                    title = group.title,
                    items = group.items,
                    navController = navController
                )



                if (index != groups.lastIndex) {
                    HorizontalDivider()
                }
            }

            Spacer(Modifier.height(32.dp))

            // Footer 插槽（退出登录按钮等）
            footerSlot()

            Spacer(Modifier.height(50.dp))

            // 弹窗插槽
            dialogsSlot()
        }
    }
}
