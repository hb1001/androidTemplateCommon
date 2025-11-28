package com.template.core.ui.vant

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- 数据模型 ---
data class VanAction(
    val name: String? = null,
    val subname: String? = null,
    val color: Color = Color.Unspecified,
    val loading: Boolean = false,
    val disabled: Boolean = false,
    val callback: (() -> Unit)? = null
)

// --- 颜色常量 ---
object VanActionSheetColors {
    val Background = Color(0xFFF7F8FA) // 分割线的背景色，或者 Gap 颜色
    val ItemBackground = Color.White
    val ItemText = Color(0xFF323233)
    val SubnameText = Color(0xFF969799)
    val DescriptionText = Color(0xFF969799)
    val DisabledText = Color(0xFFC8C9CC)
    val CancelText = Color(0xFF646566)
    val PressedBackground = Color(0xFFF2F3F5)
}

// --- 组件实现 ---

/**
 * VanActionSheet - 动作面板
 *
 * @param visible 是否显示
 * @param onCancel 取消/关闭回调
 * @param actions 选项列表
 * @param title 顶部标题 (如果有标题，则为 Custom Panel 模式或 Header 模式)
 * @param description 选项上方的描述信息
 * @param cancelText 取消按钮文案
 * @param closeable 是否显示关闭图标 (仅在有 title 时生效)
 * @param onSelect 选中选项回调
 * @param content 自定义内容插槽 (如果有 content，则 actions 失效或显示在 actions 之下)
 */
@Composable
fun VanActionSheet(
    visible: Boolean,
    onCancel: () -> Unit, // 对应 onCancel & onClose
    modifier: Modifier = Modifier,
    actions: List<VanAction> = emptyList(),
    title: String? = null,
    description: String? = null,
    cancelText: String? = null,
    closeable: Boolean = true,
    round: Boolean = true,
    onSelect: ((VanAction, Int) -> Unit)? = null,
    content: (@Composable () -> Unit)? = null
) {
    VanPopup(
        visible = visible,
        onClose = onCancel,
        position = VanPopupPosition.Bottom,
        round = round,
        safeAreaInsetBottom = true, // 底部安全区
        // 标题栏逻辑复用 Popup 的 title，但 ActionSheet 的 title 样式可能微调，这里用 Popup 自带的够用了
        title = title,
        closeable = closeable && !title.isNullOrEmpty(), // Vant 逻辑：有 title 时 closeable 才生效
        // 描述信息复用 Popup 的 description ?
        // Vant ActionSheet 的 description 是在 actions 上方，且不仅限于 Title 下方。
        // 如果没有 Title，Description 依然显示。
        // VanPopup 的 description 是紧跟 Title 的。
        // 这里我们可以传给 VanPopup，或者自己渲染。
        // 为了灵活性，我们自己渲染 Description，不传给 VanPopup。
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(VanActionSheetColors.Background) // 整体背景灰，利用 gap 做分割线效果
        ) {
            // 1. Description (如果没有 Title，Description 显示在最上面)
            // 如果有 Title，VanPopup 已经渲染了 Title。
            // 这里我们需要渲染 Description。
            if (!description.isNullOrEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(VanActionSheetColors.ItemBackground)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = description,
                        color = VanActionSheetColors.DescriptionText,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }
                // 分割线
                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEBEDF0))
            }

            // 2. 自定义内容
            if (content != null) {
                Box(modifier = Modifier.background(VanActionSheetColors.ItemBackground)) {
                    content()
                }
            }

            // 3. Actions List
            if (actions.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.background(VanActionSheetColors.ItemBackground)
                ) {
                    itemsIndexed(actions) { index, action ->
                        ActionItem(
                            action = action,
                            onClick = {
                                if (!action.disabled && !action.loading) {
                                    action.callback?.invoke()
                                    onSelect?.invoke(action, index)
                                    // 注意：ActionSheet 点击选项默认不自动关闭，通常由 onSelect 触发 setVisible(false)
                                    // Vant React: closeOnClickAction prop default false.
                                    // 这里我们把控制权交给调用者，或者调用者在 onSelect 里关闭。
                                }
                            }
                        )
                        if (index < actions.lastIndex) {
                            HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEBEDF0))
                        }
                    }
                }
            }

            // 4. Cancel Button (Gap above)
            if (!cancelText.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(8.dp)) // 灰色间隔
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(VanActionSheetColors.ItemBackground)
                        .clickable(onClick = onCancel),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = cancelText,
                        color = VanActionSheetColors.CancelText,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionItem(
    action: VanAction,
    onClick: () -> Unit
) {
    val textColor = if (action.disabled) VanActionSheetColors.DisabledText else
        if (action.color != Color.Unspecified) action.color else VanActionSheetColors.ItemText

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp) // 最小高度
            .clickable(enabled = !action.disabled && !action.loading, onClick = onClick)
            .padding(vertical = 14.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (action.loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = VanActionSheetColors.SubnameText,
                strokeWidth = 2.dp
            )
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = action.name ?: "",
                    fontSize = 16.sp,
                    color = textColor,
                    textAlign = TextAlign.Center
                )
                if (!action.subname.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = action.subname,
                        fontSize = 12.sp,
                        color = VanActionSheetColors.SubnameText,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}