package com.template.generated.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.template.core.model.Post
import com.template.core.model.Reactions
import com.template.core.navigation.AppRoutes
import com.template.feature.map.MapScreen
import com.template.generated.base.CardItem
import com.template.generated.base.UiState
import com.template.generated.component.BottomTabScreen
import com.template.generated.component.GenericTabScreen
import com.template.generated.component.PullRefreshOnlyList
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

@Composable
fun AppMainEntryScreen() {

    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    var data by remember { mutableStateOf(UiState(getList())) }

    // 在这里组装你想要的页面，可以把 HomeScreen, SettingsScreen 传进去
    // 甚至可以给 HomeScreen 传参数，比如 HomeScreen(userId = "1001")
    BottomTabScreen(
        tabs = listOf(
            TabItem("首页", Icons.Filled.Home) { MapScreen() },
            TabItem("设置", Icons.Filled.Settings) {
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
            },
            TabItem("我的", Icons.Filled.Person) { ProfileScreen() }
        )
    )

}

@Composable
fun SettingsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("设置页面", style = MaterialTheme.typography.headlineLarge)
    }
}

@Composable
fun ProfileScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("个人中心", style = MaterialTheme.typography.headlineLarge)
    }
}