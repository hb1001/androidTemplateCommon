package com.template.feature.webview.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.template.core.navigation.AppRoutes
import com.template.feature.webview.WebViewScreen

fun NavController.navigateToWebview52() {
    this.navigate("webview/https://www.52pojie.cn/") {
        // 清除回退栈，使登录后无法返回
        popUpTo(0)
    }
}

fun NavGraphBuilder.webviewScreen(navController: NavController) {
    composable(AppRoutes.WEBVIEW_ROUTE) { backStack ->
        val url = backStack.arguments?.getString("url") ?: "https://www.baidu.com"

        WebViewScreen(
            url = url,
            onExitPage = { navController.popBackStack() }
        )
    }
}