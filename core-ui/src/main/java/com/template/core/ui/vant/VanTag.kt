package com.template.core.ui.vant

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- 枚举定义 ---
enum class VanTagType {
    Default, Primary, Success, Warning, Danger
}

enum class VanTagSize {
    Small, Medium, Large
}

// --- 颜色常量 ---
private object VanTagColors {
    val Primary = Color(0xFF1989FA)
    val Success = Color(0xFF07C160)
    val Warning = Color(0xFFFF976A)
    val Danger = Color(0xFFEE0A24)
    val Default = Color(0xFF969799)
    val TextWhite = Color.White
}

/**
 * VanTag - 标签组件
 *
 * @param type 类型：primary, success, warning, danger, default
 * @param size 大小：large, medium, small (默认)
 * @param color 标签背景颜色 (会覆盖 type 的默认色)
 * @param show 是否展示标签 (配合动画)
 * @param plain 是否为空心样式
 * @param round 是否为圆角样式 (全圆角)
 * @param mark 是否为标记样式 (半圆角)
 * @param textColor 文本颜色 (优先级高于 color 属性自动推算的文字色)
 * @param closeable 是否为可关闭标签
 * @param onClick 点击事件
 * @param onClose 关闭事件
 * @param content 标签内容
 */
@Composable
fun VanTag(
    modifier: Modifier = Modifier,
    type: VanTagType = VanTagType.Default,
    size: VanTagSize = VanTagSize.Medium,
    color: Color? = null,
    show: Boolean = true,
    plain: Boolean = false,
    round: Boolean = false,
    mark: Boolean = false,
    textColor: Color? = null,
    closeable: Boolean = false,
    onClick: (() -> Unit)? = null,
    onClose: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit
) {
    // 1. 尺寸计算
    val (paddingValues, fontSize) = when (size) {
        VanTagSize.Large -> Pair(PaddingValues(horizontal = 8.dp, vertical = 4.dp), 14.sp)
        VanTagSize.Medium -> Pair(PaddingValues(horizontal = 6.dp, vertical = 2.dp), 12.sp)
        VanTagSize.Small -> Pair(PaddingValues(horizontal = 5.dp, vertical = 2.dp), 10.sp) // Vant 默认更小一点
    }

    // 2. 形状计算
    val shape: Shape = when {
        round -> RoundedCornerShape(percent = 50) // 全圆角
        mark -> RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp, topEnd = 50.dp, bottomEnd = 50.dp) // Vant Mark 样式通常是半圆
        else -> RoundedCornerShape(2.dp) // 默认微圆角
    }

    // 3. 颜色逻辑
    val baseColor = color ?: when (type) {
        VanTagType.Primary -> VanTagColors.Primary
        VanTagType.Success -> VanTagColors.Success
        VanTagType.Warning -> VanTagColors.Warning
        VanTagType.Danger -> VanTagColors.Danger
        VanTagType.Default -> VanTagColors.Default
    }

    // 实际背景色
    val backgroundColor = if (plain) Color.Transparent else baseColor

    // 实际文字/图标颜色
    val finalContentColor = textColor ?: if (plain) baseColor else VanTagColors.TextWhite

    // 边框 (仅 plain 模式)
    val borderModifier = if (plain) {
        Modifier.border(BorderStroke(1.dp, baseColor), shape)
    } else {
        Modifier
    }

    // 4. 动画显示
    AnimatedVisibility(
        visible = show,
        enter = scaleIn() + fadeIn(),
        exit = scaleOut() + fadeOut()
    ) {
        Box(
            modifier = modifier
                .then(borderModifier) // 先画边框
                .background(backgroundColor, shape) // 再画背景
                .clip(shape) // 裁剪点击区域
                .clickable(enabled = onClick != null, onClick = onClick ?: {})
                .padding(paddingValues), // 内边距
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // 提供内容样式
                androidx.compose.runtime.CompositionLocalProvider(
                    androidx.compose.material3.LocalContentColor provides finalContentColor,
                    androidx.compose.material3.LocalTextStyle provides androidx.compose.ui.text.TextStyle(
                        fontSize = fontSize
                    )
                ) {
                    content()

                    // 关闭按钮
                    if (closeable) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Close",
                            modifier = Modifier
                                .size(fontSize.value.dp + 2.dp) // 稍微比文字大一点点方便点击
                                .clickable { onClose?.invoke() }
                        )
                    }
                }
            }
        }
    }
}