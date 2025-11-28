package com.template.core.ui.vant

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- 常量 ---
object VanSearchColors {
    val Background = Color.White // 外部背景默认白色
    val ContentBackground = Color(0xFFF7F8FA) // 内部输入框背景 Gray-1
    val Placeholder = Color(0xFF969799) // Gray-6
    val Text = Color(0xFF323233)
    val Icon = Color(0xFF969799)
    val ActionText = Color(0xFF323233)
}

enum class VanSearchShape {
    Square, Round
}

enum class VanSearchAlign {
    Left, Center, Right
}

/**
 * VanSearch - 搜索框
 *
 * @param value 当前输入的值
 * @param onValueChange 输入变化回调
 * @param label 左侧文本
 * @param shape 形状 Round | Square
 * @param background 外部背景色
 * @param contentBackground 内部输入框背景色 (高级定制)
 * @param placeholder 占位符
 * @param clearable 是否启用清除图标
 * @param showAction 是否显示右侧取消按钮
 * @param actionText 取消按钮文字
 * @param action 自定义右侧操作内容 (覆盖 showAction)
 * @param disabled 是否禁用
 * @param readOnly 是否只读
 * @param align 内容对齐方式
 * @param autoFocus 自动聚焦
 * @param leftIcon 左侧图标
 * @param rightIcon 右侧图标 (输入框内部)
 * @param maxLength 最大长度
 * @param onSearch 确定搜索时触发
 * @param onCancel 点击取消按钮触发
 * @param onClear 点击清除按钮触发
 * @param onClickInput 点击输入区域触发
 */
@Composable
fun VanSearch(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: (@Composable () -> Unit)? = null,
    shape: VanSearchShape = VanSearchShape.Square,
    background: Color = VanSearchColors.Background,
    contentBackground: Color = VanSearchColors.ContentBackground,
    placeholder: String = "",
    clearable: Boolean = true,
    showAction: Boolean = false,
    actionText: String = "取消",
    action: (@Composable () -> Unit)? = null,
    disabled: Boolean = false,
    readOnly: Boolean = false,
    align: VanSearchAlign = VanSearchAlign.Left,
    autoFocus: Boolean = false,
    leftIcon: ImageVector? = Icons.Outlined.Search,
    rightIcon: (@Composable () -> Unit)? = null,
    maxLength: Int = Int.MAX_VALUE,
    onSearch: ((String) -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
    onClear: (() -> Unit)? = null,
    onClickInput: (() -> Unit)? = null,
    focusRequester: FocusRequester = remember { FocusRequester() } // 允许外部控制焦点
) {
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    // 处理自动聚焦
    LaunchedEffect(autoFocus) {
        if (autoFocus) {
            focusRequester.requestFocus()
        }
    }

    val textAlign = when (align) {
        VanSearchAlign.Left -> TextAlign.Start
        VanSearchAlign.Center -> TextAlign.Center
        VanSearchAlign.Right -> TextAlign.End
    }

    val shapeObj = if (shape == VanSearchShape.Round) CircleShape else RoundedCornerShape(2.dp)

    // 外部容器
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(background)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- Label (左侧外) ---
        if (label != null) {
            Box(modifier = Modifier.padding(end = 8.dp)) {
                CompositionLocalProvider(
                    LocalTextStyle provides TextStyle(
                        fontSize = 14.sp,
                        color = VanSearchColors.Text
                    )
                ) {
                    label()
                }
            }
        }

        // --- 输入框容器 ---
        Row(
            modifier = Modifier
                .weight(1f)
                .height(34.dp) // Vant 标准高度
                .background(contentBackground, shapeObj)
                .padding(horizontal = 8.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = !disabled,
                    onClick = {
                        if (!readOnly && !disabled) {
                            focusRequester.requestFocus()
                        }
                        onClickInput?.invoke()
                    }
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 左侧图标 (Search Icon)
            if (leftIcon != null) {
                Icon(
                    imageVector = leftIcon,
                    contentDescription = null,
                    tint = VanSearchColors.Icon,
                    modifier = Modifier.size(20.dp) // Vant Icon Size
                )
                Spacer(modifier = Modifier.width(4.dp))
            }

            // 输入框 Core
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = if (align == VanSearchAlign.Center) Alignment.Center else Alignment.CenterStart
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = {
                        if (it.length <= maxLength) {
                            onValueChange(it)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged { isFocused = it.isFocused },
                    enabled = !disabled,
                    readOnly = readOnly,
                    singleLine = true,
                    textStyle = TextStyle(
                        color = if (disabled) VanSearchColors.Icon else VanSearchColors.Text,
                        fontSize = 14.sp,
                        textAlign = textAlign
                    ),
                    cursorBrush = SolidColor(VanSearchColors.ActionText), // 光标颜色
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onSearch?.invoke(value)
                            focusManager.clearFocus()
                        }
                    ),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = if (align == VanSearchAlign.Center) Alignment.Center else Alignment.CenterStart) {
                            // Placeholder
                            if (value.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    style = TextStyle(
                                        color = VanSearchColors.Placeholder,
                                        fontSize = 14.sp,
                                        textAlign = textAlign
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            // 清除按钮 (仅在有内容、启用clearable、且聚焦时显示，或者 Vant 逻辑是有内容+聚焦)
            // React Vant 文档: clearTrigger 默认为 focus (输入框聚焦且不为空时展示)
            if (clearable && value.isNotEmpty() && isFocused && !disabled && !readOnly) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Clear",
                    tint = Color(0xFFC8C9CC), // 浅灰色关闭图标
                    modifier = Modifier
                        .size(16.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onValueChange("")
                            onClear?.invoke()
                            focusRequester.requestFocus() // 点击清除后保持焦点
                        }
                )
            }

            // 右侧图标 (Input 内)
            if (rightIcon != null) {
                Spacer(modifier = Modifier.width(4.dp))
                rightIcon()
            }
        }

        // --- Action (右侧外) ---
        // 动画显示 Action
        AnimatedVisibility(
            visible = showAction || action != null,
            enter = fadeIn() + expandHorizontally(expandFrom = Alignment.Start),
            exit = fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.Start)
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        enabled = !disabled
                    ) {
                        onCancel?.invoke()
                        focusManager.clearFocus()
                    },
                contentAlignment = Alignment.Center
            ) {
                if (action != null) {
                    action()
                } else {
                    Text(
                        text = actionText,
                        color = VanSearchColors.ActionText,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}