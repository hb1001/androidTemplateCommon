package com.template.core.common.permission

import android.app.Activity
import android.widget.Toast
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.hjq.permissions.permission.PermissionLists
import com.hjq.permissions.permission.base.IPermission



fun requestTestPermission(activity: Activity){
    XXPermissions.with(activity)
        // 申请多个权限
//        .permission(PermissionLists.getRecordAudioPermission())
        .permission(PermissionLists.getCameraPermission())
        // 设置不触发错误检测机制（局部设置）
        //.unchecked()
        .interceptor(PermissionInterceptor())
        .description(PermissionDescription())
        .request(object : OnPermissionCallback {

            override fun onResult(grantedList: MutableList<IPermission>, deniedList: MutableList<IPermission>) {
                val allGranted = deniedList.isEmpty()
                if (!allGranted) {
                    // 判断请求失败的权限是否被用户勾选了不再询问的选项
                    val doNotAskAgain = XXPermissions.isDoNotAskAgainPermissions(activity, deniedList)
                    // 在这里处理权限请求失败的逻辑
                    // ......
//                    Toast.makeText(activity, "权限请求失败", Toast.LENGTH_SHORT).show()
                    return
                }
                // 在这里处理权限请求成功的逻辑
                // ......
//                showGrantedPermissionsToast(grantedList);
                Toast.makeText(activity, "成功获取权限", Toast.LENGTH_SHORT).show()

            }
        })
}