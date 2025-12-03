package com.template.core.ui.vant

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import java.math.BigDecimal
import java.math.RoundingMode

enum class VanStepperTheme {
    Default,
    Round
}

/**
 * VanStepper - 步进器
 *
 * @param value 当前值
 * @param onValueChange 值变化回调
 * @param min 最小值
 * @param max 最大值
 * @param step 步长
 * @param decimalLength 固定显示的小数位数，null 表示不限制
 * @param theme 样式风格 (Default / Round)
 * @param integer 是否只允许输入整数
 * @param disabled 是否禁用整个组件
 * @param disableInput 是否禁用输入框
 * @param disablePlus 是否禁用增加按钮
 * @param disableMinus 是否禁用减少按钮
 * @param inputWidth 输入框宽度
 * @param buttonSize 按钮大小 (高度 & 宽度)
 * @param showPlus 是否显示增加按钮
 * @param showMinus 是否显示减少按钮
 * @param showInput 是否显示输入框
 * @param longPress 是否开启长按手势
 */
@Composable
fun VanStepper(
    value: Double,
    onValueChange: (Double) -> Unit,
    modifier: Modifier = Modifier,
    min: Double = 0.0, // Vant 默认为 -Infinity，这里给个常用默认值，或者使用 Double.MIN_VALUE
    max: Double = Double.MAX_VALUE,
    step: Double = 1.0,
    decimalLength: Int? = null,
    theme: VanStepperTheme = VanStepperTheme.Default,
    integer: Boolean = false,
    disabled: Boolean = false,
    disableInput: Boolean = false,
    disablePlus: Boolean = false,
    disableMinus: Boolean = false,
    inputWidth: Dp = 32.dp,
    buttonSize: Dp = 28.dp,
    showPlus: Boolean = true,
    showMinus: Boolean = true,
    showInput: Boolean = true,
    longPress: Boolean = true,
) {
    // BigDecimal 计算，避免浮点数精度问题 (例如 0.1 + 0.2)
    fun calculate(v1: Double, v2: Double, operator: (BigDecimal, BigDecimal) -> BigDecimal): Double {
        val b1 = BigDecimal.valueOf(v1)
        val b2 = BigDecimal.valueOf(v2)
        return operator(b1, b2).toDouble()
    }

    // 格式化数值：处理小数位限制
    fun formatValue(num: Double): Double {
        val bd = BigDecimal.valueOf(num)
        return if (decimalLength != null) {
            bd.setScale(decimalLength, RoundingMode.HALF_UP).toDouble()
        } else {
            // 如果没有指定小数位，可以考虑去除多余的0，但在 Double 层面由 formatText 处理显示
            num
        }
    }

    // 格式化显示的文本
    fun formatText(num: Double): String {
        return if (decimalLength != null) {
            String.format("%.${decimalLength}f", num)
        } else {
            // 如果是整数，去掉 .0
            if (num % 1.0 == 0.0) {
                String.format("%.0f", num)
            } else {
                num.toString()
            }
        }
    }

    // 更新值的统一入口
    fun triggerChange(newValue: Double) {
        val formatted = formatValue(newValue)
        // 边界检查
        val clamped = formatted.coerceIn(min, max)
        if (clamped != value) {
            onValueChange(clamped)
        }
    }

    // 按钮动作
    fun onPlus() {
        triggerChange(calculate(value, step, BigDecimal::add))
    }

    fun onMinus() {
        triggerChange(calculate(value, step, BigDecimal::subtract))
    }

    // 状态判断
    // 注意：BigDecimal 的比较更安全，这里简化使用 Double
    val minusDisabled = disabled || disableMinus || value <= min
    val plusDisabled = disabled || disablePlus || value >= max

    // 样式参数
    val isRound = theme == VanStepperTheme.Round
    // Default 风格：按钮灰色背景；Round 风格：按钮跟随主题色(透明/实心)
    val defaultBgColor = Color(0xFFF2F3F5) // Vant Gray 2
    val buttonBgColor = if (isRound) Color.Transparent else defaultBgColor

    // 按钮形状
    val buttonShape: Shape = if (isRound) CircleShape else RoundedCornerShape(4.dp)

    // 输入框样式
    val inputBgColor = if (isRound) Color.Transparent else defaultBgColor
    val inputTextColor = if (disabled) Color(0xFFC8C9CC) else Color(0xFF323233)

    // Round 风格下的按钮颜色 (通常是 Red)
    val themeColor = Color(0xFFEE0A24)
    val roundButtonColor = if (disabled) Color(0xFFC8C9CC) else themeColor
    val roundButtonContentColor = if (disabled) Color.White.copy(alpha = 0.8f) else Color.White

    // Default 风格下的按钮内容颜色
    val defaultButtonContentColor = if (disabled) Color(0xFFC8C9CC) else Color(0xFF323233)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // --- Minus Button ---
        if (showMinus) {
            StepperButton(
                icon = Icons.Default.KeyboardArrowDown,
                onClick = { if (!minusDisabled) onMinus() },
                disabled = minusDisabled,
                longPress = longPress,
                size = buttonSize,
                shape = buttonShape,
                // Round 风格下，Minus 按钮是白色背景带边框；Default 风格是灰色背景
                backgroundColor = if (isRound) Color.White else buttonBgColor,
                contentColor = if (isRound) roundButtonColor else defaultButtonContentColor,
                border = if (isRound && !minusDisabled) 1.dp else 0.dp,
                borderColor = if (isRound) roundButtonColor else Color.Transparent
            )
        }

        // --- Input Field ---
        if (showInput) {
            // 输入框状态管理：当外部 value 变化时，重置 text
            // 使用 TextFieldValue 以便控制光标位置 (虽然 BasicTextField String 重载也能用，但 Value 更稳)
            var textFieldValue by remember(value) {
                mutableStateOf(
                    TextFieldValue(
                        text = formatText(value),
                        selection = TextRange(formatText(value).length)
                    )
                )
            }

            Box(
                modifier = Modifier
                    .width(inputWidth)
                    .height(buttonSize)
                    .clip(if (isRound) RoundedCornerShape(0.dp) else RoundedCornerShape(4.dp))
                    .background(inputBgColor)
                    .alpha(if (disabled) 0.5f else 1f),
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    value = textFieldValue,
                    onValueChange = { newValue ->
                        if (disableInput || disabled) return@BasicTextField

                        val newText = newValue.text

                        // 1. 允许清空
                        if (newText.isEmpty()) {
                            textFieldValue = newValue
                            // 此时不回调 onValueChange，等待输入
                            return@BasicTextField
                        }

                        // 2. 允许输入负号 (如果 min < 0)
                        if (newText == "-" && min < 0) {
                            textFieldValue = newValue
                            return@BasicTextField
                        }

                        // 3. 验证数字格式
                        // 如果要求 integer，不允许有点
                        if (integer && newText.contains(".")) return@BasicTextField

                        val parsed = newText.toDoubleOrNull()
                        if (parsed != null) {
                            textFieldValue = newValue
                            // 这里可以做立即回调，也可以做防抖。
                            // Vant 逻辑是立即回调，但如果不合法则回正。
                            // 我们这里回调解析后的值，上层 View 会更新 active value，从而触发 remember(value) 更新 textFieldValue
                            // 为了输入体验流畅，不应在输入过程中强制 coerceIn，否则用户无法删减数字
                            onValueChange(parsed)
                        }
                    },
                    textStyle = LocalTextStyle.current.copy(
                        color = inputTextColor,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Normal
                    ),
                    singleLine = true,
                    enabled = !disabled && !disableInput,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = if (integer) KeyboardType.Number else KeyboardType.Decimal
                    ),
                    modifier = Modifier.fillMaxHeight(),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.Center) {
                            innerTextField()
                        }
                    }
                )
            }
        }

        // --- Plus Button ---
        if (showPlus) {
            StepperButton(
                icon = Icons.Default.Add,
                onClick = { if (!plusDisabled) onPlus() },
                disabled = plusDisabled,
                longPress = longPress,
                size = buttonSize,
                shape = buttonShape,
                // Round 风格下，Plus 按钮是实心颜色
                backgroundColor = if (isRound) roundButtonColor else buttonBgColor,
                contentColor = if (isRound) roundButtonContentColor else defaultButtonContentColor,
                border = 0.dp,
                borderColor = Color.Transparent
            )
        }
    }
}

