package com.template.generated.vant

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.template.core.ui.vant.VanCheckbox
import com.template.core.ui.vant.VanCheckboxDirection
import com.template.core.ui.vant.VanCheckboxGroup
import com.template.core.ui.vant.VanCheckboxShape
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun VanCheckboxDemo() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8FA)) // Vant 背景灰
            .padding(bottom = 40.dp)
    ) {
        DemoTitle("基础用法")
        BasicUsage()

        DemoTitle("自定义样式")
        CustomStyle()

        DemoTitle("异步更新")
        AsyncUpdate()

        DemoTitle("复选框组 (Vertical)")
        CheckboxGroupDemo()

        DemoTitle("复选框组 (Horizontal)")
        CheckboxGroupHorizontalDemo()

        DemoTitle("限制最大可选数 (Max = 2)")
        CheckboxGroupMaxDemo()

        DemoTitle("全选与反选")
        CheckAllDemo()

        DemoTitle("搭配单元格")
        CellIntegrationDemo()
    }
}

// --- 1. 基础用法 ---
@Composable
private fun BasicUsage() {
    var checked1 by remember { mutableStateOf(false) }
    var checked2 by remember { mutableStateOf(true) }

    Column(
        Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        VanCheckbox(
            checked = checked1,
            onChange = { checked1 = it }
        ) {
            Text("复选框 (Status: $checked1)")
        }

        VanCheckbox(
            checked = checked2,
            onChange = { checked2 = it }
        ) {
            Text("默认勾选")
        }

        VanCheckbox(checked = false, disabled = true) {
            Text("禁用复选框")
        }

        VanCheckbox(checked = true, disabled = true) {
            Text("禁用且勾选")
        }

        var checkedLabel by remember { mutableStateOf(true) }
        VanCheckbox(
            checked = checkedLabel,
            onChange = { checkedLabel = it },
            labelDisabled = true
        ) {
            Text("禁止文本点击 (只能点框)")
        }
    }
}

