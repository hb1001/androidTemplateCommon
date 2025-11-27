package com.template.core.ui.vant

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

// --- 颜色常量 ---
private object VanSwipeColors {
    val IndicatorActive = Color(0xFF1989FA)
    val IndicatorInactive = Color(0xFFEBEDF0)
}

/**
 * VanSwipe - 轮播组件 (修复版)
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VanSwipe(
    itemCount: Int,
    modifier: Modifier = Modifier,
    autoplay: Int = 0,
    duration: Int = 500,
    initialSwipe: Int = 0,
    loop: Boolean = true,
    showIndicators: Boolean = true,
    vertical: Boolean = false,
    indicatorColor: Color = VanSwipeColors.IndicatorActive,
    width: Dp? = null,
    height: Dp? = null,
    onChange: ((Int) -> Unit)? = null,
    indicator: (@Composable BoxScope.(active: Int, total: Int) -> Unit)? = null,
    content: @Composable (Int) -> Unit
) {
    if (itemCount <= 0) return

    // 【关键修复 1】不要使用 Int.MAX_VALUE。
    // 使用一个足够大的倍数即可。比如 400 倍，足够用户滑很久了。
    // 如果是 loop=false，则直接用 itemCount
    val multiplier = if (loop) 400 else 1
    val pageCount = itemCount * multiplier

    // 【关键修复 2】计算起始位置，确保它位于中间且对应 item index 为 0
    // 这样用户既可以向左滑也可以向右滑
    val startIndex = if (loop) {
        val center = pageCount / 2
        // 调整 center 让它正好对应 realIndex == 0 的位置，再加上 initialSwipe
        val remainder = center % itemCount
        center - remainder + initialSwipe
    } else {
        initialSwipe
    }

    val pagerState = rememberPagerState(initialPage = startIndex) { pageCount }

    // 监听页码变化，触发 onChange
    // 使用 snapshotFlow 性能更好
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            val realIndex = page % itemCount
            onChange?.invoke(realIndex)
        }
    }

    // 【关键修复 3】自动轮播逻辑优化
    // 使用 while(true) 循环，而不是依赖 key(currentPage) 重启 Effect，防止频繁重组
    if (autoplay > 0) {
        LaunchedEffect(autoplay, loop) {
            while (isActive) {
                delay(autoplay.toLong())
                // 如果用户正在拖拽，或者如果不循环且已经到了最后一页，则不滚动
                if (!pagerState.isScrollInProgress) {
                    if (loop || pagerState.currentPage < pageCount - 1) {
                        try {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } catch (e: Exception) {
                            // 忽略动画被中断的情况
                        }
                    }
                }
            }
        }
    }

    Box(modifier = modifier) {
        // --- 核心 Pager ---
        if (vertical) {
            VerticalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                pageSize = if (height != null && !loop) PageSize.Fixed(height) else PageSize.Fill,
                pageContent = { pageIndex ->
                    val realIndex = pageIndex % itemCount
                    content(realIndex)
                }
            )
        } else {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                pageSize = if (width != null && !loop) PageSize.Fixed(width) else PageSize.Fill,
                pageContent = { pageIndex ->
                    val realIndex = pageIndex % itemCount
                    content(realIndex)
                }
            )
        }

        // --- 指示器 ---
        if (indicator != null) {
            // 自定义指示器
            val realIndex = pagerState.currentPage % itemCount
            indicator(realIndex, itemCount)
        } else if (showIndicators) {
            // 默认指示器
            val realIndex = pagerState.currentPage % itemCount

            if (vertical) {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    repeat(itemCount) { index ->
                        VanSwipeIndicator(
                            active = index == realIndex,
                            activeColor = indicatorColor
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    repeat(itemCount) { index ->
                        VanSwipeIndicator(
                            active = index == realIndex,
                            activeColor = indicatorColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VanSwipeIndicator(
    active: Boolean,
    activeColor: Color
) {
    val color = if (active) activeColor else VanSwipeColors.IndicatorInactive
    val opacity = if (active) 1f else 0.3f

    Box(
        modifier = Modifier
            .size(6.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = opacity))
    )
}

@Composable
fun VanSwipeItem(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}