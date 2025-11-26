package com.template.feature.setting.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.template.core.navigation.AppRoutes
import com.template.data.datastore.UserSettings
import com.template.feature.setting.MapSettingScreen
import com.template.feature.setting.ProfilePage
import com.template.feature.setting.SettingsViewModel
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
fun NavController.navigateToSettingMap(
) {
    this.navigate(AppRoutes.SETTING_MAP_ROUTER)
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
                    ?.set(title, newValue)
                navController.popBackStack()
            }
        )
    }
}
//fun NavGraphBuilder.settingMapScreen(
//    userSetting: UserSettings,
//    setUserSetting: (UserSettings) -> Unit,
//) {
//
//    composable(AppRoutes.SETTING_MAP_ROUTER) {
//        MapSettingScreen(userSetting, setUserSetting)
//    }
//
//}

fun NavGraphBuilder.settingMapScreen(

    navController: NavController,
) {

    composable(AppRoutes.SETTING_MAP_ROUTER) { backStackEntry ->

        val viewModel: SettingsViewModel = hiltViewModel(backStackEntry)

        val settings by viewModel.userSettings.collectAsState()

        if(settings == null){
            Box(modifier = Modifier.fillMaxSize())
        }else{
            MapSettingScreen(
                navController = navController,
                userSetting = settings!!,
                setUserSetting = { viewModel.updateSettings(it) }
            )
        }
    }
}
