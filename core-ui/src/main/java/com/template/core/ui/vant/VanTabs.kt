package com.template.core.ui.vant

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch

// --- Data Structures & DSL ---

enum class VanTabsType {
    Line, Card, Capsule, Jumbo
}

class VanTabScope {
    internal val tabs = mutableListOf<VanTabItemData>()

    fun tab(
        title: String,
        name: Any? = null,
        disabled: Boolean = false,
        badge: @Composable (() -> Unit)? = null,
        description: String? = null,
        content: @Composable () -> Unit
    ) {
        tabs.add(VanTabItemData(title, name, disabled, badge, description, content))
    }
}

data class VanTabItemData(
    val title: String,
    val name: Any?,
    val disabled: Boolean,
    val badge: @Composable (() -> Unit)?,
    val description: String?,
    val content: @Composable () -> Unit
)

// --- Main Component ---

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VanTabs(
    active: Any? = 0,
    modifier: Modifier = Modifier,
    type: VanTabsType = VanTabsType.Line,
    color: Color = Color(0xFFEE0A24),
    background: Color = Color.White,
    sticky: Boolean = false,
    swipeable: Boolean = false,
    onChange: ((Any) -> Unit)? = null,
    content: VanTabScope.() -> Unit
) {
    // 1. 收集 Tab 数据
    val tabs = remember(content) {
        val scope = VanTabScope()
        scope.content()
        scope.tabs.toList()
    }

    if (tabs.isEmpty()) return

    // 2. 状态管理 (关键修复：兼容受控与非受控模式)
    // 内部维护一个状态，初始化为传入的 active
    var internalActive by remember { mutableStateOf(active ?: 0) }

    // 监听外部 active 变化，如果变化了，同步到内部 (受控模式支持)
    LaunchedEffect(active) {
        if (active != null) {
            internalActive = active
        }
    }

    // 计算当前的 Index
    val activeIndex = remember(internalActive, tabs) {
        val indexByName = tabs.indexOfFirst { it.name == internalActive }
        if (indexByName != -1) indexByName else (internalActive as? Int) ?: 0
    }

    // Pager State
    val pagerState = rememberPagerState(initialPage = activeIndex) { tabs.size }
    val coroutineScope = rememberCoroutineScope()

    // 3. 联动逻辑
    // 当内部计算出的 activeIndex 变化时，同步 Pager
    LaunchedEffect(activeIndex) {
        if (pagerState.currentPage != activeIndex && !pagerState.isScrollInProgress) {
            pagerState.animateScrollToPage(activeIndex)
        }
    }

    // 监听 Pager 滑动 -> 更新 internalActive -> 触发 onChange
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (page in tabs.indices) {
                val tab = tabs[page]
                val name = tab.name ?: page

                if (internalActive != name && internalActive != page) {
                    internalActive = name // 更新内部状态
                    onChange?.invoke(name) // 通知外部
                }
            }
        }
    }

    // 点击 Tab 的处理
    val onTabClick: (Int) -> Unit = { index ->
        val tab = tabs[index]
        if (!tab.disabled) {
            val name = tab.name ?: index
            internalActive = name // 立即更新内部状态，保证非滑动模式下 UI 立即响应
            onChange?.invoke(name)

            if (swipeable) {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }
        }
    }

    // 用于 Header 显示的高亮索引
    val headerActiveIndex by remember(swipeable, activeIndex) {
        derivedStateOf {
            if (swipeable) pagerState.currentPage else activeIndex
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        // --- Header ---
        Box(
            modifier = Modifier
                .zIndex(if (sticky) 1f else 0f)
                .fillMaxWidth()
                .background(background)
        ) {
            VanTabsHeader(
                tabs = tabs,
                activeIndex = headerActiveIndex,
                type = type,
                color = color,
                onTabClick = onTabClick
            )
        }

        // --- Content ---
        if (swipeable) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFFF7F8FA)),
                userScrollEnabled = true
            ) { page ->
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    tabs.getOrNull(page)?.content?.invoke()
                }
            }
        } else {
            // 非滑动模式：直接渲染当前 index 对应的内容
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color(0xFFF7F8FA))
                    .padding(12.dp)
            ) {
                tabs.getOrNull(activeIndex)?.content?.invoke()
            }
        }
    }
}

// --- Header Implementation ---

