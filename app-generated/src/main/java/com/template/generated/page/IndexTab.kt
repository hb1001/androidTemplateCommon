package com.template.generated.page


import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.rememberCoroutineScope
import com.template.core.model.Post
import com.template.core.model.Reactions
import com.template.core.navigation.AppRoutes
import com.template.core.navigation.LocalNavController
import com.template.feature.atrust.navigation.navigateToLoginWithVpn
import com.template.feature.map.AiScreen
import com.template.core.ui.uimodel.CardItem
import com.template.core.ui.uimodel.UiState
import com.template.core.ui.components.CommonTitleBar
import com.template.core.ui.components.MaxWidthCard
import com.template.core.ui.components.PullRefreshOnlyList
import com.template.feature.setting.ProfilePage
import com.template.feature.setting.navigation.navigateToSettingMap
import com.template.feature.setting.navigation.navigateToSettingSingle
import com.template.feature.webview.navigation.navigateToWebview52
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.template.core.ui.vant.VanTabbar
import com.template.core.ui.vant.VanTabbarItem

/**
 * 主页面：Tabbar 切换示例 (页面状态保持)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppMainEntryScreen() {
    var activeTabName by rememberSaveable { mutableStateOf("home") }

    val navController = LocalNavController.current
    val scope = rememberCoroutineScope()

    var data by remember { mutableStateOf(UiState(emptyList<Post>())) }

    Scaffold(
        topBar = { /* 不需要标题，留空即可 */ },
        bottomBar = {
            VanTabbar(
                value = activeTabName,
                onChange = { activeTabName = it as String },
                fixed = true,
                safeAreaInsetBottom = true
            ) {
                VanTabbarItem(
                    name = "home",
                    icon = { active -> TabIcon(active, Icons.Filled.Home, Icons.Outlined.Home) }
                ) { Text("首页") }

                VanTabbarItem(
                    name = "list",
                    icon = { active -> TabIcon(active, Icons.Filled.Search, Icons.Outlined.Search) }
                ) { Text("列表") }

                VanTabbarItem(
                    name = "profile",
                    icon = { active -> TabIcon(active, Icons.Filled.Person, Icons.Outlined.Person) }
                ) { Text("我的") }
            }
        }
    ) { paddingValues ->
        // 去掉标题、padding，让内容紧贴上方
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
                .background(Color(0xFFF7F8FA))
        ) {
            when (activeTabName) {

                "home" -> AiScreen()

                "list" -> {
                    Scaffold(
                        topBar = {
                            CommonTitleBar(
                                title = "列表",
                                showBack = false,
                                showDropDown = true,
                                dropdownMenuComponent = { close ->
                                    DropdownMenuItem(
                                        text = { Text("刷新") },
                                        onClick = {
                                            scope.launch {
                                                data = data.copy(isLoading = true)
                                                data = UiState(getList())
                                            }
                                            close()
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("关于") },
                                        onClick = { /* ... */ }
                                    )
                                })
                        }
                    ) { paddingValues ->
                        Column(modifier = Modifier.padding(paddingValues)) {

                            PullRefreshOnlyList(
                                uiState = data,
                                refresh = {
                                    scope.launch {
                                        data = data.copy(isLoading = true)
                                        data = UiState(getList())
                                    }
                                },
                                content = {
                                    MaxWidthCard(
                                        CardItem(
                                            id = it.id.toString(),
                                            title = it.title,
                                            description = it.body
                                        )
                                    ) {
                                        navController.navigate(AppRoutes.CUSTOM_POST_DETAIL_ROUTE)
                                    }
                                },
                                key = { it.title }
                            )
                        }
                    }
                }

                "profile" -> {
                    ProfilePage(
                        navController = navController,
                        onClickProfileInfo = {
                            navController.navigateToSettingSingle("姓名", "张三")
                        },
                        onClickLogout = { navController.navigateToLoginWithVpn() },
                        onClickSystemSetting = { navController.navigateToWebview52() },
                        onClickMapSetting = { navController.navigateToSettingMap() }
                    )
                }
            }
        }
    }
}

// 辅助图标切换函数
@Composable
private fun TabIcon(active: Boolean, filled: ImageVector, outlined: ImageVector) {
    Icon(
        imageVector = if (active) filled else outlined,
        contentDescription = null
    )
}




suspend fun getList(): List<Post> {
    delay(1250)
    if ((1..3).random() < 2) {
        return emptyList()
    }
    val size = (1..7).random()
    return List(size) { index ->
        Post(
            id = index + 1,
            title = "Post Title ${index + 1}",
            body = "This is the body of post ${index + 1}.",
            tags = emptyList(),
            reactions = Reactions(likes = (0..20).random(), dislikes = (0..5).random()),
            views = (10..500).random(),
            userId = 1
        )
    }
}

