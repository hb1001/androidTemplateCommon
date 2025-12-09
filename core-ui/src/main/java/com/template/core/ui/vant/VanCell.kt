package com.template.core.ui.vant

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// --- 枚举定义 ---

enum class VanCellSize {
    Normal, Large
}

enum class VanCellArrowDirection {
    Right, Left, Up, Down
}

// --- 颜色常量 ---
private object VanCellColors {
    val TextColor = Color(0xFF323233)        // 标题色
    val TextColor2 = Color(0xFF969799)       // 说明/Value色
    val ActiveColor = Color(0xFFF2F3F5)      // 点击背景色
    val Background = Color.White             // 单元格背景
    val GroupTitle = Color(0xFF969799)       // 分组标题色
    val Border = Color(0xFFEBEDF0)           // 分割线
    val Required = Color(0xFFEE0A24)         // 必填星号
}

/**
 * VanCellGroup - 单元格组
 *
 * @param title 分组标题
 * @param inset 是否展示为圆角卡片风格
 * @param border 是否显示外边框 (Compose版中主要控制是否裁切圆角背景)
 */
@Composable
fun VanCellGroup(
    modifier: Modifier = Modifier,
    title: String? = null,
    inset: Boolean = false,
    border: Boolean = true, // 在 Compose 实现中，border 更多体现为容器背景处理
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(if (inset) Modifier.padding(horizontal = 16.dp) else Modifier)
    ) {
        // 分组标题
        if (!title.isNullOrEmpty()) {
            Text(
                text = title,
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        top = 16.dp,
                        end = 16.dp,
                        bottom = 8.dp
                    ),
                color = VanCellColors.GroupTitle,
                fontSize = 14.sp,
                lineHeight = 16.sp
            )
        }

        // 内容容器
        Surface(
            color = VanCellColors.Background,
            shape = if (inset) RoundedCornerShape(8.dp) else RoundedCornerShape(0.dp),
            modifier = Modifier.fillMaxWidth().clip(if (inset) RoundedCornerShape(8.dp) else RoundedCornerShape(0.dp))
        ) {
            Column {
                content()
            }
        }
    }
}

/**
 * VanCell - 单元格
 *
 * @param title 左侧标题
 * @param value 右侧内容
 * @param label 标题下方的描述信息
 * @param size 单元格大小 (Normal, Large)
 * @param icon 左侧图标 Vector
 * @param isLink 是否展示右侧箭头并开启点击反馈
 * @param arrowDirection 箭头方向
 * @param center 是否使内容垂直居中
 * @param required 是否显示表单必填星号
 * @param clickable 是否开启点击反馈 (isLink=true 时默认为 true)
 * @param border 是否显示内边框 (底部分割线)
 * @param onClick 点击回调
 * @param titleComposable 自定义标题插槽 (覆盖 title 参数)
 * @param valueComposable 自定义右侧内容插槽 (覆盖 value 参数)
 * @param labelComposable 自定义描述信息插槽 (覆盖 label 参数)
 * @param iconComposable 自定义左侧图标插槽 (覆盖 icon 参数)
 * @param rightIconComposable 自定义右侧图标插槽 (覆盖默认箭头)
 */
@Composable
fun VanCell(
    modifier: Modifier = Modifier,
    // 基础属性
    title: String? = null,
    value: String? = null,
    label: String? = null,
    size: VanCellSize = VanCellSize.Normal,
    // 图标与交互
    icon: ImageVector? = null,
    isLink: Boolean = false,
    arrowDirection: VanCellArrowDirection = VanCellArrowDirection.Right,
    center: Boolean = false,
    required: Boolean = false,
    clickable: Boolean? = null, // null时自动根据 isLink 判断
    border: Boolean = true,
    // 事件
    onClick: () -> Unit = {},
    // 插槽 (Slots)
    titleComposable: (@Composable () -> Unit)? = null,
    valueComposable: (@Composable () -> Unit)? = null,
    labelComposable: (@Composable () -> Unit)? = null,
    iconComposable: (@Composable () -> Unit)? = null,
    rightIconComposable: (@Composable () -> Unit)? = null,
) {
    // 1. 样式计算
    val isLarge = size == VanCellSize.Large
    val verticalPadding = if (isLarge) 12.dp else 10.dp
    val titleFontSize = if (isLarge) 16.sp else 14.sp
    val labelFontSize = if (isLarge) 14.sp else 12.sp
    val horizontalPadding = 16.dp

    // 是否可点击
    val isClickable = clickable ?: isLink

    // 垂直对齐方式：center=true 则居中，否则(默认)顶部对齐
    val verticalAlignment = if (center) Alignment.CenterVertically else Alignment.Top

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(VanCellColors.Background)
            .clickable(
                enabled = isClickable,
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
//                indication = if (isClickable) rememberRipple(color = Color.Gray) else null
            )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            verticalAlignment = verticalAlignment
        ) {

            // --- 左侧 Icon ---
            if (iconComposable != null) {
                iconComposable()
                Spacer(modifier = Modifier.width(4.dp))
            } else if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = VanCellColors.TextColor,
                    modifier = Modifier
                        .size(16.dp)
                        // 如果不是 center 对齐，且有 label，图标通常需要微调下沉一点点或顶部对齐，这里简化为根据行高对齐
                        .padding(top = if (!center) 3.dp else 0.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }

            // --- 标题区域 (Title + Label) ---
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // 必填星号
                    if (required) {
                        Text(text = "*", color = VanCellColors.Required, fontSize = titleFontSize)
                    }

                    if (titleComposable != null) {
                        titleComposable()
                    } else if (title != null) {
                        Text(
                            text = title,
                            color = VanCellColors.TextColor,
                            fontSize = titleFontSize,
                            lineHeight = 24.sp
                        )
                    }
                }

                // Label 描述信息
                if (labelComposable != null) {
                    labelComposable()
                } else if (!label.isNullOrEmpty()) {
                    Text(
                        text = label,
                        color = VanCellColors.TextColor2,
                        fontSize = labelFontSize,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // --- 右侧 Value 区域 ---
            val valueColor = if (title.isNullOrEmpty() && titleComposable == null) VanCellColors.TextColor else VanCellColors.TextColor2

            if (valueComposable != null) {
                Box(modifier = Modifier.padding(start = 8.dp)) {
                    valueComposable()
                }
            } else if (value != null) {
                Text(
                    text = value,
                    color = valueColor,
                    fontSize = 14.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // --- 右侧箭头 / 图标 ---
            if (rightIconComposable != null) {
                Box(modifier = Modifier.padding(start = 4.dp)) {
                    rightIconComposable()
                }
            } else if (isLink) {
                val rotateDegrees = when (arrowDirection) {
                    VanCellArrowDirection.Left -> 180f
                    VanCellArrowDirection.Up -> -90f
                    VanCellArrowDirection.Down -> 90f
                    else -> 0f // Right
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = VanCellColors.TextColor2,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .padding(top = if (!center) 2.dp else 0.dp) // 视觉对齐
                        .size(16.dp)
                        .rotate(rotateDegrees)
                )
            }
        }

        // --- 底部内边框 (Hairline) ---
        if (border) {
            HorizontalDivider(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = horizontalPadding), // Vant 默认留出左侧 Padding
                thickness = 0.5.dp,
                color = VanCellColors.Border
            )
        }
    }
}