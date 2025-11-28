package com.template.core.ui.vant

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- 常量与枚举 ---

object VanCheckboxColors {
    val CheckedDefault = Color(0xFF1989FA) // Vant Blue
    val UncheckedBorder = Color(0xFFC8C9CC) // Gray-5
    val DisabledLabel = Color(0xFFC8C9CC)
    val Label = Color(0xFF323233)
}

enum class VanCheckboxShape {
    Round, Square
}

enum class VanCheckboxLabelPosition {
    Left, Right
}

enum class VanCheckboxDirection {
    Vertical, Horizontal
}

// --- Group 上下文 ---

/**
 * 用于在 Group 和 Checkbox 之间共享状态
 */
internal data class CheckboxGroupContext(
    val values: Set<Any?>,
    val onValueChange: (Set<Any?>) -> Unit,
    val max: Int,
    val disabled: Boolean,
    val checkedColor: Color,
    val iconSize: Dp,
    val direction: VanCheckboxDirection
)

internal val LocalCheckboxGroup = compositionLocalOf<CheckboxGroupContext?> { null }

// --- 组件实现 ---

/**
 * VanCheckboxGroup - 复选框组
 *
 * @param value 当前选中的标识符集合
 * @param onChange 选中变化回调
 * @param max 最大可选数 (0 表示不限制)
 * @param direction 排列方向
 * @param disabled 是否整组禁用
 * @param checkedColor 统一选中的颜色
 * @param iconSize 统一图标大小
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> VanCheckboxGroup(
    value: Set<T>,
    onChange: (Set<T>) -> Unit,
    modifier: Modifier = Modifier,
    max: Int = 0,
    direction: VanCheckboxDirection = VanCheckboxDirection.Vertical,
    disabled: Boolean = false,
    checkedColor: Color = VanCheckboxColors.CheckedDefault,
    iconSize: Dp = 20.dp,
    content: @Composable () -> Unit
) {
    // 将泛型 Set 转为 Any 供内部 Context 使用，回调时再转回
    val context = CheckboxGroupContext(
        values = value as Set<Any?>,
        onValueChange = { newSet -> onChange(newSet as Set<T>) },
        max = max,
        disabled = disabled,
        checkedColor = checkedColor,
        iconSize = iconSize,
        direction = direction
    )

    CompositionLocalProvider(LocalCheckboxGroup provides context) {
        if (direction == VanCheckboxDirection.Vertical) {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                content()
            }
        } else {
            // 水平排列，使用 FlowRow 自动换行，或者 Row
            FlowRow(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                content()
            }
        }
    }
}

/**
 * VanCheckbox - 复选框
 *
 * @param checked 是否选中 (单独使用时)
 * @param onChange 状态变更回调 (单独使用时)
 * @param name 标识符 (在 Group 中使用时必填)
 * @param shape 形状 Round | Square
 * @param disabled 是否禁用
 * @param labelDisabled 是否禁用文本点击
 * @param labelPosition 文本位置 Left | Right
 * @param iconSize 图标大小
 * @param checkedColor 选中颜色
 * @param iconRender 自定义图标渲染 (checked, disabled) -> Composable
 */
