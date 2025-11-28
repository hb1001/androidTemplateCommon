package com.template.core.ui.vant

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- 枚举与配置 ---

enum class VanInputType {
    Text, Tel, Digit, Number, Password
}

enum class VanInputAlign {
    Left, Center, Right
}

enum class VanInputClearTrigger {
    Always, Focus
}

object VanInputColors {
    val Text = Color(0xFF323233)
    val Placeholder = Color(0xFF969799)
    val Disabled = Color(0xFFC8C9CC) // Gray-5
    val ClearIcon = Color(0xFFC8C9CC)
    val WordLimit = Color(0xFF646566) // Gray-7
}

// --- 组件实现 ---

/**
 * VanInput - 单行输入框
 *
 * @param value 当前值
 * @param onValueChange 值变化回调
 * @param type 输入类型
 * @param placeholder 占位符
 * @param disabled 是否禁用
 * @param readOnly 是否只读
 * @param clearable 是否可清除
 * @param clearTrigger 清除图标显示时机
 * @param align 文本对齐
 * @param prefix 前缀内容
 * @param suffix 后缀内容
 * @param maxLength 最大长度
 * @param autoFocus 自动聚焦
 * @param onOverlimit 超出限制回调
 * @param onClickInput 点击输入框回调
 */
@Composable
fun VanInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    type: VanInputType = VanInputType.Text,
    placeholder: String = "",
    disabled: Boolean = false,
    readOnly: Boolean = false,
    clearable: Boolean = false,
    clearTrigger: VanInputClearTrigger = VanInputClearTrigger.Focus,
    align: VanInputAlign = VanInputAlign.Left,
    prefix: (@Composable () -> Unit)? = null,
    suffix: (@Composable () -> Unit)? = null,
    maxLength: Int = Int.MAX_VALUE,
    autoFocus: Boolean = false,
    onOverlimit: (() -> Unit)? = null,
    onClickInput: (() -> Unit)? = null,
) {
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(autoFocus) {
        if (autoFocus) focusRequester.requestFocus()
    }

    // 键盘类型映射
    val keyboardOptions = when (type) {
        VanInputType.Tel -> KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done)
        VanInputType.Digit -> KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done) // Digit 通常指支持小数
        VanInputType.Number -> KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done) // 纯数字
        VanInputType.Password -> KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)
        else -> KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done)
    }

    val visualTransformation = if (type == VanInputType.Password) PasswordVisualTransformation() else VisualTransformation.None

    // 对齐映射
    val textAlign = when(align) {
        VanInputAlign.Left -> TextAlign.Start
        VanInputAlign.Center -> TextAlign.Center
        VanInputAlign.Right -> TextAlign.End
    }

    val textColor = if (disabled) VanInputColors.Disabled else VanInputColors.Text
    val focusManager = LocalFocusManager.current

    // 外层容器 (模拟 Cell 的内容区域，不包含 Cell 的 Title)
    // 通常 Input 是放在 VanCell 里的，所以这里只负责 Input 自身
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = !disabled,
                onClick = {
                    if (!readOnly && !disabled) focusRequester.requestFocus()
                    onClickInput?.invoke()
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 前缀
        if (prefix != null) {
            Box(modifier = Modifier.padding(end = 8.dp)) {
                prefix()
            }
        }

        // 输入核心
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = when(align) {
                VanInputAlign.Center -> Alignment.Center
                VanInputAlign.Right -> Alignment.CenterEnd
                else -> Alignment.CenterStart
            }
        ) {
            // Placeholder (手动绘制以支持 Alignment)
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = TextStyle(
                        color = VanInputColors.Placeholder,
                        fontSize = 14.sp,
                        textAlign = textAlign
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            BasicTextField(
                value = value,
                onValueChange = {
                    // 过滤逻辑
                    var nextValue = it

                    // Digit/Number 简单过滤 (Compose KeyboardOptions 只能限制键盘，不能完全阻止粘贴非数字)
                    if (type == VanInputType.Number) {
                        nextValue = it.filter { char -> char.isDigit() }
                    } else if (type == VanInputType.Digit) {
                        nextValue = it.filter { char -> char.isDigit() || char == '.' }
                    }

                    if (nextValue.length <= maxLength) {
                        onValueChange(nextValue)
                    } else {
                        // 超出限制
                        onOverlimit?.invoke()
                        // 不更新值，或者截断更新? Vant 通常是截断
                        onValueChange(nextValue.take(maxLength))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                enabled = !disabled,
                readOnly = readOnly,
                textStyle = TextStyle(
                    color = textColor,
                    fontSize = 14.sp,
                    textAlign = textAlign
                ),
                cursorBrush = SolidColor(VanInputColors.Text), // 光标颜色
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                interactionSource = interactionSource,
                singleLine = true
            )
        }

        // 清除按钮
        val showClear = clearable && value.isNotEmpty() && !disabled && !readOnly &&
                (clearTrigger == VanInputClearTrigger.Always || isFocused)

        AnimatedVisibility(
            visible = showClear,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear",
                tint = VanInputColors.ClearIcon,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(16.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onValueChange("")
                        focusRequester.requestFocus()
                    }
            )
        }

        // 后缀
        if (suffix != null) {
            Box(modifier = Modifier.padding(start = 8.dp)) {
                suffix()
            }
        }
    }
}

/**
 * VanTextArea - 多行文本域
 *
 * @param autoSize 自适应高度 (Compose BasicTextField 默认自适应，可以通过 minHeight/maxHeight 限制)
 * @param showWordLimit 是否显示字数统计
 */
@Composable
fun VanTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    disabled: Boolean = false,
    readOnly: Boolean = false,
    maxLength: Int = Int.MAX_VALUE,
    minHeight: Dp = 60.dp, // 默认高度
    maxHeight: Dp = Dp.Unspecified,
    showWordLimit: Boolean = false,
    wordLimitCustom: (@Composable (Int, Int) -> Unit)? = null, // 自定义统计文案
    onOverlimit: (() -> Unit)? = null,
) {
    val textColor = if (disabled) VanInputColors.Disabled else VanInputColors.Text

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = minHeight, max = maxHeight), // 控制高度范围
            contentAlignment = Alignment.TopStart
        ) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = TextStyle(
                        color = VanInputColors.Placeholder,
                        fontSize = 14.sp
                    )
                )
            }

            BasicTextField(
                value = value,
                onValueChange = {
                    if (it.length <= maxLength) {
                        onValueChange(it)
                    } else {
                        onOverlimit?.invoke()
                        onValueChange(it.take(maxLength))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !disabled,
                readOnly = readOnly,
                textStyle = TextStyle(
                    color = textColor,
                    fontSize = 14.sp
                ),
                cursorBrush = SolidColor(VanInputColors.Text)
            )
        }

        // 字数统计
        if (showWordLimit || wordLimitCustom != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                if (wordLimitCustom != null) {
                    wordLimitCustom(value.length, maxLength)
                } else {
                    Text(
                        text = "${value.length}/$maxLength",
                        color = VanInputColors.WordLimit,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}