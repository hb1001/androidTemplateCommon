package com.template.generated.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.core.ui.components.CommonTitleBar
// 请确保 VanButton, VanCell, VanCellGroup 等都在这个包下，或者根据你的实际路径修改import
import com.template.core.ui.vant.*

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import coil.compose.AsyncImage

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestVan1() {
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
            VanTypographyDemo()
//            VanButtons()
//            Spacer(modifier = Modifier.height(20.dp))
//            VanCollapses()

        }
    }
}

@Composable
fun VanTypographyDemo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Typography 文本", color = Color.Gray, fontSize = 14.sp)

        // 1. 基础用法
        DemoSection("基础用法 (AnnotatedString)") {
            // 模拟 React Vant 的嵌套结构：
            // In the process of <Text type="danger">internal</Text> ...
            val text = buildAnnotatedString {
                append("In the process of ")
                withStyle(SpanStyle(color = VanTypographyColors.Danger)) {
                    append("internal")
                }
                append(" ")
                withStyle(SpanStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough)) {
                    append("desktop")
                }
                append(" applications development, ")
                withStyle(SpanStyle(color = VanTypographyColors.Primary)) {
                    append("many different")
                }
                append(" design specs and ")
                withStyle(SpanStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline)) {
                    append("implementations")
                }
                append(" would be ")
                withStyle(SpanStyle(color = VanTypographyColors.Warning)) {
                    append("involved")
                }
            }
            VanTypography(text = text)
        }

        // 2. 文本省略
        val content = "React Vant 是一套轻量、可靠的移动端 React 组件库，提供了丰富的基础组件和业务组件，帮助开发者快速搭建移动应用，使用过程中发现任何问题都可以提 Issue 给我们，当然，我们也非常欢迎你给我们发 PR。"

        DemoSection("文本省略 (Ellipsis)") {
            // 单行省略
            Text("单行省略:", fontSize = 12.sp, color = Color.Gray)
            VanTypography(
                text = content,
                ellipsis = VanEllipsisConfig(rows = 1)
            )
            Divider(Modifier.padding(vertical = 8.dp))

            // 多行省略
            Text("多行省略 (Rows=2):", fontSize = 12.sp, color = Color.Gray)
            VanTypography(
                text = content,
                ellipsis = VanEllipsisConfig(rows = 2)
            )
            Divider(Modifier.padding(vertical = 8.dp))

            // 带展开/收起
            Text("带展开/收起:", fontSize = 12.sp, color = Color.Gray)
            VanTypography(
                text = content,
                ellipsis = VanEllipsisConfig(
                    rows = 2,
                    collapseText = "收起",
                    expandText = "展开"
                )
            )
            Divider(Modifier.padding(vertical = 8.dp))

            // 保留末位文本 (如文件名)
            Text("保留末位文本 (中间省略):", fontSize = 12.sp, color = Color.Gray)
            VanTypography(
                text = content,
                ellipsis = VanEllipsisConfig(
                    rows = 2,
                    symbol = "......",
                    suffixCount = 10 // 保留最后10个字
                )
            )
            Divider(Modifier.padding(vertical = 8.dp))

            // 自定义后缀
            Text("自定义文本后缀:", fontSize = 12.sp, color = Color.Gray)
            VanTypography(
                text = content,
                ellipsis = VanEllipsisConfig(
                    rows = 2,
                    suffixText = "--William",
                    expandText = "更多"
                )
            )
        }

        // 3. 标题
        DemoSection("标题 (Title)") {
            VanTitle(text = "一级测试标题 (Level 1)", level = VanTypographyLevel.L1)
            VanTitle(text = "二级测试标题 (Level 2)", level = VanTypographyLevel.L2)
            VanTitle(text = "三级测试标题 (Level 3)", level = VanTypographyLevel.L3)
            VanTitle(text = "四级测试标题 (Level 4)", level = VanTypographyLevel.L4)
            VanTitle(text = "五级测试标题 (Level 5)", level = VanTypographyLevel.L5)
            VanTitle(text = "六级测试标题 (Level 6)", level = VanTypographyLevel.L6)
        }

        // 4. 链接
        DemoSection("链接 (Link)") {
            VanLink(
                text = "测试 Link (Open Google)",
                url = "https://www.google.com"
            )
            Spacer(Modifier.height(8.dp))
            VanLink(
                text = "测试 Link (Underline)",
                underline = true,
                onClick = { /* Handle click */ }
            )
        }

        // 5. 样式变量模拟
        DemoSection("类型样式 (Types)") {
            VanTypography(text = "Default Type")
            VanTypography(text = "Primary Type", type = VanTypographyType.Primary)
            VanTypography(text = "Success Type", type = VanTypographyType.Success)
            VanTypography(text = "Danger Type", type = VanTypographyType.Danger)
            VanTypography(text = "Warning Type", type = VanTypographyType.Warning)
            VanTypography(text = "Secondary Type", type = VanTypographyType.Secondary)
            VanTypography(text = "Disabled Text", disabled = true)
        }
    }
}

