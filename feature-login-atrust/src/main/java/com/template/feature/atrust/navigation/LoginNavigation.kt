package com.template.feature.atrust.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.template.core.navigation.AppRoutes
import com.template.feature.atrust.LoginWithVpnScreen
fun NavController.navigateToLoginWithVpn() {
    this.navigate(AppRoutes.LOGIN_WITH_VPN_ROUTE) {
        // 清除回退栈，使登录后无法返回
        popUpTo(0)
    }
}

fun NavGraphBuilder.loginWithVpnScreen(onLoginSuccess: () -> Unit) {
    composable(route = AppRoutes.LOGIN_WITH_VPN_ROUTE) {
        LoginWithVpnScreen(onLoginSuccess = onLoginSuccess)
    }
}