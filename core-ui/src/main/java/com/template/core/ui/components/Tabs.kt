package com.template.core.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.launch

// 1. 定义 Tab 数据模型
// content 属性就是一个 Composable 函数，它可以是 HomeScreen，也可以是任何其他 UI
data class TabItem(
    val title: String,
    val icon: ImageVector,
    val content: @Composable () -> Unit
)

// 2. 创建一个通用的 Tab 组件，它不关心具体显示什么，只负责布局逻辑
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GenericTabScreen(
    tabs: List<TabItem> // <--- 这里接收参数
) {
    // 如果列表为空，直接返回，防止越界错误
    if (tabs.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TabRow(selectedTabIndex = pagerState.currentPage) {
                tabs.forEachIndexed { index, item ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch { pagerState.animateScrollToPage(index) }
                        },
                        text = { Text(text = item.title) },
                        icon = { Icon(imageVector = item.icon, contentDescription = null) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { pageIndex ->
                // 3. 在这里调用传入的 content 函数
                tabs[pageIndex].content()
            }
        }
    }
}