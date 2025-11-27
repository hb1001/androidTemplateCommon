package com.template.core.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
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
    ) {
        // 5. 内容区域
        // 使用 innerPadding 防止内容被底部的 NavigationBar 遮挡
        Column(
            modifier = Modifier
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

@Composable
fun StableBottomTab(
    tabs: List<TabItem>,
    modifier: Modifier = Modifier
) {
//    var currentIndex by remember { mutableIntStateOf(0) }

    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    // 它的作用是：当页面从导航栈恢复时，根据 Key 恢复内部组件的所有状态
    val saveableStateHolder = rememberSaveableStateHolder()
    Column(modifier.fillMaxSize()) {

        Box(modifier.weight(1f)) {
            tabs.forEachIndexed { index, tab ->
                val isSelected = currentIndex == index

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(if (isSelected) 1f else 0f)
                        .graphicsLayer {
                            alpha = if (isSelected) 1f else 0f
                        }
                ) {
                    // 这确保了即使整个 StableBottomTab 被重建，
                    // 只要 index 对应的 Key 存在，内部的 ScrollState/TextField 等状态就会被恢复
                    saveableStateHolder.SaveableStateProvider(key = index) {
                        tab.content()
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            tabs.forEachIndexed { index, item ->
                val selected = index == currentIndex

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { currentIndex = index }
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = if (selected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = item.title,
                        color = if (selected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}