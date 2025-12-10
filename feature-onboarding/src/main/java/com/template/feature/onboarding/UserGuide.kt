package com.template.feature.onboarding
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.template.core.ui.components.CoachMarkController
import com.template.core.ui.components.CoachMarkOverlay
import com.template.core.ui.components.CoachMarkShape
import com.template.core.ui.components.CoachMarkStep
import com.template.core.ui.components.coachMarkTarget

@Composable
fun CoachMarkDemoScreen() {
    val controller = remember { CoachMarkController() }

    // 定义引导步骤
    val step1 = CoachMarkStep(
        id = "avatar",
        title = "这是头像",
        description = "点击这里可以修改你的个人资料。",
        shape = CoachMarkShape.Circle
    )
    val step2 = CoachMarkStep(
        id = "button",
        title = "操作按钮",
        description = "点击这里提交表单。",
        shape = CoachMarkShape.Rectangle
    )

    CoachMarkOverlay(
        controller = controller,
        steps = listOf(step1, step2)
    ) {
        // 你的实际 UI 内容
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // 目标 1：头像
            Box(
                modifier = Modifier
                    .size(80.dp)
                    // 标记 ID
                    .coachMarkTarget("avatar", controller)
                    .background(Color.Blue, androidx.compose.foundation.shape.CircleShape)
            )

            Spacer(modifier = Modifier.height(200.dp))

            Text("中间的一些无关内容...")

            Spacer(modifier = Modifier.height(200.dp))

            // 目标 2：按钮
            Button(
                onClick = { controller.show(step1) },
                // 标记 ID
                modifier = Modifier.coachMarkTarget("button", controller)
            ) {
                Text("开始引导 (Start Tutorial)")
            }
        }
    }
}