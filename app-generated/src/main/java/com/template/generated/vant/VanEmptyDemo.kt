package com.template.generated.vant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.template.core.ui.vant.VanButton
import com.template.core.ui.vant.VanButtonType
import com.template.core.ui.vant.VanEmpty
import com.template.core.ui.vant.VanEmptyImagePreset
import com.template.core.ui.vant.VanTabs
import com.template.core.ui.vant.VanTabsType

@Composable
fun VanEmptyDemo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(bottom = 50.dp)
    ) {
        DemoTitle("Empty 空状态")

        // 1. 基础用法
        DemoTitle("基础用法", padding = false)
        VanEmpty(description = "描述文字")

        // 2. 图片类型
        DemoTitle("图片类型", padding = false)
        // 使用 Tabs 切换不同类型
        VanTabs(type = VanTabsType.Line) {
            tab(title = "通用错误") {
                VanEmpty(image = VanEmptyImagePreset.Error, description = "描述文字")
            }
            tab(title = "网络错误") {
                VanEmpty(image = VanEmptyImagePreset.Network, description = "描述文字")
            }
            tab(title = "搜索提示") {
                VanEmpty(image = VanEmptyImagePreset.Search, description = "描述文字")
            }
        }

        // 3. 自定义图片
        DemoTitle("自定义图片", padding = false)
//        VanEmpty(
//            imageSize = 90.dp,
//            image = {
//                // 自定义 Image 组件
//                AsyncImage(
//                    model = "https://img.yzcdn.cn/vant/custom-empty-image.png",
//                    contentDescription = null,
//                    modifier = Modifier.size(90.dp)
//                )
//            },
//            description = "描述文字"
//        )

        // 4. 底部内容
        DemoTitle("底部内容", padding = false)
        VanEmpty(description = "描述文字") {
            VanButton(
                text = "按钮",
                round = true,
                type = VanButtonType.Primary,
                modifier = Modifier.width(160.dp),
                onClick = {}
            )
        }
    }
}