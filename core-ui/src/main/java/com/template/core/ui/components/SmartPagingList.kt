package com.template.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey

/**
 * 一个智能的、可复用的分页列表组件，集成了下拉刷新和分页加载的UI逻辑。
 *
 * @param T 列表项的数据类型
 * @param lazyPagingItems Paging 3 提供的数据流
 * @param isRefreshing 是否正处于手动下拉刷新状态
 * @param onRefresh 手动下拉刷新的回调。如果为 null，则禁用下拉刷新功能。
 * @param itemContent 如何渲染单个列表项的 Composable
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T : Any> SmartPagingList(
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<T>,
    isRefreshing: Boolean,
    onRefresh: (() -> Unit)?, // 设为可空，以控制是否启用下拉刷新
    itemContent: @Composable (item: T) -> Unit
) {
    // 只有在 onRefresh 回调非空时才启用下拉刷新
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { onRefresh?.invoke() }
    )
    val isPullRefreshEnabled = onRefresh != null

    Box(
        modifier = modifier
            .fillMaxSize()
            .then(if (isPullRefreshEnabled) Modifier.pullRefresh(pullRefreshState) else Modifier)
    ) {
        // 列表内容
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                count = lazyPagingItems.itemCount,
                key = lazyPagingItems.itemKey() // 使用默认的key或提供自定义key
            ) { index ->
                val item = lazyPagingItems[index]
                if (item != null) {
                    itemContent(item)
                }
            }

            // 处理分页加载的状态
            lazyPagingItems.loadState.apply {
                when {
                    // 追加（加载下一页）时的状态
                    append is LoadState.Loading -> {
                        item { LoadingIndicator(modifier = Modifier.fillMaxWidth()) }
                    }
                    append is LoadState.Error -> {
                        item {
                            ErrorRetryItem(
                                message = "Failed to load more items",
                                onRetry = { lazyPagingItems.retry() }
                            )
                        }
                    }
                    // 首次加载或刷新的状态
                    refresh is LoadState.Error && lazyPagingItems.itemCount == 0 -> {
                        item {
                            FullScreenError(
                                message = (refresh as LoadState.Error).error.localizedMessage ?: "Unknown Error",
                                onRetry = { lazyPagingItems.retry() }
                            )
                        }
                    }
                }
            }
        }

        // 处理全屏加载和空状态
        when {
            // 首次加载中
            lazyPagingItems.loadState.refresh is LoadState.Loading && lazyPagingItems.itemCount == 0 -> {
                FullScreenLoading()
            }
            // 加载完成且列表为空
            lazyPagingItems.loadState.refresh is LoadState.NotLoading && lazyPagingItems.itemCount == 0 -> {
                EmptyState(message = "No posts found.")
            }
        }

        // 下拉刷新的指示器（仅在启用时显示）
        if (isPullRefreshEnabled) {
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

// --- 辅助UI组件 ---

@Composable
private fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(16.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun FullScreenLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorRetryItem(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
private fun FullScreenError(message: String, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        ErrorRetryItem(message = message, onRetry = onRetry)
    }
}

@Composable
private fun EmptyState(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}