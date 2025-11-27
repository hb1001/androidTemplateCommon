package com.template.generated.pickdemo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlin.math.abs

/**
 * 通用滚轮选择器
 * @param items 选项列表
 * @param initialIndex 初始选中索引
 * @param visibleItemsCount 可见的选项数量（建议奇数，如 3, 5）
 * @param onSelectionChanged 选中项改变回调
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> WheelPicker(
    modifier: Modifier = Modifier,
    items: List<T>,
    initialIndex: Int = 0,
    visibleItemsCount: Int = 5,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    itemHeight: Int = 40, // 每个选项的高度(dp)
    onSelectionChanged: (Int) -> Unit
) {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val itemHeightPx = with(LocalDensity.current) { itemHeight.dp.toPx() }

    // 监听滚动停止后的选中项
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index ->
                // 计算中心点的 item 索引
                val firstVisibleOffset = listState.firstVisibleItemScrollOffset
                if (firstVisibleOffset > itemHeightPx / 2) index + 1 else index
            }
            .distinctUntilChanged()
            .collect { index ->
                if (index in items.indices) {
                    onSelectionChanged(index)
                }
            }
    }

    Box(
        modifier = modifier.height((visibleItemsCount * itemHeight).dp),
        contentAlignment = Alignment.Center
    ) {
        // 选中框（中间的高亮两条线）
        HorizontalDivider(
            modifier = Modifier
                .offset(y = (-itemHeight / 2).dp)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
        HorizontalDivider(
            modifier = Modifier
                .offset(y = (itemHeight / 2).dp)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )

        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            contentPadding = PaddingValues(vertical = ((visibleItemsCount - 1) * itemHeight / 2).dp),
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(items.size) { index ->
                Box(
                    modifier = Modifier
                        .height(itemHeight.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    // 计算透明度：离中心越远越透明
                    val opacity by remember {
                        derivedStateOf {
                            val currentItemInfo = listState.layoutInfo.visibleItemsInfo
                                .firstOrNull { it.index == index }
                                ?: return@derivedStateOf 0.3f

                            val itemCenter = currentItemInfo.offset + currentItemInfo.size / 2
                            val viewportCenter = listState.layoutInfo.viewportEndOffset / 2
                            val distance = abs(viewportCenter - itemCenter)

                            // 简单的透明度计算公式
                            val scale = 1f - (distance.toFloat() / (visibleItemsCount * itemHeightPx / 2))
                            scale.coerceIn(0.3f, 1f)
                        }
                    }

                    Text(
                        text = items[index].toString(),
                        style = textStyle.copy(fontSize = if (opacity > 0.8f) 20.sp else 16.sp),
                        modifier = Modifier.alpha(opacity),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}