package com.template.core.ui.vant

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import kotlin.math.abs

// --- 常量与样式 ---
object VanPickerColors {
    val Background = Color.White
    val ItemText = Color(0xFF323233)
    val ActiveText = Color(0xFF1989FA) // 选中高亮色，或者黑色
    val Mask = Color(0xFFF2F3F5) // 选中框背景
    val ButtonCancelBg = Color(0xFFF2F3F5)
    val ButtonConfirmBg = Color(0xFFF2F3F5)
    val ButtonCancelText = Color(0xFF969799)
    val ButtonConfirmText = Color(0xFF1989FA)
}

// --- 1. 核心滚轮组件 (Wheel) ---

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VanWheelColumn(
    items: List<String>,
    selectedIndex: Int,
    onIndexChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    itemHeight: Dp = 44.dp,
    visibleItemCount: Int = 5
) {
    val density = LocalDensity.current
    val itemHeightPx = with(density) { itemHeight.toPx() }

    // LazyColumn 的 contentPadding 会让第一个 item 初始位置在中间
    // 我们需要 scroll 到 selectedIndex
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = selectedIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    // 核心修复：精确计算中心项
    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .filter { !it } // 只在滚动停止时触发
            .collect {
                val layoutInfo = listState.layoutInfo
                if (layoutInfo.visibleItemsInfo.isNotEmpty()) {
                    // 容器的中心点坐标（相对于列表内容）
                    val containerCenter = layoutInfo.viewportStartOffset + (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset) / 2

                    // 找到离中心点最近的 Item
                    var closestItemIndex = -1
                    var minDistance = Float.MAX_VALUE

                    layoutInfo.visibleItemsInfo.forEach { item ->
                        val itemCenter = item.offset + item.size / 2
                        val distance = abs(containerCenter - itemCenter).toFloat()
                        if (distance < minDistance) {
                            minDistance = distance
                            closestItemIndex = item.index
                        }
                    }

                    if (closestItemIndex != -1 && closestItemIndex != selectedIndex && closestItemIndex in items.indices) {
                        onIndexChanged(closestItemIndex)
                    }
                }
            }
    }

    // 外部 selectedIndex 变更时，滚动到对应位置
    LaunchedEffect(selectedIndex) {
        // 防止循环触发：如果当前状态已经是停止且位置正确，就不再 scroll
        if (!listState.isScrollInProgress) {
            // 检查当前中心是否已经是 selectedIndex
            // 简单做法：直接 scrollToItem。Compose 会处理 diff。
            // 注意：scrollToItem 会把 item 放到 viewport 顶部，但因为我们有 padding，所以实际效果是放到中间。
            // 这里的 index 对应的是 listState 的 firstVisibleItemIndex (不含 padding 偏移)
            // 实际上，因为 padding 的存在，scrollToItem(index) 会把 index 这个 item 放在 padding 下面。
            // 我们 contentPadding 是 itemHeight * 2。
            // 所以 scrollToItem(0) -> item 0 在中间。
            // 这与我们的逻辑一致。
            if (listState.firstVisibleItemIndex != selectedIndex || listState.firstVisibleItemScrollOffset != 0) {
                listState.animateScrollToItem(selectedIndex)
            }
        }
    }

    Box(
        modifier = modifier.height(itemHeight * visibleItemCount),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight * visibleItemCount),
            // padding = (VisibleCount - 1) / 2 * ItemHeight
            // 例如 Visible=5, padding = 2 * ItemHeight. 这样第0项就在第3格(中间)
            contentPadding = PaddingValues(vertical = itemHeight * (visibleItemCount / 2))
        ) {
            items(items.size) { index ->
                // 这里的选中状态由外部 props 驱动，而不是内部 state
                // 这样滚动停止 -> 回调 onIndexChanged -> Parent 更新 State -> Recompose -> isSelected 更新
                val isSelected = index == selectedIndex

                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = items[index],
                        fontSize = if (isSelected) 18.sp else 16.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) VanPickerColors.ItemText else Color.Gray,
                        modifier = Modifier.alpha(if (isSelected) 1f else 0.6f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// --- 2. 选择器容器 (Picker Container) ---
// 包含选中条背景和蒙层

@Composable
fun VanPickerContainer(
    itemHeight: Dp = 44.dp,
    visibleItemCount: Int = 5,
    columns: @Composable RowScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp), // 两侧留白
        contentAlignment = Alignment.Center
    ) {
        // 选中态背景条 (中间的灰色圆角矩形)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .clip(RoundedCornerShape(8.dp))
                .background(VanPickerColors.Mask)
        )

        // 滚轮列区域
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            columns()
        }

        // 渐变遮罩 (可选，增加立体感)
        // 上遮罩
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(itemHeight * (visibleItemCount / 2))
//                .align(Alignment.TopCenter)
//                .background(
//                    Brush.verticalGradient(
//                        colors = listOf(Color.White.copy(alpha = 0.9f), Color.White.copy(alpha = 0.4f))
//                    )
//                )
//        )
        // 下遮罩
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(itemHeight * (visibleItemCount / 2))
//                .align(Alignment.BottomCenter)
//                .background(
//                    Brush.verticalGradient(
//                        colors = listOf(Color.White.copy(alpha = 0.4f), Color.White.copy(alpha = 0.9f))
//                    )
//                )
//        )
    }
}

