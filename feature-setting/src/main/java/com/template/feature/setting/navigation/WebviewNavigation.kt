package com.template.feature.setting.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.template.core.navigation.AppRoutes
import com.template.feature.setting.ProfilePage
import com.template.feature.setting.components.SettingSingleItemPage

fun NavController.navigateToSettingSingle(
    title: String,
    initialValue: String
) {
    this.navigate(AppRoutes.buildNavigateRoute(
        AppRoutes.SETTING_ONE_ITEM,
        title,
        initialValue
    ))
}


fun NavGraphBuilder.settingScreen(
    navController: NavController
) {
    composable(AppRoutes.SETTING_ONE_ITEM_ROUTE) { backStack ->

        val title = backStack.arguments?.getString("title") ?: ""
        val initialValue = backStack.arguments?.getString("value") ?: ""

        SettingSingleItemPage(
            title = title,
            initialValue = initialValue,
            hint = "请输入内容",
            onConfirm = { newValue ->

                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("settingResult", newValue)

                navController.popBackStack()
            }
        )
    }

    // 暂时实际没用。因为是作为tab页
//    composable(AppRoutes.SETTING_LIST_ROUTE) { backStack ->
//        ProfilePage(
//            onClickProfileInfo,
//            onClickLogout
//        )
//    }
}
