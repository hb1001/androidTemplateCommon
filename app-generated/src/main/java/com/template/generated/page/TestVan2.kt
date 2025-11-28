package com.template.generated.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.template.core.ui.components.CommonTitleBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.core.ui.components.CommonTitleBar
// 请确保 VanButton, VanCell, VanCellGroup 等都在这个包下，或者根据你的实际路径修改import
import com.template.core.ui.vant.*

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestVan() {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                CommonTitleBar(title = "测试 Vant 组件", showBack = false)
            })
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFF7F8FA)) // 设置淡灰色背景，方便查看 Cell 效果
                .verticalScroll(rememberScrollState())
                .padding(bottom = 40.dp) // 底部留白
        ) {
//            VanTypographyDemo()
            VanRadioDemo()
//            VanButtons()
//            Spacer(modifier = Modifier.height(20.dp))
//            VanCollapses()

        }
    }
}


@Composable
fun VanRadioDemo() {
    // 外层容器，不包含滚动，滚动由父级 TestVan 提供
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Radio 单选框", color = Color.Gray, fontSize = 14.sp)

        // 1. 基础用法
        DemoSection("基础用法") {
            var value by remember { mutableStateOf("1") }
            VanRadioGroup(value = value, onChange = { value = it }) {
                VanRadio(name = "1") { Text("单选框 1") }
                VanRadio(name = "2") { Text("单选框 2") }
            }
        }

        // 2. 水平排列
        DemoSection("水平排列") {
            var value by remember { mutableStateOf("1") }
            VanRadioGroup(
                value = value,
                onChange = { value = it },
                direction = VanRadioDirection.Horizontal
            ) {
                VanRadio(name = "1") { Text("单选框 1") }
                VanRadio(name = "2") { Text("单选框 2") }
            }
        }

        // 3. 禁用状态
        DemoSection("禁用状态") {
            var value by remember { mutableStateOf("1") }
            VanRadioGroup(value = value, onChange = { value = it }) {
                VanRadio(name = "1", disabled = true) { Text("单选框 1 (Disabled)") }
                VanRadio(name = "2", disabled = true) { Text("单选框 2 (Disabled)") }
            }
        }

        // 4. 自定义形状 (Square)
        DemoSection("自定义形状 (Square)") {
            var value by remember { mutableStateOf("1") }
            VanRadioGroup(value = value, onChange = { value = it }) {
                VanRadio(name = "1", shape = VanRadioShape.Square) { Text("单选框 1") }
                VanRadio(name = "2", shape = VanRadioShape.Square) { Text("单选框 2") }
            }
        }

        // 5. 自定义颜色
        DemoSection("自定义颜色") {
            var value by remember { mutableStateOf("1") }
            VanRadioGroup(value = value, onChange = { value = it }) {
                VanRadio(name = "1", checkedColor = Color(0xFFEE0A24)) { Text("单选框 1") }
                VanRadio(name = "2", checkedColor = Color(0xFFEE0A24)) { Text("单选框 2") }
            }
        }

        // 6. 自定义大小
        DemoSection("自定义大小") {
            var value by remember { mutableStateOf("1") }
            VanRadioGroup(value = value, onChange = { value = it }) {
                VanRadio(name = "1", iconSize = 24.dp) { Text("单选框 1 (24dp)") }
                VanRadio(name = "2", iconSize = 24.dp) { Text("单选框 2 (24dp)") }
            }
        }

        // 7. 禁用文本点击
        DemoSection("禁用文本点击") {
            var value by remember { mutableStateOf("1") }
            VanRadioGroup(value = value, onChange = { value = it }) {
                VanRadio(name = "1", labelDisabled = true) { Text("单选框 1 (只能点图标)") }
                VanRadio(name = "2", labelDisabled = true) { Text("单选框 2 (只能点图标)") }
            }
        }

        // 8. 异步更新
        DemoSection("异步更新") {
            var value by remember { mutableStateOf("1") }
            var loading by remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()

            VanRadioGroup(
                value = value,
                onChange = { newValue ->
                    if (!loading && newValue != value) {
                        loading = true
                        scope.launch {
                            delay(500) // 模拟网络请求
                            value = newValue
                            loading = false
                        }
                    }
                }
            ) {
                VanRadio(name = "1") { Text("单选框 1") }
                VanRadio(name = "2") { Text("单选框 2") }
            }
            if (loading) {
                Row(Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                    Spacer(Modifier.width(8.dp))
                    Text("更新中...", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        // 9. 搭配单元格组件
        DemoSection("搭配单元格组件") {
            var cellValue by remember { mutableStateOf("1") }

            // 使用 VanCellGroup 和 VanCell
            // 注意：VanCell 需要处理 onClick 来更新 Radio 的状态
            VanRadioGroup(value = cellValue, onChange = { cellValue = it }) {
                VanCellGroup {
                    VanCell(
                        title = "单选框 1",
                        clickable = true,
                        onClick = { cellValue = "1" }, // Cell 点击触发更新
                        rightIconComposable = {
                            VanRadio(name = "1") // 这里的 VanRadio 只负责显示状态
                        }
                    )
                    VanCell(
                        title = "单选框 2",
                        clickable = true,
                        onClick = { cellValue = "2" },
                        rightIconComposable = {
                            VanRadio(name = "2")
                        }
                    )
                }
            }
        }
    }
}