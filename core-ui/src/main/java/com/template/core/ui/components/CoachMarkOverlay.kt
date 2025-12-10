package com.template.core.ui.components

/**
 *
 * 它的核心能力是：
 * 接收目标 ID 和提示内容。
 * 使用 Box 或 SubcomposeLayout，将其置于所有内容之上。
 * 处理点击穿透： 允许用户点击非高亮区域的蒙版。
 * 计算高亮区域： 通过目标 Composable 暴露的位置信息，在蒙版上使用 Canvas 绘制一个“挖洞”效果（在指定区域挖空透明）。
 */

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.positionInRoot

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

// 定义引导的形状
enum class CoachMarkShape {
    Circle, Rectangle
}

// 定义每一步引导的数据
data class CoachMarkStep(
    val id: String,
    val title: String,
    val description: String,
    val shape: CoachMarkShape = CoachMarkShape.Circle
)

// 控制器：管理状态和坐标
class CoachMarkController {
    // 存储各个组件的布局信息：Key = ID, Value = 坐标区域
    private val targets = mutableStateMapOf<String, Rect>()

    // 当前正在展示的引导步骤
    var currentStep by mutableStateOf<CoachMarkStep?>(null)
        private set

    // 更新目标组件的位置
    fun updateTarget(id: String, coordinates: LayoutCoordinates) {
        if (coordinates.isAttached) {
            val position = coordinates.positionInRoot()
            val size = coordinates.size
            targets[id] = Rect(
                left = position.x,
                top = position.y,
                right = position.x + size.width,
                bottom = position.y + size.height
            )
        }
    }

    // 显示指定步骤
    fun show(step: CoachMarkStep) {
        currentStep = step
    }

    // 隐藏/结束
    fun dismiss() {
        currentStep = null
    }

    // 获取当前步骤对应的目标区域
    fun getCurrentTargetRect(): Rect? {
        return currentStep?.let { step -> targets[step.id] }
    }
}

// CompositionLocal 方便全局访问
val LocalCoachMarkController = compositionLocalOf { CoachMarkController() }


fun Modifier.coachMarkTarget(
    id: String,
    controller: CoachMarkController
): Modifier = this.onGloballyPositioned { coordinates ->
    controller.updateTarget(id, coordinates)
}
@Composable
fun CoachMarkOverlay(
    controller: CoachMarkController,
    steps: List<CoachMarkStep> = emptyList(),
    overlayColor: Color = Color.Black.copy(alpha = 0.7f),
    content: @Composable BoxScope.() -> Unit
) {
    val onSkip = {
        // 简单的逻辑：点击空白处切换下一步，或者关闭
        val index = steps.indexOfFirst { it == controller.currentStep }
        if(index < steps.size - 1 && index >= 0){
            controller.show(steps[index + 1])
        }else{
            controller.dismiss()
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        content()

        val currentStep = controller.currentStep
        val targetRectRoot = controller.getCurrentTargetRect() // 这是基于 Root 的绝对坐标

        // 新增状态：记录蒙版自身在屏幕上的偏移量
        var overlayOffset by remember { mutableStateOf(Offset.Zero) }

        if (currentStep != null && targetRectRoot != null) {

            // 计算相对坐标：目标绝对坐标 - 蒙版绝对坐标
            // translate 函数可以将 Rect 的 x,y 进行平移
            val localTargetRect = targetRectRoot.translate(
                -overlayOffset.x,
                -overlayOffset.y
            )

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(10f)
                    // 关键点：获取蒙版自身的全局位置
                    .onGloballyPositioned { coordinates ->
                        overlayOffset = coordinates.positionInRoot()
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onSkip
                    )
                    .graphicsLayer { alpha = 0.99f }
            ) {
                drawContext.canvas.saveLayer(
                    Rect(0f, 0f, size.width, size.height),
                    Paint()
                )

                drawRect(color = overlayColor)

                // 使用转换后的【相对坐标】进行绘制
                val padding = 8.dp.toPx()
                val highlightRect = localTargetRect.inflate(padding)

                when (currentStep.shape) {
                    CoachMarkShape.Circle -> {
                        val radius = maxOf(highlightRect.width, highlightRect.height) / 2
                        drawCircle(
                            color = Color.Transparent,
                            center = highlightRect.center, // 这里的 center 已经是相对坐标了
                            radius = radius,
                            blendMode = BlendMode.Clear
                        )
                    }
                    CoachMarkShape.Rectangle -> {
                        drawRoundRect(
                            color = Color.Transparent,
                            topLeft = highlightRect.topLeft, // 这里的 topLeft 已经是相对坐标了
                            size = highlightRect.size,
                            cornerRadius = CornerRadius(12.dp.toPx()),
                            blendMode = BlendMode.Clear
                        )
                    }
                }
                drawContext.canvas.restore()
            }

            // Tooltip 也是放在这个 Box 里的，所以也需要使用相对坐标
            Tooltip(
                targetRect = localTargetRect, // 传入修正后的坐标
                step = currentStep,
                modifier = Modifier.zIndex(11f)
            )
        }
    }
}

// 辅助函数：Rect 放大
fun Rect.inflate(delta: Float): Rect {
    return Rect(
        left = left - delta,
        top = top - delta,
        right = right + delta,
        bottom = bottom + delta
    )
}
@Composable
fun Tooltip(
    targetRect: Rect,
    step: CoachMarkStep,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    // 简单的布局逻辑：如果目标在屏幕下半部分，提示框显示在上方，反之亦然
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val screenHeight = constraints.maxHeight
        val isTargetInTopHalf = targetRect.center.y < screenHeight / 2

        val tooltipY = if (isTargetInTopHalf) {
            // 目标在上方，Tooltip 显示在目标底部 + 偏移
            with(density) { (targetRect.bottom + 16.dp.toPx()).toDp() }
        } else {
            // 目标在下方，Tooltip 显示在目标顶部 - 偏移（大概估算一个高度，或者用 bottom padding）
            with(density) { (targetRect.top - 120.dp.toPx()).toDp() } // 简单处理，实际上可以用 alignment
        }

        // 这里使用 Column + Spacer 的方式定位，或者使用 absoluteOffset
        Column(
            modifier = Modifier
                .offset(
                    y = if(isTargetInTopHalf)
                        with(density) { (targetRect.bottom + 16).toDp() }
                    else
                        with(density) { (targetRect.top - 100).toDp() } // 这里的计算可以更精细
                )
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
        ) {
            // 提示卡片内容
            Box(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = step.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = step.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}