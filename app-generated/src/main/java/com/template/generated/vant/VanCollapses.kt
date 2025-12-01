package com.template.generated.vant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
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
import com.template.core.ui.vant.VanButton
import com.template.core.ui.vant.VanButtonSize
import com.template.core.ui.vant.VanButtonType
import com.template.core.ui.vant.VanCellGroup
import com.template.core.ui.vant.VanCollapse
import com.template.core.ui.vant.VanCollapseItem


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
                            Icon(
                                Icons.Filled.Info,
                                null,
                                tint = Color.Blue,
                                modifier = Modifier.size(14.dp)
                            )
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
