package com.template.generated.vant

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.core.ui.vant.VanBadge
import com.template.core.ui.vant.VanIcon


@Composable
fun VanIconDemo() {
    val context = LocalContext.current

    // 模拟: 实际使用时，请确保 assets/icons/ 目录下有这些 svg 文件
    // 这里为了演示方便，使用了 placeholder 的网络 SVG，或者你需要手动放置文件
    // 假设你放入了 'star.svg', 'chat.svg' 等到 assets/icons/ 目录

    // 如果没有本地文件，这里使用一个在线 SVG 做演示 (Vite Logo)
    val demoSvgUrl = "BillO" //""https://vitejs.dev/logo.svg"
    val demoName = "PhoneO" // 假设你下载了这个文件并命名为 vite-logo.svg 放在 assets/icons/

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            "Icon 图标 (Coil SVG)",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            "请确保 assets/icons/ 下存在对应的 .svg 文件",
            fontSize = 12.sp,
            color = Color.Red,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // 1. 基础用法
        DemoSection("基础用法 (加载 Assets/Network)", padding = false) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // 加载 assets/icons/location-o.svg (你需要放入文件)
                // 这里暂时用网络图演示效果，实际请用: VanIcon(name = "location-o")
                VanIcon(name = demoSvgUrl, size = 32.dp)

                // 假设 assets/icons/like-o.svg 存在
                VanIcon(name = "PhoneO", size = 32.dp)

                // 假设 assets/icons/star-o.svg 存在
                VanIcon(name = "star-o", size = 32.dp)
                VanIcon(name = "PhoneO", size = 32.dp)
            }
        }

        // 2. 徽标提示
        DemoSection("徽标提示 (搭配 Badge)", padding = false) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                // 红点
                VanBadge(dot = true) {
                    VanIcon(name = "Bars", size = 32.dp)
                }

                // 数字
                VanBadge(content = "99+") {
                    VanIcon(name = "Bill", size = 32.dp)
                }
                // 数字
                VanBadge(content = "99+") {
                    VanIcon(name = "BillO", size = 32.dp)
                }
            }
        }

        // 3. 图标颜色
        DemoSection("图标颜色 (Tint)", padding = false) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // SVG 会被自动着色
                VanIcon(name = demoSvgUrl, color = Color(0xFFF44336), size = 32.dp)
                VanIcon(name = demoSvgUrl, color = Color(0xFF3F45FF), size = 32.dp)
            }
        }

        // 4. 图标大小
        DemoSection("图标大小", padding = false) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                VanIcon(name = demoSvgUrl, size = 20.dp)
                VanIcon(name = demoSvgUrl, size = 30.dp)
                VanIcon(name = demoSvgUrl, size = 40.dp)
            }
        }

        // 4. 图标大小
        DemoSection("图标大小", padding = false) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                VanIcon(name = demoSvgUrl, size = 20.dp)
                VanIcon(name = demoSvgUrl, size = 30.dp)
                VanIcon(name = demoSvgUrl, size = 40.dp)
            }
        }

        // 5. 图标旋转
        DemoSection("图标旋转 (Spin & Rotate)", padding = false) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // 动画旋转
                    VanIcon(name = demoSvgUrl, spin = true, size = 32.dp, color = Color(0xFF1989FA))
                    Spacer(Modifier.height(4.dp))
                    Text("Spin", fontSize = 12.sp, color = Color.Gray)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // 静态旋转
                    VanIcon(name = demoSvgUrl, rotate = 90f, size = 32.dp)
                    Spacer(Modifier.height(4.dp))
                    Text("Rotate 90°", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        // 6. 点击事件
        DemoSection("点击事件", padding = false) {
            Row(modifier = Modifier.padding(16.dp)) {
                VanIcon(
                    name = demoSvgUrl,
                    size = 32.dp,
                    onClick = {
                        Toast.makeText(context, "Icon Clicked!", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}
