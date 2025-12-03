package com.template.core.ui.vant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// Vant 内置的 Empty 图片 CDN 地址
// 实际生产中建议替换为本地 Vector Drawable
object VanEmptyImages {
    const val Default = "https://img.yzcdn.cn/vant/empty-image-default.png"
    const val Error = "https://img.yzcdn.cn/vant/empty-image-error.png"
    const val Network = "https://img.yzcdn.cn/vant/empty-image-network.png"
    const val Search = "https://img.yzcdn.cn/vant/empty-image-search.png"
}

enum class VanEmptyImagePreset {
    Default, Error, Network, Search
}

/**
 * VanEmpty - 空状态
 *
 * @param image 图片类型，支持枚举预设 (Default, Error, Network, Search) 或传入 URL 字符串。
 *              如果是预设，使用内置 CDN 图片；如果是 URL，直接加载。
 * @param description 图片下方的描述文字
 * @param modifier 修饰符
 * @param imageSize 图片大小，默认 160dp
 * @param content 底部内容插槽 (如按钮)
 */
@Composable
fun VanEmpty(
    modifier: Modifier = Modifier,
    image: Any = VanEmptyImagePreset.Default, // 可以是 Enum, String URL, 或 @Composable
    description: String? = null,
    imageSize: Dp? = null, // 默认根据类型决定，Default 为 160dp
    content: (@Composable () -> Unit)? = null
) {
    // 解析 Image URL 或 Composable
    val imageUrl = when (image) {
        VanEmptyImagePreset.Default -> VanEmptyImages.Default
        VanEmptyImagePreset.Error -> VanEmptyImages.Error
        VanEmptyImagePreset.Network -> VanEmptyImages.Network
        VanEmptyImagePreset.Search -> VanEmptyImages.Search
        is String -> image // 自定义 URL
        else -> null
    }

    // 默认大小
    val finalImageSize = imageSize ?: 160.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp, horizontal = 0.dp), // --rv-empty-padding
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // --- Image ---
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = description,
                modifier = Modifier.size(finalImageSize),
                contentScale = ContentScale.Fit
            )
        }
//        else if (image is @Composable () -> Unit) {
//            // 支持传入 Composable 作为 image (React 的 image={<img ... />})
//            // 在 Kotlin DSL 中通常通过另一个参数传入，这里为了保持参数统一性稍微 hack 一下，
//            // 实际上 Compose 风格通常会有 imageSlot 参数。
//            // 为了简化 API，我们假设 image 参数如果是 String/Enum 走上面，否则如果是 Composable lambda (比较少见做法)，
//            // 推荐使用下面的 imageSlot 参数重载版本。
//        }

        // --- Description ---
        if (!description.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(16.dp)) // --rv-empty-description-margin-top
            Text(
                text = description,
                color = Color(0xFF969799), // --rv-empty-description-color
                fontSize = 14.sp, // --rv-empty-description-font-size
                lineHeight = 20.sp, // --rv-empty-description-line-height
                modifier = Modifier.padding(horizontal = 60.dp) // --rv-empty-description-padding
            )
        }

        // --- Bottom Content ---
        if (content != null) {
            Spacer(modifier = Modifier.height(24.dp)) // --rv-empty-bottom-margin-top
            content()
        }
    }
}

/**
 * VanEmpty 重载 - 支持自定义 Image 组件
 */
@Composable
fun VanEmpty(
    modifier: Modifier = Modifier,
    image: @Composable () -> Unit,
    description: String? = null,
    content: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        image()

        if (!description.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = description,
                color = Color(0xFF969799),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = 60.dp)
            )
        }

        if (content != null) {
            Spacer(modifier = Modifier.height(24.dp))
            content()
        }
    }
}