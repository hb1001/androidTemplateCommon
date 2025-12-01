package com.template.generated.vant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.core.ui.vant.VanSwipe
import com.template.core.ui.vant.VanSwipeItem

@Composable
fun VanSwipes() {
    Column {
        Text(
            "Swipe 轮播",
            modifier = Modifier.padding(16.dp),
            color = Color.Gray,
            fontSize = 14.sp
        )

        // 1. 基础用法 (自动播放)
        Text(
            "基础用法 (Autoplay)",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
        VanSwipe(
            itemCount = 4,
            autoplay = 3000,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) // 必须指定高度
        ) { index ->
            // 模拟不同背景色
            val color = when (index) {
                0 -> Color(0xFF39A9ED)
                1 -> Color(0xFF66C6F2)
                2 -> Color(0xFF39A9ED)
                else -> Color(0xFF66C6F2)
            }
            VanSwipeItem(modifier = Modifier.background(color)) {
                Text(text = "${index + 1}", color = Color.White, fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 2. 纵向滚动
        Text(
            "纵向滚动",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
        VanSwipe(
            itemCount = 4,
            vertical = true,
            autoplay = 3000,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { index ->
            val color = when (index) {
                0 -> Color(0xFF39A9ED)
                1 -> Color(0xFF66C6F2)
                2 -> Color(0xFF39A9ED)
                else -> Color(0xFF66C6F2)
            }
            VanSwipeItem(modifier = Modifier.background(color)) {
                Text(text = "Vertical ${index + 1}", color = Color.White, fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3. 自定义滑块大小 (一屏多页)
        Text(
            "自定义滑块大小 (Loop=false)",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
        VanSwipe(
            itemCount = 4,
            loop = false,
            width = 300.dp, // 设置固定宽度
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { index ->
            // 给个 margin 来看出间隙
            Box(modifier = Modifier
                .padding(end = 10.dp)
                .fillMaxSize()) {
                val color = when (index) {
                    0 -> Color(0xFF39A9ED)
                    1 -> Color(0xFF66C6F2)
                    2 -> Color(0xFF39A9ED)
                    else -> Color(0xFF66C6F2)
                }
                VanSwipeItem(modifier = Modifier.background(color)) {
                    Text(text = "${index + 1}", color = Color.White, fontSize = 20.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 4. 自定义指示器
        Text(
            "自定义指示器",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
        VanSwipe(
            itemCount = 4,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            indicator = { active, total ->
                // 自定义右上角数字指示器
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp)
                        .background(Color(0x33000000), shape = RoundedCornerShape(4.dp))
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${active + 1}/$total",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        ) { index ->
            val color = when (index) {
                0 -> Color(0xFF39A9ED)
                1 -> Color(0xFF66C6F2)
                2 -> Color(0xFF39A9ED)
                else -> Color(0xFF66C6F2)
            }
            VanSwipeItem(modifier = Modifier.background(color)) {
                Text(text = "${index + 1}", color = Color.White, fontSize = 20.sp)
            }
        }
    }
}

