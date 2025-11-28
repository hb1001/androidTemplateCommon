package com.template.core.ui.vant

import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// --- 枚举 ---
enum class VanSwipeCellSide {
    Left, Center, Right
}

enum class VanSwipeCellPosition {
    Left, Right, Cell, Outside
}

// --- State 管理 ---

@OptIn(ExperimentalFoundationApi::class)
class VanSwipeCellState(
    internal val anchoredDraggableState: AnchoredDraggableState<VanSwipeCellSide>
) {
    /**
     * 打开指定侧边栏
     */
    suspend fun open(side: VanSwipeCellSide) {
        if (side != VanSwipeCellSide.Center) {
            anchoredDraggableState.animateTo(side)
        }
    }

    /**
     * 关闭侧边栏
     */
    suspend fun close() {
        anchoredDraggableState.animateTo(VanSwipeCellSide.Center)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberVanSwipeCellState(): VanSwipeCellState {
    val density = LocalDensity.current
    val anchoredDraggableState = remember {
        AnchoredDraggableState(
            initialValue = VanSwipeCellSide.Center,
            positionalThreshold = { distance: Float -> distance * 0.5f }, // 滑动超过 50% 触发切换
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            animationSpec = TweenSpec(durationMillis = 300),
            anchors = DraggableAnchors {
                // 初始锚点，后续会在 Layout 中动态更新
                VanSwipeCellSide.Center at 0f
            }
        )
    }
    return remember(anchoredDraggableState) { VanSwipeCellState(anchoredDraggableState) }
}

// --- 组件实现 ---

/**
 * VanSwipeCell - 滑动单元格
 *
 * @param leftAction 左侧滑动区域内容
 * @param rightAction 右侧滑动区域内容
 * @param onOpen 打开时触发
 * @param onClose 关闭时触发
 * @param onClick 点击时触发 (区分 position)
 * @param beforeClose 关闭/动作前的回调。返回 false 或抛出异常阻止关闭；返回 true 允许关闭。支持 suspend (异步)。
 * @param disabled 是否禁用滑动
 * @param state 外部控制状态
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VanSwipeCell(
    modifier: Modifier = Modifier,
    leftAction: (@Composable () -> Unit)? = null,
    rightAction: (@Composable () -> Unit)? = null,
    onOpen: ((VanSwipeCellPosition) -> Unit)? = null,
    onClose: ((VanSwipeCellPosition) -> Unit)? = null,
    onClick: ((VanSwipeCellPosition) -> Unit)? = null,
    beforeClose: (suspend (VanSwipeCellPosition) -> Boolean)? = null,
    disabled: Boolean = false,
    state: VanSwipeCellState = rememberVanSwipeCellState(),
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    // 测量左右菜单的宽度
    var leftWidth by remember { mutableFloatStateOf(0f) }
    var rightWidth by remember { mutableFloatStateOf(0f) }

    // 动态更新 Anchors
    // 当左右 Action 的宽度计算完成后，更新可拖拽的锚点
    SideEffect {
        val newAnchors = DraggableAnchors {
            VanSwipeCellSide.Center at 0f
            if (leftWidth > 0) VanSwipeCellSide.Left at leftWidth
            if (rightWidth > 0) VanSwipeCellSide.Right at -rightWidth
        }
        // 只有当锚点发生实质变化时才更新，避免重组循环
        if (state.anchoredDraggableState.anchors != newAnchors) {
            state.anchoredDraggableState.updateAnchors(newAnchors)
        }
    }

    // 监听状态变化触发 onOpen/onClose 回调
    LaunchedEffect(state.anchoredDraggableState.currentValue) {
        when (state.anchoredDraggableState.currentValue) {
            VanSwipeCellSide.Left -> onOpen?.invoke(VanSwipeCellPosition.Left)
            VanSwipeCellSide.Right -> onOpen?.invoke(VanSwipeCellPosition.Right)
            VanSwipeCellSide.Center -> onClose?.invoke(VanSwipeCellPosition.Cell)
        }
    }

    // 统一处理点击和关闭逻辑
    fun handleClick(position: VanSwipeCellPosition) {
        scope.launch {
            // 1. 触发点击回调
            onClick?.invoke(position)

            // 2. 判断是否需要关闭
            // 如果点击的是侧边栏(Action)，通常意味着执行了操作，默认行为是关闭
            // 如果点击的是内容(Cell)且当前是打开状态，默认行为也是关闭
            // 如果点击的是内容(Cell)且当前是关闭状态，不做 SwipeCell层面的处理(除非 beforeClose 强制拦截?) -> Vant 逻辑是点 Cell 不触发 beforeClose，除非 Cell 本身有关闭意图

            val isSideAction = position == VanSwipeCellPosition.Left || position == VanSwipeCellPosition.Right
            val isOpen = state.anchoredDraggableState.currentValue != VanSwipeCellSide.Center

            if (isSideAction || (position == VanSwipeCellPosition.Cell && isOpen)) {
                // 执行 beforeClose 拦截逻辑
                val shouldClose = beforeClose?.invoke(position) ?: true

                if (shouldClose) {
                    state.close()
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min) // 关键：让子元素高度一致 (Action 高度跟随 Cell)
            .clipToBounds()
    ) {
        val currentOffset = if (state.anchoredDraggableState.offset.isNaN()) 0f else state.anchoredDraggableState.offset

        // --- 左侧菜单 (底层) ---
        if (leftAction != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .onSizeChanged { leftWidth = it.width.toFloat() }
                    // 视觉优化：让菜单紧贴 Cell 边缘滑动 (Parallax/Follow 效果)
                    // 当 offset = 0, x = -width (隐藏)
                    // 当 offset = width, x = 0 (显示)
                    .offset { IntOffset((currentOffset - leftWidth).roundToInt(), 0) }
                    .fillMaxHeight() // 高度撑满
                    .zIndex(0f)
                    .clickable { handleClick(VanSwipeCellPosition.Left) }
            ) {
                leftAction()
            }
        }

        // --- 右侧菜单 (底层) ---
        if (rightAction != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .onSizeChanged { rightWidth = it.width.toFloat() }
                    // 视觉优化：让菜单紧贴 Cell 边缘滑动
                    // 初始位置在最右边 (align End)，即 x=0 relative to end
                    // 我们需要它初始平移 width (藏在右边屏幕外)
                    // 当 offset = 0, x = rightWidth
                    // 当 offset = -rightWidth, x = 0
                    .offset { IntOffset((rightWidth + currentOffset).roundToInt(), 0) }
                    .fillMaxHeight() // 高度撑满
                    .zIndex(0f)
                    .clickable { handleClick(VanSwipeCellPosition.Right) }
            ) {
                rightAction()
            }
        }

        // --- 内容层 (上层) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(currentOffset.roundToInt(), 0) }
                .anchoredDraggable(
                    state = state.anchoredDraggableState,
                    orientation = Orientation.Horizontal,
                    enabled = !disabled
                )
                .zIndex(1f)
                .background(Color.White) // 必须有背景，否则透视到底下 Action
                .clickable { handleClick(VanSwipeCellPosition.Cell) }
        ) {
            content()
        }
    }
}