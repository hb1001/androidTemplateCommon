package com.template.core.ui.vant

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
    val PlaceholderIcon = Color(0xFFDCDEE0) // Gray-4
    val PlaceholderText = Color(0xFF969799) // Gray-6
}

// --- 组件实现 ---

/**
 * VanImage - 图片组件
 * 基于 AsyncImage 封装，支持 Vant 风格的 fit, round, radius 以及自定义 loading/error 插槽
 *
 * @param src 图片链接
 * @param modifier 修饰符
 * @param fit 填充模式
 * @param alt 替代文本
 * @param width 宽度
 * @param height 高度
 * @param radius 圆角大小
 * @param round 是否显示为圆形
 * @param showError 是否展示加载失败提示
 * @param showLoading 是否展示加载中提示
 * @param errorIcon 失败时提示的图标/内容 (Composable)
 * @param loadingIcon 加载时提示的图标/内容 (Composable)
 * @param iconSize 加载/失败图标的大小
 * @param onClick 点击事件
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

    // 1. 处理尺寸
    var finalModifier = modifier
    if (width != null) finalModifier = finalModifier.width(width)
    if (height != null) finalModifier = finalModifier.height(height)

    // 2. 处理点击
    if (onClick != null) {
        finalModifier = finalModifier.clickable(onClick = onClick)
    }

    // 3. 处理圆角/形状
    val shape: Shape = if (round) CircleShape else RoundedCornerShape(radius)
    finalModifier = finalModifier.clip(shape)

    // 4. 处理填充模式
    val contentScale = when (fit) {
        VanImageFit.Contain -> ContentScale.Fit
        VanImageFit.Cover -> ContentScale.Crop
        VanImageFit.Fill -> ContentScale.FillBounds
        VanImageFit.None -> ContentScale.None
        VanImageFit.ScaleDown -> ContentScale.Inside
    }

    Box(
        modifier = finalModifier.background(VanImageColors.PlaceholderBackground),
        contentAlignment = Alignment.Center
    ) {
        // 【核心修复】使用 SubcomposeAsyncImage 代替 AsyncImage + 手动状态
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

            // 根据 Coil 的内部状态决定显示什么
            when (state) {
                is AsyncImagePainter.State.Loading -> {
                    if (showLoading) {
                        // 显示加载中占位符
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
                        // 显示错误占位符
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
                    // Success 或 Empty 状态，显示图片内容
                    SubcomposeAsyncImageContent()
                }
            }
        }
    }
}