package com.template.generated.vant

import android.widget.Toast
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
import com.template.core.ui.vant.VanButton
import com.template.core.ui.vant.VanButtonSize
import com.template.core.ui.vant.VanButtonType
import com.template.core.ui.vant.VanCell
import com.template.core.ui.vant.VanCellGroup
import com.template.core.ui.vant.VanInput
import com.template.core.ui.vant.VanInputAlign
import com.template.core.ui.vant.VanInputType
import com.template.core.ui.vant.VanTextArea

@Composable
fun VanInputDemo() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            "Input 输入框",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // 1. 基础用法
        DemoSection("基础用法", padding = false) {
            var text by remember { mutableStateOf("") }
            var tel by remember { mutableStateOf("") }
            var digit by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            VanCellGroup {
                // 文本
                VanCell(
                    title = "文本",
                    valueComposable = {
                        VanInput(
                            value = text,
                            onValueChange = { text = it },
                            placeholder = "请输入文本",
                            clearable = true,
                            align = VanInputAlign.Right // Cell 中通常右对齐看起来比较整齐，或者左对齐紧跟 Label
                        )
                    }
                )
                // 手机号
                VanCell(
                    title = "手机号",
                    valueComposable = {
                        VanInput(
                            value = tel,
                            onValueChange = { tel = it },
                            type = VanInputType.Tel,
                            placeholder = "请输入手机号",
                            align = VanInputAlign.Right
                        )
                    }
                )
                // 整数
                VanCell(
                    title = "整数",
                    valueComposable = {
                        VanInput(
                            value = digit,
                            onValueChange = { digit = it },
                            type = VanInputType.Digit,
                            placeholder = "请输入整数",
                            align = VanInputAlign.Right
                        )
                    }
                )
                // 密码
                VanCell(
                    title = "密码",
                    valueComposable = {
                        VanInput(
                            value = password,
                            onValueChange = { password = it },
                            type = VanInputType.Password,
                            placeholder = "请输入密码",
                            align = VanInputAlign.Right
                        )
                    }
                )
            }
        }

        // 2. 插入内容 (前后缀)
        DemoSection("插入内容", padding = false) {
            var sms by remember { mutableStateOf("") }

            VanCellGroup {
                VanCell(
                    title = "短信验证码",
                    valueComposable = {
                        VanInput(
                            value = sms,
                            onValueChange = { sms = it },
                            placeholder = "请输入验证码",
                            prefix = { Text("💁", fontSize = 16.sp) }, // 前缀 Emoji
                            suffix = {
                                VanButton(
                                    text = "发送",
                                    type = VanButtonType.Primary,
                                    size = VanButtonSize.Mini,
                                    onClick = {
                                        Toast.makeText(
                                            context,
                                            "发送验证码",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }
                        )
                    }
                )
            }
        }

        // 3. 多行输入
        DemoSection("多行输入 (TextArea)", padding = false) {
            var val1 by remember { mutableStateOf("") }
            var val2 by remember { mutableStateOf("") }

            VanCellGroup {
                VanCell(
                    title = "多行输入",
                    label = "自适应高度", // 放在 label 显示描述
                    valueComposable = {
                        VanTextArea(
                            value = val1,
                            onValueChange = { val1 = it },
                            placeholder = "请输入留言"
                        )
                    }
                )

                VanCell(
                    title = "固定高度",
                    valueComposable = {
                        VanTextArea(
                            value = val2,
                            onValueChange = { val2 = it },
                            minHeight = 100.dp, // 强制最小高度
                            placeholder = "最小高度 100dp"
                        )
                    }
                )
            }
        }

        // 4. 字数统计
        DemoSection("字数统计", padding = false) {
            var val1 by remember { mutableStateOf("") }
            var val2 by remember { mutableStateOf("") }

            VanCellGroup {
                VanCell(
                    title = "单行限制",
                    valueComposable = {
                        VanInput(
                            value = val1,
                            onValueChange = { val1 = it },
                            maxLength = 10,
                            placeholder = "最多10个字符",
                            onOverlimit = {
                                Toast.makeText(
                                    context,
                                    "不能超过10个字符",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                )

                VanCell(
                    title = "多行统计",
                    valueComposable = {
                        VanTextArea(
                            value = val2,
                            onValueChange = { val2 = it },
                            maxLength = 50,
                            showWordLimit = true,
                            placeholder = "显示字数统计"
                        )
                    }
                )
            }
        }

        // 5. 对齐方式 & 状态
        DemoSection("状态与对齐", padding = false) {
            var v1 by remember { mutableStateOf("只读模式") }
            var v2 by remember { mutableStateOf("禁用模式") }
            var v3 by remember { mutableStateOf("") }

            VanCellGroup {
                VanCell(
                    title = "只读",
                    valueComposable = {
                        VanInput(value = v1, onValueChange = {}, readOnly = true)
                    }
                )
                VanCell(
                    title = "禁用",
                    valueComposable = {
                        VanInput(value = v2, onValueChange = {}, disabled = true)
                    }
                )
                VanCell(
                    title = "居中对齐",
                    valueComposable = {
                        VanInput(
                            value = v3,
                            onValueChange = { v3 = it },
                            align = VanInputAlign.Center,
                            placeholder = "输入内容居中"
                        )
                    }
                )
            }
        }
    }
}