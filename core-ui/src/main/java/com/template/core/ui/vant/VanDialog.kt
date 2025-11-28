package com.template.core.ui.vant

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// --- 常量 ---
object VanDialogColors {
    val Background = Color.White
    val Title = Color(0xFF323233)
    val Message = Color(0xFF646566)
    val ConfirmButtonText = Color(0xFFEE0A24) // Danger Red
    val CancelButtonText = Color(0xFF323233)
    val Divider = Color(0xFFEBEDF0)
}

enum class VanDialogTheme {
    Default, RoundButton
}

enum class VanDialogMessageAlign {
    Left, Center, Right
}

// --- 组件式调用 ---

/**
 * VanDialog - 基础弹窗组件
 */
@Composable
fun VanDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit, // 点击遮罩或返回键关闭
    title: String? = null,
    message: String? = null,
    messageAlign: VanDialogMessageAlign = VanDialogMessageAlign.Center,
    theme: VanDialogTheme = VanDialogTheme.Default,
    showConfirmButton: Boolean = true,
    showCancelButton: Boolean = false,
    confirmButtonText: String = "确认",
    confirmButtonColor: Color = VanDialogColors.ConfirmButtonText,
    cancelButtonText: String = "取消",
    cancelButtonColor: Color = VanDialogColors.CancelButtonText,
    closeable: Boolean = false,
    width: Dp = 320.dp,
    onConfirm: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
    content: (@Composable () -> Unit)? = null // 自定义内容插槽
) {
    if (visible) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(usePlatformDefaultWidth = false) // 允许自定义宽度
        ) {
            Box(
                modifier = Modifier
                    .width(width)
                    .clip(RoundedCornerShape(16.dp))
                    .background(VanDialogColors.Background)
            ) {
                Column {
                    // 1. 标题
                    if (title != null) {
                        Text(
                            text = title,
                            color = VanDialogColors.Title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 26.dp, bottom = if (message == null && content == null) 26.dp else 0.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    // 2. 内容 (Message 或 Custom Content)
                    if (content != null) {
                        Box(modifier = Modifier.padding(24.dp)) {
                            content()
                        }
                    } else if (message != null) {
                        val textAlign = when (messageAlign) {
                            VanDialogMessageAlign.Left -> TextAlign.Start
                            VanDialogMessageAlign.Center -> TextAlign.Center
                            VanDialogMessageAlign.Right -> TextAlign.End
                        }
                        Text(
                            text = message,
                            color = if (title != null) VanDialogColors.Message else VanDialogColors.Title,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            textAlign = textAlign,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 24.dp,
                                    end = 24.dp,
                                    top = if (title != null) 8.dp else 24.dp,
                                    bottom = 24.dp
                                )
                        )
                    } else {
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // 3. 按钮区域
                    if (theme == VanDialogTheme.RoundButton) {
                        // 圆角按钮风格
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (showCancelButton) {
                                VanButton(
                                    text = cancelButtonText,
                                    type = VanButtonType.Default,
                                    round = true,
                                    modifier = Modifier.weight(1f),
                                    onClick = { onCancel?.invoke() }
                                )
                            }
                            if (showConfirmButton) {
                                VanButton(
                                    text = confirmButtonText,
                                    type = VanButtonType.Danger, // Vant 默认是 Danger
                                    round = true,
                                    modifier = Modifier.weight(1f),
                                    onClick = { onConfirm?.invoke() }
                                )
                            }
                        }
                    } else {
                        // 默认风格 (iOS 风格分割线)
                        Divider(color = VanDialogColors.Divider, thickness = 0.5.dp)
                        Row(
                            modifier = Modifier.height(48.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (showCancelButton) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .clickable { onCancel?.invoke() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = cancelButtonText, color = cancelButtonColor, fontSize = 16.sp)
                                }
                                // 垂直分割线
                                Box(
                                    modifier = Modifier
                                        .width(0.5.dp)
                                        .fillMaxHeight()
                                        .background(VanDialogColors.Divider)
                                )
                            }
                            if (showConfirmButton) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .clickable { onConfirm?.invoke() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = confirmButtonText, color = confirmButtonColor, fontSize = 16.sp)
                                }
                            }
                        }
                    }
                }

                // 4. 关闭按钮
                if (closeable) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(0xFFC8C9CC),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .size(20.dp)
                            .clickable { onCancel?.invoke() }
                    )
                }
            }
        }
    }
}

