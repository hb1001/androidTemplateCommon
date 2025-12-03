package com.template.core.ui.vant

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest

// --- 枚举 ---
enum class VanImageFit {
    Contain,
    Cover,
    Fill,
    None,
    ScaleDown
}

// --- 颜色常量 ---
object VanImageColors {
    val PlaceholderBackground = Color(0xFFF7F8FA) // Vant 背景色
    val PlaceholderIcon = Color(0xFFDCDEE0)       // Gray-4
    val PlaceholderText = Color(0xFF969799)       // Gray-6
}

// --- 组件实现 ---

/**
 * VanImage - 图片组件
 *
 * 修复说明：
 * 增加了对 [ImageVector] 的判断。如果是矢量图，使用原生 [Image] 组件渲染，
 * 避免 Coil 抛出 IllegalArgumentException。
 */
@Composable
fun VanImage(
    src: Any?,
    modifier: Modifier = Modifier,
    fit: VanImageFit = VanImageFit.Fill,
    alt: String? = null,
    width: Dp? = null,
    height: Dp? = null,
    radius: Dp = 0.dp,
    round: Boolean = false,
    showError: Boolean = true,
    showLoading: Boolean = true,
    errorIcon: (@Composable () -> Unit)? = null,
    loadingIcon: (@Composable () -> Unit)? = null,
    iconSize: Dp = 32.dp,
    onClick: (() -> Unit)? = null,
) {
    val context = LocalContext.current

    // 1. 基础修饰符 & 尺寸处理
    var finalModifier = modifier
    if (width != null) finalModifier = finalModifier.width(width)
    if (height != null) finalModifier = finalModifier.height(height)

    // 2. 处理圆角/形状
    val shape: Shape = if (round) CircleShape else RoundedCornerShape(radius)
    finalModifier = finalModifier.clip(shape)

    // 3. 处理点击
    if (onClick != null) {
        finalModifier = finalModifier.clickable { onClick() }
    }

    // 4. 处理填充模式
    val contentScale = when (fit) {
        VanImageFit.Contain -> ContentScale.Fit
        VanImageFit.Cover -> ContentScale.Crop
        VanImageFit.Fill -> ContentScale.FillBounds
        VanImageFit.None -> ContentScale.None
        VanImageFit.ScaleDown -> ContentScale.Inside
    }

    // 5. 渲染容器
    Box(
        modifier = finalModifier.background(VanImageColors.PlaceholderBackground),
        contentAlignment = Alignment.Center
    ) {
        // 【核心修复】判断类型
        if (src is ImageVector) {
            // A. 如果是矢量图 (ImageVector)，直接显示，无需网络加载
            Image(
                imageVector = src,
                contentDescription = alt,
                modifier = Modifier.fillMaxSize(),
                contentScale = contentScale
            )
        } else {
            // B. 其他类型 (Url, File, ResId)，使用 Coil 加载
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(context)
                    .data(src)
                    .crossfade(true)
                    .build(),
                contentDescription = alt,
                modifier = Modifier.fillMaxSize(),
                contentScale = contentScale,
            ) {
                val state = painter.state
                when (state) {
                    is AsyncImagePainter.State.Loading -> {
                        if (showLoading) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                if (loadingIcon != null) {
                                    loadingIcon()
                                } else {
                                    Icon(
                                        imageVector = Icons.Outlined.AccountBox,
                                        contentDescription = "Loading",
                                        tint = VanImageColors.PlaceholderIcon,
                                        modifier = Modifier.size(iconSize)
                                    )
                                }
                            }
                        }
                    }
                    is AsyncImagePainter.State.Error -> {
                        if (showError) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                if (errorIcon != null) {
                                    errorIcon()
                                } else {
                                    Icon(
                                        imageVector = Icons.Outlined.Info,
                                        contentDescription = "Error",
                                        tint = VanImageColors.PlaceholderIcon,
                                        modifier = Modifier.size(iconSize)
                                    )
                                }
                            }
                        }
                    }
                    else -> {
                        // Success 或 Empty 状态
                        SubcomposeAsyncImageContent()
                    }
                }
            }
        }
    }
}