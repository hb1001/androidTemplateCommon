package com.template.generated.vant

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.core.ui.vant.VanCell
import com.template.core.ui.vant.VanCellGroup
import com.template.core.ui.vant.VanSwitch

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
