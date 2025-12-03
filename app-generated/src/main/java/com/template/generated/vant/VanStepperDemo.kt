package com.template.generated.vant

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.template.core.ui.vant.VanCell
import com.template.core.ui.vant.VanCellGroup
import com.template.core.ui.vant.VanStepper
import com.template.core.ui.vant.VanStepperTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun VanStepperDemo() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // State holders
    var valueBasic by remember { mutableDoubleStateOf(1.0) }
    var valueMin by remember { mutableDoubleStateOf(1.0) }
    var valueStep by remember { mutableDoubleStateOf(1.0) }
    var valueRange by remember { mutableDoubleStateOf(5.0) }
    var valueDisabledInput by remember { mutableDoubleStateOf(1.0) }
    var valueDecimal by remember { mutableDoubleStateOf(1.0) }
    var valueCustomSize by remember { mutableDoubleStateOf(1.0) }
    var valueAsync by remember { mutableDoubleStateOf(1.0) }
    var valueRound by remember { mutableDoubleStateOf(1.0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8FA))
            .padding(bottom = 50.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DemoTitle("Stepper 步进器")

        VanCellGroup {
            // 1. 基础用法
            VanCell(title = "基础用法", center = true) {
                VanStepper(
                    value = valueBasic,
                    onValueChange = { valueBasic = it }
                )
            }

            // 2. 设置最小值
            VanCell(title = "设置最小值 (min=0)", center = true) {
                VanStepper(
                    value = valueMin,
                    min = 0.0,
                    onValueChange = { valueMin = it }
                )
            }

            // 3. 步长设置
            VanCell(title = "步长设置 (step=2)", center = true) {
                VanStepper(
                    value = valueStep,
                    step = 2.0,
                    onValueChange = { valueStep = it }
                )
            }

            // 4. 设置输入范围
            VanCell(title = "设置输入范围 (5~8)", center = true) {
                VanStepper(
                    value = valueRange,
                    min = 5.0,
                    max = 8.0,
                    step = 1.0,
                    onValueChange = { valueRange = it }
                )
            }

            // 5. 禁用状态
            VanCell(title = "禁用状态", center = true) {
                VanStepper(
                    value = 1.0,
                    disabled = true,
                    onValueChange = {}
                )
            }

            // 6. 禁用输入框
            VanCell(title = "禁用输入框", center = true) {
                VanStepper(
                    value = valueDisabledInput,
                    disableInput = true,
                    onValueChange = { valueDisabledInput = it }
                )
            }

            // 7. 固定小数位数
            VanCell(title = "固定小数位数 (1位)", center = true) {
                VanStepper(
                    value = valueDecimal,
                    step = 0.2,
                    decimalLength = 1,
                    onValueChange = { valueDecimal = it }
                )
            }

            // 8. 自定义大小
            VanCell(title = "自定义大小", center = true) {
                VanStepper(
                    value = valueCustomSize,
                    inputWidth = 44.dp,
                    buttonSize = 28.dp,
                    onValueChange = { valueCustomSize = it }
                )
            }

            // 9. 异步变更
            VanCell(title = "异步变更 (延迟 1s)", center = true) {
                VanStepper(
                    value = valueAsync,
                    onValueChange = { newVal ->
                        // 模拟异步
                        Toast.makeText(context, "加载中...", Toast.LENGTH_SHORT).show()
                        scope.launch {
                            delay(1000)
                            valueAsync = newVal
                            Toast.makeText(context, "已更新", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }

            // 10. 圆角风格
            VanCell(title = "圆角风格", center = true) {
                VanStepper(
                    value = valueRound,
                    theme = VanStepperTheme.Round,
                    buttonSize = 22.dp,
                    disableInput = true, // 圆角风格通常禁用输入，类似购物车数量加减
                    onValueChange = { valueRound = it }
                )
            }
        }
    }
}