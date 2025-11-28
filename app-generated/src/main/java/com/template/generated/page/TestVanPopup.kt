package com.template.generated.page

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.template.core.ui.vant.VanAction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.template.core.ui.vant.*

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun VanPickerDemo() {
    val context = LocalContext.current

    // --- 日期选择器状态 ---
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    // 临时状态，用于弹窗内滚动，只有点击确定才同步到 selectedDate
    var tempDate by remember { mutableStateOf(LocalDate.now()) }

    // --- 时间选择器状态 ---
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }
    var tempTime by remember { mutableStateOf(LocalTime.now()) }

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Datetime Picker 选择器", color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 16.dp))

        // 1. 日期选择
        DemoSection("日期选择器", padding = false) {
            VanCellGroup {
                VanCell(
                    title = "选择日期",
                    value = selectedDate.format(dateFormatter),
                    isLink = true,
                    onClick = {
                        tempDate = selectedDate // 重置临时状态
                        showDatePicker = true
                    }
                )
            }
        }

        // 2. 时间选择
        DemoSection("时间选择器", padding = false) {
            VanCellGroup {
                VanCell(
                    title = "选择时间",
                    value = selectedTime.format(timeFormatter),
                    isLink = true,
                    onClick = {
                        tempTime = selectedTime
                        showTimePicker = true
                    }
                )
            }
        }
    }

    // --- Popup: Date Picker ---
    VanPickerPopup(
        visible = showDatePicker,
        onClose = { showDatePicker = false },
        title = "${tempDate.year}年${tempDate.monthValue}月", // 动态标题，仿照截图
        onCancel = { showDatePicker = false },
        onConfirm = {
            selectedDate = tempDate
            showDatePicker = false
            Toast.makeText(context, "选择了: ${selectedDate.format(dateFormatter)}", Toast.LENGTH_SHORT).show()
        }
    ) {
        VanDatePicker(
            currentDate = tempDate,
            onDateChange = { tempDate = it }
        )
    }

    // --- Popup: Time Picker ---
    VanPickerPopup(
        visible = showTimePicker,
        onClose = { showTimePicker = false },
        title = "选择时间",
        onCancel = { showTimePicker = false },
        onConfirm = {
            selectedTime = tempTime
            showTimePicker = false
            Toast.makeText(context, "选择了: ${selectedTime.format(timeFormatter)}", Toast.LENGTH_SHORT).show()
        }
    ) {
        VanTimePicker(
            currentTime = tempTime,
            onTimeChange = { tempTime = it }
        )
    }
}


@Composable
fun VanActionSheetDemo() {
    val context = LocalContext.current

    // 状态管理
    var visible1 by remember { mutableStateOf(false) } // 基础
    var visible2 by remember { mutableStateOf(false) } // 取消按钮
    var visible3 by remember { mutableStateOf(false) } // 描述信息
    var visible4 by remember { mutableStateOf(false) } // 选项状态
    var visible5 by remember { mutableStateOf(false) } // 自定义面板

    // 数据源
    val actions1 = listOf(
        VanAction(name = "选项一"),
        VanAction(name = "选项二"),
        VanAction(name = "选项三")
    )
    val actions3 = listOf(
        VanAction(name = "选项一"),
        VanAction(name = "选项二"),
        VanAction(name = "选项三", subname = "描述信息")
    )
    val actions4 = listOf(
        VanAction(name = "选项一", color = Color(0xFFEE0A24)),
        VanAction(name = "选项二", disabled = true),
        VanAction(loading = true)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("ActionSheet 动作面板", color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 16.dp))

        // 1. 基础用法
        DemoSection("基础用法", padding = false) {
            VanCellGroup {
                VanCell(title = "基础用法", isLink = true, onClick = { visible1 = true })
                VanCell(title = "展示取消按钮", isLink = true, onClick = { visible2 = true })
                VanCell(title = "展示描述信息", isLink = true, onClick = { visible3 = true })
            }
        }

        // 2. 选项状态
        DemoSection("选项状态", padding = false) {
            VanCellGroup {
                VanCell(title = "选项状态", isLink = true, onClick = { visible4 = true })
            }
        }

        // 3. 自定义面板
        DemoSection("自定义面板", padding = false) {
            VanCellGroup {
                VanCell(title = "自定义面板", isLink = true, onClick = { visible5 = true })
            }
        }
    }

    // --- ActionSheets ---

    // 1. 基础
    VanActionSheet(
        visible = visible1,
        onCancel = { visible1 = false },
        actions = actions1,
        onSelect = { action, index ->
            Toast.makeText(context, "选中: ${action.name}", Toast.LENGTH_SHORT).show()
            visible1 = false
        }
    )

    // 2. 取消按钮
    VanActionSheet(
        visible = visible2,
        onCancel = { visible2 = false },
        actions = actions1,
        cancelText = "取消",
        onSelect = { _, _ -> visible2 = false }
    )

    // 3. 描述信息
    VanActionSheet(
        visible = visible3,
        onCancel = { visible3 = false },
        actions = actions3,
        description = "这是一段描述信息",
        cancelText = "取消",
        onSelect = { _, _ -> visible3 = false }
    )

    // 4. 选项状态
    VanActionSheet(
        visible = visible4,
        onCancel = { visible4 = false },
        actions = actions4,
        cancelText = "取消",
        onSelect = { _, _ -> visible4 = false }
    )

    // 5. 自定义面板
    VanActionSheet(
        visible = visible5,
        onCancel = { visible5 = false },
        title = "标题"
    ) {
        Box(Modifier.padding(16.dp, 16.dp, 16.dp, 160.dp)) {
            Text("内容")
        }
    }
}