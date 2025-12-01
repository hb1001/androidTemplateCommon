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
import coil.compose.AsyncImage
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

    // 5. 状态管理
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    // 不需要手动 LaunchedEffect(src)，AsyncImage 的 onLoading 会在请求开始时自动触发
    // 但为了保险起见（比如空链接直接报错），保留一个重置逻辑是好的，但要配合下方的回调
    LaunchedEffect(src) {
        // 重置状态，防止切换图片时残留之前的状态
        // 注意：Coil 加载非常快时，这里可能会和回调有竞态，
        // 但通常 AsyncImage 的 onLoading 会再次覆盖它，所以问题不大。
        // 最重要的是下面的 AsyncImage 回调修复。
        isError = false
        isLoading = true
    }

    Box(
        modifier = finalModifier.background(VanImageColors.PlaceholderBackground),
        contentAlignment = Alignment.Center
    ) {
        // 核心修改：将 listener 移除，改用 AsyncImage 的参数
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(src)
                .crossfade(true)
                .build(), // 这里构建的 Model 不再包含变动的 Listener Lambda，因此是稳定的
            contentDescription = alt,
            modifier = Modifier.fillMaxSize(),
            contentScale = contentScale,
            // 使用 Compose 版本的 Coil 回调，它们不会导致 Model 变化引起的重载
            onLoading = {
                isLoading = true
                isError = false
            },
            onSuccess = {
                isLoading = false
                isError = false
            },
            onError = {
                isLoading = false
                isError = true
            }
        )

        // 6. 遮罩层：加载中
        if (isLoading && showLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(VanImageColors.PlaceholderBackground),
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

        // 7. 遮罩层：加载失败
        if (isError && showError) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(VanImageColors.PlaceholderBackground),
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
}