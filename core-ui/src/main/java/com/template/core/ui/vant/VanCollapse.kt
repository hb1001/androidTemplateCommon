package com.template.core.ui.vant

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- 状态传递 ---
// 用于将父组件的配置传递给子项 (如是否手风琴模式，当前选中的列表，以及切换回调)
data class CollapseScope(
    val activeNames: Set<String>,
    val onToggle: (String) -> Unit,
    val accordion: Boolean
)

val LocalCollapseScope = compositionLocalOf<CollapseScope> {
    error("No CollapseScope provided")
}

// --- 颜色常量 ---
private object VanCollapseColors {
    val ContentBg = Color.White
    val ContentText = Color(0xFF969799)
    val Border = Color(0xFFEBEDF0)
}

/**
 * VanCollapse - 折叠面板容器
 *
 * @param activeNames 当前展开面板的 name 集合 (多选模式)
 * @param onChange 切换面板时触发的回调
 * @param accordion 是否开启手风琴模式 (只允许展开一个)
 * @param border 是否显示外边框 (Compose 中主要影响底部分割线)
 */
@Composable
fun VanCollapse(
    modifier: Modifier = Modifier,
    activeNames: Set<String>, // 使用 Set 方便处理多选
    onChange: (Set<String>) -> Unit,
    accordion: Boolean = false,
    border: Boolean = true,
    content: @Composable () -> Unit
) {
    // 切换逻辑
    val toggleAction: (String) -> Unit = { name ->
        if (accordion) {
            // 手风琴模式：点击已展开的则收起，点击未展开的则切换为该项
            val newSet = if (activeNames.contains(name)) emptySet() else setOf(name)
            onChange(newSet)
        } else {
            // 普通模式：切换选中状态
            val newSet = activeNames.toMutableSet()
            if (newSet.contains(name)) {
                newSet.remove(name)
            } else {
                newSet.add(name)
            }
            onChange(newSet)
        }
    }

    val scope = CollapseScope(
        activeNames = activeNames,
        onToggle = toggleAction,
        accordion = accordion
    )

    Column(modifier = modifier) {
        // 顶部边框 (如果需要严格还原，Vant 默认 CellGroup 有上下边框，这里简化)
        if (border) {
            HorizontalDivider(thickness = 0.5.dp, color = VanCollapseColors.Border)
        }

        CompositionLocalProvider(LocalCollapseScope provides scope) {
            content()
        }

        // 底部边框
        if (border) {
            HorizontalDivider(thickness = 0.5.dp, color = VanCollapseColors.Border)
        }
    }
}

/**
 * VanCollapseItem - 折叠面板子项
 *
 * @param name 唯一标识符
 * @param title 标题栏左侧内容
 * @param value 标题栏右侧内容
 * @param label 标题栏描述信息
 * @param icon 标题栏左侧图标
 * @param size 标题栏大小
 * @param disabled 是否禁用面板
 * @param border 是否显示内边框
 * @param isLink 是否展示标题栏右侧箭头
 * @param titleComposable 自定义标题插槽
 * @param valueComposable 自定义右侧内容插槽
 * @param content 面板内容
 */
@Composable
fun VanCollapseItem(
    modifier: Modifier = Modifier,
    name: String,
    title: String? = null,
    value: String? = null,
    label: String? = null,
    icon: ImageVector? = null,
    size: VanCellSize = VanCellSize.Normal,
    disabled: Boolean = false,
    border: Boolean = true,
    isLink: Boolean = true,
    titleComposable: (@Composable () -> Unit)? = null,
    valueComposable: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val scope = LocalCollapseScope.current
    val isExpanded = scope.activeNames.contains(name)

    // 箭头旋转动画
    val rotation: Float by animateFloatAsState(
        targetValue = if (isExpanded) -180f else 0f,
        label = "ArrowRotation"
    )

    Column(modifier = modifier) {
        // 1. 标题栏 (复用 VanCell)
        VanCell(
            title = title,
            value = value,
            label = label,
            icon = icon,
            size = size,
            isLink = isLink,
            // 如果禁用了，Cell 本身虽然不可点，但这里我们需要拦截处理
            clickable = !disabled,
            border = isExpanded && border, // 展开时显示底部分割线，收起时通常不显示（或由下一个 item 负责）
            // 自定义右侧图标 (带旋转动画)
            rightIconComposable = if (isLink) {
                {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = if (disabled) Color.LightGray else Color(0xFF969799),
                        modifier = Modifier.rotate(rotation)
                    )
                }
            } else null,
            titleComposable = titleComposable,
            valueComposable = valueComposable,
            onClick = {
                if (!disabled) {
                    scope.onToggle(name)
                }
            }
        )

        // 2. 内容区域 (折叠动画)
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VanCollapseColors.ContentBg)
                    .padding(16.dp)
            ) {
                // 提供默认的内容样式
                CompositionLocalProvider(
                    androidx.compose.material3.LocalTextStyle provides androidx.compose.ui.text.TextStyle(
                        color = if (disabled) Color(0xFFC8C9CC) else VanCollapseColors.ContentText,
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                ) {
                    // 如果禁用了，内容也置灰
                    Box(modifier = if (disabled) Modifier.background(Color.Transparent.copy(alpha=0.6f)) else Modifier) {
                        content()
                    }
                }
            }

            // 内容区域底部分割线
            if (border) {
                HorizontalDivider(thickness = 0.5.dp, color = VanCollapseColors.Border)
            }
        }
    }
}