package com.template.generated.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.template.core.model.Post
import com.template.core.model.Reactions
import com.template.core.navigation.AppRoutes
import com.template.feature.map.MapScreen
import com.template.generated.LocalNavController
import com.template.generated.base.CardItem
import com.template.generated.base.UiState
import com.template.generated.component.BottomTabScreen
import com.template.generated.component.GenericTabScreen
import com.template.generated.component.ProfileHeader
import com.template.generated.component.ProfileMenuItem
import com.template.generated.component.PullRefreshOnlyList
import com.template.generated.component.SettingsGroup
import com.template.generated.component.SimpleCard
import com.template.generated.component.TabItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.listOf

fun getList(): List<Post> {
    return listOf(
        Post(
            1,
            "Post Title 111",
            "This is the body of post 1.",
            emptyList(),
            Reactions(5, 1),
            100,
            1
        ),
        Post(
            2,
            "Post Title 222-",
            "This is the body of post 2.",
            emptyList(),
            Reactions(10, 2),
            200,
            2
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppMainEntryScreen() {

//    val navController = rememberNavController()
    val navController = LocalNavController.current
    val scope = rememberCoroutineScope()

    // 状态变量
    val scrollState = rememberScrollState()
    var data by remember { mutableStateOf(UiState(getList())) }

    // 在这里组装你想要的页面，可以把 HomeScreen, SettingsScreen 传进去
    // 甚至可以给 HomeScreen 传参数，比如 HomeScreen(userId = "1001")
    BottomTabScreen(
        tabs = listOf(
            TabItem("首页", Icons.Filled.Home) { MapScreen() },
            TabItem("设置", Icons.Filled.Settings) {
                Scaffold(
                    topBar = { TopAppBar(title = { Text("列表") }) }
                ) { paddingValues ->
                    Column(modifier = Modifier.padding(paddingValues)) {
                        PullRefreshOnlyList(
                            uiState = data,
                            refresh = {
                                scope.launch {
                                    data = UiState(getList(), isLoading = true)
                                    delay(2000)
                                    data = UiState(getList())
                                }
                            },
                            content = {
                                SimpleCard(
                                    CardItem(
                                        id = it.id.toString(),
                                        title = it.title,
                                        description = it.body
                                    )
                                ){
                                    navController.navigate(AppRoutes.CUSTOM_POST_DETAIL_ROUTE)
                                }
                            },
                            key = { it.title }
                        )
                    }
                }
            },
            TabItem("我的", Icons.Filled.Person) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface) // 背景色
                        .verticalScroll(scrollState)
                ) {
                    // --- 头部区域 ---
                    ProfileHeader(
                        name = "Android 开发者",
                        email = "developer@example.com",
                        onClick = {}
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // --- 分割线 ---
                    HorizontalDivider(thickness = 8.dp, color = MaterialTheme.colorScheme.surfaceContainerLow)

                    // --- 菜单列表 ---
                    // 第一组
                    SettingsGroup(
                        title = "账户中心",
                        items = listOf(
                            ProfileMenuItem(Icons.Default.Person, "个人信息", {}),
                            ProfileMenuItem(Icons.Default.Notifications, "消息通知", {})
                        )
                    )

                    HorizontalDivider()

                    // 第二组
                    SettingsGroup(
                        title = "通用设置",
                        items = listOf(
                            ProfileMenuItem(Icons.Default.Settings, "系统设置", {}),
                            ProfileMenuItem(Icons.Default.AccountCircle, "切换账号", {}),
                            ProfileMenuItem(Icons.Outlined.Info, "关于我们", {})
                        )
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // --- 退出登录按钮 ---
                    Button(
                        onClick = { /* 处理登出 */ },
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

                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        )
    )

}