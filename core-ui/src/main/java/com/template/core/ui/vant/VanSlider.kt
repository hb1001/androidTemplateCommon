package com.template.core.ui.vant

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.roundToInt

// --- 颜色常量 ---
private object VanSliderColors {
    val ActiveDefault = Color(0xFF1989FA)
    val InactiveDefault = Color(0xFFEBEDF0)
}

/**
 * VanSlider - 滑块 (修复版)
 */
@Composable
fun VanSlider(
    value: Any, // Float or List<Float>
    onValueChange: (Any) -> Unit,
    modifier: Modifier = Modifier,
    onValueChangeFinished: ((Any) -> Unit)? = null,
    min: Float = 0f,
    max: Float = 100f,
    step: Float = 1f,
    range: Boolean = false,
    disabled: Boolean = false,
    vertical: Boolean = false,
    barHeight: Dp = 2.dp,
    buttonSize: Dp = 24.dp,
    activeColor: Color = VanSliderColors.ActiveDefault,
    inactiveColor: Color = VanSliderColors.InactiveDefault,
    button: (@Composable () -> Unit)? = null,
    leftButton: (@Composable () -> Unit)? = null,
    rightButton: (@Composable () -> Unit)? = null
) {
    val density = LocalDensity.current

    // 1. 统一处理 Values，确保它是 List<Float> 并排序
    // 注意：这里仅仅作为 UI 渲染的数据源
    val currentValues = remember(value, range) {
        if (range && value is List<*>) {
            value.map { (it as Number).toFloat() }.sorted()
        } else {
            listOf((value as Number).toFloat())
        }
    }

    // 轨道尺寸
    var trackSize by remember { mutableStateOf(IntSize.Zero) }

    // --- 关键修复 1：使用 rememberUpdatedState 持有最新状态 ---
    // 这样我们就不需要在 pointerInput 中传入 currentValues 导致手势重启
    val currentValuesState = rememberUpdatedState(currentValues)
    val onValueChangeState = rememberUpdatedState(onValueChange)
    val trackSizeState = rememberUpdatedState(trackSize)

    // 辅助函数：Value -> Px (用于 UI 绘制)
    fun valueToPx(valIn: Float, size: IntSize): Float {
        val totalLen = if (vertical) size.height else size.width
        if (totalLen == 0) return 0f
        val fraction = (valIn - min) / (max - min)
        return fraction * totalLen.toFloat()
    }

    // 触摸热区
    val touchSize = buttonSize.coerceAtLeast(40.dp)

    Box(
        modifier = modifier
            .then(
                if (vertical) Modifier.width(touchSize).fillMaxHeight()
                else Modifier.height(touchSize).fillMaxWidth()
            )
            .onSizeChanged { trackSize = it }
            // --- 关键修复 2：pointerInput 的 key 只包含不会频繁变化的配置项 ---
            // 移除了 currentValues 和 trackSize，改用 disabled, range, vertical 等
            .pointerInput(disabled, range, vertical, min, max, step) {
                if (disabled) return@pointerInput

                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)

                    // 在手势内部获取最新的状态值
                    val latestValues = currentValuesState.value
                    val currentSize = trackSizeState.value
                    if (currentSize.width == 0 || currentSize.height == 0) return@awaitEachGesture

                    // 内部计算函数：Px -> Value
                    fun pxToValue(px: Float): Float {
                        val totalLen = if (vertical) currentSize.height else currentSize.width
                        if (totalLen == 0) return min
                        val clampedPx = px.coerceIn(0f, totalLen.toFloat())
                        val fraction = clampedPx / totalLen.toFloat()
                        val rawVal = min + fraction * (max - min)
                        if (step > 0) {
                            val steps = ((rawVal - min) / step).roundToInt()
                            return (min + steps * step).coerceIn(min, max)
                        }
                        return rawVal.coerceIn(min, max)
                    }

                    // 内部计算函数：Value -> Px (用于寻找最近滑块)
                    fun valToPx(v: Float): Float = valueToPx(v, currentSize)

                    val touchPx = if (vertical) down.position.y else down.position.x

                    // 决定操作哪个滑块
                    var activeThumbIndex = 0
                    if (range && latestValues.size >= 2) {
                        val px0 = valToPx(latestValues[0])
                        val px1 = valToPx(latestValues[1])
                        // 找离触点最近的滑块
                        activeThumbIndex = if (abs(touchPx - px0) <= abs(touchPx - px1)) 0 else 1
                    }

                    // 更新逻辑
                    fun update(currentPos: Offset) {
                        val currPx = if (vertical) currentPos.y else currentPos.x
                        val newValue = pxToValue(currPx)
                        val currentList = currentValuesState.value // 再次获取最新列表，防止并发修改问题

                        if (range) {
                            val mutableList = currentList.toMutableList()
                            // 保护性检查，防止 index越界
                            if (activeThumbIndex < mutableList.size) {
                                mutableList[activeThumbIndex] = newValue
                                val sortedList = mutableList.sorted()

                                // 回调最新值
                                onValueChangeState.value(sortedList)

                                // 追踪手指控制的那个滑块的新索引（防止滑块交叉后控制权丢失）
                                val newIndex = sortedList.indexOf(newValue)
                                if (newIndex != -1) {
                                    activeThumbIndex = newIndex
                                }
                            }
                        } else {
                            onValueChangeState.value(newValue)
                        }
                    }

                    // 响应按下
                    update(down.position)

                    // 响应拖拽
                    drag(down.id) { change ->
                        change.consume()
                        update(change.position)
                    }

                    onValueChangeFinished?.invoke(currentValuesState.value)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // 1. 轨道背景
        Box(
            modifier = Modifier
                .background(inactiveColor, RoundedCornerShape(100))
                .then(
                    if (vertical) Modifier.width(barHeight).fillMaxHeight()
                    else Modifier.height(barHeight).fillMaxWidth()
                )
        )

        // UI 绘制参数计算
        val pxValues = currentValues.map { valueToPx(it, trackSize) }

        // --- 关键修复 3：明确 Active Bar 的起始点和长度 ---
        // Range 模式：Start = min, End = max, Length = End - Start
        // Single 模式：Start = 0, End = value, Length = value
        val startPx = if (range) pxValues.minOrNull() ?: 0f else 0f
        val endPx = pxValues.maxOrNull() ?: 0f
        val barSize = abs(endPx - startPx)

        // 2. 激活轨道 (Active)
        Box(
            modifier = Modifier
                .background(activeColor, RoundedCornerShape(100))
                .then(
                    if (vertical) {
                        Modifier
                            .width(barHeight)
                            .height(with(density) { barSize.toDp() })
                            .align(Alignment.TopCenter) // 垂直模式下，从顶部开始计算偏移
                            .offset(y = with(density) { startPx.toDp() })
                    } else {
                        Modifier
                            .height(barHeight)
                            .width(with(density) { barSize.toDp() })
                            .align(Alignment.CenterStart) // 水平模式下，从左侧开始计算偏移
                            .offset(x = with(density) { startPx.toDp() })
                    }
                )
        )

        // 3. 按钮 (Thumb)
        currentValues.forEachIndexed { index, _ ->
            val px = pxValues.getOrElse(index) { 0f }
            val buttonSizePx = with(density) { buttonSize.toPx() }

            val thumbOffset = if (vertical) {
                // 垂直：Y轴偏移
                IntOffset(0, (px - buttonSizePx / 2).toInt())
            } else {
                // 水平：X轴偏移
                IntOffset((px - buttonSizePx / 2).toInt(), 0)
            }

            Box(
                modifier = Modifier
                    .size(buttonSize)
                    .align(if (vertical) Alignment.TopCenter else Alignment.CenterStart)
                    .offset { thumbOffset }
                    .shadow(2.dp, CircleShape)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (range) {
                    if (index == 0 && leftButton != null) leftButton()
                    else if (index == 1 && rightButton != null) rightButton()
                    else button?.invoke()
                } else {
                    button?.invoke()
                }
            }
        }
    }
}