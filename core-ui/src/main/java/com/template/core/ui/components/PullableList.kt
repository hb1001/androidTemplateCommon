package com.template.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.template.core.ui.uimodel.UiState

// 下拉刷新的list
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun <T> PullRefreshOnlyList(
    uiState: UiState<List<T>>,
    refresh: () -> Unit,
    content: @Composable (data: T) -> Unit,
    key: (T) -> String
) {

    // 对于非分页场景，我们需要一个不同的 "Smart" 组件或直接实现
    // 这里为了简单，我们直接用你之前的 PostList 加上 pullRefresh
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = { refresh() }
    )
    Box(
        modifier = Modifier
            .pullRefresh(pullRefreshState)
    ) {
        if (uiState.data.isNotEmpty()) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.data, key = { key(it) }) { post ->
                    content(post)
                }
            }
        } else if (!uiState.isLoading && uiState.error == null) {
            // 空状态
        }
        // ... 处理错误和加载状态 ...
        PullRefreshIndicator(
            refreshing = uiState.isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }

}