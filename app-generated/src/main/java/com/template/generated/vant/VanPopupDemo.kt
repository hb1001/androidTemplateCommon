package com.template.generated.vant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.core.ui.vant.VanCell
import com.template.core.ui.vant.VanCellGroup
import com.template.core.ui.vant.VanPopup
import com.template.core.ui.vant.VanPopupPosition


@Composable
fun VanPopupDemo() {
    // 状态管理
    var showBasic by remember { mutableStateOf(false) }

    var showTop by remember { mutableStateOf(false) }
    var showBottom by remember { mutableStateOf(false) }
    var showLeft by remember { mutableStateOf(false) }
    var showRight by remember { mutableStateOf(false) }

    var showRound by remember { mutableStateOf(false) }
    var showCloseable by remember { mutableStateOf(false) }
    var showTitle by remember { mutableStateOf(false) }

    // 由于 VanPopup 需要覆盖在页面最上层，我们这里用一个 Box 包裹整个演示页面
    // 实际项目中，VanPopup 建议放在 Scaffold 的最外层 Box 中
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                "Popup 弹出层",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // 1. 基础用法
            DemoSection("基础用法", padding = false) {
                VanCellGroup {
                    VanCell(title = "展示弹出层", isLink = true, onClick = { showBasic = true })
                }
            }

//            // 1. 基础用法
//            DemoSection("子组件", padding = false) {
//
//                var showTopSub by remember { mutableStateOf(false) }
//                var showBottomSub by remember { mutableStateOf(false) }
//                VanCellGroup {
//                    VanCell(title = "展示弹出层", isLink = true, onClick = { showTopSub = true })
//                    VanCell(title = "展示弹出层", isLink = true, onClick = { showBottomSub = true })
//                }
//
//                Box(modifier = Modifier.fillMaxWidth().height(50.dp).background(Color.Red)){
//                    // 2. Top
//                    VanPopup(
//                        visible = showTopSub,
//                        onClose = { showTopSub = false },
//                        position = VanPopupPosition.Top,
//                        contentHeight = 200.dp
//                    ) {
//                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("顶部弹出") }
//                    }
//
//                    // Bottom
//                    VanPopup(
//                        visible = showBottomSub,
//                        onClose = { showBottomSub = false },
//                        position = VanPopupPosition.Bottom,
//                        contentHeight = 200.dp,
//                        safeAreaInsetBottom = true
//                    ) {
//                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("底部弹出") }
//                    }
//                }
//            }

            // 2. 弹出位置
            DemoSection("弹出位置", padding = false) {
                VanCellGroup {
                    VanCell(title = "顶部弹出", isLink = true, onClick = { showTop = true })
                    VanCell(title = "底部弹出", isLink = true, onClick = { showBottom = true })
                    VanCell(title = "左侧弹出", isLink = true, onClick = { showLeft = true })
                    VanCell(title = "右侧弹出", isLink = true, onClick = { showRight = true })
                }
            }

            // 3. 圆角弹窗
            DemoSection("圆角弹窗", padding = false) {
                VanCellGroup {
                    VanCell(title = "圆角弹窗", isLink = true, onClick = { showRound = true })
                }
            }

            // 4. 关闭图标
            DemoSection("关闭图标", padding = false) {
                VanCellGroup {
                    VanCell(title = "关闭图标", isLink = true, onClick = { showCloseable = true })
                }
            }

            // 5. 标题弹窗
            DemoSection("标题弹窗", padding = false) {
                VanCellGroup {
                    VanCell(title = "标题弹窗", isLink = true, onClick = { showTitle = true })
                }
            }
        }

        // --- Popups (放在最上层) ---

        // 1. 基础 (Center)
        VanPopup(
            visible = showBasic,
            onClose = { showBasic = false },
            contentWidth = 200.dp,
            contentHeight = 150.dp
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("内容")
            }
        }

        // 2. Top
        VanPopup(
            visible = showTop,
            onClose = { showTop = false },
            position = VanPopupPosition.Top,
            contentHeight = 200.dp
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("顶部弹出") }
        }

        // Bottom
        VanPopup(
            visible = showBottom,
            onClose = { showBottom = false },
            position = VanPopupPosition.Bottom,
            contentHeight = 200.dp,
            safeAreaInsetBottom = true
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("底部弹出") }
        }

        // Left
        VanPopup(
            visible = showLeft,
            onClose = { showLeft = false },
            position = VanPopupPosition.Left,
            contentWidth = 200.dp
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("左侧弹出") }
        }

        // Right
        VanPopup(
            visible = showRight,
            onClose = { showRight = false },
            position = VanPopupPosition.Right,
            contentWidth = 200.dp
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("右侧弹出") }
        }

        // Round
        VanPopup(
            visible = showRound,
            onClose = { showRound = false },
            position = VanPopupPosition.Bottom,
            round = true,
            contentHeight = 200.dp
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("圆角弹窗") }
        }

        // Closeable
        VanPopup(
            visible = showCloseable,
            onClose = { showCloseable = false },
            position = VanPopupPosition.Bottom,
            closeable = true,
            contentHeight = 200.dp
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("关闭图标") }
        }

        // Title
        VanPopup(
            visible = showTitle,
            onClose = { showTitle = false },
            position = VanPopupPosition.Bottom,
            round = true,
            closeable = true,
            title = "标题",
            description = "这是一段很长很长的描述这是一段很长很长的描述",
            contentHeight = 250.dp
        ) {
            // 内容区域
        }
    }
}
