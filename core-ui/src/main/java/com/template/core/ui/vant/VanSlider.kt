package com.template.core.ui.vant

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
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
 * 修复 1: 解决连续滑动卡顿 (使用 rememberUpdatedState)
 * 修复 2: 解决双滑块蓝色区域渲染错误 (使用 Canvas 绘制)
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

    // 1. 数据标准化：确保 currentValues 始终是排序好的 List<Float>
    val currentValues = remember(value, range) {
        if (range && value is List<*>) {
            value.map { (it as Number).toFloat() }.sorted()
        } else {
            listOf((value as Number).toFloat())
        }
    }

    // 轨道尺寸 (用于手势计算和按钮定位)
    var trackSize by remember { mutableStateOf(IntSize.Zero) }

    // --- 状态持有 (解决手势中断的关键) ---
    val currentValuesState = rememberUpdatedState(currentValues)
    val onValueChangeState = rememberUpdatedState(onValueChange)
    val trackSizeState = rememberUpdatedState(trackSize)

    // 辅助函数：值转像素 (UI 用)
    fun valueToPx(valIn: Float, size: IntSize): Float {
        val totalLen = if (vertical) size.height else size.width
        if (totalLen == 0) return 0f
        val fraction = (valIn - min) / (max - min)
        return fraction * totalLen.toFloat()
    }

    // 触摸区域大小
    val touchSize = buttonSize.coerceAtLeast(40.dp)

    Box(
        modifier = modifier
            .then(
                if (vertical) Modifier.width(touchSize).fillMaxHeight()
                else Modifier.height(touchSize).fillMaxWidth()
            )
            .onSizeChanged { trackSize = it }
            // --- 手势处理 ---
            // Key 只包含静态配置，不包含变化的 value，防止手势重置
            .pointerInput(disabled, range, vertical, min, max, step) {
                if (disabled) return@pointerInput

                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    val currentSize = trackSizeState.value
                    val latestValues = currentValuesState.value

                    if (currentSize.width == 0 || currentSize.height == 0) return@awaitEachGesture

                    // 内部: Px 转 Value
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

                    // 内部: Value 转 Px (用于寻找最近滑块)
                    fun valToPx(v: Float) = valueToPx(v, currentSize)

                    val touchPx = if (vertical) down.position.y else down.position.x

                    // Range 模式：判断点击的是哪个滑块（找最近的）
                    var activeThumbIndex = 0
                    if (range && latestValues.size >= 2) {
                        val px0 = valToPx(latestValues[0])
                        val px1 = valToPx(latestValues[1])
                        activeThumbIndex = if (abs(touchPx - px0) <= abs(touchPx - px1)) 0 else 1
                    }

                    fun update(currentPos: Offset) {
                        val currPx = if (vertical) currentPos.y else currentPos.x
                        val newValue = pxToValue(currPx)
                        val currentList = currentValuesState.value

                        if (range) {
                            val mutableList = currentList.toMutableList()
                            if (activeThumbIndex < mutableList.size) {
                                mutableList[activeThumbIndex] = newValue
                                val sortedList = mutableList.sorted()
                                onValueChangeState.value(sortedList)

                                // 关键：如果滑块发生了交叉（左边的拖到右边了），更新当前控制的索引
                                val newIndex = sortedList.indexOf(newValue)
                                if (newIndex != -1) activeThumbIndex = newIndex
                            }
                        } else {
                            onValueChangeState.value(newValue)
                        }
                    }

                    // 响应按下
                    update(down.position)

                    // 响应拖动
                    drag(down.id) { change ->
                        change.consume()
                        update(change.position)
                    }

                    onValueChangeFinished?.invoke(currentValuesState.value)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // --- 轨道绘制 (Canvas 替代 Box) ---
        // 使用 Canvas 可以精确控制绘制坐标，解决双滑块蓝色位置计算错误的问题
        Canvas(
            modifier = Modifier
                .then(
                    if (vertical) Modifier.width(barHeight).fillMaxHeight()
                    else Modifier.height(barHeight).fillMaxWidth()
                )
        ) {
            val strokeW = if (vertical) size.width else size.height
            val trackLen = if (vertical) size.height else size.width

            // 1. 绘制底色 (Inactive)
            drawLine(
                color = inactiveColor,
                start = if (vertical) Offset(size.width / 2, 0f) else Offset(0f, size.height / 2),
                end = if (vertical) Offset(size.width / 2, size.height) else Offset(size.width, size.height / 2),
                strokeWidth = strokeW,
                cap = StrokeCap.Round
            )

            // 2. 绘制激活色 (Active)
            if (trackLen > 0) {
                // 在 Canvas 内部重新计算像素位置，保证绝对精确
                fun getPx(v: Float): Float {
                    val fraction = (v - min) / (max - min)
                    return fraction * trackLen
                }

                // 确定绘制的起点和终点
                val startVal = if (range) currentValues.minOrNull() ?: min else min
                val endVal = currentValues.maxOrNull() ?: min

                val startPx = getPx(startVal)
                val endPx = getPx(endVal)

                drawLine(
                    color = activeColor,
                    start = if (vertical) Offset(size.width / 2, startPx) else Offset(startPx, size.height / 2),
                    end = if (vertical) Offset(size.width / 2, endPx) else Offset(endPx, size.height / 2),
                    strokeWidth = strokeW,
                    cap = StrokeCap.Round
                )
            }
        }

        // --- 按钮 (Thumb) ---
        // 按钮仍然使用 Box + Offset，因为它们需要承载 Composable 内容
        val pxValues = currentValues.map { valueToPx(it, trackSize) }

        pxValues.forEachIndexed { index, px ->
            val buttonSizePx = with(density) { buttonSize.toPx() }

            val thumbOffset = if (vertical) {
                IntOffset(0, (px - buttonSizePx / 2).toInt())
            } else {
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