@Composable
private fun VanTabsHeader(
    tabs: List<VanTabItemData>,
    activeIndex: Int,
    type: VanTabsType,
    color: Color,
    onTabClick: (Int) -> Unit
) {
    val scrollState = rememberScrollState()
    val height = if (type == VanTabsType.Jumbo) 64.dp else 44.dp

    val cardModifier = if (type == VanTabsType.Card) {
        Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .height(30.dp)
            .border(1.dp, color, RoundedCornerShape(4.dp))
            .clip(RoundedCornerShape(4.dp))
    } else {
        Modifier.height(height)
    }
    val sizeModifier = if (type == VanTabsType.Card) {
        Modifier.wrapContentWidth()
    } else {
        Modifier.fillMaxWidth()
    }

    Row(
        modifier = Modifier
            .then(sizeModifier) // 应用宽度策略
            .then(cardModifier)
            .horizontalScroll(scrollState),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEachIndexed { index, tab ->
            val selected = index == activeIndex

            when (type) {
                VanTabsType.Line -> TabStyleLine(tab, selected, color) { onTabClick(index) }
                VanTabsType.Card -> TabStyleCard(tab, selected, color, index, tabs.size) { onTabClick(index) }
                VanTabsType.Capsule -> TabStyleCapsule(tab, selected, color) { onTabClick(index) }
                VanTabsType.Jumbo -> TabStyleJumbo(tab, selected, color) { onTabClick(index) }
            }
        }
    }
}

// --- Styles ---

@Composable
private fun TabStyleLine(
    tab: VanTabItemData,
    selected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    val textColor = if (tab.disabled) Color(0xFFC8C9CC) else if (selected) Color(0xFF323233) else Color(0xFF646566)
    val fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal

    Column(
        modifier = Modifier
            .clickable(enabled = !tab.disabled, onClick = onClick)
            .padding(horizontal = 16.dp)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box {
            Text(
                text = tab.title,
                fontSize = 14.sp,
                fontWeight = fontWeight,
                color = textColor
            )
            if (tab.badge != null) {
                Box(modifier = Modifier.align(Alignment.TopEnd).offset(x = 10.dp, y = (-6).dp)) {
                    tab.badge.invoke()
                }
            }
        }

        if (selected) {
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(3.dp)
                    .background(color, RoundedCornerShape(3.dp))
            )
        } else {
            Spacer(modifier = Modifier.height(9.dp))
        }
    }
}

@Composable
private fun TabStyleCard(
    tab: VanTabItemData,
    selected: Boolean,
    color: Color,
    index: Int,
    total: Int,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) color else Color.White
    val textColor = if (selected) Color.White else color

    Row(
        modifier = Modifier
            .clickable(enabled = !tab.disabled, onClick = onClick)
            .background(backgroundColor)
            .width(80.dp)
            .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = tab.title,
            fontSize = 14.sp,
            color = if (tab.disabled) Color.White.copy(0.5f) else textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
    if (index < total - 1) {
        Box(
            modifier = Modifier.width(1.dp).fillMaxHeight().background(color)
        )
    }
}

@Composable
private fun TabStyleCapsule(
    tab: VanTabItemData,
    selected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    // 胶囊样式的修复
    val borderColor = if (selected) color else Color(0xFFEBEDF0)

    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            // 关键修改：先 Clip 形状，再 Clickable，这样点击水波纹就是圆角的
            .clip(CircleShape)
            .clickable(enabled = !tab.disabled, onClick = onClick)
            // 边框和背景放在后面
            .border(1.dp, if(tab.disabled) Color.Transparent else if(selected) color else Color(0xFFEBEDF0), CircleShape)
            .background(if(tab.disabled) Color.Transparent else if (selected) color else Color.White)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tab.title,
            fontSize = 14.sp,
            color = if (selected) Color.White else Color(0xFF323233)
        )
    }
}

@Composable
private fun TabStyleJumbo(
    tab: VanTabItemData,
    selected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    val titleColor = if (selected) color else Color(0xFF323233)
    val descColor = Color(0xFF969799)
    val bg = if (selected) Color.White else Color(0xFFF7F8FA)

    Column(
        modifier = Modifier
            .clickable(enabled = !tab.disabled, onClick = onClick)
            .background(bg)
            .padding(horizontal = 20.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = tab.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = titleColor
        )
        if (!tab.description.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = tab.description,
                fontSize = 12.sp,
                color = descColor
            )
        }
    }
}