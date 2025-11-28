package com.template.core.ui.vant

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

// --- 常量与枚举 ---

object VanTypographyColors {
    val Primary = Color(0xFF1989FA)
    val Success = Color(0xFF07C160)
    val Danger = Color(0xFFEE0A24)
    val Warning = Color(0xFFFF976A)
    val Text = Color(0xFF323233)     // 主要文字
    val Secondary = Color(0xFF969799) // 次要文字/Gray-6
    val Disabled = Color(0xFFC8C9CC)  // Gray-5
    val Light = Color.White
    val Link = Color(0xFF576B95)      // 链接色
}

enum class VanTypographyType {
    Default, Primary, Success, Danger, Warning, Secondary, Light
}

enum class VanTypographySize(val fontSize: TextUnit) {
    XS(10.sp),
    SM(12.sp),
    MD(14.sp), // Default
    LG(16.sp),
    XL(20.sp),
    XXL(24.sp)
}

enum class VanTypographyLevel(val fontSize: TextUnit, val fontWeight: FontWeight) {
    L1(32.sp, FontWeight.Bold), // React Vant 默认 30px-32px
    L2(26.sp, FontWeight.Bold),
    L3(22.sp, FontWeight.Bold),
    L4(20.sp, FontWeight.Bold),
    L5(16.sp, FontWeight.Bold),
    L6(14.sp, FontWeight.Bold)
}

/**
 * 省略配置
 * @param rows 省略行数
 * @param expandText 展开操作的文案
 * @param collapseText 收起操作的文案
 * @param suffixText 添加后缀文本
 * @param symbol 省略符号
 * @param suffixCount 保留末位文本数量 (用于文件名等场景)
 */
data class VanEllipsisConfig(
    val rows: Int = 1,
    val expandText: String? = null,
    val collapseText: String? = null,
    val suffixText: String? = null,
    val symbol: String = "...",
    val suffixCount: Int = 0
)

// --- 核心组件 ---

/**
 * VanTypography (Text)
 * 对应 React Vant 的 Typography.Text
 */
@Composable
fun VanTypography(
    text: String,
    modifier: Modifier = Modifier,
    type: VanTypographyType = VanTypographyType.Default,
    size: VanTypographySize = VanTypographySize.MD,
    disabled: Boolean = false,
    delete: Boolean = false,
    underline: Boolean = false,
    strong: Boolean = false,
    center: Boolean = false,
    ellipsis: VanEllipsisConfig? = null,
    onClick: (() -> Unit)? = null,
) {
    val textColor = getTypographyColor(type, disabled)
    val textDecoration = when {
        delete -> TextDecoration.LineThrough
        underline -> TextDecoration.Underline
        else -> null
    }
    val fontWeight = if (strong) FontWeight.Bold else FontWeight.Normal
    val textAlign = if (center) TextAlign.Center else TextAlign.Start

    val style = LocalTextStyle.current.copy(
        color = textColor,
        fontSize = size.fontSize,
        fontWeight = fontWeight,
        textDecoration = textDecoration,
        textAlign = textAlign
    )

    if (ellipsis == null) {
        // 普通文本
        Text(
            text = text,
            modifier = modifier.clickable(
                enabled = onClick != null,
                indication = null, // 文本通常无水波纹，除非是 Link
                interactionSource = remember { MutableInteractionSource() },
                onClick = { onClick?.invoke() }
            ),
            style = style
        )
    } else {
        // 带省略逻辑的文本
        VanEllipsisText(
            fullText = text,
            style = style,
            config = ellipsis,
            modifier = modifier,
            onClick = onClick
        )
    }
}

/**
 * 支持 AnnotatedString 的版本 (用于嵌套样式)
 */
@Composable
fun VanTypography(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    type: VanTypographyType = VanTypographyType.Default,
    size: VanTypographySize = VanTypographySize.MD,
    center: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    // AnnotatedString 通常包含了具体的 SpanStyle，这里主要控制容器层级属性
    val textColor = getTypographyColor(type, false)
    val textAlign = if (center) TextAlign.Center else TextAlign.Start

    Text(
        text = text,
        modifier = modifier.clickable(
            enabled = onClick != null,
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = { onClick?.invoke() }
        ),
        color = textColor,
        fontSize = size.fontSize,
        textAlign = textAlign
    )
}

/**
 * VanTitle
 * 对应 React Vant 的 Typography.Title
 */
