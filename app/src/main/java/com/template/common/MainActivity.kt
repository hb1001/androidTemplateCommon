package com.template.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.template.core.ui.theme.AppTheme
import com.template.feature.home.homelist.navigateToPagingAndRefresh
import com.template.feature.home.homelist.navigateToPagingOnly
import com.template.feature.home.homelist.navigateToPullRefreshOnly
import com.template.feature.home.homelist.pagingListScreens
import com.template.feature.home.navigation.homeScreen
import com.template.feature.home.navigation.navigateToHome
import com.template.feature.login.navigation.loginScreen
import com.template.feature.login.navigation.navigateToLogin
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // 安装启动屏，并设置条件，直到我们确定起始路由前，启动屏不消失
        installSplashScreen().setKeepOnScreenCondition {
            viewModel.uiState.value == MainUiState.Loading
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (uiState) {
                        is MainUiState.Loading -> {
                            // 在确定路由前，显示一个加载指示器 (通常会被启动屏覆盖)
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                CircularProgressIndicator()
                            }
                        }
                        is MainUiState.Success -> {
                            val startRoute = (uiState as MainUiState.Success).startRoute
                            AppNavigation(startRoute = startRoute)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavigation(startRoute: String) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startRoute
    ) {
        // 注册登录页
        loginScreen(
            onLoginSuccess = {
                // 登录成功后，导航到首页
                navController.navigateToHome()
            }
        )

        // 注册首页
        homeScreen(
            onNavigateToPagingAndRefresh = { navController.navigateToPagingAndRefresh() },
            onNavigateToPagingOnly = { navController.navigateToPagingOnly() },
            onNavigateToPullRefreshOnly = { navController.navigateToPullRefreshOnly() }
        )

        pagingListScreens(navController)

        // 当有调试页面时，可以这样添加
        // debugScreen()
    }
}