package com.template.core.ui.vant

import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder

/**
 * VanIcon - 图标组件 (Coil SVG 版)
 *
 * @param name 图标名称 (如 "location-o") 或完整 URL。
 *             如果是名称，默认从 assets/icons/ 目录下加载。
 * @param modifier 修饰符
 * @param size 图标大小 (默认 24dp)
 * @param color 图标颜色 (Tint)。如果不传，默认跟随当前文本颜色。
 * @param spin 是否开启旋转动画
 * @param rotate 图标静态旋转角度
 * @param directory Assets 中的子目录，默认为 "icons"
 * @param onClick 点击回调
 */
@Composable
fun VanIcon(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp, // Vant 默认大小
    color: Color = Color.Unspecified,
    spin: Boolean = false,
    rotate: Float = 0f,
    directory: String = "icons",
    onClick: (() -> Unit)? = null
) {
    val context = LocalContext.current

    // 1. 构建 ImageLoader (支持 SVG)
    // 建议在 Application 全局配置，这里为了组件独立性进行局部配置
    val imageLoader = remember(context) {
        ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
    }

    // 2. 构建 Model (路径)
    val model = remember(name, directory) {
        if (name.startsWith("http") || name.startsWith("file://")) {
            name
        } else {
            // 默认加载 assets/icons/ 下的 .svg 文件
            "file:///android_asset/$directory/$name.svg"
        }
    }

    // 3. 旋转动画逻辑
    val currentRotate = if (spin) {
        val infiniteTransition = rememberInfiniteTransition(label = "VanIconSpin")
        val angle by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "VanIconSpinAngle"
        )
        angle
    } else {
        rotate
    }

    // 4. 点击与交互
    val clickModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick,
            role = Role.Image
        )
    } else {
        Modifier
    }

    // 5. 渲染
    // 使用 Icon 组件来渲染 Painter，这样可以利用 Icon 的 tint 机制实现 SVG 变色
    Box(
        modifier = modifier
            .size(size)
            .then(clickModifier)
            .rotate(currentRotate),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = rememberAsyncImagePainter(model = model, imageLoader = imageLoader),
            contentDescription = name,
            // 关键：如果传入了 color，使用该 color；否则使用 LocalContentColor (跟随文本)
            tint = if (color != Color.Unspecified) color else LocalContentColor.current
        )
    }
}