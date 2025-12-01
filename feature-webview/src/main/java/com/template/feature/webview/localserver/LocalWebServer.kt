package com.template.feature.webview.localserver

import android.content.Context
import android.util.Log
import android.webkit.MimeTypeMap
import fi.iki.elonen.NanoHTTPD

class LocalWebServer(
    private val context: Context,
    port: Int = 8080
) : NanoHTTPD(port) {

    companion object {
        private const val TAG = "LocalWebServer"
    }

    override fun serve(session: IHTTPSession): Response {
        var uri = session.uri

        // 1. 如果请求根路径 "/"，指向 index.html
        if (uri == "/" || uri.isEmpty()) {
            uri = "/index.html"
        }

        return try {
            // 2. 尝试从 Assets 打开对应文件
            // uri 比如是 "/assets/index.js"，assets.open 需要 "assets/index.js" (去掉开头的/)
            val assetPath = uri.substring(1)

            val mimeType = getMimeType(assetPath)
            val inputStream = context.assets.open(assetPath)

            // 找到文件，返回 200 OK
            newChunkedResponse(Response.Status.OK, mimeType, inputStream)
        } catch (e: Exception) {
            // 3. 【关键 SPA 支持】如果文件找不到（比如路由路径 /user/profile），
            // 统统返回 index.html，让前端 JS 去处理路由。
            try {
                Log.d(TAG, "File not found: $uri, falling back to index.html (SPA Mode)")
                val indexStream = context.assets.open("index.html")
                newChunkedResponse(Response.Status.OK, "text/html", indexStream)
            } catch (e2: Exception) {
                Log.e(TAG, "Fatal: index.html not found in assets!", e2)
                newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT, "404 - Index Not Found")
            }
        }
    }

    private fun getMimeType(url: String): String {
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        var mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

        // 兜底补充一些常见类型
        if (mimeType == null) {
            mimeType = when {
                url.endsWith(".js") -> "application/javascript"
                url.endsWith(".css") -> "text/css"
                url.endsWith(".html") -> "text/html"
                url.endsWith(".json") -> "application/json"
                url.endsWith(".svg") -> "image/svg+xml"
                else -> "application/octet-stream"
            }
        }
        return mimeType
    }
}