@Composable
fun DemoSection(title: String, padding:Boolean = false, content: @Composable () -> Unit) {
    Column {
        Text(title, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .background(Color.White)
                .padding(12.dp)
        ) {
            Column {
                content()
            }
        }
    }
}




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

    Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
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

    Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
        Text("当前值: $values", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))

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



@Composable
fun VanSliders() {
    // 状态
    var value1 by remember { mutableFloatStateOf(50f) }
    var valueRange by remember { mutableStateOf(listOf(20f, 60f)) }
    var valueStep by remember { mutableFloatStateOf(0f) }
    var valueVertical by remember { mutableFloatStateOf(50f) }
    var valueCustom by remember { mutableFloatStateOf(30f) }

    Column {
        Text(
            "Slider 滑块",
            modifier = Modifier.padding(16.dp),
            color = Color.Gray,
            fontSize = 14.sp
        )

        // 1. 基础用法
        Text("基础用法: ${value1.toInt()}", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(start = 16.dp, bottom = 8.dp))
        Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)) {
            VanSlider(
                value = value1,
                onValueChange = { value1 = it as Float }
            )
        }

        // 2. 双滑块
        Text("双滑块: ${valueRange[0].toInt()} - ${valueRange[1].toInt()}", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(start = 16.dp, bottom = 8.dp))
        Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)) {
            VanSlider(
                range = true,
                value = valueRange,
                onValueChange = { valueRange = it as List<Float> }
            )
        }

        // 3. 指定步长
        Text("指定步长 (Step=10): ${valueStep.toInt()}", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(start = 16.dp, bottom = 8.dp))
        Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)) {
            VanSlider(
                step = 10f,
                value = valueStep,
                onValueChange = { valueStep = it as Float }
            )
        }

        // 4. 自定义样式 & 按钮
        Text("自定义样式 & 按钮", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(start = 16.dp, bottom = 8.dp))
        Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)) {
            VanSlider(
                value = valueCustom,
                activeColor = Color(0xFFEE0A24),
                barHeight = 4.dp,
                onValueChange = { valueCustom = it as Float },
                button = {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFEE0A24), RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${valueCustom.toInt()}",
                            color = Color.White,
                            fontSize = 10.sp
                        )
                    }
                }
            )
        }

        // 5. 垂直方向
        Text("垂直方向", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(start = 16.dp, bottom = 8.dp))
        Row(
            modifier = Modifier
                .height(200.dp) // 必须给高度
                .padding(start = 30.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            VanSlider(
                vertical = true,
                value = valueVertical,
                onValueChange = { valueVertical = it as Float }
            )

            // 垂直双滑块
            VanSlider(
                vertical = true,
                range = true,
                value = valueRange, // 复用之前的 Range 值
                onValueChange = { valueRange = it as List<Float> }
            )
        }
    }
}
@Composable
fun VanSwitches() {
    // 状态管理
    var checked1 by remember { mutableStateOf(true) }
    var checked2 by remember { mutableStateOf(true) }
    var checked3 by remember { mutableStateOf(true) }
    var checked4 by remember { mutableStateOf(true) }
    var checked5 by remember { mutableStateOf(true) }

    // 异步控制模拟
    var checkedAsync by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    // 模拟 Dialog (这里用 AlertDialog 简单替代)
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("提醒") },
            text = { Text("是否切换开关？") },
            confirmButton = {
                TextButton(onClick = {
                    checkedAsync = !checkedAsync
                    showDialog = false
                }) { Text("确认") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("取消") }
            }
        )
    }

    Column {
        Text(
            "Switch 开关",
            modifier = Modifier.padding(16.dp),
            color = Color.Gray,
            fontSize = 14.sp
        )

        // 1. 基础用法
        VanCellGroup(title = "基础用法") {
            VanCell(title = "基础用法", rightIconComposable = {
                VanSwitch(checked = checked1, onCheckedChange = { checked1 = it })
            })
        }

        // 2. 禁用状态
        VanCellGroup(title = "禁用状态") {
            VanCell(title = "禁用状态", rightIconComposable = {
                VanSwitch(checked = checked2, onCheckedChange = { checked2 = it }, disabled = true)
            })
        }

        // 3. 加载状态
        VanCellGroup(title = "加载状态") {
            VanCell(title = "加载状态", rightIconComposable = {
                VanSwitch(checked = checked3, onCheckedChange = { checked3 = it }, loading = true)
            })
        }

        // 4. 自定义大小
        VanCellGroup(title = "自定义大小") {
            VanCell(title = "自定义大小 (24dp)", rightIconComposable = {
                VanSwitch(checked = checked4, onCheckedChange = { checked4 = it }, size = 24.dp)
            })
        }

        // 5. 自定义颜色
        VanCellGroup(title = "自定义颜色") {
            VanCell(title = "自定义颜色", rightIconComposable = {
                VanSwitch(
                    checked = checked5,
                    onCheckedChange = { checked5 = it },
                    activeColor = Color(0xFFEE0A24),
                    inactiveColor = Color(0xFFDCDEE0)
                )
            })
        }

        // 6. 异步控制
        VanCellGroup(title = "异步控制") {
            VanCell(title = "异步控制", rightIconComposable = {
                VanSwitch(
                    checked = checkedAsync,
                    onCheckedChange = {
                        // 不直接修改 checkedAsync，而是弹窗
                        showDialog = true
                    }
                )
            })
        }
    }
}

