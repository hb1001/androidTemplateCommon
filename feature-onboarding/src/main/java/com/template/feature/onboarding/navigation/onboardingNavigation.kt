package com.template.feature.onboarding.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.template.core.navigation.AppRoutes
import com.template.feature.onboarding.OnboardingScreen

fun NavGraphBuilder.onboardingScreen(onFinish: () -> Unit) {
    composable(route = AppRoutes.ONBOARDING_ROUTE) {
        OnboardingScreen{
            onFinish()
        }
    }
}

