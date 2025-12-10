package com.template.core.common.permission

data class PermissionInfo(
    val name: String,        // 中文名
    val desc: String,        // 权限用途描述
    val category: String? = null  // 可选，例如：隐私、设备、定位等
)


object PermissionConfig {

    private val map = mapOf(
        android.Manifest.permission.CAMERA to PermissionInfo(
            name = "相机",
            desc = "用于拍摄照片或视频",
            category = "隐私权限"
        ),
        android.Manifest.permission.READ_CONTACTS to PermissionInfo(
            name = "读取联系人",
            desc = "用于读取您的联系人信息",
            category = "隐私权限"
        ),
        android.Manifest.permission.ACCESS_FINE_LOCATION to PermissionInfo(
            name = "精准定位",
            desc = "用于根据 GPS 精确定位您的位置",
            category = "定位权限"
        ),
        android.Manifest.permission.ACCESS_COARSE_LOCATION to PermissionInfo(
            name = "粗略定位",
            desc = "用于根据网络大致定位您的位置",
            category = "定位权限"
        ),
        android.Manifest.permission.RECORD_AUDIO to PermissionInfo(
            name = "麦克风",
            desc = "用于录制音频",
            category = "隐私权限"
        ),
    )

    /** 通过权限字符串获取中文配置 */
    fun get(permission: String) = map.getOrElse( permission) { PermissionInfo("", "更好地使用app") }
}
