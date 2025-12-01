package com.template.generated.vant

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.core.ui.vant.VanSearch
import com.template.core.ui.vant.VanSearchAlign
import com.template.core.ui.vant.VanSearchShape

@Composable
fun VanSearchDemo() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            "Search 搜索",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp)
        )

        // 1. 基础用法
        DemoSection("基础用法", padding = false) {
            var value by remember { mutableStateOf("") }
            VanSearch(
                value = value,
                onValueChange = { value = it },
                placeholder = "请输入搜索关键词"
            )
        }

        // 2. 事件监听
        DemoSection("事件监听", padding = false) {
            var value by remember { mutableStateOf("") }
            VanSearch(
                value = value,
                onValueChange = { value = it },
                showAction = true, // 显示右侧取消按钮
                placeholder = "请输入搜索关键词",
                onSearch = {
                    Toast.makeText(context, "Search: $it", Toast.LENGTH_SHORT).show()
                },
                onCancel = {
                    Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show()
                    value = ""
                },
                onClear = {
                    Toast.makeText(context, "Clear", Toast.LENGTH_SHORT).show()
                },
                onClickInput = {
                    // Toast.makeText(context, "Click Input", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // 3. 搜索框内容对齐 (Center)
        DemoSection("搜索框内容对齐 (Center)", padding = false) {
            var value by remember { mutableStateOf("") }
            VanSearch(
                value = value,
                onValueChange = { value = it },
                align = VanSearchAlign.Center,
                placeholder = "请输入搜索关键词"
            )
        }

        // 4. 禁用搜索框
        DemoSection("禁用搜索框", padding = false) {
            VanSearch(
                value = "无法输入",
                onValueChange = {},
                disabled = true,
                placeholder = "请输入搜索关键词"
            )
        }

        // 5. 自定义背景色 & 圆角
        DemoSection("自定义背景色 & 圆角", padding = false) {
            var value by remember { mutableStateOf("") }
            VanSearch(
                value = value,
                onValueChange = { value = it },
                shape = VanSearchShape.Round,
                background = Color(0xFF4FC08D),
                placeholder = "请输入搜索关键词"
            )
        }

        // 6. 自定义按钮
        DemoSection("自定义按钮 (Action)", padding = false) {
            var value by remember { mutableStateOf("") }
            VanSearch(
                value = value,
                onValueChange = { value = it },
                label = { Text("地址") }, // 左侧 Label
                placeholder = "请输入搜索关键词",
                action = {
                    // 自定义 Action 插槽
                    Text(
                        text = "搜索",
                        color = Color(0xFF323233),
                        modifier = Modifier.clickable {
                            Toast.makeText(context, "Custom Search: $value", Toast.LENGTH_SHORT)
                                .show()
                        }
                    )
                }
            )
        }
    }
}