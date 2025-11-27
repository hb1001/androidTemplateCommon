import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// =================================================================
// Part A: 基础绘制核心 (只负责画，不负责动)
// =================================================================

/**
 * A-1. 淘宝流光风格 - 绘制核心
 * @param rotation: 当前旋转角度
 */
@Composable
fun FashionSpinner(
    rotation: Float,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    strokeWidth: Dp = 3.dp,
    colors: List<Color> = listOf(
        Color(0xFFFF9800), // 浅橙
        Color(0xFFFF5722), // 深橙
        Color(0xFFFF1744)  // 红
    )
) {
    Box(modifier = modifier.size(size)) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val gradientColors = listOf(Color.Transparent) + colors
            val strokePx = strokeWidth.toPx()

            rotate(rotation) {
                drawArc(
                    brush = Brush.sweepGradient(gradientColors),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = strokePx, cap = StrokeCap.Round)
                )
            }
        }
    }
}

/**
 * A-2. 科技灵动风格 - 绘制核心
 * @param rotation: 当前整体旋转角度
 * @param arcAngle: 圆弧的张开角度 (用于模拟呼吸效果)
 */
@Composable
fun TechSpinner(
    rotation: Float,
    arcAngle: Float,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    color: Color = Color(0xFF2196F3),
    strokeWidth: Dp = 3.dp
) {
    Box(modifier = modifier.size(size)) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val strokePx = strokeWidth.toPx()
            val diameter = this.size.minDimension

            rotate(rotation) {
                // 圆弧 1
                drawArc(
                    color = color,
                    startAngle = 0f,
                    sweepAngle = arcAngle,
                    useCenter = false,
                    style = Stroke(width = strokePx, cap = StrokeCap.Round),
                    size = Size(diameter, diameter)
                )
                // 圆弧 2 (对称位置)
                drawArc(
                    color = color.copy(alpha = 0.8f),
                    startAngle = 180f,
                    sweepAngle = arcAngle,
                    useCenter = false,
                    style = Stroke(width = strokePx, cap = StrokeCap.Round),
                    size = Size(diameter, diameter)
                )
            }
        }
    }
}

// =================================================================
// Part B: 1 & 2 通用加载组件 (全自动动画)
// =================================================================

/**
 * 1. 淘宝风通用 Loading (自动旋转)
 */
@Composable
fun FashionLoading(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp
) {
    val transition = rememberInfiniteTransition(label = "fashion_loading")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1000, easing = LinearEasing)),
        label = "rotation"
    )

    FashionSpinner(
        rotation = rotation,
        modifier = modifier,
        size = size,
        strokeWidth = 4.dp
    )
}

/**
 * 2. 科技风通用 Loading (自动旋转 + 呼吸)
 */
@Composable
fun TechLoading(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    color: Color = Color(0xFF2196F3)
) {
    val transition = rememberInfiniteTransition(label = "tech_loading")

    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing)),
        label = "rotation"
    )

    val arcAngle by transition.animateFloat(
        initialValue = 10f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "arc"
    )

    TechSpinner(
        rotation = rotation,
        arcAngle = arcAngle,
        modifier = modifier,
        size = size,
        color = color,
        strokeWidth = 4.dp
    )
}

// =================================================================
// Part C: 3 & 4 下拉刷新组件 (手势控制 + 自动动画)
// =================================================================

