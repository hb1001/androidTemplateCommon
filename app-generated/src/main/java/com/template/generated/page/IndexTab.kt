package com.template.generated.page

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.template.core.model.Post
import com.template.core.model.Reactions
import com.template.core.navigation.AppRoutes
import com.template.feature.atrust.navigation.navigateToLoginWithVpn
import com.template.feature.map.MapScreen
import com.template.generated.LocalNavController
import com.template.core.ui.uimodel.CardItem
import com.template.core.ui.uimodel.UiState
import com.template.core.ui.components.BottomTabScreen
import com.template.core.ui.components.CommonTitleBar
import com.template.core.ui.components.CustomLoadingDialog
import com.template.core.ui.components.LogoutDialog
import com.template.core.ui.components.MaxWidthCard
import com.template.core.ui.components.PaymentBottomSheet
import com.template.core.ui.components.PullRefreshOnlyList
import com.template.core.ui.components.TabItem
import com.template.feature.setting.ProfilePage
import com.template.feature.setting.navigation.navigateToSettingSingle
import com.template.feature.webview.navigation.navigateToWebview52
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.listOf

suspend fun getList(): List<Post> {
    delay(250)
    if ((1..7).random() < 5) {
        return emptyList<Post>()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppMainEntryScreen() {

//    val navController = rememberNavController()
//    val navController = rememberNavController()
    val navController = LocalNavController.current
    val scope = rememberCoroutineScope()

    // 状态变量
    val scrollState = rememberScrollState()
    var data by remember { mutableStateOf(UiState(emptyList<Post>())) }
    var showDialog1 by remember { mutableStateOf(false) }
    var showDialog2 by remember { mutableStateOf(false) }
    var showDialog3 by remember { mutableStateOf(false) }
    var showDialog4 by remember { mutableStateOf(false) }

    // 在这里组装你想要的页面，可以把 HomeScreen, SettingsScreen 传进去
    // 甚至可以给 HomeScreen 传参数，比如 HomeScreen(userId = "1001")
    BottomTabScreen(
        tabs = listOf(
            TabItem("首页", Icons.Filled.Home) { MapScreen() },
            TabItem("列表", Icons.Filled.Settings) {
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
            },
            TabItem("我的", Icons.Filled.Person) {
                ProfilePage(
                    navController = navController,
                    onClickProfileInfo = {
                        navController.navigateToSettingSingle(
                            "姓名",
                            "张三"
                        )
                    },
                    onClickLogout = { navController.navigateToLoginWithVpn() },
                    onClickSystemSetting = {
                        navController.navigateToWebview52()
                    }
                )
            }
        )
    )

}