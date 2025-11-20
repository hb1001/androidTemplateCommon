package com.template.generated

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.template.core.navigation.AppRoutes
import com.template.feature.atrust.navigation.loginWithVpnScreen
import com.template.feature.login.navigation.loginScreen
import com.template.generated.page.AppMainEntryScreen
import com.template.generated.page.PostEditScreen
import kotlin.OptIn

/**
 * 目录结构
 * 1. 部分页面是固定不动的（最多修改几个地方）
 *  MainApplication
 *  MainActivity
 *  component
 *  2. 部分页面是动态生成的
 *  page目录：每一个文件表示一个用户定义的页面。当然用户也可以直接用预设页面，比如login。这些页面不在本模块中，不是动态生成的。
 *  AppNavigation：列出本项目一共有哪几个页面，
 * 3. 其他模块也需要调整
 *  导航相关
 *  data相关
 *
 * 代码生成过程：
 * 1. 本模块作为app模块，在这个基础上修改。一开始，模块已经包含如下文件：MainApplication、MainActivity、component、AndroidManifest.xml
 * 2. 用户指定app名字、包名、主题等。根据信息，修改上面的相关文件。
 * 3. 用户指定了页面数量、数据源。根据信息，修改导航相关模块、data相关模块
 * 4. 根据用户输入的页面信息，动态生成页面文件。
 *
 *
 */
val LocalNavController = staticCompositionLocalOf<NavController> {
    error("No NavController provided")
}

@Composable
public fun AppNavigation() {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(navController = navController, startDestination = AppRoutes.LOGIN_WITH_VPN_ROUTE) {
            loginScreen(
                onLoginSuccess = {
                    navController.navigateToCustom()
                }
            )

            loginWithVpnScreen(onLoginSuccess = {
                navController.navigateToCustom()
            })

            customScreen()
        }
    }

}

public fun NavController.navigateToCustom() {
    this.navigate(AppRoutes.CUSTOM_ROUTER_ROUTE)
}

@OptIn(ExperimentalMaterial3Api::class)
public fun NavGraphBuilder.customScreen(

) {

    composable(route = AppRoutes.CUSTOM_ROUTER_ROUTE) {
        AppMainEntryScreen()
    }
    composable(route = AppRoutes.CUSTOM_POST_DETAIL_ROUTE) {
        PostEditScreen()
    }
}
