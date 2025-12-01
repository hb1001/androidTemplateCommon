package com.template.generated.vant

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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.template.core.ui.vant.VanBadge
import com.template.core.ui.vant.VanTabbar
import com.template.core.ui.vant.VanTabbarItem

@Composable
fun VanTabbarDemo() {
    // 整体页面滚动容器
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8FA))
            .padding(bottom = 100.dp) // 给底部的 Fixed Tabbar 留出空间
    ) {
        DemoTitle("Tabbar 标签栏")

        // 1. 基础用法
        DemoSection("基础用法") {
            var active by remember { mutableStateOf<Any>(0) }
            VanTabbar(
                value = active,
                onChange = { active = it },
                safeAreaInsetBottom = false,
                fixed = false // Demo 中为了展示多条，不固定在屏幕底部
            ) {
                VanTabbarItem(
                    name = 0,
                    icon = { TabIcon(it, Icons.Filled.Home, Icons.Outlined.Home) }
                ) { Text("标签") }
                VanTabbarItem(
                    name = 1,
                    icon = { TabIcon(it, Icons.Filled.Search, Icons.Outlined.Search) }
                ) { Text("标签") }
                VanTabbarItem(
                    name = 2,
                    icon = { TabIcon(it, Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder) }
                ) { Text("标签") }
                VanTabbarItem(
                    name = 3,
                    icon = { TabIcon(it, Icons.Filled.Settings, Icons.Outlined.Settings) }
                ) { Text("标签") }
            }
        }

        // 2. 通过名称匹配 (受控)
        DemoSection("通过名称匹配") {
            var activeName by remember { mutableStateOf<Any>("setting") }

            Text("当前选中: $activeName", modifier = Modifier.padding(bottom = 8.dp), fontSize = 12.sp)

            VanTabbar(
                value = activeName,
                onChange = { activeName = it },
                fixed = false
            ) {
                VanTabbarItem(
                    name = "home",
                    icon = { TabIcon(it, Icons.Filled.Home, Icons.Outlined.Home) }
                ) { Text("首页") }
                VanTabbarItem(
                    name = "search",
                    icon = { TabIcon(it, Icons.Filled.Search, Icons.Outlined.Search) }
                ) { Text("搜索") }
                VanTabbarItem(
                    name = "friends",
                    icon = { TabIcon(it, Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder) }
                ) { Text("朋友") }
                VanTabbarItem(
                    name = "setting",
                    icon = { TabIcon(it, Icons.Filled.Settings, Icons.Outlined.Settings) }
                ) { Text("设置") }
            }
        }

        // 3. 徽标提示
        DemoSection("徽标提示") {
            var active by remember { mutableStateOf<Any>(0) }
            VanTabbar(
                value = active,
                onChange = { active = it },
                fixed = false
            ) {
                VanTabbarItem(
                    name = 0,
                    icon = { TabIcon(it, Icons.Filled.Home, Icons.Outlined.Home) }
                ) { Text("标签") }

                // 红点
                VanTabbarItem(
                    name = 1,
                    icon = { TabIcon(it, Icons.Filled.Search, Icons.Outlined.Search) },
                    badge = { VanBadge(dot = true) } // 仅作为标记传入，VanTabbarItem 内部负责定位
                ) { Text("标签") }

                // 数字 5
                VanTabbarItem(
                    name = 2,
                    icon = { TabIcon(it, Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder) },
                    badge = { VanBadge(content = "5") }
                ) { Text("标签") }

                // 数字 20
                VanTabbarItem(
                    name = 3,
                    icon = { TabIcon(it, Icons.Filled.Settings, Icons.Outlined.Settings) },
                    badge = { VanBadge(content = "20") }
                ) { Text("标签") }
            }
        }

        // 4. 自定义颜色
        DemoSection("自定义颜色") {
            var active by remember { mutableStateOf<Any>(0) }
            VanTabbar(
                value = active,
                onChange = { active = it },
                activeColor = Color(0xFFEE0A24), // Vant Red
                inactiveColor = Color.Black,
                fixed = false
            ) {
                VanTabbarItem(
                    name = 0,
                    icon = { TabIcon(it, Icons.Filled.Home, Icons.Outlined.Home) }
                ) { Text("颜色") }
                VanTabbarItem(
                    name = 1,
                    icon = { TabIcon(it, Icons.Filled.Search, Icons.Outlined.Search) }
                ) { Text("颜色") }
                VanTabbarItem(
                    name = 2,
                    icon = { TabIcon(it, Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder) }
                ) { Text("颜色") }
                VanTabbarItem(
                    name = 3,
                    icon = { TabIcon(it, Icons.Filled.Settings, Icons.Outlined.Settings) }
                ) { Text("颜色") }
            }
        }

        // 5. 自定义图标 (图片)
        DemoSection("自定义图标 (图片)") {
            val iconActive = "https://img.yzcdn.cn/vant/user-active.png"
            val iconInactive = "https://img.yzcdn.cn/vant/user-inactive.png"
            var active by remember { mutableStateOf<Any>(0) }

            VanTabbar(
                value = active,
                onChange = { active = it },
                fixed = false
            ) {
                VanTabbarItem(
                    name = 0,
                    icon = { isActive ->
                        AsyncImage(
                            model = if (isActive) iconActive else iconInactive,
                            contentDescription = null
                        )
                    }
                ) { Text("自定义") }

                VanTabbarItem(
                    name = 1,
                    icon = { TabIcon(it, Icons.Filled.Search, Icons.Outlined.Search) }
                ) { Text("标签") }

                VanTabbarItem(
                    name = 2,
                    icon = { TabIcon(it, Icons.Filled.Settings, Icons.Outlined.Settings) }
                ) { Text("标签") }
            }
        }

        Spacer(Modifier.height(50.dp))
        Text(
            "提示：实际开发中，Tabbar 通常配合 Scaffold 的 bottomBar 使用，并开启 fixed=true (默认) 和 safeAreaInsetBottom=true。",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(16.dp)
        )
    }
}

// 辅助函数：根据选中状态切换实心/空心图标
@Composable
private fun TabIcon(active: Boolean, filled: ImageVector, outlined: ImageVector) {
    Icon(
        imageVector = if (active) filled else outlined,
        contentDescription = null
    )
}