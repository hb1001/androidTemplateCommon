package com.template.data.network.update

import android.app.Application
import android.content.Context
import com.xuexiang.xupdate.XUpdate
import com.xuexiang.xupdate.proxy.IUpdateHttpService
import com.xuexiang.xupdate.proxy.IUpdateHttpService.Callback
import com.xuexiang.xupdate.proxy.IUpdateHttpService.DownloadCallback
import com.xuexiang.xupdate.utils.UpdateUtils
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.timeout
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.prepareGet
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
//import io.ktor.client.statement.isSuccess
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentLength
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
//import io.ktor.serialization.gson.gson
import io.ktor.utils.io.core.isEmpty
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.readRemaining
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import kotlin.text.get

fun initXUpdate(context: Application, ) {
    XUpdate.get()
        .debug(true) // 正式发布时关闭
        .isWifiOnly(true) // 默认只在 WiFi 下下载
        .isGet(true) // 使用 GET 请求
        .isAutoMode(false) // 非自动模式，需手动调用
        .param("version", UpdateUtils.getVersionName(context)) // 可传递本地版本号
        .param("appKey", context.packageName) // 可传递 appKey
        .setOnUpdateFailureListener({ error ->
            // 设置版本更新出错的监听
            error.printStackTrace()
        })
        .supportSilentInstall(true) // 支持静默安装，需设备拥有 root 权限
        .setIUpdateHttpService(KtorUpdateHttpService()) // 使用 OkHttp 进行网络请求
        .init(context) // 初始化
}
/**
 * 使用 Ktor + Coroutines 实现的更新请求服务
 */
class KtorUpdateHttpService(private val isPostJson: Boolean = false) : IUpdateHttpService {
    // 配置 Json 解析器
    private val jsonConfig = Json {
        prettyPrint = false
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }

    // Ktor HttpClient 实例
    private val client = HttpClient(CIO) {
        // 配置 Gson 序列化 (对应原代码中的 Gson)
        install(ContentNegotiation) {
            // 使用 Kotlinx Serialization
            json(jsonConfig)
        }
        // 可以根据需要添加 HttpTimeout 或 Logging 插件
    }

    // 用于管理协程的 Scope，使用 IO 调度器
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // 用于存储下载任务的 Job，以便实现取消功能
    private val downloadJobs = ConcurrentHashMap<String, Job>()

    override fun asyncGet(url: String, params: Map<String, Any>, callBack: Callback) {
        scope.launch {
            try {
                val response = client.get(url) {
                    // 添加查询参数
                    params.forEach { (key, value) ->
                        parameter(key, value)
                    }
                }

                if (response.status.isSuccess()) {
                    val result = response.bodyAsText()
                    // 回调通常需要在主线程或是调用者不关心的线程，XUpdate 内部通常会处理线程切换，
                    // 但为了保险，保持在 IO 线程回调即可，或者根据 XUpdate 文档切换到 Main
                    withContext(Dispatchers.Main) {
                        callBack.onSuccess(result)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        callBack.onError(IOException("Unexpected code ${response.status}"))
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callBack.onError(e)
                }
            }
        }
    }

    override fun asyncPost(url: String, params: Map<String, Any>, callBack: Callback) {
        scope.launch {
            try {
                val response = client.post(url) {
                    if (isPostJson) {
                        // JSON 形式提交
                        contentType(ContentType.Application.Json)
                        setBody(params) // ContentNegotiation 会自动将 Map 转为 JSON
                    } else {
                        // 表单形式提交
                        val formData = Parameters.build {
                            params.forEach { (key, value) ->
                                append(key, value.toString())
                            }
                        }
                        setBody(FormDataContent(formData))
                    }
                }

                if (response.status.isSuccess()) {
                    withContext(Dispatchers.Main) {
                        callBack.onSuccess(response.bodyAsText())
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        callBack.onError(IOException("Unexpected code ${response.status}"))
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callBack.onError(e)
                }
            }
        }
    }

    override fun download(url: String, path: String, fileName: String, callback: DownloadCallback) {
        // onStart 通常在 UI 线程调用，但为了保险
        scope.launch(Dispatchers.Main) { callback.onStart() }

        val job = scope.launch(Dispatchers.IO) {
            try {
                client.prepareGet(url){
                    timeout {
                        // 关键：将请求超时设置为无限，防止大文件下载中途断开
                        requestTimeoutMillis = 1000*60*200
                        // socketTimeout 指的是两次数据包之间的时间间隔，
                        // 如果网断了超过 60秒没有数据传输，则报错，这是合理的。
                        socketTimeoutMillis = 60000
                    }
                }.execute { httpResponse ->
                    if (!httpResponse.status.isSuccess()) {
                        throw IOException("Unexpected code ${httpResponse.status}")
                    }

                    val channel = httpResponse.bodyAsChannel()
                    val totalLength = httpResponse.contentLength() ?: -1L

                    val directory = File(path)
                    if (!directory.exists()) directory.mkdirs()
                    val file = File(directory, fileName)

                    var currentLength = 0L
                    var lastRefreshTime = 0L

                    FileOutputStream(file).use { output ->
                        while (!channel.isClosedForRead) {
                            val packet = channel.readRemaining(8192)
                            while (!packet.isEmpty) {
                                val bytes = packet.readBytes()
                                output.write(bytes)
                                currentLength += bytes.size

                                if (totalLength > 0) {
                                    val currentTime = System.currentTimeMillis()
                                    // 限制回调频率，避免 UI 卡顿 (例如每 100ms 更新一次进度)
                                    if (currentTime - lastRefreshTime > 100 || currentLength == totalLength) {
                                        val progress = currentLength.toFloat() / totalLength
                                        // 必须在主线程更新进度条
                                        withContext(Dispatchers.Main) {
                                            callback.onProgress(progress, totalLength)
                                        }
                                        lastRefreshTime = currentTime
                                    }
                                }
                            }
                        }
                    }
                    // 下载完成，主线程回调
                    withContext(Dispatchers.Main) {
                        callback.onSuccess(file)
                    }
                }
            } catch (e: Exception) {
                if (e !is kotlinx.coroutines.CancellationException) {
                    withContext(Dispatchers.Main) {
                        callback.onError(e)
                    }
                }
            } finally {
                downloadJobs.remove(url)
            }
        }
        downloadJobs[url] = job
    }
    override fun cancelDownload(url: String) {
        val job = downloadJobs[url]
        if (job != null && job.isActive) {
            job.cancel() // 取消协程
            downloadJobs.remove(url)
        }
    }

    // 建议在应用退出或组件销毁时调用此方法清理资源
    fun close() {
        client.close()
        scope.cancel()
    }
}