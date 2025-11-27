package com.template.core.ui.vant

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- 枚举定义 ---
enum class VanBadgePosition {
    TopLeft, TopRight, BottomLeft, BottomRight
}

// --- 颜色常量 ---
private object VanBadgeColors {
    val DefaultRed = Color(0xFFEE0A24)
    val White = Color.White
}

/**
 * VanBadge - 徽标
 *
 * @param content 徽标内容 (String)，优先级低于 badgeSlot
 * @param count 徽标数字 (Int)，若设置了此项，max 属性才会生效。优先级低于 content
 * @param max 最大值，超过最大值会显示 {max}+
 * @param dot 是否展示为小红点
 * @param showZero 当 count 为 0 时，是否展示
 * @param color 徽标背景颜色
 * @param textColor 徽标文字颜色
 * @param position 徽标位置 (仅在有子元素时生效)
 * @param offset 偏移量 (x, y)，正数向右/下，负数向左/上
 * @param badgeSlot 自定义徽标内容插槽 (覆盖 content/count/dot)
 * @param child 需要挂载徽标的子元素。若为 null，则独立展示徽标
 */
@Composable
fun VanBadge(
    modifier: Modifier = Modifier,
    content: String? = null,
    count: Int? = null,
    max: Int? = null,
    dot: Boolean = false,
    showZero: Boolean = true,
    color: Color = VanBadgeColors.DefaultRed,
    textColor: Color = VanBadgeColors.White,
    position: VanBadgePosition = VanBadgePosition.TopRight,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    badgeSlot: (@Composable () -> Unit)? = null,
    child: (@Composable () -> Unit)? = null
) {
    // 1. 计算最终显示的文本内容
    val finalContent: String? = when {
        !content.isNullOrEmpty() -> content
        count != null -> {
            if (count == 0 && !showZero) null
            else if (max != null && count > max) "$max+"
            else count.toString()
        }
        else -> null
    }

    // 判断是否需要显示徽标 (有内容，或者 dot为true，或者有插槽)
    val shouldShowBadge = dot || !finalContent.isNullOrEmpty() || badgeSlot != null

    // 2. 徽标本身的 Composable
    val badgeInstance = @Composable {
        if (shouldShowBadge) {
            Box(
                modifier = Modifier
                    // 如果有子元素，给徽标加一个白色描边，视觉上把子元素切开，还原 Vant 风格
                    .then(
                        if (child != null) Modifier.border(1.dp, Color.White, CircleShape) else Modifier
                    )
                    .background(color, CircleShape) // 圆角或圆形背景
                    .then(
                        if (dot) Modifier.size(8.dp) // 小红点固定大小
                        else Modifier
                            .defaultMinSize(minWidth = 16.dp) // 徽标最小宽度
                            .height(16.dp) // 固定高度
                            .padding(horizontal = 3.dp) // 内边距
                    )
                    .clip(CircleShape), // 裁剪
                contentAlignment = Alignment.Center
            ) {
                if (badgeSlot != null) {
                    badgeSlot()
                } else if (!dot && !finalContent.isNullOrEmpty()) {
                    Text(
                        text = finalContent,
                        color = textColor,
                        fontSize = 10.sp, // 10px in Vant roughly
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        lineHeight = 10.sp
                    )
                }
            }
        }
    }

    // 3. 布局逻辑
    if (child == null) {
        // --- 独立展示模式 ---
        Box(modifier = modifier) {
            badgeInstance()
        }
    } else {
        // --- 包裹模式 ---
        Box(modifier = modifier) {
            child()

            // 只有当需要显示时才渲染徽标
            if (shouldShowBadge) {
                // 计算对齐方式
                val alignment = when (position) {
                    VanBadgePosition.TopLeft -> Alignment.TopStart
                    VanBadgePosition.TopRight -> Alignment.TopEnd
                    VanBadgePosition.BottomLeft -> Alignment.BottomStart
                    VanBadgePosition.BottomRight -> Alignment.BottomEnd
                }

                Box(
                    modifier = Modifier
                        .align(alignment)
                        // 关键：自定义 Layout 来实现徽标中心点对齐角点
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)
                            // 根据位置决定偏移方向
                            // Vant 逻辑：translate(50%, -50%) for TopRight
                            val width = placeable.width
                            val height = placeable.height

                            val xOffset = when(position) {
                                VanBadgePosition.TopLeft, VanBadgePosition.BottomLeft -> -width / 2
                                VanBadgePosition.TopRight, VanBadgePosition.BottomRight -> width / 2
                            }
                            val yOffset = when(position) {
                                VanBadgePosition.TopLeft, VanBadgePosition.TopRight -> -height / 2
                                VanBadgePosition.BottomLeft, VanBadgePosition.BottomRight -> height / 2
                            }

                            // 加上用户自定义的 offset (dp 转 px)
                            val manualX = offset.x.roundToPx()
                            val manualY = offset.y.roundToPx()

                            layout(width, height) {
                                placeable.placeRelative(xOffset + manualX, yOffset + manualY)
                            }
                        }
                ) {
                    badgeInstance()
                }
            }
        }
    }
}