package com.template.core.ui.vant

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// --- 枚举 ---
enum class VanLoadingType {
    Circular, Spinner, Ball
}

// --- 颜色常量 ---
object VanLoadingColors {
    val Default = Color(0xFFC8C9CC) // Gray-5
    val Text = Color(0xFF969799) // Gray-6
}

// --- 组件实现 ---

/**
 * VanLoading - 加载组件
 *
 * @param type 类型: Circular, Spinner, Ball
 * @param color 图标颜色
 * @param size 图标大小
 * @param textSize 文字大小
 * @param textColor 文字颜色
 * @param vertical 是否垂直排列
 * @param content 加载文案 (Slot)
 */
@Composable
fun VanLoading(
    modifier: Modifier = Modifier,
    type: VanLoadingType = VanLoadingType.Circular,
    color: Color = VanLoadingColors.Default,
    size: Dp = 24.dp, // Vant 默认约 30px -> 24dp
    textSize: Dp = 14.dp,
    textColor: Color = VanLoadingColors.Text,
    vertical: Boolean = false,
    content: (@Composable () -> Unit)? = null
) {
    val layoutDirection = if (vertical) ColumnLayout(Alignment.CenterHorizontally) else RowLayout(Alignment.CenterVertically)

    // 使用 Layout 手动布局或者简单的 Row/Column
    if (vertical) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoadingIcon(type, color, size)
            if (content != null) {
                Spacer(modifier = Modifier.height(8.dp))
                LoadingText(content, textSize, textColor)
            }
        }
    } else {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            LoadingIcon(type, color, size)
            if (content != null) {
                Spacer(modifier = Modifier.width(8.dp))
                LoadingText(content, textSize, textColor)
            }
        }
    }
}

@Composable
private fun LoadingText(
    content: @Composable () -> Unit,
    textSize: Dp,
    textColor: Color
) {
    androidx.compose.material3.ProvideTextStyle(
        value = androidx.compose.ui.text.TextStyle(
            color = textColor,
            fontSize = textSize.value.sp
        )
    ) {
        content()
    }
}

@Composable
private fun LoadingIcon(
    type: VanLoadingType,
    color: Color,
    size: Dp
) {
    when (type) {
        VanLoadingType.Circular -> {
            CircularProgressIndicator(
                modifier = Modifier.size(size),
                color = color,
                strokeWidth = 2.dp, // Vant 线条较细
                strokeCap = StrokeCap.Round
            )
        }
        VanLoadingType.Spinner -> {
            SpinnerLoading(color = color, size = size)
        }
        VanLoadingType.Ball -> {
            BallLoading(color = color, size = size)
        }
    }
}

// --- Spinner 动画实现 (菊花图) ---
@Composable
private fun SpinnerLoading(
    color: Color,
    size: Dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "Spinner")
    // 12个叶片，每个叶片透明度循环
    // 简单实现：整体旋转是不够的，Spinner 是每个叶片依次变暗
    // 这里的实现方式：绘制12个叶片，每个叶片的透明度由 time 决定

    // 为了性能，我们可以只做一个旋转动画，让看起来像是在闪烁，或者做真实的 opacity 动画
    // Vant 的 Spinner 是 step 动画。

    // 使用旋转动画模拟最简单且性能最好
    // 但为了还原 Vant 的 "花瓣依次变亮" 效果：
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing), // 0.8s 一圈
            repeatMode = RepeatMode.Restart
        ),
        label = "SpinnerAngle"
    )

    // 我们绘制静态的12个花瓣，然后旋转整个 Canvas，视觉效果就是跑马灯
    // 但是 Vant 的 Spinner 是离散的跳变吗？不是，是平滑旋转 + 渐变？
    // Vant CSS: steps(12) 旋转。

    // 修正：让动画是离散的 steps(12)
    val stepAngle = (angle / 30).toInt() * 30f // 360 / 12 = 30度一跳

    Canvas(modifier = Modifier.size(size)) {
        val center = Offset(drawContext.size.width / 2, drawContext.size.height / 2)
        val radius = drawContext.size.width / 2
        val itemWidth = radius * 0.15f
        val itemHeight = radius * 0.5f // 长度

        rotate(angle) { // 这里如果是平滑旋转，效果如原生 ProgressBar；如果是 stepAngle，效果如 iOS菊花
            // Vant 实际上是平滑旋转但每个花瓣透明度不同？
            // 实际上最简单的还原：绘制12个不同透明度的花瓣，然后整体旋转
            for (i in 0 until 12) {
                rotate(degrees = i * 30f) {
                    // 透明度从 1 到 0.x
                    // i=0 最亮，i=11 最暗
                    val alpha = 1f - (i / 12f)
                    drawRoundRect(
                        color = color,
                        topLeft = Offset(center.x - itemWidth / 2, center.y - radius), // 顶部
                        size = Size(itemWidth, itemHeight * 0.6f), // 花瓣长度
                        cornerRadius = CornerRadius(itemWidth / 2),
                        alpha = alpha
                    )
                }
            }
        }
    }
}

// --- Ball 动画实现 (三点跳动) ---
@Composable
private fun BallLoading(
    color: Color,
    size: Dp
) {
    // Ball Pulse: 三个球，缩放动画
    val infiniteTransition = rememberInfiniteTransition(label = "Ball")
    val duration = 750

    val scale1 by infiniteTransition.animateFloat(
        initialValue = 0.5f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = CubicBezierEasing(0.2f, 0.68f, 0.18f, 1.08f)),
            repeatMode = RepeatMode.Reverse
        ), label = "Ball1"
    )
    // 错开时间比较麻烦，不如直接手写 offsets
    // 简单模拟：三个球排成一行

    Row(
        modifier = Modifier.size(size), // 注意：这里的 size 对于 Ball 模式可能指宽度？React Vant 文档 ball 模式是三个点
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val ballSize = size / 4
        // 实际上 BallPulse 通常是三个独立的动画
        BallDot(color, ballSize, 0)
        BallDot(color, ballSize, 150)
        BallDot(color, ballSize, 300)
    }
}

@Composable
private fun BallDot(color: Color, size: Dp, delay: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "BallDot")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = delay, easing = LinearEasing), // 模拟脉冲
            repeatMode = RepeatMode.Reverse
        ), label = "Scale"
    )

    Box(
        modifier = Modifier
            .size(size)
            .scale(scale)
            .background(color, androidx.compose.foundation.shape.CircleShape)
    )
}

// 辅助类用于逻辑判断，不直接使用
private object RowLayout { operator fun invoke(a: Alignment.Vertical) = a }
private object ColumnLayout { operator fun invoke(a: Alignment.Horizontal) = a }