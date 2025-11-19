package com.template.feature.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.template.core.navigation.AppRoutes
import com.template.feature.login.LoginScreen

fun NavController.navigateToLogin() {
    this.navigate(AppRoutes.LOGIN_ROUTE) {
        // 清除回退栈，使登录后无法返回
        popUpTo(0)
    }
}

fun NavGraphBuilder.loginScreen(onLoginSuccess: () -> Unit) {
    composable(route = AppRoutes.LOGIN_ROUTE) {
        LoginScreen(onLoginSuccess = onLoginSuccess)
    }
}