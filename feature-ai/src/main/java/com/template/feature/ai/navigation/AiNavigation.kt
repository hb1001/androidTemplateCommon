package com.template.feature.ai.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.template.core.navigation.AppRoutes
import com.template.feature.ai.AiScreen


fun NavGraphBuilder.aiScreen() {
    composable(route = AppRoutes.LOGIN_ROUTE) {
        AiScreen()
    }
}