// --- 3. 日期选择器 (Date Picker) ---

@Composable
fun VanDatePicker(
    currentDate: LocalDate = LocalDate.now(),
    minDate: LocalDate = LocalDate.of(2000, 1, 1),
    maxDate: LocalDate = LocalDate.of(2050, 12, 31),
    onDateChange: (LocalDate) -> Unit
) {
    // 状态
    var year by remember(currentDate) { mutableIntStateOf(currentDate.year) }
    var month by remember(currentDate) { mutableIntStateOf(currentDate.monthValue) }
    var day by remember(currentDate) { mutableIntStateOf(currentDate.dayOfMonth) }

    // 数据源计算
    val years = (minDate.year..maxDate.year).toList()
    val months = (1..12).toList()

    // 动态计算天数
    val daysInMonth = remember(year, month) {
        val date = LocalDate.of(year, month, 1)
        (1..date.lengthOfMonth()).toList()
    }

    // 索引计算
    val yearIndex = years.indexOf(year).coerceAtLeast(0)
    val monthIndex = months.indexOf(month).coerceAtLeast(0)
    val dayIndex = daysInMonth.indexOf(day).coerceAtLeast(0).coerceAtMost(daysInMonth.lastIndex)

    VanPickerContainer {
        // 年
        VanWheelColumn(
            items = years.map { "${it}年" },
            selectedIndex = yearIndex,
            onIndexChanged = { idx ->
                val newYear = years[idx]
                year = newYear
                // 修正日期 (如 2月29日 -> 平年2月28日)
                val maxDay = LocalDate.of(newYear, month, 1).lengthOfMonth()
                val newDay = day.coerceAtMost(maxDay)
                onDateChange(LocalDate.of(newYear, month, newDay))
            },
            modifier = Modifier.weight(1f)
        )

        // 月
        VanWheelColumn(
            items = months.map { "${it}月" },
            selectedIndex = monthIndex,
            onIndexChanged = { idx ->
                val newMonth = months[idx]
                month = newMonth
                val maxDay = LocalDate.of(year, newMonth, 1).lengthOfMonth()
                val newDay = day.coerceAtMost(maxDay)
                onDateChange(LocalDate.of(year, newMonth, newDay))
            },
            modifier = Modifier.weight(1f)
        )

        // 日
        VanWheelColumn(
            items = daysInMonth.map { "${it}日" },
            selectedIndex = dayIndex,
            onIndexChanged = { idx ->
                val newDay = daysInMonth[idx]
                day = newDay
                onDateChange(LocalDate.of(year, month, newDay))
            },
            modifier = Modifier.weight(1f)
        )
    }
}

// --- 4. 时间选择器 (Time Picker) ---

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VanTimePicker(
    currentTime: LocalTime = LocalTime.now(),
    onTimeChange: (LocalTime) -> Unit
) {
    var hour by remember(currentTime) { mutableIntStateOf(currentTime.hour) }
    var minute by remember(currentTime) { mutableIntStateOf(currentTime.minute) }

    val hours = (0..23).toList()
    val minutes = (0..59).toList()

    VanPickerContainer {
        // 时
        VanWheelColumn(
            items = hours.map { it.toString().padStart(2, '0') },
            selectedIndex = hours.indexOf(hour),
            onIndexChanged = { idx ->
                hour = hours[idx]
                onTimeChange(LocalTime.of(hour, minute))
            },
            modifier = Modifier.weight(1f)
        )

        // 分
        VanWheelColumn(
            items = minutes.map { it.toString().padStart(2, '0') },
            selectedIndex = minutes.indexOf(minute),
            onIndexChanged = { idx ->
                minute = minutes[idx]
                onTimeChange(LocalTime.of(hour, minute))
            },
            modifier = Modifier.weight(1f)
        )
    }
}

// --- 5. 底部弹框封装 (Popup Wrapper) ---

@Composable
fun VanPickerPopup(
    visible: Boolean,
    onClose: () -> Unit,
    title: String = "",
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    content: @Composable () -> Unit
) {
    VanPopup(
        visible = visible,
        onClose = onClose,
        position = VanPopupPosition.Bottom,
        round = true, // 圆角弹窗
        safeAreaInsetBottom = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(VanPickerColors.Background)
                .padding(bottom = 16.dp), // 底部按钮留白
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 顶部标题
            if (title.isNotEmpty()) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = VanPickerColors.ItemText,
                    modifier = Modifier.padding(top = 24.dp, bottom = 10.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(24.dp))
            }

            // 选择器内容
            content()

            Spacer(modifier = Modifier.height(20.dp))

            // 底部按钮区域 (参考图片样式：两个大按钮并排)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 取消按钮
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(VanPickerColors.ButtonCancelBg)
                        .clickable(onClick = onCancel),
                    contentAlignment = Alignment.Center
                ) {
                    Text("取消", color = VanPickerColors.ButtonCancelText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                // 确定按钮
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(VanPickerColors.ButtonConfirmBg)
                        .clickable(onClick = onConfirm),
                    contentAlignment = Alignment.Center
                ) {
                    Text("确定", color = VanPickerColors.ButtonConfirmText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}