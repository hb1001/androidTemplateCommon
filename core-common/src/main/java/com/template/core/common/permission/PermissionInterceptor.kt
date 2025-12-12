package com.template.core.common.permission


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.text.TextUtils
import android.widget.Toast
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.OnPermissionInterceptor
import com.hjq.permissions.XXPermissions
import com.hjq.permissions.permission.PermissionGroups
import com.hjq.permissions.permission.PermissionNames
import com.hjq.permissions.permission.base.IPermission

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/XXPermissions
 * time   : 2021/01/04
 * desc   : 权限申请拦截器
 */
class PermissionInterceptor : OnPermissionInterceptor {
    public override fun onRequestPermissionEnd(
        activity: Activity, skipRequest: Boolean,
        requestList: MutableList<IPermission?>,
        grantedList: MutableList<IPermission?>,
        deniedList: MutableList<IPermission>,
        callback: OnPermissionCallback?
    ) {
        if (callback != null) {
            callback.onResult(grantedList, deniedList)
        }

        if (deniedList.isEmpty()) {
            return
        }
        val doNotAskAgain: Boolean = XXPermissions.isDoNotAskAgainPermissions(activity, deniedList)
        val permissionHint = generatePermissionHint(activity, deniedList, doNotAskAgain)
        if (!doNotAskAgain) {
            // 如果没有勾选不再询问选项，就弹 Toast 提示给用户
            Toast.makeText(activity, permissionHint, Toast.LENGTH_SHORT).show()
            return
        }

        // 如果勾选了不再询问选项，就弹 Dialog 引导用户去授权
        showPermissionSettingDialog(activity, requestList, deniedList, callback, permissionHint)
    }

    private fun showPermissionSettingDialog(
        activity: Activity,
        requestList: MutableList<IPermission?>,
        deniedList: MutableList<IPermission>,
        callback: OnPermissionCallback?,
        permissionHint: String
    ) {
        if (activity.isFinishing || activity.isDestroyed) {
            return
        }

        // 弹框
        PermissionDescription.delegate?.showGoSettingDialog(
            activity,
            permissionHint,
            {
                // 用户点击确定，引导用户去授权
                XXPermissions.startPermissionActivity(activity, deniedList)
            }
        )

//        val dialog = AlertDialog.Builder(activity)
//            .setTitle("提示")
//            .setMessage(permissionHint)
//            .setPositiveButton("去授权") { _: DialogInterface?, _: Int ->
//                // 引导用户去授权
//                XXPermissions.startPermissionActivity(activity, deniedList)
//            }
//            .setNegativeButton("取消") { _: DialogInterface?, _: Int ->
//                // 如果用户取消授权，就回调 OnPermissionCallback 接口
//                if (callback != null) {
//                    callback.onResult(requestList, deniedList)
//                }
//            }
//        dialog.show()
  }

    /**
     * 生成权限提示文案
     */
    private fun generatePermissionHint(
        activity: Activity,
        deniedList: MutableList<IPermission>,
        doNotAskAgain: Boolean
    ): String {
        return PermissionConfig.getDialogMessage(deniedList[0].permissionName)
    }
}