@Composable
fun VanCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onChange: ((Boolean) -> Unit)? = null,
    name: Any? = null, // Group 模式下的 Key
    shape: VanCheckboxShape = VanCheckboxShape.Round,
    disabled: Boolean = false,
    labelDisabled: Boolean = false,
    labelPosition: VanCheckboxLabelPosition = VanCheckboxLabelPosition.Right,
    iconSize: Dp = 20.dp,
    checkedColor: Color = VanCheckboxColors.CheckedDefault,
    iconRender: (@Composable (Boolean, Boolean) -> Unit)? = null,
    content: (@Composable () -> Unit)? = null // Label
) {
    val groupContext = LocalCheckboxGroup.current

    // --- 状态判定逻辑 ---
    // 如果在 Group 中，状态由 Group 决定；否则由 props 决定
    val isChecked = if (groupContext != null && name != null) {
        groupContext.values.contains(name)
    } else {
        checked
    }

    val isDisabled = groupContext?.disabled ?: disabled
    val activeColor = groupContext?.checkedColor ?: checkedColor
    val currentIconSize = groupContext?.iconSize ?: iconSize

    // --- 交互逻辑 ---
    val toggle = {
        if (!isDisabled) {
            val nextChecked = !isChecked

            if (groupContext != null && name != null) {
                // Group 模式
                val currentSet = groupContext.values.toMutableSet()
                if (nextChecked) {
                    // 检查 Max 限制
                    if (groupContext.max == 0 || currentSet.size < groupContext.max) {
                        currentSet.add(name)
                        groupContext.onValueChange(currentSet)
                    }
                } else {
                    currentSet.remove(name)
                    groupContext.onValueChange(currentSet)
                }
            } else {
                // 单独模式
                onChange?.invoke(nextChecked)
            }
        }
    }

    // --- 布局 ---
    val interactionSource = remember { MutableInteractionSource() }

    // 整个 Row 的点击事件 (如果 Label 没有禁用)
    val rowModifier = modifier
        .clickable(
            interactionSource = interactionSource,
            indication = null, // 移除水波纹，Vant 默认没有整体水波纹，或者只有 Icon 有
            enabled = !isDisabled && !labelDisabled,
            onClick = { toggle() },
            role = Role.Checkbox
        )

    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 如果文本在左边
        if (labelPosition == VanCheckboxLabelPosition.Left && content != null) {
            LabelContent(content, isDisabled)
        }

        // --- 图标区域 ---
        Box(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null, // 自定义反馈动画，这里先去掉默认
                    enabled = !isDisabled,
                    onClick = {
                        // 如果 labelDisabled 为 true，这里必须响应点击；
                        // 如果 labelDisabled 为 false，外层 Row 已经处理了，这里防止冒泡重复触发
                        if (labelDisabled) toggle()
                    }
                )
        ) {
            if (iconRender != null) {
                // 自定义图标
                Box(modifier = Modifier.size(currentIconSize)) {
                    iconRender(isChecked, isDisabled)
                }
            } else {
                // 默认图标
                DefaultIcon(
                    checked = isChecked,
                    disabled = isDisabled,
                    shape = shape,
                    checkedColor = activeColor,
                    size = currentIconSize
                )
            }
        }

        // 如果文本在右边 (默认)
        if (labelPosition == VanCheckboxLabelPosition.Right && content != null) {
            LabelContent(content, isDisabled)
        }
    }
}

@Composable
private fun LabelContent(
    content: @Composable () -> Unit,
    disabled: Boolean
) {
    val textColor = if (disabled) VanCheckboxColors.DisabledLabel else VanCheckboxColors.Label
    Box {
        CompositionLocalProvider(
            androidx.compose.material3.LocalTextStyle provides TextStyle(
                color = textColor,
                fontSize = 15.sp // Vant Font Size
            )
        ) {
            content()
        }
    }
}

@Composable
private fun DefaultIcon(
    checked: Boolean,
    disabled: Boolean,
    shape: VanCheckboxShape,
    checkedColor: Color,
    size: Dp
) {
    // 动画状态
    val transition = updateTransition(checked, label = "CheckboxTransition")

    val backgroundColor by transition.animateColor(label = "BgColor") { isChecked ->
        if (isChecked && !disabled) checkedColor
        else if (isChecked && disabled) Color(0xFFEBEDF0) // Disabled Checked
        else Color.Transparent // Unchecked
    }

    val borderColor by transition.animateColor(label = "BorderColor") { isChecked ->
        if (isChecked && !disabled) checkedColor
        else if (disabled) Color(0xFFC8C9CC).copy(alpha = 0.5f)
        else VanCheckboxColors.UncheckedBorder
    }

    val iconScale by transition.animateFloat(
        label = "IconScale",
        transitionSpec = {
            if (targetState) {
                spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium) // 弹跳效果
            } else {
                snap() // 消失时瞬间消失
            }
        }
    ) { isChecked ->
        if (isChecked) 1f else 0f
    }

    val shapeObj: Shape = if (shape == VanCheckboxShape.Round) CircleShape else RoundedCornerShape(3.dp)

    Box(
        modifier = Modifier
            .size(size)
            .clip(shapeObj)
            .background(backgroundColor)
            .border(1.dp, borderColor, shapeObj),
        contentAlignment = Alignment.Center
    ) {
        // 勾选图标
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = if (disabled) VanCheckboxColors.DisabledLabel else Color.White,
                modifier = Modifier
                    .scale(iconScale)
                    .size(size * 0.7f) // 图标略小于圆框
            )
        }
    }
}