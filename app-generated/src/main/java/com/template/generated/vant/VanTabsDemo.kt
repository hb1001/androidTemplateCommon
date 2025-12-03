package com.template.generated.vant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.core.ui.vant.VanBadge
import com.template.core.ui.vant.VanTabs
import com.template.core.ui.vant.VanTabsType

@Composable
fun VanTabsDemo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8FA))
            .padding(bottom = 50.dp)
    ) {
        DemoTitle("Tabs -标签页")

        // 1. 基础用法 (Line)
        DemoTitle("基础用法 (Line)")
        Box(Modifier.height(150.dp)) {
            VanTabs {
                tab(title = "标签 1") { ContentBox("内容 1") }
                tab(title = "标签 2") { ContentBox("内容 2") }
                tab(title = "标签 3") { ContentBox("内容 3") }
                tab(title = "标签 4") { ContentBox("内容 4") }
            }
        }

        // 2. 卡片风格 (Card)
        DemoTitle("卡片风格 (Card)")
        Box(Modifier.height(150.dp)) {
            VanTabs(type = VanTabsType.Card) {
                tab(title = "标签 1") { ContentBox("内容 1") }
                tab(title = "标签 2") { ContentBox("内容 2") }
                tab(title = "标签 3") { ContentBox("内容 3") }
            }
        }

        // 3. 胶囊风格 (Capsule)
        DemoTitle("胶囊风格 (Capsule)")
        Box(Modifier.height(150.dp)) {
            VanTabs(type = VanTabsType.Capsule) {
                tab(title = "标签 1") { ContentBox("内容 1") }
                tab(title = "标签 2") { ContentBox("内容 2") }
                tab(title = "标签 3") { ContentBox("内容 3") }
            }
        }

        // 4. 带描述 (Jumbo) & 徽标
        DemoTitle("带描述 (Jumbo)")
        Box(Modifier.height(200.dp)) {
            VanTabs(type = VanTabsType.Jumbo) {
                tab(title = "标签 1", description = "描述信息") { ContentBox("内容 1") }
                tab(
                    title = "标签 2",
                    description = "描述信息",
                    badge = { VanBadge(content = "5") }
                ) { ContentBox("内容 2") }
                tab(title = "标签 3", description = "描述信息") { ContentBox("内容 3") }
            }
        }

        // 5. 滑动切换 & 粘性布局
        // 注意：Sticky 在这里是相对于父容器的，由于我们在 ScrollColumn 里，
        // 这里的 sticky 实际上只会让 Header 浮在当前 Box 顶部。
        DemoTitle("滑动切换 (Swipeable)")
        Box(Modifier.height(250.dp)) {
            VanTabs(
                swipeable = true,
                sticky = true
            ) {
                (1..6).forEach { i ->
                    tab(title = "标签 $i") { ContentBox("支持手势滑动的内容 $i") }
                }
            }
        }

        // 6. 禁用 & Name 匹配
        DemoTitle("禁用 & Name 匹配")
        var activeName by remember { mutableStateOf<Any>("b") }
        Text("当前选中 Name: $activeName", Modifier.padding(horizontal = 16.dp), fontSize = 12.sp)

        Box(Modifier.height(150.dp)) {
            VanTabs(
                active = activeName,
                onChange = { activeName = it }
            ) {
                tab(title = "标签 A", name = "a") { ContentBox("内容 A") }
                tab(title = "标签 B", name = "b") { ContentBox("内容 B") }
                tab(title = "禁用 C", name = "c", disabled = true) { ContentBox("内容 C") }
            }
        }
    }
}

@Composable
fun ContentBox(text: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White, androidx.compose.foundation.shape.RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontSize = 16.sp, color = Color.Gray)
    }
}