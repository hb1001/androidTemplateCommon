package com.template.generated.vant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.core.ui.vant.VanSlider

@Composable
fun VanSliders() {
    // 状态
    var value1 by remember { mutableFloatStateOf(50f) }
    var valueRange by remember { mutableStateOf(listOf(20f, 60f)) }
    var valueStep by remember { mutableFloatStateOf(0f) }
    var valueVertical by remember { mutableFloatStateOf(50f) }
    var valueCustom by remember { mutableFloatStateOf(30f) }

    Column {
        Text(
            "Slider 滑块",
            modifier = Modifier.padding(16.dp),
            color = Color.Gray,
            fontSize = 14.sp
        )

        // 1. 基础用法
        Text(
            "基础用法: ${value1.toInt()}",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
        Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)) {
            VanSlider(
                value = value1,
                onValueChange = { value1 = it as Float }
            )
        }

        // 2. 双滑块
        Text(
            "双滑块: ${valueRange[0].toInt()} - ${valueRange[1].toInt()}",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
        Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)) {
            VanSlider(
                range = true,
                value = valueRange,
                onValueChange = { valueRange = it as List<Float> }
            )
        }

        // 3. 指定步长
        Text(
            "指定步长 (Step=10): ${valueStep.toInt()}",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
        Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)) {
            VanSlider(
                step = 10f,
                value = valueStep,
                onValueChange = { valueStep = it as Float }
            )
        }

        // 4. 自定义样式 & 按钮
        Text(
            "自定义样式 & 按钮",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
        Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)) {
            VanSlider(
                value = valueCustom,
                activeColor = Color(0xFFEE0A24),
                barHeight = 4.dp,
                onValueChange = { valueCustom = it as Float },
                button = {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFEE0A24), RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${valueCustom.toInt()}",
                            color = Color.White,
                            fontSize = 10.sp
                        )
                    }
                }
            )
        }

        // 5. 垂直方向
        Text(
            "垂直方向",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
        Row(
            modifier = Modifier
                .height(200.dp) // 必须给高度
                .padding(start = 30.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            VanSlider(
                vertical = true,
                value = valueVertical,
                onValueChange = { valueVertical = it as Float }
            )

            // 垂直双滑块
            VanSlider(
                vertical = true,
                range = true,
                value = valueRange, // 复用之前的 Range 值
                onValueChange = { valueRange = it as List<Float> }
            )
        }
    }
}


