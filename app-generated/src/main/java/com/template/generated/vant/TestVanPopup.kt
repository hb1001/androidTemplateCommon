package com.template.generated.vant

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import com.template.core.ui.uimodel.*
import com.template.core.ui.vant.*

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@Composable
fun VanLoadingDemo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            "Loading 加载",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )


        var loading by remember { mutableStateOf(false) }
        var loading2 by remember { mutableStateOf(false) }
        // 5. 垂直排列
        DemoSection("弹框", padding = false) {
            VanCell(title = "加载数据", isLink = true, size = VanCellSize.Large, onClick = {
                loading = true
            })
            VanCell(title = "加载数据2", isLink = true, onClick = {
                loading2 = true
            })
            VanPopup(
                visible = loading,
                onClose = { },
                contentWidth = 200.dp,
                contentHeight = 150.dp,
                // 透明背景
                modifier = Modifier.background(Color.Black.copy(alpha = 0f))
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    VanLoading(size = 24.dp) {
                        Text("加载中...")
                    }
                }
            }
            // 只能结束应用,不推荐。
            VanDialog(
                visible = loading2,
                onDismissRequest = { },
                showConfirmButton = false,
                content = {
                    VanLoading(size = 24.dp, vertical = true) {
                        Text("加载中...")
                    }
                }
            )
        }

        // 1. 加载类型
        DemoSection("加载类型", padding = false) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                VanLoading(type = VanLoadingType.Circular)
                VanLoading(type = VanLoadingType.Spinner)
                VanLoading(type = VanLoadingType.Ball, size = 40.dp) // Ball 需要稍微宽一点
            }
        }

        // 2. 自定义颜色
        DemoSection("自定义颜色", padding = false) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                VanLoading(color = Color(0xFF1989FA))
                VanLoading(type = VanLoadingType.Spinner, color = Color(0xFF1989FA))
            }
        }

        // 3. 自定义大小
        DemoSection("自定义大小", padding = false) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                VanLoading(size = 20.dp)
                VanLoading(type = VanLoadingType.Spinner, size = 20.dp)
            }
        }

        // 4. 加载文案
        DemoSection("加载文案", padding = false) {
            Row(modifier = Modifier.padding(16.dp)) {
                VanLoading(size = 24.dp) {
                    Text("加载中...")
                }
            }
        }


        // 6. 自定义文本颜色
        DemoSection("自定义文本颜色垂直", padding = false) {
            Row(modifier = Modifier.padding(16.dp)) {
                VanLoading(
                    size = 24.dp,
                    vertical = true,
                    textColor = Color(0xFF1989FA)
                ) {
                    Text("加载中...")
                }
            }
        }

        // 弹框中加载
        DemoSection("弹框中加载", padding = false) {
            Row(modifier = Modifier.padding(16.dp)) {
                VanLoading(size = 24.dp, vertical = true) {
                    Text("加载中...")
                }
            }
        }
    }
}


@Composable
fun VanShareSheetDemo() {
    val context = LocalContext.current

    // States
    var showBasic by remember { mutableStateOf(false) }
    var showMultiLine by remember { mutableStateOf(false) }
    var showCustomIcon by remember { mutableStateOf(false) }
    var showDesc by remember { mutableStateOf(false) }

    // Mock Options
    val basicOptions = listOf(
        VanShareOption(name = "微信", icon = "wechat"),
        VanShareOption(name = "微博", icon = "weibo"),
        VanShareOption(name = "复制链接", icon = "link"),
        VanShareOption(name = "分享海报", icon = "poster"),
        VanShareOption(name = "二维码", icon = "qrcode")
    )

    val multiLineOptions = listOf(
        listOf(
            VanShareOption(name = "微信", icon = "wechat"),
            VanShareOption(name = "朋友圈", icon = "wechat-moments"),
            VanShareOption(name = "微博", icon = "weibo"),
            VanShareOption(name = "QQ", icon = "qq")
        ),
        listOf(
            VanShareOption(name = "复制链接", icon = "link"),
            VanShareOption(name = "分享海报", icon = "poster"),
            VanShareOption(name = "二维码", icon = "qrcode"),
            VanShareOption(name = "小程序码", icon = "weapp-qrcode")
        )
    )

    val customIconOptions = listOf(
        VanShareOption(name = "名称", icon = "https://img.yzcdn.cn/vant/custom-icon-fire.png"),
        VanShareOption(name = "名称", icon = "https://img.yzcdn.cn/vant/custom-icon-light.png"),
        VanShareOption(name = "名称", icon = "https://img.yzcdn.cn/vant/custom-icon-water.png")
    )

    val descOptions = listOf(
        VanShareOption(name = "微信", icon = "wechat"),
        VanShareOption(name = "微博", icon = "weibo"),
        VanShareOption(name = "复制链接", icon = "link", description = "描述信息"),
        VanShareOption(name = "分享海报", icon = "poster"),
        VanShareOption(name = "二维码", icon = "qrcode")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            "ShareSheet 分享面板",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // 1. 基础用法
        DemoSection("基础用法", padding = false) {
            VanCellGroup {
                VanCell(title = "显示分享面板", isLink = true, onClick = { showBasic = true })
            }
        }

        // 2. 展示多行选项
        DemoSection("展示多行选项", padding = false) {
            VanCellGroup {
                VanCell(title = "显示分享面板", isLink = true, onClick = { showMultiLine = true })
            }
        }

        // 3. 自定义图标
        DemoSection("自定义图标", padding = false) {
            VanCellGroup {
                VanCell(title = "显示分享面板", isLink = true, onClick = { showCustomIcon = true })
            }
        }

        // 4. 展示描述信息
        DemoSection("展示描述信息", padding = false) {
            VanCellGroup {
                VanCell(title = "显示分享面板", isLink = true, onClick = { showDesc = true })
            }
        }
    }

    // --- ShareSheets ---

    VanShareSheet(
        visible = showBasic,
        onCancel = { showBasic = false },
        title = "立即分享给好友",
        options = basicOptions,
        onSelect = { option, index ->
            Toast.makeText(context, "${option.name} $index", Toast.LENGTH_SHORT).show()
            showBasic = false
        }
    )

    VanShareSheet(
        visible = showMultiLine,
        onCancel = { showMultiLine = false },
        title = "立即分享给好友",
        options = multiLineOptions,
        onSelect = { option, index ->
            Toast.makeText(context, "${option.name} $index", Toast.LENGTH_SHORT).show()
            showMultiLine = false
        }
    )

    VanShareSheet(
        visible = showCustomIcon,
        onCancel = { showCustomIcon = false },
        options = customIconOptions,
        onSelect = { option, _ ->
            Toast.makeText(context, option.name, Toast.LENGTH_SHORT).show()
            showCustomIcon = false
        }
    )

    VanShareSheet(
        visible = showDesc,
        onCancel = { showDesc = false },
        title = "立即分享给好友",
        description = "描述信息",
        options = descOptions,
        onSelect = { option, _ ->
            Toast.makeText(context, option.name, Toast.LENGTH_SHORT).show()
            showDesc = false
        }
    )
}


