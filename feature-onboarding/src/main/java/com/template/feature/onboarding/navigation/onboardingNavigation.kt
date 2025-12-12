package com.template.feature.onboarding.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.template.core.navigation.AppRoutes
import com.template.feature.onboarding.components.GuideDemoScreen
import com.template.feature.onboarding.components.PreviewOnboarding

fun NavGraphBuilder.onboardingScreen(onFinish: () -> Unit) {
    composable(route = AppRoutes.GUIDE_DEMO_ROUTE) {
        GuideDemoScreen(onFinish)
    }
    composable(route = AppRoutes.ONBOARDING_ROUTE) {
        PreviewOnboarding(onFinish)
    }
}

