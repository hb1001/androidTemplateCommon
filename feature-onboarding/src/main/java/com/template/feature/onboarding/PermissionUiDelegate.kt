package com.template.feature.onboarding
// 在你的 Application 初始化 或 MainActivity 中设置
import android.app.Activity
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import android.view.ViewGroup
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.core.common.permission.PermissionDescription
import com.template.core.ui.vant.VanPopup
import com.template.core.ui.vant.VanPopupPosition
import com.template.core.ui.vant.VanPopupColors

// 初始化方法
fun initPermissionDelegate() {
    val x  = object : PermissionDescription.PermissionUiDelegate {

        // 用来持有当前的 View，以便销毁
        private var currentOverlay: ComposeView? = null

        override fun showDialog(activity: Activity, message: String, onConfirm: () -> Unit) {
            showOverlay(activity) { dismissTrigger ->
                val visible = remember { mutableStateOf(true) }

                // 复用你的 VanPopup 作为 Dialog
                VanPopup(
                    visible = visible.value,
                    onClose = { /* 禁止点击背景关闭 */ },
                    position = VanPopupPosition.Center,
                    round = true,
                    title = "权限使用说明",
                    description = message,
                    overlay = true,
                    closeOnClickOverlay = false,
                    contentWidth = 300.dp
                ) {
                    Button(
                        onClick = {
                            dismissTrigger() // 关闭弹窗 View
                            onConfirm()      // 通知 XXPermissions 继续
                        },
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEE0A24))
                    ) {
                        Text("我知道了", fontSize = 16.sp)
                    }
                }
            }
        }

        override fun showPopup(activity: Activity, message: String) {
            showOverlay(activity) {
                val visible = remember { mutableStateOf(true) }

                // 复用你的 VanPopup 作为顶部提示
                VanPopup(
                    visible = visible.value,
                    onClose = { },
                    position = VanPopupPosition.Top,
                    overlay = false, // 无遮罩
                    round = false,
                    safeAreaInsetBottom = false
                ) {
                    //
                    Text(
                        text = message,
                        color = VanPopupColors.Title,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 12.dp)
                    )
                }
            }
        }

        override fun dismiss(activity: Activity) {
            currentOverlay?.let { view ->
                (view.parent as? ViewGroup)?.removeView(view)
                currentOverlay = null
            }
        }

        // 辅助方法：将 Compose 注入到 DecorView
        private fun showOverlay(activity: Activity, content: @Composable (dismiss: () -> Unit) -> Unit) {
            dismiss(activity) // 先清理旧的
            if (activity.isFinishing || activity.isDestroyed) return

            val decorView = activity.window.decorView as ViewGroup
            val view = ComposeView(activity).apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
                setContent {
                    content { dismiss(activity) }
                }
            }
            decorView.addView(view)
            currentOverlay = view
        }
    }
    PermissionDescription.delegate = x
}