// --- 函数式调用支持 ---

data class DialogOptions(
    val title: String? = null,
    val message: String? = null,
    val theme: VanDialogTheme = VanDialogTheme.Default,
    val showCancelButton: Boolean = false,
    val confirmButtonText: String = "确认",
    val cancelButtonText: String = "取消",
    val closeable: Boolean = false,
    val content: (@Composable () -> Unit)? = null,
    // 异步回调
    val onConfirm: (() -> Unit)? = null,
    val onCancel: (() -> Unit)? = null,
    // 内部控制
    val dismissOnAction: Boolean = true // 点击按钮是否自动关闭
)

/**
 * 全局 Dialog 控制器
 */
class VanDialogController {
    var state by mutableStateOf<DialogOptions?>(null)
        private set

    // 用于 Promise/Suspend 调用
    private var confirmDeferred: CompletableDeferred<Boolean>? = null

    fun show(options: DialogOptions) {
        state = options
    }

    suspend fun alert(
        title: String? = null,
        message: String? = null,
        theme: VanDialogTheme = VanDialogTheme.Default
    ) {
        val deferred = CompletableDeferred<Boolean>()
        confirmDeferred = deferred

        show(
            DialogOptions(
                title = title,
                message = message,
                theme = theme,
                showCancelButton = false,
                onConfirm = { deferred.complete(true) },
                onCancel = { deferred.complete(false) } // Alert 通常只有确认，但防万一
            )
        )
        deferred.await()
        dismiss()
    }

    suspend fun confirm(
        title: String? = null,
        message: String? = null,
        theme: VanDialogTheme = VanDialogTheme.Default
    ): Boolean {
        val deferred = CompletableDeferred<Boolean>()
        confirmDeferred = deferred

        show(
            DialogOptions(
                title = title,
                message = message,
                theme = theme,
                showCancelButton = true,
                onConfirm = { deferred.complete(true) },
                onCancel = { deferred.complete(false) }
            )
        )
        // 等待结果
        val result = deferred.await()
        dismiss()
        if (!result) throw Exception("Cancel") // 模拟 Vant 的 Promise.reject
        return true
    }

    fun dismiss() {
        state = null
        confirmDeferred = null
    }
}

// CompositionLocal 用于在子组件中获取 Controller
val LocalVanDialog = compositionLocalOf { VanDialogController() }

/**
 * VanDialogProvider - 必须包裹在 App 的顶层
 */
@Composable
fun VanDialogProvider(
    content: @Composable () -> Unit
) {
    val controller = remember { VanDialogController() }

    CompositionLocalProvider(LocalVanDialog provides controller) {
        Box(Modifier.fillMaxSize()) {
            content()

            // 全局 Dialog 渲染
            val state = controller.state
            if (state != null) {
                VanDialog(
                    visible = true,
                    onDismissRequest = {
                        // 点击背景默认当作 Cancel
                        state.onCancel?.invoke()
                        controller.dismiss()
                    },
                    title = state.title,
                    message = state.message,
                    theme = state.theme,
                    showCancelButton = state.showCancelButton,
                    confirmButtonText = state.confirmButtonText,
                    cancelButtonText = state.cancelButtonText,
                    closeable = state.closeable,
                    onConfirm = {
                        state.onConfirm?.invoke()
                        if (state.dismissOnAction) controller.dismiss()
                    },
                    onCancel = {
                        state.onCancel?.invoke()
                        if (state.dismissOnAction) controller.dismiss()
                    },
                    content = state.content
                )
            }
        }
    }
}