package com.template.core.ui.vant

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

// 用于在 Tabbar 和 Item 之间传递配置
@Immutable
data class VanTabbarConfig(
    val activeValue: Any?,
    val onValueChange: ((Any) -> Unit)?,
    val activeColor: Color,
    val inactiveColor: Color
)

val LocalVanTabbarConfig = compositionLocalOf<VanTabbarConfig> {
    error("VanTabbarItem must be used within a VanTabbar")
}

/**
 * VanTabbar - 底部标签栏
 *
 * @param value 当前选中标签的名称或索引值
 * @param onChange 切换标签时触发
 * @param modifier 修饰符
 * @param fixed 是否固定在底部 (在 Compose 中通常由父布局如 Scaffold 控制，此处如果为 true，会处理 safeArea)
 * @param border 是否显示外边框
 * @param zIndex 元素 z-index
 * @param activeColor 选中标签的颜色
 * @param inactiveColor 未选中标签的颜色
 * @param safeAreaInsetBottom 是否开启底部安全区适配
 * @param content 子元素 (VanTabbarItem)
 */
@Composable
fun VanTabbar(
    value: Any?,
    modifier: Modifier = Modifier,
    onChange: ((Any) -> Unit)? = null,
    fixed: Boolean = true, // 在 Vant 中默认为 true，影响是否适配底部安全区
    border: Boolean = true,
    zIndex: Float = 1f,
    activeColor: Color = Color(0xFF1989FA), // Vant Primary Blue
    inactiveColor: Color = Color(0xFF7D7E80), // Vant Gray 6
    safeAreaInsetBottom: Boolean = false, // 若 fixed=true，通常需要配合 safeArea
    content: @Composable RowScope.() -> Unit
) {
    val config = remember(value, onChange, activeColor, inactiveColor) {
        VanTabbarConfig(value, onChange, activeColor, inactiveColor)
    }

    // 处理安全区：如果 safeAreaInsetBottom 为 true，或者 fixed 为 true (通常底部栏都需要避让手势条)
    // 这里遵循 Vant API：fixed 只是定位方式，safeAreaInsetBottom 显式控制 padding
    // 但在 Compose 沉浸式中，通常底部 Bar 都需要处理 insets。
    val applySafeArea = safeAreaInsetBottom || fixed

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .zIndex(zIndex)
    ) {
        if (border) {
            Divider(color = Color(0xFFEBEDF0), thickness = 0.5.dp)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp) // Vant 默认高度 50px
                .selectableGroup()
                .run {
                    if (applySafeArea) {
                        // 仅添加 padding，保持背景色延伸
                        this.windowInsetsPadding(WindowInsets.navigationBars)
                    } else {
                        this
                    }
                },
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompositionLocalProvider(LocalVanTabbarConfig provides config) {
                content()
            }
        }
    }
}

/**
 * VanTabbarItem - 标签项
 *
 * @param name 标签名称，作为匹配的标识符。如果不传，请确保在使用时逻辑正确(Compose 无法自动获取 Index)
 * @param icon 图标插槽，接收一个 boolean 表示是否选中
 * @param badge 徽标配置 (接收 Badge 的参数，如 content, dot)
 * @param onClick 点击回调 (会覆盖 Tabbar 的 onChange，一般不需要传)
 * @param content 标签文字内容
 */
@Composable
fun RowScope.VanTabbarItem(
    name: Any,
    modifier: Modifier = Modifier,
    icon: @Composable (active: Boolean) -> Unit,
    badge: @Composable (() -> Unit)? = null, // 可以是 VanBadge 包裹
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val config = LocalVanTabbarConfig.current
    val selected = config.activeValue == name

    val currentColor = if (selected) config.activeColor else config.inactiveColor

    Column(
        modifier = modifier
            .fillMaxHeight()
            .weight(1f) // 均分宽度
            .selectable(
                selected = selected,
                onClick = {
                    if (onClick != null) {
                        onClick()
                    } else {
                        config.onValueChange?.invoke(name)
                    }
                },
                role = Role.Tab,
                interactionSource = remember { MutableInteractionSource() },
                indication = null // Vant Tabbar 点击通常没有水波纹，只有颜色变化
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon 区域
        Box(contentAlignment = Alignment.TopEnd) {
            // 图标
            Box(
                modifier = Modifier.size(22.dp),
                contentAlignment = Alignment.Center
            ) {
                CompositionLocalProvider(LocalContentColor provides currentColor) {
                    icon(selected)
                }
            }

            // 徽标 (Badge)
            // 这里为了适配 Vant 的 badge={{ content: 5 }} 写法，
            // 我们在 Demo 中使用 VanBadge 包裹 icon，或者在这里提供一个覆盖层
            if (badge != null) {
                // 稍微调整 badge 的位置，使其位于图标右上角
                Box(modifier = Modifier.padding(start = 12.dp, bottom = 12.dp)) {
                    badge()
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 文字区域
        CompositionLocalProvider(LocalContentColor provides currentColor) {
            Box {
                androidx.compose.material3.ProvideTextStyle(
                    value = androidx.compose.ui.text.TextStyle(
                        fontSize = 10.sp, // 10px ~ 12px
                        lineHeight = 1.sp,
                        fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                ) {
                    content()
                }
            }
        }
    }
}