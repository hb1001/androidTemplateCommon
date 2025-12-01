package com.template.generated.vant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.template.core.ui.vant.VanButton
import com.template.core.ui.vant.VanButtonSize
import com.template.core.ui.vant.VanCell
import com.template.core.ui.vant.VanSwipeCell
import com.template.core.ui.vant.VanSwipeCellPosition
import com.template.core.ui.vant.VanSwipeCellSide
import com.template.core.ui.vant.rememberVanSwipeCellState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun VanSwipeCellDemo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            "SwipeCell 滑动单元格",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // 1. 基础用法
        DemoSection("基础用法", padding = false) {
            VanSwipeCell(
                rightAction = {
                    // Action 按钮高度会自动撑满
                    ActionBox(color = Color(0xFFEE0A24), text = "删除")
                },
                onOpen = { /* log */ },
                onClose = { /* log */ }
            ) {
                VanCell(title = "单元格", value = "内容")
            }
        }

        // 2. 事件监听与双侧滑动
        DemoSection("事件监听", padding = false) {
            VanSwipeCell(
                leftAction = {
                    ActionBox(color = Color(0xFF1989FA), text = "选择")
                },
                rightAction = {
                    Row {
                        ActionBox(color = Color(0xFFEE0A24), text = "删除")
                        ActionBox(color = Color(0xFF1989FA), text = "收藏")
                    }
                },
                onClick = { position ->
                    // Handle click: Left, Right, Cell
                }
            ) {
                VanCell(title = "单元格", value = "左右均可滑动")
            }
        }

        // 3. 自定义内容 (商品卡片)
        DemoSection("自定义内容", padding = false) {
            VanSwipeCell(
                rightAction = {
                    ActionBox(color = Color(0xFFEE0A24), text = "删除")
                }
            ) {
                // 模拟商品卡片布局
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White) // 显式背景
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
//                    Image(
//                        painter = rememberAsyncImagePainter("https://img.yzcdn.cn/vant/ipad.jpeg"),
//                        contentDescription = null,
//                        modifier = Modifier.size(88.dp).background(Color.LightGray),
//                        contentScale = ContentScale.Crop
//                    )
                    AsyncImage(
                        model = "https://img.yzcdn.cn/vant/ipad.jpeg",
                        contentDescription = null,
                        modifier = Modifier
                            .size(88.dp)
                            .background(Color.LightGray),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .height(88.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("商品标题", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("这里是商品描述", color = Color.Gray, fontSize = 12.sp)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("¥2.00", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("x2", color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // 4. 异步关闭
        DemoSection("异步关闭 (点击删除延迟1s)", padding = false) {
            var loading by remember { mutableStateOf(false) }

            VanSwipeCell(
                leftAction = { ActionBox(color = Color(0xFF1989FA), text = "选择") },
                rightAction = {
                    if (loading) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(60.dp)
                                .background(Color(0xFFEE0A24)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    } else {
                        ActionBox(color = Color(0xFFEE0A24), text = "删除")
                    }
                },
                beforeClose = { position ->
                    if (position == VanSwipeCellPosition.Right) {
                        loading = true
                        // 模拟异步操作 (如弹窗确认或网络请求)
                        delay(1000)
                        loading = false
                        true // 返回 true 允许关闭
                    } else {
                        true
                    }
                }
            ) {
                VanCell(title = "异步关闭", value = "向左滑动删除")
            }
        }

        // 5. 外部调用
        DemoSection("外部控制", padding = false) {
            val state = rememberVanSwipeCellState()
            val scope = rememberCoroutineScope()

            Column {
                VanSwipeCell(
                    state = state,
                    leftAction = { ActionBox(color = Color(0xFF1989FA), text = "选择") },
                    rightAction = { ActionBox(color = Color(0xFFEE0A24), text = "删除") }
                ) {
                    VanCell(title = "单元格", value = "代码控制开闭")
                }

                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    VanButton(text = "左滑", size = VanButtonSize.Small, onClick = {
                        scope.launch { state.open(VanSwipeCellSide.Left) }
                    })
                    VanButton(text = "关闭", size = VanButtonSize.Small, onClick = {
                        scope.launch { state.close() }
                    })
                    VanButton(text = "右滑", size = VanButtonSize.Small, onClick = {
                        scope.launch { state.open(VanSwipeCellSide.Right) }
                    })
                }
            }
        }
    }
}
// --- 辅助组件 ---
@Composable
private fun ActionBox(color: Color, text: String) {
    Box(
        modifier = Modifier
            .fillMaxHeight() // 关键：填充高度
            .width(60.dp)    // 宽度固定，或者用 padding
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Color.White, fontSize = 14.sp)
    }
}