@Composable
fun VanTags() {
    // 状态：用于控制可关闭标签的显示
    var showCloseableTag by remember { mutableStateOf(true) }

    Column {
        Text(
            "Tag 标签",
            modifier = Modifier.padding(16.dp),
            color = Color.Gray,
            fontSize = 14.sp
        )

        // 1. 基础用法
        VanCellGroup(title = "基础用法") {
            VanCell(title = "primary 类型", valueComposable = {
                VanTag(type = VanTagType.Primary) { Text("标签") }
            })
            VanCell(title = "success 类型", valueComposable = {
                VanTag(type = VanTagType.Success) { Text("标签") }
            })
            VanCell(title = "danger 类型", valueComposable = {
                VanTag(type = VanTagType.Danger) { Text("标签") }
            })
            VanCell(title = "warning 类型", valueComposable = {
                VanTag(type = VanTagType.Warning) { Text("标签") }
            })
        }

        // 2. 样式风格
        VanCellGroup(title = "样式风格") {
            VanCell(title = "空心样式", valueComposable = {
                VanTag(plain = true, type = VanTagType.Primary) { Text("标签") }
            })
            VanCell(title = "圆角样式", valueComposable = {
                VanTag(round = true, type = VanTagType.Primary) { Text("标签") }
            })
            VanCell(title = "标记样式", valueComposable = {
                VanTag(mark = true, type = VanTagType.Primary) { Text("标签") }
            })
            VanCell(title = "可关闭标签", valueComposable = {
                VanTag(
                    show = showCloseableTag,
                    plain = true,
                    closeable = true,
                    size = VanTagSize.Medium,
                    type = VanTagType.Primary,
                    onClose = { showCloseableTag = false }
                ) {
                    Text("标签")
                }
                // 为了演示效果，如果关闭了提供一个重置按钮
                if (!showCloseableTag) {
                    VanButton(size = VanButtonSize.Mini, type = VanButtonType.Default, text = "重置", onClick = { showCloseableTag = true })
                }
            })
        }

        // 3. 标签大小
        VanCellGroup(title = "标签大小") {
            VanCell(title = "小号标签", valueComposable = {
                VanTag(type = VanTagType.Primary, size = VanTagSize.Small) { Text("标签") }
            })
            VanCell(title = "中号标签", valueComposable = {
                VanTag(type = VanTagType.Primary, size = VanTagSize.Medium) { Text("标签") }
            })
            VanCell(title = "大号标签", valueComposable = {
                VanTag(type = VanTagType.Primary, size = VanTagSize.Large) { Text("标签") }
            })
        }

        // 4. 自定义颜色
        VanCellGroup(title = "自定义颜色") {
            VanCell(title = "背景颜色", valueComposable = {
                VanTag(color = Color(0xFF7232DD)) { Text("标签") }
            })
            VanCell(title = "文字颜色", valueComposable = {
                VanTag(color = Color(0xFFFFE1E1), textColor = Color(0xFFAD0000)) { Text("标签") }
            })
            VanCell(title = "空心颜色", valueComposable = {
                VanTag(color = Color(0xFF7232DD), plain = true) { Text("标签") }
            })
        }
    }
}
@Composable
fun VanSwipes() {
    Column {
        Text(
            "Swipe 轮播",
            modifier = Modifier.padding(16.dp),
            color = Color.Gray,
            fontSize = 14.sp
        )

        // 1. 基础用法 (自动播放)
        Text("基础用法 (Autoplay)", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(start = 16.dp, bottom = 8.dp))
        VanSwipe(
            itemCount = 4,
            autoplay = 3000,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) // 必须指定高度
        ) { index ->
            // 模拟不同背景色
            val color = when (index) {
                0 -> Color(0xFF39A9ED)
                1 -> Color(0xFF66C6F2)
                2 -> Color(0xFF39A9ED)
                else -> Color(0xFF66C6F2)
            }
            VanSwipeItem(modifier = Modifier.background(color)) {
                Text(text = "${index + 1}", color = Color.White, fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 2. 纵向滚动
        Text("纵向滚动", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(start = 16.dp, bottom = 8.dp))
        VanSwipe(
            itemCount = 4,
            vertical = true,
            autoplay = 3000,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { index ->
            val color = when (index) {
                0 -> Color(0xFF39A9ED)
                1 -> Color(0xFF66C6F2)
                2 -> Color(0xFF39A9ED)
                else -> Color(0xFF66C6F2)
            }
            VanSwipeItem(modifier = Modifier.background(color)) {
                Text(text = "Vertical ${index + 1}", color = Color.White, fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3. 自定义滑块大小 (一屏多页)
        Text("自定义滑块大小 (Loop=false)", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(start = 16.dp, bottom = 8.dp))
        VanSwipe(
            itemCount = 4,
            loop = false,
            width = 300.dp, // 设置固定宽度
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { index ->
            // 给个 margin 来看出间隙
            Box(modifier = Modifier.padding(end = 10.dp).fillMaxSize()) {
                val color = when (index) {
                    0 -> Color(0xFF39A9ED)
                    1 -> Color(0xFF66C6F2)
                    2 -> Color(0xFF39A9ED)
                    else -> Color(0xFF66C6F2)
                }
                VanSwipeItem(modifier = Modifier.background(color)) {
                    Text(text = "${index + 1}", color = Color.White, fontSize = 20.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 4. 自定义指示器
        Text("自定义指示器", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(start = 16.dp, bottom = 8.dp))
        VanSwipe(
            itemCount = 4,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            indicator = { active, total ->
                // 自定义右上角数字指示器
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp)
                        .background(Color(0x33000000), shape = RoundedCornerShape(4.dp))
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${active + 1}/$total",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        ) { index ->
            val color = when (index) {
                0 -> Color(0xFF39A9ED)
                1 -> Color(0xFF66C6F2)
                2 -> Color(0xFF39A9ED)
                else -> Color(0xFF66C6F2)
            }
            VanSwipeItem(modifier = Modifier.background(color)) {
                Text(text = "${index + 1}", color = Color.White, fontSize = 20.sp)
            }
        }
    }
}
@Composable
fun VanCollapses() {
    // --- 状态管理 ---
    // 1. 基础用法 (多选)
    var activeNames1 by remember { mutableStateOf(setOf("1")) }

    // 2. 手风琴 (单选)
    var activeNames2 by remember { mutableStateOf(setOf("1")) }

    // 3. 禁用状态
    var activeNames3 by remember { mutableStateOf(setOf("1")) }

    // 4. 全部展开/切换控制
    var activeNames4 by remember { mutableStateOf(setOf<String>()) }

    Column {
        Text(
            "Collapse 折叠面板",
            modifier = Modifier.padding(16.dp),
            color = Color.Gray,
            fontSize = 14.sp
        )

        // 1. 基础用法
        VanCellGroup(title = "基础用法") {
            VanCollapse(
                activeNames = activeNames1,
                onChange = { activeNames1 = it }
            ) {
                VanCollapseItem(title = "标题1", name = "1") {
                    Text("代码是写出来给人看的，附带能在机器上运行。")
                }
                VanCollapseItem(title = "标题2", name = "2") {
                    Text("技术无非就是那些开发它的人的共同灵魂。")
                }
                VanCollapseItem(title = "标题3", name = "3") {
                    Text("在代码阅读过程中人们说脏话的频率是衡量代码质量的唯一标准。")
                }
            }
        }

        // 2. 手风琴
        VanCellGroup(title = "手风琴 (只能展开一个)") {
            VanCollapse(
                activeNames = activeNames2,
                onChange = { activeNames2 = it },
                accordion = true // 开启手风琴
            ) {
                VanCollapseItem(title = "标题1", name = "1") {
                    Text("代码是写出来给人看的，附带能在机器上运行。")
                }
                VanCollapseItem(title = "标题2", name = "2") {
                    Text("技术无非就是那些开发它的人的共同灵魂。")
                }
                VanCollapseItem(title = "标题3", name = "3") {
                    Text("在代码阅读过程中人们说脏话的频率是衡量代码质量的唯一标准。")
                }
            }
        }

        // 3. 禁用状态与自定义标题
        VanCellGroup(title = "禁用与自定义标题") {
            VanCollapse(
                activeNames = activeNames3,
                onChange = { activeNames3 = it }
            ) {
                VanCollapseItem(title = "标题1", name = "1") {
                    Text("正常内容")
                }
                VanCollapseItem(title = "标题2 (禁用)", name = "2", disabled = true) {
                    Text("这部分内容无法点击展开")
                }
                // 自定义标题插槽
                VanCollapseItem(
                    name = "3",
                    titleComposable = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("标题3")
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Filled.Info, null, tint = Color.Blue, modifier = Modifier.size(14.dp))
                        }
                    }
                ) {
                    Text("通过插槽自定义了标题内容，带了一个小图标。")
                }
            }
        }

        // 4. 外部控制 (Toggle All)
        VanCellGroup(title = "外部控制 (全部展开/切换)") {
            VanCollapse(
                activeNames = activeNames4,
                onChange = { activeNames4 = it }
            ) {
                VanCollapseItem(title = "标题1", name = "1") { Text("内容1") }
                VanCollapseItem(title = "标题2", name = "2") { Text("内容2") }
            }

            // 控制按钮区域
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                VanButton(
                    type = VanButtonType.Primary,
                    size = VanButtonSize.Small,
                    text = "全部展开",
                    onClick = { activeNames4 = setOf("1", "2") }
                )
                VanButton(
                    type = VanButtonType.Default,
                    size = VanButtonSize.Small,
                    text = "全部收起",
                    onClick = { activeNames4 = emptySet() }
                )
            }
        }
    }
}

@Composable
fun VanBadges() {
    Text(
        "Badge 徽标",
        modifier = Modifier.padding(16.dp),
        color = Color.Gray,
        fontSize = 14.sp
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp) // 行间距
    ) {
        // 1. 基础用法
        Text("基础用法", fontSize = 14.sp, color = Color.Gray)
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            VanBadge(count = 5) { BadgeChildBox() }
            VanBadge(count = 10) { BadgeChildBox() }
            VanBadge(content = "Hot") { BadgeChildBox() }
            VanBadge(dot = true) { BadgeChildBox() }
        }

        // 2. 最大值
        Text("最大值", fontSize = 14.sp, color = Color.Gray)
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            VanBadge(count = 20, max = 9) { BadgeChildBox() }
            VanBadge(count = 50, max = 20) { BadgeChildBox() }
            VanBadge(count = 200, max = 99) { BadgeChildBox() }
        }

        // 3. 自定义颜色
        Text("自定义颜色", fontSize = 14.sp, color = Color.Gray)
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            VanBadge(count = 5, color = Color(0xFF1989FA)) { BadgeChildBox() }
            VanBadge(count = 10, color = Color(0xFF1989FA)) { BadgeChildBox() }
            VanBadge(dot = true, color = Color(0xFF1989FA)) { BadgeChildBox() }
        }

        // 4. 自定义内容 (Icon)
        Text("自定义徽标内容", fontSize = 14.sp, color = Color.Gray)
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            VanBadge(badgeSlot = {
                Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(10.dp))
            }) { BadgeChildBox() }

            VanBadge(badgeSlot = {
                Icon(Icons.Default.Close, null, tint = Color.White, modifier = Modifier.size(10.dp))
            }) { BadgeChildBox() }

            VanBadge(badgeSlot = {
                Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.White, modifier = Modifier.size(10.dp))
            }) { BadgeChildBox() }
        }

        // 5. 自定义位置
        Text("自定义位置", fontSize = 14.sp, color = Color.Gray)
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            VanBadge(count = 10, position = VanBadgePosition.TopLeft) { BadgeChildBox() }
            VanBadge(count = 10, position = VanBadgePosition.BottomLeft) { BadgeChildBox() }
            VanBadge(count = 10, position = VanBadgePosition.BottomRight) { BadgeChildBox() }
        }

        // 6. 偏移量
        Text("自定义偏移量 (x=10, y=10)", fontSize = 14.sp, color = Color.Gray)
        Row {
            VanBadge(count = 10, offset = DpOffset(10.dp, 10.dp)) { BadgeChildBox() }
        }

        // 7. 独立展示
        Text("独立展示", fontSize = 14.sp, color = Color.Gray)
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            VanBadge(count = 20)
            VanBadge(count = 200, max = 99)
        }
    }
}
// 模拟 Vant 文档中的灰色方块子元素
@Composable
fun BadgeChildBox() {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(Color(0xFFF2F3F5), RoundedCornerShape(4.dp))
    )
}
@Composable
fun VanButtons() {
// ================== 1. 原有的按钮测试 ==================
    Text(
        "Button 按钮",
        modifier = Modifier.padding(16.dp),
        color = Color.Gray,
        fontSize = 14.sp
    )

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            VanButton(square = true, type = VanButtonType.Primary, text = "方形按钮")
            VanButton(square = true, type = VanButtonType.Primary, text = "方形按钮")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            // 左侧按钮
            VanButton(
                type = VanButtonType.Default,
                text = "重置",
                block = true,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 右侧按钮
            VanButton(
                type = VanButtonType.Primary,
                text = "确定",
                block = true,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun VanCellGroups(){
    Column() {
        // ================== 2. Cell 单元格测试 ==================

        // --- 基础用法 ---
        VanCellGroup(title = "Cell 基础用法") {
            VanCell(title = "单元格", value = "内容")
            VanCell(title = "单元格", value = "内容", label = "描述信息", border = false)
        }

        // --- 卡片风格 ---
        VanCellGroup(title = "卡片风格 (Inset)", inset = true) {
            VanCell(title = "单元格", value = "内容")
            VanCell(title = "单元格", value = "内容", label = "描述信息", border = false)
        }

        // --- 单元格大小 ---
        VanCellGroup(title = "单元格大小") {
            VanCell(title = "普通", value = "内容")
            VanCell(title = "大号", value = "内容", size = VanCellSize.Large, label = "描述信息", border = false)
        }

        // --- 展示图标 & 箭头 ---
        VanCellGroup(title = "展示图标与箭头") {
            VanCell(title = "带图标", icon = Icons.Filled.LocationOn, value = "定位")
            VanCell(title = "跳转链接", isLink = true)
            VanCell(title = "向下箭头", isLink = true, arrowDirection = VanCellArrowDirection.Down, value = "展开", border = false)
        }

        // --- 垂直居中 ---
        VanCellGroup(title = "垂直居中 (Center)") {
            VanCell(
                center = true,
                title = "多行文本",
                value = "内容居中",
                label = "这是一段很长很长的描述信息，会让单元格高度增加，此时右侧内容应该垂直居中。",
                border = false
            )
        }

        // --- 高级用法 (插槽) ---
        VanCellGroup(title = "高级用法 (自定义插槽)") {
            // 自定义右侧图标
            VanCell(
                title = "自定义右侧图标",
                rightIconComposable = {
                    Icon(Icons.Filled.Search, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            )

            // 自定义标题
            VanCell(
                value = "自定义标题",
                isLink = true,
                titleComposable = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("我的等级")
                        Spacer(modifier = Modifier.width(4.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.extraSmall
                        ) {
                            Text(
                                "LV.5",
                                fontSize = 10.sp,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                },
                border = false
            )
        }
    }
}