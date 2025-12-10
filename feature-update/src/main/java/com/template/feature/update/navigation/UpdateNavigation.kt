package com.template.feature.map.update

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.template.core.navigation.AppRoutes
import com.template.feature.update.UpdateScreen

fun NavGraphBuilder.updateScreen() {
    composable(route = AppRoutes.AI_TEST_ROUTE) {
        UpdateScreen()
    }
}