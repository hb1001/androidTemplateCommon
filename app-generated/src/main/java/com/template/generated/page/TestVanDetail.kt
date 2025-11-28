package com.template.generated.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.template.core.navigation.LocalNavController
import com.template.core.ui.components.CommonTitleBar
import com.template.core.ui.vant.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestVanDetail(path: String?) {
    // 根据 path 获取标题
    val title = demoCells.find { it.path == path }?.title ?: "组件详情"

    Scaffold(
        topBar = {
            CommonTitleBar(title = title, showBack = true)
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFF7F8FA))
                .then(
                    if (path=="popup") {
                        Modifier
                    } else {
                        Modifier.verticalScroll(rememberScrollState())
                    }
                )
                // 统一在这里处理滚动，Demo 组件内部不要再写 verticalScroll
//                .verticalScroll(rememberScrollState())
//                .padding(bottom = 40.dp)
        ) {
            // 根据 path 路由到具体的 Demo 组件
            when (path) {
                "button" -> VanButtonDemo()
                "cell" -> VanCellGroups()
                "typography" -> VanTypographyDemo()
                "checkbox" -> VanCheckboxDemo()
                "radio" -> VanRadioDemo()
                "input" -> VanInputDemo()
                "search" -> VanSearchDemo()
                "slider" -> VanSliders()
                "switch" -> VanSwitches()
                "swipecell" -> VanSwipeCellDemo()
                "tag" -> VanTags()
                "badge" -> VanBadges()
                "collapse" -> VanCollapses()
                "swipe" -> VanSwipes()
                "image"-> VanImageDemo()
                "icon"-> VanIconDemo()
                "dialog"->VanDialogDemo()
                "popup"->VanPopupDemo()

                else -> {
                    // 默认或未找到
                    VanCell(title = "未找到该组件示例", value = path)
                }
            }
        }
    }
}