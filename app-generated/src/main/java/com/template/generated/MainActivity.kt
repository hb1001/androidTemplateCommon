package com.template.generated

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.template.core.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation()
                }
            }
        }
    }
}
val LocalNavController = staticCompositionLocalOf<NavController> {
    error("No NavController provided")
}
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
