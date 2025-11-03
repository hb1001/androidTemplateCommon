package com.template.feature.home.homelist


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.template.core.model.Post
import com.template.core.ui.components.SmartPagingList
import com.template.feature.home.HomeViewModel

// 场景1: 下拉刷新 + 分页加载
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagingAndRefreshScreen(viewModel: PagingListViewModel = hiltViewModel()) {
    val lazyPagingItems = viewModel.postsPagingFlow.collectAsLazyPagingItems()
    // 我们需要一个独立的状态来追踪“手动刷新”的动作
    var isRefreshing by rememberSaveable { mutableStateOf(false) }

    // 当 Paging 库的刷新状态变化时，同步我们的 isRefreshing 状态
    isRefreshing = lazyPagingItems.loadState.refresh is LoadState.Loading

    Scaffold(
        topBar = { TopAppBar(title = { Text("Paging + Pull Refresh") }) }
    ) { paddingValues ->
        SmartPagingList<Post>(
            modifier = Modifier.padding(paddingValues),
            lazyPagingItems = lazyPagingItems,
            isRefreshing = isRefreshing,
            onRefresh = { lazyPagingItems.refresh() }, // 启用下拉刷新
            itemContent = { post -> PostItem(post) }
        )
    }
}

// 场景2: 仅分页加载
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagingOnlyScreen(viewModel: PagingListViewModel = hiltViewModel()) {
    val lazyPagingItems = viewModel.postsPagingFlow.collectAsLazyPagingItems()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Paging Only") }) }
    ) { paddingValues ->
        SmartPagingList<Post>(
            modifier = Modifier.padding(paddingValues),
            lazyPagingItems = lazyPagingItems,
            isRefreshing = false, // 因为禁用了，所以永远是 false
            onRefresh = null,     // 传入 null 来禁用下拉刷新
            itemContent = { post -> PostItem(post) }
        )
    }
}

// 场景3: 仅下拉刷新 (全量数据)
// 注意：这个场景不适合用 PagingSource，我们复用之前的 HomeViewModel 逻辑
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun PullRefreshOnlyScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Pull Refresh Only") }) }
    ) { paddingValues ->
        // 对于非分页场景，我们需要一个不同的 "Smart" 组件或直接实现
        // 这里为了简单，我们直接用你之前的 PostList 加上 pullRefresh
        val pullRefreshState = rememberPullRefreshState(
            refreshing = uiState.isLoading,
            onRefresh = { viewModel.syncData() }
        )
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .pullRefresh(pullRefreshState)
        ) {
            if (uiState.posts.isNotEmpty()) {
                PostList(posts = uiState.posts) // 你之前的 PostList 组件
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
}