@Composable
private fun StepperButton(
    icon: ImageVector,
    onClick: () -> Unit,
    disabled: Boolean,
    longPress: Boolean,
    size: Dp,
    shape: Shape,
    backgroundColor: Color,
    contentColor: Color,
    border: Dp,
    borderColor: Color
) {
    val currentOnClick by rememberUpdatedState(onClick)
    val interactionSource = remember { MutableInteractionSource() }

    // 长按处理
    // 只有非禁用且开启长按时才处理手势
    val modifier = if (!disabled) {
        Modifier.pointerInput(longPress) {
            detectTapGestures(
                onPress = {
                    val press = PressInteraction.Press(it)
                    interactionSource.emit(press)

                    // 1. 立即触发一次
                    currentOnClick()

                    if (longPress) {
                        // 2. 长按逻辑：延迟 600ms 后开始连续触发
                        val start = System.currentTimeMillis()
                        try {
                            // 等待直到松手或超时
                            withTimeoutOrNull(600) {
                                tryAwaitRelease()
                            } ?: run {
                                // 超过 600ms 还没松手，进入循环触发
                                while (true) { // 持续触发
                                    currentOnClick()
                                    delay(200) // 200ms 间隔
                                }
                            }
                        } catch (e: Exception) {
                            // ignore cancel
                        }
                    }

                    interactionSource.emit(PressInteraction.Release(press))
                }
            )
        }
    } else {
        Modifier // 禁用时无手势
    }

    Box(
        modifier = Modifier
            .size(size)
            .clip(shape)
            .background(backgroundColor)
            .border(border, borderColor, shape)
            .then(modifier)
            .alpha(if (disabled) 0.5f else 1f),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(if (size < 24.dp) size * 0.7f else 16.dp) // 根据按钮大小调整图标大小
        )
    }
}

// 辅助函数：替代标准库没有的 withTimeoutOrNull (其实标准库有，这里为了减少依赖如果环境不包含)
// 但 Compose 环境通常有 kotlinx-coroutines-core。上面的代码假设有。
// 如果没有，可以用 delay 实现简单的逻辑。
private suspend fun <T> withTimeoutOrNull(
    timeMillis: Long,
    block: suspend () -> T
): T? {
    return try {
        coroutineScope {
            withTimeout(timeMillis) {
                block()
            }
        }
    } catch (_: TimeoutCancellationException) {
        null
    }
}