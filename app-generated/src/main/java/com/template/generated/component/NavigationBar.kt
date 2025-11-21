package com.template.generated.component

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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomTabScreen(
    tabs: List<TabItem>
) {
    if (tabs.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        // 2. 关键修改：使用 bottomBar 放置在底部
        bottomBar = {
            // 使用 NavigationBar 组件 (Material3 底部导航栏)
            NavigationBar(
                tonalElevation = 1.dp
            ) {
                tabs.forEachIndexed { index, item ->
                    // 判断当前项是否被选中
                    val isSelected = pagerState.currentPage == index

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            coroutineScope.launch { pagerState.animateScrollToPage(index) }
                        },
                        // 3. 设置图标 (上面)
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title
                            )
                        },
                        // 4. 设置文字 (下面)
                        // label 默认就会显示在 icon 下方
                        label = {
                            Text(text = item.title)
                        },
                        // 可选：是否总是显示文字？(true=总是显示, false=只有选中时显示)
                        alwaysShowLabel = true
                    )
                }
            }
        }
    ) { innerPadding ->
        // 5. 内容区域
        // 使用 innerPadding 防止内容被底部的 NavigationBar 遮挡
        Column(
            modifier = Modifier
                .padding(innerPadding) // 这一点非常重要！
                .fillMaxSize()
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { pageIndex ->
                tabs[pageIndex].content()
            }
        }
    }
}
