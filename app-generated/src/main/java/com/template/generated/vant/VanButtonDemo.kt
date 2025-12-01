package com.template.generated.vant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.template.core.ui.vant.VanButton
import com.template.core.ui.vant.VanButtonIconPosition
import com.template.core.ui.vant.VanButtonSize
import com.template.core.ui.vant.VanButtonType

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VanButtonDemo() {
    Column {
        // 1. 按钮类型
        DemoSection("按钮类型") {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                VanButton(type = VanButtonType.Primary, text = "主要按钮")
                VanButton(type = VanButtonType.Success, text = "成功按钮")
                VanButton(type = VanButtonType.Default, text = "默认按钮")
                VanButton(type = VanButtonType.Danger, text = "危险按钮")
                VanButton(type = VanButtonType.Warning, text = "警告按钮")
            }
        }

        // 2. 朴素按钮
        DemoSection("朴素按钮") {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                VanButton(plain = true, type = VanButtonType.Primary, text = "朴素按钮")
                VanButton(plain = true, type = VanButtonType.Success, text = "朴素按钮")
            }
        }

        // 3. 细边框
        DemoSection("细边框 (Hairline)") {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                VanButton(
                    plain = true,
                    hairline = true,
                    type = VanButtonType.Primary,
                    text = "细边框按钮"
                )
                VanButton(
                    plain = true,
                    hairline = true,
                    type = VanButtonType.Success,
                    text = "细边框按钮"
                )
            }
        }

        // 4. 禁用状态
        DemoSection("禁用状态") {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                VanButton(disabled = true, type = VanButtonType.Primary, text = "禁用状态")
                VanButton(disabled = true, type = VanButtonType.Success, text = "禁用状态")
            }
        }

        // 5. 加载状态
        DemoSection("加载状态") {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                VanButton(loading = true, type = VanButtonType.Primary)
                VanButton(
                    loading = true,
                    type = VanButtonType.Primary,
                    loadingText = "加载中..."
                )
                VanButton(
                    loading = true,
                    type = VanButtonType.Success,
                    loadingText = "加载中..."
                )
            }
        }

        // 6. 按钮形状
        DemoSection("按钮形状") {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                VanButton(square = true, type = VanButtonType.Primary, text = "方形按钮")
                VanButton(round = true, type = VanButtonType.Success, text = "圆形按钮")
            }
        }

        // 7. 图标按钮
        DemoSection("图标按钮") {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                VanButton(icon = Icons.Default.Add, type = VanButtonType.Primary)
                VanButton(
                    icon = Icons.Default.Add,
                    type = VanButtonType.Primary,
                    text = "按钮"
                )
                VanButton(
                    plain = true,
                    icon = Icons.Default.Star,
                    type = VanButtonType.Primary,
                    text = "按钮"
                )
            }
        }

        // 8. 图标位置
        DemoSection("图标位置") {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                VanButton(
                    icon = Icons.Default.ArrowForward,
                    iconPosition = VanButtonIconPosition.Right,
                    type = VanButtonType.Primary,
                    text = "下一步"
                )
            }
        }

        // 9. 按钮尺寸
        DemoSection("按钮尺寸") {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                VanButton(
                    size = VanButtonSize.Large,
                    type = VanButtonType.Primary,
                    text = "大号按钮",
                    block = true
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    VanButton(
                        size = VanButtonSize.Normal,
                        type = VanButtonType.Primary,
                        text = "普通按钮"
                    )
                    VanButton(
                        size = VanButtonSize.Small,
                        type = VanButtonType.Primary,
                        text = "小型按钮"
                    )
                    VanButton(
                        size = VanButtonSize.Mini,
                        type = VanButtonType.Primary,
                        text = "迷你"
                    )
                }
            }
        }

        // 10. 块级元素
        DemoSection("块级元素") {
            VanButton(type = VanButtonType.Primary, block = true, text = "块级元素")
        }

        // 11. 自定义颜色
        DemoSection("自定义颜色") {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                VanButton(color = Color(0xFF7232DD), text = "单色按钮")
                VanButton(color = Color(0xFF7232DD), plain = true, text = "单色按钮")

                // 渐变色
                val gradient = Brush.horizontalGradient(
                    colors = listOf(Color(0xFFFF6034), Color(0xFFEE0A24))
                )
                VanButton(
                    gradient = gradient,
                    text = "渐变色按钮",
                    color = Color.White // 这里指定color是为了确保文字是白色的
                )
            }
            Row {
                VanButton(square = true, type = VanButtonType.Primary, text = "方形按钮")
                VanButton(square = true, type = VanButtonType.Primary, text = "方形按钮")
            }
        }
    }
}