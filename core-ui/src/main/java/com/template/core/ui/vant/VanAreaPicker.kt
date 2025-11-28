package com.template.core.ui.vant

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.core.ui.uimodel.*

// --- 颜色常量 ---
object VanAreaColors {
    val Active = Color(0xFFEE0A24) // Vant Red
    val Text = Color(0xFF323233)
    val TextSecondary = Color(0xFF969799)
    val Background = Color.White
    val Divider = Color(0xFFEBEDF0)
}

/**
 * 省市区选择器 UI 组件
 *
 * @param provinces 所有省份数据
 * @param cities 所有城市数据
 * @param areas 所有区县数据
 * @param onFinish 选择完成回调 (Province, City, Area)
 * @param onCancel 点击关闭回调
 */
@Composable
fun VanAreaPicker(
    provinces: List<Province>,
    cities: List<City>,
    areas: List<Area>,
    onFinish: (Province, City, Area) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    // --- 状态管理 ---
    var selectedProvince by remember { mutableStateOf<Province?>(null) }
    var selectedCity by remember { mutableStateOf<City?>(null) }
    var selectedArea by remember { mutableStateOf<Area?>(null) }

    // 当前激活的 Tab 索引: 0=省, 1=市, 2=区
    var activeTabIndex by remember { mutableIntStateOf(0) }

    // --- 数据筛选逻辑 ---
    // 根据当前选中的省，筛选市
    val currentCityList by remember(selectedProvince, cities) {
        derivedStateOf {
            if (selectedProvince == null) emptyList()
            else cities.filter { it.provinceCode == selectedProvince!!.code }
        }
    }

    // 根据当前选中的市，筛选区
    val currentAreaList by remember(selectedCity, areas) {
        derivedStateOf {
            if (selectedCity == null) emptyList()
            else areas.filter { it.cityCode == selectedCity!!.code }
        }
    }

    // --- 交互逻辑 ---

    // 选中省份
    fun selectProvince(province: Province) {
        if (selectedProvince != province) {
            selectedProvince = province
            selectedCity = null
            selectedArea = null
        }
        activeTabIndex = 1 // 跳转到市
    }

    // 选中城市
    fun selectCity(city: City) {
        if (selectedCity != city) {
            selectedCity = city
            selectedArea = null
        }
        activeTabIndex = 2 // 跳转到区
    }

    // 选中区县 (完成)
    fun selectArea(area: Area) {
        selectedArea = area
        if (selectedProvince != null && selectedCity != null) {
            onFinish(selectedProvince!!, selectedCity!!, area)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(500.dp) // 给定一个高度，模拟 BottomSheet
            .background(VanAreaColors.Background)
    ) {
        // 1. 标题栏
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "请选择所在地区",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = VanAreaColors.Text
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color(0xFFC8C9CC),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(22.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onCancel
                    )
            )
        }

        // 2. Tabs 栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 省 Tab
            TabItem(
                text = selectedProvince?.name ?: "请选择",
                isSelected = activeTabIndex == 0,
                hasValue = selectedProvince != null,
                onClick = { activeTabIndex = 0 }
            )

            // 市 Tab (只有选中了省才显示)
            if (selectedProvince != null) {
                TabItem(
                    text = selectedCity?.name ?: "请选择",
                    isSelected = activeTabIndex == 1,
                    hasValue = selectedCity != null,
                    onClick = { activeTabIndex = 1 }
                )
            }

            // 区 Tab (只有选中了市才显示)
            if (selectedCity != null) {
                TabItem(
                    text = selectedArea?.name ?: "请选择",
                    isSelected = activeTabIndex == 2,
                    hasValue = selectedArea != null,
                    onClick = { activeTabIndex = 2 }
                )
            }
        }

        Divider(color = VanAreaColors.Divider, thickness = 0.5.dp)

        // 3. 列表区域 (使用 AnimatedContent 做淡入淡出切换)
        AnimatedContent(
            targetState = activeTabIndex,
            transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
            label = "ListSwitch"
        ) { tabIndex ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                when (tabIndex) {
                    0 -> {
                        // 省份列表
                        items(provinces) { item ->
                            ListItemRow(
                                text = item.name,
                                isSelected = item.code == selectedProvince?.code,
                                onClick = { selectProvince(item) }
                            )
                        }
                    }
                    1 -> {
                        // 城市列表
                        items(currentCityList) { item ->
                            ListItemRow(
                                text = item.name,
                                isSelected = item.code == selectedCity?.code,
                                onClick = { selectCity(item) }
                            )
                        }
                    }
                    2 -> {
                        // 区域列表
                        items(currentAreaList) { item ->
                            ListItemRow(
                                text = item.name,
                                isSelected = item.code == selectedArea?.code,
                                onClick = { selectArea(item) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- 内部辅助组件 ---

@Composable
private fun TabItem(
    text: String,
    isSelected: Boolean,
    hasValue: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 6.dp), // 增加点击区域
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            // 选中的 Tab 或者是已经选好值的 Tab 显示黑色，未选的显示高亮色(如果是当前)
            color = if (isSelected) VanAreaColors.Text else if(hasValue) VanAreaColors.Text else VanAreaColors.TextSecondary,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
        // 底部红色横条
        if (isSelected) {
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .width(24.dp) // 横条宽度
                    .height(3.dp)
                    .background(VanAreaColors.Active, RoundedCornerShape(2.dp))
            )
        } else {
            // 占位，防止文字跳动
            Spacer(modifier = Modifier.height(9.dp)) // 6 + 3
        }
    }
}

@Composable
private fun ListItemRow(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 10.dp), // 增大点击区域
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = if (isSelected) VanAreaColors.Active else VanAreaColors.Text,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )

        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = VanAreaColors.Active,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}