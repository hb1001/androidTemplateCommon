package com.template.feature.webview.navigation

import android.app.Activity
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.template.core.navigation.AppRoutes
import com.template.feature.webview.local.LocalWebViewScreen
import com.template.feature.webview.localserver.ServerWebViewScreen
import com.template.feature.webview.server.WebViewScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun NavController.navigateToWebview52() {
    val url = "https://www.52pojie.cn/"
    val encoded = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
    this.navigate("webview/${encoded}") {}
}

fun NavController.navigateToWebviewLocal() {
// 2. 调用跳转
    navigate("local_webview")
}

fun NavController.navigateBack() {
// 2. 调用跳转
    if (!popBackStack()) {
        (context as? Activity)?.finish()
    }
}
fun NavGraphBuilder.webviewScreen(navController: NavController) {
    composable(AppRoutes.WEBVIEW_ROUTE) { backStack ->
        val encodedUrl = backStack.arguments?.getString("url")?.also {
            URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
        } ?: "https://www.52pojie.cn/"
//        val realUrl = URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())

        WebViewScreen(
            url = encodedUrl,
            onExitPage = {
                navController.navigateBack()
            }
        )
    }
    // 1. 定义路由
    composable("local_webview") {
        LocalWebViewScreen(
            onExitPage = {
//                navController.popBackStack()
                navController.navigateBack()
            }

        )
    }

    composable("server_webview") {
        ServerWebViewScreen(
            onExitPage = {
                navController.navigateBack()
            }
        )
    }

    // 定义一个 dialog 路由，而不是 composable 路由
    dialog("confirm_exit") {
        // 这里的背景默认就是透明黑遮罩
//        LogoutDialog(
//            onConfirm = { navController.popBackStack() },
//            onDismiss = { navController.popBackStack() }
//        )
    }
}

