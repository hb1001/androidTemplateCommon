package com.template.core.ui.components

import FashionPullRefreshIndicator
import TechPullRefreshIndicator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.template.core.ui.uimodel.UiState
import timber.log.Timber

// 下拉刷新的list
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun <T> PullRefreshOnlyList(
    uiState: UiState<List<T>>,
    refresh: () -> Unit,
    content: @Composable (data: T) -> Unit,
    key: (T) -> String
) {
    // 进入后，自动刷新一次
    LaunchedEffect( Unit) {
        Timber.d("自动刷新列表") // 每次返回都输出日志了
        refresh()
    }

    // 对于非分页场景，我们需要一个不同的 "Smart" 组件或直接实现
    // 这里为了简单，我们直接用你之前的 PostList 加上 pullRefresh
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = { refresh() }
    )
    Box(
        modifier = Modifier
            .pullRefresh(pullRefreshState)
            .fillMaxWidth()
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
            // 空状态,占位
//            Box(modifier = Modifier.fillMaxHeight())
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()), // <- 关键：提供 nested-scroll 能力
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("暂无数据，下拉刷新")
            }
        }
        // ... 处理错误和加载状态 ...
        TechPullRefreshIndicator(
            refreshing = uiState.isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }

}