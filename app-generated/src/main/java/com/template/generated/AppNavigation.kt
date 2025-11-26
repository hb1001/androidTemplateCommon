package com.template.generated

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.template.core.navigation.AppRoutes
import com.template.core.navigation.LocalNavController
import com.template.feature.atrust.navigation.loginWithVpnScreen
import com.template.feature.login.navigation.loginScreen
import com.template.feature.setting.navigation.settingMapScreen
import com.template.feature.setting.navigation.settingScreen
import com.template.feature.webview.navigation.webviewScreen
import com.template.generated.page.AppMainEntryScreen
import com.template.generated.page.PostEditScreen
import kotlin.OptIn


@Composable
public fun AppNavigation() {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController = navController,
            startDestination = AppRoutes.LOGIN_WITH_VPN_ROUTE,

            // 直接禁止入场动画
            enterTransition = { EnterTransition.None },
            // 直接禁止出场动画
            exitTransition = { ExitTransition.None },
            // 同样禁止 Pop 动画
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }

        ) {
            loginScreen(
                onLoginSuccess = {
                    navController.navigateToCustom()
                }
            )

            loginWithVpnScreen(onLoginSuccess = {
                navController.navigateToCustom()
            })

            customScreen()

            webviewScreen(navController)
            settingScreen(navController)

            settingMapScreen(navController)
        }
    }

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
}
