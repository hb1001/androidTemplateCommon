package com.template.feature.map.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.template.core.navigation.AppRoutes
import com.template.feature.map.MapScreen

// 实际上map一般不作为单独页面，而是tab标签页。所以下面的两个函数不会被调用
fun NavController.navigateToMap() {
    this.navigate(AppRoutes.LOGIN_ROUTE) {
    }
}

fun NavGraphBuilder.mapScreen() {
    composable(route = AppRoutes.LOGIN_ROUTE) {
        MapScreen()
    }
}