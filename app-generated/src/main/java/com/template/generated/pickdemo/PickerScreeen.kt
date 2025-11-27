package com.template.generated.pickdemo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickerDemoScreen() {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showCustomPicker by remember { mutableStateOf(false) }
    var showAreaPicker by remember { mutableStateOf(false) }

    // 结果展示
    var selectedDate by remember { mutableStateOf("未选择") }
    var selectedTime by remember { mutableStateOf("未选择") }
    var selectedHeight by remember { mutableStateOf("未选择") }
    var selectedArea by remember { mutableStateOf("未选择") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Compose 选择器大全", style = MaterialTheme.typography.headlineMedium)

        // 1. 日期选择器按钮
        Button(onClick = { showDatePicker = true }) {
            Text("日期选择 (原生): $selectedDate")
        }

        // 2. 时间选择器按钮
        Button(onClick = { showTimePicker = true }) {
            Text("时间选择 (原生): $selectedTime")
        }

        // 3. 自定义单列选择器按钮
        Button(onClick = { showCustomPicker = true }) {
            Text("单列选择 (身高): $selectedHeight")
        }

        // 4. 省市区联动选择器按钮
        Button(onClick = { showAreaPicker = true }) {
            Text("省市区联动: $selectedArea")
        }
    }

    // --- 1. 日期选择器 Dialog ---
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        val date = SimpleDateFormat("yyyy-MM-dd").format(Date(millis))
                        selectedDate = date
                    }
                    showDatePicker = false
                }) { Text("确定") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("取消") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // --- 2. 时间选择器 Dialog ---
    if (showTimePicker) {
        val timePickerState = rememberTimePickerState()
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedTime = "${timePickerState.hour}:${timePickerState.minute}"
                    showTimePicker = false
                }) { Text("确定") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("取消") }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }

    // --- 3. 自定义单列选择器 BottomSheet ---
    if (showCustomPicker) {
        ModalBottomSheet(onDismissRequest = { showCustomPicker = false }) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("选择身高", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))

                val heights = (140..200).map { "$it cm" }
                var tempSelection by remember { mutableStateOf(heights[0]) }

                WheelPicker(
                    items = heights,
                    initialIndex = 30, // 默认停在 170cm
                    onSelectionChanged = { index -> tempSelection = heights[index] }
                )

                Button(
                    onClick = {
                        selectedHeight = tempSelection
                        showCustomPicker = false
                    },
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                ) {
                    Text("确定")
                }
            }
        }
    }

    // --- 4. 省市区联动 BottomSheet ---
    if (showAreaPicker) {
        AreaPickerBottomSheet(
            onDismiss = { showAreaPicker = false },
            onConfirm = { province, city, district ->
                selectedArea = "$province - $city - $district"
                showAreaPicker = false
            }
        )
    }
}



// 模拟数据结构
data class Province(val name: String, val cities: List<City>) {
    override fun toString() = name
}
data class City(val name: String, val districts: List<String>) {
    override fun toString() = name
}

// 模拟数据源
val mockData = listOf(
    Province("北京市", listOf(
        City("市辖区", listOf("东城区", "西城区", "朝阳区", "海淀区"))
    )),
    Province("浙江省", listOf(
        City("杭州市", listOf("上城区", "拱墅区", "西湖区", "滨江区")),
        City("宁波市", listOf("海曙区", "江北区", "镇海区"))
    )),
    Province("广东省", listOf(
        City("广州市", listOf("荔湾区", "越秀区", "海珠区", "天河区")),
        City("深圳市", listOf("罗湖区", "福田区", "南山区"))
    ))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AreaPickerBottomSheet(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    // 状态管理
    var provinceIndex by remember { mutableIntStateOf(0) }
    var cityIndex by remember { mutableIntStateOf(0) }
    var districtIndex by remember { mutableIntStateOf(0) }

    // 联动逻辑：利用 derivedStateOf 动态计算当前显示的列表
    val currentProvince = mockData[provinceIndex]

    // 当省份变化时，城市列表变化。需要注意：如果之前的 cityIndex 超出了新列表范围，需重置
    val currentCities = currentProvince.cities
    // 安全检查，防止数组越界（实际项目中建议在 Effect 中重置 index）
    val safeCityIndex = cityIndex.coerceIn(currentCities.indices)

    val currentCity = currentCities[safeCityIndex]
    val currentDistricts = currentCity.districts
    val safeDistrictIndex = districtIndex.coerceIn(currentDistricts.indices)

    // 当省份改变时，强制重置城市和区索引
    // 注意：WheelPicker 内部使用 rememberLazyListState，如果 key 不变，位置不会重置。
    // 我们需要通过 key 来强制重组 WheelPicker，或者 WheelPicker 内部暴露 scrollTo 方法。
    // 这里使用 key 是一种简单的强制刷新手段。

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onDismiss) { Text("取消") }
                Text("选择地区", style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = {
                    onConfirm(
                        currentProvince.name,
                        currentCities[safeCityIndex].name,
                        currentDistricts[safeDistrictIndex]
                    )
                }) { Text("确定") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                // 省
                WheelPicker(
                    modifier = Modifier.weight(1f),
                    items = mockData,
                    initialIndex = provinceIndex,
                    onSelectionChanged = {
                        provinceIndex = it
                        cityIndex = 0 // 重置下级
                        districtIndex = 0
                    }
                )

                // 市 (使用 key 强制在数据源变动时重绘，回到顶部)
                key(provinceIndex) {
                    WheelPicker(
                        modifier = Modifier.weight(1f),
                        items = currentCities,
                        initialIndex = 0,
                        onSelectionChanged = {
                            cityIndex = it
                            districtIndex = 0 // 重置下级
                        }
                    )
                }

                // 区
                key(provinceIndex, cityIndex) {
                    WheelPicker(
                        modifier = Modifier.weight(1f),
                        items = currentDistricts,
                        initialIndex = 0,
                        onSelectionChanged = { districtIndex = it }
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}