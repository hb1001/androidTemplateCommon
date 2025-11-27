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
            VanSwipes()
//            VanButtons()
//            Spacer(modifier = Modifier.height(20.dp))
//            VanCollapses()

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