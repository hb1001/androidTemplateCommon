package com.template.core.ui.vant

import androidx.compose.animation.animateColor
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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- 常量与枚举 ---

object VanRadioColors {
    val CheckedDefault = Color(0xFF1989FA) // Vant Blue
    val UncheckedBorder = Color(0xFFC8C9CC) // Gray-5
    val DisabledLabel = Color(0xFFC8C9CC)
    val Label = Color(0xFF323233)
    val DisabledIcon = Color(0xFFEBEDF0)
}

enum class VanRadioShape {
    Round, Square
}

enum class VanRadioDirection {
    Vertical, Horizontal
}

// --- Group 上下文 ---

internal data class RadioGroupContext(
    val currentValue: Any?,
    val onValueChange: (Any?) -> Unit,
    val disabled: Boolean,
    val direction: VanRadioDirection,
    val iconSize: Dp,
    val checkedColor: Color
)

internal val LocalRadioGroup = compositionLocalOf<RadioGroupContext?> { null }

// --- 组件实现 ---

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> VanRadioGroup(
    value: T,
    onChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    direction: VanRadioDirection = VanRadioDirection.Vertical,
    disabled: Boolean = false,
    iconSize: Dp = 20.dp,
    checkedColor: Color = VanRadioColors.CheckedDefault,
    content: @Composable () -> Unit
) {
    val context = RadioGroupContext(
        currentValue = value,
        onValueChange = { onChange(it as T) },
        disabled = disabled,
        direction = direction,
        iconSize = iconSize,
        checkedColor = checkedColor
    )

    CompositionLocalProvider(LocalRadioGroup provides context) {
        if (direction == VanRadioDirection.Vertical) {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                content()
            }
        } else {
            FlowRow(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun <T> VanRadio(
    name: T,
    modifier: Modifier = Modifier,
    shape: VanRadioShape = VanRadioShape.Round,
    disabled: Boolean = false,
    labelDisabled: Boolean = false,
    iconSize: Dp? = null,
    checkedColor: Color? = null,
    iconRender: (@Composable (Boolean, Boolean) -> Unit)? = null,
    content: (@Composable () -> Unit)? = null
) {
    val groupContext = LocalRadioGroup.current
        ?: throw IllegalStateException("VanRadio must be used inside VanRadioGroup")

    val isChecked = (groupContext.currentValue == name)
    val isDisabled = groupContext.disabled || disabled
    val activeColor = checkedColor ?: groupContext.checkedColor
    val currentIconSize = iconSize ?: groupContext.iconSize

    val onClick = {
        if (!isDisabled && !isChecked) {
            groupContext.onValueChange(name)
        }
    }

    // 整个 Row 的点击事件
    val rowModifier = modifier
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            // 只有当整体未禁用，且 Label 未禁用时，Row 才可以点击
            enabled = !isDisabled && !labelDisabled,
            onClick = onClick,
            role = Role.RadioButton
        )

    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // --- 图标区域 ---
        // 修复逻辑：
        // 如果 labelDisabled = true，说明 Row 不响应点击，我们需要单独给 Icon 添加点击事件。
        // 如果 labelDisabled = false，Row 已经响应点击，Icon 不需要添加 clickable (否则会拦截父级点击)。
        val iconModifier = if (labelDisabled) {
            Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = !isDisabled,
                onClick = onClick
            )
        } else {
            Modifier
        }

        Box(modifier = iconModifier) {
            if (iconRender != null) {
                Box(modifier = Modifier.size(currentIconSize)) {
                    iconRender(isChecked, isDisabled)
                }
            } else {
                DefaultRadioIcon(
                    checked = isChecked,
                    disabled = isDisabled,
                    shape = shape,
                    checkedColor = activeColor,
                    size = currentIconSize
                )
            }
        }

        // --- 文本区域 ---
        if (content != null) {
            val textColor = if (isDisabled) VanRadioColors.DisabledLabel else VanRadioColors.Label
            Box {
                CompositionLocalProvider(
                    LocalTextStyle provides TextStyle(
                        color = textColor,
                        fontSize = 15.sp
                    )
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
private fun DefaultRadioIcon(
    checked: Boolean,
    disabled: Boolean,
    shape: VanRadioShape,
    checkedColor: Color,
    size: Dp
) {
    val transition = updateTransition(checked, label = "RadioTransition")

    val backgroundColor by transition.animateColor(label = "BgColor") { isChecked ->
        if (isChecked && !disabled) checkedColor
        else if (isChecked && disabled) VanRadioColors.DisabledIcon
        else Color.Transparent
    }

    val borderColor by transition.animateColor(label = "BorderColor") { isChecked ->
        if (isChecked && !disabled) checkedColor
        else if (disabled) VanRadioColors.DisabledIcon
        else VanRadioColors.UncheckedBorder
    }

    val iconScale by transition.animateFloat(
        label = "IconScale",
        transitionSpec = {
            if (targetState) {
                spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium)
            } else {
                snap()
            }
        }
    ) { isChecked ->
        if (isChecked) 1f else 0f
    }

    val shapeObj: Shape = if (shape == VanRadioShape.Round) CircleShape else RoundedCornerShape(3.dp)

    Box(
        modifier = Modifier
            .size(size)
            .clip(shapeObj)
            .background(backgroundColor)
            .border(1.dp, borderColor, shapeObj),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = if (disabled) VanRadioColors.DisabledLabel else Color.White,
                modifier = Modifier
                    .scale(iconScale)
                    .size(size * 0.7f)
            )
        }
    }
}