@Composable
fun VanTitle(
    text: String,
    modifier: Modifier = Modifier,
    level: VanTypographyLevel = VanTypographyLevel.L5,
    type: VanTypographyType = VanTypographyType.Default,
    ellipsis: Boolean = false, // 标题通常只需要简单的单行省略
    onClick: (() -> Unit)? = null,
) {
    val textColor = getTypographyColor(type, false)

    Text(
        text = text,
        modifier = modifier.clickable(
            enabled = onClick != null,
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = { onClick?.invoke() }
        ),
        color = textColor,
        fontSize = level.fontSize,
        fontWeight = level.fontWeight,
        maxLines = if (ellipsis) 1 else Int.MAX_VALUE,
        overflow = if (ellipsis) TextOverflow.Ellipsis else TextOverflow.Clip
    )
}

/**
 * VanLink
 * 对应 React Vant 的 Typography.Link
 */
@Composable
fun VanLink(
    text: String,
    modifier: Modifier = Modifier,
    url: String? = null,
    underline: Boolean = false, // Vant Link 默认没有下划线，hover 才有，移动端通常无
    onClick: (() -> Unit)? = null
) {
    val uriHandler = LocalUriHandler.current

    Text(
        text = text,
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            if (url != null) {
                try { uriHandler.openUri(url) } catch (e: Exception) { /* ignore */ }
            }
            onClick?.invoke()
        },
        color = VanTypographyColors.Link,
        fontSize = VanTypographySize.MD.fontSize,
        textDecoration = if (underline) TextDecoration.Underline else null
    )
}

// --- 内部逻辑实现 ---

@Composable
private fun getTypographyColor(type: VanTypographyType, disabled: Boolean): Color {
    if (disabled) return VanTypographyColors.Disabled
    return when (type) {
        VanTypographyType.Primary -> VanTypographyColors.Primary
        VanTypographyType.Success -> VanTypographyColors.Success
        VanTypographyType.Danger -> VanTypographyColors.Danger
        VanTypographyType.Warning -> VanTypographyColors.Warning
        VanTypographyType.Secondary -> VanTypographyColors.Secondary
        VanTypographyType.Light -> VanTypographyColors.Light
        else -> VanTypographyColors.Text
    }
}

/**
 * 复杂省略文本组件
 * 实现逻辑：使用 TextMeasurer 测量文本，计算截断位置，手动拼接 "..." + "展开"
 */
