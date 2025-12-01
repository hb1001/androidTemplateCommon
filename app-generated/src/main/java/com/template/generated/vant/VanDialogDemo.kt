package com.template.generated.vant

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.template.core.ui.vant.DialogOptions
import com.template.core.ui.vant.LocalVanDialog
import com.template.core.ui.vant.VanCell
import com.template.core.ui.vant.VanCellGroup
import com.template.core.ui.vant.VanDialog
import com.template.core.ui.vant.VanDialogTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun VanDialogDemo() {
    val context = LocalContext.current
    val dialogController = LocalVanDialog.current
    val scope = rememberCoroutineScope()

    // 组件式调用的状态
    var componentVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            "Dialog 弹出框",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // 1. 函数调用 (消息提示)
        DemoSection("消息提示 (函数调用)", padding = false) {
            VanCellGroup {
                VanCell(
                    title = "弹窗提示",
                    isLink = true,
                    onClick = {
                        scope.launch {
                            dialogController.alert(
                                title = "标题",
                                message = "代码是写出来给人看的，附带能在机器上运行"
                            )
                        }
                    }
                )
                VanCell(
                    title = "弹窗提示 (无标题)",
                    isLink = true,
                    onClick = {
                        scope.launch {
                            dialogController.alert(
                                message = "代码是写出来给人看的，附带能在机器上运行"
                            )
                        }
                    }
                )
                VanCell(
                    title = "确认弹框 (Confirm)",
                    isLink = true,
                    onClick = {
                        scope.launch {
                            try {
                                dialogController.confirm(
                                    title = "标题",
                                    message = "代码是写出来给人看的，附带能在机器上运行"
                                )
                                Toast.makeText(context, "Confirm", Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )
            }
        }

        // 2. 圆角按钮风格
        DemoSection("圆角按钮风格", padding = false) {
            VanCellGroup {
                VanCell(
                    title = "圆角按钮弹窗",
                    isLink = true,
                    onClick = {
                        scope.launch {
                            try {
                                dialogController.confirm(
                                    title = "标题",
                                    message = "代码是写出来给人看的，附带能在机器上运行",
                                    theme = VanDialogTheme.RoundButton
                                )
                            } catch (e: Exception) {
                            }
                        }
                    }
                )
            }
        }

        // 3. 异步关闭 (模拟)
        DemoSection("异步关闭", padding = false) {
            VanCellGroup {
                VanCell(
                    title = "异步关闭",
                    isLink = true,
                    onClick = {
                        // 使用底层 show 方法来自定义逻辑
                        dialogController.show(
                            DialogOptions(
                                title = "标题",
                                message = "点击确认 1秒后关闭",
                                showCancelButton = true,
                                dismissOnAction = false, // 只有手动 dismiss 才关闭
                                onConfirm = {
                                    scope.launch {
                                        delay(1000)
                                        dialogController.dismiss()
                                        Toast.makeText(context, "异步关闭成功", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                },
                                onCancel = {
                                    dialogController.dismiss()
                                }
                            )
                        )
                    }
                )
            }
        }

        // 4. 组件调用 (自定义内容)
        DemoSection("组件调用 (自定义内容)", padding = false) {
            VanCellGroup {
                VanCell(
                    title = "组件调用",
                    isLink = true,
                    onClick = { componentVisible = true }
                )
            }

            // 嵌入组件调用
            VanDialog(
                visible = componentVisible,
                onDismissRequest = { componentVisible = false },
                title = "标题",
                showCancelButton = true,
                onConfirm = {
                    Toast.makeText(context, "点击确认", Toast.LENGTH_SHORT).show()
                    componentVisible = false
                },
                onCancel = { componentVisible = false }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(
                        model = "https://img.yzcdn.cn/vant/apple-3.jpg",
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )
                    Spacer(Modifier.height(10.dp))
                    Text("这是自定义的图片内容", fontSize = 14.sp, color = Color.Gray)
                }
            }
        }
    }
}