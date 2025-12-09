package com.template.core.ui.vant

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview

// --- 枚举定义 ---

enum class VanButtonType {
    Default, Primary, Success, Warning, Danger
}

enum class VanButtonSize {
    Large, Normal, Small, Mini
}

enum class VanButtonIconPosition {
    Left, Right
}

// --- 颜色常量 (参考 Vant 默认主题) ---
private object VanColors {
    val Blue = Color(0xFF1989FA)
    val Green = Color(0xFF07C160)
    val Red = Color(0xFFEE0A24)
    val Orange = Color(0xFFFF976A)
    val GrayText = Color(0xFF323233)
    val White = Color.White
    val DefaultBg = Color.White
    val DefaultBorder = Color(0xFFEBEDF0)
}

/**
 * VanButton - 安卓 Compose 版
 * 参数命名和逻辑尽可能与 Vant 前端保持一致
 */
@Composable
fun VanButton(
    modifier: Modifier = Modifier,
    // 内容
    text: String? = null,
    // 类型：primary, success, warning, danger, default
    type: VanButtonType = VanButtonType.Default,
    // 尺寸：large, normal, small, mini
    size: VanButtonSize = VanButtonSize.Normal,
    // 图标 (支持 ImageVector 或 Composable)
    icon: ImageVector? = null,
    iconComposable: (@Composable () -> Unit)? = null,
    iconPosition: VanButtonIconPosition = VanButtonIconPosition.Left,
    // 样式属性
    color: Color? = null, // 自定义颜色 (背景色，若 plain 则为文字/边框色)
    gradient: Brush? = null, // 渐变色背景
    plain: Boolean = false, // 朴素按钮
    block: Boolean = false, // 块级元素 (fillMaxWidth)
    round: Boolean = false, // 圆形 (胶囊形)
    square: Boolean = false, // 方形 (圆角为0)
    hairline: Boolean = false, // 细边框
    disabled: Boolean = false, // 禁用
    loading: Boolean = false, // 加载中
    loadingText: String? = null, // 加载文字
    loadingSize: Dp? = null, // 加载图标大小
    // 点击事件
    onClick: () -> Unit = {},
    // 自定义 Slot 内容 (覆盖 text)
    content: (@Composable RowScope.() -> Unit)? = null
) {
    // --- 1. 计算尺寸 ---
    val height = when (size) {
        VanButtonSize.Large -> 50.dp
        VanButtonSize.Normal -> 44.dp
        VanButtonSize.Small -> 32.dp
        VanButtonSize.Mini -> 26.dp
    }

    val fontSize = when (size) {
        VanButtonSize.Large -> 16.sp
        VanButtonSize.Normal -> 14.sp
        VanButtonSize.Small -> 12.sp
        VanButtonSize.Mini -> 10.sp
    }

    val paddingH = when (size) {
        VanButtonSize.Large -> 24.dp
        VanButtonSize.Normal -> 15.dp
        VanButtonSize.Small -> 8.dp
        VanButtonSize.Mini -> 5.dp
    }

    // --- 2. 计算形状 ---
    val shape: Shape = when {
        square -> RoundedCornerShape(0.dp)
        round -> CircleShape // Compose 中 CircleShape 自动处理胶囊圆角
        else -> RoundedCornerShape(4.dp) // Vant 默认圆角
    }

    // --- 3. 计算颜色 ---
    // 获取类型对应的基础色
    val typeColor = color ?: when (type) {
        VanButtonType.Primary -> VanColors.Blue
        VanButtonType.Success -> VanColors.Green
        VanButtonType.Danger -> VanColors.Red
        VanButtonType.Warning -> VanColors.Orange
        VanButtonType.Default -> VanColors.DefaultBg
    }

    val isDefaultType = type == VanButtonType.Default && color == null

    // 背景色
    val backgroundColor = when {
        plain -> Color.Transparent // 朴素按钮背景透明(或白)
        isDefaultType -> VanColors.White
        else -> typeColor
    }

    // 内容颜色 (文字/图标)
    val contentColor = when {
        plain -> typeColor // 朴素按钮文字跟随主色
        isDefaultType -> VanColors.GrayText
        else -> VanColors.White
    }

    // 边框
    val borderWidth = if (hairline) 0.5.dp else 1.dp
    val borderColor = when {
        plain -> typeColor
        isDefaultType -> VanColors.DefaultBorder
        else -> typeColor // 实色按钮边框同色
    }

    val borderStroke = if (!isDefaultType || plain) {
        BorderStroke(borderWidth, borderColor)
    } else {
        BorderStroke(borderWidth, VanColors.DefaultBorder)
    }

    // 禁用透明度
    val alpha = if (disabled) 0.5f else 1f
    val widthModifier = if (block) Modifier.fillMaxWidth() else Modifier.wrapContentWidth()

    Box(
        modifier = modifier
            .then(widthModifier) // 关键修复：由 block 参数严格控制外层宽度
            .height(height)
            .alpha(alpha)
            .clip(shape)
            // 背景色处理（支持渐变）
            .then(if (gradient != null) Modifier.background(gradient) else Modifier.background(backgroundColor))
            // 边框处理（渐变通常无边框）
            .then(if (gradient == null) Modifier.border(borderStroke, shape) else Modifier)
            // 点击事件
            .clickable(
                enabled = !disabled && !loading,
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
//                indication = rememberRipple()
            ),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            Row(
                // Padding 加在内部 Row 上，确保按钮有最小宽度支撑
                modifier = Modifier.padding(horizontal = paddingH),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // --- 加载状态 ---
                if (loading) {
                    val spinnerSize = loadingSize ?: (height * 0.5f)
                    CircularProgressIndicator(
                        modifier = Modifier.size(spinnerSize),
                        color = contentColor,
                        strokeWidth = 2.dp
                    )
                    if (!loadingText.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = loadingText, fontSize = fontSize)
                    }
                } else {
                    // --- 正常状态 ---

                    // 左侧图标
                    if (iconPosition == VanButtonIconPosition.Left) {
                        if (icon != null) {
                            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(fontSize.value.dp + 4.dp))
                            if (!text.isNullOrEmpty() || content != null) Spacer(modifier = Modifier.width(4.dp))
                        } else if (iconComposable != null) {
                            iconComposable()
                            if (!text.isNullOrEmpty() || content != null) Spacer(modifier = Modifier.width(4.dp))
                        }
                    }

                    // 文字
                    if (content != null) {
                        content()
                    } else if (text != null) {
                        Text(
                            text = text,
                            fontSize = fontSize,
                            fontWeight = if (isDefaultType) FontWeight.Normal else FontWeight.Medium,
                            style = TextStyle(lineHeight = fontSize)
                        )
                    }

                    // 右侧图标
                    if (iconPosition == VanButtonIconPosition.Right) {
                        if (icon != null) {
                            if (!text.isNullOrEmpty() || content != null) Spacer(modifier = Modifier.width(4.dp))
                            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(fontSize.value.dp + 4.dp))
                        } else if (iconComposable != null) {
                            if (!text.isNullOrEmpty() || content != null) Spacer(modifier = Modifier.width(4.dp))
                            iconComposable()
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Preview(showBackground = true, backgroundColor = 0xFFF7F8FA, heightDp = 2000)
@Composable
fun VanButtonDemoPreview() {
    MaterialTheme {
        Surface(color = Color(0xFFF7F8FA), modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 40.dp)
            ) {
                // 1. 按钮类型
                DemoSection("按钮类型") {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        VanButton(type = VanButtonType.Primary, text = "主要按钮")
                        VanButton(type = VanButtonType.Success, text = "成功按钮")
                        VanButton(type = VanButtonType.Default, text = "默认按钮")
                        VanButton(type = VanButtonType.Danger, text = "危险按钮")
                        VanButton(type = VanButtonType.Warning, text = "警告按钮")
                    }
                }

                // 2. 朴素按钮
                DemoSection("朴素按钮") {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        VanButton(plain = true, type = VanButtonType.Primary, text = "朴素按钮")
                        VanButton(plain = true, type = VanButtonType.Success, text = "朴素按钮")
                    }
                }

                // 3. 细边框
                DemoSection("细边框 (Hairline)") {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        VanButton(plain = true, hairline = true, type = VanButtonType.Primary, text = "细边框按钮")
                        VanButton(plain = true, hairline = true, type = VanButtonType.Success, text = "细边框按钮")
                    }
                }

                // 4. 禁用状态
                DemoSection("禁用状态") {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        VanButton(disabled = true, type = VanButtonType.Primary, text = "禁用状态")
                        VanButton(disabled = true, type = VanButtonType.Success, text = "禁用状态")
                    }
                }

                // 5. 加载状态
                DemoSection("加载状态") {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        VanButton(loading = true, type = VanButtonType.Primary)
                        VanButton(loading = true, type = VanButtonType.Primary, loadingText = "加载中...")
                        VanButton(loading = true, type = VanButtonType.Success, loadingText = "加载中...")
                    }
                }

                // 6. 按钮形状
                DemoSection("按钮形状") {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        VanButton(square = true, type = VanButtonType.Primary, text = "方形按钮")
                        VanButton(round = true, type = VanButtonType.Success, text = "圆形按钮")
                    }
                }

                // 7. 图标按钮
                DemoSection("图标按钮") {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        VanButton(icon = Icons.Default.Add, type = VanButtonType.Primary)
                        VanButton(icon = Icons.Default.Add, type = VanButtonType.Primary, text = "按钮")
                        VanButton(
                            plain = true,
                            icon = Icons.Default.Star,
                            type = VanButtonType.Primary,
                            text = "按钮"
                        )
                    }
                }

                // 8. 图标位置
                DemoSection("图标位置") {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        VanButton(
                            icon = Icons.Default.ArrowForward,
                            iconPosition = VanButtonIconPosition.Right,
                            type = VanButtonType.Primary,
                            text = "下一步"
                        )
                    }
                }

                // 9. 按钮尺寸
                DemoSection("按钮尺寸") {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        VanButton(size = VanButtonSize.Large, type = VanButtonType.Primary, text = "大号按钮", block = true)
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            VanButton(size = VanButtonSize.Normal, type = VanButtonType.Primary, text = "普通按钮")
                            VanButton(size = VanButtonSize.Small, type = VanButtonType.Primary, text = "小型按钮")
                            VanButton(size = VanButtonSize.Mini, type = VanButtonType.Primary, text = "迷你")
                        }
                    }
                }

                // 10. 块级元素
                DemoSection("块级元素") {
                    VanButton(type = VanButtonType.Primary, block = true, text = "块级元素")
                }

                // 11. 自定义颜色
                DemoSection("自定义颜色") {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        VanButton(color = Color(0xFF7232DD), text = "单色按钮")
                        VanButton(color = Color(0xFF7232DD), plain = true, text = "单色按钮")

                        // 渐变色
                        val gradient = Brush.horizontalGradient(
                            colors = listOf(Color(0xFFFF6034), Color(0xFFEE0A24))
                        )
                        VanButton(
                            gradient = gradient,
                            text = "渐变色按钮",
                            color = Color.White // 这里指定color是为了确保文字是白色的
                        )
                    }
                    Row{
                        VanButton(square = true, type = VanButtonType.Primary, text = "方形按钮")
                        VanButton(square = true, type = VanButtonType.Primary, text = "方形按钮")
                    }
                }
            }
        }
    }
}

/**
 * 辅助组件：用于 Demo 标题展示
 */
@Composable
fun DemoSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = title,
            modifier = Modifier.padding(bottom = 12.dp),
            style = TextStyle(
                color = Color(0xFF969799),
                fontSize = 14.sp
            )
        )
        content()
    }
}