@Composable
private fun VanEllipsisText(
    fullText: String,
    style: TextStyle,
    config: VanEllipsisConfig,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    var isExpanded by remember { mutableStateOf(false) }
    val textMeasurer = rememberTextMeasurer()

    // 状态：需要重新计算显示的文本
    var displayedText by remember { mutableStateOf<AnnotatedString?>(null) }

    // 触发测量与计算
    LaunchedEffect(fullText, style, config, isExpanded,  modifier) {
        if (isExpanded) {
            // 展开状态：显示全文 + 收起按钮
            val builder = AnnotatedString.Builder(fullText)
            if (!config.collapseText.isNullOrEmpty()) {
                builder.append(" ")
                builder.pushStringAnnotation(tag = "ACTION", annotation = "COLLAPSE")
                builder.withStyle(SpanStyle(color = VanTypographyColors.Primary)) {
                    append(config.collapseText)
                }
                builder.pop()
            }
            displayedText = builder.toAnnotatedString()
        } else {
            // 收起状态：需要计算截断
            val measureResult = textMeasurer.measure(
                text = fullText,
                style = style
            )

            // 检查行数是否超过限制
            // 注意：MeasureResult 默认是无限宽度的，除非我们在 layout 中限制，或者传入 constraints。
            // 这里为了简化，我们依赖于 Compose 的 BoxWithConstraints 传入 constraints 会更准，
            // 但简单的近似算法是：如果不换行，measureResult.lineCount 总是 1 (如果 width 是 Int.MAX_VALUE)。
            // 因此我们需要在 UI 渲染层获取 constraints。
            // 这是一个经典的 Chicken-Egg 问题。
            // 解决方案：使用 SubcomposeLayout 或者 BoxWithConstraints 在外部包裹。
            // 为了代码简洁，我们假定这里会被 BoxWithConstraints 包裹，或者我们在 BoxWithConstraints 内部调用。
        }
    }

    // 使用 BoxWithConstraints 获取最大宽度以进行精确测量
    androidx.compose.foundation.layout.BoxWithConstraints(modifier = modifier) {
        val constraints = constraints

        // 实时计算
        val resultText = remember(fullText, constraints.maxWidth, isExpanded, config, style) {
            val measuredResult = textMeasurer.measure(
                text = AnnotatedString(fullText),
                style = style,
                constraints = constraints
            )

            // 如果没超过行数，直接显示
            if (measuredResult.lineCount <= config.rows) {
                AnnotatedString(fullText)
            } else {
                if (isExpanded) {
                    // 展开逻辑同上
                    val builder = AnnotatedString.Builder(fullText)
                    if (!config.collapseText.isNullOrEmpty()) {
                        builder.append(" ")
                        builder.pushStringAnnotation(tag = "ACTION", annotation = "COLLAPSE")
                        builder.withStyle(SpanStyle(color = VanTypographyColors.Primary)) {
                            append(config.collapseText)
                        }
                        builder.pop()
                    }
                    builder.toAnnotatedString()
                } else {
                    // --- 核心截断逻辑 ---

                    // 1. 准备后缀 (Expand Text + Suffix Text)
                    val actionText = config.expandText ?: ""
                    val suffixConfigText = config.suffixText ?: "" // e.g. "--William"
                    val symbol = config.symbol // "..."

                    // 2. 如果配置了 suffixCount (保留末尾N个字符)，逻辑优先
                    val tailText = if (config.suffixCount > 0 && fullText.length > config.suffixCount) {
                        fullText.takeLast(config.suffixCount)
                    } else {
                        ""
                    }

                    // 3. 构造 "虚拟后缀" 用于测量占位: "...(尾部) (后缀) 展开"
                    val ellipsisContent = "$symbol$tailText$suffixConfigText $actionText"

                    // 4. 找到第 N 行的结束偏移量
                    val lineIndex = config.rows - 1
                    val lineEndOffset = measuredResult.getLineEnd(lineIndex, visibleEnd = true)

                    // 5. 尝试二分查找或逐步回退，直到 "前缀 + 虚拟后缀" 不超过行数/宽度
                    // 简单做法：从 lineEndOffset 开始回退，减去 virtualSuffix 的宽度

                    val suffixWidth = textMeasurer.measure(ellipsisContent, style).size.width
                    var cutIndex = lineEndOffset

                    // 防止越界
                    if (cutIndex > fullText.length) cutIndex = fullText.length

                    // 快速估算：根据字符平均宽度回退
                    // 这里的计算非常复杂，为了 Demo 效果，我们做一个简单的回退逻辑：
                    // 回退字符数 = (后缀宽度 / 最后一行的宽度) * 字符数 + 安全余量

                    // 更精确的做法是循环测量，但性能较差。我们采用 String 截取测试。

                    var candidate = fullText.substring(0, cutIndex)
                    // 强制回退一部分空间给后缀
                    // 假设一个字符大概占 width/chars
                    val lastLineWidth = measuredResult.getLineRight(lineIndex) - measuredResult.getLineLeft(lineIndex)
                    val charWidthAvg = if(candidate.isNotEmpty()) lastLineWidth / candidate.length else 10f
                    val charsToRemove = (suffixWidth / charWidthAvg).toInt() + 2 // +2 安全余量

                    cutIndex = (cutIndex - charsToRemove).coerceAtLeast(0)

                    // 构建最终字符串
                    val builder = AnnotatedString.Builder()

                    // 前半部分 (处理 suffixCount 逻辑：如果是中间省略)
                    val headText = if (config.suffixCount > 0) {
                        fullText.take((fullText.length - config.suffixCount - charsToRemove).coerceAtLeast(0))
                    } else {
                        fullText.substring(0, cutIndex)
                    }

                    builder.append(headText)
                    builder.append(symbol)

                    // 后半部分 (SuffixCount 保留的尾部)
                    if (tailText.isNotEmpty()) {
                        builder.append(tailText)
                    }

                    // 配置的 suffixText
                    if (suffixConfigText.isNotEmpty()) {
                        builder.append(suffixConfigText)
                    }

                    // 展开按钮
                    if (actionText.isNotEmpty()) {
                        builder.append(" ")
                        builder.pushStringAnnotation(tag = "ACTION", annotation = "EXPAND")
                        builder.withStyle(SpanStyle(color = VanTypographyColors.Primary)) {
                            append(actionText)
                        }
                        builder.pop()
                    }

                    builder.toAnnotatedString()
                }
            }
        }

        // 渲染文本
        androidx.compose.foundation.text.ClickableText(
            text = resultText,
            style = style,
            onClick = { offset ->
                // 检查是否点击了展开/收起
                resultText.getStringAnnotations(tag = "ACTION", start = offset, end = offset)
                    .firstOrNull()?.let { annotation ->
                        if (annotation.item == "EXPAND") {
                            isExpanded = true
                        } else if (annotation.item == "COLLAPSE") {
                            isExpanded = false
                        }
                    } ?: onClick?.invoke() // 如果没点到 Action，触发整体点击
            }
        )
    }
}