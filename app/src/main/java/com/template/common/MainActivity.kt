package com.template.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.template.core.navigation.AppRoutes
import com.template.core.ui.theme.AppTheme
import com.template.feature.home.navigation.homeScreen // <-- 导入 feature-home 的导航扩展
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = AppRoutes.HOME_ROUTE // <-- 设置首页为启动页
                    ) {
                        // 在这里组装所有 feature 模块的导航
                        homeScreen() // <-- 调用 home 模块的导航函数

                        // 当有其他 feature 模块时，像这样继续添加：
                        // loginScreen()
                        // debugScreen()
                    }
                }
            }
        }
    }
}