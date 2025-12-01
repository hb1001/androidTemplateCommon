package com.template.generated.vant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.core.ui.vant.VanBadge
import com.template.core.ui.vant.VanBadgePosition

class VanBadage {
}

@Composable
fun VanBadges() {
    Text(
        "Badge 徽标",
        modifier = Modifier.padding(16.dp),
        color = Color.Gray,
        fontSize = 14.sp
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp) // 行间距
    ) {
        // 1. 基础用法
        Text("基础用法", fontSize = 14.sp, color = Color.Gray)
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            VanBadge(count = 5) { BadgeChildBox() }
            VanBadge(count = 10) { BadgeChildBox() }
            VanBadge(content = "Hot") { BadgeChildBox() }
            VanBadge(dot = true) { BadgeChildBox() }
        }

        // 2. 最大值
        Text("最大值", fontSize = 14.sp, color = Color.Gray)
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            VanBadge(count = 20, max = 9) { BadgeChildBox() }
            VanBadge(count = 50, max = 20) { BadgeChildBox() }
            VanBadge(count = 200, max = 99) { BadgeChildBox() }
        }

        // 3. 自定义颜色
        Text("自定义颜色", fontSize = 14.sp, color = Color.Gray)
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            VanBadge(count = 5, color = Color(0xFF1989FA)) { BadgeChildBox() }
            VanBadge(count = 10, color = Color(0xFF1989FA)) { BadgeChildBox() }
            VanBadge(dot = true, color = Color(0xFF1989FA)) { BadgeChildBox() }
        }

        // 4. 自定义内容 (Icon)
        Text("自定义徽标内容", fontSize = 14.sp, color = Color.Gray)
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            VanBadge(badgeSlot = {
                Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(10.dp))
            }) { BadgeChildBox() }

            VanBadge(badgeSlot = {
                Icon(Icons.Default.Close, null, tint = Color.White, modifier = Modifier.size(10.dp))
            }) { BadgeChildBox() }

            VanBadge(badgeSlot = {
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    null,
                    tint = Color.White,
                    modifier = Modifier.size(10.dp)
                )
            }) { BadgeChildBox() }
        }

        // 5. 自定义位置
        Text("自定义位置", fontSize = 14.sp, color = Color.Gray)
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            VanBadge(count = 10, position = VanBadgePosition.TopLeft) { BadgeChildBox() }
            VanBadge(count = 10, position = VanBadgePosition.BottomLeft) { BadgeChildBox() }
            VanBadge(count = 10, position = VanBadgePosition.BottomRight) { BadgeChildBox() }
        }

        // 6. 偏移量
        Text("自定义偏移量 (x=10, y=10)", fontSize = 14.sp, color = Color.Gray)
        Row {
            VanBadge(count = 10, offset = DpOffset(10.dp, 10.dp)) { BadgeChildBox() }
        }

        // 7. 独立展示
        Text("独立展示", fontSize = 14.sp, color = Color.Gray)
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            VanBadge(count = 20)
            VanBadge(count = 200, max = 99)
        }
    }
}

// 模拟 Vant 文档中的灰色方块子元素
@Composable
fun BadgeChildBox() {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(Color(0xFFF2F3F5), RoundedCornerShape(4.dp))
    )
}