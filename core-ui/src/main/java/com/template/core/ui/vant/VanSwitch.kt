package com.template.core.ui.vant

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// --- 颜色常量 ---
private object VanSwitchColors {
    val ActiveDefault = Color(0xFF1989FA) // Vant Primary
    val InactiveDefault = Color.White // Vant Inactive (通常是白色背景 + 灰色边框)
    val InactiveBorder = Color(0xFFE5E5E5) // 边框色
    val NodeBackground = Color.White
}

/**
 * VanSwitch - 开关
 *
 * @param checked 开关选中状态
 * @param onCheckedChange 状态切换回调
 * @param loading 是否为加载状态
 * @param disabled 是否为禁用状态
 * @param size 开关尺寸 (高度)，宽度会自动设为高度的 2 倍
 * @param activeColor 打开时的背景色
 * @param inactiveColor 关闭时的背景色
 */
@Composable
fun VanSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    disabled: Boolean = false,
    size: Dp = 26.dp, // Vant 默认大约是 26px - 30px
    activeColor: Color = VanSwitchColors.ActiveDefault,
    inactiveColor: Color = VanSwitchColors.InactiveDefault,
) {
    // 尺寸计算
    val height = size
    val width = size * 2
    val nodeSize = size // 滑块大小等于高度

    // 边距：Vant 的滑块与边缘有约 2px 的间隙，为了视觉还原，我们稍微缩小滑块或设置 padding
    val padding = 2.dp
    val actualNodeSize = nodeSize - (padding * 2)

    // 滑块位移距离
    val maxOffset = width - nodeSize
    val offset by animateDpAsState(
        targetValue = if (checked) maxOffset else 0.dp,
        label = "SwitchOffset"
    )

    // 背景颜色动画
    val backgroundColor by animateColorAsState(
        targetValue = if (checked) activeColor else inactiveColor,
        label = "SwitchBackground"
    )

    // 边框逻辑：未选中且颜色为默认白色时，显示灰色边框；选中或自定义颜色时，边框透明（或同色）
    val borderColor = if (!checked && inactiveColor == VanSwitchColors.InactiveDefault) {
        VanSwitchColors.InactiveBorder
    } else {
        Color.Transparent
    }

    // 交互逻辑
    val isEnabled = !disabled && !loading
    val alpha = if (disabled) 0.5f else 1f

    Box(
        modifier = modifier
            .size(width, height)
            .alpha(alpha)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(1.dp, borderColor, CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null, // 无水波纹
                enabled = isEnabled,
                onClick = { onCheckedChange?.invoke(!checked) }
            )
            .padding(padding) // 给滑块留出边距
    ) {
        // 滑块 Node
        Box(
            modifier = Modifier
                .offset(x = offset)
                .size(actualNodeSize)
                .shadow(1.dp, CircleShape) // 阴影
                .background(VanSwitchColors.NodeBackground, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Loading 状态
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(actualNodeSize * 0.6f), // Loading 图标比滑块小一圈
                    color = if (checked) activeColor else VanSwitchColors.ActiveDefault,
                    strokeWidth = 2.dp
                )
            }
        }
    }
}