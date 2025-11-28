package com.template.core.ui.vant

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- 数据模型 ---

data class VanShareOption(
    val name: String,
    val icon: Any, // String (URL/Name) or @Composable or ImageVector
    val description: String? = null,
    val className: String? = null // 保留字段，Compose 中可能不太需要
)

// --- 颜色常量 ---
object VanShareSheetColors {
    val Title = Color(0xFF323233)
    val Description = Color(0xFF969799)
    val OptionName = Color(0xFF646566)
    val OptionDescription = Color(0xFFC8C9CC)
    val CancelButton = Color(0xFF323233)
    val CancelBackground = Color.White
    val Background = Color(0xFFF7F8FA) // 面板背景色
}

// --- 组件实现 ---

/**
 * VanShareSheet - 分享面板
 *
 * @param visible 是否显示
 * @param onCancel 点击取消或遮罩层触发
 * @param onSelect 点击选项触发
 * @param options 分享选项 (支持单层 List 或双层 List)
 * @param title 顶部标题
 * @param description 辅助描述文字
 * @param cancelText 取消按钮文字
 * @param overlay 是否显示遮罩
 * @param closeOnClickOverlay 点击遮罩关闭
 * @param safeAreaInsetBottom 底部安全区适配
 */
@Composable
fun VanShareSheet(
    visible: Boolean,
    onCancel: () -> Unit,
    options: List<Any>, // List<VanShareOption> or List<List<VanShareOption>>
    modifier: Modifier = Modifier,
    onSelect: ((VanShareOption, Int) -> Unit)? = null,
    title: String? = null,
    description: String? = null,
    cancelText: String? = "取消",
    overlay: Boolean = true,
    closeOnClickOverlay: Boolean = true,
    safeAreaInsetBottom: Boolean = true
) {
    // 数据标准化：统一转为 List<List<VanShareOption>>
    val normalizedOptions: List<List<VanShareOption>> = if (options.isNotEmpty()) {
        if (options.first() is List<*>) {
            @Suppress("UNCHECKED_CAST")
            options as List<List<VanShareOption>>
        } else {
            @Suppress("UNCHECKED_CAST")
            listOf(options as List<VanShareOption>)
        }
    } else {
        emptyList()
    }

    VanPopup(
        visible = visible,
        onClose = onCancel,
        position = VanPopupPosition.Bottom,
        round = true,
        overlay = overlay,
        closeOnClickOverlay = closeOnClickOverlay,
        safeAreaInsetBottom = safeAreaInsetBottom
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(VanShareSheetColors.Background) // 整体背景
        ) {
            // 1. Header (Title & Description)
            if (title != null || description != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 16.dp), // Vant 默认 padding
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (title != null) {
                        Text(
                            text = title,
                            color = VanShareSheetColors.Title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    if (description != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = description,
                            color = VanShareSheetColors.Description,
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            // 2. Options Rows
            normalizedOptions.forEachIndexed { rowIndex, rowOptions ->
                // 行容器
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(vertical = 16.dp)
                        .horizontalScroll(rememberScrollState()), // 支持横向滚动
                    horizontalArrangement = if (rowOptions.size <= 4) Arrangement.SpaceAround else Arrangement.Start // 少于4个均分，多于4个左对齐滚动
                ) {
                    rowOptions.forEachIndexed { index, option ->
                        ShareOptionItem(
                            option = option,
                            onClick = { onSelect?.invoke(option, index) }, // 这里 index 是当前行的 index，如果需要全局 index 需要累加
                            modifier = Modifier.padding(horizontal = if (rowOptions.size > 4) 12.dp else 0.dp) // 滚动模式下给点间距
                        )
                    }
                }

                // 如果有多行，添加分割线 (但在 Vant 好像是直接堆叠，中间没有明显分割线，或者是 padding)
                // 这里为了美观，如果是多行，中间不加分割线，而是靠 padding 分开
                if (rowIndex < normalizedOptions.size - 1) {
                    Divider(color = Color(0xFFF7F8FA), thickness = 8.dp) // 用背景色做分割
                }
            }

            // 3. Cancel Button
            if (!cancelText.isNullOrEmpty()) {
                // 分割块
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(VanShareSheetColors.Background)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(VanShareSheetColors.CancelBackground)
                        .clickable(onClick = onCancel),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = cancelText,
                        fontSize = 16.sp,
                        color = VanShareSheetColors.CancelButton
                    )
                }
            }
        }
    }
}

@Composable
private fun ShareOptionItem(
    option: VanShareOption,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(80.dp) // 固定项宽度
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icon 容器
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp)) // Vant 图标通常是圆角或圆形，这里用圆角矩形或圆形
                .background(Color(0xFFF2F3F5)), // 灰色背景底
            contentAlignment = Alignment.Center
        ) {
            when (val icon = option.icon) {
                is String -> {
                    // 内置图标名称映射 (简化版，实际项目可以做全量映射)
                    if (icon.startsWith("http")) {
                        VanImage(src = icon, width = 32.dp, height = 32.dp, fit = VanImageFit.Contain)
                    } else {
                        // 假设是 "wechat", "weibo" 等名称
                        // 这里使用 VanIcon 加载 (需要 assets/icons/wechat.svg 等)
                        // 或者使用预置的 Icon 映射
                        val iconName = when(icon) {
                            "wechat" -> "wechat" // 需 assets/icons/wechat.svg
                            "wechat-moments" -> "wechat-moments"
                            "weibo" -> "weibo"
                            "qq" -> "qq"
                            "link" -> "link"
                            "poster" -> "poster"
                            "qrcode" -> "qrcode"
                            else -> icon
                        }

                        // 尝试加载 svg
                        VanIcon(name = iconName, size = 32.dp)

                        // Fallback: 如果没有 svg，可以临时显示首字母
                        // Text(icon.take(1).uppercase())
                    }
                }
//                is androidx.compose.ui.graphics.vector.ImageVector -> {
//                    VanIcon(name = icon, size = 32.dp)
//                }
//                is @Composable () -> Unit -> {
//                    icon()
//                }
                else -> {
                    // Fallback
                    VanIcon(name = "PhoneO", size = 32.dp)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Name
        Text(
            text = option.name,
            fontSize = 12.sp,
            color = VanShareSheetColors.OptionName,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // Description
        if (option.description != null) {
            Text(
                text = option.description,
                fontSize = 12.sp,
                color = VanShareSheetColors.OptionDescription,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}