package com.template.core.ui.vant

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import kotlinx.coroutines.delay

// --- 枚举 ---
enum class VanPopupPosition {
    Top, Bottom, Left, Right, Center
}

enum class VanPopupCloseIconPosition {
    TopLeft, TopRight, BottomLeft, BottomRight
}

// --- 颜色常量 ---
object VanPopupColors {
    val Background = Color.White
    val Overlay = Color(0x99000000)
    val Title = Color(0xFF323233)
    val Description = Color(0xFF969799)
    val CloseIcon = Color(0xFFC8C9CC)
}

// --- 全屏 PositionProvider ---
// 强制 Popup 铺满整个屏幕窗口 (0,0)
internal object FullScreenPositionProvider : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset = IntOffset.Zero
}

// --- 组件实现 ---

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun VanPopup(
    visible: Boolean,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    position: VanPopupPosition = VanPopupPosition.Center,
    round: Boolean = false,
    closeable: Boolean = false,
    closeIcon: (@Composable () -> Unit)? = null,
    closeIconPosition: VanPopupCloseIconPosition = VanPopupCloseIconPosition.TopRight,
    title: String? = null,
    description: String? = null,
    overlay: Boolean = true,
    closeOnClickOverlay: Boolean = true,
    contentWidth: Dp = Dp.Unspecified,
    contentHeight: Dp = Dp.Unspecified,
    safeAreaInsetBottom: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    val duration = 300
    var internalVisible by remember { mutableStateOf(false) }

    // 动态计算顶部偏移量 (即 TopBar 的高度)
    var topOffsetPx by remember { mutableStateOf(0f) }
    val density = LocalDensity.current

    // 使用一个 0 尺寸的 Box 来探测当前组件在 Window 中的 Y 坐标
    // 这个 Y 坐标就是 TopBar 的底部位置
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.dp)
            .onGloballyPositioned { coordinates ->
                // 获取组件在 Window 中的位置
                topOffsetPx = coordinates.positionInWindow().y
            }
    )

    LaunchedEffect(visible) {
        if (visible) {
            internalVisible = true
        } else {
            delay(duration.toLong())
            internalVisible = false
        }
    }

    if (internalVisible) {
        Popup(

        ) {
            // 将像素转换为 Dp
            val topPaddingDp = with(density) { topOffsetPx.toDp() }

            // 根容器：全屏，但顶部留出 padding
            Box(
                modifier = Modifier
                    .fillMaxSize()
//                    .padding(top = topPaddingDp)
                    , // 关键修复：把 TopBar 的位置空出来
                contentAlignment = when (position) {
                    VanPopupPosition.Top -> Alignment.TopCenter
                    VanPopupPosition.Bottom -> Alignment.BottomCenter
                    VanPopupPosition.Left -> Alignment.CenterStart
                    VanPopupPosition.Right -> Alignment.CenterEnd
                    VanPopupPosition.Center -> Alignment.Center
                }
            ) {
                // 1. 遮罩层 (铺满剩余空间)
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(duration)),
                    exit = fadeOut(tween(duration))
                ) {
                    if (overlay) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(VanPopupColors.Overlay)
                                .pointerInput(Unit) {
                                    detectTapGestures(onTap = {
                                        if (closeOnClickOverlay) onClose()
                                    })
                                }
                        )
                    }
                }

                // 2. 动画配置
                val enterTransition = when (position) {
                    VanPopupPosition.Bottom -> slideInVertically(tween(duration)) { it }
                    VanPopupPosition.Top -> slideInVertically(tween(duration)) { -it }
                    VanPopupPosition.Left -> slideInHorizontally(tween(duration)) { -it }
                    VanPopupPosition.Right -> slideInHorizontally(tween(duration)) { it }
                    VanPopupPosition.Center -> fadeIn(tween(duration)) + scaleIn(tween(duration), initialScale = 0.9f)
                }

                val exitTransition = when (position) {
                    VanPopupPosition.Bottom -> slideOutVertically(tween(duration)) { it }
                    VanPopupPosition.Top -> slideOutVertically(tween(duration)) { -it }
                    VanPopupPosition.Left -> slideOutHorizontally(tween(duration)) { -it }
                    VanPopupPosition.Right -> slideOutHorizontally(tween(duration)) { it }
                    VanPopupPosition.Center -> fadeOut(tween(duration)) + scaleOut(tween(duration), targetScale = 0.9f)
                }

                // 3. 内容层
                AnimatedVisibility(
                    visible = visible,
                    enter = enterTransition,
                    exit = exitTransition
                ) {
                    // 圆角
                    val shape: Shape = if (round) {
                        when (position) {
                            VanPopupPosition.Bottom -> RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                            VanPopupPosition.Top -> RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                            VanPopupPosition.Left -> RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                            VanPopupPosition.Right -> RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                            VanPopupPosition.Center -> RoundedCornerShape(16.dp)
                        }
                    } else {
                        androidx.compose.ui.graphics.RectangleShape
                    }

                    // 尺寸 Modifier
                    var contentModifier = modifier
                        .clip(shape)
                        .background(VanPopupColors.Background)
                        .clickable(enabled = false) {} // 拦截点击

                    if (safeAreaInsetBottom && position == VanPopupPosition.Bottom) {
                        contentModifier = contentModifier.windowInsetsPadding(WindowInsets.navigationBars)
                    }

                    // 宽高处理
                    if (position == VanPopupPosition.Left || position == VanPopupPosition.Right) {
                        contentModifier = contentModifier.fillMaxHeight()
                        if (contentWidth != Dp.Unspecified) contentModifier = contentModifier.width(contentWidth)
                    } else if (position == VanPopupPosition.Top || position == VanPopupPosition.Bottom) {
                        contentModifier = contentModifier.fillMaxWidth()
                        if (contentHeight != Dp.Unspecified) contentModifier = contentModifier.height(contentHeight)
                    } else {
                        // Center
                        if (contentWidth != Dp.Unspecified) contentModifier = contentModifier.width(contentWidth)
                        if (contentHeight != Dp.Unspecified) contentModifier = contentModifier.height(contentHeight)
                    }

                    Box(modifier = contentModifier) {
                        Column {
                            // Title & Description
                            if (title != null || description != null) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = if (title != null) 26.dp else 16.dp,
                                            bottom = 16.dp,
                                            start = 16.dp,
                                            end = 16.dp
                                        ),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    if (title != null) {
                                        Text(
                                            text = title,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = VanPopupColors.Title
                                        )
                                    }
                                    if (description != null) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = description,
                                            fontSize = 14.sp,
                                            color = VanPopupColors.Description,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                            content()
                        }

                        // Close Icon
                        if (closeable) {
                            val iconAlignment = when (closeIconPosition) {
                                VanPopupCloseIconPosition.TopLeft -> Alignment.TopStart
                                VanPopupCloseIconPosition.TopRight -> Alignment.TopEnd
                                VanPopupCloseIconPosition.BottomLeft -> Alignment.BottomStart
                                VanPopupCloseIconPosition.BottomRight -> Alignment.BottomEnd
                            }
                            Box(
                                modifier = Modifier
                                    .align(iconAlignment)
                                    .padding(16.dp)
                                    .clickable(onClick = onClose)
                            ) {
                                if (closeIcon != null) {
                                    closeIcon()
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close",
                                        tint = VanPopupColors.CloseIcon,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}