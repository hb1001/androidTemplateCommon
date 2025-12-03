package com.template.generated

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.template.core.navigation.AppRoutes
import com.template.core.navigation.LocalNavController
import com.template.feature.ai.navigation.aiScreen
import com.template.feature.atrust.navigation.loginWithVpnScreen
import com.template.feature.login.navigation.loginScreen
import com.template.feature.setting.navigation.settingMapScreen
import com.template.feature.setting.navigation.settingScreen
import com.template.feature.webview.navigation.webviewScreen
import com.template.generated.page.AppMainEntryScreen
import com.template.generated.page.PostEditScreen
import com.template.generated.page.TestListScreen
import com.template.generated.page.TestVanDetail
import kotlin.OptIn


@Composable
public fun AppNavigation() {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController = navController,
//            startDestination = AppRoutes.WEBVIEW_LOCAL, // 启动页面
            startDestination = AppRoutes.AI_TEST_ROUTE, // 启动页面

            // 直接禁止入场动画
            enterTransition = { EnterTransition.None },
            // 直接禁止出场动画
            exitTransition = { ExitTransition.None },
            // 同样禁止 Pop 动画
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }

        ) {
            existPage(navController)
            customScreen()
        }
    }

}
fun NavGraphBuilder.existPage(navController: NavController){
    loginScreen(
        onLoginSuccess = {
            navController.navigateToCustom()
        }
    )

    loginWithVpnScreen(onLoginSuccess = {
        navController.navigateToCustom()
    })


    webviewScreen(navController)
    settingScreen(navController)

    settingMapScreen(navController)
    aiScreen()
}

public fun NavController.navigateToCustom() {
    this.navigate(AppRoutes.CUSTOM_ROUTER_ROUTE) {
        popUpTo(0)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
public fun NavGraphBuilder.customScreen() {
    composable(route = AppRoutes.CUSTOM_ROUTER_ROUTE) {
        AppMainEntryScreen()
    }
    composable(route = AppRoutes.CUSTOM_POST_DETAIL_ROUTE) {
        PostEditScreen()
    }

    // 1. 列表页
    composable(route = AppRoutes.CUSTOM_TEST_VANT_ROUTE) {
        TestListScreen()
    }

    // 2. 详情页 (接收 path 参数)
    composable(
        route = AppRoutes.CUSTOM_TEST_VANT_DETAIL_ROUTE,
        arguments = listOf(navArgument("path") { type = NavType.StringType })
    ) { backStackEntry ->
        // 获取参数
        val path = backStackEntry.arguments?.getString("path")
        TestVanDetail(path = path)
    }
}
fun NavController.navigateToTestDetail(path: String) {
    // 替换路由中的 {path} 占位符
    val route = AppRoutes.CUSTOM_TEST_VANT_DETAIL_ROUTE.replace("{path}", path)
    this.navigate(route)
}