@Composable
fun VanAreaDemo() {
    val context = LocalContext.current
    var showAreaPicker by remember { mutableStateOf(false) }
    var resultText by remember { mutableStateOf("点击选择省市区") }

    val areaRepo = AreaRepository(context)
    // --- 异步加载数据 ---
    // 为了不阻塞 UI，我们在 LaunchedEffect 或 produceState 中加载
    // 假设 context.loadProvinces() 等方法已经按照你提供的代码写好
    val areaDataState = produceState(initialValue = AreaDataState(loading = true)) {
        // 模拟一点延迟，或者真实读取IO
        val provinces = areaRepo.getProvinces()
        val cities = areaRepo.getCities()
        val areas = areaRepo.getAreas()

        value = AreaDataState(
            loading = false,
            provinces = provinces,
            cities = cities,
            areas = areas
        )
    }

    val areaData = areaDataState.value

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            "Area 省市区选择",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // 触发按钮
        VanCellGroup {
            VanCell(
                title = "选择地区",
                value = resultText,
                isLink = true,
                onClick = { showAreaPicker = true }
            )
        }
    }

    // --- 弹出层 ---
    VanPopup(
        visible = showAreaPicker,
        onClose = { showAreaPicker = false },
        position = VanPopupPosition.Bottom,
        round = true, // 关键：顶部圆角
        // 高度自适应，或者在这里限制高度 (VanAreaPicker 内部给了 500.dp)
    ) {
        if (areaData.loading) {
            // 加载中状态
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = VanAreaColors.Active)
            }
        } else {
            // 选择器组件
            VanAreaPicker(
                provinces = areaData.provinces,
                cities = areaData.cities,
                areas = areaData.areas,
                onCancel = { showAreaPicker = false },
                onFinish = { p, c, a ->
                    resultText = "${p.name} / ${c.name} / ${a.name}"
                    showAreaPicker = false
                }
            )
        }
    }
}

// 简单的状态持有类
data class AreaDataState(
    val loading: Boolean = false,
    val provinces: List<Province> = emptyList(),
    val cities: List<City> = emptyList(),
    val areas: List<Area> = emptyList()
)

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
        Text(
            "Datetime Picker 选择器",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

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
        title = "选择日期",  // "${tempDate.year}年${tempDate.monthValue}月", // 动态标题，仿照截图
        onCancel = { showDatePicker = false },
        onConfirm = {
            selectedDate = tempDate
            showDatePicker = false
            Toast.makeText(
                context,
                "选择了: ${selectedDate.format(dateFormatter)}",
                Toast.LENGTH_SHORT
            ).show()
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
            Toast.makeText(
                context,
                "选择了: ${selectedTime.format(timeFormatter)}",
                Toast.LENGTH_SHORT
            ).show()
        }
    ) {
        VanTimePicker(
            currentTime = tempTime,
            onTimeChange = { tempTime = it }
        )
    }
    VanAreaDemo()
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
        Text(
            "ActionSheet 动作面板",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

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