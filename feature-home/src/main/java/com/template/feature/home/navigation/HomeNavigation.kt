package com.template.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.template.core.navigation.AppRoutes
import com.template.feature.home.HomeScreen

fun NavController.navigateToHome() {
    this.navigate(AppRoutes.HOME_ROUTE)
}

fun NavGraphBuilder.homeScreen(
    onNavigateToPagingAndRefresh: () -> Unit,
    onNavigateToPagingOnly: () -> Unit,
    onNavigateToPullRefreshOnly: () -> Unit,
) {
    composable(route = AppRoutes.HOME_ROUTE) {
        HomeScreen(
            onNavigateToPagingAndRefresh = onNavigateToPagingAndRefresh,
            onNavigateToPagingOnly = onNavigateToPagingOnly,
            onNavigateToPullRefreshOnly = onNavigateToPullRefreshOnly
        )
    }
}