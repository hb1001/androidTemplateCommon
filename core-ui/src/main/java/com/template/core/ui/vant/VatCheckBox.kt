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

object VanCheckboxColors {
    val CheckedDefault = Color(0xFF1989FA)
    val UncheckedBorder = Color(0xFFC8C9CC)
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

@Composable
fun VanCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onChange: ((Boolean) -> Unit)? = null,
    name: Any? = null,
    shape: VanCheckboxShape = VanCheckboxShape.Round,
    disabled: Boolean = false,
    labelDisabled: Boolean = false,
    labelPosition: VanCheckboxLabelPosition = VanCheckboxLabelPosition.Right,
    iconSize: Dp = 20.dp,
    checkedColor: Color = VanCheckboxColors.CheckedDefault,
    iconRender: (@Composable (Boolean, Boolean) -> Unit)? = null,
    content: (@Composable () -> Unit)? = null
) {
    val groupContext = LocalCheckboxGroup.current

    val isChecked = if (groupContext != null && name != null) {
        groupContext.values.contains(name)
    } else {
        checked
    }

    val isDisabled = groupContext?.disabled ?: disabled
    val activeColor = groupContext?.checkedColor ?: checkedColor
    val currentIconSize = groupContext?.iconSize ?: iconSize

    val toggle = {
        if (!isDisabled) {
            val nextChecked = !isChecked

            if (groupContext != null && name != null) {
                val currentSet = groupContext.values.toMutableSet()
                if (nextChecked) {
                    if (groupContext.max == 0 || currentSet.size < groupContext.max) {
                        currentSet.add(name)
                        groupContext.onValueChange(currentSet)
                    }
                } else {
                    currentSet.remove(name)
                    groupContext.onValueChange(currentSet)
                }
            } else {
                onChange?.invoke(nextChecked)
            }
        }
    }

    val interactionSource = remember { MutableInteractionSource() }

    val rowModifier = modifier
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = !isDisabled && !labelDisabled,
            onClick = { toggle() },
            role = Role.Checkbox
        )

    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (labelPosition == VanCheckboxLabelPosition.Left && content != null) {
            LabelContent(content, isDisabled)
        }

        // --- 图标区域 ---
        // 修复逻辑同 Radio：labelDisabled=false 时移除子 View 的 clickable，让事件穿透
        val iconModifier = if (labelDisabled) {
            Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = !isDisabled,
                onClick = { toggle() }
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
                DefaultIcon(
                    checked = isChecked,
                    disabled = isDisabled,
                    shape = shape,
                    checkedColor = activeColor,
                    size = currentIconSize
                )
            }
        }

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
                fontSize = 15.sp
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
    val transition = updateTransition(checked, label = "CheckboxTransition")

    val backgroundColor by transition.animateColor(label = "BgColor") { isChecked ->
        if (isChecked && !disabled) checkedColor
        else if (isChecked && disabled) Color(0xFFEBEDF0)
        else Color.Transparent
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
                spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium)
            } else {
                snap()
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
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = if (disabled) VanCheckboxColors.DisabledLabel else Color.White,
                modifier = Modifier
                    .scale(iconScale)
                    .size(size * 0.7f)
            )
        }
    }
}