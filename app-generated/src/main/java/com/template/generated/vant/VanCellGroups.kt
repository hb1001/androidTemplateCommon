package com.template.generated.vant

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.core.ui.vant.VanCell
import com.template.core.ui.vant.VanCellArrowDirection
import com.template.core.ui.vant.VanCellGroup
import com.template.core.ui.vant.VanCellSize

@Composable
fun VanCellGroups() {
    Column() {
        // ================== 2. Cell 单元格测试 ==================

        // --- 基础用法 ---
        VanCellGroup(title = "Cell 基础用法") {
            VanCell(title = "单元格", value = "内容")
            VanCell(title = "单元格", value = "内容", label = "描述信息", border = false)
        }

        // --- 卡片风格 ---
        VanCellGroup(title = "卡片风格 (Inset)", inset = true) {
            VanCell(title = "单元格", value = "内容")
            VanCell(title = "单元格", value = "内容", label = "描述信息", border = false)
        }

        // --- 单元格大小 ---
        VanCellGroup(title = "单元格大小") {
            VanCell(title = "普通", value = "内容")
            VanCell(
                title = "大号",
                value = "内容",
                size = VanCellSize.Large,
                label = "描述信息",
                border = false
            )
        }

        // --- 展示图标 & 箭头 ---
        VanCellGroup(title = "展示图标与箭头") {
            VanCell(title = "带图标", icon = Icons.Filled.LocationOn, value = "定位")
            VanCell(title = "跳转链接", isLink = true)
            VanCell(
                title = "向下箭头",
                isLink = true,
                arrowDirection = VanCellArrowDirection.Down,
                value = "展开",
                border = false
            )
        }

        // --- 垂直居中 ---
        VanCellGroup(title = "垂直居中 (Center)") {
            VanCell(
                center = true,
                title = "多行文本",
                value = "内容居中",
                label = "这是一段很长很长的描述信息，会让单元格高度增加，此时右侧内容应该垂直居中。",
                border = false
            )
        }

        // --- 高级用法 (插槽) ---
        VanCellGroup(title = "高级用法 (自定义插槽)") {
            // 自定义右侧图标
            VanCell(
                title = "自定义右侧图标",
                rightIconComposable = {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            )

            // 自定义标题
            VanCell(
                value = "自定义标题",
                isLink = true,
                titleComposable = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("我的等级")
                        Spacer(modifier = Modifier.width(4.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.extraSmall
                        ) {
                            Text(
                                "LV.5",
                                fontSize = 10.sp,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                },
                border = false
            )
        }
    }
}