package com.template.generated.page

import android.widget.Toast
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import coil.compose.AsyncImage

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


            VanInputDemo()

        }
    }
}


@Composable
fun VanPopupDemo() {
    // 状态管理
    var showBasic by remember { mutableStateOf(false) }

    var showTop by remember { mutableStateOf(false) }
    var showBottom by remember { mutableStateOf(false) }
    var showLeft by remember { mutableStateOf(false) }
    var showRight by remember { mutableStateOf(false) }

    var showRound by remember { mutableStateOf(false) }
    var showCloseable by remember { mutableStateOf(false) }
    var showTitle by remember { mutableStateOf(false) }

    // 由于 VanPopup 需要覆盖在页面最上层，我们这里用一个 Box 包裹整个演示页面
    // 实际项目中，VanPopup 建议放在 Scaffold 的最外层 Box 中
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                "Popup 弹出层",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // 1. 基础用法
            DemoSection("基础用法", padding = false) {
                VanCellGroup {
                    VanCell(title = "展示弹出层", isLink = true, onClick = { showBasic = true })
                }
            }

            // 2. 弹出位置
            DemoSection("弹出位置", padding = false) {
                VanCellGroup {
                    VanCell(title = "顶部弹出", isLink = true, onClick = { showTop = true })
                    VanCell(title = "底部弹出", isLink = true, onClick = { showBottom = true })
                    VanCell(title = "左侧弹出", isLink = true, onClick = { showLeft = true })
                    VanCell(title = "右侧弹出", isLink = true, onClick = { showRight = true })
                }
            }

            // 3. 圆角弹窗
            DemoSection("圆角弹窗", padding = false) {
                VanCellGroup {
                    VanCell(title = "圆角弹窗", isLink = true, onClick = { showRound = true })
                }
            }

            // 4. 关闭图标
            DemoSection("关闭图标", padding = false) {
                VanCellGroup {
                    VanCell(title = "关闭图标", isLink = true, onClick = { showCloseable = true })
                }
            }

            // 5. 标题弹窗
            DemoSection("标题弹窗", padding = false) {
                VanCellGroup {
                    VanCell(title = "标题弹窗", isLink = true, onClick = { showTitle = true })
                }
            }
        }

        // --- Popups (放在最上层) ---

        // 1. 基础 (Center)
        VanPopup(
            visible = showBasic,
            onClose = { showBasic = false },
            contentWidth = 200.dp,
            contentHeight = 150.dp
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("内容")
            }
        }

        // 2. Top
        VanPopup(
            visible = showTop,
            onClose = { showTop = false },
            position = VanPopupPosition.Top,
            contentHeight = 200.dp
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("顶部弹出") }
        }

        // Bottom
        VanPopup(
            visible = showBottom,
            onClose = { showBottom = false },
            position = VanPopupPosition.Bottom,
            contentHeight = 200.dp,
            safeAreaInsetBottom = true
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("底部弹出") }
        }

        // Left
        VanPopup(
            visible = showLeft,
            onClose = { showLeft = false },
            position = VanPopupPosition.Left,
            contentWidth = 200.dp
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("左侧弹出") }
        }

        // Right
        VanPopup(
            visible = showRight,
            onClose = { showRight = false },
            position = VanPopupPosition.Right,
            contentWidth = 200.dp
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("右侧弹出") }
        }

        // Round
        VanPopup(
            visible = showRound,
            onClose = { showRound = false },
            position = VanPopupPosition.Bottom,
            round = true,
            contentHeight = 200.dp
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("圆角弹窗") }
        }

        // Closeable
        VanPopup(
            visible = showCloseable,
            onClose = { showCloseable = false },
            position = VanPopupPosition.Bottom,
            closeable = true,
            contentHeight = 200.dp
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("关闭图标") }
        }

        // Title
        VanPopup(
            visible = showTitle,
            onClose = { showTitle = false },
            position = VanPopupPosition.Bottom,
            round = true,
            closeable = true,
            title = "标题",
            description = "这是一段很长很长的描述这是一段很长很长的描述",
            contentHeight = 250.dp
        ) {
            // 内容区域
        }
    }
}