/**
 * 3. 淘宝风下拉刷新 (Fashion PullRefresh)
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FashionPullRefreshIndicator(
    refreshing: Boolean,
    state: PullRefreshState,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White
) {
    // --- 动画状态计算 ---
    val progress = state.progress
    val visible = progress > 0.05f || refreshing

    // 进出场缩放
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    // 自动旋转动画 (刷新时用)
    val infiniteTransition = rememberInfiniteTransition(label = "spin")
    val spinnerRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1000, easing = LinearEasing)),
        label = "spin_auto"
    )

    // 最终角度：刷新时自动转，下拉时跟手转
    val finalRotation = if (refreshing) spinnerRotation else (progress * 360f)

    // --- 布局与位置 ---
    BasePullRefreshContainer(
        refreshing = refreshing,
        state = state,
        modifier = modifier,
        scale = scale,
        backgroundColor = backgroundColor
    ) {
        FashionSpinner(
            rotation = finalRotation,
            size = 20.dp
        )
    }
}

/**
 * 4. 科技风下拉刷新 (Tech PullRefresh)
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TechPullRefreshIndicator(
    refreshing: Boolean,
    state: PullRefreshState,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    iconColor: Color = Color(0xFF2196F3)
) {
    val progress = state.progress
    val visible = progress > 0.05f || refreshing

    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "tech_spin")

    // 1. 旋转动画
    val autoRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing)),
        label = "rot"
    )

    // 2. 呼吸动画 (只在刷新时呼吸)
    val autoArc by infiniteTransition.animateFloat(
        initialValue = 10f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "arc"
    )

    // 逻辑：
    // - 刷新时：全自动旋转 + 呼吸
    // - 下拉时：跟手旋转，但是圆弧角度固定(比如90度)，不呼吸，否则看起来太乱
    val finalRotation = if (refreshing) autoRotation else (progress * 360f)
    val finalArcAngle = if (refreshing) autoArc else 90f

    BasePullRefreshContainer(
        refreshing = refreshing,
        state = state,
        modifier = modifier,
        scale = scale,
        backgroundColor = backgroundColor
    ) {
        TechSpinner(
            rotation = finalRotation,
            arcAngle = finalArcAngle,
            size = 20.dp,
            color = iconColor
        )
    }
}

// --- 辅助方法：统一的下拉刷新容器 (处理位置偏移和阴影背景) ---
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BasePullRefreshContainer(
    refreshing: Boolean,
    state: PullRefreshState,
    modifier: Modifier,
    scale: Float,
    backgroundColor: Color,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val indicatorHeight = 40.dp
    val triggerDistance = 80.dp

    // 模拟原生偏移逻辑
    val offsetData = with(density) {
        val maxDrag = triggerDistance * 2
        val y = if (refreshing) {
            triggerDistance
        } else {
            (triggerDistance * state.progress).coerceAtMost(maxDrag)
        }
        y
    }

    Box(
        modifier = modifier.graphicsLayer {
            translationY = offsetData.toPx() - (indicatorHeight.toPx() / 2)
            scaleX = scale
            scaleY = scale
            alpha = if (scale < 0.2f) 0f else 1f
        }
    ) {
        Surface(
            modifier = Modifier.size(indicatorHeight),
            shape = CircleShape,
            shadowElevation = 6.dp,
            color = backgroundColor
        ) {
            Box(contentAlignment = Alignment.Center) {
                content()
            }
        }
    }
}

// =================================================================
// 预览
// =================================================================
@Preview(showBackground = true, heightDp = 600)
@Composable
fun AllComponentsPreview() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        Text("1. 淘宝风 Loading (自动)", style = MaterialTheme.typography.titleMedium)
        FashionLoading()

        Text("2. 科技风 Loading (自动)", style = MaterialTheme.typography.titleMedium)
        TechLoading()

        // 注意：下拉刷新组件通常在 Box 中配合 PullRefreshState 使用
        // 这里仅展示其静态外观（缩放为1，无位移）

        Text("3. 淘宝风 PullRefresh (外观示例)", style = MaterialTheme.typography.titleMedium)
        Surface(shadowElevation = 6.dp, shape = CircleShape) {
            Box(Modifier.padding(10.dp)) {
                FashionSpinner(rotation = 45f, size = 20.dp)
            }
        }

        Text("4. 科技风 PullRefresh (外观示例)", style = MaterialTheme.typography.titleMedium)
        Surface(shadowElevation = 6.dp, shape = CircleShape) {
            Box(Modifier.padding(10.dp)) {
                TechSpinner(rotation = 45f, arcAngle = 90f, size = 20.dp)
            }
        }
    }
}