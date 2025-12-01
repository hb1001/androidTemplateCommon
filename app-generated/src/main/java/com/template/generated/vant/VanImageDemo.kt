package com.template.generated.vant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.core.ui.vant.VanImage
import com.template.core.ui.vant.VanImageColors
import com.template.core.ui.vant.VanImageFit


@Composable
fun VanImageDemo() {
    val src = "https://img.yzcdn.cn/vant/cat.jpeg"
    val errorSrc = "https://error.url/x.jpg" // 故意错误的链接

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            "Image 图片",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // 1. 基础用法
        DemoSection("基础用法", padding = false) {
            Box(Modifier.padding(16.dp)) {
                VanImage(
                    src = src,
                    width = 100.dp,
                    height = 100.dp
                )
            }
        }

        // 2. 填充模式,删掉这部分正常
        DemoSection("填充模式", padding = false) {
            val fits = VanImageFit.entries.toTypedArray()

            @OptIn(ExperimentalLayoutApi::class)
            FlowRow(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                maxItemsInEachRow = 3
            ) {
                fits.forEach { fit ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(100.dp)
                    ) {
                        VanImage(
                            src = src,
                            width = 100.dp,
                            height = 100.dp,
                            fit = fit,
                            radius = 4.dp
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(fit.name, fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }

        // 3. 圆形图片
        DemoSection("圆形图片", padding = false) {
            val fits = listOf(VanImageFit.Contain, VanImageFit.Cover, VanImageFit.Fill)

            @OptIn(ExperimentalLayoutApi::class)
            FlowRow(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                maxItemsInEachRow = 3
            ) {
                fits.forEach { fit ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(100.dp)
                    ) {
                        VanImage(
                            src = src,
                            width = 100.dp,
                            height = 100.dp,
                            fit = fit,
                            round = true
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(fit.name, fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }

        // 4. 加载中提示
        DemoSection("加载中提示", padding = false) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    VanImage(
                        src = "", // 空链接或慢速链接保持 Loading 状态
                        width = 100.dp,
                        height = 100.dp,
                        showError = false // 为了演示 Loading 样式，强制不显示错误
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("默认提示", fontSize = 12.sp)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    VanImage(
                        src = "",
                        width = 100.dp,
                        height = 100.dp,
                        showError = false,
                        // 自定义 Loading 插槽 (Spinner)
                        loadingIcon = {
                            CircularProgressIndicator(
                                color = VanImageColors.PlaceholderIcon,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("自定义提示", fontSize = 12.sp)
                }
            }
        }

        // 5. 加载失败提示
        DemoSection("加载失败提示", padding = false) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    VanImage(
                        src = errorSrc, // 错误链接
                        width = 100.dp,
                        height = 100.dp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("默认提示", fontSize = 12.sp)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    VanImage(
                        src = errorSrc,
                        width = 100.dp,
                        height = 100.dp,
                        // 自定义 Error 插槽 (文字)
                        errorIcon = {
                            Text(
                                "加载失败",
                                fontSize = 14.sp,
                                color = VanImageColors.PlaceholderText
                            )
                        }
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("自定义提示", fontSize = 12.sp)
                }
            }
        }
    }
}