// --- 2. 自定义样式 ---
@Composable
private fun CustomStyle() {
    var checked1 by remember { mutableStateOf(true) }
    var checked2 by remember { mutableStateOf(true) }
    var checked3 by remember { mutableStateOf(true) }
    var checked4 by remember { mutableStateOf(true) }

    Column(
        Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        VanCheckbox(
            checked = checked1,
            onChange = { checked1 = it },
            shape = VanCheckboxShape.Square
        ) {
            Text("自定义形状 (Square)")
        }

        VanCheckbox(
            checked = checked2,
            onChange = { checked2 = it },
            checkedColor = Color(0xFFEE0A24)
        ) {
            Text("自定义颜色 (Red)")
        }

        VanCheckbox(
            checked = checked3,
            onChange = { checked3 = it },
            iconSize = 24.dp
        ) {
            Text("自定义大小 (24dp)")
        }

        // 自定义图标 Render
        val activeIcon = "https://img.yzcdn.cn/vant/user-active.png"
        val inactiveIcon = "https://img.yzcdn.cn/vant/user-inactive.png"

        VanCheckbox(
            checked = checked4,
            onChange = { checked4 = it },
            iconRender = { checked, _ ->
                val url = if (checked) activeIcon else inactiveIcon
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        ) {
            Text("自定义图标 (网络图)")
        }
    }
}

// --- 3. 异步更新 ---
@Composable
private fun AsyncUpdate() {
    var checked by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(Modifier.padding(horizontal = 16.dp)) {
        VanCheckbox(
            checked = checked,
            onChange = { newVal ->
                if (!loading) {
                    loading = true
                    // 模拟网络请求
                    scope.launch {
                        delay(500)
                        checked = newVal
                        loading = false
                    }
                }
            }
        ) {
            Text(if (loading) "更新中..." else "复选框 (延迟500ms)")
        }
    }
}

// --- 4. 复选框组 ---
@Composable
private fun CheckboxGroupDemo() {
    var values by remember { mutableStateOf(setOf("a", "b")) } // 默认选 a, b

    Column(Modifier.padding(horizontal = 16.dp)) {
        Text(
            "当前值: $values",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        VanCheckboxGroup(
            value = values,
            onChange = { values = it }
        ) {
            VanCheckbox(name = "a") { Text("复选框 A") }
            VanCheckbox(name = "b") { Text("复选框 B") }
            VanCheckbox(name = "c") { Text("复选框 C") }
        }
    }
}

// --- 5. 水平排列 ---
@Composable
private fun CheckboxGroupHorizontalDemo() {
    var values by remember { mutableStateOf(setOf<String>()) }

    Column(Modifier.padding(horizontal = 16.dp)) {
        VanCheckboxGroup(
            value = values,
            onChange = { values = it },
            direction = VanCheckboxDirection.Horizontal
        ) {
            VanCheckbox(name = "a") { Text("复选框 A") }
            VanCheckbox(name = "b") { Text("复选框 B") }
            VanCheckbox(name = "c") { Text("复选框 C") }
        }
    }
}

// --- 6. 限制最大可选数 ---
@Composable
private fun CheckboxGroupMaxDemo() {
    var values by remember { mutableStateOf(setOf<String>()) }

    Column(Modifier.padding(horizontal = 16.dp)) {
        VanCheckboxGroup(
            value = values,
            onChange = { values = it },
            max = 2
        ) {
            VanCheckbox(name = "a") { Text("复选框 A") }
            VanCheckbox(name = "b") { Text("复选框 B") }
            VanCheckbox(name = "c") { Text("复选框 C (最多选2个)") }
        }
    }
}

// --- 7. 全选与反选 ---
@Composable
private fun CheckAllDemo() {
    val allItems = listOf("a", "b", "c")
    var values by remember { mutableStateOf(setOf("a")) }

    Column(Modifier.padding(horizontal = 16.dp)) {
        VanCheckboxGroup(
            value = values,
            onChange = { values = it }
        ) {
            allItems.forEach { item ->
                VanCheckbox(name = item) { Text("复选框 $item") }
            }
        }

        Row(Modifier.padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = { values = allItems.toSet() }) {
                Text("全选")
            }
            Button(onClick = {
                // 反选逻辑：当前有的去掉，没有的加上
                val newSet = allItems.filter { !values.contains(it) }.toSet()
                values = newSet
            }) {
                Text("反选")
            }
        }
    }
}

// --- 8. 搭配单元格 ---
// 简单模拟 VanCell，实际项目中请引用真正的 Cell 组件
@Composable
private fun CellIntegrationDemo() {
    var values by remember { mutableStateOf(setOf<String>()) }

    // 辅助函数：切换某个 key
    fun toggle(key: String) {
        val newSet = values.toMutableSet()
        if (newSet.contains(key)) newSet.remove(key) else newSet.add(key)
        values = newSet
    }

    Column(Modifier.fillMaxWidth()) {
        VanCheckboxGroup(value = values, onChange = { values = it }) {
            // 模拟 CellGroup
            Column(Modifier.background(Color.White)) {

                // Cell 1
                MockCell(
                    title = "单选框 1",
                    onClick = { toggle("a") },
                    rightIcon = { VanCheckbox(name = "a") }
                )
                Divider(color = Color(0xFFF5F6F7))

                // Cell 2
                MockCell(
                    title = "单选框 2",
                    onClick = { toggle("b") },
                    rightIcon = { VanCheckbox(name = "b") }
                )
            }
        }
    }
}

@Composable
fun MockCell(
    title: String,
    onClick: () -> Unit,
    rightIcon: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 16.sp)
        rightIcon()
    }
}

@Composable
fun DemoTitle(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 10.dp),
        color = Color.Gray,
        fontSize = 14.sp
    )
}

