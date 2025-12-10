package com.template.data.network.update

import android.app.Activity
import android.content.Context
import com.xuexiang.xupdate.XUpdate
import com.xuexiang.xupdate.entity.UpdateEntity
import com.xuexiang.xupdate.listener.IUpdateParseCallback
import com.xuexiang.xupdate.proxy.IUpdateParser
import com.xuexiang.xupdate.utils.UpdateUtils
import org.json.JSONObject
import kotlin.math.max

/**
 * XUpdate App更新检查管理类
 * 封装了使用 XUpdate 检查更新的逻辑
 */
object XUpdateManager {
    // 版本检查API地址
    private const val VERSION_CHECK_URL = "https://admin.yunpibao.com/api/apk/get_version"

    // 新版APK下载地址 (从你的旧代码中获取，最好也由API返回)
    private const val APK_DOWNLOAD_URL = "https://admin.yunpibao.com/yunpibao.apk"

    /**
     * 公开的入口方法：执行检查更新
     *
     * @param activity 用于显示更新弹窗的 Activity
     */
    fun checkUpdate(activity: Activity) {
        XUpdate.newBuild(activity)
            .updateUrl(VERSION_CHECK_URL)
            .updateParser(CustomUpdateParser()) // 使用自定义的 JSON 解析器
            .build()
            .update()
    }

    /**
     * 比较版本号，判断是否需要更新
     */
    /**
     * 【修改点 3】: 将 isUpdateNeeded 方法改造为 public static
     * 这样它就可以在任何地方被调用，包括静态内部类 CustomUpdateParser
     *
     * @param context       上下文，用于获取当前版本号
     * @param latestVersion 从服务器获取的最新版本号
     * @return 是否需要更新
     */
    fun isUpdateNeeded(context: Context?, latestVersion: String?): Boolean {
        // 使用 XUpdate 提供的工具类获取当前版本号
        val currentVersion: String? = UpdateUtils.getVersionName(context)

        // 如果版本号信息为空，则不进行更新
        if (currentVersion == null || latestVersion == null) {
            return false
        }

        val currentParts: Array<String?> =
            currentVersion.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val latestParts: Array<String?> =
            latestVersion.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val length = max(currentParts.size, latestParts.size)
        for (i in 0..<length) {
            // 注意处理版本号位数不一致的情况，例如 2.1 vs 2.1.3
            val currentPart = if (i < currentParts.size) currentParts[i]!!.toInt() else 0
            val latestPart = if (i < latestParts.size) latestParts[i]!!.toInt() else 0
            if (latestPart > currentPart) {
                return true // 最新版本号的某一位大于当前版本号
            }
            if (latestPart < currentPart) {
                return false // 最新版本号的某一位小于当前版本号
            }
        }
        // 所有版本号位都相同
        return false
    }

    /**
     * 自定义版本更新解析器，用于解析你提供的 API 响应格式
     */
    class CustomUpdateParser : IUpdateParser {
        @Throws(Exception::class)
        public override fun parseJson(json: String): UpdateEntity? {
            val jsonObject = JSONObject(json)
            if (jsonObject.optInt("code") == 200) {
                val data = jsonObject.getJSONObject("data")
                val versionName = data.optString("version")

                val updateEntity: UpdateEntity = UpdateEntity()
                // 设置是否有新版本
                updateEntity.setHasUpdate(true) // 只要 code 是 200，就认为有更新信息

                val needUpdate = isUpdateNeeded(XUpdate.getContext(), versionName)

                // 【修改点 2】: 根据比较结果来设置是否有更新
                updateEntity.setHasUpdate(needUpdate)


                // 设置新版版本号
                updateEntity.setVersionName(versionName)
                // 设置下载地址
                updateEntity.setDownloadUrl(APK_DOWNLOAD_URL) // 【重要】你的API没有返回下载地址，我们暂时硬编码
                // 设置更新内容
                updateEntity.setUpdateContent("发现新版本 v" + versionName + "，建议立即升级！") // 【重要】你的API没有更新内容，我们自定义一个

                // 【可选】设置其他属性
                // updateEntity.setIsForce(false); // 是否强制更新
                // updateEntity.setSize(1024 * 1024 * 20); // APK 大小，不设置的话，下载进度条将不显示进度
                // updateEntity.setMd5("xxxxxxxxxxxxxxxx"); // APK 的 MD5，用于校验
                return updateEntity
            }
            // 如果 code 不为 200 或解析失败，返回 null，XUpdate 会认为没有更新
            return null
        }

        @Throws(Exception::class)
        public override fun parseJson(json: String, callback: IUpdateParseCallback) {
            callback.onParseResult(this.parseJson(json))
        }

        override fun isAsyncParser(): Boolean {
            return false
        }
    }
}