package com.template.feature.home.homelist


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.template.core.navigation.AppRoutes

fun NavController.navigateToPagingAndRefresh() = navigate(AppRoutes.PAGING_AND_REFRESH_ROUTE)
fun NavController.navigateToPagingOnly() = navigate(AppRoutes.PAGING_ONLY_ROUTE)
fun NavController.navigateToPullRefreshOnly() = navigate(AppRoutes.PULL_REFRESH_ONLY_ROUTE)

fun NavGraphBuilder.pagingListScreens(navController: NavController) {
    composable(route = AppRoutes.PAGING_AND_REFRESH_ROUTE) {
        PagingAndRefreshScreen()
    }
    composable(route = AppRoutes.PAGING_ONLY_ROUTE) {
        PagingOnlyScreen()
    }
    composable(route = AppRoutes.PULL_REFRESH_ONLY_ROUTE) {
        // 你可能需要一个方式来回到主页
        PullRefreshOnlyScreen()
    }
}