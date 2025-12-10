package com.template.core.common.permission

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.DisplayMetrics
import android.view.WindowManager
import com.hjq.permissions.OnPermissionDescription
import com.hjq.permissions.permission.PermissionPageType
import com.hjq.permissions.permission.base.IPermission
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * 权限描述逻辑实现类
 * 特点：不包含任何 UI 代码，通过 Delegate 接口回调外部实现
 */
class PermissionDescription : OnPermissionDescription {

    /**
     * UI 行为代理接口
     * 在你的 Application 或初始化代码中设置这个 Delegate
     */
    interface PermissionUiDelegate {
        /**
         * 显示模态对话框 (Dialog 模式)
         * @param message 权限描述文本
         * @param onConfirm 用户点击确定后的回调
         */
        fun showDialog(activity: Activity, message: String, onConfirm: () -> Unit)

        /**
         * 显示顶部提示 (Popup 模式)
         * @param message 权限描述文本
         */
        fun showPopup(activity: Activity, message: String)

        /**
         * 销毁所有弹窗
         */
        fun dismiss(activity: Activity)
    }

    companion object {
        // 全局静态代理，由外部注入
        var delegate: PermissionUiDelegate? = null

        private val HANDLER = Handler(Looper.getMainLooper())
        private const val WINDOW_TYPE_DIALOG = 0
        private const val WINDOW_TYPE_POPUP = 1
    }

    private var descriptionWindowType = WINDOW_TYPE_DIALOG
    private val handlerToken = Any()

    override fun askWhetherRequestPermission(
        activity: Activity,
        requestList: List<IPermission>,
        continueRequestRunnable: Runnable,
        breakRequestRunnable: Runnable
    ) {
        // 1. 逻辑判断：决定使用 Dialog 还是 Popup
        val isLandscape = activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val isSmallScreen = activity.getPhysicalScreenSize() < 8.5
        val hasOpaquePage = requestList.any {
            it.getPermissionPageType(activity) == PermissionPageType.OPAQUE_ACTIVITY
        }

        descriptionWindowType = if (isLandscape && isSmallScreen) {
            WINDOW_TYPE_DIALOG
        } else if (hasOpaquePage) {
            WINDOW_TYPE_DIALOG
        } else {
            WINDOW_TYPE_POPUP
        }

        // 2. 如果是 Popup 模式，这里直接放行，稍后显示提示
        if (descriptionWindowType == WINDOW_TYPE_POPUP) {
            continueRequestRunnable.run()
            return
        }

        // 3. Dialog 模式：调用外部代理显示
        val description = generatePermissionDescription(activity, requestList)

        delegate?.showDialog(activity, description) {
            // 外部 UI 点击确定后，执行此回调
            continueRequestRunnable.run()
        }
    }

    override fun onRequestPermissionStart(activity: Activity, requestList: List<IPermission>) {
        if (descriptionWindowType != WINDOW_TYPE_POPUP) return

        val showPopupRunnable = Runnable {
            // Popup 模式：调用外部代理显示
            delegate?.showPopup(activity, generatePermissionDescription(activity, requestList))
        }
        // 延迟 350ms 显示
        HANDLER.postAtTime(showPopupRunnable, handlerToken, SystemClock.uptimeMillis() + 350)
    }

    override fun onRequestPermissionEnd(activity: Activity, requestList: List<IPermission>) {
        HANDLER.removeCallbacksAndMessages(handlerToken)
        delegate?.dismiss(activity)
    }

    private fun generatePermissionDescription(activity: Activity, requestList: List<IPermission>): String {
        // 尝试调用转换器，如果报错则返回默认文案 (防止类找不到)
        val info = PermissionConfig.get(requestList.get(0).permissionName)
        return "请授予${info.name}权限以${info.desc}"
    }

    private fun Context.getPhysicalScreenSize(): Double {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as? WindowManager ?: return 0.0
        val display = windowManager.defaultDisplay ?: return 0.0
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        val point = Point()
        display.getRealSize(point)
        val widthInches = point.x / metrics.xdpi
        val heightInches = point.y / metrics.ydpi
        return sqrt(widthInches.pow(2) + heightInches.pow(2).toDouble())
    }
}