@Composable
fun VanDialogDemo() {
    val context = LocalContext.current
    val dialogController = LocalVanDialog.current
    val scope = rememberCoroutineScope()

    // 组件式调用的状态
    var componentVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            "Dialog 弹出框",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // 1. 函数调用 (消息提示)
        DemoSection("消息提示 (函数调用)", padding = false) {
            VanCellGroup {
                VanCell(
                    title = "弹窗提示",
                    isLink = true,
                    onClick = {
                        scope.launch {
                            dialogController.alert(
                                title = "标题",
                                message = "代码是写出来给人看的，附带能在机器上运行"
                            )
                        }
                    }
                )
                VanCell(
                    title = "弹窗提示 (无标题)",
                    isLink = true,
                    onClick = {
                        scope.launch {
                            dialogController.alert(
                                message = "代码是写出来给人看的，附带能在机器上运行"
                            )
                        }
                    }
                )
                VanCell(
                    title = "确认弹框 (Confirm)",
                    isLink = true,
                    onClick = {
                        scope.launch {
                            try {
                                dialogController.confirm(
                                    title = "标题",
                                    message = "代码是写出来给人看的，附带能在机器上运行"
                                )
                                Toast.makeText(context, "Confirm", Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )
            }
        }

        // 2. 圆角按钮风格
        DemoSection("圆角按钮风格", padding = false) {
            VanCellGroup {
                VanCell(
                    title = "圆角按钮弹窗",
                    isLink = true,
                    onClick = {
                        scope.launch {
                            try {
                                dialogController.confirm(
                                    title = "标题",
                                    message = "代码是写出来给人看的，附带能在机器上运行",
                                    theme = VanDialogTheme.RoundButton
                                )
                            } catch (e: Exception) {
                            }
                        }
                    }
                )
            }
        }

        // 3. 异步关闭 (模拟)
        DemoSection("异步关闭", padding = false) {
            VanCellGroup {
                VanCell(
                    title = "异步关闭",
                    isLink = true,
                    onClick = {
                        // 使用底层 show 方法来自定义逻辑
                        dialogController.show(
                            DialogOptions(
                                title = "标题",
                                message = "点击确认 1秒后关闭",
                                showCancelButton = true,
                                dismissOnAction = false, // 只有手动 dismiss 才关闭
                                onConfirm = {
                                    scope.launch {
                                        kotlinx.coroutines.delay(1000)
                                        dialogController.dismiss()
                                        Toast.makeText(context, "异步关闭成功", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                },
                                onCancel = {
                                    dialogController.dismiss()
                                }
                            )
                        )
                    }
                )
            }
        }

        // 4. 组件调用 (自定义内容)
        DemoSection("组件调用 (自定义内容)", padding = false) {
            VanCellGroup {
                VanCell(
                    title = "组件调用",
                    isLink = true,
                    onClick = { componentVisible = true }
                )
            }

            // 嵌入组件调用
            VanDialog(
                visible = componentVisible,
                onDismissRequest = { componentVisible = false },
                title = "标题",
                showCancelButton = true,
                onConfirm = {
                    Toast.makeText(context, "点击确认", Toast.LENGTH_SHORT).show()
                    componentVisible = false
                },
                onCancel = { componentVisible = false }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(
                        model = "https://img.yzcdn.cn/vant/apple-3.jpg",
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )
                    Spacer(Modifier.height(10.dp))
                    Text("这是自定义的图片内容", fontSize = 14.sp, color = Color.Gray)
                }
            }
        }
    }
}


@Composable
fun VanIconDemo() {
    val context = LocalContext.current

    // 模拟: 实际使用时，请确保 assets/icons/ 目录下有这些 svg 文件
    // 这里为了演示方便，使用了 placeholder 的网络 SVG，或者你需要手动放置文件
    // 假设你放入了 'star.svg', 'chat.svg' 等到 assets/icons/ 目录

    // 如果没有本地文件，这里使用一个在线 SVG 做演示 (Vite Logo)
    val demoSvgUrl = "https://vitejs.dev/logo.svg"
    val demoName = "PhoneO" // 假设你下载了这个文件并命名为 vite-logo.svg 放在 assets/icons/

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            "Icon 图标 (Coil SVG)",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            "请确保 assets/icons/ 下存在对应的 .svg 文件",
            fontSize = 12.sp,
            color = Color.Red,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // 1. 基础用法
        DemoSection("基础用法 (加载 Assets/Network)", padding = false) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // 加载 assets/icons/location-o.svg (你需要放入文件)
                // 这里暂时用网络图演示效果，实际请用: VanIcon(name = "location-o")
                VanIcon(name = demoSvgUrl, size = 32.dp)

                // 假设 assets/icons/like-o.svg 存在
                VanIcon(name = "PhoneO", size = 32.dp)

                // 假设 assets/icons/star-o.svg 存在
                VanIcon(name = "star-o", size = 32.dp)
                VanIcon(name = "PhoneO", size = 32.dp)
            }
        }

        // 2. 徽标提示
        DemoSection("徽标提示 (搭配 Badge)", padding = false) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                // 红点
                VanBadge(dot = true) {
                    VanIcon(name = demoSvgUrl, size = 32.dp)
                }

                // 数字
                VanBadge(content = "99+") {
                    VanIcon(name = demoSvgUrl, size = 32.dp)
                }
            }
        }

        // 3. 图标颜色
        DemoSection("图标颜色 (Tint)", padding = false) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // SVG 会被自动着色
                VanIcon(name = demoSvgUrl, color = Color(0xFFF44336), size = 32.dp)
                VanIcon(name = demoSvgUrl, color = Color(0xFF3F45FF), size = 32.dp)
            }
        }

        // 4. 图标大小
        DemoSection("图标大小", padding = false) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                VanIcon(name = demoSvgUrl, size = 20.dp)
                VanIcon(name = demoSvgUrl, size = 30.dp)
                VanIcon(name = demoSvgUrl, size = 40.dp)
            }
        }

        // 5. 图标旋转
        DemoSection("图标旋转 (Spin & Rotate)", padding = false) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // 动画旋转
                    VanIcon(name = demoSvgUrl, spin = true, size = 32.dp, color = Color(0xFF1989FA))
                    Spacer(Modifier.height(4.dp))
                    Text("Spin", fontSize = 12.sp, color = Color.Gray)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // 静态旋转
                    VanIcon(name = demoSvgUrl, rotate = 90f, size = 32.dp)
                    Spacer(Modifier.height(4.dp))
                    Text("Rotate 90°", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        // 6. 点击事件
        DemoSection("点击事件", padding = false) {
            Row(modifier = Modifier.padding(16.dp)) {
                VanIcon(
                    name = demoSvgUrl,
                    size = 32.dp,
                    onClick = {
                        Toast.makeText(context, "Icon Clicked!", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

@Composable
fun VanImageDemo() {
    val src = "https://img.yzcdn.cn/vant/cat.jpeg"
    val errorSrc = "https://error.url/x.jpg" // 故意错误的链接

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            "Image 图片",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // 1. 基础用法
        DemoSection("基础用法", padding = false) {
            Box(Modifier.padding(16.dp)) {
                VanImage(
                    src = src,
                    width = 100.dp,
                    height = 100.dp
                )
            }
        }

        // 2. 填充模式
        DemoSection("填充模式", padding = false) {
            val fits = VanImageFit.values()

            @OptIn(ExperimentalLayoutApi::class)
            FlowRow(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                maxItemsInEachRow = 3
            ) {
                fits.forEach { fit ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(100.dp)
                    ) {
                        VanImage(
                            src = src,
                            width = 100.dp,
                            height = 100.dp,
                            fit = fit,
                            radius = 4.dp
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(fit.name, fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }

        // 3. 圆形图片
        DemoSection("圆形图片", padding = false) {
            val fits = listOf(VanImageFit.Contain, VanImageFit.Cover, VanImageFit.Fill)

            @OptIn(ExperimentalLayoutApi::class)
            FlowRow(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                maxItemsInEachRow = 3
            ) {
                fits.forEach { fit ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(100.dp)
                    ) {
                        VanImage(
                            src = src,
                            width = 100.dp,
                            height = 100.dp,
                            fit = fit,
                            round = true
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(fit.name, fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }

        // 4. 加载中提示
        DemoSection("加载中提示", padding = false) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    VanImage(
                        src = "", // 空链接或慢速链接保持 Loading 状态
                        width = 100.dp,
                        height = 100.dp,
                        showError = false // 为了演示 Loading 样式，强制不显示错误
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("默认提示", fontSize = 12.sp)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    VanImage(
                        src = "",
                        width = 100.dp,
                        height = 100.dp,
                        showError = false,
                        // 自定义 Loading 插槽 (Spinner)
                        loadingIcon = {
                            CircularProgressIndicator(
                                color = VanImageColors.PlaceholderIcon,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("自定义提示", fontSize = 12.sp)
                }
            }
        }

        // 5. 加载失败提示
        DemoSection("加载失败提示", padding = false) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    VanImage(
                        src = errorSrc, // 错误链接
                        width = 100.dp,
                        height = 100.dp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("默认提示", fontSize = 12.sp)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    VanImage(
                        src = errorSrc,
                        width = 100.dp,
                        height = 100.dp,
                        // 自定义 Error 插槽 (文字)
                        errorIcon = {
                            Text(
                                "加载失败",
                                fontSize = 14.sp,
                                color = VanImageColors.PlaceholderText
                            )
                        }
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("自定义提示", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun VanInputDemo() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            "Input 输入框",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // 1. 基础用法
        DemoSection("基础用法", padding = false) {
            var text by remember { mutableStateOf("") }
            var tel by remember { mutableStateOf("") }
            var digit by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            VanCellGroup {
                // 文本
                VanCell(
                    title = "文本",
                    valueComposable = {
                        VanInput(
                            value = text,
                            onValueChange = { text = it },
                            placeholder = "请输入文本",
                            clearable = true,
                            align = VanInputAlign.Right // Cell 中通常右对齐看起来比较整齐，或者左对齐紧跟 Label
                        )
                    }
                )
                // 手机号
                VanCell(
                    title = "手机号",
                    valueComposable = {
                        VanInput(
                            value = tel,
                            onValueChange = { tel = it },
                            type = VanInputType.Tel,
                            placeholder = "请输入手机号",
                            align = VanInputAlign.Right
                        )
                    }
                )
                // 整数
                VanCell(
                    title = "整数",
                    valueComposable = {
                        VanInput(
                            value = digit,
                            onValueChange = { digit = it },
                            type = VanInputType.Digit,
                            placeholder = "请输入整数",
                            align = VanInputAlign.Right
                        )
                    }
                )
                // 密码
                VanCell(
                    title = "密码",
                    valueComposable = {
                        VanInput(
                            value = password,
                            onValueChange = { password = it },
                            type = VanInputType.Password,
                            placeholder = "请输入密码",
                            align = VanInputAlign.Right
                        )
                    }
                )
            }
        }

        // 2. 插入内容 (前后缀)
        DemoSection("插入内容", padding = false) {
            var sms by remember { mutableStateOf("") }

            VanCellGroup {
                VanCell(
                    title = "短信验证码",
                    valueComposable = {
                        VanInput(
                            value = sms,
                            onValueChange = { sms = it },
                            placeholder = "请输入验证码",
                            prefix = { Text("💁", fontSize = 16.sp) }, // 前缀 Emoji
                            suffix = {
                                VanButton(
                                    text = "发送",
                                    type = VanButtonType.Primary,
                                    size = VanButtonSize.Mini,
                                    onClick = {
                                        Toast.makeText(
                                            context,
                                            "发送验证码",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }
                        )
                    }
                )
            }
        }

        // 3. 多行输入
        DemoSection("多行输入 (TextArea)", padding = false) {
            var val1 by remember { mutableStateOf("") }
            var val2 by remember { mutableStateOf("") }

            VanCellGroup {
                VanCell(
                    title = "多行输入",
                    label = "自适应高度", // 放在 label 显示描述
                    valueComposable = {
                        VanTextArea(
                            value = val1,
                            onValueChange = { val1 = it },
                            placeholder = "请输入留言"
                        )
                    }
                )

                VanCell(
                    title = "固定高度",
                    valueComposable = {
                        VanTextArea(
                            value = val2,
                            onValueChange = { val2 = it },
                            minHeight = 100.dp, // 强制最小高度
                            placeholder = "最小高度 100dp"
                        )
                    }
                )
            }
        }

        // 4. 字数统计
        DemoSection("字数统计", padding = false) {
            var val1 by remember { mutableStateOf("") }
            var val2 by remember { mutableStateOf("") }

            VanCellGroup {
                VanCell(
                    title = "单行限制",
                    valueComposable = {
                        VanInput(
                            value = val1,
                            onValueChange = { val1 = it },
                            maxLength = 10,
                            placeholder = "最多10个字符",
                            onOverlimit = {
                                Toast.makeText(
                                    context,
                                    "不能超过10个字符",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                )

                VanCell(
                    title = "多行统计",
                    valueComposable = {
                        VanTextArea(
                            value = val2,
                            onValueChange = { val2 = it },
                            maxLength = 50,
                            showWordLimit = true,
                            placeholder = "显示字数统计"
                        )
                    }
                )
            }
        }

        // 5. 对齐方式 & 状态
        DemoSection("状态与对齐", padding = false) {
            var v1 by remember { mutableStateOf("只读模式") }
            var v2 by remember { mutableStateOf("禁用模式") }
            var v3 by remember { mutableStateOf("") }

            VanCellGroup {
                VanCell(
                    title = "只读",
                    valueComposable = {
                        VanInput(value = v1, onValueChange = {}, readOnly = true)
                    }
                )
                VanCell(
                    title = "禁用",
                    valueComposable = {
                        VanInput(value = v2, onValueChange = {}, disabled = true)
                    }
                )
                VanCell(
                    title = "居中对齐",
                    valueComposable = {
                        VanInput(
                            value = v3,
                            onValueChange = { v3 = it },
                            align = VanInputAlign.Center,
                            placeholder = "输入内容居中"
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun VanSearchDemo() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            "Search 搜索",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp)
        )

        // 1. 基础用法
        DemoSection("基础用法", padding = false) {
            var value by remember { mutableStateOf("") }
            VanSearch(
                value = value,
                onValueChange = { value = it },
                placeholder = "请输入搜索关键词"
            )
        }

        // 2. 事件监听
        DemoSection("事件监听", padding = false) {
            var value by remember { mutableStateOf("") }
            VanSearch(
                value = value,
                onValueChange = { value = it },
                showAction = true, // 显示右侧取消按钮
                placeholder = "请输入搜索关键词",
                onSearch = {
                    Toast.makeText(context, "Search: $it", Toast.LENGTH_SHORT).show()
                },
                onCancel = {
                    Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show()
                    value = ""
                },
                onClear = {
                    Toast.makeText(context, "Clear", Toast.LENGTH_SHORT).show()
                },
                onClickInput = {
                    // Toast.makeText(context, "Click Input", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // 3. 搜索框内容对齐 (Center)
        DemoSection("搜索框内容对齐 (Center)", padding = false) {
            var value by remember { mutableStateOf("") }
            VanSearch(
                value = value,
                onValueChange = { value = it },
                align = VanSearchAlign.Center,
                placeholder = "请输入搜索关键词"
            )
        }

        // 4. 禁用搜索框
        DemoSection("禁用搜索框", padding = false) {
            VanSearch(
                value = "无法输入",
                onValueChange = {},
                disabled = true,
                placeholder = "请输入搜索关键词"
            )
        }

        // 5. 自定义背景色 & 圆角
        DemoSection("自定义背景色 & 圆角", padding = false) {
            var value by remember { mutableStateOf("") }
            VanSearch(
                value = value,
                onValueChange = { value = it },
                shape = VanSearchShape.Round,
                background = Color(0xFF4FC08D),
                placeholder = "请输入搜索关键词"
            )
        }

        // 6. 自定义按钮
        DemoSection("自定义按钮 (Action)", padding = false) {
            var value by remember { mutableStateOf("") }
            VanSearch(
                value = value,
                onValueChange = { value = it },
                label = { Text("地址") }, // 左侧 Label
                placeholder = "请输入搜索关键词",
                action = {
                    // 自定义 Action 插槽
                    Text(
                        text = "搜索",
                        color = Color(0xFF323233),
                        modifier = Modifier.clickable {
                            Toast.makeText(context, "Custom Search: $value", Toast.LENGTH_SHORT)
                                .show()
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun VanSwipeCellDemo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            "SwipeCell 滑动单元格",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // 1. 基础用法
        DemoSection("基础用法", padding = false) {
            VanSwipeCell(
                rightAction = {
                    // Action 按钮高度会自动撑满
                    ActionBox(color = Color(0xFFEE0A24), text = "删除")
                },
                onOpen = { /* log */ },
                onClose = { /* log */ }
            ) {
                VanCell(title = "单元格", value = "内容")
            }
        }

        // 2. 事件监听与双侧滑动
        DemoSection("事件监听", padding = false) {
            VanSwipeCell(
                leftAction = {
                    ActionBox(color = Color(0xFF1989FA), text = "选择")
                },
                rightAction = {
                    Row {
                        ActionBox(color = Color(0xFFEE0A24), text = "删除")
                        ActionBox(color = Color(0xFF1989FA), text = "收藏")
                    }
                },
                onClick = { position ->
                    // Handle click: Left, Right, Cell
                }
            ) {
                VanCell(title = "单元格", value = "左右均可滑动")
            }
        }

        // 3. 自定义内容 (商品卡片)
        DemoSection("自定义内容", padding = false) {
            VanSwipeCell(
                rightAction = {
                    ActionBox(color = Color(0xFFEE0A24), text = "删除")
                }
            ) {
                // 模拟商品卡片布局
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White) // 显式背景
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
//                    Image(
//                        painter = rememberAsyncImagePainter("https://img.yzcdn.cn/vant/ipad.jpeg"),
//                        contentDescription = null,
//                        modifier = Modifier.size(88.dp).background(Color.LightGray),
//                        contentScale = ContentScale.Crop
//                    )
                    AsyncImage(
                        model = "https://img.yzcdn.cn/vant/ipad.jpeg",
                        contentDescription = null,
                        modifier = Modifier
                            .size(88.dp)
                            .background(Color.LightGray),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .height(88.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("商品标题", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("这里是商品描述", color = Color.Gray, fontSize = 12.sp)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("¥2.00", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("x2", color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // 4. 异步关闭
        DemoSection("异步关闭 (点击删除延迟1s)", padding = false) {
            var loading by remember { mutableStateOf(false) }

            VanSwipeCell(
                leftAction = { ActionBox(color = Color(0xFF1989FA), text = "选择") },
                rightAction = {
                    if (loading) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(60.dp)
                                .background(Color(0xFFEE0A24)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    } else {
                        ActionBox(color = Color(0xFFEE0A24), text = "删除")
                    }
                },
                beforeClose = { position ->
                    if (position == VanSwipeCellPosition.Right) {
                        loading = true
                        // 模拟异步操作 (如弹窗确认或网络请求)
                        delay(1000)
                        loading = false
                        true // 返回 true 允许关闭
                    } else {
                        true
                    }
                }
            ) {
                VanCell(title = "异步关闭", value = "向左滑动删除")
            }
        }

        // 5. 外部调用
        DemoSection("外部控制", padding = false) {
            val state = rememberVanSwipeCellState()
            val scope = rememberCoroutineScope()

            Column {
                VanSwipeCell(
                    state = state,
                    leftAction = { ActionBox(color = Color(0xFF1989FA), text = "选择") },
                    rightAction = { ActionBox(color = Color(0xFFEE0A24), text = "删除") }
                ) {
                    VanCell(title = "单元格", value = "代码控制开闭")
                }

                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    VanButton(text = "左滑", size = VanButtonSize.Small, onClick = {
                        scope.launch { state.open(VanSwipeCellSide.Left) }
                    })
                    VanButton(text = "关闭", size = VanButtonSize.Small, onClick = {
                        scope.launch { state.close() }
                    })
                    VanButton(text = "右滑", size = VanButtonSize.Small, onClick = {
                        scope.launch { state.open(VanSwipeCellSide.Right) }
                    })
                }
            }
        }
    }
}

// --- 辅助组件 ---
@Composable
private fun ActionBox(color: Color, text: String) {
    Box(
        modifier = Modifier
            .fillMaxHeight() // 关键：填充高度
            .width(60.dp)    // 宽度固定，或者用 padding
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Color.White, fontSize = 14.sp)
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