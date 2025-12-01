package com.template.generated.vant

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.core.ui.vant.VanButton
import com.template.core.ui.vant.VanButtonSize
import com.template.core.ui.vant.VanButtonType
import com.template.core.ui.vant.VanCell
import com.template.core.ui.vant.VanCellGroup
import com.template.core.ui.vant.VanTag
import com.template.core.ui.vant.VanTagSize
import com.template.core.ui.vant.VanTagType


@Composable
fun VanTags() {
    // 状态：用于控制可关闭标签的显示
    var showCloseableTag by remember { mutableStateOf(true) }

    Column {
        Text(
            "Tag 标签",
            modifier = Modifier.padding(16.dp),
            color = Color.Gray,
            fontSize = 14.sp
        )

        // 1. 基础用法
        VanCellGroup(title = "基础用法") {
            VanCell(title = "primary 类型", valueComposable = {
                VanTag(type = VanTagType.Primary) { Text("标签") }
            })
            VanCell(title = "success 类型", valueComposable = {
                VanTag(type = VanTagType.Success) { Text("标签") }
            })
            VanCell(title = "danger 类型", valueComposable = {
                VanTag(type = VanTagType.Danger) { Text("标签") }
            })
            VanCell(title = "warning 类型", valueComposable = {
                VanTag(type = VanTagType.Warning) { Text("标签") }
            })
        }

        // 2. 样式风格
        VanCellGroup(title = "样式风格") {
            VanCell(title = "空心样式", valueComposable = {
                VanTag(plain = true, type = VanTagType.Primary) { Text("标签") }
            })
            VanCell(title = "圆角样式", valueComposable = {
                VanTag(round = true, type = VanTagType.Primary) { Text("标签") }
            })
            VanCell(title = "标记样式", valueComposable = {
                VanTag(mark = true, type = VanTagType.Primary) { Text("标签") }
            })
            VanCell(title = "可关闭标签", valueComposable = {
                VanTag(
                    show = showCloseableTag,
                    plain = true,
                    closeable = true,
                    size = VanTagSize.Medium,
                    type = VanTagType.Primary,
                    onClose = { showCloseableTag = false }
                ) {
                    Text("标签")
                }
                // 为了演示效果，如果关闭了提供一个重置按钮
                if (!showCloseableTag) {
                    VanButton(
                        size = VanButtonSize.Mini,
                        type = VanButtonType.Default,
                        text = "重置",
                        onClick = { showCloseableTag = true })
                }
            })
        }

        // 3. 标签大小
        VanCellGroup(title = "标签大小") {
            VanCell(title = "小号标签", valueComposable = {
                VanTag(type = VanTagType.Primary, size = VanTagSize.Small) { Text("标签") }
            })
            VanCell(title = "中号标签", valueComposable = {
                VanTag(type = VanTagType.Primary, size = VanTagSize.Medium) { Text("标签") }
            })
            VanCell(title = "大号标签", valueComposable = {
                VanTag(type = VanTagType.Primary, size = VanTagSize.Large) { Text("标签") }
            })
        }

        // 4. 自定义颜色
        VanCellGroup(title = "自定义颜色") {
            VanCell(title = "背景颜色", valueComposable = {
                VanTag(color = Color(0xFF7232DD)) { Text("标签") }
            })
            VanCell(title = "文字颜色", valueComposable = {
                VanTag(color = Color(0xFFFFE1E1), textColor = Color(0xFFAD0000)) { Text("标签") }
            })
            VanCell(title = "空心颜色", valueComposable = {
                VanTag(color = Color(0xFF7232DD), plain = true) { Text("标签") }
            })
        }
    }
}