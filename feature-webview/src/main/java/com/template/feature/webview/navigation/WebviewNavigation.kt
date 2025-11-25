package com.template.feature.webview.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.template.core.navigation.AppRoutes
import com.template.feature.webview.WebViewScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun NavController.navigateToWebview52() {
    val url = "https://www.52pojie.cn/"
    val encoded = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
    this.navigate("webview/${encoded}") {
        // 清除回退栈，使登录后无法返回
        popUpTo(0)
    }
}

fun NavGraphBuilder.webviewScreen(navController: NavController) {
    composable(AppRoutes.WEBVIEW_ROUTE) { backStack ->
        val encodedUrl = backStack.arguments?.getString("url")
        val realUrl = URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())

        WebViewScreen(
            url = realUrl,
            onExitPage = { navController.popBackStack() }
        )
    }
}